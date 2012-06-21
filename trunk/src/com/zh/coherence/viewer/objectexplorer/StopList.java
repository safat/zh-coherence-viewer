package com.zh.coherence.viewer.objectexplorer;

import java.sql.Timestamp;
import java.util.HashSet;
import java.util.Set;

public class StopList {
    public static Set<Class> STOP_LIST = new HashSet<Class>();

    static{
        STOP_LIST.add(Number.class);
        STOP_LIST.add(String.class);
        STOP_LIST.add(Boolean.class);
        STOP_LIST.add(Integer.class);
        STOP_LIST.add(Float.class);
        STOP_LIST.add(Long.class);
        STOP_LIST.add(Timestamp.class);
        STOP_LIST.add(java.util.Date.class);
        STOP_LIST.add(java.sql.Date.class);
        STOP_LIST.add(java.util.Calendar.class);
    }
    
    public static boolean isStopped(Object obj){
        if(obj == null) return true;
        if(STOP_LIST.contains(obj.getClass())) return true;
//        if(obj.getClass().isArray()) return true;
        if(obj.getClass().isPrimitive()) return true;
        return false;
    }
}
