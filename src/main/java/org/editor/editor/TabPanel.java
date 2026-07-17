package org.editor.editor;

import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.icons.FlatTabbedPaneCloseIcon;
import org.editor.config.AppConfig;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.*;
import java.util.function.BiConsumer;

import static com.formdev.flatlaf.FlatClientProperties.*;

/**
 * Tab panel container that manages multiple editor tabs with close buttons and
 * file icons.
 */
public class TabPanel extends JPanel {

    private final java.util.List<EditorTab> tabs = new ArrayList<>();
    private final JTabbedPane tabbedPane;
    private final AppConfig config;

    private static final String ICON_PATH = "images/fileicon/";
    private static final Map<String, String> FILE_ICONS = new HashMap<>();

    static {
        FILE_ICONS.put("css", ICON_PATH + "css.svg");
        FILE_ICONS.put("java", ICON_PATH + "java2.svg");
        FILE_ICONS.put("svg", ICON_PATH + "svg.svg");
        FILE_ICONS.put("yml", ICON_PATH + "yml.svg");
        FILE_ICONS.put("yaml", ICON_PATH + "yml.svg");
        FILE_ICONS.put("py", ICON_PATH + "python.svg");
        FILE_ICONS.put("js", ICON_PATH + "js.svg");
        FILE_ICONS.put("jsx", ICON_PATH + "js.svg");
        FILE_ICONS.put("ts", ICON_PATH + "js.svg");
        FILE_ICONS.put("tsx", ICON_PATH + "js.svg");
        FILE_ICONS.put("jpg", ICON_PATH + "pic.svg");
        FILE_ICONS.put("jpeg", ICON_PATH + "pic.svg");
        FILE_ICONS.put("png", ICON_PATH + "pic.svg");
        FILE_ICONS.put("gif", ICON_PATH + "pic.svg");
        FILE_ICONS.put("c", ICON_PATH + "c.svg");
        FILE_ICONS.put("cpp", ICON_PATH + "cpp.svg");
        FILE_ICONS.put("cc", ICON_PATH + "cpp.svg");
        FILE_ICONS.put("h", ICON_PATH + "c.svg");
        FILE_ICONS.put("hpp", ICON_PATH + "cpp.svg");
        FILE_ICONS.put("cs", ICON_PATH + "cs.svg");
        FILE_ICONS.put("go", ICON_PATH + "go.svg");
        FILE_ICONS.put("json", ICON_PATH + "json.svg");
    }

    public TabPanel(AppConfig config) {
        this.config = config;
        setLayout(new BorderLayout());

        tabbedPane = new JTabbedPane();
        tabbedPane.putClientProperty(TABBED_PANE_TAB_CLOSABLE, true);
        tabbedPane.putClientProperty(TABBED_PANE_TAB_CLOSE_TOOLTIPTEXT, "Close");
        tabbedPane.putClientProperty(TABBED_PANE_SHOW_TAB_SEPARATORS, true);
        tabbedPane.putClientProperty(TABBED_PANE_TAB_CLOSE_CALLBACK,
                (BiConsumer<JTabbedPane, Integer>) (pane, tabIndex) -> {
                    removeTab(tabIndex);
                });

        setupTabUI();
        add(tabbedPane, BorderLayout.CENTER);
    }

    private void setupTabUI() {
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);

        UIManager.put("TabbedPane.closeArc", 999);
        UIManager.put("TabbedPane.closeCrossFilledSize", 5.5f);
        UIManager.put("TabbedPane.closeIcon", new FlatTabbedPaneCloseIcon());
        tabbedPane.updateUI();
        UIManager.put("TabbedPane.closeArc", null);
        UIManager.put("TabbedPane.closeCrossFilledSize", null);
        UIManager.put("TabbedPane.closeIcon", null);
    }

    /** Create a new untitled tab. */
    public EditorTab addNewTab() {
        return addTab("Untitled", null, null, null);
    }

    /** Add a tab with content from a file. */
    public EditorTab openFile(String filePath, String content) {
        String fileName = org.editor.util.FileUtils.getFileName(filePath);
        String ext = org.editor.util.FileUtils.getFileExtension(filePath);
        return addTab(fileName, filePath, ext, content);
    }

    /** Create a new tab with a name and optional content. */
    public EditorTab addTab(String tabName, String filePath, String fileExtension, String content) {
        EditorTab tab = new EditorTab(tabName, filePath, fileExtension, config);
        if (content != null) {
            tab.setText(content);
        }

        Icon icon = getFileIcon(fileExtension);
        tabbedPane.addTab(tabName, icon, tab);
        tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
        tabs.add(tab);
        return tab;
    }

    /** Remove a tab by index. */
    public void removeTab(int index) {
        if (index < 0 || index >= tabs.size())
            return;
        tabbedPane.remove(index);
        tabs.remove(index);
    }

    /** Close the currently active tab. */
    public void closeActiveTab() {
        int index = tabbedPane.getSelectedIndex();
        if (index >= 0) {
            EditorTab tab = tabs.get(index);
            if (tab.isModified()) {
                int choice = JOptionPane.showConfirmDialog(this,
                        "Save changes to \"" + tab.getTabName() + "\"?",
                        "Unsaved Changes",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (choice == JOptionPane.YES_OPTION) {
                    if (!tab.save())
                        return;
                } else if (choice == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            removeTab(index);
        }
    }

    /** Close all tabs. */
    public void closeAllTabs() {
        while (getTabCount() > 0) {
            tabbedPane.remove(0);
            tabs.remove(0);
        }
    }

    /** Get the currently active editor tab, or null. */
    public EditorTab getActiveTab() {
        int index = tabbedPane.getSelectedIndex();
        if (index < 0 || index >= tabs.size())
            return null;
        return tabs.get(index);
    }

    /** Get tab by index. */
    public EditorTab getTabAt(int index) {
        if (index < 0 || index >= tabs.size())
            return null;
        return tabs.get(index);
    }

    /** Get the number of open tabs. */
    public int getTabCount() {
        return tabs.size();
    }

    /** Get the tabbed pane component. */
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /** Apply theme to all open tabs. */
    public void applyThemeToAll() {
        for (EditorTab tab : tabs) {
            tab.applyTheme();
        }
    }

    /** Apply settings (font, tab size, etc.) to all open tabs. */
    public void applySettingsToAll() {
        for (EditorTab tab : tabs) {
            tab.applySettings();
        }
    }

    /** Get file icon for a given extension. */
    private Icon getFileIcon(String extension) {
        if (extension == null || extension.isEmpty()) {
            return new FlatSVGIcon(ICON_PATH + "file_txt.svg");
        }
        String iconPath = FILE_ICONS.getOrDefault(extension.toLowerCase(), ICON_PATH + "file_txt.svg");
        return new FlatSVGIcon(iconPath);
    }

    public java.util.List<EditorTab> getAllTabs() {
        return Collections.unmodifiableList(tabs);
    }
}
