package id.mezuu.mcdiscrot.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ConfigManager {
    private static ConfigManager instance = null;
    private Config config;
    private static final String CONFIG_PATH = "config/menkrep-discrot.yaml";

    private ConfigManager() {
        try {
            loadConfig();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static ConfigManager getInstance() {
        if (instance == null) {
            synchronized (ConfigManager.class) {
                if (instance == null) {
                    instance = new ConfigManager();
                }
            }
        }
        return instance;
    }

    private void loadConfig() throws IOException {
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
        Path configJsonPath = Paths.get(CONFIG_PATH);
        config = mapper.readValue(new File(configJsonPath.toString()), Config.class);
    }

    public Config getConfig() {
        return config;
    }
}
