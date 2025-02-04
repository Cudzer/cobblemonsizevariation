package dev.cudzer.cobblemonsizevariation;

import java.nio.file.Path;

public interface Platform {
    boolean isModInstalled(String modId);
    Path getConfigDirectory();
}
