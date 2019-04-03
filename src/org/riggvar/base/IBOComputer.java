package org.riggvar.base;

public interface IBOComputer {
    String Test(String EventData, boolean IsWebService);

    String CalcStatefull(String EventData, boolean IsWebService);

    String CalcStateless(String EventData, boolean IsWebService);
}
