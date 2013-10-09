package org.zh.coherence.connection;

import org.zh.coherence.connection.proxy.ProxyConnectionProvider;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 24.12.12
 * Time: 22:08
 */
public interface ConnectionConfig {
    /**
     * It's type of connection, e.g. extend:// kerberos_extend://
     *
     * @return ID of connection type
     */
    String getExtention();

    String getConnectionGroupName();

    String getConnectionUserName();

    /**
     * sets URL to connect
     *
     * @param url URL to connect
     */
    void setURL(String url);

    /**
     * @return connection URL
     */
    String getURL();

    /**
     * it's a flag to notify the system that it needs some advanced properties
     *
     * @return is connection needs advanced properties
     */
    boolean hasAdvanced();

    /**
     * This panel will be opened on logon panel as a tab
     *
     * @return
     */
    JComponent getAdvancedPanel();

    void setAdvancedProperties();

    /**
     * Connect to Coherence and return ProxyConnectionProvider to user
     *
     * @return ProxyConnectionProvider
     */
    ProxyConnectionProvider connect();
}