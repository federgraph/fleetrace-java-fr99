package org.riggvar.base;

import java.util.*;
import java.awt.event.*;

public class TGridUpdate {
    private ActionListener FOnUpdateView;

    private int Delay;

    public int getDelay() {
        return Delay;
    }

    public boolean NeedFullUpdate = false;

    public Calendar FLastUpdateTime = Calendar.getInstance();
    public boolean IMarker;
    public boolean ViewEnabled;

    public TGridUpdate(int delay) {
        Delay = delay;
        if (Delay < 100)
            Delay = 100;
        ActionListener taskPerformer = new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                DoOnIdle();
            }
        };
        // check 10 times faster then Delay
        new javax.swing.Timer(Delay / 10, taskPerformer).start();
    }

    /**
     * attach an EventHandler to be called with controlled maximum frequency
     *
     * @return ActionListener EventHandler to be called.
     */
    public ActionListener getOnUpdateView() {
        return FOnUpdateView;
    }

    public void setOnUpdateView(ActionListener value) {
        FOnUpdateView = value;
    }

    private void UpdateView() {
        if (FOnUpdateView != null)
            FOnUpdateView.actionPerformed(null);
    }

    /**
     * extend the passive period
     */
    public void DelayUpdate() {
        if (!IMarker) {
            FLastUpdateTime = Calendar.getInstance();
            // Caption := Caption + 'D';
        } else
            IMarker = false;
    }

    /**
     * make sure assigned EventHandler is called from within next call to DoOnIdle
     */
    public void InvalidateView() {
        FLastUpdateTime.add(Calendar.SECOND, -1);
        FLastUpdateTime.add(Calendar.MILLISECOND, -100);
        // Caption = Caption + 'I';
        IMarker = true;
    }

    /**
     * call assigned EventHandler, if any - but only if not called recently
     */
    public void DoOnIdle() {
        if (ViewEnabled) {
            if (ElapsedMillis() > 1000) {
                UpdateView();
                FLastUpdateTime = Calendar.getInstance();
            }
        }
    }

    public Date Now() {
        return Calendar.getInstance().getTime();
    }

    public long ElapsedMillis() {
        return Calendar.getInstance().getTimeInMillis() - FLastUpdateTime.getTimeInMillis();
    }

    /**
     * directly call assigned EventHandler, if any
     */
    public void HandleInform() {
        if (ViewEnabled)
            UpdateView();
    }

    public void ScheduleFullUpdate() {
        NeedFullUpdate = true;
    }

    /**
     * ScheduleFullUpdate
     *
     * @param value int pass any positive value to schedule an update. (pass a value
     *              <= 0 to unschedule an update.)
     */
    public void ScheduleFullUpdate(int value) {
        if (value > 0)
            NeedFullUpdate = true;
        else
            NeedFullUpdate = false;
    }
}
