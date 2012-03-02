package com.zh.coherence.viewer.pof;

import com.tangosol.io.pof.PofSerializer;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 20.02.12
 * Time: 20:36
 */
public class SerializationContext {
    public Class<?> type;
    public int pofId;
    public PofSerializer serializer;
}
