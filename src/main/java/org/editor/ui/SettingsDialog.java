package org.editor.ui;

import org.editor.config.AppConfig;
import org.editor.config.EditorTheme;

import javax.swing.*;
import java.awt.*;
import java.util.Map;

/**
 * Settings dialog for configuring editor appearance: themes, font, font size.
 */
public class SettingsDialog extends JDialog {

    private final AppConfig config;
    private final java.util.function.Consumer<AppConfig> onApply;
    private boolean confirmed = false;

    private JComboBox<String> windowThemeCombo;
    private JComboBox<String> editorThemeCombo;
    private JComboBox<String> fontFamilyCombo;
    private JSpinner fontSizeSpinner;
    private JSpinner tabSizeSpinner;
    private JCheckBox wordWrapCheck;
    private JCheckBox lineNumbersCheck;
    private JCheckBox codeFoldingCheck;
    private JCheckBox markOccurrencesCheck;

    public SettingsDialog(Frame owner, AppConfig config, java.util.function.Consumer<AppConfig> onApply) {
        super(owner, "Settings", true);
        this.config = config.copy();
        this.onApply = onApply;
        initUI();
    }

    private void initUI() {
        setLayout(new BorderLayout());
        setSize(480, 500);
        setLocationRelativeTo(getOwner());

        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Appearance", createAppearancePanel());
        tabs.addTab("Editor", createEditorPanel());
        tabs.addTab("Behavior", createBehaviorPanel());

        add(tabs, BorderLayout.CENTER);

        // Bottom buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton okButton = new JButton("OK");
        JButton cancelButton = new JButton("Cancel");
        JButton applyButton = new JButton("Apply");

        okButton.addActionListener(e -> {
            confirmed = true;
            applySettings();
            dispose();
        });
        cancelButton.addActionListener(e -> dispose());
        applyButton.addActionListener(e -> applySettings());

        buttonPanel.add(applyButton);
        buttonPanel.add(okButton);
        buttonPanel.add(cancelButton);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    private JPanel createAppearancePanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        // Window theme
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Window Theme:"), gbc);
        windowThemeCombo = new JComboBox<>(new String[] { "Darcula", "Dark", "Light", "IntelliJ" });
        windowThemeCombo.setSelectedItem(config.getWindowTheme());
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(windowThemeCombo, gbc);

        // Editor theme
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        panel.add(new JLabel("Editor Theme:"), gbc);
        Map<String, String> themes = EditorTheme.getAllThemes();
        String[] themeNames = themes.values().toArray(new String[0]);
        String[] themeIds = themes.keySet().toArray(new String[0]);
        editorThemeCombo = new JComboBox<>(themeNames);
        // Set selection to current theme
        String currentTheme = config.getEditorTheme();
        for (int i = 0; i < themeIds.length; i++) {
            if (themeIds[i].equals(currentTheme)) {
                editorThemeCombo.setSelectedIndex(i);
                break;
            }
        }
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(editorThemeCombo, gbc);

        // Font family
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.weightx = 0;
        panel.add(new JLabel("Font Family:"), gbc);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        String[] fonts = ge.getAvailableFontFamilyNames();
        fontFamilyCombo = new JComboBox<>(fonts);
        fontFamilyCombo.setSelectedItem(config.getFontFamily());
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(fontFamilyCombo, gbc);

        // Font size
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.weightx = 0;
        panel.add(new JLabel("Font Size:"), gbc);
        fontSizeSpinner = new JSpinner(new SpinnerNumberModel(config.getFontSize(), 8, 48, 1));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(fontSizeSpinner, gbc);

        // Fill empty space
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        panel.add(new JLabel(), gbc);

        return panel;
    }

    private JPanel createEditorPanel() {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);

        // Tab size
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Tab Size:"), gbc);
        tabSizeSpinner = new JSpinner(new SpinnerNumberModel(config.getTabSize(), 1, 16, 1));
        gbc.gridx = 1;
        gbc.weightx = 1.0;
        panel.add(tabSizeSpinner, gbc);

        // Word wrap
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.weightx = 0;
        wordWrapCheck = new JCheckBox("Word Wrap", config.isWordWrap());
        gbc.gridx = 1;
        panel.add(wordWrapCheck, gbc);

        // Line numbers
        gbc.gridx = 0;
        gbc.gridy = 2;
        lineNumbersCheck = new JCheckBox("Show Line Numbers", config.isLineNumbers());
        gbc.gridx = 1;
        panel.add(lineNumbersCheck, gbc);

        // Code folding
        gbc.gridx = 0;
        gbc.gridy = 3;
        codeFoldingCheck = new JCheckBox("Code Folding", config.isCodeFolding());
        gbc.gridx = 1;
        panel.add(codeFoldingCheck, gbc);

        // Mark occurrences
        gbc.gridx = 0;
        gbc.gridy = 4;
        markOccurrencesCheck = new JCheckBox("Mark Occurrences", config.isMarkOccurrences());
        gbc.gridx = 1;
        panel.add(markOccurrencesCheck, gbc);

        // Fill
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2;
        gbc.weighty = 1.0;
        panel.add(new JLabel(), gbc);

        return panel;
    }

    private JPanel createBehaviorPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(16, 16, 16, 16));
        panel.add(new JLabel("<html><p>Additional behavior settings can be added here.</p>" +
                "<p>Window size and position are saved automatically on exit.</p></html>"));
        return panel;
    }

    private void applySettings() {
        // Window
        config.setWindowTheme((String) windowThemeCombo.getSelectedItem());

        // Editor theme
        Map<String, String> themes = EditorTheme.getAllThemes();
        String[] themeIds = themes.keySet().toArray(new String[0]);
        int editorIdx = editorThemeCombo.getSelectedIndex();
        if (editorIdx >= 0 && editorIdx < themeIds.length) {
            config.setEditorTheme(themeIds[editorIdx]);
        }

        // Font
        config.setFontFamily((String) fontFamilyCombo.getSelectedItem());
        config.setFontSize((Integer) fontSizeSpinner.getValue());

        // Editor settings
        config.setTabSize((Integer) tabSizeSpinner.getValue());
        config.setWordWrap(wordWrapCheck.isSelected());
        config.setLineNumbers(lineNumbersCheck.isSelected());
        config.setCodeFolding(codeFoldingCheck.isSelected());
        config.setMarkOccurrences(markOccurrencesCheck.isSelected());

        if (onApply != null) {
            onApply.accept(config);
        }
    }

    public boolean isConfirmed() {
        return confirmed;
    }
}
