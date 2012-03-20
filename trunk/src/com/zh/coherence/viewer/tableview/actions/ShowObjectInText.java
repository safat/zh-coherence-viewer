package com.zh.coherence.viewer.tableview.actions;

import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;
import com.zh.coherence.viewer.utils.panes.SearchTextPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 08.03.12
 * Time: 13:40
 */
public class ShowObjectInText extends AbstractAction {
    private String value;
    private SearchTextPanel searchTextPanel;

    public ShowObjectInText(Object value) {
        this.value = String.valueOf(value);
        searchTextPanel = new SearchTextPanel();

        putValue(Action.NAME, "Show text");
        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.TEXT));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        final JDialog dialog = new JDialog();
        Container container = dialog.getContentPane();

        container.add(searchTextPanel, BorderLayout.CENTER);
        searchTextPanel.setText(value);

        JPanel buts = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JButton close = new JButton("Close");
        close.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });
        buts.add(close);
        container.add(buts, BorderLayout.SOUTH);

        dialog.setSize(520, 410);
        dialog.setLocationRelativeTo(null);
        dialog.setVisible(true);
    }
}
