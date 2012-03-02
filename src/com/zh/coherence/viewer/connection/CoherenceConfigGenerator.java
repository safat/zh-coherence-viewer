package com.zh.coherence.viewer.connection;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 12.02.12
 * Time: 11:21
 */
public class CoherenceConfigGenerator {

    public void setupExtendConfig(String host, String port) {
        try{
            File template = new File("config/extend-client-config-template.xml");
            FileInputStream fis = new FileInputStream(template);
            byte[] binary = new byte[fis.available()];
            fis.read(binary);
            fis.close();
            String str = new String(binary);
            str = str.replaceAll("__host__", host);
            str = str.replaceAll("__port__", port);

            //save file
            File result = new File("tmp/extend-client-config.xml");
            FileOutputStream fos = new FileOutputStream(result);
            fos.write(str.getBytes());
            fos.close();

            //configure coherence
            System.setProperty("gridkit.auto-pof.use-public-cache-config", "true");

            System.setProperty("tangosol.coherence.cacheconfig", "tmp/extend-client-config.xml");

            //todo pof config

        }catch(Exception ex){
            ex.printStackTrace();
            //todo process this exception
        }
    }
}
