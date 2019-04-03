package org.riggvar.fr99;

import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import org.riggvar.base.DrawNotifierEventArgs;
import org.riggvar.base.IDrawNotifier;
import org.riggvar.base.TGridUpdate;
import org.riggvar.bo.TBO;
import org.riggvar.bo.TMain;

public class TIdleManager implements ActionListener, IDrawNotifier {
    public static boolean sTrace = false;

    public TGridUpdate StammdatenUpdate;
    public TGridUpdate EventUpdate;

    private Cursor waitCursor;
    private FrameFR62 frame;
    private boolean waiting;
    protected long OnIdleCounter;

    private TGridUpdate IdleGenerator;

    public TIdleManager(FrameFR62 frameFR62) {
        frame = frameFR62;

        waitCursor = new Cursor(Cursor.WAIT_CURSOR);

        StammdatenUpdate = new TGridUpdate(5000);
        EventUpdate = new TGridUpdate(2000);

        IdleGenerator = new TGridUpdate(5000);
        IdleGenerator.setOnUpdateView(this);
    }

    public void setEnabled(boolean value) {
        IdleGenerator.ViewEnabled = value;
    }

    private TBO BO() {
        return TMain.BO;
    }

    private boolean IsEventModified() {
        return frame.PanelEvent.getModified();
    }

    /**
     * global OnIdle() method
     *
     * @param e ActionEvent can be null
     */
    public void actionPerformed(ActionEvent e) {
        if (frame == null)
            return;

        try {
            if (EventUpdate.NeedFullUpdate)
                frame.PanelEvent.UpdateView();

            if (waiting)
                return;
            if (!IsEventModified())
                return;
            OnIdleCounter++;

            Cursor oldCursor = frame.getCursor();
            frame.setCursor(waitCursor);
            try {
                BO().OnIdle();
                if (frame.PageControl.getSelectedComponent() == frame.PanelEvent) {
                    if (IsEventModified()) {
                        EventUpdate.DoOnIdle();
                    }
                }
            } finally {
                frame.setCursor(oldCursor);
            }
        } catch (Exception ex) {
            HandleError(ex);
        }
    }

    private void HandleError(Exception ex) {
        System.err.println("exception in IdleManager.OnIdle");
    }

    public void ScheduleFullUpdate(Object sender, DrawNotifierEventArgs e) {
        if (e != null) {
            switch (e.getDrawTarget()) {
            case DrawNotifierEventArgs.DrawTargetAll:
                if (sTrace)
                    System.out.println("ScheduleFullUpdate All");
                if (e.getDrawAction() == DrawNotifierEventArgs.OperationSchedule) {
                    EventUpdate.ScheduleFullUpdate();
                }
                break;
            case DrawNotifierEventArgs.DrawTargetEvent:
                if (sTrace)
                    System.out.println("ScheduleFullUpdate Event " + e.getDrawAction());
                EventUpdate.ScheduleFullUpdate(e.getDrawAction());
                break;
            }
        }
    }

    public void EnableViews() {
        EventUpdate.ViewEnabled = true;
        StammdatenUpdate.ViewEnabled = true;
    }

    public void DisableViews() {
        EventUpdate.ViewEnabled = false;
        StammdatenUpdate.ViewEnabled = false;

        EventUpdate.NeedFullUpdate = false;
        StammdatenUpdate.NeedFullUpdate = false;

        EventUpdate.setOnUpdateView(null);
        StammdatenUpdate.setOnUpdateView(null);
    }

    public void HandleInform(Object sender, ActionEvent e) {
        EventUpdate.HandleInform();
        StammdatenUpdate.HandleInform();
    }

}
