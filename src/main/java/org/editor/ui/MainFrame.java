package org.editor.ui;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import com.formdev.flatlaf.extras.FlatSVGUtils;
import org.editor.config.AppConfig;
import org.editor.config.ConfigLoader;
import org.editor.editor.EditorTab;
import org.editor.editor.TabPanel;
import org.editor.util.FileUtils;
import org.fife.rsta.ui.GoToDialog;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

/**
 * Main application window for the LightCoding editor.
 */
public class MainFrame extends JFrame {

    private final AppConfig config;
    private final ConfigLoader configLoader;

    // Core components
    private final TabPanel tabPanel;
    private final WorkSpacePanel workSpacePanel;
    private final SearchBarManager searchBarManager;
    private final JSplitPane splitPane;
    private final JLabel searchStatusLabel;

    // Toolbar buttons
    private JToggleButton sidebarButton;

    public MainFrame(AppConfig config, ConfigLoader configLoader) {
        this.config = config;
        this.configLoader = configLoader;

        // Initialize components
        tabPanel = new TabPanel(config);
        workSpacePanel = new WorkSpacePanel();
        searchStatusLabel = new JLabel(" ");
        searchBarManager = new SearchBarManager(searchStatusLabel);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);

        initFrame();
        setupWorkSpaceHandler();

