package com.zh.coherence.viewer.pof;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 20.02.12
 * Time: 23:11
 */
public class ValueContainer {
    private byte[] binary; //todo create flag, is need keep binary
    private List<Object> value = new ArrayList<Object>();

    private Object userValue;

    private int typeId;

    public int size() {
        return value.size();
    }

    public boolean add(Object o) {
        return value.add(o);
    }

    public Object get(int index) {
        return value.get(index);
    }

    public Object set(int index, Object element) {
        return value.set(index, element);
    }

    public byte[] getBinary() {
        return binary;
    }

    public void setBinary(byte[] binary) {
        this.binary = binary;
    }

    public int getTypeId() {
        return typeId;
    }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public Object getUserValue() {
        return userValue;
    }

    public void setUserValue(Object userValue) {
        this.userValue = userValue;
    }

    @Override
    public String toString() {
        if (userValue != null) {
            return userValue.toString();
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append(typeId).append("{");
            for (Object obj : value) {
                sb.append(obj).append(",");
            }
            sb.deleteCharAt(sb.length() - 1);
            sb.append("}");

            return sb.toString();
        }
    }
}
