package com.zh.coherence.viewer.tools.backup.networkchart;


import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class NetworkChartModel {

    private List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    private LinkedList<Integer> values = new LinkedList<Integer>();

    private Integer max = 0;

    private long sum = 0;

    public void addValue(Integer number){
        if(values.size() > 0){
            int prev = values.getLast();
            values.add((number + prev) / 2);
        }else{
            values.add(number);
        }
        if(number > max){
            max = number;
        }
        sum += number;
        if(values.size() > 400){
            int first = values.getFirst();
            if(first == max){
                //search new max
                max = 0;
                for(Integer i : values){
                    if(i > max){
                        max = i;
                    }
                }
            }
            values.removeFirst();
        }
    }

    public void fireChartChanges(){
        for(ChangeListener listener : listeners){
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    public LinkedList<Integer> getValues() {
        return values;
    }

    public Integer getMax() {
        return max;
    }

    public void addListener(ChangeListener listener){
        listeners.add(listener);
    }

    public long getSum() {
        return sum;
    }
}
