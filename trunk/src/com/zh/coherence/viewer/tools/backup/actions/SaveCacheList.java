package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupContext;
import com.zh.coherence.viewer.utils.icons.IconLoader;

import javax.swing.*;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.awt.event.ActionEvent;
import java.io.File;

public class SaveCacheList extends AbstractAction {

    private BackupContext context;

    public SaveCacheList(BackupContext context) {
        this.context = context;

        putValue(Action.SMALL_ICON, new IconLoader("icons/floppy.png"));
        putValue(Action.SHORT_DESCRIPTION, "Save");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(BackupContext.class);
            Marshaller m = jaxbContext.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
            JFileChooser fileChooser = new JFileChooser();
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
            fileChooser.showSaveDialog(null);
            File selected = fileChooser.getSelectedFile();
            if (selected != null) {
                m.marshal(context, selected);
            }
        } catch (JAXBException e1) {
            e1.printStackTrace();
        }
    }
}
