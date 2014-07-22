package com.ehsunbehravesh.shuli.application;

import com.ehsunbehravesh.shuli.exception.ApplicationException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.text.MessageFormat;
import java.util.Properties;
import java.util.Set;
import org.reflections.Reflections;

/**
 *
 * @author Ehsun Behravesh
 */
public class Application {

    protected final Properties config;

    public Application(InputStream inputStream) throws IOException, ApplicationException {
        this.config = new Properties();
        this.config.load(inputStream);

        init();
    }

    public Application(Properties config) throws ApplicationException {
        this.config = config;

        init();
    }

    protected final void init() throws ApplicationException {
        loadResources();
    }

    protected void loadResources() throws ApplicationException {
        String packag = config.getProperty(Config.SCAN_PACKAGE);

        if (packag == null) {
            throw new ApplicationException(MessageFormat.format(
                    "can not find the value for {0} in the application config file.",
                    Config.SCAN_PACKAGE), null);
        }

        Reflections reflections = new Reflections(packag);

        Set<Class<? extends Object>> allClasses = reflections.getSubTypesOf(Object.class);
    }

}