        // Create initial untitled tab
        tabPanel.addNewTab();
    }

    private void initFrame() {
        // Window setup
        setTitle("LightCoding");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(config.getWindowWidth(), config.getWindowHeight());

        // Center on screen
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        setLocation(
                (screenSize.width - config.getWindowWidth()) / 2,
                (screenSize.height - config.getWindowHeight()) / 2);

        setIconImages(FlatSVGUtils.createWindowIconImages("/images/div.svg"));

        // Window close handler - save config
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                onWindowClosing();
            }
        });

        // Menu bar
        setJMenuBar(createMenuBar());

        // Layout
        setLayout(new BorderLayout());

        // Toolbar
        add(createToolBar(), BorderLayout.NORTH);

        // Search panel (collapsible)
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.add(tabPanel, BorderLayout.CENTER);
        add(centerPanel, BorderLayout.CENTER);

        // Search status
        searchStatusLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 8));
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(searchStatusLabel, BorderLayout.WEST);

        // Bottom: search bars (collapsible) + status
        JPanel bottomArea = new JPanel(new BorderLayout());
        bottomArea.add(searchBarManager.getPanel(), BorderLayout.CENTER);
        bottomArea.add(southPanel, BorderLayout.SOUTH);
        add(bottomArea, BorderLayout.SOUTH);

        // Tab change listener - update search target
        tabPanel.getTabbedPane().addChangeListener(e -> {
            EditorTab active = tabPanel.getActiveTab();
            if (active != null) {
                searchBarManager.setTextArea(active.getTextArea());
            }
        });
    }

    private void setupWorkSpaceHandler() {
        workSpacePanel.setFileOpenHandler(filePath -> {
            try {
                File file = new File(filePath);
                if (!file.exists() || !file.isFile())
                    return;
                String content = FileUtils.readFile(file);
                tabPanel.openFile(filePath, content);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to open file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }

    // ============ Menu Bar ============

    private JMenuBar createMenuBar() {
        JMenuBar mb = new JMenuBar();
        mb.add(createFileMenu());
        mb.add(createEditMenu());
        mb.add(createSearchMenu());
        mb.add(createViewMenu());
        return mb;
    }

    private JMenu createFileMenu() {
        JMenu menu = new JMenu("File");
        menu.setMnemonic('F');

        JMenuItem newItem = new JMenuItem("New");
        newItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_N, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        newItem.addActionListener(e -> tabPanel.addNewTab());
        menu.add(newItem);

        JMenuItem openItem = new JMenuItem("Open...");
        openItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_O, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        openItem.addActionListener(e -> openFile());
        menu.add(openItem);

        JMenuItem openFolderItem = new JMenuItem("Open Folder...");
        openFolderItem.addActionListener(e -> openFolder());
        menu.add(openFolderItem);

        menu.addSeparator();

        JMenuItem saveItem = new JMenuItem("Save");
        saveItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_S, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        saveItem.addActionListener(e -> saveFile());
        menu.add(saveItem);

        JMenuItem saveAsItem = new JMenuItem("Save As...");
        saveAsItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx() | InputEvent.SHIFT_DOWN_MASK));
        saveAsItem.addActionListener(e -> saveFileAs());
        menu.add(saveAsItem);

        menu.addSeparator();

        JMenuItem closeItem = new JMenuItem("Close Tab");
        closeItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_W, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        closeItem.addActionListener(e -> tabPanel.closeActiveTab());
        menu.add(closeItem);

        menu.addSeparator();

        JMenuItem settingsItem = new JMenuItem("Settings...");
        settingsItem.addActionListener(e -> openSettings());
        menu.add(settingsItem);

        menu.addSeparator();

        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Q, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        exitItem.addActionListener(e -> onWindowClosing());
        menu.add(exitItem);

        return menu;
    }

    private JMenu createEditMenu() {
        JMenu menu = new JMenu("Edit");
        menu.setMnemonic('E');

        JMenuItem undoItem = new JMenuItem("Undo");
        undoItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Z, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        undoItem.addActionListener(e -> getActiveTab().undo());
        menu.add(undoItem);

        JMenuItem redoItem = new JMenuItem("Redo");
        redoItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_Y, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        redoItem.addActionListener(e -> getActiveTab().redo());
        menu.add(redoItem);

        menu.addSeparator();

        JMenuItem cutItem = new JMenuItem("Cut");
        cutItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_X, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        cutItem.addActionListener(e -> getActiveTab().cut());
        menu.add(cutItem);

        JMenuItem copyItem = new JMenuItem("Copy");
        copyItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_C, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        copyItem.addActionListener(e -> getActiveTab().copy());
        menu.add(copyItem);

        JMenuItem pasteItem = new JMenuItem("Paste");
        pasteItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_V, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        pasteItem.addActionListener(e -> getActiveTab().paste());
        menu.add(pasteItem);

        menu.addSeparator();

        JMenuItem selectAllItem = new JMenuItem("Select All");
        selectAllItem.setAccelerator(
                KeyStroke.getKeyStroke(KeyEvent.VK_A, Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        selectAllItem.addActionListener(e -> getActiveTab().selectAll());
        menu.add(selectAllItem);

        JMenuItem deleteItem = new JMenuItem("Delete");
        deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
        deleteItem.addActionListener(e -> getActiveTab().delete());
        menu.add(deleteItem);

        return menu;
    }

    private JMenu createSearchMenu() {
        JMenu menu = new JMenu("Find");
        menu.setMnemonic('F');

        int ctrl = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();

        JMenuItem findItem = new JMenuItem("Find...");
        findItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ctrl));
        findItem.addActionListener(e -> searchBarManager.toggleFindBar());
        menu.add(findItem);

        JMenuItem replaceItem = new JMenuItem("Replace...");
        replaceItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H, ctrl));
        replaceItem.addActionListener(e -> searchBarManager.toggleReplaceBar());
        menu.add(replaceItem);

        menu.addSeparator();

        JMenuItem findNextItem = new JMenuItem("Find Next");
        findNextItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, 0));
        findNextItem.addActionListener(e -> searchBarManager.findNext());
        menu.add(findNextItem);

        JMenuItem findPrevItem = new JMenuItem("Find Previous");
        findPrevItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F3, InputEvent.SHIFT_DOWN_MASK));
        findPrevItem.addActionListener(e -> searchBarManager.findPrevious());
        menu.add(findPrevItem);

        menu.addSeparator();

        JMenuItem gotoItem = new JMenuItem("Go to Line...");
        gotoItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L, ctrl));
        gotoItem.addActionListener(e -> goToLine());
        menu.add(gotoItem);

        menu.addSeparator();

        JMenuItem selFindItem = new JMenuItem("Use Selection for Find");
        selFindItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ctrl));
        selFindItem.addActionListener(e -> searchBarManager.useSelectionForFind());
        menu.add(selFindItem);

        JMenuItem selReplaceItem = new JMenuItem("Use Selection for Replace");
        selReplaceItem.addActionListener(e -> searchBarManager.useSelectionForReplace());
        menu.add(selReplaceItem);

        return menu;
    }

    private JMenu createViewMenu() {
        JMenu menu = new JMenu("View");
        menu.setMnemonic('V');

        JCheckBoxMenuItem sidebarItem = new JCheckBoxMenuItem("Show Sidebar");
        sidebarItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
                Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx()));
        sidebarItem.addActionListener(e -> toggleSidebar(sidebarItem.isSelected()));
        menu.add(sidebarItem);

        return menu;
    }

    // ============ Toolbar ============

    private JToolBar createToolBar() {
        JPanel toolBarPanel = new JPanel(new BorderLayout());

        JToolBar mainToolBar = new JToolBar();
        mainToolBar.setMargin(new Insets(1, 1, 1, 1));

        JButton undoBtn = createToolBarButton("Undo", "images/undo.svg", e -> getActiveTab().undo());
        JButton redoBtn = createToolBarButton("Redo", "images/redo.svg", e -> getActiveTab().redo());
        mainToolBar.add(undoBtn);
        mainToolBar.add(redoBtn);
        mainToolBar.addSeparator();

        JButton cutBtn = createToolBarButton("Cut", "images/menu-cut.svg", e -> getActiveTab().cut());
        JButton copyBtn = createToolBarButton("Copy", "images/copy.svg", e -> getActiveTab().copy());
        JButton pasteBtn = createToolBarButton("Paste", "images/menu-paste.svg", e -> getActiveTab().paste());
        mainToolBar.add(cutBtn);
        mainToolBar.add(copyBtn);
        mainToolBar.add(pasteBtn);
        mainToolBar.addSeparator();

        JButton searchBtn = createToolBarButton("Find", "images/search2.svg", e -> searchBarManager.toggleFindBar());
        mainToolBar.add(searchBtn);
        mainToolBar.addSeparator();

        JButton refreshBtn = createToolBarButton("Refresh File Tree", "images/refresh.svg",
                e -> workSpacePanel.refresh());
        mainToolBar.add(refreshBtn);

        toolBarPanel.add(mainToolBar, BorderLayout.WEST);

        // Right side toolbar
        JToolBar rightToolBar = new JToolBar();
        JButton newBtn = createToolBarButton("New File", "images/add.svg", e -> tabPanel.addNewTab());
        rightToolBar.add(newBtn);

        sidebarButton = new JToggleButton(new FlatSVGIcon("images/sidebar.svg"));
        sidebarButton.setToolTipText("Toggle Sidebar");
        sidebarButton.addActionListener(e -> toggleSidebar(sidebarButton.isSelected()));
        rightToolBar.add(sidebarButton);

        toolBarPanel.add(rightToolBar, BorderLayout.EAST);

        JToolBar result = new JToolBar();
        result.setLayout(new BorderLayout());
        result.add(toolBarPanel, BorderLayout.CENTER);
        result.setFloatable(false);

        return result;
    }

    private JButton createToolBarButton(String tooltip, String iconPath, ActionListener action) {
        JButton btn = new JButton(new FlatSVGIcon(iconPath));
        btn.setToolTipText(tooltip);
        btn.addActionListener(action);
        return btn;
    }

    // ============ Actions ============

    private EditorTab getActiveTab() {
        EditorTab tab = tabPanel.getActiveTab();
        if (tab == null) {
            tabPanel.addNewTab();
            tab = tabPanel.getActiveTab();
        }
        return tab;
    }

    private void openFile() {
        JFileChooser chooser = new JFileChooser();
        if (config.getLastOpenedFolder() != null && !config.getLastOpenedFolder().isEmpty()) {
            chooser.setCurrentDirectory(new File(config.getLastOpenedFolder()));
        }
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            config.setLastOpenedFolder(file.getParent());
            try {
                String content = FileUtils.readFile(file);
                tabPanel.openFile(file.getAbsolutePath(), content);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(this,
                        "Failed to open file: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    private void openFolder() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            config.setLastOpenedFolder(path);
            workSpacePanel.initJTree(path);

            if (!config.isSidebarVisible()) {
                toggleSidebar(true);
                sidebarButton.setSelected(true);
            }
            saveConfig();
        }
    }

    private void saveFile() {
        EditorTab tab = tabPanel.getActiveTab();
        if (tab != null)
            tab.save();
    }

    private void saveFileAs() {
        EditorTab tab = tabPanel.getActiveTab();
        if (tab != null)
            tab.saveAs();
    }

    private void goToLine() {
        EditorTab tab = tabPanel.getActiveTab();
        if (tab == null)
            return;

        GoToDialog dialog = new GoToDialog(this);
        dialog.setMaxLineNumberAllowed(tab.getTextArea().getLineCount());
        dialog.setVisible(true);
        int line = dialog.getLineNumber();
        if (line > 0) {
            try {
                tab.getTextArea().setCaretPosition(tab.getTextArea().getLineStartOffset(line - 1));
            } catch (BadLocationException ex) {
                UIManager.getLookAndFeel().provideErrorFeedback(tab.getTextArea());
            }
        }
    }

    private void toggleSidebar(boolean show) {
        if (show) {
            splitPane.setLeftComponent(workSpacePanel);
            splitPane.setRightComponent(tabPanel);
            splitPane.setDividerLocation(config.getSidebarWidth());
            // Replace center component
            Container contentPane = getContentPane();
            // Find and replace the tabPanel in center layout
            for (Component comp : contentPane.getComponents()) {
                if (comp == tabPanel) {
                    contentPane.remove(comp);
                    break;
                }
            }
            contentPane.add(splitPane, BorderLayout.CENTER);
        } else {
            splitPane.removeAll();
            Container contentPane = getContentPane();
            for (Component comp : contentPane.getComponents()) {
                if (comp == splitPane) {
                    contentPane.remove(comp);
                    break;
                }
            }
            contentPane.add(tabPanel, BorderLayout.CENTER);
        }
        config.setSidebarVisible(show);
        revalidate();
        repaint();
    }

    private void openSettings() {
        SettingsDialog dialog = new SettingsDialog(this, config, updatedConfig -> {
            // Apply window theme
            applyWindowTheme(updatedConfig.getWindowTheme());
            SwingUtilities.updateComponentTreeUI(this);

            // Apply editor settings to all tabs
            tabPanel.applySettingsToAll();
            tabPanel.applyThemeToAll();

            saveConfig();
        });
        dialog.setVisible(true);
    }

    private void applyWindowTheme(String theme) {
        try {
            switch (theme) {
                case "Dark":
                    UIManager.setLookAndFeel(new FlatDarkLaf());
                    break;
                case "IntelliJ":
                    UIManager.setLookAndFeel(new FlatIntelliJLaf());
                    break;
                case "Light":
                    UIManager.setLookAndFeel(new FlatLightLaf());
                    break;
                default:
                    UIManager.setLookAndFeel(new FlatDarculaLaf());
                    break;
            }
        } catch (Exception ex) {
            System.err.println("Failed to set theme: " + ex.getMessage());
        }
    }

    private void onWindowClosing() {
        // Save window size
        config.setWindowWidth(getWidth());
        config.setWindowHeight(getHeight());
        saveConfig();
        dispose();
        System.exit(0);
    }

    private void saveConfig() {
        configLoader.saveConfig(config);
    }
}
