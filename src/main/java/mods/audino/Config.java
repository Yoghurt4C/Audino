package mods.audino;

import com.google.common.collect.ImmutableSet;
import cpw.mods.fml.common.Loader;

import java.io.*;
import java.lang.ref.WeakReference;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import static mods.audino.Audino.L;

public class Config {
    static boolean isInitialized = false;
    static int wrapAmount;
    static boolean showNBT, showOD, enableLinking;

    public static int getWrapAmount() {
        return wrapAmount;
    }

    public static boolean showNBT() {
        return showNBT;
    }

    public static boolean showOD() {
        return showOD;
    }

    public static boolean enableLinking() { return enableLinking; }

    private static void handleConfig(Map<String, String> cfg) {
        wrapAmount = getInt("wrapAmount", cfg);
        showNBT = getBool("showNBT", cfg);
        showOD = getBool("showOD", cfg);
        enableLinking = getBool("enableLinking", cfg);
    }

    public static void tryInit() {
        if (!isInitialized) init();
    }

    private static void init() {
        Map<String, String> cfg = new HashMap<>();
        ImmutableSet<? extends Entry<? extends Serializable>> entries = ImmutableSet.of(
                Entry.of("wrapAmount", 96,
                        "wrapAmount: The amount of columns after which the NBT will be wrapped. [Side: CLIENT | Default: 96]"),
                Entry.of("showNBT", true,
                        "showNBT: Shows the ItemStack NBT in Tooltips. [Side: CLIENT | Default: true]"),
                Entry.of("showOD", true,
                        "showOD: Shows the OreDictionary names the ItemStack belongs to. [Side: CLIENT | Default: true]"),
                Entry.of("enableLinking", true,
                        "enableLinking: Lets you showcase your items in chat for everyone to see. Stolen NinjaTips feature. [Side: BOTH | Default: true]")
        );
        Path configPath = Loader.instance().getConfigDir().toPath().resolve("Audino.properties");
        try {
            boolean changed = false;
            File configurationFile = configPath.toFile();
            if (Files.notExists(configPath) && !configPath.toFile().createNewFile()) {
                L.error("[Audino] Error creating config file \"" + configurationFile + "\".");
            }
            Properties config = new Properties();
            StringBuilder content = new StringBuilder().append("#Audino Configuration.\n");
            content.append("#Last generated at: ").append(new Date()).append("\n\n");
            FileInputStream input = new FileInputStream(configurationFile);
            config.load(input);
            for (Entry<?> entry : entries) {
                String key = entry.key;
                Object value = entry.value;
                Class<?> cls = entry.cls;
                if (config.containsKey(key)) {
                    Object obj = config.getProperty(key);
                    String s = String.valueOf(obj);
                    if (s.equals("")) {
                        L.error("[Audino] Error processing configuration file \"" + configurationFile + "\".");
                        L.error("[Audino] Expected configuration value for " + key + " to be present, found nothing. Using default value \"" + value + "\" instead.");
                        cfg.put(key, value.toString());
                    } else if (cls.equals(Integer.class)) {
                        try {
                            Integer.parseInt(s);
                            cfg.put(key, s);
                        } catch (NumberFormatException e) {
                            L.error("[Audino] Error processing configuration file \"" + configurationFile + "\".");
                            L.error("[Audino] Expected configuration value for " + key + " to be an integer, found \"" + s + "\". Using default value \"" + value + "\" instead.");
                            cfg.put(key, value.toString());
                        }
                    } else if (cls.equals(Float.class)) {
                        try {
                            Float.parseFloat(s);
                            cfg.put(key, s);
                        } catch (NumberFormatException e) {
                            L.error("[Audino] Error processing configuration file \"" + configurationFile + "\".");
                            L.error("[Audino] Expected configuration value for " + key + " to be a float, found \"" + s + "\". Using default value \"" + value + "\" instead.");
                            cfg.put(key, value.toString());
                        }
                    } else if (cls.equals(Boolean.class)) {
                        if (!"true".equalsIgnoreCase(s) && !"false".equalsIgnoreCase(s)) {
                            L.error("[Audino] Error processing configuration file \"" + configurationFile + "\".");
                            L.error("[Audino] Expected configuration value for " + key + " to be a boolean, found \"" + s + "\". Using default value \"" + value + "\" instead.");
                            cfg.put(key, value.toString());
                        } else cfg.put(key, s);
                    }
                } else {
                    changed = true;
                    config.setProperty(key, value.toString());
                    cfg.put(key, value.toString());
                }
                content.append("#").append(entry.comment.get()).append("\n");
                content.append(key).append("=").append(cfg.get(key)).append("\n");
            }
            if (changed) {
                FileWriter fw = new FileWriter(configurationFile, false);
                fw.write(content.toString());
                fw.close();
            }
            handleConfig(cfg);
            isInitialized = true;
        } catch (IOException e) {
            L.error("[Audino] Could not read/write config! Stacktrace: " + e);
        }
    }

    private static int getInt(String s, Map<String, String> cfg) {
        return Integer.parseInt(cfg.get(s));
    }

    private static boolean getBool(String s, Map<String, String> cfg) {
        return Boolean.parseBoolean(cfg.get(s));
    }

    private static class Entry<T> {
        private final String key;
        private final T value;
        private final WeakReference<String> comment;
        private final Class<T> cls;

        private Entry(String key, T value, String comment, Class<T> cls) {
            this.key = key;
            this.value = value;
            this.comment = new WeakReference<>(comment);
            this.cls = cls;
        }

        public static Entry<Integer> of(String key, int value, String comment) {
            return new Entry<>(key, value, comment, Integer.class);
        }

        public static Entry<Float> of(String key, float value, String comment) {
            return new Entry<>(key, value, comment, Float.class);
        }

        public static Entry<Boolean> of(String key, boolean value, String comment) {
            return new Entry<>(key, value, comment, Boolean.class);
        }
    }
}
