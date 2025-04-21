package utils;

import utils.config.EnvDataConfig;
import utils.config.ResourceConfig;

import java.util.Objects;

public class Test {

    EnvDataConfig envDataConfig;
    ResourceConfig resourceConfig;
    WaitFor waitFor;

    public Test() {
    }


    public EnvDataConfig domainConfig() {
        return Objects.requireNonNullElseGet(envDataConfig, () -> envDataConfig = new EnvDataConfig(this));
    }

    public ResourceConfig resourceConfig() {
        return Objects.requireNonNullElseGet(resourceConfig, () -> resourceConfig = new ResourceConfig());
    }

    public WaitFor waitFor() {
        return Objects.requireNonNullElseGet(waitFor, () -> waitFor = new WaitFor(this));
    }
}
