package com.zh.coherence.viewer.tools.report.jmx.action;

import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import java.awt.event.ActionEvent;

public class RefreshJmxDataAction extends AbstractAction{

    public RefreshJmxDataAction() {
        putValue(Action.SMALL_ICON, new IconLoader("icons/arrow-circle-double.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JMXReport.getInstance().refreshReport();
    }
}
