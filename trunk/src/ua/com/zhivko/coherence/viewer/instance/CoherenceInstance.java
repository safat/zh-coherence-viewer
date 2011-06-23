package ua.com.zhivko.coherence.viewer.instance;

import java.util.List;

/**
 *
 * @author Живко
 */
public class CoherenceInstance {
    private String serverConfig, pofConfig;

    public CoherenceInstance() {
    }

    public List<String> getSchemes(){
        return null;
    }

    public List<String> getServices(){
        return null;
    }

    public String getPofConfig() {
        return pofConfig;
    }

    public void setPofConfig(String pofConfig) {
        this.pofConfig = pofConfig;
    }

    public String getServerConfig() {
        return serverConfig;
    }

    public void setServerConfig(String serverConfig) {
        this.serverConfig = serverConfig;
    }

    public void connect(){
        System.setProperty("", serverConfig);
        if(pofConfig != null)
            System.setProperty("", pofConfig);
    }

    public void disconnect(){
        //@todo any actions?
    }
}
