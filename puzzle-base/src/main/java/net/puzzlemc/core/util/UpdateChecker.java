package net.puzzlemc.core.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import net.puzzlemc.core.PuzzleCore;
import net.minecraft.client.MinecraftClient;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.CompletableFuture;


public class UpdateChecker {
    private static final Gson GSON = new Gson();
    private static final String minecraftVersion = MinecraftClient.getInstance().getGame().getVersion().getId();
    public static final Logger logger = LogManager.getLogger(PuzzleCore.name);

    public static final Type UPDATE_TYPE_TOKEN = new TypeToken<Map<String, String>>(){}.getType();
    public static String latestVersion;
    public static boolean isUpToDate = true;

    public static void init() {
        CompletableFuture.supplyAsync(() -> {
            try (Reader reader = new InputStreamReader(new URL(PuzzleCore.UPDATE_URL).openStream())) {
                return GSON.<Map<String, String>>fromJson(reader, UPDATE_TYPE_TOKEN);
            } catch (MalformedURLException error) {
                logger.log(Level.ERROR, "Unable to check for updates because of connection problems: " + error.getMessage());
            } catch (IOException error) {
                logger.log(Level.ERROR, "Unable to check for updates because of an I/O Exception: " + error.getMessage());
            }

            return null;
        }).thenAcceptAsync(versionMap -> {
            if (versionMap != null && versionMap.containsKey(minecraftVersion)) {
                latestVersion = versionMap.get(minecraftVersion);
                if (!latestVersion.equals(minecraftVersion)) {
                    isUpToDate = false;
                    PuzzleCore.updateURL = PuzzleCore.website + "download/" + latestVersion.replace(PuzzleCore.name + " ","");
                    logger.log(Level.INFO, "There is a newer version of "+ PuzzleCore.name +" available: " + latestVersion);
                    logger.log(Level.INFO, "Please update immediately!");
                }
            } else {
                logger.log(Level.WARN, "A problem with the database occured, could not check for updates.");
            }
        }, MinecraftClient.getInstance());
    }
}