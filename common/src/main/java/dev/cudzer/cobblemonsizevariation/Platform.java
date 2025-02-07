package dev.cudzer.cobblemonsizevariation;

import com.cobblemon.mod.common.NetworkManager;

import java.nio.file.Path;

public interface Platform {
    boolean isModInstalled(String modId);
    Path getConfigDirectory();

    NetworkManager getNetworkManager();
}
