package org.riggvar.base;

/* BO as seen from perspective of Bridge  */
public interface IBridgeBO {
    IConnection getInputConnection();

    IConnection getOutputConnection();

    void Broadcast(String s);

    String getReport(String sRequest);

    TBaseIniImage getIniImage();

    void InjectClientBridgeMsg(String s);

    void InjectServerBridgeMsg(TContextMsg cm);
}
