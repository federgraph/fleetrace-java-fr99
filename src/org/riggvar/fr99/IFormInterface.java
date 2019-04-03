package org.riggvar.fr99;

import org.riggvar.web.GuiAction;

public interface IFormInterface {
    void HandleInform(GuiAction action);

    void NewItem();

    void UpdateCaption();

    void ToggleEventMenu();

    void ToggleKeyPad();

    void Exit();
}
