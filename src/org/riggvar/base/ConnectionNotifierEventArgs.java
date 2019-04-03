package org.riggvar.base;

public class ConnectionNotifierEventArgs {
    public static final int ConnectStatusDisconnect = 0;
    public static final int ConnectStatusConnect = 1;

    public static final int SenderTypeInternalConnection = 0;
    public static final int SenderTypeExternalPlug = 1;

    private int connectStatus = 0;
    private int senderType = 0;

    public ConnectionNotifierEventArgs(int aSenderType, int aConnectStatus) {
        senderType = aSenderType;
        connectStatus = aConnectStatus;
    }

    public int getConnectStatus() {
        return connectStatus;
    }

    public int getSenderType() {
        return senderType;
    }
}
