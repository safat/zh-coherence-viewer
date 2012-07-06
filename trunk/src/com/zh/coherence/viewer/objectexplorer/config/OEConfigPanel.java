package com.zh.coherence.viewer.objectexplorer.config;

import com.zh.coherence.viewer.config.AbstractConfigPanel;
import layout.TableLayout;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTitledSeparator;
import org.jdesktop.swingx.VerticalLayout;
import org.springframework.beans.factory.InitializingBean;

import javax.swing.*;
import java.awt.*;

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
        listModel.setClasses(manager.getDataKeeper().getLocalClasses());
        //
        manager.getDataKeeper().setPrimitives(primitive.isSelected());
        manager.getDataKeeper().setEnums(enums.isSelected());
        manager.getDataKeeper().setArray(array.isSelected());
        manager.getDataKeeper().setAnonymous(anonymous.isSelected());
        manager.getDataKeeper().setSynthetic(synthetic.isSelected());
        //list
        manager.getDataKeeper().setLocalClasses(listModel.getClasses());
        manager.convertStringListToClasses();

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

        JToolBar toolBar = new JToolBar();
        toolBar.setFloatable(false);
        toolBar.add(new AddClassAction(listModel));
        toolBar.add(new RemoveClassesAction(listModel, jxList));

        listContainer.add(toolBar, BorderLayout.NORTH);
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
        primitive.setSelected(manager.getDataKeeper().isPrimitives());
        enums.setSelected(manager.getDataKeeper().isEnums());
        array.setSelected(manager.getDataKeeper().isArray());
        anonymous.setSelected(manager.getDataKeeper().isAnonymous());
        synthetic.setSelected(manager.getDataKeeper().isSynthetic());
        //list
        listModel.setClasses(manager.getDataKeeper().getLocalClasses());
    }
}
