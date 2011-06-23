package ua.com.zhivko.coherence.viewer.navigation;

import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTree;
import net.infonode.docking.View;

/**
 * @author Живко
 */
public class NavigationPanel extends JPanel{
    private JTree tree;
    private NavigationTreeModel treeModel;

    public NavigationPanel() {
        treeModel = new NavigationTreeModel();
        tree = new JTree(treeModel.getModel());
        tree.setRootVisible(true);

        setLayout(new BorderLayout());
        add(tree, BorderLayout.CENTER);
        add(new JButton("test"), BorderLayout.NORTH);
    }


    public void updateData(){
        treeModel.updateTree();
        tree.updateUI();
    }
}
