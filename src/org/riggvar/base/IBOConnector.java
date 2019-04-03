package org.riggvar.base;

public interface IBOConnector {
    void ConnectBO();

    void DisconnectBO();

    boolean getConnected();

    boolean getConnectedBO();
}
