package org.editor.config;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Editor color theme definitions.
 * Each theme has a name and a display name.
 */
public enum EditorTheme {

    MONOKAI("monokai", "Monokai"),
    DARK("dark", "Dark"),
    ECLIPSE("eclipse", "Eclipse"),
    IDEA("idea", "IntelliJ IDEA"),
    VS("vs", "Visual Studio"),
    DRUID("druid", "Druid"),
    DEFAULT("default", "Default"),
    DEFAULT_ALT("default-alt", "Default Alt"),
    NORD("nord", "Nord"),
    ONE_DARK("onedark", "One Dark"),
    SOLARIZED_LIGHT("solarized_light", "Solarized Light"),
    SOLARIZED_DARK("solarized_dark", "Solarized Dark");

    private final String id;
    private final String displayName;

    EditorTheme(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String getId() {
        return id;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getResourcePath() {
        return "/themes/" + id + ".xml";
    }

    /**
     * Find a theme by its ID string.
     */
    public static EditorTheme fromId(String id) {
        for (EditorTheme t : values()) {
            if (t.id.equals(id))
                return t;
        }
        return MONOKAI; // default fallback
    }

    /**
     * Get all themes as a map of id -> display name for UI use.
     */
    public static Map<String, String> getAllThemes() {
        Map<String, String> map = new LinkedHashMap<>();
        for (EditorTheme t : values()) {
            map.put(t.id, t.displayName);
        }
        return map;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
