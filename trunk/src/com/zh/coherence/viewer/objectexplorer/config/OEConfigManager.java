package com.zh.coherence.viewer.objectexplorer.config;

public class OEConfigManager {
    private static OEConfigManager ourInstance = new OEConfigManager();

    public static OEConfigManager getInstance() {
        return ourInstance;
    }

    private OEConfigManager() {
    }

    protected void loadConfig(){
        //todo !
    }

    protected void saveConfig(){
        //todo !
    }
}
