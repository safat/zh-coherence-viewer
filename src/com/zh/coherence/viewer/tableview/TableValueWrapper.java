package com.zh.coherence.viewer.tableview;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 18.03.12
 * Time: 11:21
 */
public class TableValueWrapper {
    private Object value;
    private String strValue;
    
    int limit = 200;

    public TableValueWrapper(Object value) {
        this.value = value;
        if(value == null){
            strValue = "NULL";
        }
    }

    public String toString(){
        if(strValue == null){
            strValue = value.toString();
            if(strValue.length() > limit){
                strValue = strValue.substring(0, limit) + "...";
            }
        }
        return strValue;
    }
}
