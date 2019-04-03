package org.riggvar.event;

import java.util.*;
import org.riggvar.base.*;
import org.riggvar.bo.*;

public class TOTimeErrorList {
    private List<TEventRowCollectionItem> FL;
    private TStringList FErrorList;
    private TStringList FSortedSL;

    public TOTimeErrorList() {
        FL = new ArrayList<TEventRowCollectionItem>();
        FErrorList = new TStringList();
        FSortedSL = new TStringList();
        // FSortedSL.Sorted = true; //IndexOf does not work yet
    }

    private boolean CheckOTime(TEventNode ev) {
        if (ev.UseFleets) {
            boolean hasError = false;
            TEventRowCollection cl = ev.getBaseRowCollection();
            for (int r = 1; r < cl.RCount(); r++) {
                int fc = cl.getFleetCount(r);
                for (int f = 0; f <= fc; f++) {
                    cl.FillFleetList(FL, r, f);
                    if (FL.size() > 0)
                        if (CheckOTimeForFleet(FL, r))
                            hasError = true;
                }
            }
            FL.clear();
            return hasError;
        } else {
            return CheckOTimeForAll(ev.getBaseRowCollection());
        }
    }

    private boolean CheckOTimeForFleet(List<TEventRowCollectionItem> cl, int r) {
        int oldErrorCount = FErrorList.getCount();
        int EntryCount = cl.size();
        int[] a = new int[EntryCount + 1];

        // clear array slots
        for (int i = 0; i < a.length; i++) {
            a[i] = 0;
        }
        for (int i = 0; i < cl.size(); i++) {
            TEventRowCollectionItem cr = cl.get(i);
            TEventRaceEntry re = cr.Race[r];
            int temp = re.OTime;
            if (temp < 0) {
                re.FinishErrors.Include(TFinishError.error_OutOfRange_OTime_Min);
                AddFinishError(r, cr, TFinishError.error_OutOfRange_OTime_Min); // below lower limit
            } else if (temp > EntryCount) {
                re.FinishErrors.Include(TFinishError.error_OutOfRange_OTime_Max);
                AddFinishError(r, cr, TFinishError.error_OutOfRange_OTime_Max); // beyond upper limit
            } else if ((temp > 0) && (a[temp] == 1)) {
                re.FinishErrors.Include(TFinishError.error_Duplicate_OTime);
                AddFinishError(r, cr, TFinishError.error_Duplicate_OTime); // has duplicates
            } else {
                a[temp] = 1;
            }
        }

        boolean HasNull = false;
        for (int position = 1; position <= EntryCount; position++) {
            if ((a[position] == 1) && HasNull) {
                AddContiguousError(cl, r, position);
                HasNull = false;
            }
            if (a[position] == 0)
                HasNull = true;
        }
        return (FErrorList.getCount() > oldErrorCount);
    }

    private boolean CheckOTimeForAll(TEventRowCollection cl) {
        int oldErrorCount = FErrorList.getCount();
        int EntryCount = cl.getCount();
        int[] a = new int[EntryCount + 1]; // SetLength(a, EntryCount + 1);
        for (int r = 1; r < cl.RCount(); r++) {
            // clear array slots
            for (int i = 0; i < a.length; i++) {
                a[i] = 0;
            }
            for (int i = 0; i < cl.getCount(); i++) {
                TEventRowCollectionItem cr = cl.get(i);
                TEventRaceEntry re = cr.Race[r];
                int temp = re.OTime;
                if (temp < 0) {
                    re.FinishErrors.Include(TFinishError.error_OutOfRange_OTime_Min);
                    AddFinishError(r, cr, TFinishError.error_OutOfRange_OTime_Min); // below lower limit
                } else if (temp > EntryCount) {
                    re.FinishErrors.Include(TFinishError.error_OutOfRange_OTime_Max);
                    AddFinishError(r, cr, TFinishError.error_OutOfRange_OTime_Max); // beyond upper limit
                } else if ((temp > 0) && (a[temp] == 1)) {
                    re.FinishErrors.Include(TFinishError.error_Duplicate_OTime);
                    AddFinishError(r, cr, TFinishError.error_Duplicate_OTime); // has duplicates
                } else {
                    a[temp] = 1;
                }
            }

            boolean HasNull = false;
            for (int position = 1; position <= EntryCount; position++) {
                if ((a[position] == 1) && HasNull) {
                    AddContiguousError(cl, r, position);
                    HasNull = false;
                }
                if (a[position] == 0)
                    HasNull = true;
            }
        }
        return (FErrorList.getCount() > oldErrorCount);
    }

