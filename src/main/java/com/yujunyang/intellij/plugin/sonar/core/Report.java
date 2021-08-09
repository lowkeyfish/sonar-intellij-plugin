package com.yujunyang.intellij.plugin.sonar.core;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiFile;
import com.yujunyang.intellij.plugin.sonar.api.RulesSearchResponse;
import com.yujunyang.intellij.plugin.sonar.api.SonarApiImpl;
import com.yujunyang.intellij.plugin.sonar.common.IdeaUtils;
import com.yujunyang.intellij.plugin.sonar.common.exceptions.ApiRequestFailedException;
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
    private ConcurrentMap<PsiFile, List<Issue>> issues;

    public Report(@NotNull Project project, @NotNull File reportDir) {
        this.project = project;
        this.reportDir = reportDir;
        issues = new ConcurrentHashMap<>();
        analyze();
    }

    private void analyze() {
        List<RulesSearchResponse.Rule> javaDefaultProfileRules = getJavaDefaultProfileRules();
        List<Integer> componentFileNumbers = getAllComponentFileNumbers();
        ScannerReportReader reader = new ScannerReportReader(reportDir);
        for (Integer componentFileNumber : componentFileNumbers) {
            ScannerReport.Component component = reader.readComponent(componentFileNumber);
            String projectRelativePath = component.getProjectRelativePath();
            File file = Paths.get(project.getBasePath(), projectRelativePath).toFile();
            PsiFile psiFile = IdeaUtils.getPsiFile(project, file);
            CloseableIterator<ScannerReport.Issue> reportIssues = reader.readComponentIssues(componentFileNumber);
            while (reportIssues.hasNext()) {
                ScannerReport.Issue reportIssue = reportIssues.next();
                String issueRuleKey = String.format("%s:%s", reportIssue.getRuleRepository(), reportIssue.getRuleKey());
                RulesSearchResponse.Rule rule = findRule(javaDefaultProfileRules, issueRuleKey);

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
                    default:
                        ignoreIssue = true;
                        break;
                }

                if (ignoreIssue) {
                    MessageBusManager.publishLog(project, String.format("Rule[%s] type[%s] is not supported", issueRuleKey, rule.getType()), LogOutput.Level.ERROR);
                    continue;
                }

                Issue issue = new Issue(
                        psiFile,
                        reportIssue.getRuleRepository(),
                        reportIssue.getRuleKey(),
                        reportIssue.getMsg(),
                        reportIssue.getSeverity().toString(),
                        reportIssue.getTextRange().getStartLine(),
                        reportIssue.getTextRange().getEndLine(),
                        new TextRange(reportIssue.getTextRange().getStartOffset(), reportIssue.getTextRange().getEndOffset()),
                        rule.getType());
                if (!issues.containsKey(psiFile)) {
                    List<Issue> list = new ArrayList<>();
                    list.add(issue);
                    issues.put(psiFile, list);
                } else {
                    issues.get(psiFile).add(issue);
                }
            }

            CloseableIterator<ScannerReport.Duplication> reportDuplications = reader.readComponentDuplications(componentFileNumber);
            while (reportDuplications.hasNext()) {

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

    private List<RulesSearchResponse.Rule> getJavaDefaultProfileRules() {
        try {
            List<RulesSearchResponse.Rule> rules = new SonarApiImpl().getJavaDefaultProfileRules();
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
