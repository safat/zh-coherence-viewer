package com.zh.coherence.viewer.objectexplorer.config;

import javax.xml.bind.annotation.*;
import java.util.List;
import java.util.Set;

@XmlRootElement(name = "config")
@XmlAccessorType(XmlAccessType.NONE)
public class OEConfigDataKeeper {

    @XmlElement
    private boolean primitives;

    @XmlElement
    private boolean enums;

    @XmlElement
    private boolean array;

    @XmlElement
    private boolean anonymous;

    @XmlElement
    private boolean synthetic;

    @XmlElement(name = "class")
    @XmlElementWrapper(name = "classes")
    private List<String> localClasses;

    @XmlElement
    private int arrayLimit = 100;

    private Set<Class> classes;

    public boolean isAnonymous() {
        return anonymous;
    }

    public void setAnonymous(boolean anonymous) {
        this.anonymous = anonymous;
    }

    public boolean isArray() {
        return array;
    }

    public void setArray(boolean array) {
        this.array = array;
    }

    public Set<Class> getClasses() {
        return classes;
    }

    public void setClasses(Set<Class> classes) {
        this.classes = classes;
    }

    public List<String> getLocalClasses() {
        return localClasses;
    }

    public void setLocalClasses(List<String> localClasses) {
        this.localClasses = localClasses;
    }

    public boolean isEnums() {
        return enums;
    }

    public void setEnums(boolean enums) {
        this.enums = enums;
    }

    public boolean isPrimitives() {
        return primitives;
    }

    public void setPrimitives(boolean primitives) {
        this.primitives = primitives;
    }

    public boolean isSynthetic() {
        return synthetic;
    }

    public void setSynthetic(boolean synthetic) {
        this.synthetic = synthetic;
    }

    public int getArrayLimit() {
        return arrayLimit;
    }

    public void setArrayLimit(int arrayLimit) {
        this.arrayLimit = arrayLimit;
    }
}
