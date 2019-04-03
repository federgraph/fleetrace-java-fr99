package org.riggvar.eventkey;

import java.awt.*;

public class TMatrixItem {
    private String value;
    private int age;
    private TMatrix owner;

    public boolean isAmpelMode = false;

    public TMatrixItem(TMatrix aOwner) {
        owner = aOwner;
        age = KeyMatrix.OldAge;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String aValue) {
        value = aValue;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Color getColor() {
        int i = owner.getAge();
        i = i - age;

        if (age < 0)
            return Color.white;
        else if (i > KeyMatrix.maxBands - KeyMatrix.StopBand)
            return KeyMatrix.BlendColor[KeyMatrix.maxBands - KeyMatrix.StopBand];
        else if (i < 0)
            return Color.white;

        else {
            if (isAmpelMode) {
                if (i > 10)
                    return Color.white;
                else if (i > 5)
                    return Color.yellow;
                else if (i >= 0)
                    return Color.green.brighter();
                else
                    return Color.white;
            } else {
                return KeyMatrix.BlendColor[i];
            }
        }
    }

}
