package org.editor.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Application configuration model.
 * Holds all user-configurable settings for the editor.
 */
public class AppConfig {

    // ===== Window Settings =====
    private int windowWidth = 1200;
    private int windowHeight = 700;
    private String windowTheme = "Darcula"; // Dark, Light, IntelliJ, Darcula

    // ===== Editor Settings =====
    private int fontSize = 16;
    private String fontFamily = "Monospaced";
    private String editorTheme = "monokai"; // monokai, dark, eclipse, idea, vs, nord, onedark, solarized_light,
                                            // solarized_dark, etc.
    private int tabSize = 4;
    private boolean wordWrap = false;
    private boolean lineNumbers = true;
    private boolean codeFolding = true;
    private boolean markOccurrences = true;
    private int marginLinePosition = 100;

    // ===== Recent Files =====
    private List<String> recentFiles = new ArrayList<>();
    private int maxRecentFiles = 10;

    // ===== Sidebar =====
    private boolean sidebarVisible = false;
    private int sidebarWidth = 250;
    private String lastOpenedFolder = "";

    // ===== Getters and Setters =====

    public int getWindowWidth() {
        return windowWidth;
    }

    public void setWindowWidth(int windowWidth) {
        this.windowWidth = windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public void setWindowHeight(int windowHeight) {
        this.windowHeight = windowHeight;
    }

    public String getWindowTheme() {
        return windowTheme;
    }

    public void setWindowTheme(String windowTheme) {
        this.windowTheme = windowTheme;
    }

    public int getFontSize() {
        return fontSize;
    }

    public void setFontSize(int fontSize) {
        this.fontSize = fontSize;
    }

    public String getFontFamily() {
        return fontFamily;
    }

    public void setFontFamily(String fontFamily) {
        this.fontFamily = fontFamily;
    }

    public String getEditorTheme() {
        return editorTheme;
    }

    public void setEditorTheme(String editorTheme) {
        this.editorTheme = editorTheme;
    }

    public int getTabSize() {
        return tabSize;
    }

    public void setTabSize(int tabSize) {
        this.tabSize = tabSize;
    }

    public boolean isWordWrap() {
        return wordWrap;
    }

    public void setWordWrap(boolean wordWrap) {
        this.wordWrap = wordWrap;
    }

    public boolean isLineNumbers() {
        return lineNumbers;
    }

    public void setLineNumbers(boolean lineNumbers) {
        this.lineNumbers = lineNumbers;
    }

    public boolean isCodeFolding() {
        return codeFolding;
    }

    public void setCodeFolding(boolean codeFolding) {
        this.codeFolding = codeFolding;
    }

    public boolean isMarkOccurrences() {
        return markOccurrences;
    }

    public void setMarkOccurrences(boolean markOccurrences) {
        this.markOccurrences = markOccurrences;
    }

    public int getMarginLinePosition() {
        return marginLinePosition;
    }

    public void setMarginLinePosition(int marginLinePosition) {
        this.marginLinePosition = marginLinePosition;
    }

    public List<String> getRecentFiles() {
        return recentFiles;
    }

    public void setRecentFiles(List<String> recentFiles) {
        this.recentFiles = recentFiles;
    }

    public int getMaxRecentFiles() {
        return maxRecentFiles;
    }

    public void setMaxRecentFiles(int maxRecentFiles) {
        this.maxRecentFiles = maxRecentFiles;
    }

    public boolean isSidebarVisible() {
        return sidebarVisible;
    }

    public void setSidebarVisible(boolean sidebarVisible) {
        this.sidebarVisible = sidebarVisible;
    }

    public int getSidebarWidth() {
        return sidebarWidth;
    }

    public void setSidebarWidth(int sidebarWidth) {
        this.sidebarWidth = sidebarWidth;
    }

    public String getLastOpenedFolder() {
        return lastOpenedFolder;
    }

    public void setLastOpenedFolder(String lastOpenedFolder) {
        this.lastOpenedFolder = lastOpenedFolder;
    }

    /**
     * Create a copy of this config.
     */
    public AppConfig copy() {
        AppConfig c = new AppConfig();
        c.windowWidth = this.windowWidth;
        c.windowHeight = this.windowHeight;
        c.windowTheme = this.windowTheme;
        c.fontSize = this.fontSize;
        c.fontFamily = this.fontFamily;
        c.editorTheme = this.editorTheme;
        c.tabSize = this.tabSize;
        c.wordWrap = this.wordWrap;
        c.lineNumbers = this.lineNumbers;
        c.codeFolding = this.codeFolding;
        c.markOccurrences = this.markOccurrences;
        c.marginLinePosition = this.marginLinePosition;
        c.recentFiles = new ArrayList<>(this.recentFiles);
        c.maxRecentFiles = this.maxRecentFiles;
        c.sidebarVisible = this.sidebarVisible;
        c.sidebarWidth = this.sidebarWidth;
        c.lastOpenedFolder = this.lastOpenedFolder;
        return c;
    }
}
