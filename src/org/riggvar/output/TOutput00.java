package org.riggvar.output;

import org.riggvar.base.TStrings;
import org.riggvar.bo.TBO;
import org.riggvar.bo.TMain;

public class TOutput00 {

    protected TBO BO;
    protected TStrings SL;

    public TOutput00() {
        BO = TMain.BO;
        SL = TMain.BO.Output.SL;
    }

    public boolean getWantPageHeader() {
        return BO.Output.WantPageHeader;
    }

    public void setWantPageHeader(boolean value) {
        BO.Output.WantPageHeader = value;
    }

    public int getOutputType() {
        return BO.Output.OutputType;
    }

    public void setOutputType(int value) {
        BO.Output.OutputType = value;
    }

    public boolean getXMLSection() {
        return BO.Output.XMLSection;
    }

    public void setXMLSection(boolean value) {
        BO.Output.XMLSection = value;
    }

}
