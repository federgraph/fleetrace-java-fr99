package org.riggvar.base;

import java.util.*;
import java.io.*;

public class TStringList implements TStrings {
    private boolean useUnicode = true;

    private boolean isDelimiterDefined;
    private boolean isQuoteCharDefined;
//    private boolean isNameValueSeparatorDefined;

    private char FDelimiter = ',';
    private char FQuoteChar = '#';
//    private char FNameValueSeparator = '=';

    private int FUpdateCount = 0;

    private ArrayList<String> StringsA;
    private HashMap<String, Object> Objects;

    public TStringList() {
        StringsA = new ArrayList<String>();
        Objects = new HashMap<String, Object>();
    }

    public Object[] getStrings() {
        return StringsA.toArray();
    }

    public HashMap<String, Object> getObjects() {
        return Objects;
    }

    public void Assign(TStringList source) {
        {
            this.Clear();
            StringsA.addAll(source.StringsA);
            Objects.putAll(source.Objects);
        }
    }

    public int getCount() {
        return StringsA.size();
    }

    public Object getObject(int Idx) {
        return Objects.get(StringsA.get(Idx));
    }

    public Object setObject(int Idx, Object value) {
        // added 20. December 2005
        String s = "" + Idx;
        return Objects.put(s, value);
    }

    public String getString(int Idx) {
        if (Idx == -1)
            return "";
        return StringsA.get(Idx);
    }

    public void setString(int Idx, String s) {
        StringsA.set(Idx, s);
    }

    public int IndexOf(String s) {
        for (int i = 0; i < StringsA.size(); i++) {
            String temp = StringsA.get(i);
            if (temp.equals(s)) {
                return i;
            }
        }
        return -1;
    }

    public void AddObject(String s, Object o) {
        StringsA.add(s);
        Objects.put(s, o);
    }

    public void Clear() {
        StringsA.clear();
        Objects.clear();
    }

    public void Add(String s) {
        StringsA.add(s);
    }

    public void Insert(int Idx, String s) {
        StringsA.add(Idx, s);
    }

    public void Delete(int Idx) {
        Objects.remove(StringsA.get(Idx));
        StringsA.remove(Idx);
    }

    public String getPlatformText() {
        String s;
        if (File.separator.equals("/"))
            s = "\n";
        else
            s = "\r\n";
        return getAsString(s);
    }

    public String getText() {
        String s = "\r\n";
        return getAsString(s);
    }

    public void setText(String s) {
        setAsString(s, "\r\n");
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
        int Idx = this.IndexOfKey(Key);
        return this.getValueFromIndex(Idx);
    }

    public int IndexOfKey(String s) {
        if (s == null) {
            return -1;
        }
        if (s.length() == 0) {
            return -1;
        }
        for (int i = 0; i < StringsA.size(); i++) {
            String s1 = StringsA.get(i);
            String s2 = getKey(s1);
            if (s2.equals(s)) {
                return i;
            }
        }
        return -1;
    }

    private String getKey(String s) {
        if (s != null) {
            java.util.StringTokenizer t = new java.util.StringTokenizer(s, "=", false);
            if (t.countTokens() >= 2) {
                return t.nextToken();
            }
        }
        return "";
    }

    public void setValue(String Key, String Value) {
        int Idx = this.IndexOfKey(Key);
        if (Idx > -1) {
            StringsA.set(Idx, Key + '=' + Value);
        } else {
            StringsA.add(Key + '=' + Value);
        }
    }

