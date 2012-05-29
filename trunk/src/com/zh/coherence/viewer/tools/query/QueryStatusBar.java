package com.zh.coherence.viewer.tools.query;

import layout.TableLayout;
import org.jdesktop.swingx.JXBusyLabel;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.TimeUnit;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 09.04.12
 * Time: 21:33
 */
public class QueryStatusBar extends JPanel {
    private QueryContext context;

    private JXBusyLabel busy = new JXBusyLabel();
    private JTextField text = new JTextField();
    private JTextField timeField = new JTextField();
    private JTextField rowsField = new JTextField();

    private JButton tableButton = new JButton("Table");
    private JButton eventLogButton = new JButton("Event Log");

    public QueryStatusBar(final QueryContext context) {
        super(new TableLayout(new double[][]{
                {2, TableLayout.PREFERRED, 2, TableLayout.FILL, 2, TableLayout.PREFERRED, 2, 70, 2,
                        TableLayout.PREFERRED, 2, 130, 2, TableLayout.PREFERRED, 2, TableLayout.PREFERRED, 2},
                {1, TableLayout.PREFERRED, 1}
        }));
        this.context = context;

        setBorder(BorderFactory.createEtchedBorder());
        add(busy, "1,1");

        text.setEditable(false);
        text.setEnabled(false);
        add(text, "3,1");

        add(new JLabel("Rows:"), "5,1");
        rowsField.setEditable(false);
        rowsField.setEnabled(false);
        add(rowsField, "7,1");

        add(new JLabel("Time:"), "9,1");
        timeField.setEditable(false);
        timeField.setEnabled(false);
        add(timeField, "11,1");

        add(tableButton, "13, 1");
        tableButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.showOutputPane(QueryContext.TABLE_VIEW);
            }
        });

        add(eventLogButton, "15,1");
        eventLogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                context.showOutputPane(QueryContext.EVENT_LOG);
            }
        });
    }

    public void setBusy(boolean busy) {
        this.busy.setBusy(busy);
    }

    public void setTime(long time) {
        timeField.setText(getTime(time));
    }

    public void setRowsSize(int size){
        rowsField.setText(String.valueOf(size));
    }

    private String getTime(long millis) {
        long sec;
        long min = TimeUnit.MILLISECONDS.toMinutes(millis);
        sec = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(min);
        long ms = millis - TimeUnit.SECONDS.toMillis(sec) - TimeUnit.MINUTES.toMillis(min);
        return String.format("%d min, %d sec %d ms", min, sec, ms);
    }

    public void showShortMessage(String msg){
        text.setText(msg);
    }
}
