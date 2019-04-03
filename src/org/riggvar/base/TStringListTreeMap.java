package org.riggvar.base;

import java.util.TreeMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class TStringListTreeMap implements TStrings {
    public String delimiter = ",";
    private String sep = ",";
    private TreeMap<Integer, String> Strings;
    private HashMap<Integer, Object> Objects;

    public TStringListTreeMap() {
        Strings = new TreeMap<Integer, String>();
        Objects = new HashMap<Integer, Object>();
    }

    public void Assign(Object source) {
    }

    public int getCount() {
        return Strings.size();
    }

    public Object getObject(int Idx) {
        return Objects.get(Idx);

    }

    public String getString(int Idx) {
        return Strings.get(Idx);
    }

    public void setString(int Idx, String s) {
        Strings.put(Idx, s);
    }

    public int IndexOf(String s) {
        return 0;
    }

    public void AddObject(String s, Object o) {
        Integer i = Strings.size();
        Strings.put(i, s);
        Objects.put(i, o);
    }

    public void Clear() {
        Strings.clear();
        Objects.clear();
    }

    public void Add(String s) {
        Integer i = Strings.size();
        Strings.put(i, s);
    }

    public void Insert(int Idx, String s) {
        Integer i = Idx;
        Strings.put(i, s);
    }

    public void Delete(int Idx) {
        Integer i = Idx;
        Strings.remove(i);
    }

    public void setText(String s) {
        setAsString(s, "\r\n");
    }

    public String getText() {
        String s = "\r\n";
        return getAsString(s);
    }

    private String getAsString(String sep) {
        java.util.Set<Entry<Integer, String>> es = Strings.entrySet();
        java.util.Iterator<Entry<Integer, String>> iterator = es.iterator();
        StringBuffer sb = new StringBuffer();
        boolean isFirstElement = true;
        while (iterator.hasNext()) {
            Map.Entry<Integer, String> entry = iterator.next();
            String s = entry.getValue();
            if (isFirstElement) {
                sb.append(s);
                isFirstElement = false;
            } else {
                sb.append(sep);
                sb.append(s);
            }
        }
        return sb.toString();
    }

    private void setAsString(String s, String sep) {
        this.Clear();
        java.util.StringTokenizer t = new java.util.StringTokenizer(s, sep, false);
        while (t.hasMoreTokens()) {
            String tmp = t.nextToken();
            Add(tmp);
        }
    }

    public String getCommaText() {
        return getAsString(sep);
    }

    public void setCommaText(String s) {
        setAsString(s, sep);
    }

    public String getDelimitedText() {
        return getAsString(delimiter);
    }

    public void setDelimitedText(String s) {
        setAsString(s, delimiter);
    }

    public String getName(int Idx) {
        String result = "";
        String s = this.getString(Idx);
        if (s != null) {
            java.util.StringTokenizer t = new java.util.StringTokenizer(s, "=", false);
            if (t.hasMoreTokens()) {
                result = t.nextToken();
            }
        }
        return result;
    }

    public String getValue(String Key) {
        int Idx = this.IndexOf(Key);
        return this.getValueFromIndex(Idx);
    }

    public void setValue(String Key, String Value) {
        int Idx = this.IndexOf(Key);
        if (Idx > -1) {
            Integer i = Idx;
            String s = Strings.get(i);
            if (s != null) {
                java.util.StringTokenizer t = new java.util.StringTokenizer(s, "=", false);
                if (t.countTokens() >= 2) {
                    String n = t.nextToken();
                    Strings.put(i, n + '=' + Value);
                }
            }
        }
    }

    public String getValueFromIndex(int Idx) {
        String result = "";
        if (Idx > -1) {
            String s = Strings.get(Idx);
            if (s != null) {
                java.util.StringTokenizer t = new java.util.StringTokenizer(s, "=", false);
                if (t.countTokens() >= 2) {
                    t.nextToken();
                    result = t.nextToken();
                }
            }
        }
        return result;
    }

    public void SaveToFile(String fn) {
    }

    public void LoadFromFile(String fn) {
    }
}
