package com.zh.coherence.viewer.tools.query.actions;

import com.zh.coherence.viewer.tools.query.QueryContext;
import com.zh.coherence.viewer.tools.query.QueryEngine;
import com.zh.coherence.viewer.tools.query.QueryLogEvent;
import com.zh.coherence.viewer.tools.query.QueryResult;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.Collection;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 17:30
 */
public class ExecuteQueryAction extends AbstractAction {
    private QueryContext context;
    private QueryEngine queryEngine;

    public ExecuteQueryAction(QueryContext context, QueryEngine queryEngine) {
        this.context = context;

        this.queryEngine = queryEngine;
        putValue(Action.NAME, "Execute");
        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.START));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        context.setBusy(true);
        final long time = System.currentTimeMillis();

        try {

            new SwingWorker<Void, Void>() {

                @Override
                protected Void doInBackground() throws Exception {
                    QueryResult result = queryEngine.execute(context.getQueryTool().getScript());
                    if (result.isContainsError()) {
                        context.showShortMessage(result.getErrorMessage());
                        context.logEvent(new QueryLogEvent(result.getError(),
                                result.getErrorMessage(), QueryLogEvent.EventType.ERROR));
                        context.showOutputPane(QueryContext.EVENT_LOG);
                        context.setSize(0);
                    } else {
                        context.showShortMessage("OK");

                        Object ret = result.getResult();
                        if (ret == null) {
                            context.setSize(0);
                            context.showOutputPane(QueryContext.NO_DATA);
                        } else if (ret instanceof Map) {
                            int size = ((Map) ret).size();
                            context.setSize(size);
                            if (size == 0) {
                                context.showOutputPane(QueryContext.NO_DATA);
                            } else {
                                context.showOutputPane(context.getCurrentOutputTool());
                                context.getQueryTool().showResult(ret, result.getTerm(), size);
                            }
                        } else if (ret instanceof Collection) {
                            int size = ((Collection) ret).size();
                            context.setSize(size);
                            if (size == 0) {
                                context.showOutputPane(QueryContext.NO_DATA);
                            } else {
                                context.showOutputPane(context.getCurrentOutputTool());
                                context.getQueryTool().showResult(ret, result.getTerm(), size);
                            }
                        } else if (ret instanceof Number || ret instanceof String) {
                            context.setSize(1);
                            context.showOutputPane(context.getCurrentOutputTool());
                            context.getQueryTool().showResult(ret, result.getTerm(), 1);
                            context.showShortMessage("OK");
                        } else {
                            context.setSize(0);
                            context.showOutputPane(QueryContext.ERROR);
                            context.logEvent(new QueryLogEvent(null, "unknown class: " +
                                    ret.getClass(), QueryLogEvent.EventType.MESSAGE));
                            context.showOutputPane(QueryContext.EVENT_LOG);
                        }
                    }
                    return null;
                }

                @Override
                protected void done() {
                    context.setTime(System.currentTimeMillis() - time);
                    context.setBusy(false);
                    super.done();
                }
            }.execute();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
