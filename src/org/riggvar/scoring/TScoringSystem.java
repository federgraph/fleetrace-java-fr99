package org.riggvar.scoring;

import javax.swing.*;

public class TScoringSystem {
    public static final int LowPoint = 0;
    public static final int Bonus = 1;
    public static final int BonusDSV = 2;

    public static String getString(int e) {
        switch (e) {
        case LowPoint:
            return "Low Point System";
        case Bonus:
            return "Bonus Point System";
        case BonusDSV:
            return "Bonus Point System DSV";
        }
        return "";
    }

    public static void InitComboBoxModel(DefaultComboBoxModel<String> m) {
        m.addElement("Low Point System");
        m.addElement("Bonus Point System");
        m.addElement("Bonus Point System DSV");
    }

}
