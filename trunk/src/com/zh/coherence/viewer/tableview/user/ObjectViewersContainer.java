package com.zh.coherence.viewer.tableview.user;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@XmlRootElement(name = "viewers")
@XmlAccessorType(XmlAccessType.FIELD)
public class ObjectViewersContainer {

    @XmlElement(name = "item")
    private List<UserViewerItem> items = new ArrayList<UserViewerItem>();

    public Collection<UserViewerItem> getAvailableViewers(Object obj){
        ArrayList<UserViewerItem> list = new ArrayList<UserViewerItem>();
        Class clazz;
        for(UserViewerItem item : items){
            try{
                clazz = Class.forName(item.getClazz().trim());
                if(clazz.isInstance(obj)){
                    list.add(item);
                }
            }catch (ClassNotFoundException ex){
                ex.printStackTrace();
            }
        }

        return list;
    }
}
