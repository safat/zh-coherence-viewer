package com.zh.coherence.viewer.tools;

import com.zh.coherence.viewer.ApplicationPane;
import com.zh.coherence.viewer.menubar.CreateToolAction;
import com.zh.coherence.viewer.menubar.SubMenu;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.util.ArrayList;
import java.util.List;

public class ToolManager {
    private List<Object> launchers;

    private ApplicationPane applicationPane;

    private List<JMenuItem> items = new ArrayList<JMenuItem>();

    private void recursiveBuild(Object obj, JMenu menu){
        if (obj instanceof ToolLauncher) {
            ToolLauncher launcher = (ToolLauncher) obj;
            JMenuItem item = buildMenuItem(menu, launcher);
            items.add(item);
        }else if(obj instanceof SubMenu){
            SubMenu sub = (SubMenu) obj;
            JMenu m = new JMenu();
            m.setText(sub.getName());
            if(sub.getIcon() != null){
                m.setIcon(sub.getIcon());
            }
            menu.add(m);
            for(Object o : sub.getChild()){
                recursiveBuild(o, m);
            }
        }
    }

    public JMenu getMenu() {
        final JMenu menu = new JMenu("Tools");
        if (launchers != null) {
            for (Object obj : launchers) {
                recursiveBuild(obj, menu);
            }
        }

        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                for (JMenuItem item : items) {
                    Action action = item.getAction();
                    if (action instanceof CreateToolAction) {
                        item.setEnabled(((CreateToolAction) action).getLauncher().isAvailable());
                    }
                }
            }

            @Override
            public void menuDeselected(MenuEvent e) {
            }

            @Override
            public void menuCanceled(MenuEvent e) {
            }
        });

        return menu;
    }

    private JMenuItem buildMenuItem(JMenu menu, ToolLauncher launcher){
        JMenuItem item = menu.add(new CreateToolAction(launcher, applicationPane));
        item.setEnabled(launcher.isAvailable());
        return item;
    }

    public List<Object> getLaunchers() {
        return launchers;
    }

    public void setLaunchers(List<Object> launchers) {
        this.launchers = launchers;
    }

    public ApplicationPane getApplicationPane() {
        return applicationPane;
    }

    public void setApplicationPane(ApplicationPane applicationPane) {
        this.applicationPane = applicationPane;
    }
}
