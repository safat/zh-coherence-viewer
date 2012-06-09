package com.zh.coherence.viewer.tools;

import com.zh.coherence.viewer.ApplicationPane;
import com.zh.coherence.viewer.menubar.CreateToolAction;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;
import java.util.List;

public class ToolManager {
    private List<ToolLauncher> launchers;

    private ApplicationPane applicationPane;

    public JMenu getMenu(){
        final JMenu menu = new JMenu("Tools");
        if(launchers != null){
            for(ToolLauncher launcher : launchers){
                JMenuItem item = menu.add(new CreateToolAction(launcher, applicationPane));
                item.setEnabled(launcher.isAvailable());
            }
        }

        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                for(int i = 0, size = menu.getItemCount(); i < size; i++){
                    JMenuItem item = menu.getItem(i);
                    Action action = item.getAction();
                    if(action instanceof CreateToolAction){
                        item.setEnabled(((CreateToolAction)action).getLauncher().isAvailable());
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

    public List<ToolLauncher> getLaunchers() {
        return launchers;
    }

    public void setLaunchers(List<ToolLauncher> launchers) {
        this.launchers = launchers;
    }

    public ApplicationPane getApplicationPane() {
        return applicationPane;
    }

    public void setApplicationPane(ApplicationPane applicationPane) {
        this.applicationPane = applicationPane;
    }
}
