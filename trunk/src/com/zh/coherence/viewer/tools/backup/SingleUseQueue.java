package com.zh.coherence.viewer.tools.backup;

import com.tangosol.io.WrapperBufferInput;
import com.tangosol.io.WrapperBufferOutput;
import com.tangosol.util.Binary;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SingleUseQueue {
    private File file;
    private WrapperBufferInput ins;
    private int size;
    private boolean endReached;

    public SingleUseQueue() throws IOException {
        file = File.createTempFile("cacheViewer_", "");
        ins = null;
        size = 0;
        endReached = false;
    }

    public int size() {
        return size;
    }

    public void populate(Collection<Binary> c) throws IOException {
        if(ins != null){
            throw new IllegalArgumentException("Queue can be initialized only once.");
        }
        WrapperBufferOutput buf = null;
        try{
            buf = new WrapperBufferOutput(new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file))));
            for(Binary b : c){
                byte[] byteArray = b.toByteArray();
                buf.writeInt(byteArray.length);
                buf.write(byteArray);
                size++;
            }
        }finally {
            buf.writeInt(-1);
            buf.close();
        }
        ins = new WrapperBufferInput((new DataInputStream(new BufferedInputStream(new FileInputStream(file)))));
        if(ins.markSupported()){
            ins.mark(5 * 1024 * 1024);
        }else{
            System.out.println("Mark is unsupported");
        }
    }

    public synchronized Binary poll() throws IOException{
        if(ins != null && !endReached){
            int readInt = ins.readInt();
            if(readInt == -1){
                endReached = true;
                return null;
            }else{
                byte[] ab = new byte[readInt];
                ins.read(ab);
                return new Binary(ab);
            }
        }
        return null;
    }

    public List<Binary> poll(int amount) throws IOException{
        List<Binary> list = new ArrayList<Binary>(amount);
        for(int i = 0; i < amount; i++){
            Binary object = poll();
            if(object != null){
                list.add(object);
            }else{
                break;
            }
        }
        return list;
    }

    public synchronized void release() throws IOException{
        if(ins != null){
            ins.close();
            ins = null;
        }
        if(file != null && file.exists()){
            file.delete();
            file = null;
        }
    }

    public void reset() throws IOException{
        if(ins != null){
            ins.reset();
            endReached = false;
        }
    }
}
