package com.zh.coherence.viewer.tableview.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class UserViewerItem {

    @XmlAttribute
    private UserObjectViewer.Target target;

    @XmlAttribute(name = "class")
    private String clazz;

    @XmlAttribute(name = "viewer")
    private String renderer;

    @XmlAttribute
    private String icon;

    @XmlAttribute
    private String name;

    public UserViewerItem() {
    }

    public UserViewerItem(String clazz, String renderer, UserObjectViewer.Target target) {
        this.clazz = clazz;
        this.renderer = renderer;
        this.target = target;
    }

    public String getClazz() {
        return clazz;
    }

    public void setClazz(String clazz) {
        this.clazz = clazz;
    }

    public String getRenderer() {
        return renderer;
    }

    public void setRenderer(String renderer) {
        this.renderer = renderer;
    }

    public UserObjectViewer.Target getTarget() {
        return target;
    }

    public void setTarget(UserObjectViewer.Target target) {
        this.target = target;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
