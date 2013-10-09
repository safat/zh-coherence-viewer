package org.zh.coherence.connection.proxy;

import org.zh.coherence.viewer.shared.ConnectionProvider;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 24.12.12
 * Time: 22:21
 */
public class ProxyConnectionProvider implements ConnectionProvider {
    @Override
    public void init(JComponent root) {
        root.add(new JLabel("test"));
    }
}
