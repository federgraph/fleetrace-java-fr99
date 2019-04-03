package org.riggvar.base;

public class TTokenParser {
    private String sToken;
    private String sRest;
    private java.util.StringTokenizer Tokenizer;

    public TTokenParser(String s) {
        setString(s);
    }

    public String getToken() {
        return sToken;
    }

    public String getRest() {
        return sRest;
    }

    public void setString(String s) {
        Tokenizer = new java.util.StringTokenizer(s, ".", false);
        sToken = "";
        sRest = s;
    }

    public void NextToken() {
        if (Tokenizer.hasMoreTokens()) {
            sToken = Tokenizer.nextToken();
            if (sRest.length() > sToken.length() + 1)
                sRest = sRest.substring(sToken.length() + 1);
            else
                sRest = "";
        } else {
            sToken = sRest;
            sRest = "";
        }
    }

    public int NextTokenX(String TokenName) {
        NextToken();
        int result = -1;
        int l = TokenName.length();
        if (Utils.Copy(sToken, 1, l).equals(TokenName)) {
            sToken = Utils.Copy(sToken, l + 1, sToken.length() - l);
            result = Utils.StrToIntDef(sToken, -1);
        }
        return result;
    }
}
