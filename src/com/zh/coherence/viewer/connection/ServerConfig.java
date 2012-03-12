package com.zh.coherence.viewer.connection;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 12.03.12
 * Time: 21:33
 */
@XmlType
@XmlAccessorType(XmlAccessType.FIELD)
public class ServerConfig {
    @XmlAttribute
    private String name;
    @XmlAttribute
    private String host;
    @XmlAttribute
    private int port;
    @XmlAttribute
    private String jmxUrl;
    @XmlAttribute
    private boolean ignoreUserPof = false;

    public ServerConfig() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getJmxUrl() {
        return jmxUrl;
    }

    public void setJmxUrl(String jmxUrl) {
        this.jmxUrl = jmxUrl;
    }

    public String toString(){
        return name;
    }

    public boolean isIgnoreUserPof() {
        return ignoreUserPof;
    }

    public void setIgnoreUserPof(boolean ignoreUserPof) {
        this.ignoreUserPof = ignoreUserPof;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ServerConfig config = (ServerConfig) o;

        if (ignoreUserPof != config.ignoreUserPof) return false;
        if (port != config.port) return false;
        if (host != null ? !host.equals(config.host) : config.host != null) return false;
        if (jmxUrl != null ? !jmxUrl.equals(config.jmxUrl) : config.jmxUrl != null) return false;
        if (name != null ? !name.equals(config.name) : config.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + port;
        result = 31 * result + (jmxUrl != null ? jmxUrl.hashCode() : 0);
        result = 31 * result + (ignoreUserPof ? 1 : 0);
        return result;
    }
}
