package com.zh.coherence.viewer.tools.statistic.report;

import com.zh.coherence.viewer.jmx.JMXManager;
import com.zh.coherence.viewer.tools.statistic.report.cache.CacheInfo;

import javax.management.MBeanServerConnection;
import javax.management.ObjectName;
import java.util.*;

public class CacheReport {

    private Map<String, CacheInfo> data = new HashMap<String, CacheInfo>();

    private long totalUnits = 0;

    public void updateData() {
        try{
            data.clear();

            MBeanServerConnection server = JMXManager.getInstance().getServer();
            Set<String> ret = new HashSet<String>();
            Set<ObjectName> cacheNamesSet = server.queryNames(new ObjectName("Coherence:type=Cache,*"), null);
            for (Object aCacheNamesSet : cacheNamesSet) {
                ObjectName cacheNameObjName = (ObjectName) aCacheNamesSet;
                ret.add(cacheNameObjName.getKeyProperty("name"));
                System.err.println("size: " + server.getAttribute(cacheNameObjName, "Size"));
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
    }
}
