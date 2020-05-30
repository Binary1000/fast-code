package com.cnqisoft.fastcode;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

/**
 * @author Binary on 2020/5/29
 */
public class GenerateCodeAction extends AnAction {

    @Override
    public void actionPerformed(AnActionEvent e) {
        PathDialog dialog = new PathDialog(e.getProject());
        dialog.open();
    }
}
