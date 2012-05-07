package com.zh.coherence.viewer.tools;

import com.zh.coherence.viewer.ResourceManager;
import com.zh.coherence.viewer.menubar.CreateToolAction;

import javax.swing.*;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 11.02.12
 * Time: 0:51
 */
public class ToolsJMenuBuilder {
    public JMenu buildMenu(){
        final JMenu menu = new JMenu("Tools");
        ToolContainer toolContainer = ResourceManager.getInstance().getToolContainer();
        if(toolContainer == null){
            throw new IllegalStateException("tool container couldn't be null");
        }
        for(ToolDescription description : toolContainer.getTools()){
            ToolFounder founder = ToolFounderFactory.getToolFounder(description);
            JMenuItem item = menu.add(new CreateToolAction(founder));
            item.setEnabled(founder.isAvailable());
        }

        menu.addMenuListener(new MenuListener() {
            @Override
            public void menuSelected(MenuEvent e) {
                for(int i = 0, size = menu.getItemCount(); i < size; i++){
                    JMenuItem item = menu.getItem(i);
                    Action action = item.getAction();
                    if(action instanceof CreateToolAction){
                        item.setEnabled(((CreateToolAction)action).getFounder().isAvailable());
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
}
