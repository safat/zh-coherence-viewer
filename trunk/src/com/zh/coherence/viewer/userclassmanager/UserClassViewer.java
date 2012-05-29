package com.zh.coherence.viewer.userclassmanager;

import com.zh.coherence.viewer.pof.ZhPofContext;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXSearchField;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 26.05.12
 * Time: 18:06
 */
public class UserClassViewer extends JPanel {
    private JXList classes;
    private DefaultListModel classesListModel;

    private JXTable fields;
    private UserClassViewerTableModel tableModel;

    private ZhPofContext pofContext;

    private boolean showFullClassName = false;

    public UserClassViewer() {
        init();
    }

    private void init() {
        classesListModel = new DefaultListModel();
        classes = new JXList(classesListModel);

        tableModel = new UserClassViewerTableModel();
        fields = new JXTable(tableModel);
        fields.getColumnModel().getColumn(0).setResizable(false);
        fields.getColumnModel().getColumn(0).setMaxWidth(20);

        classes.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                int idx = classes.getSelectedIndex();
                if(idx < 0){
                    tableModel.setClazz(null);
                }else{
                    ClassName className = (ClassName) classesListModel.get(idx);
                    tableModel.setClazz(className.getClazz());
                }
            }
        });

        pofContext = new ZhPofContext();

        setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);
        add(splitPane, BorderLayout.CENTER);

        JPanel classesPane = new JPanel(new BorderLayout());
        final JXSearchField classesSearchField = new JXSearchField();

        classesPane.add(classesSearchField, BorderLayout.NORTH);
        classesPane.add(new JScrollPane(classes), BorderLayout.CENTER);

        splitPane.setLeftComponent(classesPane);
        splitPane.setRightComponent(new JScrollPane(fields));

        refresh();

        classes.setAutoCreateRowSorter(true);
        classesSearchField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                classes.setRowFilter(new RowFilter<ListModel, Integer>() {
                    @Override
                    public boolean include(Entry<? extends ListModel, ? extends Integer> entry) {
                        return entry.getStringValue(entry.getIdentifier()).toLowerCase().contains(classesSearchField.getText().toLowerCase());
                    }
                });
            }
        });
    }

    public void refresh() {
        for (int i : pofContext.getPofConfig().getRegisteredTypes()) {
            if (i > 1000) {
                String name = showFullClassName ? pofContext.getClassName(i) : pofContext.getClass(i).getSimpleName();
                classesListModel.addElement(new ClassName(pofContext.getClass(i), i, name));
            }
        }
    }
}
