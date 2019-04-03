package org.riggvar.web;

public interface IGuiManager {
    void HandleInform(GuiAction action);

    void UpdateCaption();

    void UpdateWorkspaceStatus();

    void InitViews();

    void DisposeViews();
}
