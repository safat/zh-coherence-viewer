package com.zh.coherence.viewer.tools.backup;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ThreadFactory;

import com.zh.coherence.viewer.utils.connection.ExtendConnectionBase;

public class ConnectionThreadFactory implements ThreadFactory {
	private String connectionHost;
	private String connectionPort;
	private LinkedList<ExtendConnectionBase> connections;
	
	public ConnectionThreadFactory(String connectionHost, String connectionPort){
	    this.connectionHost = connectionHost;
	    this.connectionPort = connectionPort;
	    this.connections = new LinkedList<ExtendConnectionBase>();
	}

	@Override
	public Thread newThread(Runnable r) {
	    ConnectionThread thread = new ConnectionThread(r, connectionHost, connectionPort);
	    connections.add(thread.getConnection());
	    return thread;
	}
	
	public List<ExtendConnectionBase> getThreadsConnections(){
	    return connections;
	}
}