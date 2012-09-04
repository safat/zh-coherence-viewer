package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupContext;
import com.zh.coherence.viewer.tools.backup.BackupTool;
import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.awt.event.ActionEvent;
import java.io.File;

public class LoadCacheList extends AbstractAction {

    private BackupContext context;
    private BackupTool backupTool;

    public LoadCacheList(BackupContext context, BackupTool backupTool) {

        this.context = context;
        this.backupTool = backupTool;

        putValue(Action.SMALL_ICON, new IconLoader("icons/folder-gray-open.png"));
        putValue(Action.SHORT_DESCRIPTION, "Load cache list");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.showOpenDialog(null);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fileChooser.setFileFilter(new javax.swing.filechooser.FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory() || f.getName().endsWith(".xml");
            }

            @Override
            public String getDescription() {
                return "*.xml";
            }
        });
        File selected = fileChooser.getSelectedFile();
        if(selected != null){
            try {
                JAXBContext jaxbContext = JAXBContext.newInstance(BackupContext.class);
                Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
                BackupContext backupContext = (BackupContext) unmarshaller.unmarshal(selected);
                context.setPath(backupContext.getPath());
                context.setBufferSize(backupContext.getBufferSize());
                context.setCacheInfoList(backupContext.getCacheInfoList());
                backupTool.updateUIFromContext();
            } catch (JAXBException ex) {
                ex.printStackTrace();
            }

        }
    }
}
