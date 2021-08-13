package com.endremastered.endrem.config;

import com.endremastered.endrem.EndRemastered;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.fabricmc.loader.api.FabricLoader;


public class ConfigHandler {

    private static Path configFilePath;
    private static Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static boolean disableEyeOfEnder = false;
    public static String eyesLocateStructure = "minecraft:stronghold";


    public static void load() {
        Reader reader;
        if(getFilePath().toFile().exists()) {
            try {
                reader = Files.newBufferedReader(getFilePath());

                Data data = gson.fromJson(reader, Data.class);

                disableEyeOfEnder = data.common.disableEyeOfEnder;
                eyesLocateStructure = data.common.eyesLocateStructure;
                reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        save();
    }

    public static void save() {
        try {
            Writer writer = Files.newBufferedWriter(getFilePath());
            Data data = new Data(new Data.Common(disableEyeOfEnder, eyesLocateStructure));
            gson.toJson(data, writer);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path getFilePath() {
        if(configFilePath == null) {
            configFilePath = FabricLoader.getInstance().getConfigDir().resolve(EndRemastered.MOD_ID + ".json");
        }
        return configFilePath;
    }

    private static class Data {

        private Common common;

        public Data(Common common) {
            this.common = common;
        }

        private static class Common {
            private final String disableEyeOfEnderComment = "Enable/Disable usage of Ender Eyes";
            private final boolean disableEyeOfEnder;

            private final String eyesLocateStructureComment = "Changes the structure that End Remastered eyes track (set value to \"null\" to disable)";
            private final String eyesLocateStructure;


            private Common() {
                disableEyeOfEnder = false;
                eyesLocateStructure = "minecraft:stronghold";
            }

            private Common(boolean disableEyeOfEnder, String eyesLocateStructure) {
                this.disableEyeOfEnder = disableEyeOfEnder;
                this.eyesLocateStructure = eyesLocateStructure;
            }
        }
    }
}
