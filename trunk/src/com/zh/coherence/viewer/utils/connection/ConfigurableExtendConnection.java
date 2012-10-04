package com.zh.coherence.viewer.utils.connection;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.tangosol.net.Cluster;
import com.tangosol.net.DefaultConfigurableCacheFactory;
import com.tangosol.net.InvocationService;
import com.tangosol.net.Service;
import com.tangosol.run.xml.XmlElement;
import com.tangosol.run.xml.XmlHelper;

public class ConfigurableExtendConnection extends ExtendConnectionBase{
    
//    private static final Logger LOGGER = LoggerFactory.getLogger(ConfigurableExtendConnection.class);

    private static AtomicInteger CONNECTION_COUNTER = new AtomicInteger();

    private int connectionId = CONNECTION_COUNTER.incrementAndGet();
    
    private ConcurrentMap<Service, Service> activeServices = new ConcurrentHashMap<Service, Service>(4, 0.5f, 2);

    private String serviceName;
    private String host;
    private String port;
    private String connectTimeout;
    private String requestTimeout;
    private Status status;

    public static enum Status {
        CONNECTED, NOT_CONNECTED, MEMBERS_MATCHED, MEMBERS_NOT_MATCHED;
    }

    private ConfigurableExtendConnection(String configFile, String host, String port, String connectTimeout, String requestTimeout) {
	super(configFile);
        
        this.host = host;
        this.port = port;
        this.connectTimeout = connectTimeout;
        this.requestTimeout = requestTimeout;
    }

    
    /**
     * factory for Extend Connection
     * 
     * @param configFile
     *            the coherence application config file name
     * @param host
     *            the host (for Extend Connection)
     * @param port
     *            the port (for Extend Connection)
     * @param connectTimeout
     *            the connection timeout (for Extend Connection)
     * @param requestTimeout
     *            the request timeout (for Extend Connection)
     */
    public static ConfigurableExtendConnection getInstance(String configFile, String host, String port, String connectTimeout, String requestTimeout) {
	ConfigurableExtendConnection connection = new ConfigurableExtendConnection(configFile, host, port, connectTimeout, requestTimeout);
	connection.connect();
	return connection;
    }
    
    /**
     * Factory for Extend Connection
     * 
     * @param configFile
     *            the coherence application config file name
     * @param host
     *            the host (for Extend Connection)
     * @param port
     *            the port (for Extend Connection)
     */
    public static ConfigurableExtendConnection getInstance(String configFile, String host, String port) {
	return getInstance(configFile, host, port, "10000", "10000");
    }

    
    @Override
    protected DefaultConfigurableCacheFactory initPrivateCacheFactory() {
//        LOGGER.info(String.format("New Extend connection #%s is going to be created, config: %s", connectionId,
//                configFile));

        // load coherence application config as XML
        XmlElement xml = XmlHelper.loadFileOrResource(configFile,
                String.format("Coherence cache configuration for Extend connection #%s", connectionId));

        // transforming configuration
        XmlElement schemes = xml.getSafeElement("caching-schemes");
        for (Object element : schemes.getElementList()) {
            XmlElement scheme = (XmlElement) element;
            if (this.isRemoteScheme(scheme)) {
                String name = scheme.getSafeElement("service-name").getString();
                if (name != null) {
                    serviceName = name + "-" + connectionId;
                    scheme.getElement("service-name").setString(serviceName);
                } else {
                    throw new IllegalStateException("Service name not found in the cache config: " + configFile);
                }

                XmlElement socketAddressElement = scheme.getElement("initiator-config").getElement("tcp-initiator")
                        .getElement("remote-addresses").getElement("socket-address");

                socketAddressElement.getElement("address").setString(host);
                socketAddressElement.getElement("port").setString(port);

                try {
                    XmlElement tcpInitiatorElement = scheme.getElement("initiator-config").getElement("tcp-initiator")
                            .getElement("connect-timeout");
                    tcpInitiatorElement.setString(connectTimeout);
                } catch (RuntimeException e) {
                    // if connect timeout is not used in the XML
                }

                try {
                    XmlElement outgoingMessageHandlerElement = scheme.getElement("initiator-config")
                            .getElement("outgoing-message-handler").getElement("request-timeout");
                    outgoingMessageHandlerElement.setString(requestTimeout);
                } catch (RuntimeException e) {
                    // if request timeout is not used in the XML
                }
            }
        }

        // build new configurable cache factory (to get NamedCache,
        // InvocationService etc ...) which managed a single connection
        DefaultConfigurableCacheFactory factory = new DefaultConfigurableCacheFactory(xml);

        return factory;
    }

    /**
     * Get Cluster
     * 
     * @return Cluster
     */
    public Cluster getCluster() {
        InvocationService service = (InvocationService) this.cacheFactory.ensureService(serviceName);
        activeServices.putIfAbsent(service, service);
        Cluster cluster = service.getCluster();

        return cluster;
    }

    public InvocationService getInvocationService() {
        InvocationService service = (InvocationService) cacheFactory.ensureService(serviceName);
        activeServices.putIfAbsent(service, service);

        return service;
    }
    
    public String getConfigFile() {
        return configFile;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getHost() {
        return host;
    }

    public String getPort() {
        return port;
    }

    public String getConnectTimeout() {
        return connectTimeout;
    }

    public String getRequestTimeout() {
        return requestTimeout;
    }

    public int getConnectionId() {
        return connectionId;
    }

    public ConcurrentMap<Service, Service> getActiveServices() {
        return activeServices;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ConfigurableExtendConnection that = (ConfigurableExtendConnection) o;

        if (connectionId != that.connectionId) return false;
        if (activeServices != null ? !activeServices.equals(that.activeServices) : that.activeServices != null)
            return false;
        if (connectTimeout != null ? !connectTimeout.equals(that.connectTimeout) : that.connectTimeout != null)
            return false;
        if (host != null ? !host.equals(that.host) : that.host != null) return false;
        if (port != null ? !port.equals(that.port) : that.port != null) return false;
        if (requestTimeout != null ? !requestTimeout.equals(that.requestTimeout) : that.requestTimeout != null)
            return false;
        if (serviceName != null ? !serviceName.equals(that.serviceName) : that.serviceName != null) return false;
        if (status != that.status) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = connectionId;
        result = 31 * result + (activeServices != null ? activeServices.hashCode() : 0);
        result = 31 * result + (serviceName != null ? serviceName.hashCode() : 0);
        result = 31 * result + (host != null ? host.hashCode() : 0);
        result = 31 * result + (port != null ? port.hashCode() : 0);
        result = 31 * result + (connectTimeout != null ? connectTimeout.hashCode() : 0);
        result = 31 * result + (requestTimeout != null ? requestTimeout.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ConfigurableExtendConnection{" +
                "connectionId=" + connectionId +
                ", activeServices=" + activeServices +
                ", serviceName='" + serviceName + '\'' +
                ", host='" + host + '\'' +
                ", port='" + port + '\'' +
                ", connectTimeout='" + connectTimeout + '\'' +
                ", requestTimeout='" + requestTimeout + '\'' +
                ", status=" + status +
                '}';
    }
}
