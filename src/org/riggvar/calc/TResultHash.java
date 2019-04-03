package org.riggvar.calc;

import java.text.*;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.riggvar.bo.*;
import org.riggvar.event.*;

public class TResultHash {

    Hasher hasher = new Hasher();

    class Hasher {
        boolean wantDashes = true;

        String GetHash(THashAlgo hashAlgo, String s) {
            try {
                switch (hashAlgo) {
                case MD5:
                    return MD5(s);
                case SHA1:
                    return SHA1(s);
                default:
                    return "";
                }
            } catch (NoSuchAlgorithmException e) {
                return ("Hasher: Algorithm not available.");
            } catch (UnsupportedEncodingException e) {
                return ("Hasher: Encoding not supported.");
            }
        }

        String convertToHex(byte[] data) {
            StringBuffer buf = new StringBuffer();
            int l = data.length;
            for (int i = 0; i < l; i++) {
                int halfbyte = (data[i] >>> 4) & 0x0F;
                int two_halfs = 0;
                do {
                    if ((0 <= halfbyte) && (halfbyte <= 9)) {
                        buf.append((char) ('0' + halfbyte));
                    } else {
                        buf.append((char) ('a' + (halfbyte - 10)));
                    }
                    halfbyte = data[i] & 0x0F;

                    if (wantDashes)
                        if (two_halfs == 1) // after each second
                            if (i < l - 1) // but not the last
                                buf.append((char) '-');
                } while (two_halfs++ < 1);
            }
            return buf.toString();
        }

        String SHA1(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            MessageDigest md;
            md = MessageDigest.getInstance("SHA-1");
            byte[] h = new byte[40];
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            h = md.digest();
            return convertToHex(h);
        }

        String MD5(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException {
            MessageDigest md;
            md = MessageDigest.getInstance("MD5");
            byte[] h = new byte[40];
            md.update(text.getBytes("iso-8859-1"), 0, text.length());
            h = md.digest();
            return convertToHex(h);
        }

    }

    public String getMD5() {
        algo = THashAlgo.MD5;
        Init();
        return GetRealHash();
    }

    public String getSHA1() {
        algo = THashAlgo.SHA1;
        Init();
        return GetRealHash();
    }

    public String Value() {
        return getSHA1();
    }

    public String MemoString() {
        Init();
        return GetMemoString();
    }

    int DebugLevel = 2;

    int[][] ResultArray;
    String ByteArray;

    THashAlgo algo;

    enum THashAlgo {
        MD5, SHA1
    }

    public TResultHash() {
    }

    private void Init() {
        InitResultArray();
        InitByteArray();
    }

    private int Count() {

        return TMain.BO.EventNode.getBaseRowCollection().size();
    }

    private void InitResultArray() {
        TEventRowCollection cl = TMain.BO.EventNode.getBaseRowCollection();
        ResultArray = new int[cl.size()][2];
        TEventRowCollectionItem cr;
        int v0, v1;
        for (int i = 0; i < cl.size(); i++) {
            cr = cl.get(i);
            cr = cl.get(cr.PLZ());
            v0 = cr.BaseID;
            v1 = cr.Race[0].CTime;
            ResultArray[i][0] = v0;
            ResultArray[i][1] = v1;
        }
    }

    private void InitByteArray() {
        StringBuilder sb = new StringBuilder();
        String s;
        int c = Count();
        for (int i = 0; i < c; i++) {
            s = EntryString(i);
            sb.append(s);
        }
        ByteArray = sb.toString();

    }

    private String GetMemoString() {
        String crlf = "\n";

        StringBuilder sb = new StringBuilder();

        algo = THashAlgo.MD5;
        String m0 = GetTestHash();
        String m1 = GetRealHash();

        algo = THashAlgo.SHA1;
        String s0 = GetTestHash();
        String s1 = GetRealHash();

        String TestMsg = "abc";
        String RealMsg = "MsgList, w/o line-breaks";

        String x1 = MessageFormat.format("<ResultHash algo=\"MD5\" value=\"{0}\" />", m1);
        // String x2 = MessageFormat.format("<ResultHash algo=\"SHA1\" value=\"{0}\" />", s1);

        sb.append(x1 + crlf);
        // sb.append(x2 + crlf);

        if (DebugLevel > 0) {
            sb.append(crlf);
            int c = Count();
            for (int i = 0; i < c; i++) {
                sb.append(EntryString(i) + crlf);
            }
        }

        if (DebugLevel > 1) {
            sb.append(crlf);
            sb.append("Test-Msg: " + TestMsg + crlf);
            sb.append("Real-Msg: " + RealMsg + crlf);

            sb.append(crlf);
            sb.append("Test-MD5 : " + m0 + crlf);
            sb.append("Test-SHA1: " + s0 + crlf);

            sb.append(crlf);
            sb.append("Test-Msg: " + m1 + crlf);
            sb.append("Real-Msg: " + s1 + crlf);
        }

        return sb.toString();
    }

    private String GetTestHash() {
        return hasher.GetHash(algo, "abc");
    }

    private String GetRealHash() {
        return hasher.GetHash(algo, ByteArray);
    }

    private String EntryString(int i) {
        // for ID=20 and Points(*100)=400, generate the following output
        // including the separating colon and the closing semicolon
        // '020:00400;'

        // %[argument_index$][flags][width]conversion

        // argument index: 1$ and 2$
        // flags: 0 (want leading zero's)
        // width: 3 and 5
        // conversion: d (Integral numbers)

        return String.format("%1$03d:%2$05d;", ResultArray[i][0], ResultArray[i][1]);
    }

}
