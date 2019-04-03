package org.riggvar.bo;

import org.riggvar.base.*;

public class TBODelegate implements INotifyEvent {
    private int command = -1;
    private TBO owner;

    public TBODelegate(TBO aOwner, int aCommand) {
        owner = aOwner;
        command = aCommand;
    }

    public static final int commandModified = 0;

    public void HandleNotifyEvent(Object sender) {
        switch (command) {
        case commandModified:
            owner.setModified(sender);

        }
    }
}
