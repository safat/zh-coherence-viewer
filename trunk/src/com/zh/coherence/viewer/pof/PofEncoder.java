package com.zh.coherence.viewer.pof;

import com.tangosol.io.pof.PofWriter;

import java.io.IOException;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 21.02.12
 * Time: 0:52
 */
public class PofEncoder {
    public void encode(PofWriter writer, ValueContainer container){
        for(int i = 0; i < container.size(); i++){
            try {
                writer.writeObject(i, container.get(i));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    
//    private void write(int type, PofWriter writer, int idx, Object value){
//        switch (type){
//            case -2:
//                writer.writeInt(idx, );
//        }
//    }
}
