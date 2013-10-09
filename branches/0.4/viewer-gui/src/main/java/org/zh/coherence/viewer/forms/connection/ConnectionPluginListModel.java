package org.zh.coherence.viewer.forms.connection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.zh.coherence.viewer.CorePlugin;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static org.java.plugin.registry.Extension.Parameter;

/**
 * Created with IntelliJ IDEA.
 * User: zhivko
 * Date: 09.10.13
 * Time: 22:23
 */
public class ConnectionPluginListModel extends AbstractListModel<ConnectionProviderInfo> {

    private static final Log LOGGER = LogFactory.getLog(ConnectionPluginListModel.class);
    private List<ConnectionProviderInfo> providers = new ArrayList<>();

    public ConnectionPluginListModel() {
        try {
            loadPlugins();
        } catch (Exception ex) {
            LOGGER.error("ERROR", ex);
        }
    }

    private void loadPlugins() {
        PluginManager pm = PluginManager.lookup(ConnectionPluginListModel.class);
        ExtensionPoint extensionPoint = pm.getRegistry().getExtensionPoint(CorePlugin.PLUGIN_ID, "ConnectionProvider");
        LOGGER.info("found: " + extensionPoint.getConnectedExtensions());
        for (Extension extension : extensionPoint.getConnectedExtensions()) {
            Parameter name = extension.getParameter("name");
            Parameter descrParam = extension.getParameter("description");
            Parameter iconParam = extension.getParameter("icon");
            Parameter providerClass = extension.getParameter("class");

            ConnectionProviderInfo info = new ConnectionProviderInfo();
            info.setName(name.valueAsString());
            info.setProviderClass(providerClass.valueAsString());
            if (descrParam != null) {
                info.setDescription(descrParam.valueAsString());
            }
            if (iconParam != null) {
                URL iconUrl = pm.getPluginClassLoader(extension.getDeclaringPluginDescriptor())
                        .getResource(iconParam.valueAsString());
                LOGGER.info("URL icon: " + iconUrl);
                info.setIcon(new ImageIcon(iconUrl));
            }

            providers.add(info);
        }
    }

    @Override
    public int getSize() {
        return providers.size();
    }

    @Override
    public ConnectionProviderInfo getElementAt(int index) {
        return providers.get(index);
    }
}
