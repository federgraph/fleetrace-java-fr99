package org.riggvar.dal;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyManager {
    private static PropertyManager instance = new PropertyManager();
    private Properties propertiesMap = new Properties();

    private PropertyManager() {
        initialize();
    }

    public static PropertyManager getInstance() {
        return instance;
    }

    public String getProperty(String name) {
        String props = propertiesMap.getProperty(name);
        if (props == null)
            return new String();
        return props;
    }

    private void initialize() {
        String propfile = "FR68.properties";
        try {
            propertiesMap = getPropertiesFromResource(propfile);
        } catch (IOException e) {
            System.out.println("There was an error loading the PropertyMgr properties file (" + propfile + "). Cause:  "
                    + e.toString());
        }
    }

    private Properties getPropertiesFromResource(String resource) throws IOException {
        Properties props = new Properties();
        InputStream in = null;
        String propfile = resource;
        in = getResourceAsStream(propfile);
        props.load(in);
        in.close();
        return props;
    }

    private InputStream getResourceAsStream(String name) throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream(name);
        if (in == null)
            in = ClassLoader.getSystemResourceAsStream(name);
        if (in == null)
            throw new IOException("Could not find resource " + name);
        return in;
    }
}
