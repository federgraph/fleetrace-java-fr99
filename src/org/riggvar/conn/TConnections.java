package org.riggvar.conn;

public class TConnections extends TCollection<TConnections, TConnection> {
    private static final long serialVersionUID = 1L;

    public TServerIntern Server;

    public TConnections() {
        super();
    }

    @Override
    protected TConnection NewItem() {
        TConnection o = new TConnection();
        o.Collection = this;
        return o;
    }

}

//public class TConnections extends TBOCollection
//{
//    public TServerIntern Server;
//
//    public TConnections(Object aOwner, Class aItemClass)
//    {
//        super(aOwner, aItemClass);
//
//    }
//
//    public TConnection Add2()
//    {
//        return (TConnection)super.Add();
//    }
//
//    public TConnection getItem2(int Index)
//    {
//        if ( (Index < 0) || (Index >= getCount()))
//        {
//            return null;
//        }
//        else
//        {
//            return (TConnection)super.getItem(Index);
//        }
//    }
//
//    public void setItem2(int Index, TConnection value)
//    {
//        super.setItem(Index, value);
//    }
//}
