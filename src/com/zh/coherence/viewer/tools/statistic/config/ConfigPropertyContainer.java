package com.zh.coherence.viewer.tools.statistic.config;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.FIELD)
public class ConfigPropertyContainer {

    @XmlElement(name = "property")
    private List<ConfigProperty> properties = new ArrayList<ConfigProperty>();

    public String[] getFilteredNames(String target){
        List<String> list = new ArrayList<String>();
        for(ConfigProperty property : properties){
            if(property.getTarget().equals(target)){
                list.add(property.getName());
            }
        }

        return list.toArray(new String[list.size()]);
    }

    public String getDescription(String name){
        for(ConfigProperty property : properties){
            if(property.getName().equals(name)){
                return property.getDescription();
            }
        }

        return "";
    }

    public List<ConfigProperty> getProperties() {
        return properties;
    }

    public void setProperties(List<ConfigProperty> properties) {
        this.properties = properties;
    }
}
