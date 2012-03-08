package com.zh.coherence.viewer.pof.xml;

import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.PortableObjectSerializer;
import com.zh.coherence.viewer.pof.StubPofSerializer;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 08.03.12
 * Time: 2:25
 */
public class XmlPofConfig {
    private Map<Integer, PofSerializer> typeToSerializerMap = new HashMap<Integer, PofSerializer>();
    private Map<Integer, Class> typeToSerializerClassMap = new HashMap<Integer, Class>();
    private Map<Integer, Class> typeToClassMap = new HashMap<Integer, Class>();
    private Map<Class, Integer> classToTypeMap = new HashMap<Class, Integer>();

    private StubPofSerializer stubPofSerializer = new StubPofSerializer(null);

    public boolean containsType(int type){
        return typeToSerializerMap.containsKey(type);
    }

    public PofSerializer getPofSerializerById(int type){
        PofSerializer serializer = typeToSerializerMap.get(type);
        if(serializer == null){
            try{
                Class clazz = typeToSerializerClassMap.get(type);

                if(clazz != null){
                    if(PortableObject.class.isAssignableFrom(clazz)){
                        serializer = new StubPofSerializer(new PortableObjectSerializer(type));
                    }else{
                        serializer = new StubPofSerializer((PofSerializer) clazz.newInstance());
                    }
                }else{
                    serializer = stubPofSerializer;
                }
                typeToSerializerMap.put(type, serializer);
            }catch (Exception ex){
                ex.printStackTrace();
            }
        }
        return serializer;
    }

    public Integer getTypeByClass(Class clazz){
        return classToTypeMap.get(clazz);
    }

    public Class getClassByType(int type){
        return typeToClassMap.get(type);
    }

    public void add(XmlUserType userType){
        try {
            Class clazz = Class.forName(userType.clazz);
            Class serializer;
            if(userType.serializer != null){
                serializer = Class.forName(userType.serializer);
            }else{
                serializer = clazz;
            }

            typeToSerializerMap.put(userType.type, null);
            typeToClassMap.put(userType.type, clazz);
            classToTypeMap.put(clazz, userType.type);
            typeToSerializerClassMap.put(userType.type, serializer);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
