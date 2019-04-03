package org.riggvar.base;

public interface TStrings {
    public int getCount();

    public Object getObject(int Idx);

    public String getString(int Idx);

    public void setString(int Idx, String s);

    public int IndexOf(String s);

    public void AddObject(String s, Object o);

    public void Clear();

    public void Add(String s);

    public void Insert(int Idx, String s);

    public void Delete(int Idx);

    public void setText(String s);

    public String getText();

    public String getCommaText();

    public void setCommaText(String s);

    public String getDelimitedText();

    public void setDelimitedText(String s);

    public String getName(int Idx);

    public String getValue(String Key);

    public void setValue(String Key, String s);

    public String getValueFromIndex(int Idx);

    public void SaveToFile(String fn);

    public void LoadFromFile(String fn);
}
