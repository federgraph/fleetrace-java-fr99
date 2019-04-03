package org.riggvar.eventmenu;

import org.riggvar.base.TStrings;

public interface IWorkspaceList {
    void Init();

    void Load(TStrings Combo);

    String GetName(int i);

    String GetUrl(int i);

    boolean IsWritable(int i);
}
