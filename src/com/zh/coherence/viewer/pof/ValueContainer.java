package com.zh.coherence.viewer.pof;

import com.zh.coherence.viewer.pof.printer.Printer;
import com.zh.coherence.viewer.pof.printer.ValueContainerPrinterFactory;

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
    private List<Object> values = new ArrayList<Object>();

    private Object userValue;

    private int typeId;

    public int size() {
        return values.size();
    }

    public boolean add(Object o) {
        return values.add(o);
    }

    public Object get(int index) {
        return values.get(index);
    }

    public Object set(int index, Object element) {
        return values.set(index, element);
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

    public List<Object> getValues() {
        return values;
    }

    public void setValues(List<Object> values) {
        this.values = values;
    }

    @Override
    public String toString() {
        Printer printer = ValueContainerPrinterFactory.getInstance().getPrinter(typeId);
        if (printer != null) {
            return printer.print(this);
        } else {
            if (userValue != null) {
                return userValue.toString();
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append(typeId).append("{");
                for (Object obj : values) {
                    sb.append(obj).append(",");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append("}");

                return sb.toString();
            }
        }
    }
}
