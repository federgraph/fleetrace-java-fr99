package org.riggvar.base;

public class FREnumSet {
    private int count;
    private java.util.BitSet bits;

    public FREnumSet(int bitCount) {
        count = bitCount;
        bits = new java.util.BitSet(count);
    }

    public void Assign(Object source) {
        if (source instanceof FREnumSet) {
            FREnumSet e = (FREnumSet) source;
            count = e.Count();
            bits = new java.util.BitSet(count);
            for (int i = 0; i < count; i++) {
                if (e.IsMember(i)) {
                    bits.set(i);
                }
            }
        }
    }

    public void Clear() {
        for (int i = 0; i < count; i++) {
            bits.clear(i);
        }
    }

    public int Count() {
        return bits.size();
    }

    public boolean IsEmpty() {
        for (int i = 0; i < count; i++) {
            if (bits.get(i)) {
                return false;
            }
        }
        return true;
    }

    public int Low() {
        return 0;
    }

    public int High() {
        return count - 1;
    }

    public boolean IsMember(int i) {
        return bits.get(i);
    }

    public void Exclude(int i) {
        bits.clear(i);
    }

    public void Include(int i) {
        bits.set(i);
    }
}
