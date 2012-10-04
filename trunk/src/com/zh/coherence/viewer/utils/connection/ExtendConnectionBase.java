package com.zh.coherence.viewer.utils.connection;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;

import com.tangosol.net.ConfigurableCacheFactory;
import com.tangosol.net.DefaultConfigurableCacheFactory;
import com.tangosol.net.InvocationService;
import com.tangosol.net.NamedCache;
import com.tangosol.net.Service;
import com.tangosol.run.xml.XmlElement;
import com.tangosol.run.xml.XmlHelper;

public class ExtendConnectionBase {

    private static AtomicInteger CONNECTION_COUNTER = new AtomicInteger();

    private int connectionId = CONNECTION_COUNTER.incrementAndGet();
    private ConcurrentMap<Service, Service> activeServices = new ConcurrentHashMap<Service, Service>(4, 0.5f, 2);
    protected String configFile;
    protected ConfigurableCacheFactory cacheFactory;

    protected ExtendConnectionBase(String configFile) {
        this.configFile = configFile;
    }

    public static ExtendConnectionBase getInstance(String configFile) {
        ExtendConnectionBase connection = new ExtendConnectionBase(configFile);
        connection.connect();
        return connection;
    }

    protected void connect() {
        if (cacheFactory == null) {
            cacheFactory = initPrivateCacheFactory();
        }
    }

    protected DefaultConfigurableCacheFactory initPrivateCacheFactory() {

        XmlElement xml = XmlHelper.loadFileOrResource(configFile, "Coherence cache configuration for Extend connection #" + connectionId);
        // transforming configuration
        XmlElement schemes = xml.getSafeElement("caching-schemes");
        for (Object o : schemes.getElementList()) {
            XmlElement scheme = (XmlElement) o;
            if (isRemoteScheme(scheme)) {
                String name = scheme.getSafeElement("service-name").getString();
                if (name != null) {
                    String nname = name + "-" + connectionId;
                    scheme.getElement("service-name").setString(nname);
                }
            }
        }

        DefaultConfigurableCacheFactory factory = new DefaultConfigurableCacheFactory(xml);
        return factory;
    }

    /**
     * Check, is scheme remote
     *
     * @param scheme the scheme
     * @return result of check
     */
    protected boolean isRemoteScheme(XmlElement scheme) {
        String name = scheme.getName();
        return "remote-cache-scheme".equals(name) || "remote-invocation-scheme".equals(name);
    }

    /**
     * Get NamedCache by the cache name
     *
     * @param name the cache name
     * @return NamedCache by the cache name
     */
    public NamedCache getCache(String name) {
//	if(cacheFactory == null){
//	    throw new IllegalStateException("cacheFactory shouldn't be null. Please init it by connect()");
//	}
        NamedCache cache = cacheFactory.ensureCache(name, null);
        Service service = cache.getCacheService();
        activeServices.putIfAbsent(service, service);
        return cache;
    }

    /**
     * Get invocation service
     *
     * @return invocation service
     */
    public InvocationService getInvocationService(String serviceName) {
//	if(cacheFactory == null){
//	    throw new IllegalStateException("cacheFactory shouldn't be null. Please init it by connect()");
//	}
        InvocationService service = (InvocationService) cacheFactory.ensureService(serviceName + "-" + connectionId);
        activeServices.putIfAbsent(service, service);
        return service;
    }

    /**
     * Do disconnection from the extend proxy
     * <p/>
     * <pre>
     * Warning: this method is not
     * concurrency safe, you may get to trouble if you are accessing caches of
     * services via this connection during shutdown.
     * </pre>
     */
    public void disconnect() {
        for (Service service : new ArrayList<Service>(activeServices.keySet())) {
            try {
                if (service.isRunning()) {
                    service.stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
