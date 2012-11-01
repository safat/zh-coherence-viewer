package com.zh.coherence.viewer.userclassmanager;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 26.05.12
 * Time: 18:52
 */
public class ClassName {
    private Class clazz;
    private String name;
    private int id;

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append(" [").append(id).append(']');
        return sb.toString();
    }

    public ClassName(Class clazz, int id, String name) {
        this.clazz = clazz;
        this.id = id;
        this.name = name;
    }

    public Class getClazz() {
        return clazz;
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
