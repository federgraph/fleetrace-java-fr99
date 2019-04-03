package org.riggvar.inspector;

import org.riggvar.col.*;

public class TNameValueNode extends
        TBaseNode<TNameValueColGrid, TNameValueBO, TNameValueNode, TNameValueRowCollection, TNameValueRowCollectionItem, TNameValueColProps, TNameValueColProp> {
    public TNameValueBO BO;

    public TNameValueNode(TNameValueBO cbo, String aNameID) {
        super();
        BO = cbo;
        NameID = aNameID;
        setBaseRowCollection(new TNameValueRowCollection());
        getBaseRowCollection().Owner = this;
    }

}
