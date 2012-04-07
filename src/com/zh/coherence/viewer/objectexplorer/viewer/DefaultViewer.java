package com.zh.coherence.viewer.objectexplorer.viewer;

public class DefaultViewer implements Viewer {
    private String name;
    private Object subject;

    public DefaultViewer(String name, Object subject) {
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
        builder.append("name: \"");
        if (subject == null) {
            builder.append("NULL");
        } else {
            builder.append(subject.getClass().getName());
        }
        builder.append("\"");
        builder.append("\n\n");
        if (subject == null) {
            builder.append("NULL");
        } else {
            builder.append(subject.toString());
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
