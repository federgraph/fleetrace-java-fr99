package org.riggvar.base;

public class TBOPersistent {
    public TBOPersistent() {
    }

    public void Assign(Object source) // throws java.lang.Exception
    {
        if (source instanceof TBOPersistent) {
            ((TBOPersistent) source).AssignTo(this);
        } else {
            AssignError(null);
        }
    }

    public void AssignTo(Object dest) // throws java.lang.Exception
    {
        if (dest instanceof TBOPersistent) {
            ((TBOPersistent) dest).AssignError(this);
        }
    }

    public void AssignError(Object source) // throws java.lang.Exception
    {
        String SourceName;
        if (source != null) {
            SourceName = source.getClass().getName();
        } else {
            SourceName = "null";
        }
        String ClassName = this.getClass().getName();
        String s = "cannot assign " + SourceName + " to " + ClassName;
        System.out.println(s);
        // throw new Exception(s);
    }
}
