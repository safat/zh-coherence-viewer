package com.zh.coherence.viewer.userclassmanager;

import com.zh.coherence.viewer.pof.ZhPofContext;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 26.05.12
 * Time: 18:06
 */
public class UserClassViewer extends JPanel{
    private JXList classes;
    private DefaultListModel classesListModel;

    private JXTable fields;

    private ZhPofContext pofContext;

    private boolean showFullClassName = false;

    public UserClassViewer() {
        init();
    }

    private void init(){
        classesListModel = new DefaultListModel();
        classes = new JXList(classesListModel);

        fields = new JXTable();

        pofContext = new ZhPofContext();

        setLayout(new BorderLayout());
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setDividerLocation(250);
        add(splitPane, BorderLayout.CENTER);

        splitPane.setLeftComponent(new JScrollPane(classes));
        splitPane.setRightComponent(new JScrollPane(fields));

        refresh();
        /*
        * todo
        * add search for classes and functions
        *
        * */
    }

    public void refresh(){
        for(int i : pofContext.getPofConfig().getRegisteredTypes()){
            if(i > 1000){
                String name = showFullClassName?pofContext.getClassName(i):pofContext.getClass(i).getSimpleName();
                classesListModel.addElement(new ClassName(pofContext.getClass(i), i, name));
            }
        }
    }
}
