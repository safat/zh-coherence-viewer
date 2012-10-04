package com.zh.coherence.viewer.utils.connection.checker;

import java.util.EventListener;

public interface ConnectionListener extends EventListener {

    void connectionChecked(ConnectionEvent event);
}
