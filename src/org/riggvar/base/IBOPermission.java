package org.riggvar.base;

public interface IBOPermission {
    boolean HaveLocalAccess();

    boolean HaveNetworkAccess();

    boolean HaveWebPermission();

    boolean HaveSocketPermission();
}
