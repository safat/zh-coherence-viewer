package com.zh.coherence.viewer.objectexplorer;

import com.zh.coherence.viewer.objectexplorer.viewer.Viewer;
import org.jdesktop.swingx.renderer.*;

public class TreeStringValue implements StringValue {
    @Override
    public String getString(Object parent) {
        if (parent != null && parent instanceof Viewer) {
            return ((Viewer)parent).getNodeName();
        }else {
            return null;
        }
    }
}
