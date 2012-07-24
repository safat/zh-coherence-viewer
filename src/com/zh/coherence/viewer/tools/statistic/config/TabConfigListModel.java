package com.zh.coherence.viewer.tools.statistic.config;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TabConfigListModel extends AbstractListModel {

    private List<TabContainer> list = null;

    @Override
    public int getSize() {
        return list == null || list.isEmpty() ? 0 : list.size();
    }

    @Override
    public Object getElementAt(int index) {
        if (list != null) {
            return list.get(index);
        } else {
            return null;
        }
    }

    public List<TabContainer> getList() {
        return list;
    }

    public void setList(List<TabContainer> list) {
        this.list = list;
    }

    public void addTab(TabContainer container) {
        if (list == null) {
            list = new ArrayList<TabContainer>();
        }
        list.add(container);
        fireIntervalAdded(this, list.size() - 1, list.size() - 1);
    }

    public void removeTabs(Object[] containers) {
        if (containers != null && containers.length > 0) {
            list.removeAll(Arrays.asList(containers));
            fireContentsChanged(this, 0, list.size() - 1);
        }
    }

    public int[] moveContainersUp(Object[] containers) {
        List<Integer> indexes = new ArrayList<Integer>();
        if (containers != null && containers.length > 0) {
            for (Object obj : containers) {
                int idx = list.indexOf(obj);
                if (idx > 0) {
                    TabContainer container = list.remove(idx);
                    list.add(idx - 1, container);
                    indexes.add(idx - 1);
                }
            }
            fireContentsChanged(this, 0, list.size() - 1);
        }
        int[] ret = new int[indexes.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = indexes.get(i);
        }
        return ret;
    }

    public int[] moveContainersDown(Object[] containers) {
        List<Integer> indexes = new ArrayList<Integer>();

        if (containers != null && containers.length > 0) {
            for (int i = containers.length -1; i >= 0; i --) {
                Object obj = containers[i];
                int idx = list.indexOf(obj);
                if (idx != -1 && idx != list.size() - 1) {
                    TabContainer container = list.remove(idx);
                    list.add(idx + 1, container);
                    indexes.add(idx + 1);
                }
            }
            fireContentsChanged(this, 0, list.size() - 1);
        }

        int[] ret = new int[indexes.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = indexes.get(i);
        }
        return ret;
    }
}
