package org.editor.config;

import org.yaml.snakeyaml.Yaml;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

/**
 * Loads configuration from classpath default config and user config,
 * and saves user-modified configuration to ~/.lightcoding/config.yaml.
 */
public class ConfigLoader {

    private static final String DEFAULT_CONFIG_PATH = "config/defaultConfig.yaml";
    private static final Path USER_CONFIG_DIR = Paths.get(System.getProperty("user.home"), ".lightcoding");
    private static final Path USER_CONFIG_FILE = USER_CONFIG_DIR.resolve("config.yaml");

    private final AppConfig config;

    public ConfigLoader() {
        this.config = new AppConfig();
        loadDefaultConfig();
        loadUserConfig();
    }

    public AppConfig getConfig() {
        return config;
    }

    /**
     * Load default configuration from classpath resource.
     */
    private void loadDefaultConfig() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(DEFAULT_CONFIG_PATH)) {
            if (in == null) {
                System.err.println("Default config not found: " + DEFAULT_CONFIG_PATH);
                return;
            }
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(in);
            if (data == null)
                return;
            applyYamlToConfig(data, config);
        } catch (IOException e) {
            System.err.println("Failed to load default config: " + e.getMessage());
        }
    }

    /**
     * Load user configuration from ~/.lightcoding/config.yaml, merging over
     * defaults.
     */
    private void loadUserConfig() {
        if (!Files.exists(USER_CONFIG_FILE))
            return;

        try (InputStream in = Files.newInputStream(USER_CONFIG_FILE)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(in);
            if (data == null)
                return;
            applyYamlToConfig(data, config);
        } catch (IOException e) {
            System.err.println("Failed to load user config: " + e.getMessage());
        }
    }

    /**
     * Save current configuration to user config file.
     */
    public void saveConfig(AppConfig configToSave) {
        try {
            Files.createDirectories(USER_CONFIG_DIR);
            Map<String, Object> data = configToYamlMap(configToSave);
            Yaml yaml = new Yaml();
            try (Writer writer = Files.newBufferedWriter(USER_CONFIG_FILE)) {
                yaml.dump(data, writer);
            }
        } catch (IOException e) {
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }

    /**
     * Apply parsed YAML map to an AppConfig instance.
     */
    @SuppressWarnings("unchecked")
    private void applyYamlToConfig(Map<String, Object> data, AppConfig cfg) {
        Map<String, Object> window = (Map<String, Object>) data.get("window");
        if (window != null) {
            if (window.get("width") != null)
                cfg.setWindowWidth(toInt(window.get("width")));
            if (window.get("height") != null)
                cfg.setWindowHeight(toInt(window.get("height")));
            if (window.get("theme") != null)
                cfg.setWindowTheme((String) window.get("theme"));
        }

        Map<String, Object> editor = (Map<String, Object>) data.get("editor");
        if (editor != null) {
            if (editor.get("fontSize") != null)
                cfg.setFontSize(toInt(editor.get("fontSize")));
            if (editor.get("fontFamily") != null)
                cfg.setFontFamily((String) editor.get("fontFamily"));
            if (editor.get("theme") != null)
                cfg.setEditorTheme((String) editor.get("theme"));
            if (editor.get("tabSize") != null)
                cfg.setTabSize(toInt(editor.get("tabSize")));
            if (editor.get("wordWrap") != null)
                cfg.setWordWrap(toBool(editor.get("wordWrap")));
            if (editor.get("lineNumbers") != null)
                cfg.setLineNumbers(toBool(editor.get("lineNumbers")));
            if (editor.get("codeFolding") != null)
                cfg.setCodeFolding(toBool(editor.get("codeFolding")));
            if (editor.get("markOccurrences") != null)
                cfg.setMarkOccurrences(toBool(editor.get("markOccurrences")));
            if (editor.get("marginLinePosition") != null)
                cfg.setMarginLinePosition(toInt(editor.get("marginLinePosition")));
        }

        Map<String, Object> sidebar = (Map<String, Object>) data.get("sidebar");
        if (sidebar != null) {
            if (sidebar.get("visible") != null)
                cfg.setSidebarVisible(toBool(sidebar.get("visible")));
            if (sidebar.get("width") != null)
                cfg.setSidebarWidth(toInt(sidebar.get("width")));
        }

        if (data.get("lastOpenedFolder") != null)
            cfg.setLastOpenedFolder((String) data.get("lastOpenedFolder"));

        List<String> recent = (List<String>) data.get("recentFiles");
        if (recent != null)
            cfg.setRecentFiles(recent);
    }

    /**
     * Convert AppConfig to a YAML-friendly map structure.
     */
    private Map<String, Object> configToYamlMap(AppConfig cfg) {
        Map<String, Object> data = new LinkedHashMap<>();

        Map<String, Object> window = new LinkedHashMap<>();
        window.put("width", cfg.getWindowWidth());
        window.put("height", cfg.getWindowHeight());
        window.put("theme", cfg.getWindowTheme());
        data.put("window", window);

        Map<String, Object> editor = new LinkedHashMap<>();
        editor.put("fontSize", cfg.getFontSize());
        editor.put("fontFamily", cfg.getFontFamily());
        editor.put("theme", cfg.getEditorTheme());
        editor.put("tabSize", cfg.getTabSize());
        editor.put("wordWrap", cfg.isWordWrap());
        editor.put("lineNumbers", cfg.isLineNumbers());
        editor.put("codeFolding", cfg.isCodeFolding());
        editor.put("markOccurrences", cfg.isMarkOccurrences());
        editor.put("marginLinePosition", cfg.getMarginLinePosition());
        data.put("editor", editor);

        Map<String, Object> sidebar = new LinkedHashMap<>();
        sidebar.put("visible", cfg.isSidebarVisible());
        sidebar.put("width", cfg.getSidebarWidth());
        data.put("sidebar", sidebar);

        data.put("lastOpenedFolder", cfg.getLastOpenedFolder());
        data.put("recentFiles", cfg.getRecentFiles());

        return data;
    }

    private static int toInt(Object val) {
        if (val instanceof Number)
            return ((Number) val).intValue();
        if (val instanceof String)
            return Integer.parseInt((String) val);
        return 0;
    }

    private static boolean toBool(Object val) {
        if (val instanceof Boolean)
            return (Boolean) val;
        if (val instanceof String)
            return Boolean.parseBoolean((String) val);
        return false;
    }
}
