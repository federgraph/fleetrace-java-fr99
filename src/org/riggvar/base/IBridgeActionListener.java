package org.riggvar.base;

public interface IBridgeActionListener {
    public final int backup_action_id = 1;

    void bridgeActionPerformed(Object source, int id, String s);
}
