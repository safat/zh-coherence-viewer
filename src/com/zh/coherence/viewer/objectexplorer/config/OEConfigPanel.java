package com.zh.coherence.viewer.objectexplorer.config;

import com.zh.coherence.viewer.config.AbstractConfigPanel;
import com.zh.coherence.viewer.config.ConfigPanel;
import layout.TableLayout;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.beans.factory.InitializingBean;

import javax.swing.*;

import java.awt.*;
import java.util.Set;

import static layout.TableLayoutConstants.FILL;
import static layout.TableLayoutConstants.PREFERRED;

public class OEConfigPanel extends AbstractConfigPanel implements InitializingBean{
    private OEConfigManager manager;
    private JPanel config;

    //checks
    private JCheckBox primitive = new JCheckBox("Primitives");
    private JCheckBox enums = new JCheckBox("Enums");
    private JCheckBox array = new JCheckBox("Arrays");
    private JCheckBox anonymous = new JCheckBox("Anonymous classes");
    private JCheckBox synthetic = new JCheckBox("Synthetic classes");

    private ClassesListModel listModel = new ClassesListModel();

    @Override
    public void applyChanges() {
        manager.saveConfig();
    }

    @Override
    public void cancelChanges() {
        manager.loadConfig();
    }

    @Override
    public void showHelp() {
    }

    @Override
    public boolean isHelpAvailable() {
        return false;
    }

    @Override
    public JComponent getConfigPanel() {
        updateConfigPanel();
        return config;
    }

    @Override
    public int getChildCount() {
        return 0;
    }

    @Override
    public ConfigPanel getChild(int index) {
        return null;
    }

    @Override
    public boolean leaveThePage() {
        return true;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        manager = OEConfigManager.getInstance();
        //load config from file
        config = new JPanel(new TableLayout(new double[][]{
                {4, 400, 10, FILL,4},
                {4, PREFERRED, 2, FILL, 4}
        }));

        config.add(new JXTitledSeparator("Stop at classes:"), "1,1");
        config.add(new JXTitledSeparator("Stop at types:"), "3,1");
        //list
        JXList jxList = new JXList(listModel);
        JPanel listContainer = new JPanel(new BorderLayout());
        listContainer.add(new JToolBar(), BorderLayout.NORTH);
        listContainer.add(new JScrollPane(jxList));
        config.add(listContainer, "1,3");
        //checks
        JPanel checks = new JPanel(new VerticalLayout(3));
        checks.add(primitive);
        checks.add(enums);
        checks.add(array);
        checks.add(anonymous);
        checks.add(synthetic);

        config.add(checks, "3,3");
    }

    private void updateConfigPanel(){
        //todo !
    }

    private class ClassesListModel extends AbstractListModel{

        private Set<Class> classes = null;

        @Override
        public int getSize() {
            return 0;
        }

        @Override
        public Object getElementAt(int index) {
            return null;
        }

        public Set<Class> getClasses() {
            return classes;
        }

        public void setClasses(Set<Class> classes) {
            this.classes = classes;
        }
    }
}
