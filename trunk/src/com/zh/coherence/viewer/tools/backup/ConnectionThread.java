package com.zh.coherence.viewer.tools.backup;

import com.tangosol.net.NamedCache;
import com.zh.coherence.viewer.utils.connection.ConfigurableExtendConnection;
import com.zh.coherence.viewer.utils.connection.ExtendConnectionBase;

/*
 *  Thread with it's own connection to Coherence
 */
public class ConnectionThread extends Thread {
	private ExtendConnectionBase connection;
	
	public ConnectionThread(Runnable r, String host, String port) {
	    super(r, "ZHConnectionThread-" + r.hashCode());
	    this.connection = ConfigurableExtendConnection.getInstance("extend-client-config-template.xml", host, port);
	}
	
	public ExtendConnectionBase getConnection(){
	    return connection;
	}
	
	public NamedCache getCache(String cacheName){
	    return connection.getCache(cacheName);
	}
}