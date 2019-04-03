package org.riggvar.base;

import java.io.*;

public class TWorkspaceFile {
    public int WorkspaceID;
    public String Key;
    public String Value;

    public String Path;
    public String Name;
    public String Ext;

    public void Prepare() {
        // TODO: test out TWorkspaceFile.Prepare()
        File fi = new File(Key);

        Path = fi.getPath();
        Name = fi.getName();

        int dotpos = Name.lastIndexOf('.');

        Ext = "";
        if (dotpos > -1 && Name.length() > dotpos + 1)
            Ext = Name.substring(dotpos + 1);
        if (dotpos > -1)
            Name = Name.substring(0, dotpos);

        // C#
        // FileInfo fi = new FileInfo(Key);
        // Ext = fi.Extension;
        // Path = fi.DirectoryName;
        // Name = fi.Name;
        // Name = Name.Substring(0, Name.LastIndexOf('.'));

        // Delphi
        // Ext := ExtractFileExt(Key);
        // Path := ExtractFilePath(Key);
        // Name := ExtractFileName(Key);
        // Name := ChangeFileExt(Name, "");
    }

    public void Clear() {
        WorkspaceID = 1;
        Key = "Trace\\Test.txt";
        Value = "abc";
        Prepare();
    }

}
