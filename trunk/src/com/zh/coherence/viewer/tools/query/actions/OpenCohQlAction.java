package com.zh.coherence.viewer.tools.query.actions;

import com.zh.coherence.viewer.tools.query.QueryTool;
import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 26.05.12
 * Time: 16:15
 */
public class OpenCohQlAction extends AbstractAction {
    private QueryTool tool;

    public OpenCohQlAction(QueryTool tool) {
        this.tool = tool;

        putValue(Action.SMALL_ICON, IconHelper.getInstance().getIcon(IconType.FOLDER_OPEN));
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
