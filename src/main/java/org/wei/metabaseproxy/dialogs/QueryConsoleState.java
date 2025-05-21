package org.wei.metabaseproxy.dialogs;

import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.wm.ToolWindow;

/**
 * @author deanwanghewei@gmail.com
 * @description
 * @date 2025年05月20日 17:41
 */
public class QueryConsoleState {
    private final ToolWindow toolWindow;
    private final ConsoleView consoleView;

    public QueryConsoleState(ToolWindow toolWindow, ConsoleView consoleView) {
        this.toolWindow = toolWindow;
        this.consoleView = consoleView;
    }

    public ToolWindow getToolWindow() {
        return toolWindow;
    }

    public ConsoleView getConsoleView() {
        return consoleView;
    }
}
