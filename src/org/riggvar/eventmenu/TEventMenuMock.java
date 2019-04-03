package org.riggvar.eventmenu;

public class TEventMenuMock implements IEventMenu {

    private static String ImageRoot1 = "http://gsmac/CubeImages/Images02/";
    private static String ImageRoot2 = "http://gsmac/CubeImages/Images03/";
    private static String ResultRoot = "http://gsmac/CubeImages/Results05/";

    @Override
    public String GetCaption(int i) {
        switch (i) {
        case -1:
            return "no selection";
        case 1:
            return "49er";
        case 2:
            return "470 W";
        case 3:
            return "470 M";
        case 4:
            return "Laser Radial";
        case 5:
            return "Laser";
        case 6:
            return "Finn";
        case 7:
            return "RSX W";
        case 8:
            return "RSX M";
        case 9:
            return "Star";
        case 10:
            return "420";
        case 11:
            return "Opti";
        default:
            return "B" + i;
        }
    }

    @Override
    public String GetDataUrl(int i) {
        if (i > 0 && i <= getCount())
            return ResultRoot + String.format("Event-%.2d.xml", i);
        else
            return "";
    }

    @Override
    public String GetImageUrl(int i) {
        switch (i) {
        case 1:
            return ImageRoot1 + "Seite-1.png";
        case 2:
            return ImageRoot1 + "Seite-2.png";
        case 3:
            return ImageRoot1 + "Seite-3.png";
        case 4:
            return ImageRoot1 + "Seite-4.png";
        case 5:
            return ImageRoot1 + "Seite-5.png";
        case 6:
            return ImageRoot1 + "Seite-6.png";
        case 7:
            return ImageRoot2 + "Seite-1.png";
        case 8:
            return ImageRoot2 + "Seite-2.png";
        case 9:
            return ImageRoot2 + "Seite-3.png";
        case 10:
            return ImageRoot2 + "Seite-4.png";
        case 11:
            return ImageRoot2 + "Seite-5.png";
        default:
            return "";
        }
    }

    @Override
    public void Load(String data) {
    }

    @Override
    public String getComboCaption() {
        return "Test Data";
    }

    @Override
    public int getCount() {
        return 11;
    }

    @Override
    public boolean isMock() {
        return true;
    }

}
