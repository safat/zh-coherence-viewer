package com.zh.coherence.viewer.tools.statistic.action;

import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UpdateDataAction extends AbstractAction{
    private JMXReport report;

    public UpdateDataAction(JMXReport report) {
        this.report = report;
        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.ARROW_CIRCLE_DOUBLE));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        report.refresh();
    }
}
