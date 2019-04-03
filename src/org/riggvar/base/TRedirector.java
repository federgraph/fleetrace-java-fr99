package org.riggvar.base;

import java.io.*;

import org.riggvar.bo.TMain;

public class TRedirector {
    public TRedirector() {
    }

    public boolean UseDB() {
        return TMain.UseDB;
    }

    private IDBWorkspace DBWorkspace() {
        return TMain.WorkspaceManager.DBWorkspace;
    }

    public boolean DBDirectoryExists(String dn) {
        if (UseDB())
            return true;
        else {
            File fi = new File(dn);
            return fi.exists() && fi.isDirectory();
        }
    }

    public boolean DBFileExists(String fn) {
        if (UseDB())
            return DBWorkspace().DBFileExists(fn);
        else
            return new File(fn).exists();
    }

    public boolean DBCreateDir(String dn) {
        if (UseDB())
            return true;
        else {
            // result := SysUtils.CreateDir(dn);
            File fi = new File(dn);
            return fi.mkdirs();
            // return Directory.CreateDirectory(dn) != null;
        }
    }

    public boolean DBDeleteFile(String fn) {
        if (UseDB())
            return DBWorkspace().DBDeleteFile(fn);
        else {
            // result := SysUtils.DeleteFile(fn); //Delphi
            // File.Delete(fn); //C#
            // new File(fn).delete();

            return true;
        }
    }

    public void DBSaveToFile(String fn, TStrings SL) {
        if (UseDB())
            DBWorkspace().DBSaveToFile(fn, SL);
        else
            SL.SaveToFile(fn);
    }

    public void DBLoadFromFile(String fn, TStrings SL) {
        if (UseDB())
            DBWorkspace().DBLoadFromFile(fn, SL);
        else
            SL.LoadFromFile(fn);
    }

}
