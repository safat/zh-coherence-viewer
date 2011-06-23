package ua.com.zhivko.coherence.viewer.navigation;

import com.tangosol.io.AbstractReadBuffer;
import com.tangosol.io.pof.PofWriter;
import com.tangosol.io.pof.RawDate;
import com.tangosol.util.Binary;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeModel;
import ua.com.zhivko.coherence.viewer.ProjectManager;

/**
 * @author Живко
 */
public class NavigationTreeModel{
    private DefaultTreeModel model;
    DefaultMutableTreeNode root;

    public NavigationTreeModel() {
        root = new DefaultMutableTreeNode("test");
        model = new DefaultTreeModel(root);
        root.add(new DefaultMutableTreeNode("test2"));
    }

    public TreeModel getModel(){
        return model;
    }

    public void updateTree() throws IOException{
        ProjectManager manager = ProjectManager.getInstance();
        String name = (String) manager.currentProj.get("project.name");
        root.setUserObject(name);
        root.removeAllChildren();
        //find all schemas
        
    }
}
