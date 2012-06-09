package com.zh.coherence.viewer.objectexplorer;

import com.zh.coherence.viewer.components.text.SearchTextPanel;
import com.zh.coherence.viewer.objectexplorer.viewer.Viewer;
import org.jdesktop.swingx.JXTree;
import org.jdesktop.swingx.search.TreeSearchable;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;

public class ObjectExplorer extends JPanel{
    private JXTree fieldsTree;
    private ObjectExplorerTreeModel treeModel;

    public ObjectExplorer() {
        super(new BorderLayout());
        JSplitPane split = new JSplitPane();
        treeModel = new ObjectExplorerTreeModel();
        fieldsTree = new JXTree(treeModel);
        SearchTextPanel text = new SearchTextPanel();
        fieldsTree.addTreeSelectionListener(new ObjectExplorerTreeSelectionListener(text));
        fieldsTree.setSearchable(new TreeSearchable(fieldsTree));

        add(split, BorderLayout.CENTER);
        split.setLeftComponent(new JScrollPane(fieldsTree));
        split.setRightComponent(new JScrollPane(text));
        split.setDividerLocation(280);
        split.setDividerSize(2);
    }

    public void setData(Object value){
        treeModel.setSubject(value);
        fieldsTree.updateUI();
    }

    private class ObjectExplorerTreeSelectionListener implements TreeSelectionListener{
        SearchTextPanel field;

        private ObjectExplorerTreeSelectionListener(SearchTextPanel field) {
            this.field = field;
        }

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            Object source = fieldsTree.getLastSelectedPathComponent();
            if(source == null){
                field.setText("NULL");
            }else if(source instanceof Viewer){
                field.setText(((Viewer)source).getText());
            }else{
                field.setText(source.toString());
            }
        }
    }
}
