package com.zh.coherence.viewer.pof;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Collection;
import java.util.Date;
import java.util.Map;

import com.tangosol.io.ReadBuffer;
import com.tangosol.io.Serializer;
import com.tangosol.io.WriteBuffer;
import com.tangosol.io.pof.PofBufferReader;
import com.tangosol.io.pof.PofBufferWriter;
import com.tangosol.io.pof.PofContext;
import com.tangosol.io.pof.PofHelper;
import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PortableObject;
import com.tangosol.io.pof.RawDate;
import com.tangosol.io.pof.RawDateTime;
import com.tangosol.io.pof.RawDayTimeInterval;
import com.tangosol.io.pof.RawQuad;
import com.tangosol.io.pof.RawTime;
import com.tangosol.io.pof.RawTimeInterval;
import com.tangosol.io.pof.RawYearMonthInterval;
import com.tangosol.util.Binary;
import com.tangosol.util.LongArray;
import com.tangosol.util.NullImplementation;
import com.zh.coherence.viewer.pof.xml.XmlPofConfig;
import com.zh.coherence.viewer.pof.xml.XmlPofContextReader;
import com.zh.coherence.viewer.tools.backup.MapPersisterTask;

public class ZhPofContext implements Serializer, PofContext {
    private XmlPofConfig pofConfig;

    public ZhPofContext() {
        String ignoreUserPof = System.getProperty("zh.coherence.viewer.ignoreUserPof");
        if (ignoreUserPof == null || !Boolean.parseBoolean(ignoreUserPof)) {
            pofConfig = new XmlPofContextReader().readXmlPofConfig("/zh-pof-config.xml");
        } else {
            pofConfig = new XmlPofContextReader().readXmlPofConfig("coherence-pof-config.xml");
        }
    }

    @Override
    public PofSerializer getPofSerializer(int i) {
        return pofConfig.getPofSerializerById(i);
    }

    @Override
    public int getUserTypeIdentifier(Object o) {
        if (o instanceof ValueContainer) {
            return ((ValueContainer) o).getTypeId();
        } else {
            return getUserTypeIdentifier(o.getClass());
        }
    }

    @Override
    public int getUserTypeIdentifier(Class clazz) {
        Integer type = pofConfig.getTypeByClass(clazz);
        return type != null ? type : Integer.MAX_VALUE;
    }

    @Override
    public int getUserTypeIdentifier(String s) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getClassName(int type) {
        Class clazz = pofConfig.getClassByType(type);
        return clazz != null ? clazz.getName() : Object.class.getName();
    }

    @Override
    public Class getClass(int type) {
        Class clazz = pofConfig.getClassByType(type);
        return clazz != null ? clazz : Object.class;
    }

    @Override
    public boolean isUserType(Object o) {
        return isUserType(o.getClass());
    }

    @Override
    public boolean isUserType(Class clazz) {
        Integer type = PofHelper.getPofTypeId(clazz, this);
        return type == null ? !PofHelper.isIntrinsicPofType(clazz) : type > 0;
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
        } catch (RuntimeException ex) {
            throw new IOException(ex.getMessage(), ex);
        }
    }

    @Override
    public Object deserialize(ReadBuffer.BufferInput bufferInput) throws IOException {
        PofBufferReader reader = new PofBufferReader(bufferInput, this);
        try {
            return reader.readObject(-1);
        } catch (RuntimeException ex) {
            throw new IOException(ex.getMessage(), ex);
        }
    }

    public XmlPofConfig getPofConfig() {
        return pofConfig;
    }
}