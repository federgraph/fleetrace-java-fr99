package org.riggvar.scoring;

public class TRemoteScorer {
    public static int MsgCounter = 0;

    public TRemoteScorer() {
    }

    public String Calc(String xml) {
        MsgCounter++;
        String s = xml;

        int i = xml.indexOf("<?xml ");
        if (i == 3 || xml.startsWith("<?xml ")) {
            System.out.print("message " + MsgCounter + "(" + xml.length() + ")");

            // read xml into object
            TFRProxy p = new TFRProxy();
            if (i == 3)
                p.ReadFromString(xml.substring(3));
            else
                p.ReadFromString(xml);

            // load new regatta from data in p,
            // calc (scoreRegatta in javascore),
            // and retrieve (unload) calculated data into p

            // new TProxyLoaderJS08().Calc(p);
            // new TFRProxyLoader().Calc(p);
            TProxyLoader.getInstance().Calc(p);

            // create new xml msg (fill StringBuffer inside XmlWriter)
            // form data in p
            XmlWriter xw = new XmlWriter();
            p.WriteXml(xw);
            p = null;

            // log new xml to console when debugging
            // System.out.println(xw.toString());

            // send back answer over network
            s = xw.toString();
            xw = null;
        } else {
            System.err.println(xml);
        }
        return s;
    }
}
