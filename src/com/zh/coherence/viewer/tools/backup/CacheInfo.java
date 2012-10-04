package com.zh.coherence.viewer.tools.backup;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class CacheInfo {
    public enum Status {PENDING, WARN, ERROR, PROCESSED}

    @XmlAttribute
    private String name;

    @XmlAttribute
    private boolean enabled = true;

    private boolean processed = false;

    private Status status;

    @XmlElement
    private BackupFilter filter = new BackupFilter();

    public CacheInfo(String name) {
        this.name = name;
    }

    public CacheInfo() {
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CacheInfo cacheInfo = (CacheInfo) o;

        return !(name != null ? !name.equals(cacheInfo.name) : cacheInfo.name != null);
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public BackupFilter getFilter() {
        return filter;
    }

    public void setFilter(BackupFilter filter) {
        this.filter = filter;
    }

    public String getName() {
        return name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
