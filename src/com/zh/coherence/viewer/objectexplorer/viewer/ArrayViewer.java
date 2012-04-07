package com.zh.coherence.viewer.objectexplorer.viewer;

import java.lang.reflect.Array;

public class ArrayViewer implements Viewer {
    private String name;
    private Object subject;

    public ArrayViewer(String name, Object subject) {
        this.name = name;
        this.subject = subject;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Object getSubject() {
        return subject;
    }

    public void setSubject(Object subject) {
        this.subject = subject;
    }

    @Override
    public String getText() {
        StringBuilder builder = new StringBuilder();
        builder.append("Array type: \"");
        if (subject == null) {
            builder.append("NULL");
        } else {
            builder.append(subject.getClass().getName());
            builder.append("\"");
            builder.append("\nSize: ").append(Array.getLength(subject));
        }

        builder.append("\n\n");
        if (subject == null) {
            builder.append("NULL");
        } else {
            builder.append("[");
            for(int i = 0, s = Array.getLength(subject), z = s - 1; i < s; i++){
                builder.append(Array.get(subject, i));
                if(i != z){
                      builder.append(", ");
                }
            }
            builder.append("]");
        }

        return builder.toString();
    }

    @Override
    public String getNodeName() {
        if (subject == null) {
            return name + " : NULL";
        } else {
            return name + " : " + subject.getClass().getSimpleName();
        }
    }

    @Override
    public String toString() {
        return getNodeName();
    }
}
