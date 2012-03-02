package com.zh.coherence.viewer.pof;

import com.tangosol.io.ReadBuffer;
import com.tangosol.io.Serializer;
import com.tangosol.io.WriteBuffer;
import com.tangosol.io.pof.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 18.02.12
 * Time: 0:13
 */
public class ZhPofContext implements Serializer, PofContext {
    private ConfigurablePofContext context;
    private volatile Map<Class<?>, SerializationContext> classMap;
    private volatile SerializationContext[] typeIdMap = new SerializationContext[1001];

    public ZhPofContext() {
        context = new ConfigurablePofContext();
        initContextMap();
    }

    @Override
    public PofSerializer getPofSerializer(int i) {
        PofSerializer ret;
        if(i <= 1000){
            ret = context.getPofSerializer(i);
        } else {
            ret = new StubPofSerializer();
        }
        
        return ret;
    }

    @Override
    public int getUserTypeIdentifier(Object o) {
        if(o instanceof ValueContainer){
            return ((ValueContainer)o).getTypeId();
        }else{
            return getUserTypeIdentifier(o.getClass());
        }
    }

    @Override
    public int getUserTypeIdentifier(Class aClass) {
        SerializationContext ctx = classMap.get(aClass);
        if(ctx != null){
            return ctx.pofId;
        }else{
            return Integer.MAX_VALUE;
        }
    }

    @Override
    public int getUserTypeIdentifier(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getClassName(int i) {
        if(i < 1000) {
            return typeIdMap[i].type.getName();
        }
        System.err.println("UNKNOWN class name for : " + i);
        return "name";
    }

    @Override
    public Class getClass(int i) {
        if(i < 1000) {
            return typeIdMap[i].type;
        } else{
            System.err.println("ASC class from id: " + i);
            return "".getClass();
        }
    }

    @Override
    public boolean isUserType(Object o) {
        return isUserType(o.getClass());
    }

    @Override
    public boolean isUserType(Class aClass) {
        SerializationContext ctx = classMap.get(aClass);
        if(ctx != null){
            return false;
        }
        return true;
    }

    @Override
    public boolean isUserType(String s) {
        return true;
    }

    @Override
    public void serialize(WriteBuffer.BufferOutput bufferOutput, Object o) throws IOException {
        PofBufferWriter writer = new PofBufferWriter(bufferOutput, this);
        try {
            writer.writeObject(-1, o);
        } catch (RuntimeException e) {
            IOException ioex = new IOException(e.getMessage());

            ioex.initCause(e);
            throw ioex;
        }
    }

    @Override
    public Object deserialize(ReadBuffer.BufferInput bufferInput) throws IOException {
        PofBufferReader reader = new PofBufferReader(bufferInput, this);
        try {
            return reader.readObject(-1);
        } catch (RuntimeException e) {
            IOException ioex = new IOException(e.getMessage());
            ioex.initCause(e);
            throw ioex;
        }
    }

    private synchronized void initContextMap() {
        Map<Class<?>, SerializationContext> map = new HashMap<Class<?>, SerializationContext>();
        for(int i = 0; i != 1000; ++i) {
            try {
                PofSerializer serializer = context.getPofSerializer(i);
                Class<?> cls = context.getClass(i);
                SerializationContext ctx = new SerializationContext();
                ctx.pofId = i;
                ctx.type = cls;
                ctx.serializer = serializer;
                map.put(cls, ctx);
                typeIdMap[i] = ctx;
            }
            catch(IllegalArgumentException e) {
                continue;
            }
        }
        classMap = map;
    }

    private SerializationContext contextById(int id) {
        if(id < 1000) {
            return typeIdMap[id];
        } else {
            return null;
        }
    }
}

/*
* PofSerializer serializer;
		if (PortableObject.class.isAssignableFrom(cls)) {
			serializer = new PortableObjectSerializer(userType);
		}
		else {
			try {
				serializer = autoSerializer.getClassCodec(cls);
			} catch (IOException e) {
				throw new IllegalArgumentException(e.getMessage(), e.getCause());
			}
		}

		registerSerializationContext(userType, cls, serializer);
* */