    private void AddContiguousError(TEventRowCollection cl, int r, int position) {
        TEventRowCollectionItem cr;
        TEventRaceEntry re;
        for (int i = 0; i < cl.getCount(); i++) {
            cr = cl.get(i);
            re = cr.Race[r];
            if (re.OTime == position) {
                re.FinishErrors.Include(TFinishError.error_Contiguous_OTime);
                AddFinishError(r, cr, TFinishError.error_Contiguous_OTime);
            }
        }
    }

    private void AddContiguousError(List<TEventRowCollectionItem> cl, int r, int position) {
        TEventRowCollectionItem cr;
        TEventRaceEntry re;
        for (int i = 0; i < cl.size(); i++) {
            cr = cl.get(i);
            re = cr.Race[r];
            if (re.OTime == position) {
                re.FinishErrors.Include(TFinishError.error_Contiguous_OTime);
                AddFinishError(r, cr, TFinishError.error_Contiguous_OTime);
            }
        }
    }

    private boolean CheckBib(TEventNode ev) {
        // Bib must be unique, should be > 0
        boolean result = true;
        FSortedSL.Clear();

        TEventRowCollection cl = ev.getBaseRowCollection();
        for (int i = 0; i < cl.getCount(); i++) {
            TEventRowCollectionItem cr = cl.get(i);
            String s = Utils.IntToStr(cr.Bib);
            // check for duplicates
            int foundIndex = FSortedSL.IndexOf(s);
            if (foundIndex > -1) {
                cr.EntryErrors.Include(TEntryError.error_Duplicate_Bib);
                AddEntryError(cr, TEntryError.error_Duplicate_Bib);
                result = false;
            } else {
                FSortedSL.Add(s);
            }
        }
        return result;
    }

    private void ClearFlags(TEventNode ev) {
        TEventRowCollection cl = ev.getBaseRowCollection();
        for (int i = 0; i < cl.getCount(); i++) {
            TEventRowCollectionItem cr = cl.get(i);
            cr.EntryErrors.Clear();
            for (int r = 1; r < cl.RCount(); r++) {
                cr.Race[r].FinishErrors.Clear();
            }
        }
    }

    private boolean CheckSNR(TEventNode ev) {
        // SNR must be unique, must be > 0
        TEventRowCollection cl = ev.getBaseRowCollection();
        FSortedSL.Clear();
        for (int i = 0; i < cl.getCount(); i++) {
            TEventRowCollectionItem cr = cl.get(i);
            String s = Utils.IntToStr(cr.SNR);
            // check for duplicates
            if (FSortedSL.IndexOf(s) > -1) {
                cr.EntryErrors.Include(TEntryError.error_Duplicate_SNR);
                AddEntryError(cr, TEntryError.error_Duplicate_SNR);
                return false;
            } else {
                FSortedSL.Add(s);
            }
        }
        return true;
    }

    protected void AddEntryError(TEventRowCollectionItem cr, int e) {
        String s = "Error." + TMain.BO.cTokenSport;
        if (cr != null) {
            s = s + ".ID" + Utils.IntToStr(cr.BaseID);
        }
        s = s + " = " + TEntryError.getString(e);
        FErrorList.Add(s);
    }

