package org.riggvar.conn;

import java.net.URL;
import java.net.HttpURLConnection;
import java.io.InputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.*;
import java.io.*;
import java.util.*;
import org.riggvar.base.*;

public class TRestClient {
    TStringList SL = new TStringList();

    public String hostname = "gsup3";
    public int port = 80;
    public boolean debug = false;

    public String Get(String s) {
        try {
            URL u = new URL(s);
            return GetRequest(u);
        } catch (MalformedURLException ex) {
        } catch (IOException ex) {
            System.err.println(ex.getMessage());
        }
        return "";
    }

    // see SwitchController.getViaHttpConnection
    protected String GetRequest(URL u) throws IOException {
        String result = "";
        HttpURLConnection c = null;
        InputStream is = null;
        try {
            c = (HttpURLConnection) u.openConnection();

            // Getting the response code will open the connection,
            // send the request, and read the HTTP response headers.
            // The headers are stored until requested.
            int rc = c.getResponseCode();
            if (rc != HttpURLConnection.HTTP_OK) {
                throw new IOException("HTTP response code: " + rc);
            }
            is = c.getInputStream();
            // Get the length and process the data
            int len = c.getContentLength();
            if (len > 0) {
                int actual = 0;
                int bytesread = 0;
                byte[] data = new byte[len];
                while ((bytesread != len) && (actual != -1)) {
                    actual = is.read(data, bytesread, len - bytesread);
                    bytesread += actual;
                }
                result = new String(data);
            } else {
                StringBuffer sb = new StringBuffer(512);
                int bytesread = 0;
                int buffersize = 128;
                byte[] data = new byte[buffersize];
                int ch;
                while ((ch = is.read()) != -1) {
                    data[bytesread] = (byte) ch;
                    bytesread++;
                    if (bytesread == buffersize)
                        sb.append(new String(data));
                }
                if (bytesread > 0)
                    sb.append(new String(data, 0, bytesread));
                result = sb.toString();
            }
        } catch (ClassCastException e) {
            throw new IllegalArgumentException("Not an HTTP URL");
        } finally {
            if (is != null)
                is.close();
            if (c != null)
                c.disconnect();
        }
        return result;
    }

