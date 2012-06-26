package com.zh.coherence.viewer.objectexplorer;

import com.zh.coherence.viewer.components.text.SearchTextPanel;
import com.zh.coherence.viewer.objectexplorer.viewer.Viewer;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import org.jdesktop.swingx.JXTreeTable;
import org.jdesktop.swingx.renderer.DefaultTreeRenderer;
import org.jdesktop.swingx.renderer.IconValue;
import org.jdesktop.swingx.search.TableSearchable;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;

public class ObjectExplorer extends JPanel {
    private JXTreeTable fieldsTree;
    private ObjectExplorerTreeModel treeModel;
    private JTextField path;

    public ObjectExplorer() {
        super(new BorderLayout());
        JSplitPane split = new JSplitPane();
        treeModel = new ObjectExplorerTreeModel();
        fieldsTree = new JXTreeTable(treeModel);
        fieldsTree.setRootVisible(true);
        fieldsTree.setTreeCellRenderer(new DefaultTreeRenderer(getIconValue(), new TreeStringValue()));
        SearchTextPanel text = new SearchTextPanel();
        fieldsTree.addTreeSelectionListener(new ObjectExplorerTreeSelectionListener(text));
        fieldsTree.setSearchable(new TableSearchable(fieldsTree));
        fieldsTree.setColumnControlVisible(true);
        fieldsTree.getColumnModel().getColumn(1).setMaxWidth(40);

        add(split, BorderLayout.CENTER);
        split.setLeftComponent(new JScrollPane(fieldsTree));
        split.setRightComponent(new JScrollPane(text));
        split.setDividerLocation(280);
        split.setDividerSize(2);

        path = new JTextField();
        path.setEditable(false);
        add(path, BorderLayout.SOUTH);
    }

    private IconValue getIconValue() {
        return new IconValue() {
            @Override
            public Icon getIcon(Object o) {
                if (o instanceof Viewer) {
                    Viewer viewer = (Viewer) o;
                    if (viewer.getSubject() == null) {
                        return new IconLoader("icons/black_question.gif");
                    } else if (viewer.getSubject().getClass().isArray()) {
                        return new IconLoader("icons/array.png");
                    } else if (treeModel.isLeaf(o)) {
                        return new IconLoader("icons/bean-small.png");
                    }
                }
                return null;
            }
        };
    }

    public void setData(Object value) {
        treeModel.setSubject(value);
        fieldsTree.updateUI();
    }

    private class ObjectExplorerTreeSelectionListener implements TreeSelectionListener {
        SearchTextPanel field;

        private ObjectExplorerTreeSelectionListener(SearchTextPanel field) {
            this.field = field;
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            Object source = e.getPath().getLastPathComponent();
            if (source == null) {
                field.setText("NULL");
            } else if (source instanceof Viewer) {
                field.setText(((Viewer) source).getText());
            } else {
                field.setText(source.toString());
            }
            //update query
            try {
                QueryPathBuilder queryPathBuilder = new QueryPathBuilder();
                path.setText(queryPathBuilder.buildQuery(e.getPath()));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
    }
}
