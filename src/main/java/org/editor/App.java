package org.editor;

import com.formdev.flatlaf.*;
import com.formdev.flatlaf.util.SystemInfo;
import org.editor.config.AppConfig;
import org.editor.config.ConfigLoader;
import org.editor.ui.MainFrame;

import javax.swing.*;

/**
 * Application entry point for LightCoding editor.
 */
public class App {

    public static void main(String[] args) {
        // Load configuration
        ConfigLoader configLoader = new ConfigLoader();
        AppConfig config = configLoader.getConfig();

        // macOS specific settings
        if (SystemInfo.isMacOS) {
            System.setProperty("apple.laf.useScreenMenuBar", "true");
            System.setProperty("apple.awt.application.name", "LightCoding");
            System.setProperty("apple.awt.application.appearance", "system");
        }

        // Linux specific settings
        if (SystemInfo.isLinux) {
            JFrame.setDefaultLookAndFeelDecorated(true);
            JDialog.setDefaultLookAndFeelDecorated(true);
        }

        // Apply theme
        applyTheme(config.getWindowTheme());

        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame(config, configLoader);
            frame.setVisible(true);
        });
    }

    private static void applyTheme(String theme) {
        try {
            LookAndFeel laf;
            switch (theme) {
                case "Dark":
                    laf = new FlatDarkLaf();
                    break;
                case "IntelliJ":
                    laf = new FlatIntelliJLaf();
                    break;
                case "Light":
                    laf = new FlatLightLaf();
                    break;
                default:
                    laf = new FlatDarculaLaf();
                    break;
            }
            UIManager.setLookAndFeel(laf);
        } catch (Exception ex) {
            System.err.println("Failed to initialize Look and Feel: " + ex.getMessage());
        }
    }
}
