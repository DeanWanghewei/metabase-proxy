package org.wei.metabaseproxy.actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月19日 17:18
 */
public class MetabaseProxyQueryDialogAction extends MetabaseProxyQueryAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent anActionEvent) {
        doAction(anActionEvent, true);
    }
}
