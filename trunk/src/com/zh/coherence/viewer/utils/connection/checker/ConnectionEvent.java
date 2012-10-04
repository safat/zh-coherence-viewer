package com.zh.coherence.viewer.utils.connection.checker;

import java.util.EventObject;
import java.util.HashSet;
import java.util.Set;

import com.tangosol.dev.compiler.MemberInfo;
import com.zh.coherence.viewer.utils.connection.ConfigurableExtendConnection;

public class ConnectionEvent extends EventObject {
    private static final long serialVersionUID = 1L;
    private ConfigurableExtendConnection extendConnection;
    private Set<MemberInfo> members = new HashSet<MemberInfo>();

    public ConnectionEvent(Object source, ConfigurableExtendConnection extendConnection, Set<MemberInfo> members) {
        super(source);
        this.extendConnection = extendConnection;
        this.members = members;
    }

    public ConfigurableExtendConnection getExtendConnection() {
        return extendConnection;
    }

    public Set<MemberInfo> getMembers() {
        return members;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ConnectionEvent [extendConnection=");
        builder.append(extendConnection);
        builder.append(", members=");
        builder.append(members);
        builder.append("]");
        return builder.toString();
    }
}
