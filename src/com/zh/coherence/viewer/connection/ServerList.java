package com.zh.coherence.viewer.connection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 12.03.12
 * Time: 21:32
 */
@XmlRootElement(name = "server-list")
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerList {
    @XmlElement(name = "server")
    private List<ServerConfig> list = new LinkedList<ServerConfig>();

    public List<ServerConfig> getList() {
        return list;
    }

    public void addServerConfig(ServerConfig config){
        if(list.size() > 10){
            list.remove(list.size() - 1);
        }
        if(list.contains(config)){
            list.remove(config);
        }
        list.add(0, config);
    }
}