    protected void AddFinishError(int r, TEventRowCollectionItem cr, int e) {
        String s = "Error." + TMain.BO.cTokenSport + TMain.BO.cTokenRace + Utils.IntToStr(r);
        if (cr != null) {
            s = s + ".ID" + Utils.IntToStr(cr.BaseID);
        }
        s = s + " = " + TFinishError.getString(e);
        FErrorList.Add(s);
    }

    public boolean IsPreconditionForStrictInputMode(TEventNode ev) {
        TEventRowCollection cl = ev.getBaseRowCollection();
        if (ev.UseFleets) {
            if (cl.getCount() < 2)
                return true;
            for (int r = 1; r < cl.RCount(); r++) {
                int fc = cl.getFleetCount(r);
                for (int f = 0; f <= fc; f++) {
                    cl.FillFleetList(FL, r, f);
                    if (FL.size() > 0) {
                        if (!IsPreconditionForFleet(FL, r))
                            return false;
                    }
                }
            }
            return true;
        } else {
            return IsPreconditionForAll(cl);
        }
    }

    private boolean IsPreconditionForFleet(List<TEventRowCollectionItem> cl, int r) {

        int[] a;
        TEventRowCollectionItem cr;
        int EntryCount;
        int temp;
        boolean HasNull;

//exit at the first encounter of an error, only boolean result is important
        if (cl.size() < 2) {
            return false;
        }
        EntryCount = cl.size();
        a = new int[EntryCount + 1]; // SetLength(a, EntryCount + 1);

        // clear array slots
        for (int i = 0; i < a.length; i++) {
            a[i] = 0;
        }
        for (int i = 0; i < cl.size(); i++) {
            cr = cl.get(i);
            temp = cr.Race[r].OTime;
            if (temp < 0) {
                return false; // below lower limit
            }
            if (temp > EntryCount) {
                return false; // beyond upper limit
            }
            if ((temp > 0) && (a[temp] == 1)) {
                return false; // has duplicates
            }
            a[temp] = 1;
        }
        HasNull = false;
        for (int i = 1; i < cl.size(); i++) {
            if ((a[i] == 1) && HasNull) {
                return false; // not contiguous
            }
            if (a[i] == 0) {
                HasNull = true;
            }
        }
        return true;
    }

    private boolean IsPreconditionForAll(TEventRowCollection cl) {
        int[] a; // : array of Integer;
        TEventRowCollectionItem cr;
        int EntryCount;
        int temp;
        boolean HasNull;

        // exit at the first encounter of an error, only boolean result is important
        if (cl.getCount() < 2) {
            return false;
        }
        EntryCount = cl.getCount();
        a = new int[EntryCount + 1]; // SetLength(a, EntryCount + 1);
        for (int r = 1; r < cl.RCount(); r++) {
            // clear array slots
            for (int i = 0; i < a.length; i++) {
                a[i] = 0;
            }
            for (int i = 0; i < cl.getCount(); i++) {
                cr = cl.get(i);
                temp = cr.Race[r].OTime;
                if (temp < 0) {
                    return false; // below lower limit
                }
                if (temp > EntryCount) {
                    return false; // beyond upper limit
                }
                if ((temp > 0) && (a[temp] == 1)) {
                    return false; // has duplicates
                }
                a[temp] = 1;
            }
            HasNull = false;
            for (int i = 1; i < cl.getCount(); i++) {
                if ((a[i] == 1) && HasNull) {
                    return false; // not contiguous
                }
                if (a[i] == 0) {
                    HasNull = true;
                }
            }
        }
        return true;
    }

    public boolean CheckAll(TEventNode ev) {
        FErrorList.Clear();
        ClearFlags(ev);
        CheckOTime(ev);
        CheckBib(ev);
        CheckSNR(ev);
        return FErrorList.getCount() > 0;
    }

    public void getMsg(TStrings Memo) {
        for (int i = 0; i < FErrorList.getCount(); i++) {
            Memo.Add(FErrorList.getString(i));
        }
    }

    public boolean hasErrors() {
        return FErrorList.getCount() > 0;
    }
}
