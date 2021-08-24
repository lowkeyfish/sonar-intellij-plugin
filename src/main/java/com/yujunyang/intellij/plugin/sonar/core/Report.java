/*
 * Copyright 2021 Yu Junyang
 * https://github.com/lowkeyfish
 *
 * This file is part of Sonar Intellij plugin.
 *
 * Sonar Intellij plugin is free software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation, either version 3 of
 * the License, or (at your option) any later version.
 *
 * Sonar Intellij plugin is distributed in the hope that it will
 * be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Sonar Intellij plugin.
 * If not, see <http://www.gnu.org/licenses/>.
 */

package com.yujunyang.intellij.plugin.sonar.core;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiFile;
import com.yujunyang.intellij.plugin.sonar.api.RulesSearchResponse;
import com.yujunyang.intellij.plugin.sonar.api.SonarApiImpl;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.common.exceptions.ApiRequestFailedException;
import com.yujunyang.intellij.plugin.sonar.config.WorkspaceSettings;
import com.yujunyang.intellij.plugin.sonar.messages.MessageBusManager;
import org.jetbrains.annotations.NotNull;
import org.sonar.core.util.CloseableIterator;
import org.sonar.scanner.protocol.output.ScannerReport;
import org.sonar.scanner.protocol.output.ScannerReportReader;
import org.sonarsource.scanner.api.LogOutput;

public class Report {
    private Project project;
    private File reportDir;
    private int bugCount;
    private int codeSmellCount;
    private int vulnerabilityCount;
    private int duplicatedBlocksCount;
    private int securityHotSpotCount;
    private ConcurrentMap<PsiFile, List<AbstractIssue>> issues;

    public Report(@NotNull Project project, @NotNull File reportDir) {
        this.project = project;
        this.reportDir = reportDir;
        issues = new ConcurrentHashMap<>();
        analyze();
    }

    public int getBugCount() {
        return bugCount;
    }

    public int getCodeSmellCount() {
        return codeSmellCount;
    }

    public int getVulnerabilityCount() {
        return vulnerabilityCount;
    }

    public int getDuplicatedBlocksCount() {
        return duplicatedBlocksCount;
    }

    public int getSecurityHotSpotCount() {
        return securityHotSpotCount;
    }

    public ConcurrentMap<PsiFile, List<AbstractIssue>> getIssues() {
        return issues;
    }

