package com.zh.coherence.viewer.userclassmanager;

import com.zh.coherence.viewer.utils.icons.IconHelper;
import com.zh.coherence.viewer.utils.icons.IconType;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 26.05.12
 * Time: 18:50
 */
public class UserClassViewerTableModel extends AbstractTableModel {
    private Class clazz;

    private List<MethodDescription> list = new ArrayList<MethodDescription>();

    private String headers[] = new String[]{"", "Method name", "Return Type"};

    private Icon icon = IconHelper.getInstance().getIcon(IconType.ASTERISK);

    @Override
    public String getColumnName(int column) {
        return headers[column];
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex == 0) {
            return Icon.class;
        }
        return super.getColumnClass(columnIndex);
    }

    @Override
    public int getRowCount() {
        if (clazz == null) {
            return 0;
        }
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return headers.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        switch (columnIndex) {
            case 0:
                return list.get(rowIndex).isGetter ? icon : null;
            case 1:
                return list.get(rowIndex).name;
            case 2:
                return list.get(rowIndex).returnType;
            default:
                return "";
        }
    }

    public void setClazz(Class clazz) {
        this.clazz = clazz;
        list.clear();
        if (clazz != null) {
            HashSet<MethodDescription> target = new HashSet<MethodDescription>();
            update(clazz, target);
            list.addAll(target);
        }
        fireTableDataChanged();
    }

    private void update(Class clz, Set<MethodDescription> target) {
        for (Method m : clz.getMethods()) {
            boolean getter = isGetter(m);
            if (isSupported(m)) {
                target.add(new MethodDescription(getter, m.getName(), m.getReturnType().toString()));
            }
        }
        if(clz.getSuperclass() != null){
            update(clz.getSuperclass(), target);
        }
    }

    public boolean isGetter(Method method) {
        return method.getName().startsWith("get");
    }

    public boolean isSupported(Method method) {
        return method.getParameterTypes().length == 0 && !void.class.equals(method.getReturnType());
    }

    private class MethodDescription {
        public boolean isGetter;
        public String name;
        public String returnType;

        private MethodDescription(boolean getter, String name, String returnType) {
            isGetter = getter;
            this.name = name;
            this.returnType = returnType;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            MethodDescription that = (MethodDescription) o;

            return !(name != null ? !name.equals(that.name) : that.name != null);

        }

        @Override
        public int hashCode() {
            return name != null ? name.hashCode() : 0;
        }
    }
}
