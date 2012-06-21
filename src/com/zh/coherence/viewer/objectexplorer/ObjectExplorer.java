package com.zh.coherence.viewer.objectexplorer;

import com.zh.coherence.viewer.components.text.SearchTextPanel;
import com.zh.coherence.viewer.objectexplorer.viewer.Viewer;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.renderer.*;
import org.jdesktop.swingx.search.TreeSearchable;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreePath;
import java.awt.*;

public class ObjectExplorer extends JPanel {
    private JXTree fieldsTree;
    private ObjectExplorerTreeModel treeModel;
    private JTextField path;

    public ObjectExplorer() {
        super(new BorderLayout());
        JSplitPane split = new JSplitPane();
        treeModel = new ObjectExplorerTreeModel();
        fieldsTree = new JXTree(treeModel);
        fieldsTree.setCellRenderer(new DefaultTreeRenderer(getIconValue(), new StringValue() {
            @Override
            public String getString(Object o) {
                return String.valueOf(o);
            }
        }));
        SearchTextPanel text = new SearchTextPanel();
        fieldsTree.addTreeSelectionListener(new ObjectExplorerTreeSelectionListener(text));
        fieldsTree.setSearchable(new TreeSearchable(fieldsTree));

        add(split, BorderLayout.CENTER);
        split.setLeftComponent(new JScrollPane(fieldsTree));
        split.setRightComponent(new JScrollPane(text));
        split.setDividerLocation(280);
        split.setDividerSize(2);

        path = new JTextField();
        add(path, BorderLayout.SOUTH);

        fieldsTree.getSelectionModel().addTreeSelectionListener(new TreeSelectionListener() {
            @Override
            public void valueChanged(TreeSelectionEvent e) {
                try{
                QueryPathBuilder queryPathBuilder = new QueryPathBuilder();
                for(TreePath p : fieldsTree.getSelectionPaths()){
                    path.setText(queryPathBuilder.buildQuery(p));
                }
                }catch (Exception ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    private IconValue getIconValue() {
        return new IconValue() {
            @Override
            public Icon getIcon(Object o) {
                if (o instanceof Viewer) {
                    Viewer viewer = (Viewer) o;
                    if (viewer.getSubject() == null) {
                        return new IconLoader("icons/black_question.gif");
                    }
                    if (viewer.getSubject().getClass().isPrimitive() ||
                            viewer.getSubject() instanceof Number) {
                        return new IconLoader("icons/bean-small.png");
                    } else if (viewer.getSubject().getClass().isArray()) {
                        return new IconLoader("icons/array.png");
                    } else if (treeModel.isLeaf(o)) {
                        return new IconLoader("icons/block-small.png");
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
            Object source = fieldsTree.getLastSelectedPathComponent();
            if (source == null) {
                field.setText("NULL");
            } else if (source instanceof Viewer) {
                field.setText(((Viewer) source).getText());
            } else {
                field.setText(source.toString());
            }
        }
    }
}
