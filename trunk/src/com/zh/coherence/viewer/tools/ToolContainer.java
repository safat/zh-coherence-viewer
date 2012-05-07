package com.zh.coherence.viewer.tools;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "tools")
@XmlAccessorType(XmlAccessType.FIELD)
public class ToolContainer {

    @XmlElement(name = "tool")
    private List<ToolDescription> tools = new ArrayList<ToolDescription>();

    public List<ToolDescription> getTools() {
        return tools;
    }
}
