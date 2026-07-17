package org.editor;

import org.editor.config.AppConfig;
import org.editor.config.ConfigLoader;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ConfigLoaderTest {

    @Test
    void testLoadDefaultConfig() {
        ConfigLoader loader = new ConfigLoader();
        AppConfig config = loader.getConfig();
        assertNotNull(config);
        assertNotNull(config.getWindowTheme());
        assertNotNull(config.getEditorTheme());
        assertTrue(config.getFontSize() > 0);
        assertTrue(config.getWindowWidth() > 0);
        System.out.println("Window theme: " + config.getWindowTheme());
        System.out.println("Editor theme: " + config.getEditorTheme());
        System.out.println("Font size: " + config.getFontSize());
    }
}
