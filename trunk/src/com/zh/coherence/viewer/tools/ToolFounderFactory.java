package com.zh.coherence.viewer.tools;

import java.util.HashMap;
import java.util.Map;

public class ToolFounderFactory {
    private static Map<String, ToolFounder> founders = new HashMap<String, ToolFounder>();

    public static ToolFounder getToolFounder(ToolDescription description){
        if(description == null || description.getClazz() == null){
            throw new IllegalArgumentException("description: '" + description + "'");
        }

        String clazz = description.getClazz();

        ToolFounder ret = founders.get(clazz);
        if (ret == null) {
            try {
                ret = (ToolFounder) Class.forName(clazz).newInstance();
                ret.setDescription(description);
                founders.put(clazz, ret);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return ret;
    }
}
