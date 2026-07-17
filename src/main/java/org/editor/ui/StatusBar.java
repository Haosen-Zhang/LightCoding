package org.editor.ui;

import javax.swing.*;
import java.awt.*;

/**
 * Bottom status bar showing cursor position and syntax selector.
 */
public class StatusBar extends JPanel {

    private final JLabel positionLabel;

    /** Syntax selector combo box */
    public final JComboBox<String> changeSyntaxComboBox;

    private static final String[] SYNTAX_OPTIONS = {
            "ActionScript", "Assembler X86", "Assembler 6502", "C", "C#", "C++",
            "Clojure", "CSS", "CSV", "Dart", "Dockerfile", "Go", "Groovy",
            "HTML", "Java", "JavaScript", "JSON", "Kotlin", "LaTeX", "Less",
            "Lua", "Makefile", "Markdown", "MXML", "Objective-C", "Perl", "PHP",
            "Plain Text", "Properties", "INI", "Python", "Ruby", "Scala",
            "Shell Script", "SQL", "TypeScript", "Windows Batch", "XML", "XSL", "YAML"
    };

    public StatusBar() {
        setLayout(new BorderLayout());

        positionLabel = new JLabel("Line: 1, Column: 1");
        positionLabel.setBorder(BorderFactory.createEmptyBorder(2, 8, 2, 0));

        changeSyntaxComboBox = new JComboBox<>(SYNTAX_OPTIONS);
        changeSyntaxComboBox.setMaximumSize(new Dimension(160, 24));
        changeSyntaxComboBox.setPreferredSize(new Dimension(160, 24));

        JLabel syntaxLabel = new JLabel("Syntax: ");
        syntaxLabel.setBorder(BorderFactory.createEmptyBorder(2, 16, 2, 4));

        JPanel rightPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 0));
        rightPanel.add(syntaxLabel);
        rightPanel.add(changeSyntaxComboBox);

        add(positionLabel, BorderLayout.WEST);
        add(rightPanel, BorderLayout.EAST);

        setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, UIManager.getColor("Separator.foreground")));
    }

    public void setLineColumn(int line, int column) {
        positionLabel.setText("Line: " + line + ", Column: " + column);
    }
}
