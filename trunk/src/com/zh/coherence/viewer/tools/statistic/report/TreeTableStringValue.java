package com.zh.coherence.viewer.tools.statistic.report;

import org.jdesktop.swingx.renderer.*;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 19.05.12
 * Time: 0:21
 */
public class TreeTableStringValue implements StringValue {
    @Override
    public String getString(Object value) {
        if (value instanceof Named) {
            return ((Named)value).getName();
        }
        return StringValues.TO_STRING.getString(value);
    }
}
