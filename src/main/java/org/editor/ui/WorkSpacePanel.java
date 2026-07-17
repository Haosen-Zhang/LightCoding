package org.editor.ui;

import javax.swing.*;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.function.Consumer;

/**
 * Sidebar panel displaying a project folder tree.
 * Double-clicking a file opens it in the editor.
 */
public class WorkSpacePanel extends JPanel {

    private JTree tree;
    private DefaultTreeModel treeModel;
    private String projectFolderPath;
    private Consumer<String> fileOpenHandler;

    public WorkSpacePanel() {
        setLayout(new BorderLayout());
        setPreferredSize(new Dimension(250, 600));
    }

    /**
     * Set the handler to be called when a file is double-clicked.
     * 
     * @param handler receives the absolute file path
     */
    public void setFileOpenHandler(Consumer<String> handler) {
        this.fileOpenHandler = handler;
    }

    /**
     * Initialize the file tree from a folder path.
     */
    public void initJTree(String folderPath) {
        this.projectFolderPath = folderPath;
        removeAll();

        if (folderPath == null || folderPath.isEmpty())
            return;

        File rootFile = new File(folderPath);
        if (!rootFile.exists() || !rootFile.isDirectory())
            return;

        DefaultMutableTreeNode rootNode = traverseFolder(rootFile);
        if (rootNode == null)
            return;

        treeModel = new DefaultTreeModel(rootNode);
        tree = new JTree(treeModel);
        tree.setShowsRootHandles(true);
        tree.setRootVisible(true);

        // Custom renderer with file/folder icons
        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setOpenIcon(new ImageIcon("images/folder-open.svg"));
        renderer.setClosedIcon(new ImageIcon("images/folder.svg"));
        renderer.setLeafIcon(new ImageIcon("images/file.svg"));
        tree.setCellRenderer(renderer);

        // Double-click to open file
        tree.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    TreePath path = tree.getPathForLocation(e.getX(), e.getY());
                    if (path != null) {
                        DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();
                        File file = getFileFromNode(node);
                        if (file != null && file.isFile() && fileOpenHandler != null) {
                            fileOpenHandler.accept(file.getAbsolutePath());
                        }
                    }
                }
            }
        });

        JScrollPane sp = new JScrollPane(tree);
        add(sp, BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    /**
     * Refresh the current tree.
     */
    public void refresh() {
        if (projectFolderPath != null) {
            initJTree(projectFolderPath);
        }
    }

    public String getProjectFolderPath() {
        return projectFolderPath;
    }

    /**
     * Recursively traverse a folder and build a tree node.
     */
    private DefaultMutableTreeNode traverseFolder(File folder) {
        DefaultMutableTreeNode node = new DefaultMutableTreeNode(folder);
        File[] files = folder.listFiles();
        if (files == null)
            return node;

        // Sort: directories first, then files, both alphabetically
        java.util.Arrays.sort(files, (a, b) -> {
            if (a.isDirectory() && !b.isDirectory())
                return -1;
            if (!a.isDirectory() && b.isDirectory())
                return 1;
            return a.getName().compareToIgnoreCase(b.getName());
        });

        for (File file : files) {
            if (file.isDirectory()) {
                DefaultMutableTreeNode child = traverseFolder(file);
                if (child != null) {
                    node.add(child);
                }
            } else {
                node.add(new DefaultMutableTreeNode(file, false));
            }
        }
        return node;
    }

    /**
     * Extract a File object from a tree node.
     */
    private File getFileFromNode(DefaultMutableTreeNode node) {
        Object userObj = node.getUserObject();
        if (userObj instanceof File) {
            return (File) userObj;
        }
        // Try to reconstruct path from parent nodes
        StringBuilder path = new StringBuilder();
        TreeNode[] pathNodes = node.getPath();
        for (TreeNode n : pathNodes) {
            Object obj = ((DefaultMutableTreeNode) n).getUserObject();
            if (obj instanceof File) {
                if (path.length() > 0)
                    path.append(File.separator);
                path.append(((File) obj).getName());
            }
        }
        return path.length() > 0 ? new File(path.toString()) : null;
    }
}
