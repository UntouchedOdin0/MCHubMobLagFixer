package me.untouchedodin0.mchubmoblagfixer.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MCHubMobLagFixerConfig {

    // Default distance in blocks
    private static int renderDistance = 128;
    private static final File CONFIG_FILE = new File("config/mchubmoblagfixer.json");
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static int getRenderDistance() {
        return renderDistance;
    }

    public static void setRenderDistance(int distance) {
        renderDistance = distance;
        save(); // save immediately
    }

    // Convenience method: returns the squared distance for your Mixin
    public static double getRenderDistanceSquared() {
        return renderDistance * renderDistance;
    }

    public static void load() {
        if (!CONFIG_FILE.exists()) return;
        try (FileReader reader = new FileReader(CONFIG_FILE)) {
            ConfigData data = GSON.fromJson(reader, ConfigData.class);
            renderDistance = data.renderDistance;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(new ConfigData(renderDistance), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class ConfigData {
        int renderDistance;

        ConfigData(int renderDistance) {
            this.renderDistance = renderDistance;
        }
    }
}
