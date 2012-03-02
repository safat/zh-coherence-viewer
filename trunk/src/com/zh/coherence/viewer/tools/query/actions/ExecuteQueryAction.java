package com.zh.coherence.viewer.tools.query.actions;

import com.zh.coherence.viewer.tools.query.QueryTool;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 17:30
 */
public class ExecuteQueryAction extends AbstractAction{
    private CqlScriptExecutor scriptExecutor;

    public ExecuteQueryAction(QueryTool queryTool) {
        putValue(Action.NAME, "Execute");

        scriptExecutor = new CqlScriptExecutor(queryTool);
//        putValue(Action.SMALL_ICON, new ImageIcon(""));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        scriptExecutor.execute();
    }
}
