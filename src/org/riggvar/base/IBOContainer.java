package org.riggvar.base;

public interface IBOContainer {
    String GetTestData(); // get built in test data

    void CreateNew(TStrings SL); // use parameters in SL, do not load data

    void LoadNew(String Data); // use parameters in data + load data

    void RecreateBO(TAdapterParams BOParams); // use parameters, load old data

    void RecreateBOFromBackup(); // use parameters and data from backup
}
