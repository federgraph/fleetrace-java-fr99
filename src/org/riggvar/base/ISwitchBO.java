package org.riggvar.base;

/* BO as seen from perspective of Switch  */
public interface ISwitchBO {
    int getPortIn();

    int getPortOut();

    void Reply(Object Connection, String s);

    void HandleContextMsg(TContextMsg cm);

    TBaseIniImage getIniImage();
}
