package utils.config;
// to antistoixo tou: ResourceConfig , auti i klasi travaei ta payloads apo ta input/output dirs

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

public class ResourceConfig {

    public String getInputDir() {
        return getTestResourcesPath() + "/test-data/inputDir";
    }

    private String getTestResourcesPath() {
        return getResourcesPath("test");  //  "test" is the package name
    }

    private String getResourcesPath(String packageName) {
        String filePathString = getAbsolutePath() + "src/" + packageName + "/resources";
        File file = new File(filePathString);
        if(!file.exists())
            filePathString = getAbsolutePath();
        return filePathString;
    }

    private String getResourcesPath() {

        return getResourcesPath("main");
    }

    public String getAbsolutePath() {
        String absolutePath = Paths.get(".").toAbsolutePath().normalize().toString().replace("\\", "/");

        String modulePath = Objects.requireNonNull(this.getClass().getClassLoader().getResource(".")).getPath();
        modulePath = modulePath.replace("\\", "/");
        modulePath = modulePath.replace("target/test-classes", "");
        modulePath = modulePath.replace(absolutePath, "");
        modulePath = modulePath.replace("//", "/");

        return absolutePath + modulePath;
    }

    public String getEnvironmentProperties() {
        final String envProperties = "testing.properties";
        return System.getProperty(envProperties) == null ? getResourcesPath() + "/" + envProperties
                : getAbsolutePath() + System.getProperty(envProperties);
    }

}
