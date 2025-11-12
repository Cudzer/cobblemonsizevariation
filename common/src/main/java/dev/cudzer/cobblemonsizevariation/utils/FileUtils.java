package dev.cudzer.cobblemonsizevariation.utils;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.cudzer.cobblemonsizevariation.CobblemonSizeVariation;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtils {
    public static Path getSizePath(){
        return CobblemonSizeVariation.platform.getConfigDirectory().resolve(CobblemonSizeVariation.MOD_ID).resolve("sizes");
    }

    public static Path getSizeFile(String fileName){
        return getSizePath().resolve(fileName);
    }

    public static JsonObject createFile(Gson gson, JsonObject content, Path file){
        try{
            Files.createDirectories(Paths.get(file.toString()).getParent());
            FileWriter writer = new FileWriter(file.toString());
            gson.toJson(content, writer);
            writer.close();
            return content;
        }
        catch (IOException e){
            CobblemonSizeVariation.LOGGER.error("Could not create size file: {}", file);
            return null;
        }
    }
}
