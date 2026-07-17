package org.editor.editor;

import org.editor.config.AppConfig;
import org.editor.ui.StatusBar;
import org.fife.rsta.ac.LanguageSupportFactory;
import org.fife.ui.autocomplete.*;
import org.fife.ui.rsyntaxtextarea.*;
import org.fife.ui.rtextarea.Gutter;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import javax.swing.undo.UndoManager;
import java.awt.*;
import java.io.*;
import java.net.URL;
import java.util.Objects;

/**
 * A single editor tab containing an RSyntaxTextArea with syntax highlighting,
 * code folding, auto-completion, and a status bar.
 */
public class EditorTab extends JPanel {

    private final String tabName;
    private String filePath;
    private String fileExtension;
    private boolean modified;

    private final TextEditorPane textArea;
    private final StatusBar statusBar;
    private final UndoManager undoManager;

    private final AppConfig config;

    public EditorTab(String tabName, AppConfig config) {
        this(tabName, null, null, config);
    }

    public EditorTab(String tabName, String filePath, String fileExtension, AppConfig config) {
        this.tabName = tabName;
        this.filePath = filePath;
        this.fileExtension = fileExtension != null ? fileExtension.toLowerCase() : null;
        this.config = config;
        this.modified = false;
        this.undoManager = new UndoManager();

        setLayout(new BorderLayout());

        textArea = new TextEditorPane();
        statusBar = new StatusBar();

        initTextArea();
        initStatusBar();
    }

    private void initTextArea() {
        // Apply theme and font
        applyTheme();
        Font font = new Font(config.getFontFamily(), Font.PLAIN, config.getFontSize());
        textArea.setFont(font);

        // Editor settings
        textArea.setPaintTabLines(true);
        textArea.setTabSize(config.getTabSize());
        textArea.setMarginLineEnabled(true);
        textArea.setMarginLinePosition(config.getMarginLinePosition());
        textArea.setCodeFoldingEnabled(config.isCodeFolding());
        textArea.setMarkOccurrences(config.isMarkOccurrences());
        textArea.setLineWrap(config.isWordWrap());
        textArea.setEncoding("UTF-8");

        // Syntax highlighting based on file extension
        if (fileExtension != null) {
            String syntax = SyntaxMappings.getSyntaxForExtension(fileExtension);
            textArea.setSyntaxEditingStyle(syntax);
        } else {
            textArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_NONE);
        }

        // Auto-complete and language support
        LanguageSupportFactory.get().register(textArea);
        setupAutoComplete();

        // Caret listener for line/column updates
        textArea.addCaretListener(e -> {
            try {
                int pos = textArea.getCaretPosition();
                int line = textArea.getLineOfOffset(pos) + 1;
                int col = pos - textArea.getLineStartOffset(line - 1) + 1;
                statusBar.setLineColumn(line, col);
            } catch (Exception ex) {
                // ignore
            }
        });

        // Undo/redo
        textArea.getDocument().addUndoableEditListener(undoManager);

        // Error strip
        ErrorStrip errorStrip = new ErrorStrip(textArea);
        add(errorStrip, BorderLayout.LINE_END);

        // Scroll pane with line numbers and bookmarks
        RTextScrollPane scrollPane = new RTextScrollPane(textArea);
        scrollPane.setLineNumbersEnabled(config.isLineNumbers());
        Gutter gutter = scrollPane.getGutter();
        gutter.setBookmarkingEnabled(true);
        URL bookmarkUrl = getClass().getResource("/images/bookmark.png");
        if (bookmarkUrl != null) {
            gutter.setBookmarkIcon(new ImageIcon(bookmarkUrl));
        }

        add(scrollPane, BorderLayout.CENTER);

