package com.zh.coherence.viewer.objectexplorer;

import com.zh.coherence.viewer.objectexplorer.viewer.Viewer;

import javax.swing.tree.TreePath;
import java.lang.reflect.Method;
import java.util.Map;

public class QueryPathBuilder {
    public String buildQuery(TreePath path) {
        StringBuilder sb = new StringBuilder();
        Viewer viewer;
        Object parent = null;
        for (Object o : path.getPath()) {
            viewer = (Viewer) o;
            if (parent != null) {
                String name = viewer.getName();
                if (parent instanceof Map) {
                    sb.append(".get(").append(wrapObject(name)).append(")");
                    parent = viewer.getSubject();
                } else if (parent.getClass().isArray()) {
                    parent = null;
                } else {
                    String checkedMethodName;
                    if (name.length() > 1) {
                        checkedMethodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                    } else {
                        checkedMethodName = "get" + name.substring(0, 1).toUpperCase();
                    }
                    boolean getter = false;
                    for (Method method : parent.getClass().getDeclaredMethods()) {
                        if (checkedMethodName.equals(method.getName())) {
                            getter = true;
                            break;
                        }
                    }
                    sb.append(getter ? "." + name : "." + checkedMethodName + "()");
                    parent = viewer.getSubject();
                }
            }else {
                parent = viewer.getSubject();
            }
        }

        // == (equals)
        if (parent instanceof Integer) {
            sb.append(" = ").append(parent);
        } else if (parent instanceof Float) {
            sb.append(" = ").append(parent).append('F');
        } else if (parent instanceof String) {
            sb.append(" like \"").append(parent).append("\"");
        }
        if (parent instanceof Double) {
            sb.append(" = ").append(parent);
        } else if (parent instanceof Long) {
            sb.append(" = ").append(parent).append('L');
        }

        return sb.toString();
    }

    public String wrapObject(Object value) {
        String ret = null;
        if (value instanceof Integer) {
            ret = String.valueOf(value);
        } else if (value instanceof Float) {
            ret = value + "F";
        } else if (value instanceof String) {
            ret = "\"" + value + "\"";
        } else if (value instanceof Double) {
            ret = value + "D";
        } else if (value instanceof Long) {
            ret = value + "L";
        }

        return ret;
    }
}
