package org.zh.coherence.viewer.project.editor;

import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.decorator.ColorHighlighter;
import org.jdesktop.swingx.decorator.HighlightPredicate;
import org.zh.utils.gui.icons.IconProvider;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 02.02.13
 * Time: 12:01
 */
public class ListEditor extends JPanel {
    private boolean enableEdit = false;
    private boolean enableAdd = true;
    private boolean enableRemove = true;

    private List<String> dataList;
    private EditorListModel editorListModel = new EditorListModel();

    public ListEditor(boolean enableEdit, boolean enableAdd, boolean enableRemove) {
        this.enableEdit = enableEdit;
        this.enableAdd = enableAdd;
        this.enableRemove = enableRemove;

        init();
    }

    public ListEditor() {
        init();
    }

    private void init() {
        setLayout(new BorderLayout());

        JXList dataList = new JXList(editorListModel);
        dataList.setHighlighters();
        dataList.addHighlighter(new ColorHighlighter(new HighlightPredicate.RowGroupHighlightPredicate(1), new Color(192, 192, 192), Color.WHITE));
        add(dataList, BorderLayout.CENTER);

        JToolBar toolBar = new JToolBar(JToolBar.VERTICAL);
        toolBar.add(new AddAction());

        if (enableAdd) {
            add(toolBar, BorderLayout.WEST);
        }

        editorListModel.fireContentsChanged(dataList);
    }

    public String getData() {
        StringBuilder result = new StringBuilder();
        if (dataList.size() > 0) {
            for (String str : dataList) {
                result.append(str).append(";");
            }
        }

        return result.toString();
    }

    public void setData(String data) {
        if (data == null || data.isEmpty()) {
            dataList = new ArrayList<>();
        } else {
            String[] array = data.split(";");
            dataList = new ArrayList<>(array.length + 1);
            for (int i = 0, size = array.length; i < size; i++) {
                dataList.add(array[i]);
            }
        }
    }

    private class AddAction extends AbstractAction {

        private AddAction() {
            putValue(Action.SMALL_ICON, IconProvider.getIcon("32/add.png"));
            putValue(Action.SHORT_DESCRIPTION, "Add new value");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String value = JOptionPane.showInputDialog((Component) e.getSource(), "Add new value:", "List Editor", JOptionPane.QUESTION_MESSAGE);
            if (value != null && !value.isEmpty()) {
                dataList.add(value);
                editorListModel.fireContentsChanged(e.getSource());
            }
        }
    }

    private class EditorListModel extends AbstractListModel<String> {
        @Override
        public int getSize() {
            return dataList.size();
        }

        @Override
        public String getElementAt(int index) {
            return dataList.get(index);
        }

        protected void fireContentsChanged(Object source) {
            if (dataList != null) {
                super.fireContentsChanged(source, 0, dataList.size());
            }
        }
    }
}
