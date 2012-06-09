package com.zh.coherence.viewer.utils.icons;

import org.springframework.beans.factory.InitializingBean;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class IconLoader extends ImageIcon implements InitializingBean{
    private String path;

    public IconLoader() {
    }

    public IconLoader(String path) {
        this.path = path;
        init();
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        init();
    }

    private void init(){
        if(path != null){
            setImage(Toolkit.getDefaultToolkit().getImage(path));
        }else{
            URL url = getClass().getResource("broken.png");
            setImage(Toolkit.getDefaultToolkit().getImage(url));
        }
    }
}