    private void analyze() {
        List<RulesSearchResponse.Rule> rules = getRules();
        List<Integer> componentFileNumbers = getAllComponentFileNumbers();
        ScannerReportReader reader = new ScannerReportReader(reportDir);
        for (Integer componentFileNumber : componentFileNumbers) {
            ScannerReport.Component component = reader.readComponent(componentFileNumber);
            CloseableIterator<ScannerReport.Issue> reportIssues = reader.readComponentIssues(componentFileNumber);
            CloseableIterator<ScannerReport.Duplication> reportDuplications = reader.readComponentDuplications(componentFileNumber);

            String projectRelativePath;
            File file;
            PsiFile psiFile;

            if (reportIssues.hasNext() || reportDuplications.hasNext()) {
                projectRelativePath = component.getProjectRelativePath();
                file = Paths.get(project.getBasePath(), projectRelativePath).toFile();
                psiFile = IdeaUtils.getPsiFile(project, file);

                if (!issues.containsKey(psiFile)) {
                    issues.put(psiFile, new ArrayList<>());
                }
            } else {
                continue;
            }

            while (reportIssues.hasNext()) {
                ScannerReport.Issue reportIssue = reportIssues.next();
                String issueRuleKey = String.format("%s:%s", reportIssue.getRuleRepository(), reportIssue.getRuleKey());
                RulesSearchResponse.Rule rule = findRule(rules, issueRuleKey);

                boolean ignoreIssue = false;
                switch (rule.getType()) {
                    case "BUG":
                        bugCount++;
                        break;
                    case "VULNERABILITY":
                        vulnerabilityCount++;
                        break;
                    case "CODE_SMELL":
                        codeSmellCount++;
                        break;
                    case "SECURITY_HOTSPOT":
                        securityHotSpotCount++;
                        break;
                    default:
                        ignoreIssue = true;
                        break;
                }

                if (ignoreIssue) {
                    MessageBusManager.publishLogToEDT(project, String.format("Rule[%s] type[%s] 暂未被报告解析程序支持, 展示的报告中将忽略此类型相关问题, 在后续插件更新中可能会增加支持", issueRuleKey, rule.getType()), LogOutput.Level.ERROR);
                    continue;
                }

                // 构造函数不再使用TextRange传offset，因为可能Sonar提供的TextRange中startOffset会大于endOffset，创建TextRange时将输出一条error提示offset范围异常
                Issue issue = new Issue(
                        psiFile,
                        reportIssue.getRuleRepository(),
                        reportIssue.getRuleKey(),
                        reportIssue.getMsg(),
                        reportIssue.getSeverity().toString(),
                        reportIssue.getTextRange().getStartLine(),
                        reportIssue.getTextRange().getEndLine(),
                        reportIssue.getTextRange().getStartOffset(),
                        reportIssue.getTextRange().getEndOffset(),
                        rule.getType(),
                        rule.getName(),
                        rule.getHtmlDesc());

                issues.get(psiFile).add(issue);

            }

            while (reportDuplications.hasNext()) {
                String issueRuleKey = String.format("%s:%s", "common-java", "DuplicatedBlocks");
                RulesSearchResponse.Rule rule = findRule(rules, issueRuleKey);

                boolean currentFileNotExistDuplicatedBlocksIssue = issues.get(psiFile).stream().filter(n -> n.ruleKey.equalsIgnoreCase("DuplicatedBlocks")).findFirst().orElse(null) == null;
                if (currentFileNotExistDuplicatedBlocksIssue) {
                    codeSmellCount++;
                }

                ScannerReport.Duplication duplication = reportDuplications.next();
                DuplicatedBlocksIssue issue = new DuplicatedBlocksIssue(
                        psiFile,
                        "common-java",
                        "DuplicatedBlocks",
                        rule.getSeverity(),
                        rule.getType(),
                        rule.getName(),
                        rule.getHtmlDesc(),
                        duplication.getOriginPosition().getStartLine(),
                        duplication.getOriginPosition().getEndLine()
                );
                issues.get(psiFile).add(issue);
                duplicatedBlocksCount++;

                boolean existDuplicateInSameFile = false;
                for (ScannerReport.Duplicate d : duplication.getDuplicateList()) {
                    DuplicatedBlocksIssue.Duplicate duplicate = new DuplicatedBlocksIssue.Duplicate(
                            d.getOtherFileRef() == 0 ? "" : reader.readComponent(d.getOtherFileRef()).getProjectRelativePath(),
                            d.getRange().getStartLine(),
                            d.getRange().getEndLine()
                    );
                    issue.addDuplicate(duplicate);
                    boolean duplicateInSameFile = d.getOtherFileRef() == 0;
                    if (duplicateInSameFile) {
                        existDuplicateInSameFile = true;
                    }
                }

                if (existDuplicateInSameFile) {
                    DuplicatedBlocksIssue.Duplicate duplicateUseCurrentBlock = new DuplicatedBlocksIssue.Duplicate(
                            "",
                            issue.getLineStart(),
                            issue.getLineEnd()
                    );
                    issue.getDuplicates().forEach(d -> {
                        if (StringUtil.isEmpty(d.getPath())) {
                            DuplicatedBlocksIssue additionalIssue = new DuplicatedBlocksIssue(
                                    psiFile,
                                    "common-java",
                                    "DuplicatedBlocks",
                                    rule.getSeverity(),
                                    rule.getType(),
                                    rule.getName(),
                                    rule.getHtmlDesc(),
                                    d.getStartLine(),
                                    d.getEndLine()
                            );
                            additionalIssue.addDuplicate(duplicateUseCurrentBlock);
                            List<DuplicatedBlocksIssue.Duplicate> otherDuplicates = issue.getDuplicates().stream()
                                    .filter(n -> !StringUtil.isEmpty(n.getPath()) || n.getStartLine() != d.getStartLine() || n.getEndLine() != d.getEndLine())
                                    .collect(Collectors.toList());
                            additionalIssue.addDuplicates(otherDuplicates);
                            issues.get(psiFile).add(additionalIssue);
                            duplicatedBlocksCount++;
                        }
                    });
                }
            }
        }
    }

    private List<Integer> getAllComponentFileNumbers() {
        List<Integer> componentFileNumbers = new ArrayList<>();
        reportDir.listFiles((dir, name) -> {
            if (name.startsWith("component-")) {
                componentFileNumbers.add(Integer.parseInt(name.split("-")[1].replace(".pb", "")));
                return true;
            }
            return false;
        });
        return componentFileNumbers;
    }

    private List<RulesSearchResponse.Rule> getRules() {
        try {
            List<String> languages = WorkspaceSettings.getInstance().languages;
            List<RulesSearchResponse.Rule> rules = new SonarApiImpl().getRules(languages);
            return rules;
        } catch (ApiRequestFailedException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    private @NotNull RulesSearchResponse.Rule findRule(List<RulesSearchResponse.Rule> rules, String ruleKey) {
        RulesSearchResponse.Rule rule = rules.stream().filter(n -> n.getKey().equalsIgnoreCase(ruleKey)).findFirst().orElse(null);
        if (rule == null) {
            throw new RuntimeException(String.format("No rule named [%s] was found", ruleKey));
        }
        return rule;
    }
}
