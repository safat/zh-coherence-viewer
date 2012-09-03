package com.zh.coherence.viewer.tools.backup;

import javax.xml.bind.annotation.*;

@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class BackupFilter {
    public static enum FilterType{QUERY, SHELL}

    @XmlAttribute
    private boolean enabled = false;

    @XmlValue
    private String source = null;

    @XmlAttribute
    private FilterType filterType = FilterType.QUERY;

    public BackupFilter() {
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public FilterType getFilterType() {
        return filterType;
    }

    public void setFilterType(FilterType filterType) {
        this.filterType = filterType;
    }
}
