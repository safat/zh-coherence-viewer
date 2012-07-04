package com.zh.coherence.viewer.config;

import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import java.util.List;

public class ConfigTreeModel implements TreeModel {
    List<ConfigPanel> configPanels = null;

    public ConfigTreeModel(List<ConfigPanel> configPanels) {
        this.configPanels = configPanels;
    }

    Object root = new Object();

    @Override
    public Object getRoot() {
        return root;
    }

    @Override
    public Object getChild(Object parent, int index) {
        if(root.equals(parent)){
            return new TreeNodeWrapper(configPanels.get(index));
        }else if(parent instanceof TreeNodeWrapper){
            return new TreeNodeWrapper(((TreeNodeWrapper) parent).getPanel());
        }
        return null;
    }

    @Override
    public int getChildCount(Object parent) {
        if(root.equals(parent)){
            return configPanels.size();
        }else if(parent instanceof TreeNodeWrapper){
            return ((TreeNodeWrapper) parent).getPanel().getChildCount();
        }
        return 0;
    }

    @Override
    public boolean isLeaf(Object node) {
        return getChildCount(node) == 0;
    }

    @Override
    public void valueForPathChanged(TreePath path, Object newValue) {
    }

    @Override
    public int getIndexOfChild(Object parent, Object child) {
        return 0;
    }

    @Override
    public void addTreeModelListener(TreeModelListener l) {
    }

    @Override
    public void removeTreeModelListener(TreeModelListener l) {
    }

    public class TreeNodeWrapper{
        private ConfigPanel panel;

        private TreeNodeWrapper(ConfigPanel panel) {
            this.panel = panel;
        }

        public String toString(){
            return panel.getConfigName();
        }

        public ConfigPanel getPanel() {
            return panel;
        }
    }
}
