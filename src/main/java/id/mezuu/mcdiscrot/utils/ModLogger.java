package id.mezuu.mcdiscrot.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModLogger {
    private static Logger instance = null;
    private static final String modid = "menkrep-discrot";

    private ModLogger() {}

    public static Logger getInstance() {
        if (instance == null) {
            instance = LoggerFactory.getLogger(modid);
        }

        return instance;
    }
}
