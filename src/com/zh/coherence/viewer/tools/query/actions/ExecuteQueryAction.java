package com.zh.coherence.viewer.tools.query.actions;

import com.zh.coherence.viewer.tools.query.QueryContext;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;

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
    private QueryContext context;

    public ExecuteQueryAction(QueryContext context) {
        this.context = context;
        putValue(Action.NAME, "Execute");
        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.START));

        scriptExecutor = new CqlScriptExecutor(context.getQueryTool());
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        context.setBusy(true);
        long time = System.currentTimeMillis();
        scriptExecutor.execute();
        context.setTime(System.currentTimeMillis() - time);
        context.setBusy(false);
    }
}
