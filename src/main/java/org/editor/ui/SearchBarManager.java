package org.editor.ui;

import org.fife.rsta.ui.CollapsibleSectionPanel;
import org.fife.rsta.ui.search.FindToolBar;
import org.fife.rsta.ui.search.ReplaceToolBar;
import org.fife.rsta.ui.search.SearchEvent;
import org.fife.rsta.ui.search.SearchListener;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;

import javax.swing.*;
import java.awt.*;

/**
 * Manages the Find and Replace search bars.
 */
public class SearchBarManager implements SearchListener {

    private final CollapsibleSectionPanel collapsiblePanel;
    private final FindToolBar findToolBar;
    private final ReplaceToolBar replaceToolBar;
    private final SearchContext searchContext;
    private final JLabel statusLabel;

    private org.fife.ui.rsyntaxtextarea.RSyntaxTextArea currentTextArea;

    public SearchBarManager(JLabel statusLabel) {
        this.statusLabel = statusLabel;
        this.collapsiblePanel = new CollapsibleSectionPanel();
        this.searchContext = new SearchContext();

        // Keep the search context shared between find and replace
        findToolBar = new FindToolBar(this);
        findToolBar.setSearchContext(searchContext);

        replaceToolBar = new ReplaceToolBar(this);
        replaceToolBar.setSearchContext(searchContext);
    }

    public CollapsibleSectionPanel getPanel() {
        return collapsiblePanel;
    }

    public void setTextArea(org.fife.ui.rsyntaxtextarea.RSyntaxTextArea textArea) {
        this.currentTextArea = textArea;
    }

    /**
     * Toggle the Find bar visibility.
     */
    public void toggleFindBar() {
        collapsiblePanel.showBottomComponent(findToolBar);
    }

    /**
     * Toggle the Replace bar visibility.
     */
    public void toggleReplaceBar() {
        collapsiblePanel.showBottomComponent(replaceToolBar);
    }

    /**
     * Find next occurrence.
     */
    public void findNext() {
        if (currentTextArea == null)
            return;
        SearchResult result = SearchEngine.find(currentTextArea, searchContext);
        if (!result.wasFound() || result.isWrapped()) {
            UIManager.getLookAndFeel().provideErrorFeedback(currentTextArea);
        }
    }

    /**
     * Find previous occurrence.
     */
    public void findPrevious() {
        if (currentTextArea == null)
            return;
        // Toggle search forward to search backward
        boolean forward = searchContext.getSearchForward();
        searchContext.setSearchForward(false);
        SearchResult result = SearchEngine.find(currentTextArea, searchContext);
        searchContext.setSearchForward(forward);
        if (!result.wasFound() || result.isWrapped()) {
            UIManager.getLookAndFeel().provideErrorFeedback(currentTextArea);
        }
    }

    /**
     * Use current selection as find text.
     */
    public void useSelectionForFind() {
        if (currentTextArea == null)
            return;
        String selected = currentTextArea.getSelectedText();
        if (selected != null && !selected.isEmpty()) {
            searchContext.setSearchFor(selected);
        }
    }

    /**
     * Use current selection as replace text (find field).
     */
    public void useSelectionForReplace() {
        if (currentTextArea == null)
            return;
        String selected = currentTextArea.getSelectedText();
        if (selected != null && !selected.isEmpty()) {
            searchContext.setSearchFor(selected);
        }
    }

    @Override
    public void searchEvent(SearchEvent e) {
        if (currentTextArea == null)
            return;

        SearchEvent.Type type = e.getType();
        SearchContext context = e.getSearchContext();
        SearchResult result;

        switch (type) {
            case MARK_ALL:
                result = SearchEngine.markAll(currentTextArea, context);
                break;
            case FIND:
                result = SearchEngine.find(currentTextArea, context);
                if (!result.wasFound() || result.isWrapped()) {
                    UIManager.getLookAndFeel().provideErrorFeedback(currentTextArea);
                }
                break;
            case REPLACE:
                result = SearchEngine.replace(currentTextArea, context);
                if (!result.wasFound() || result.isWrapped()) {
                    UIManager.getLookAndFeel().provideErrorFeedback(currentTextArea);
                }
                break;
            case REPLACE_ALL:
                result = SearchEngine.replaceAll(currentTextArea, context);
                JOptionPane.showMessageDialog(null, result.getCount() + " occurrences replaced.");
                break;
            default:
                return;
        }

        String text;
        if (result.wasFound()) {
            text = "Text found; occurrences marked: " + result.getMarkedCount();
        } else if (type == SearchEvent.Type.MARK_ALL) {
            if (result.getMarkedCount() > 0) {
                text = "Occurrences marked: " + result.getMarkedCount();
            } else {
                text = "";
            }
        } else {
            text = "Text not found";
        }
        if (statusLabel != null) {
            statusLabel.setText(text);
        }
    }

    @Override
    public String getSelectedText() {
        return currentTextArea != null ? currentTextArea.getSelectedText() : null;
    }
}
