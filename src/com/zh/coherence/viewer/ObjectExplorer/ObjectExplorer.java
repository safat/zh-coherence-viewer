package com.zh.coherence.viewer.objectexplorer;

import com.zh.coherence.viewer.objectexplorer.viewer.Viewer;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import java.awt.*;

public class ObjectExplorer extends JPanel {
    private JTree fieldsTree;
    private ObjectExplorerTreeModel treeModel;
    private JTextArea text =new JTextArea();

    public ObjectExplorer() {
        super(new BorderLayout());
        JSplitPane split = new JSplitPane();
        treeModel = new ObjectExplorerTreeModel();
        fieldsTree = new JTree(treeModel);
        fieldsTree.addTreeSelectionListener(new ObjectExplorerTreeSelectionListener(text));
        text.setLineWrap(true);
        text.setFont(new Font("Dialog", Font.PLAIN, 12));

        add(split, BorderLayout.CENTER);
        split.setLeftComponent(new JScrollPane(fieldsTree));
        split.setRightComponent(new JScrollPane(text));
        split.setDividerLocation(280);
        split.setDividerSize(2);
    }

    public void showObject(Object obj) {
        treeModel.setSubject(obj);
        fieldsTree.updateUI();
    }

    private class ObjectExplorerTreeSelectionListener implements TreeSelectionListener{
        JTextArea field;

        private ObjectExplorerTreeSelectionListener(JTextArea field) {
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