        // Re-apply theme after scroll pane is created (RTextScrollPane may override)
        applyTheme();
    }

    private void initStatusBar() {
        statusBar.changeSyntaxComboBox.addItemListener(e -> {
            String selected = (String) statusBar.changeSyntaxComboBox.getSelectedItem();
            String style = SyntaxMappings.DISPLAY_TO_SYNTAX.getOrDefault(selected, SyntaxConstants.SYNTAX_STYLE_NONE);
            textArea.setSyntaxEditingStyle(style);
        });

        // Set initial combo box selection based on file extension
        if (fileExtension != null) {
            String displayName = SyntaxMappings.getDisplayForExtension(fileExtension);
            statusBar.changeSyntaxComboBox.setSelectedItem(displayName);
        }
        add(statusBar, BorderLayout.SOUTH);
    }

    private void setupAutoComplete() {
        DefaultCompletionProvider provider = new DefaultCompletionProvider();
        provider.addCompletion(new BasicCompletion(provider, "abstract"));
        provider.addCompletion(new BasicCompletion(provider, "assert"));
        provider.addCompletion(new BasicCompletion(provider, "break"));
        provider.addCompletion(new BasicCompletion(provider, "case"));
        provider.addCompletion(new BasicCompletion(provider, "class"));
        provider.addCompletion(new BasicCompletion(provider, "const"));
        provider.addCompletion(new BasicCompletion(provider, "continue"));
        provider.addCompletion(new BasicCompletion(provider, "default"));
        provider.addCompletion(new BasicCompletion(provider, "do"));
        provider.addCompletion(new BasicCompletion(provider, "else"));
        provider.addCompletion(new BasicCompletion(provider, "enum"));
        provider.addCompletion(new BasicCompletion(provider, "extends"));
        provider.addCompletion(new BasicCompletion(provider, "final"));
        provider.addCompletion(new BasicCompletion(provider, "finally"));
        provider.addCompletion(new BasicCompletion(provider, "for"));
        provider.addCompletion(new BasicCompletion(provider, "if"));
        provider.addCompletion(new BasicCompletion(provider, "implements"));
        provider.addCompletion(new BasicCompletion(provider, "import"));
        provider.addCompletion(new BasicCompletion(provider, "interface"));
        provider.addCompletion(new BasicCompletion(provider, "new"));
        provider.addCompletion(new BasicCompletion(provider, "package"));
        provider.addCompletion(new BasicCompletion(provider, "private"));
        provider.addCompletion(new BasicCompletion(provider, "protected"));
        provider.addCompletion(new BasicCompletion(provider, "public"));
        provider.addCompletion(new BasicCompletion(provider, "return"));
        provider.addCompletion(new BasicCompletion(provider, "static"));
        provider.addCompletion(new BasicCompletion(provider, "super"));
        provider.addCompletion(new BasicCompletion(provider, "switch"));
        provider.addCompletion(new BasicCompletion(provider, "this"));
        provider.addCompletion(new BasicCompletion(provider, "throw"));
        provider.addCompletion(new BasicCompletion(provider, "throws"));
        provider.addCompletion(new BasicCompletion(provider, "try"));
        provider.addCompletion(new BasicCompletion(provider, "void"));
        provider.addCompletion(new BasicCompletion(provider, "while"));

        provider.addCompletion(new ShorthandCompletion(provider, "sysout",
                "System.out.println(", "System.out.println("));
        provider.addCompletion(new ShorthandCompletion(provider, "syserr",
                "System.err.println(", "System.err.println("));

        AutoCompletion ac = new AutoCompletion(provider);
        ac.setParameterAssistanceEnabled(true);
        ac.setAutoCompleteEnabled(true);
        ac.setAutoActivationEnabled(true);
        ac.setAutoActivationDelay(300);
        ac.install(textArea);
    }

    /**
     * Apply the editor color theme from config.
     */
    public void applyTheme() {
        try {
            Theme theme = Theme.load(getClass().getResourceAsStream("/themes/" + config.getEditorTheme() + ".xml"));
            if (theme != null) {
                theme.apply(textArea);
            }
        } catch (IOException e) {
            System.err.println("Failed to load theme: " + config.getEditorTheme());
        }
        // Reapply font (theme may override)
        Font font = new Font(config.getFontFamily(), Font.PLAIN, config.getFontSize());
        textArea.setFont(font);
    }

    /**
     * Apply editor settings from config without reloading theme.
     */
    public void applySettings() {
        Font font = new Font(config.getFontFamily(), Font.PLAIN, config.getFontSize());
        textArea.setFont(font);
        textArea.setTabSize(config.getTabSize());
        textArea.setMarginLinePosition(config.getMarginLinePosition());
        textArea.setCodeFoldingEnabled(config.isCodeFolding());
        textArea.setMarkOccurrences(config.isMarkOccurrences());
        textArea.setLineWrap(config.isWordWrap());
    }

    /** Set the text content of the editor. */
    public void setText(String text) {
        textArea.setText(text != null ? text : "");
        textArea.setCaretPosition(0);
        modified = false;
    }

    /** Get the text content of the editor. */
    public String getText() {
        return textArea.getText();
    }

    /** Save file. Returns false if save was cancelled or failed. */
    public boolean save() {
        if (filePath == null) {
            return saveAs();
        }
        try {
            org.editor.util.FileUtils.writeFile(new File(filePath), getText());
            modified = false;
            return true;
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Failed to save: " + e.getMessage(), "Error",
                    JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /** Save file with a new path. Returns false if cancelled or failed. */
    public boolean saveAs() {
        JFileChooser chooser = new JFileChooser();
        if (filePath != null) {
            chooser.setSelectedFile(new File(filePath));
        }
        int result = chooser.showSaveDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            filePath = file.getAbsolutePath();
            fileExtension = org.editor.util.FileUtils.getFileExtension(filePath);
            // Update title in tab
            if (getParent() instanceof JTabbedPane) {
                JTabbedPane parent = (JTabbedPane) getParent();
                int index = parent.indexOfComponent(this);
                if (index >= 0) {
                    parent.setTitleAt(index, org.editor.util.FileUtils.getFileName(filePath));
                }
            }
            return save();
        }
        return false;
    }

    // ===== Edit actions =====

    public void undo() {
        if (undoManager.canUndo())
            undoManager.undo();
    }

    public void redo() {
        if (undoManager.canRedo())
            undoManager.redo();
    }

    public void cut() {
        textArea.cut();
    }

    public void copy() {
        textArea.copy();
    }

    public void paste() {
        textArea.paste();
    }

    public void selectAll() {
        textArea.selectAll();
    }

    public void delete() {
        textArea.replaceSelection("");
    }

    // ===== Getters =====

    public String getTabName() {
        return tabName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getFileExtension() {
        return fileExtension;
    }

    public TextEditorPane getTextArea() {
        return textArea;
    }

    public boolean isModified() {
        return modified;
    }

    public void setModified(boolean modified) {
        this.modified = modified;
    }

    public UndoManager getUndoManager() {
        return undoManager;
    }
}
