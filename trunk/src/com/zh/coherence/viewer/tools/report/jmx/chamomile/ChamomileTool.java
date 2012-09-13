package com.zh.coherence.viewer.tools.report.jmx.chamomile;

import com.zh.coherence.viewer.tools.CoherenceViewerTool;
import com.zh.coherence.viewer.tools.report.jmx.action.AutoRefreshJmxDataAction;
import com.zh.coherence.viewer.tools.report.jmx.action.RefreshJmxDataAction;
import com.zh.coherence.viewer.utils.icons.IconLoader;
import edu.uci.ics.jung.algorithms.layout.CircleLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.RadialTreeLayout;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Forest;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseMultigraph;
import edu.uci.ics.jung.visualization.BasicVisualizationServer;
import layout.TableLayout;
import org.jdesktop.swingx.*;

import javax.swing.*;
import java.awt.*;
import java.util.Dictionary;
import java.util.Hashtable;

import static layout.TableLayoutConstants.FILL;
import static layout.TableLayoutConstants.PREFERRED;

public class ChamomileTool extends JXPanel implements CoherenceViewerTool {
    @Override
    public JComponent getPane() {
        return this;
    }

    public ChamomileTool() {
        setBackground(Color.WHITE);
        createUI();
    }

    private void createUI() {
        setLayout(new BorderLayout());
        //glass control pane
        JXPanel controlPanel = new JXPanel(new VerticalLayout(4));
        controlPanel.setBackground(Color.WHITE);

        controlPanel.add(new JXTitledSeparator(
                "<html><b>Chamomile control panel</b></html>", SwingConstants.HORIZONTAL));
        JXPanel buttons = new JXPanel(new TableLayout(new double[][]{
                {FILL, PREFERRED, 25, PREFERRED, FILL}, {PREFERRED}
        }));
        buttons.setBackground(Color.WHITE);
        buttons.add(new JXButton(new RefreshJmxDataAction()), "1,0");
        buttons.add(new JXButton(new AutoRefreshJmxDataAction()), "3,0");
        controlPanel.add(buttons);
        JXComboBox dataType = new JXComboBox();
        //todo model
        controlPanel.add(dataType);

        JSlider zoom = new JSlider(0,500,100);
        Dictionary<Integer, JComponent> dic = new Hashtable<Integer, JComponent>();
        dic.put(100, new JLabel("100%"));
        dic.put(0, new JLabel("0%"));
        dic.put(250, new JLabel("250%"));
        dic.put(500, new JLabel("500%"));
        zoom.setLabelTable(dic);
        zoom.setPaintLabels(true);
        zoom.setPaintTrack(true);
        controlPanel.add(zoom);

        controlPanel.add(new JXTitledSeparator("<html><b>Label</b></html>", SwingConstants.HORIZONTAL));

        controlPanel.setBorder(BorderFactory.createDashedBorder(Color.GRAY));
        add(controlPanel, BorderLayout.WEST);

        //chamomile panel
        JXPanel chamomilePanel = new JXPanel();

        Graph<Integer, String> g = new SparseMultigraph<Integer, String>();
        g.addVertex((Integer)1);
        g.addVertex((Integer)2);
        g.addVertex((Integer)3);
        g.addEdge("Edge-A", 1, 2); // Note that Java 1.5 auto-boxes primitives
        g.addEdge("Edge-B", 2, 3);


        DelegateTree forest = new DelegateTree();
        forest.setRoot(1);
        forest.addChild("edge-1", 1, 2);
        forest.addChild("edge-2", 1, 3);
        forest.addChild("edge-3", 1, 4);
        forest.addChild("edge-4", 1, 5);


        RadialTreeLayout<Integer, String> layout = new RadialTreeLayout<Integer, String>(forest);

        BasicVisualizationServer<Integer,String> vv = new BasicVisualizationServer<Integer,String>(layout);
        chamomilePanel.add(vv);


        add(chamomilePanel, BorderLayout.CENTER);
    }
}
