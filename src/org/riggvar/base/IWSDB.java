package org.riggvar.base;

public interface IWSDB {
    boolean DBFileExists(String fn);

    boolean DBDirectoryExists(String dn);

    boolean DBCreateDir(String dn);

    boolean DBDeleteFile(String fn);

    void DBLoadFromFile(String fn, TStrings SL);

    void DBSaveToFile(String fn, TStrings SL);
}
