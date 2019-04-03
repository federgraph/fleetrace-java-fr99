package org.riggvar.js08;

abstract class BaseObject implements Cloneable, Comparable<BaseObject> {
    // ABSTRACT because
    // compareTo(object o) is not defined yet

    private long fLastModified;
    private long fCreateDate;

    @Override
    public abstract boolean equals(Object obj);

    public BaseObject() {
        super();
        long t = System.currentTimeMillis();
        fCreateDate = t;
        fLastModified = t; // want times to be the same
    }

    /**
     * default implementation, always false
     */
    public boolean isBlank() {
        return false;
    }

    public String getKey() {
        return Integer.toString(hashCode());
    }

    /**
     * this is to serve as parent for a "deep" clone concept wherein child objects
     * are cloned
     **/
    public Object deepClone() {
        Object c = clone();
        return c;
    }

    /**
     * to be called in JUnit tests to perform a "deeper" equality check than we may
     * need for performance purposes elsewhere. If not overridden by subclasses
     * merely calls the regular equals() method. Notably util.UtilTestCase calls
     * junitEquals on xmlEquals checks
     * 
     * @param obj
     * @return
     */
    public boolean junitEquals(Object obj) {
        return equals(obj);
    }

    /**
     * shallow clone, sub-objects cloned only where necessary for object integrity
     **/
    @Override
    public Object clone() {
        BaseObject newSO = null;
        try {
            newSO = (BaseObject) super.clone();
            newSO.fCreateDate = this.fCreateDate;
            newSO.fLastModified = this.fLastModified;
        } catch (CloneNotSupportedException e) {
            // does nothing
        }

        return newSO;
    }

}
