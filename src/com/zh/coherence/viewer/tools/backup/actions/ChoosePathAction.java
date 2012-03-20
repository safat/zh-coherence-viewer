package com.zh.coherence.viewer.tools.backup.actions;

import com.zh.coherence.viewer.tools.backup.BackupContext;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.File;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 19.03.12
 * Time: 21:46
 */
public class ChoosePathAction extends AbstractAction{
    private BackupContext context;
    private JTextField textField;

    public ChoosePathAction(BackupContext context, JTextField textField) {
        this.context = context;
        this.textField = textField;

        putValue(Action.NAME, "Choose");
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        JComponent component = (JComponent) e.getSource();

        JFileChooser fileChooser = new JFileChooser();
        String path = textField.getText();
        if(path != null && !path.isEmpty()){
            File file = new File(path);
            if(file.exists()){
                fileChooser.setSelectedFile(file);
            }
        }
        fileChooser.setDialogType(context.getAction()== BackupContext.BackupAction.BACKUP
                ? JFileChooser.SAVE_DIALOG : JFileChooser.OPEN_DIALOG);
        fileChooser.setFileSelectionMode(context.getTarget() == BackupContext.Target.FOLDER
            ? JFileChooser.DIRECTORIES_ONLY : JFileChooser.FILES_ONLY);
        fileChooser.showDialog(component.getTopLevelAncestor(), "OK");

        File target = fileChooser.getSelectedFile();
        textField.setText(target.toString());
    }
}
