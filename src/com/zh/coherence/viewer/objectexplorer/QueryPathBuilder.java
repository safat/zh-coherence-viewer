package com.zh.coherence.viewer.objectexplorer;

import com.zh.coherence.viewer.objectexplorer.viewer.Viewer;

import javax.swing.tree.TreePath;
import java.lang.reflect.Method;
import java.util.*;

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
                } else if (parent.getClass().isArray() || parent instanceof Set) {
                    break;
                } else if (parent instanceof List) {
                    List parentList = (List) parent;
                    int idx = parentList.indexOf(viewer.getSubject());
                    sb.append(".get(").append(idx).append(")");
                    parent = viewer.getSubject();
                } else {
                    String checkedMethodName;
                    if (name.length() > 1) {
                        checkedMethodName = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                    } else {
                        checkedMethodName = "get" + name.substring(0, 1).toUpperCase();
                    }
                    String methodName = null;
                    for (Method method : getDeclaredMethods(parent)) {
                        if (checkedMethodName.equals(method.getName())) {
                            methodName = name;
                            break;
                        } else if (name.equals(method.getName())) {
                            methodName = name + "()";
                        }
                    }
                    if (methodName == null) {
                        break;
                    }
                    sb.append(".").append(methodName);
                    parent = viewer.getSubject();
                }
            } else {
                parent = viewer.getSubject();
            }
        }

        // == (equals)
        if (parent == null) {
            sb.append(" is null");
        } else if (parent instanceof Integer) {
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

    private List<Method> getDeclaredMethods(Object obj) {
        List<Method> fields = new ArrayList<Method>();
        Class clazz = obj.getClass();
        fields.addAll(Arrays.asList(clazz.getDeclaredMethods()));
        while (true) {
            clazz = clazz.getSuperclass();
            if (clazz != null) {
                fields.addAll(Arrays.asList(clazz.getDeclaredMethods()));
            } else {
                break;
            }
        }
        return fields;
    }
}
