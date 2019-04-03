package org.riggvar.col;

import java.awt.*;

public class TColGridColors {
    public static Color clFocusCell = Color.yellow;
    public static Color clEditable = new Color(0xE8, 0xFF, 0xFF);

    public static Color clNormal = Color.white;
    public static Color clAlternate = new Color(0xFF, 0xFF, 0xE0);

    public static Color clMoneyGreen = new Color(0xC0, 0xDC, 0xC0);
    public static Color clCream = new Color(0xF0, 0xFB, 0xFF);

    public static Color clHellRot = new Color(0xFF, 0x80, 0x80);
    public static Color clTransRot = new Color(0xF5, 0xAA, 0x89);
    public static Color clHellBlau = new Color(0x88, 0x88, 0xFF);
    public static Color clTransBlau = new Color(0x93, 0xAD, 0xEC);

    public static Color clSkyBlue = new Color(0xEB, 0xCE, 0x87);

    public static Color clBtnFace = Color.lightGray;
    public static Color clDefault = Color.black;
    public static Color clLime = new Color(0, 255, 0);
    public static Color clMaroon = new Color(128, 0, 0);
    public static Color clNavy = new Color(0, 0, 128);
    public static Color clFuchsia = new Color(255, 0, 255);
    public static Color clOlive = new Color(128, 128, 0);
    public static Color clTeal = new Color(0, 128, 128);
    public static Color clPurple = new Color(128, 0, 128);
    public static Color clAqua = new Color(0, 255, 255);
    public static Color clSilver = new Color(0, 255, 255);
    public static Color clYellow = Color.yellow;

    public static String HTMLColor(Color c) {
        if (c == TColGridColors.clEditable) {
            return "#E8FFFF";
        }

        else if (c == TColGridColors.clNormal) {
            return "white";
        } else if (c == TColGridColors.clAlternate) {
            return "#FFFFE0";
        }

        else if (c == TColGridColors.clMoneyGreen) {
            return "#C0DCC0";
        } else if (c == TColGridColors.clCream) {
            return "#F0FBFF";
        }

        else if (c == TColGridColors.clHellBlau) {
            return "#8080FF";
        } else if (c == TColGridColors.clTransBlau) {
            return "#89AAF5";
        } else if (c == TColGridColors.clHellRot) {
            return "#FF8888";
        } else if (c == TColGridColors.clTransRot) {
            return "#ECAD93";
        } else if (c.getRGB() == 0x000080FF) {
            return "#FF8000";
        } else if (c == TColGridColors.clSkyBlue) {
            return "#87CEEB";
        } else if (c == TColGridColors.clNormal) {
            return "#E1FF80";
        } else if (c == TColGridColors.clBtnFace) {
            return "silver";
        } else if (c == Color.red) {
            return "red";
        } else if (c == TColGridColors.clMaroon) {
            return "maroon";
        } else if (c == Color.black) {
            return "black";
        } else if (c == Color.yellow) {
            return "yellow";
        } else if (c == TColGridColors.clOlive) {
            return "olive";
        } else if (c == TColGridColors.clLime) {
            return "lime";
        } else if (c == Color.green) {
            return "green";
        } else if (c == TColGridColors.clTeal) {
            return "teal";
        } else if (c == TColGridColors.clAqua) {
            return "aqua";
        } else if (c == Color.blue) {
            return "blue";
        } else if (c == TColGridColors.clNavy) {
            return "navy";
        } else if (c == TColGridColors.clSilver) {
            return "silver";
        } else if (c == TColGridColors.clPurple) {
            return "purple";
        } else if (c == TColGridColors.clFuchsia) {
            return "fuchsia";
        } else if (c == Color.white) {
            return "white"; // #FAEBD7"; //'AntiqueWhite';
        } else {
            return TranslateColor(c);
        }
    }

    public static String TranslateColor(Color c) {
        return String.format("#%1$2X%2$2X%3$2X", c.getRed(), c.getGreen(), c.getBlue());
    }

    // public static String CSSClass(TCellProp cellProp, TColGrid colGrid)
    public static String CSSClass(TCellProp cellProp) {
        Color c = cellProp.Color;
        String s = "";
        switch (cellProp.ColorClass) {
        case TColGridColorClass.AlternatingColor:
            s = "a";
            break;
        case TColGridColorClass.AlternatingEditableColor:
            s = "ae";
            break;
        case TColGridColorClass.Blank:
            return "";
        case TColGridColorClass.CurrentColor:
            s = "c";
            break;
        case TColGridColorClass.CustomColor:
            break;
        case TColGridColorClass.DefaultColor:
            s = "n";
            break;
        case TColGridColorClass.EditableColor:
            s = "e";
            break;
        case TColGridColorClass.FocusColor:
            return "";
        case TColGridColorClass.HeaderColor:
            s = "h";
            break;
        }
        if (s != "") {
            if (cellProp.Alignment == TColAlignment.taLeftJustify)
                s += "l";
            return " class=\"" + s + "\"";
        } else {
            if (cellProp.Alignment == TColAlignment.taLeftJustify)
                return " bgcolor=\"" + HTMLColor(c) + "\" aling=\"left\"";
            else
                return " bgcolor=\"" + HTMLColor(c) + "\" aling=\"right\"";
        }
    }

}
