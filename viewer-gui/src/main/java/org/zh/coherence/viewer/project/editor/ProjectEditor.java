package org.zh.coherence.viewer.project.editor;

import info.clearthought.layout.TableLayout;
import lombok.Setter;
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

    public ProjectEditor(JFrame owner) {
        super(owner, "Project Editor", ModalityType.APPLICATION_MODAL);
        this.owner = owner;

        createGui();
    }

    private void createGui() {
        getContentPane().setLayout(new BorderLayout());
        JTabbedPane tabs = new JTabbedPane(SwingConstants.TOP);
        addClusterTab(tabs);

        getContentPane().add(tabs, BorderLayout.CENTER);
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
}
