package org.riggvar.base;

public class TTokenList {
    private TBaseToken IndexedChildToken;

    public TTokenList(TBaseToken aOwner, String aTokenName, TBaseToken aChildToken) {
        IndexedChildToken = aChildToken;
        IndexedChildToken.Owner = aOwner;
        IndexedChildToken.setNameID(aTokenName);
    }

    public TBaseToken Token(int ID) {
        IndexedChildToken.TokenID = ID;
        return IndexedChildToken;
    }

}
