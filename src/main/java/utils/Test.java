package utils;

import utils.config.EnvDataConfig;
import utils.config.ResourceConfig;
import utils.factory.WebServiceFactory;

import java.util.Objects;

public class Test {

    final WebServiceFactory webServiceFactory;
    EnvDataConfig envDataConfig;
    ResourceConfig resourceConfig;
    WaitFor waitFor;

    public Test() {
        webServiceFactory = new WebServiceFactory(Test.this);
    }


    public EnvDataConfig envDataConfig() {
        return Objects.requireNonNullElseGet(envDataConfig, () -> envDataConfig = new EnvDataConfig(this));
    }

    public ResourceConfig resourceConfig() {
        return Objects.requireNonNullElseGet(resourceConfig, () -> resourceConfig = new ResourceConfig());
    }

    public WaitFor waitFor() {
        return Objects.requireNonNullElseGet(waitFor, () -> waitFor = new WaitFor(this));
    }

    public WebServiceFactory api() {
        return webServiceFactory;
    }
}
