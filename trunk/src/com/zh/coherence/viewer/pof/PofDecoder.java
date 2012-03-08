package com.zh.coherence.viewer.pof;

import com.tangosol.io.ReadBuffer;
import com.tangosol.io.pof.*;
import com.tangosol.util.Binary;
import com.tangosol.util.ImmutableArrayList;
import com.tangosol.util.LongArray;
import com.tangosol.util.SparseArray;

import java.io.IOException;
import java.io.StreamCorruptedException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 20.02.12
 * Time: 23:06
 */
public class PofDecoder extends PofHelper {

    private PofContext pofContext = null;

    public void fillBinary(ReadBuffer.BufferInput input, ValueContainer container){
        try{
            byte[] binary = new byte[input.available()];
            int offset = input.getOffset();
            input.readFully(binary);
            container.setBinary(binary);
            input.setOffset(offset);
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    public void decode(ReadBuffer.BufferInput input, int type, ValueContainer container) {
        container.setTypeId(type);

        try {
            boolean hasNext = true;
            while (hasNext) {
                int fieldType = input.readPackedInt();
                container.add(decodeType(fieldType, input));
                hasNext = input.readPackedInt() != -1;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private Object decodeType(int type, ReadBuffer.BufferInput input) throws IOException {
        switch (type) {
            case -1:
                return Short.valueOf((short) input.readPackedInt());
            case -64:
            case -63:
            case -62:
            case -61:
            case -60:
            case -59:
            case -58:
            case -57:
            case -56:
            case -55:
            case -54:
            case -53:
            case -52:
            case -51:
            case -50:
            case -49:
            case -48:
            case -47:
            case -46:
            case -45:
            case -44:
            case -43:
            case -42:
            case -41:
            case -2:
                return readAsInt(input, type);
            case -3:
                return Long.valueOf(input.readPackedLong());
            case -4:
                return readBigInteger(input);
            case -5:
                return Float.valueOf(input.readFloat());
            case -6:
                return Double.valueOf(input.readDouble());
            case -7:
                return readQuad(input);
            case -8:
                return readBigDecimal(input, 4);
            case -9:
                return readBigDecimal(input, 8);
            case -10:
                return readBigDecimal(input, 16);
            case -11:
                return input.readPackedInt() == 0 ? Boolean.FALSE : Boolean.TRUE;
            case -12:
                return Byte.valueOf(input.readByte());
            case -13:
                return readBinary(input);
            case -14:
                return Character.valueOf(readChar(input));
            case -15:
                return input.readSafeUTF();
            case -16:
                return readRawDate(input).toSqlDate();
            case -18:
                return readRawTime(input).toSqlTime();
            case -20:
                return new RawDateTime(readRawDate(input), readRawTime(input)).toSqlTimestamp();
            case -17:
                return new RawYearMonthInterval(input.readPackedInt(), input.readPackedInt());
            case -19:
                return new RawTimeInterval(input.readPackedInt(), input.readPackedInt(),
                        input.readPackedInt(), input.readPackedInt());
            case -21:
                return new RawDayTimeInterval(input.readPackedInt(), input.readPackedInt(),
                        input.readPackedInt(), input.readPackedInt(), input.readPackedInt());
            case -23:
            case -22:
                return new ImmutableArrayList(readAsObjectArray(input, type, null)).getList();
            case -24:
                return readAsObjectArray(input, type, null);
            case -25: //uniform-array
                return readArray(input, false);
            case -26:
                LongArray array = new SparseArray();
                int size = input.readPackedInt();
                do {
                    int iElement = input.readPackedInt();
                    if (iElement < 0) {
                        break;
                    }
                    array.set(iElement, decodeType(input.readPackedInt(), input));
                    size--;
                } while (size >= 0);
                return array;
            case -27: //uniform-sparse-array
                return readArray(input, true);
            case -28:
                Map map = new HashMap();
                int cEntries = input.readPackedInt();
                for (int i = 0; i < cEntries; i++) {
                    Object oKey = decodeType(input.readPackedInt(), input);
                    Object oVal = decodeType(input.readPackedInt(), input);
                    map.put(oKey, oVal);
                }
                return map;
            case -29:
                map = new HashMap();
                int nKeyType = input.readPackedInt();
                cEntries = input.readPackedInt();
                for (int i = 0; i < cEntries; i++) {
                    Object oKey = decodeType(nKeyType, input);
                    Object oVal = decodeType(input.readPackedInt(), input);
                    map.put(oKey, oVal);
                }
                return map;
            case -30:
                map = new HashMap();
                nKeyType = input.readPackedInt();
                int nValType = input.readPackedInt();
                cEntries = input.readPackedInt();
                for (int i = 0; i < cEntries; i++) {
                    Object oKey = decodeType(nKeyType, input);
                    Object oVal = decodeType(nValType, input);
                    map.put(oKey, oVal);
                }
                return map;
            case -31:
                int identityId = input.readPackedInt();
                int identityType = input.readPackedInt();
                return decodeType(identityType, input);
//
//              registerIdentity(nId, o);    todo
            case -32:
//              o = lookupIdentity(input.readPackedInt()); todo
                return null;
            case -33:
                return Boolean.FALSE;
            case -34:
                return Boolean.TRUE;
            case -35:
                return "";
            case -36:
                return COLLECTION_EMPTY;
            case -37:
                return null;
            default:
                if (type < 0){
                    throw new StreamCorruptedException("Illegal type " + type);
                }
                PofContext ctx = getPofContext();
                PofSerializer ser;
                try {
                    ser = ctx.getPofSerializer(type);
                }catch (IllegalArgumentException e)
                {
                    throw new StreamCorruptedException(e.getMessage());
                }
                PofReader reader = new PofBufferReader.UserTypeReader(input, ctx, type, input.readPackedInt());

                return ser.deserialize(reader);
        }
    }

    protected Object[] readAsObjectArray(ReadBuffer.BufferInput in, int nType, Object[] ao)
            throws IOException {
        Object[] aoResult = null;
        int size, elementType;
        switch (nType)

        {
            case -37:
                break;
            case -36:
            case -35:
                aoResult = OBJECT_ARRAY_EMPTY;
                break;
            case -24:
            case -22:
                size = in.readPackedInt();
                aoResult = resizeArray(ao, size);
                for (int i = 0; i < size; i++) {
                    aoResult[i] = decodeType(in.readPackedInt(), in);
                }

                break;
            case -25:
            case -23:
                elementType = in.readPackedInt();
                size = in.readPackedInt();
                aoResult = resizeArray(ao, size);
                for (int i = 0; i < size; i++) {
                    aoResult[i] = decodeType(elementType, in);
                }

                break;
            case -26:
                size = in.readPackedInt();
                aoResult = resizeArray(ao, size);
                do {
                    int iElement = in.readPackedInt();
                    if (iElement < 0) {
                        break;
                    }
                    aoResult[iElement] = decodeType(in.readPackedInt(), in);

                    size--;
                } while (size >= 0);

                break;
            case -27:
                elementType = in.readPackedInt();
                size = in.readPackedInt();
                aoResult = resizeArray(ao, size);
                do {
                    int iElement = in.readPackedInt();
                    if (iElement < 0) {
                        break;
                    }
                    aoResult[iElement] = decodeType(elementType, in);

                    size--;
                } while (size >= 0);

                break;
            default:
                throw new IOException("unable to convert type " + nType + " to an array type");
        }

        return aoResult;
    }

    private Object readDoubleArray(ReadBuffer.BufferInput input, int size, boolean sparse) throws IOException {
        double[] arr = new double[size];
        int idxElement;
        for (int i = 0; i < size; i++) {
            if (sparse) {
                idxElement = input.readPackedInt();
                if (idxElement < 0) break;
                arr[idxElement] = input.readDouble();
            } else {
                arr[i] = input.readDouble();
            }
        }
        return arr;
    }

    private float[] readFloatArray(ReadBuffer.BufferInput input, int size, boolean sparse) throws IOException {
        float[] arr = new float[size];
        int idxElement;
        for (int i = 0; i < size; i++) {
            if (sparse) {
                idxElement = input.readPackedInt();
                if (idxElement < 0) break;
                arr[idxElement] = input.readFloat();
            } else {
                arr[i] = input.readFloat();
            }
        }
        return arr;
    }

    private long[] readLongArray(ReadBuffer.BufferInput input, int size, boolean sparse) throws IOException {
        long[] arr = new long[size];
        int idxElement;
        for (int i = 0; i < size; i++) {
            if (sparse) {
                idxElement = input.readPackedInt();
                if (idxElement < 0) break;
                arr[idxElement] = input.readPackedLong();
            } else {
                arr[i] = input.readPackedLong();
            }
        }
        return arr;
    }

    private int[] readIntArray(ReadBuffer.BufferInput input, int size, boolean sparse) throws IOException {
        int[] arr = new int[size];
        int idxElement;
        for (int i = 0; i < size; i++) {
            if (sparse) {
                idxElement = input.readPackedInt();
                if (idxElement < 0) break;
                arr[idxElement] = input.readPackedInt();
            } else {
                arr[i] = input.readPackedInt();
            }
        }
        return arr;
    }

    private short[] readShortArray(ReadBuffer.BufferInput input, int size, boolean sparse) throws IOException {
        short[] arr = new short[size];
        int idxElement;
        for (int i = 0; i < size; i++) {
            if (sparse) {
                idxElement = input.readPackedInt();
                if (idxElement < 0) break;
                arr[idxElement] = (short) input.readPackedInt();
            } else {
                arr[i] = (short) input.readPackedInt();
            }
        }
        return arr;
    }

    private char[] readCharArray(ReadBuffer.BufferInput input, int size, boolean sparse) throws IOException {
        char[] arr = new char[size];
        int idxElement;
        for (int i = 0; i < size; i++) {
            if (sparse) {
                idxElement = input.readPackedInt();
                if (idxElement < 0) break;
                arr[idxElement] = readChar(input);
            } else {
                arr[i] = readChar(input);
            }
        }

        return arr;
    }

    private boolean[] readBooleanArray(ReadBuffer.BufferInput input, int size, boolean sparse) throws IOException {
        boolean[] arr = new boolean[size];
        int idxElement;
        for (int i = 0; i < size; i++) {
            if (sparse) {
                idxElement = input.readPackedInt();
                if (idxElement < 0) break;
                arr[idxElement] = (input.readPackedInt() != 0);
            } else {
                arr[i] = (input.readPackedInt() != 0);
            }
        }
        return arr;
    }

    private byte[] readByteArray(ReadBuffer.BufferInput input, int size, boolean sparse) throws IOException {
        byte[] arr = new byte[size];
        int idxElement;
        if (sparse) {
            for (int i = 0; i < size; i++) {
                idxElement = input.readPackedInt();
                if (idxElement < 0) break;
                arr[idxElement] = input.readByte();
            }
        } else {
            input.readFully(arr);
        }
        return arr;
    }

    private Object readArray(ReadBuffer.BufferInput input, boolean sparse) throws IOException {
        int elementType = input.readPackedInt();
        int size = input.readPackedInt();

        switch (elementType) {
            case -11: {
                return readBooleanArray(input, size, sparse);
            }
            case -12: {
                return readByteArray(input, size, sparse);
            }
            case -14:
                return readCharArray(input, size, sparse);
            case -1:
                return readShortArray(input, size, sparse);
            case -2:
                return readIntArray(input, size, sparse);
            case -3:
                return readLongArray(input, size, sparse);
            case -5:
                return readFloatArray(input, size, sparse);
            case -6:
                return readDoubleArray(input, size, sparse);
            case -13:
            case -10:
            case -9:
            case -8:
            case -7:
            case -4:
            default:
                if (sparse) {
                    LongArray array2 = new SparseArray();
                    do {
                        int iElement = input.readPackedInt();
                        if (iElement < 0) {
                            break;
                        }
                        array2.set(iElement, decodeType(elementType, input));
                        size--;
                    } while (size >= 0);
                    return array2;
                } else {
                    Object[] ao = new Object[size];
                    for (int i = 0; i < size; i++) {
                        ao[i] = decodeType(elementType, input);
                    }
                    return ao;
                }
        }
    }

    protected static Binary readBinary(ReadBuffer.BufferInput in)
            throws IOException
    {
        int cb = in.readPackedInt();

        ReadBuffer buf = in.getBuffer();
        if (buf == null)
        {
            return in.readBuffer(cb).toBinary();
        }

        int of = in.getOffset();
        Binary bin = buf.toBinary(of, cb);
        in.skipBytes(cb);
        return bin;
    }

    public PofContext getPofContext() {
        return pofContext;
    }

    public void setPofContext(PofContext pofContext) {
        this.pofContext = pofContext;
    }
}
//480