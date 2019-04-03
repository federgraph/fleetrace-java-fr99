package org.riggvar.web;

import java.util.LinkedList;

public class TWebStringList {

    public static final String crlf = "\r\n";

    private LinkedList<String> SL;

    public TWebStringList() {
        // SL = new ArrayList<String>();
        SL = new LinkedList<String>();
    }

    public void Clear() {
        SL.clear();
    }

    public int getCount() {
        return SL.size();
    }

    public void Add(String s) {
        SL.add(s);
    }

    public void AddFirst(String s) {
        SL.addFirst(s);
    }

    public void AddBefore(int index, String s) {
        if (index >= 0 && index < getCount())
            SL.add(index, s);
    }

    public void Delete(int index) {
        SL.remove(index);
    }

    public String getString(int index) {
        return SL.get(index);
    }

    public String getName(int index) {
        String s = getString(index);
        return s.split("=")[0];
    }

    public String getValue(int index) {
        String s = getString(index);
        return s.split("=")[1];
    }

    public String getText() {
        StringBuilder sb = new StringBuilder(SL.size() * 2);
        for (String s : SL) {
            sb.append(s);
            sb.append(crlf);
        }
        return sb.toString();
    }

    public void setText(String value) {
        SL.clear();
        for (String s : value.split(crlf)) {
            if (!s.equals(""))
                SL.add(s);
        }
    }
}
