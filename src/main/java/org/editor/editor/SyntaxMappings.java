package org.editor.editor;

import org.fife.ui.rsyntaxtextarea.SyntaxConstants;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Maps file extensions to syntax highlighting styles.
 * Extracted from Tab.java to keep syntax definitions centralized.
 */
public final class SyntaxMappings {

    private SyntaxMappings() {} // utility class

    /** File extension -> RSyntaxTextArea syntax style constant */
    public static final Map<String, String> EXTENSION_TO_SYNTAX;

    /** File extension -> human-readable language name */
    public static final Map<String, String> EXTENSION_TO_DISPLAY;

    /** Human-readable language name -> RSyntaxTextArea syntax style constant (for combo box) */
    public static final Map<String, String> DISPLAY_TO_SYNTAX;

    static {
        Map<String, String> ext2syn = new HashMap<>();
        ext2syn.put("txt", SyntaxConstants.SYNTAX_STYLE_NONE);
        ext2syn.put("tex", SyntaxConstants.SYNTAX_STYLE_LATEX);
        ext2syn.put("html", SyntaxConstants.SYNTAX_STYLE_HTML);
        ext2syn.put("htm", SyntaxConstants.SYNTAX_STYLE_HTML);
        ext2syn.put("css", SyntaxConstants.SYNTAX_STYLE_CSS);
        ext2syn.put("csv", SyntaxConstants.SYNTAX_STYLE_CSV);
        ext2syn.put("xml", SyntaxConstants.SYNTAX_STYLE_XML);
        ext2syn.put("xsl", SyntaxConstants.SYNTAX_STYLE_XML);
        ext2syn.put("yml", SyntaxConstants.SYNTAX_STYLE_YAML);
        ext2syn.put("yaml", SyntaxConstants.SYNTAX_STYLE_YAML);
        ext2syn.put("md", SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        ext2syn.put("js", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        ext2syn.put("ts", SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT);
        ext2syn.put("jsx", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        ext2syn.put("tsx", SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT);
        ext2syn.put("c", SyntaxConstants.SYNTAX_STYLE_C);
        ext2syn.put("cpp", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        ext2syn.put("cc", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        ext2syn.put("cxx", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        ext2syn.put("h", SyntaxConstants.SYNTAX_STYLE_C);
        ext2syn.put("hpp", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        ext2syn.put("cs", SyntaxConstants.SYNTAX_STYLE_CSHARP);
        ext2syn.put("java", SyntaxConstants.SYNTAX_STYLE_JAVA);
        ext2syn.put("py", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        ext2syn.put("json", SyntaxConstants.SYNTAX_STYLE_JSON);
        ext2syn.put("php", SyntaxConstants.SYNTAX_STYLE_PHP);
        ext2syn.put("rb", SyntaxConstants.SYNTAX_STYLE_RUBY);
        ext2syn.put("bat", SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);
        ext2syn.put("sql", SyntaxConstants.SYNTAX_STYLE_SQL);
        ext2syn.put("go", SyntaxConstants.SYNTAX_STYLE_GO);
        ext2syn.put("groovy", SyntaxConstants.SYNTAX_STYLE_GROOVY);
        ext2syn.put("lua", SyntaxConstants.SYNTAX_STYLE_LUA);
        ext2syn.put("m", SyntaxConstants.SYNTAX_STYLE_C);
        ext2syn.put("mm", SyntaxConstants.SYNTAX_STYLE_C);
        ext2syn.put("pl", SyntaxConstants.SYNTAX_STYLE_PERL);
        ext2syn.put("sh", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        ext2syn.put("scala", SyntaxConstants.SYNTAX_STYLE_SCALA);
        ext2syn.put("kt", SyntaxConstants.SYNTAX_STYLE_KOTLIN);
        ext2syn.put("swift", SyntaxConstants.SYNTAX_STYLE_NONE);
        ext2syn.put("rs", SyntaxConstants.SYNTAX_STYLE_NONE);
        ext2syn.put("dart", SyntaxConstants.SYNTAX_STYLE_DART);
        ext2syn.put("properties", SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE);
        ext2syn.put("ini", SyntaxConstants.SYNTAX_STYLE_INI);
        ext2syn.put("dockerfile", SyntaxConstants.SYNTAX_STYLE_DOCKERFILE);
        ext2syn.put("makefile", SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
        ext2syn.put("less", SyntaxConstants.SYNTAX_STYLE_LESS);
        EXTENSION_TO_SYNTAX = Collections.unmodifiableMap(ext2syn);

        Map<String, String> ext2disp = new HashMap<>();
        ext2disp.put("as", "ActionScript");
        ext2disp.put("asm", "Assembler X86");
        ext2disp.put("bat", "Windows Batch");
        ext2disp.put("c", "C");
        ext2disp.put("h", "C");
        ext2disp.put("cs", "C#");
        ext2disp.put("cpp", "C++");
        ext2disp.put("cc", "C++");
        ext2disp.put("cxx", "C++");
        ext2disp.put("hpp", "C++");
        ext2disp.put("clj", "Clojure");
        ext2disp.put("css", "CSS");
        ext2disp.put("csv", "CSV");
        ext2disp.put("dart", "Dart");
        ext2disp.put("dockerfile", "Dockerfile");
        ext2disp.put("go", "Go");
        ext2disp.put("groovy", "Groovy");
        ext2disp.put("html", "HTML");
        ext2disp.put("htm", "HTML");
        ext2disp.put("java", "Java");
        ext2disp.put("js", "JavaScript");
        ext2disp.put("jsx", "JavaScript");
        ext2disp.put("json", "JSON");
        ext2disp.put("kt", "Kotlin");
        ext2disp.put("less", "Less");
        ext2disp.put("lua", "Lua");
        ext2disp.put("tex", "LaTeX");
        ext2disp.put("md", "Markdown");
        ext2disp.put("makefile", "Makefile");
        ext2disp.put("mm", "Objective-C");
        ext2disp.put("m", "Objective-C");
        ext2disp.put("mxml", "MXML");
        ext2disp.put("pl", "Perl");
        ext2disp.put("php", "PHP");
        ext2disp.put("properties", "Properties");
        ext2disp.put("ini", "INI");
        ext2disp.put("py", "Python");
        ext2disp.put("txt", "Plain Text");
        ext2disp.put("rb", "Ruby");
        ext2disp.put("rs", "Rust");
        ext2disp.put("scala", "Scala");
        ext2disp.put("sh", "Shell Script");
        ext2disp.put("sql", "SQL");
        ext2disp.put("swift", "Swift");
        ext2disp.put("ts", "TypeScript");
        ext2disp.put("tsx", "TypeScript");
        ext2disp.put("xml", "XML");
        ext2disp.put("xsl", "XSL");
        ext2disp.put("yml", "YAML");
        ext2disp.put("yaml", "YAML");
        EXTENSION_TO_DISPLAY = Collections.unmodifiableMap(ext2disp);

        Map<String, String> disp2syn = new HashMap<>();
        disp2syn.put("ActionScript", SyntaxConstants.SYNTAX_STYLE_ACTIONSCRIPT);
        disp2syn.put("Assembler X86", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_X86);
        disp2syn.put("Assembler 6502", SyntaxConstants.SYNTAX_STYLE_ASSEMBLER_6502);
        disp2syn.put("C", SyntaxConstants.SYNTAX_STYLE_C);
        disp2syn.put("C#", SyntaxConstants.SYNTAX_STYLE_CSHARP);
        disp2syn.put("C++", SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        disp2syn.put("Clojure", SyntaxConstants.SYNTAX_STYLE_CLOJURE);
        disp2syn.put("CSS", SyntaxConstants.SYNTAX_STYLE_CSS);
        disp2syn.put("CSV", SyntaxConstants.SYNTAX_STYLE_CSV);
        disp2syn.put("Dart", SyntaxConstants.SYNTAX_STYLE_DART);
        disp2syn.put("Dockerfile", SyntaxConstants.SYNTAX_STYLE_DOCKERFILE);
        disp2syn.put("Go", SyntaxConstants.SYNTAX_STYLE_GO);
        disp2syn.put("Groovy", SyntaxConstants.SYNTAX_STYLE_GROOVY);
        disp2syn.put("HTML", SyntaxConstants.SYNTAX_STYLE_HTML);
        disp2syn.put("Java", SyntaxConstants.SYNTAX_STYLE_JAVA);
        disp2syn.put("JavaScript", SyntaxConstants.SYNTAX_STYLE_JAVASCRIPT);
        disp2syn.put("JSON", SyntaxConstants.SYNTAX_STYLE_JSON);
        disp2syn.put("Kotlin", SyntaxConstants.SYNTAX_STYLE_KOTLIN);
        disp2syn.put("Less", SyntaxConstants.SYNTAX_STYLE_LESS);
        disp2syn.put("Lua", SyntaxConstants.SYNTAX_STYLE_LUA);
        disp2syn.put("LaTeX", SyntaxConstants.SYNTAX_STYLE_LATEX);
        disp2syn.put("Markdown", SyntaxConstants.SYNTAX_STYLE_MARKDOWN);
        disp2syn.put("Makefile", SyntaxConstants.SYNTAX_STYLE_MAKEFILE);
        disp2syn.put("MXML", SyntaxConstants.SYNTAX_STYLE_MXML);
        disp2syn.put("Perl", SyntaxConstants.SYNTAX_STYLE_PERL);
        disp2syn.put("PHP", SyntaxConstants.SYNTAX_STYLE_PHP);
        disp2syn.put("Properties", SyntaxConstants.SYNTAX_STYLE_PROPERTIES_FILE);
        disp2syn.put("INI", SyntaxConstants.SYNTAX_STYLE_INI);
        disp2syn.put("Python", SyntaxConstants.SYNTAX_STYLE_PYTHON);
        disp2syn.put("Plain Text", SyntaxConstants.SYNTAX_STYLE_NONE);
        disp2syn.put("Ruby", SyntaxConstants.SYNTAX_STYLE_RUBY);
        disp2syn.put("Scala", SyntaxConstants.SYNTAX_STYLE_SCALA);
        disp2syn.put("Shell Script", SyntaxConstants.SYNTAX_STYLE_UNIX_SHELL);
        disp2syn.put("SQL", SyntaxConstants.SYNTAX_STYLE_SQL);
        disp2syn.put("TypeScript", SyntaxConstants.SYNTAX_STYLE_TYPESCRIPT);
        disp2syn.put("Windows Batch", SyntaxConstants.SYNTAX_STYLE_WINDOWS_BATCH);
        disp2syn.put("XML", SyntaxConstants.SYNTAX_STYLE_XML);
        disp2syn.put("XSL", SyntaxConstants.SYNTAX_STYLE_XML);
        disp2syn.put("YAML", SyntaxConstants.SYNTAX_STYLE_YAML);
        DISPLAY_TO_SYNTAX = Collections.unmodifiableMap(disp2syn);
    }

    /**
     * Get syntax style for a file extension.
     * @return SYNTAX_STYLE_NONE if unknown
     */
    public static String getSyntaxForExtension(String ext) {
        if (ext == null) return SyntaxConstants.SYNTAX_STYLE_NONE;
        return EXTENSION_TO_SYNTAX.getOrDefault(ext.toLowerCase(), SyntaxConstants.SYNTAX_STYLE_NONE);
    }

    /**
     * Get display name for a file extension.
     * @return "Plain Text" if unknown
     */
    public static String getDisplayForExtension(String ext) {
        if (ext == null) return "Plain Text";
        return EXTENSION_TO_DISPLAY.getOrDefault(ext.toLowerCase(), "Plain Text");
    }
}
