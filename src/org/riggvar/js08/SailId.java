package org.riggvar.js08;

class SailId {
    public int value;

    public SailId(int sailID) {
        value = sailID;
    }

    public int compareTo(SailId that) {
        if (value < that.value)
            return -1;
        if (value == that.value)
            return 0;
        else
            return 1;
    }

    @Override
    public String toString() {
        return "" + value;
    }
}
