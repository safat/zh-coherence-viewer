package com.zh.coherence.viewer.pof;

import com.tangosol.io.ReadBuffer;
import com.tangosol.io.pof.*;

import java.io.IOException;
import java.lang.reflect.Field;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 18.02.12
 * Time: 0:21
 */
public class StubPofSerializer implements PofSerializer{
    private PofSerializer parentSerializer;
    private PofDecoder decoder;

    public StubPofSerializer(PofSerializer parentSerializer) {
        this.parentSerializer = parentSerializer;
        decoder = new PofDecoder();
    }

    @Override
    public void serialize(PofWriter pofWriter, Object o) throws IOException {
        if(parentSerializer != null){
            parentSerializer.serialize(pofWriter, o);
        }else if(o instanceof ValueContainer){
            ValueContainer vc = (ValueContainer) o;
            PofEncoder encoder = new PofEncoder();
            encoder.encode(pofWriter, vc);
            pofWriter.writeRemainder(null);
        }
    }

    @Override
    public Object deserialize(PofReader pofReader) throws IOException {
        PofBufferReader reader = (PofBufferReader) pofReader;
        int type = pofReader.getUserTypeId();
        try {
            if(parentSerializer != null){
                return parentSerializer.deserialize(pofReader);
            }else{
                Class clazz = reader.getClass().getSuperclass();
                Field ff =  clazz.getDeclaredField("m_in");
                ff.setAccessible(true);
                ReadBuffer.BufferInput input = (ReadBuffer.BufferInput) ff.get(reader);
                ValueContainer container = new ValueContainer();
                decoder.fillBinary(input, container);
                decoder.setPofContext(pofReader.getPofContext());
                decoder.decode(input, type, container);
                return container;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
}
