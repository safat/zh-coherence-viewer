package com.zh.coherence.viewer.pof;

import com.tangosol.io.ReadBuffer;
import com.tangosol.io.pof.PofBufferReader;
import com.tangosol.io.pof.PofReader;
import com.tangosol.io.pof.PofSerializer;
import com.tangosol.io.pof.PofWriter;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 18.02.12
 * Time: 0:21
 */
public class StubPofSerializer implements PofSerializer{
    @Override
    public void serialize(PofWriter pofWriter, Object o) throws IOException {
        if(pofWriter.getUserTypeId() > 1000){
            ValueContainer vc = (ValueContainer) o;
            PofEncoder encoder = new PofEncoder();
            encoder.encode(pofWriter, vc);
        }

        pofWriter.writeRemainder(null);
    }

    @Override
    public Object deserialize(PofReader pofReader) throws IOException {
        PofBufferReader reader = (PofBufferReader) pofReader;
        int type = pofReader.getUserTypeId();
        ValueContainer container = null;
        try {
            Class clazz = reader.getClass().getSuperclass();
            Field ff =  clazz.getDeclaredField("m_in");
            ff.setAccessible(true);
            ReadBuffer.BufferInput input = (ReadBuffer.BufferInput) ff.get(reader);
            PofDecoder decoder = new PofDecoder();
            decoder.setPofContext(pofReader.getPofContext());
            container = decoder.decode(input, type);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return container;
    }
}
