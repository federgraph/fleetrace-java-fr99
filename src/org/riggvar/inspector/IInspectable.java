package org.riggvar.inspector;

public interface IInspectable {
    public void inspectorOnLoad(Object sender);

    public void inspectorOnSave(Object sender);
}
