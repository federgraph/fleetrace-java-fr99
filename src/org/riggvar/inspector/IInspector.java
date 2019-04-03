package org.riggvar.inspector;

public interface IInspector {
    public void loadModel();

    public void saveModel();

    public void setInspectable(IInspectable model);
}
