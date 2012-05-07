package com.zh.coherence.viewer.tools;

import com.zh.coherence.viewer.utils.icons.IconType;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class ToolDescription {
    @XmlAttribute
    private String name;

    @XmlAttribute
    private IconType icon;

    @XmlAttribute(name = "class")
    private String clazz;

    public IconType getIcon() {
        return icon;
    }

    public String getName() {
        return name;
    }

    public String getClazz() {
        return clazz;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("ToolDescription");
        sb.append("{clazz='").append(clazz).append('\'');
        sb.append(", name='").append(name).append('\'');
        sb.append(", icon=").append(icon);
        sb.append('}');
        return sb.toString();
    }
}