    public String Post(String path, TStrings Params) {

        // POST /FR88/FR/SendMsg.aspx HTTP/1.0
        // Connection: keep-alive
        // Content-Type: application/x-www-form-urlencoded
        // Content-Length: 36
        // Host: gsup3
        // Accept: text/html, */*
        // Accept-Encoding: identity
        // User-Agent: Mozilla/3.0 (compatible; Indy Library)
        // Cookie: ASP.NET_SessionId=rrwy2o45mnnwop45420gnq55
        //
        // SwitchID=261&msg=FR.%2A.W1.Bib1.RV=1

        SL.Clear();
        try {
            InetAddress addr = InetAddress.getByName(hostname);
            Socket socket = new Socket(addr, port);

            DataOutputStream raw = new DataOutputStream(socket.getOutputStream());
            Writer wr = new OutputStreamWriter(raw);

            String postData = "";
            String key, value;
            for (int i = 0; i < Params.getCount(); i++) {
                key = Params.getName(i);
                value = Params.getValue(key);

                if (i == 0)
                    postData = URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
                else
                    postData += "&" + URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8");
            }
            // command += "\r\n";

            String header = "POST " + path + " HTTP/1.0\r\n" + "Host: " + hostname + "\r\n" + "Accept: text/html\r\n"
            // + "Referer: http://localhost/index.htm\r\n"
            // + "Accept-Language: de\r\n"
                    + "Accept-Encoding: identity\r\n" + "Content-Type: application/x-www-form-urlencoded\r\n"
                    // + "User_Agent: Mozilla/3.0 (FR66)\r\n"
                    + "Content-Length: " + (postData.length()) + "\r\n"
                    // + "Connection: keep-alive\r\n" //must be false!
                    // + "Pragma: no-cache\r\n"
                    // + "Cookie: ASP.NET_SessionId=" + cooky
                    + "\r\n";

            if (debug) {
                System.out.println();
                System.out.println("--- normal post:");
                System.out.println();
                System.out.print(header);
                System.out.print(postData);
            }

            wr.write(header);
            wr.write(postData);
            wr.flush();

            if (debug) {
                System.out.println();
                System.out.println("--- answer:");
                System.out.println();
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                SL.Add(line);
                if (debug) {
                    System.out.println(line);
                }
            }
            wr.close();
            raw.close();

            socket.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        // Delete headers and first empty line
        while (SL.getCount() > 0) {
            // first empty line
            if (SL.getString(0).equals("")) {
                SL.Delete(0);
                break;
            } else
                SL.Delete(0); // header lines
        }

        // filter out empty lines
        for (int i = SL.getCount() - 1; i >= 0; i--) {
            if (SL.getString(i).equals("")) {
                SL.Delete(i);
            }
        }

        // return the first content line ( without trailing \r\n )
        if (SL.getCount() == 1)
            return SL.getString(0);

        // or all content lines
        if (SL.getCount() > 1)
            return SL.getText();
        else
            return "";
    }

    public String PostMultiPart(String path, Hashtable<String, String> Params) {
        SL.Clear();
        try {
            InetAddress addr = InetAddress.getByName(hostname);
            Socket socket = new Socket(addr, port);

            DataOutputStream raw = new DataOutputStream(socket.getOutputStream());
            Writer wr = new OutputStreamWriter(raw);

            String postData = "";
            String value;
            for (String key : Params.keySet()) {
                value = Params.get(key);
                postData += "--AAoBBoCC\r\n" + "content-disposition: form-data; name=\"" + key + "\"\r\n"
                        + "content-type: text/plain\r\n\r\n" + value + "\r\n";
            }

            String trail = "--AAoBBoCC--\r\n";

            String header = "POST " + path + " HTTP/1.0\r\n" + "Host: " + hostname + "\r\n"
                    + "Accept-Encoding: identity\r\n" + "Accept: text/html\r\n"
                    // + "Referer: http://gsup3\r\n"
                    // + "Accept-Language: de\r\n"
                    + "Content-Type: multipart/form-data; boundary=AAoBBoCC\r\n"
                    + "User_Agent: Mozilla/3.0 (compatible: FR66)\r\n" + "Content-Length: "
                    + (postData.length() + trail.length()) + "\r\n"
                    // + "Connection: keep-alive\r\n"
                    // + "Pragma: no-cache\r\n"
                    // + "Cookie: ASP.NET_SessionId=" + cookie
                    + "\r\n";

            if (debug) {
                System.out.println();
                System.out.println("--- multiline post:");
                System.out.println();
                System.out.print(header);
                System.out.print(postData);
                System.out.print(trail);
            }

            wr.write(header);
            wr.write(postData);
            wr.write(trail);
            wr.flush();

            if (debug) {
                System.out.println();
                System.out.println("--- answer:");
                System.out.println();
            }

            BufferedReader rd = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                SL.Add(line);
                if (debug) {
                    System.out.println(line);
                }
            }
            wr.close();
            raw.close();

            socket.close();
        } catch (Exception e) {
            System.out.println(e.toString());
        }

        // delete headers and first empty line
        while (SL.getCount() > 0) {
            if (SL.getString(0).equals("")) {
                SL.Delete(0);
                break;
            } else
                SL.Delete(0);
        }

        // return content
        return SL.getText();
    }

//    POST /FR88/FR/SendMsg.aspx HTTP/1.0
//    Connection: keep-alive
//    Content-Type: application/x-www-form-urlencoded
//    Content-Length: 36
//    Host: gsup3
//    Accept: text/html, */*
//    Accept-Encoding: identity
//    User-Agent: Mozilla/3.0 (compatible; Indy Library)
//    Cookie: ASP.NET_SessionId=rrwy2o45mnnwop45420gnq55
//
//    SwitchID=261&msg=FR.%2A.W1.Bib1.RV=1

//---------------------------------------------------------------------------

//    POST /FR88/FR/GetNewMessages.aspx HTTP/1.0
//    Connection: keep-alive
//    Content-Type: application/x-www-form-urlencoded
//    Content-Length: 25
//    Host: gsup3
//    Accept: text/html, */*
//    Accept-Encoding: identity
//    User-Agent: Mozilla/3.0 (compatible; Indy Library)
//    Cookie: ASP.NET_SessionId=rrwy2o45mnnwop45420gnq55
//
//    SwitchID=261&StartMsgID=0

//---------------------------------------------------------------------------

//    POST /FR88/FR/SendBackupAndLog.aspx HTTP/1.0
//    Connection: keep-alive
//    Content-Type: multipart/form-data; boundary=--------011008165758573
//    Content-Length: 1293
//    Host: gsup3
//    Accept: text/html, */*
//    Accept-Encoding: identity
//    User-Agent: Mozilla/3.0 (compatible; Indy Library)
//    Cookie: ASP.NET_SessionId=rrwy2o45mnnwop45420gnq55

//    ----------011008165758573
//    Content-Disposition: form-data; name="SwitchID"
//
//    261
//    ----------011008165758573
//    Content-Disposition: form-data; name="Backup"
//
//    #Params
//
//    DP.StartlistCount = 8
//    DP.ITCount = 0
//    DP.RaceCount = 2
//
//    #Event Properties
//
//    EP.Name = Regatta X
//    EP.Dates = Event Dates
//    EP.HostClub = Club Y
//    EP.PRO =
//    EP.JuryHead =
//    EP.ScoringSystem = Low Point System
//    EP.ScoringSystem2 = 0
//    EP.Throwouts = 0
//    EP.ThrowoutScheme = ByNumRaces
//    EP.DivisionName = 420
//    EP.InputMode = Strict
//    EP.RaceLayout = Finish
//    EP.NameSchema =
//    EP.FieldMap = SN
//    EP.FieldCaptions =
//    EP.FieldCount = 6
//    EP.NameFieldCount = 2
//    EP.NameFieldOrder = 041256
//    EP.ShowPosRColumn = False
//    EP.ShowCupColumn = False
//    EP.ColorMode = Normal
//    EP.UseFleets = False
//    EP.TargetFleetSize = 8
//    EP.FirstFinalRace = 20
//    EP.IsTimed = False
//    EP.UseCompactFormat = True
//
//    NameList.Begin
//    NameList.End
//
//    StartList.Begin
//    Pos;SNR;Bib
//    1;1000;1
//    2;1001;2
//    3;1002;3
//    4;1003;4
//    5;1004;5
//    6;1005;6
//    7;1006;7
//    8;1007;8
//    StartList.End
//
//    FinishList.Begin
//    SNR;Bib;R1;R2
//    1000;1;2;3
//    1001;2;7;4
//    1002;3;5;8
//    1003;4;1;7
//    1004;5;6;5
//    1005;6;8;6
//    1006;7;4;2
//    1007;8;3;1
//    FinishList.End
//
//    #W1
//
//
//    #W2
//
//
//    EP.IM = Strict
//
//    ----------011008165758573
//    Content-Disposition: form-data; name="Log"
//
//
//    ----------011008165758573--
//
}
