package utils.config;
// to antistoixo tou: EnvDataConfig

import utils.Logger;
import utils.Test;
import utils.enums.Application;
import utils.enums.User;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Enumeration;
import java.util.Properties;

public class DomainConfig {


    Test test;

    public DomainConfig(Test test) {
        this.test = test;
    }

    private static Properties loadProperties(String file) {
        Properties props = new Properties();
        try (InputStream inputStream = new FileInputStream(file);
             Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8)) {
            props.load(reader);
        } catch (Exception e) {
            new Logger().error(e.getMessage());
        }
        return props;
    }

    private static Properties getProperties(Properties params) {
        Properties result = new Properties();
        Enumeration<?> names = params.propertyNames();
        while (names.hasMoreElements()) {
            String name = (String) names.nextElement();
            result.put(name, params.get(name));
        }
        return result;
    }

    public String getUrl(Application application) {
        return getEnvProperties().getProperty(application + ".URL");
    }

    private Properties getEnvProperties() {
        return getProperties(loadProperties(test.resourceConfig().getEnvironmentProperties()));
    }

    public String getUsername(User user) {
        return getEnvProperties().getProperty(user + ".USERNAME");
    }

    public String getPassword(User user) {
        return getEnvProperties().getProperty(user + ".PASSWORD");
    }

    public String getInterval() {
        return getEnvProperties().getProperty("INTERVAL");
    }

    public String getTimeOut() {
        return getEnvProperties().getProperty("TIMEOUT");
    }

}
