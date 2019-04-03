package org.riggvar.eventkey;

import java.awt.*;

public class TMatrix {
    int size = 256;
    private TMatrixItem[] FData;
    private int age;

    public TMatrix() {
        FData = new TMatrixItem[size];
        for (int i = 0; i < size; i++) {
            TMatrixItem mi = new TMatrixItem(this);
            mi.setValue("" + (i + 1));
            FData[i] = mi;
        }
    }

    public int getSize() {
        if (FData != null)
            return FData.length;
        else {
            return size;
        }
    }

    public int getCount() {
        return KeyMatrix.getColCount() * KeyMatrix.getRowCount();
    }

    public int getAge() {
        return age;
    }

    public void TimerTick() {
        age++;
    }

    public void ResetAge() {
        int count = FData.length;
        for (int i = 0; i < count; i++) {
            FData[i].setAge(KeyMatrix.OldAge);
        }
        age = 0;
    }

    public String getValue(int bib) {
        if (bib >= 0 && bib < KeyMatrix.BibCount) {
            return FData[bib].getValue();
        }
        return "";
    }

    public String getValue(int ACol, int ARow) {
        if (ACol >= 0 && ARow >= 0 && ACol < KeyMatrix.getColCount() && ARow < KeyMatrix.getRowCount()) {
            int i = getIndex(ACol, ARow);
            return getValue(i);
        }
        return "";
    }

    public void setAge(int ACol, int ARow) {
        if (ACol >= 0 && ARow >= 0 && ACol < KeyMatrix.getColCount() && ARow < KeyMatrix.getRowCount()) {
            int i = getIndex(ACol, ARow);
            FData[i].setAge(age);
        }
    }

    public Color getColor(int ACol, int ARow) {
        if (ACol >= 0 && ARow >= 0 && ACol < KeyMatrix.getColCount() && ARow < KeyMatrix.getRowCount()) {
            int i = getIndex(ACol, ARow);
            return FData[i].getColor();
        }
        return Color.white;
    }

    public int getIndex(int ACol, int ARow) {
        int i = ACol + ARow * KeyMatrix.getColCount();
        int c = getSize();
        if (i < 0)
            return 0;
        else if (i > c - 1)
            return c - 1;
        else
            return i;
    }

    public TMatrixItem getItem(int ACol, int ARow) {
        if (ACol >= 0 && ARow >= 0 && ACol < KeyMatrix.getColCount() && ARow < KeyMatrix.getRowCount()) {
            int i = getIndex(ACol, ARow);
            return FData[i];
        }
        return null;
    }

}
