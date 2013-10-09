package org.zh.coherence.viewer.forms.connection;

import lombok.Data;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 02.10.13
 * Time: 22:11
 */
@Data
public class ConnectionProviderInfo {

    private String name;
    private String description;
    private ImageIcon icon;
    private String providerClass;
}
