package utils;

import utils.config.DomainConfig;
import utils.config.ResourceConfig;

import java.util.Objects;

public class Test {

    DomainConfig domainConfig;
    ResourceConfig resourceConfig;
    WaitFor waitFor;

    public Test() {
    }


    public DomainConfig domainConfig() {
        return Objects.requireNonNullElseGet(domainConfig, () -> domainConfig = new DomainConfig(this));
    }

    public ResourceConfig resourceConfig() {
        return Objects.requireNonNullElseGet(resourceConfig, () -> resourceConfig = new ResourceConfig());
    }

    public WaitFor waitFor() {
        return Objects.requireNonNullElseGet(waitFor, () -> waitFor = new WaitFor(this));
    }
}
