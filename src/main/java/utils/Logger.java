package utils;

import io.cucumber.core.logging.LoggerFactory;

public class Logger {

    public void info(String message) {
        LoggerFactory.getLogger(this.getClass()).info(() -> message);
    }

    public void warn(String message) {
        LoggerFactory.getLogger(this.getClass()).warn(() -> message);
    }

    public void error(String message) {
        LoggerFactory.getLogger(this.getClass()).error(() -> message);
    }

    public void debug(String message) {
        LoggerFactory.getLogger(this.getClass()).debug(() -> message);
    }
}
