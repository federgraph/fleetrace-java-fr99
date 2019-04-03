package org.riggvar.eventkey;

import java.awt.*;

public class KeyMatrix {
    public static int BibCount = 100;
    public static int OldAge = -256;
    public static int minBands = 3;
    public static int maxBands = 16;
    public static int StopBand = maxBands / 4;

    public static Color[] BlendColor;

    public static TMatrix Model;

    public KeyMatrix() {
        int t = getRowCount() * getColCount();
        if (BibCount > t)
            BibCount = t;

        PrepareColors(Color.orange, Color.white, maxBands);
        Model = new TMatrix();
    }

    public static void PrepareColors(Color Col1, Color Col2, int maxBands) {
        BlendColor = new Color[maxBands + 1];

        int[] dRGB = new int[3];

        dRGB[0] = Col2.getRed() - Col1.getRed();
        dRGB[1] = Col2.getGreen() - Col1.getGreen();
        dRGB[2] = Col2.getBlue() - Col1.getBlue();

        BlendColor[0] = Col1;

        for (int z = 1; z < maxBands; z++) {
            double dr = BlendColor[0].getRed() + z * (dRGB[0] / maxBands);
            double dg = BlendColor[0].getGreen() + z * (dRGB[1] / maxBands);
            double db = BlendColor[0].getBlue() + z * (dRGB[2] / maxBands);

            int ir = (int) Math.round(dr);
            int ig = (int) Math.round(dg);
            int ib = (int) Math.round(db);

            BlendColor[z] = new Color(ir, ig, ib);
        }
        BlendColor[maxBands] = Col2;

    }

    public static int getColCount() {
        if (BibCount <= 80)
            return 20;
        else
            return 25;
    }

    public static int getRowCount() {
        int c = getColCount();
        int r = BibCount / c;
        if (BibCount % c > 0)
            r++;
        return r;
    }

}
