package org.zh.coherence.viewer.project.editor;

import info.clearthought.layout.TableLayout;
import lombok.Setter;
import org.jdesktop.swingx.JXTitledSeparator;
import org.zh.coherence.viewer.project.Project;

import javax.swing.*;
import java.awt.*;

import static info.clearthought.layout.TableLayoutConstants.FILL;
import static info.clearthought.layout.TableLayoutConstants.PREFERRED;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 31.01.13
 * Time: 21:29
 */
public class ProjectEditor extends JDialog {
    @Setter
    private Project project = null;
    private JFrame owner;

    public ProjectEditor(JFrame owner, Project project) {
        super(owner, "Project Editor", ModalityType.APPLICATION_MODAL);
        this.owner = owner;
        this.project = project;

        createGui();
    }

    private void createGui() {
        getContentPane().setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane(SwingConstants.TOP);
        addClusterTab(tabs);
        addAuthenticationTab(tabs);
        addConnectionTab(tabs);
        addSerializationTab(tabs);
        addJmxTab(tabs);
        addInvocableTab(tabs);

        getContentPane().add(tabs, BorderLayout.CENTER);

        //buttons
        getContentPane().add(getButtonPanel(), BorderLayout.SOUTH);
    }

    private JPanel getButtonPanel() {
        JButton ok = new JButton("OK");
        JButton cancel = new JButton("Cancel");
        JPanel panel = new JPanel(new TableLayout(new double[][]{
                {FILL, 120, 10, 120, FILL},
                {2, FILL}
        }));
        panel.add(new JXTitledSeparator(""), "0,0,4,0");
        panel.add(ok, "1,1");
        panel.add(cancel, "3,1");

        return panel;
    }

    public void showEditor() {
        this.setSize(400, 250);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        this.setLocationRelativeTo(owner);
        this.setVisible(true);
    }

    private void addClusterTab(JTabbedPane tabs) {
        JPanel panel = new JPanel(new TableLayout(new double[][]{
                {5, PREFERRED, 5, FILL, 5}, {FILL, PREFERRED, FILL}
        }));
        JTextField clusterName = new JTextField();
        panel.add(new JLabel("Cluster name"), "1,1");
        panel.add(clusterName, "3,1");

        tabs.addTab("Cluster", panel);
    }

    private void addConnectionTab(JTabbedPane tabs) {
        JPanel panel = new JPanel(new BorderLayout());
        ListEditor listEditor = new ListEditor();
        listEditor.setData(null);
        panel.add(listEditor, BorderLayout.CENTER);

        tabs.add("Connection", panel);
    }

    private void addSerializationTab(JTabbedPane tabs) {
        JPanel panel = new JPanel(new BorderLayout());
        ListEditor listEditor = new ListEditor();
        listEditor.setData(null);
        panel.add(listEditor, BorderLayout.CENTER);

        tabs.add("Serialization", panel);
    }

    private void addJmxTab(JTabbedPane tabs) {
        JPanel panel = new JPanel(new BorderLayout());
        ListEditor listEditor = new ListEditor();
        listEditor.setData(null);
        panel.add(listEditor, BorderLayout.CENTER);

        tabs.add("JMX", panel);
    }

    private void addInvocableTab(JTabbedPane tabs) {
        JPanel panel = new JPanel(new BorderLayout());
        ListEditor listEditor = new ListEditor();
        listEditor.setData(null);
        panel.add(listEditor, BorderLayout.CENTER);

        tabs.add("Invocable", panel);
    }

    private void addAuthenticationTab(JTabbedPane tabs) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JLabel("not supported yet"));

        tabs.add("Authentication", panel);
    }
}
