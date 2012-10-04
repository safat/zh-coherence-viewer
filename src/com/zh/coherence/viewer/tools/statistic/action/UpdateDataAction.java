package com.zh.coherence.viewer.tools.statistic.action;

import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class UpdateDataAction extends AbstractAction{
    private JMXReport report;

    public UpdateDataAction(JMXReport report) {
        this.report = report;
        putValue(Action.SMALL_ICON, new IconLoader("icons/arrow-circle-double.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        report.refresh();
    }
}
