package com.zh.coherence.viewer.tools.statistic.config.actions;

import com.zh.coherence.viewer.tools.statistic.config.JMXConfigTableModel;
import com.zh.coherence.viewer.tools.statistic.report.JMXReport;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import com.zh.coherence.viewer.utils.ui.ZHHorizontalPanel;
import layout.TableLayout;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXTextArea;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;

import static layout.TableLayoutConstants.PREFERRED;

public class AddTableItemAction extends AbstractAction {

    private JMXConfigTableModel model;

    public AddTableItemAction(JMXConfigTableModel model) {
        this.model = model;

        putValue(Action.NAME, "ADD");
        putValue(Action.SMALL_ICON, new IconLoader("icons/plus.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent component = (JComponent) e.getSource();
        InnerDialog innerDialog = new InnerDialog(component);

        innerDialog.showDialog();
        String result = innerDialog.getResult();
        if(result != null){
            model.createItem(result);
        }
    }

    private class InnerDialog extends JDialog {
        private JComponent parentComponent;
        private JXComboBox properties;
        private JXTextArea description;
        private String result = null;

        private JMXReport report;

        private InnerDialog(JComponent component) {
            super(SwingUtilities.getWindowAncestor(component), "Choose JMX property",
                    ModalityType.DOCUMENT_MODAL);

            parentComponent = component;
            report = new JMXReport();

            setLayout(new TableLayout(new double[][]{
                    {4, 340, 4},
                    {4, PREFERRED, 2, PREFERRED, 2, 110,7, 3, 2, PREFERRED, 4}
            }));

            List<String> data = new ArrayList<String>(report.getProperties().keySet());
            Collections.sort(data);
            properties = new JXComboBox(data.toArray());

            description = new JXTextArea();
            description.setLineWrap(true);
            description.setWrapStyleWord(true);
            description.setEditable(false);
            description.setFont(new Font("Dialog", Font.PLAIN, 14));

            add(new JLabel("Choose JMX property:"), "1,1");

            add(properties, "1, 3");
            add(new JScrollPane(description), "1, 5");

            properties.addActionListener(new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    showPropertyDescription();
                }
            });
            add(new JSeparator(), "1,7");
            JButton okBut = new JButton("OK");
            okBut.addActionListener(new ActionListener(){
                @Override
                public void actionPerformed(ActionEvent e) {
                    result = String.valueOf(properties.getSelectedItem());
                    InnerDialog.this.dispose();
                }
            });
            JButton cancelBut = new JButton("Cancel");
            cancelBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    result = null;
                    InnerDialog.this.dispose();
                }
            });
            add(new ZHHorizontalPanel(ZHHorizontalPanel.HorizontalPanelAlignment.CENTER, 4, 0, okBut, cancelBut), "1,9");
        }

        private void showPropertyDescription() {
            String dsk = report.getProperties().get(properties.getSelectedItem());
            description.setText(dsk);
        }

        public void showDialog() {
            this.pack();
            this.setLocationRelativeTo(SwingUtilities.getWindowAncestor(parentComponent));
            showPropertyDescription();
            this.setVisible(true);
        }

        public String getResult(){
            return result;
        }
    }
}
