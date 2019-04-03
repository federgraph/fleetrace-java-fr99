package org.riggvar.eventmenu;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.MalformedInputException;

public class TEventMenuConnectionHttp implements IEventMenuConnection {
    String FUrl;
    String FErrorMsg;
    boolean FHasError;

    boolean UnicodeExpected = true;
    private Charset charset = Charset.forName("UTF-8");
    private CharsetDecoder decoder = charset.newDecoder();
    boolean hasCodingError = false;

    @Override
    public String Get() {
        try {
            return Get(FUrl);
        } catch (Exception e) {
            FHasError = true;
            FErrorMsg = e.getMessage();
            return "";
        }
    }

    @Override
    public void Post(String s) {
    }

    @Override
    public String getUrl() {
        return FUrl;
    }

    @Override
    public void setUrl(String url) {
        FUrl = url;
    }

    @Override
    public boolean hasError() {
        return FHasError;
    }

    @Override
    public String getError() {
        return FErrorMsg;
    }

    public String Get(String s) throws Exception {
        try {
            // UrlEncoding is donw via URI class:
            // The multi-argument constructors quote illegal characters as
            // required by the components in which they appear.

            URL url = new URL(s);
            url = new URI(url.getProtocol(), url.getHost(), url.getFile(), null).toURL();
            // String t = url.toString(); //for debugging
            return GetRequest(url);
        } catch (MalformedURLException ex) {
            throw new Exception("malformed URL");
        } catch (MalformedInputException ex) {
            throw new Exception("malformed input, expected: utf-8");
        } catch (NullPointerException ex) {
            throw new Exception("cannot retrieve resource, malformed url? " + s);
        }
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
                result = processBuffer(data, bytesread);
            } else {
                StringBuffer sb = new StringBuffer(512);
                int bytesread = 0;
                int buffersize = 128;
                byte[] data = new byte[buffersize];
                int ch;
                while ((ch = is.read()) != -1) {
                    data[bytesread] = (byte) ch;
                    bytesread++;
                    if (bytesread == buffersize) {
                        sb.append(processBuffer(data, buffersize));
                        bytesread = 0;
                    }
                }
                if (bytesread > 0) {
                    sb.append(processBuffer(data, bytesread));
                }
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

    private String processBuffer(byte[] data, int bytesread) {
        if (UnicodeExpected) {
            try {
                return decoder.decode(ByteBuffer.wrap(data, 0, bytesread)).toString();
            } catch (CharacterCodingException ex) {
                hasCodingError = true;
                UnicodeExpected = false;
                return new String(data, 0, bytesread);
            }
        } else {
            return new String(data, 0, bytesread);
        }
    }

}
