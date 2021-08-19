package com.yujunyang.intellij.plugin.sonar.extensions;

import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.intellij.codeInsight.daemon.GutterIconNavigationHandler;
import com.intellij.codeInsight.daemon.LineMarkerInfo;
import com.intellij.codeInsight.daemon.LineMarkerProvider;
import com.intellij.openapi.editor.markup.GutterIconRenderer;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.impl.source.tree.LeafElement;
import com.intellij.psi.impl.source.tree.TreeUtil;
import com.intellij.ui.awt.RelativePoint;
import com.intellij.util.Function;
import com.yujunyang.intellij.plugin.sonar.core.AbstractIssue;
import com.yujunyang.intellij.plugin.sonar.core.AnalyzeState;
import com.yujunyang.intellij.plugin.sonar.core.DuplicatedBlocksIssue;
import com.yujunyang.intellij.plugin.sonar.gui.popup.LineMarkerProviderPopupPanel;
import com.yujunyang.intellij.plugin.sonar.service.ProblemCacheService;
import icons.PluginIcons;
import org.jetbrains.annotations.NotNull;

public class SonarIssueLineMarkerProvider implements LineMarkerProvider {

    @Override
    public LineMarkerInfo<?> getLineMarkerInfo(@NotNull PsiElement element) {
        if (element.getFirstChild() != null) {
            return null;
        }

        final Project project = element.getProject();
        if (!AnalyzeState.get(project).isIdle()) {
            return null;
        }

        final ProblemCacheService problemCacheService = ProblemCacheService.getInstance(project);
        final Map<PsiFile, List<AbstractIssue>> issues = problemCacheService.getIssues();
        final PsiFile psiFile = element.getContainingFile();
        if (!issues.containsKey(psiFile)) {
            return null;
        }



        final List<AbstractIssue> currentFileIssues = issues.get(psiFile);
        final List<AbstractIssue> matchedIssues = new ArrayList<>();
        for (AbstractIssue item : currentFileIssues) {
            if (element == firstLeafOrNull(item.getTextRangePsiElement())) {
                matchedIssues.add(item);
            }
        }

        if (matchedIssues.isEmpty()) {
            return null;
        }

        final GutterIconNavigationHandler<PsiElement> navigationHandler = new IssueGutterIconNavigationHandler(matchedIssues, element);
        return new LineMarkerInfo<>(element, element.getTextRange(), PluginIcons.ISSUE, null, navigationHandler, GutterIconRenderer.Alignment.CENTER);

    }

    private static PsiElement firstLeafOrNull(@NotNull PsiElement element) {
        LeafElement firstLeaf = TreeUtil.findFirstLeaf(element.getNode());
        return firstLeaf != null ? firstLeaf.getPsi() : null;
    }

    private static class IssueGutterIconNavigationHandler implements GutterIconNavigationHandler<PsiElement> {

        private final List<AbstractIssue> issues;
        private final PsiElement psiElement;

        private IssueGutterIconNavigationHandler(
                List<AbstractIssue> issues,
                PsiElement psiElement) {
            this.issues = issues;
            this.psiElement = psiElement;
        }


        @Override
        public void navigate(MouseEvent e, PsiElement elt) {
            JBPopupFactory jbPopupFactory = JBPopupFactory.getInstance();
            LineMarkerProviderPopupPanel contentPanel = new LineMarkerProviderPopupPanel(elt.getProject(), issues);
            JBPopup popup = jbPopupFactory.createComponentPopupBuilder(contentPanel, null).createPopup();
            popup.show(new RelativePoint(e));
            contentPanel.setPopup(popup);
        }
    }

    private static class TooltipProvider implements Function<PsiElement, String> {

        private List<AbstractIssue> issues;

        public TooltipProvider(List<AbstractIssue> issues) {
            this.issues = issues;
        }

        @Override
        public String fun(final PsiElement psiElement) {
            return getTooltipText();
        }


        private String getTooltipText() {
            final StringBuilder buffer = new StringBuilder();
            buffer.append("<!DOCTYPE html><html lang=\"en\"><head><style>h3{margin:0;}</style></head><body>");
            buffer.append("<h3>");
            buffer.append("存在" + issues.size() + "个问题");
            buffer.append("</h3>");
            for (AbstractIssue issue : issues) {
                buffer.append("<p>");
                buffer.append(issue.getName());
                buffer.append("</p>");
                if (issue instanceof DuplicatedBlocksIssue) {
                    DuplicatedBlocksIssue duplicatedBlocksIssue = (DuplicatedBlocksIssue)issue;
                    for (DuplicatedBlocksIssue.Duplicate duplicate : duplicatedBlocksIssue.getDuplicates()) {
                        buffer.append("<p>");
                        buffer.append("<span>");
                        buffer.append(duplicateInfo(duplicate));
                        buffer.append("</span>");
                        buffer.append("</p>");
                    }
                }
            }

            buffer.append("</body></html>");
            return buffer.toString();
        }

        private String duplicateInfo(DuplicatedBlocksIssue.Duplicate duplicate) {
            return String.format("<span>%s</span><span>%s-%s</span>", duplicate.getPath(), duplicate.getStartLine(), duplicate.getEndLine());
        }
    }
}
