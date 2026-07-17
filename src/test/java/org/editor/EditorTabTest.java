package org.editor;

import org.editor.config.AppConfig;
import org.editor.editor.EditorTab;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

public class EditorTabTest {
    @Test
    void testCreateEditorTab() {
        AppConfig config = new AppConfig();
        EditorTab tab = new EditorTab("test", "test.txt", "java", config);
        JFrame frame = new JFrame();
        frame.add(tab);
        frame.setSize(800, 600);
        frame.setVisible(true);

        // Basic operations
        tab.setText(
                "public class Test {\n    public static void main(String[] args) {\n        System.out.println(\"Hello\");\n    }\n}");
        tab.selectAll();
        tab.copy();
        tab.cut();
        tab.paste();
        tab.undo();
        tab.redo();

        assertNotNull(tab.getText());
        assertEquals("test", tab.getTabName());
        assertEquals("java", tab.getFileExtension());

        frame.dispose();
    }
}
