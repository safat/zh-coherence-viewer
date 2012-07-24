package com.zh.coherence.viewer.tools.statistic.config.actions;

import com.zh.coherence.viewer.tools.statistic.config.TabConfigListModel;
import com.zh.coherence.viewer.tools.statistic.config.TabContainer;
import com.zh.coherence.viewer.tools.statistic.config.TabType;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import com.zh.coherence.viewer.utils.ui.ZHHorizontalPanel;
import layout.TableLayout;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXRadioGroup;
import org.jdesktop.swingx.JXTextField;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AddTabConfigAction extends AbstractAction {

    private TabConfigListModel model;

    public AddTabConfigAction(TabConfigListModel model) {
        this.model = model;

        putValue(Action.NAME, "ADD");
        putValue(Action.SMALL_ICON, new IconLoader("icons/plus.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent component = (JComponent) e.getSource();
        InnerDialog innerDialog = new InnerDialog(component);
        TabContainer container = innerDialog.showDialog();

        if(container != null){
            model.addTab(container);
        }
    }

    private class InnerDialog extends JDialog {

        private TabContainer container = null;

        private JComponent parentComponent;

        private JXRadioGroup<String> radioGroup;
        private JXTextField textField;

        public InnerDialog(JComponent component) {
            super(SwingUtilities.getWindowAncestor(component), "Create New Tab",
                    ModalityType.DOCUMENT_MODAL);

            parentComponent = component;
            container = new TabContainer();
            container.setType(TabType.TABLE);

            radioGroup = new JXRadioGroup<String>();
            radioGroup.add("Table");
            radioGroup.add("List");
            radioGroup.setSelectedValue("Table");

            setLayout(new TableLayout(new double[][]{
                    {4, TableLayout.PREFERRED, 4, TableLayout.FILL, 4},
                    {5, TableLayout.PREFERRED, 4, TableLayout.PREFERRED, 7, 3,
                            TableLayout.PREFERRED, 4}
            }));

            add(new JXLabel("Report type:"), "1,1");
            add(radioGroup, "3,1");

            textField = new JXTextField("Input tab's title here");
            add(new JXLabel("Name:"), "1,3");
            add(textField, "3,3");
            add(new JSeparator(), "1,5,3,5");
            JButton okBut = new JButton("OK");
            okBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //name
                    String name = textField.getText();
                    if (name == null || name.isEmpty()) {
                        container = null;
                        InnerDialog.this.setVisible(false);
                        return;
                    } else {
                        container.setName(name);
                    }
                    //type
                    container.setType(TabType.valueOf(radioGroup.getSelectedValue().toUpperCase()));
                    InnerDialog.this.setVisible(false);
                }
            });

            JButton cancelBut = new JButton("Cancel");
            cancelBut.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    container = null;
                    InnerDialog.this.setVisible(false);
                }
            });

            add(new ZHHorizontalPanel(ZHHorizontalPanel.HorizontalPanelAlignment.CENTER, 4, 0, okBut, cancelBut), "1,6,3,6");
        }

        public TabContainer showDialog() {
            this.pack();
            this.setLocationRelativeTo(SwingUtilities.getWindowAncestor(parentComponent));
            this.setVisible(true);
            this.dispose();
            return container;
        }
    }
}