    public String getValueFromIndex(int Idx) {
        String result = "";
        if (Idx > -1) {
            String s = StringsA.get(Idx);
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
        if (fn == null) {
            return;
        }

        try {
            File file = new File(fn);

            // Erzeugt einen Output-Reader zum Schreiben in die Datei.
            // FileWriter behandelt die Konvertierung der Codierung
            // internationaler Zeichen.

            OutputStreamWriter out;

            if (useUnicode)
                out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
            else
                out = new FileWriter(file);

            String text = getPlatformText();
            out.write(text);
            out.close();
        } catch (IOException e) {
        }

    }

    public void LoadFromFile(String fn) {
        try {
            File file = new File(fn);
            int size = (int) file.length();
            int chars_read = 0;

            InputStreamReader in;

            if (useUnicode)
                in = new InputStreamReader(new FileInputStream(file), "UTF-8");
            else
                in = new FileReader(file);

            char[] data = new char[size];
            while (in.ready()) {
                chars_read += in.read(data, chars_read, size - chars_read);
            }
            in.close();
            this.setText(new String(data, 0, chars_read));
        } catch (IOException e) {
            this.Clear();
        }
    }

    private String getAsString(String sep) {
        StringBuffer sb = new StringBuffer();
        boolean isFirstElement = true;
        for (int i = 0; i < StringsA.size(); i++) {
            String s = StringsA.get(i);
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

        String[] tmp = s.split(sep);
        for (int i = 0; i < tmp.length; i++)
            Add(tmp[i]);

        // following code swallows empty lines
//        java.util.StringTokenizer t = new java.util.StringTokenizer(s, sep, false);
//        while (t.hasMoreTokens())
//        {
//            String tmp = t.nextToken();
//            Add(tmp);
//        }
    }

    public void setQuoteChar(char AValue) {
        if (FQuoteChar != AValue || (isQuoteCharDefined == false)) {
            isQuoteCharDefined = true;
            FQuoteChar = AValue;
        }
    }

    public char getQuoteChar() {
        if (!isQuoteCharDefined) {
            setQuoteChar('"');
        }
        return FQuoteChar;
    }

    public char getDelimiter() {
        if (isDelimiterDefined == false) {
            setDelimiter(',');
        }
        return FDelimiter;
    }

    public void setDelimiter(char AValue) {
        if (FDelimiter != AValue || (!isDelimiterDefined)) {
            isDelimiterDefined = true;
            FDelimiter = AValue;
        }
    }

//    private char getNameValueSeparator()
//    {
//        if (!isNameValueSeparatorDefined)
//        {
//            setNameValueSeparator('=');
//
//        }
//        return FNameValueSeparator;
//    }

//    private void setNameValueSeparator(char AValue)
//    {
//        if (FQuoteChar != AValue || (!isNameValueSeparatorDefined))
//        {
//            isNameValueSeparatorDefined = true;
//            FNameValueSeparator = AValue;
//        }
//    }

    private static String AnsiExtractQuotedStr(String AValue, char AQuote) {
        int i = AValue.indexOf(AQuote);
        return i != -1 ? AValue.substring(0, i) : AValue;
    }

    static private String AnsiQuotedStr(String AValue, String AQuote) {
        if (AValue.startsWith(AQuote)) {
            if (AValue.endsWith(AQuote)) {
                return AValue;
            } else {
                return AValue + AQuote;
            }
        } else {
            if (AValue.endsWith(AQuote)) {
                return AQuote + AValue;
            } else {
                return AQuote + AValue + AQuote;
            }
        }
    }

    public String getCommaText() {
        // TStringsDefined oldDefined = FDefined;
        char oldDelim = FDelimiter;
        char oldQuote = FQuoteChar;
        FDelimiter = ',';
        // FQuoteChar = '"'; //040829
        try {
            return getDelimitedText();
        } finally {
            FDelimiter = oldDelim;
            FQuoteChar = oldQuote;
            // FDefined = oldDefined;
        }
    }

    public void setCommaText(String s) {
        FDelimiter = ',';
        setDelimitedText(s);
    }

    public String getDelimitedText() {
        int count = getCount();
        String result;

        if (count == 0) {
            return "";
        } else if (count == 1 && getString(0).equals("")) {
            result = "" + FQuoteChar;
        } else {
            result = "";
            for (int i = 0; i < getCount(); ++i) {
                String s = getString(i);
                if (s == null) {
                    s = "";
                }
                int p = 0;
                while (p < s.length() && s.charAt(p) > ' ' && s.charAt(p) != FQuoteChar && s.charAt(p) != FDelimiter) {
                    ++p;
                }
                if (p < s.length() && s.charAt(p) != '\0') {
                    s = AnsiQuotedStr(s, "" + FQuoteChar);
                }
                result = result + s + FDelimiter;
            }
        }
        return result.substring(0, result.length() - 1);
    }

    public void setDelimitedText(String AValue) {
        BeginUpdate();
        try {
            String s;
            char c;
            boolean test;

            Clear();

            int p = 0;

            // skip whitespace at begin
            while (p < AValue.length()) {
                c = AValue.charAt(p);
                if (!Character.isWhitespace(c))
                    break;
                ++p;
            }

            while (p < AValue.length()) {
                // special case: handle quoted token at position p
                // starts with quote and is not the last char in string
                if (AValue.charAt(p) == FQuoteChar && p + 1 < AValue.length()) {
                    s = AnsiExtractQuotedStr(AValue.substring(p + 1), FQuoteChar);
                    p = p + s.length() + 2;
                } else {
                    int p1 = p; // remember begin of current token
                    // fast forward to end of current token...
                    while (p < AValue.length()) {
                        c = AValue.charAt(p);
                        if (c == FDelimiter)
                            break;
//                        if (Character.isWhitespace(c))
//                            break;
                        if (Character.isISOControl(c))
                            break;
                        ++p;
                    }
                    s = AValue.substring(p1, p);
                }
                Add(s);

                // skip over whitespace at end of current token
                while (p < AValue.length()) {
                    c = AValue.charAt(p);
//                    if (c == FQuoteChar)
//                        ++p;
//                    else if (Character.isISOControl(c))
//                        p++;
                    if (Character.isWhitespace(c))
                        ++p;
                    else
                        break;
                }

                // if we are stopped at next delimiter
                if (p < AValue.length() && AValue.charAt(p) == FDelimiter) {
                    c = AValue.charAt(p);

                    // special case:
                    // this delimiter is the last char in string
                    // --> add an empty line
                    if (p + 1 >= AValue.length()) {
                        Add("");
                    }

                    // normal case:
                    // skip over whitespace at begin of current token
                    do {
                        ++p;
                        if (p >= AValue.length())
                            break;
                        else
                            c = AValue.charAt(p);
                        test = Character.isWhitespace(c); // || Character.isISOControl(c);
                    } while (p + 1 < AValue.length() && test);
                }

            }
        } finally {
            EndUpdate();
        }
    }

    private void BeginUpdate() {
        if (FUpdateCount == 0) {
            setUpdateState(true);

        }
        ++FUpdateCount;
    }

    private void EndUpdate() {
        --FUpdateCount;
        if (FUpdateCount == 0) {
            setUpdateState(false);
        }
    }

    private void setUpdateState(boolean b) {

    }

}
