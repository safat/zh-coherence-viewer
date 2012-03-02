package com.zh.coherence.viewer.pof;

import com.tangosol.net.CacheFactory;
import com.tangosol.net.DefaultConfigurableCacheFactory;
import com.tangosol.net.NamedCache;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 18.02.12
 * Time: 11:49
 */
public class Test {

    public Test() {
        try{
            CacheFactory.setConfigurableCacheFactory(
                new DefaultConfigurableCacheFactory("tmp/auto-pof-cache-config-extend-client.xml"));
            NamedCache cache = CacheFactory.getCache("ch");
            for(Object key : cache.keySet()){
                System.err.println("key: " + key);
                System.err.println("val: " + cache.get(key));
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Test();
    }
}
