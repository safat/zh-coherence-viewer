package com.zh.coherence.viewer.tools.query.actions;

import com.zh.coherence.viewer.tools.query.QueryTool;
import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;

public class OpenCohQlAction extends AbstractAction {
    private QueryTool tool;

    public OpenCohQlAction(QueryTool tool) {
        this.tool = tool;

        putValue(Action.SMALL_ICON, new IconLoader("icons/folder-open.png"));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser chooser = new JFileChooser();
        int ret = chooser.showOpenDialog((Component) e.getSource());
        if(ret == JFileChooser.APPROVE_OPTION){
            File file = chooser.getSelectedFile();
            if(file.exists()){
                try {
                    FileInputStream fis = new FileInputStream(file);
                    byte[] b = new byte[fis.available()];
                    fis.read(b);
                    fis.close();
                    tool.getEditor().setText(new String(b));
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
            }else{
                System.err.println("File doesn't exist");
            }
        }
    }
}
