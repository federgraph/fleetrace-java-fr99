package org.riggvar.calc;

import org.riggvar.event.*;
import org.riggvar.base.*;

/**
 * TCalcEvent, allows switch between simple, inline and proxy scoring methods.
 */
public class TCalcEvent {
    private int currentProxyType;
    private static TCalcEvent instance;
    private ICalcEventProxy proxy;
    public TResultHash ResultHash;

    private TCalcEvent() {
        ResultHash = new TResultHash();
        instance = this;
    }

    public void Init() {
        // Nachbrenner, falls Spring-Configuration nicht erfolgreich war.
        if (proxy == null)
            Init(2);
    }

    public void Init(int proxyType) {
        if (proxy == null || proxyType != currentProxyType) {
            switch (proxyType) {
            case 1:
                proxy = new TCalcEventProxy01(); // simple
                currentProxyType = proxyType;
                break;
            case 2:
                proxy = new TCalcEventProxy08(); // inline
                currentProxyType = proxyType;
                break;
            case 3:
                proxy = new TCalcEventProxy03(); // proxy
                currentProxyType = proxyType;
                break;
            default:
                proxy = new TCalcEventProxy08();
                currentProxyType = proxyType;
                break;
            }
        }
    }

    public static TCalcEvent getInstance() {
        if (instance == null)
            instance = new TCalcEvent();
        instance.Init();
        return instance;
    }

    // to be injected by Spring
    public void setProxy(ICalcEventProxy value) {
        proxy = value;
    }

    public void Calc(TEventNode aqn) {
        proxy.Calc(aqn);
        TUniquaPoints.Calc(aqn);
    }

    public void GetScoringNotes(TStrings SL) {
        proxy.GetScoringNotes(SL);
    }

    public void setWithTest() {
        proxy.setWithTest(true);
    }

}
