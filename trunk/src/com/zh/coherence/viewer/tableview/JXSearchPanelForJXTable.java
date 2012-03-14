package com.zh.coherence.viewer.tableview;

import org.jdesktop.swingx.AbstractPatternPanel;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXComboBox;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.renderer.DefaultListRenderer;
import org.jdesktop.swingx.renderer.LocalizableStringValue;
import org.jdesktop.swingx.renderer.StringValue;
import org.jdesktop.swingx.search.PatternMatcher;
import org.jdesktop.swingx.search.PatternModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by IntelliJ IDEA.
 * User: Живко
 * Date: 14.03.12
 * Time: 1:11
 */
public class JXSearchPanelForJXTable extends AbstractPatternPanel {
    /**
     * The action command key.
     */
    public static final String MATCH_RULE_ACTION_COMMAND = "selectMatchRule";

    private JXComboBox searchCriteria;
    private JXButton columns;

    private List<PatternMatcher> patternMatchers;
    private JXTable table;
    private Map<String, CheckStore> columnsStoreMap = new HashMap<String, CheckStore>();

    /**
     * Creates a search panel.
     */
    public JXSearchPanelForJXTable(JXTable table) {
        this.table = table;
        initComponents();

        build();
        initActions();
        bind();
        getPatternModel().setIncremental(true);
        addPatternMatcher(new TableMatcher());
    }

    /**
     * Adds a pattern matcher.
     *
     * @param matcher the matcher to add.
     */
    public void addPatternMatcher(PatternMatcher matcher) {
        getPatternMatchers().add(matcher);
        updateFieldName(matcher);
    }

    /**
     * set the label of the search combo.
     *
     * @param name the label
     */
    public void setFieldName(String name) {
        String old = searchLabel.getText();
        searchLabel.setText(name);
        firePropertyChange("fieldName", old, searchLabel.getText());
    }

    /**
     * returns the label of the search combo.
     */
    public String getFieldName() {
        return searchLabel.getText();
    }

    /**
     * returns the current compiled Pattern.
     *
     * @return the current compiled <code>Pattern</code>
     */
    public Pattern getPattern() {
        return patternModel.getPattern();
    }

    /**
     * @param matcher
     */
    protected void updateFieldName(PatternMatcher matcher) {
        if (searchLabel.getText().length() == 0) { // ugly hack
            searchLabel.setText("Field");
        }
    }

    /**
     * Updates the pattern matchers.
     */
    @Override
    public void match() {
        for (PatternMatcher patternMatcher : getPatternMatchers()) {
            patternMatcher.setPattern(getPattern());
        }
    }

    /**
     * set's the PatternModel's MatchRule to the selected in combo.
     * <p/>
     * NOTE: this
     * is public as an implementation side-effect!
     * No need to ever call directly.
     */
    public void updateMatchRule() {
        getPatternModel().setMatchRule(
                (String) searchCriteria.getSelectedItem());
    }

    private List<PatternMatcher> getPatternMatchers() {
        if (patternMatchers == null) {
            patternMatchers = new ArrayList<PatternMatcher>();
        }
        return patternMatchers;
    }

    @Override
    protected void initExecutables() {
        super.initExecutables();
        getActionMap().put(MATCH_RULE_ACTION_COMMAND,
                createBoundAction(MATCH_RULE_ACTION_COMMAND, "updateMatchRule"));
    }

    /**
     * bind the components to the patternModel/actions.
     */
    @Override
    protected void bind() {
        super.bind();
        List<?> matchRules = getPatternModel().getMatchRules();
        // PENDING: map rules to localized strings
        ComboBoxModel model = new DefaultComboBoxModel(matchRules.toArray());
        model.setSelectedItem(getPatternModel().getMatchRule());
        searchCriteria.setModel(model);
        searchCriteria.setAction(getAction(MATCH_RULE_ACTION_COMMAND));
        searchCriteria.setRenderer(new DefaultListRenderer(createStringValue(getLocale())));

    }

    private StringValue createStringValue(Locale locale) {
        Map<Object, String> keys = new HashMap<Object, String>();
        keys.put(PatternModel.MATCH_RULE_CONTAINS,
                PatternModel.MATCH_RULE_CONTAINS);
        keys.put(PatternModel.MATCH_RULE_ENDSWITH,
                PatternModel.MATCH_RULE_ENDSWITH);
        keys.put(PatternModel.MATCH_RULE_EQUALS,
                PatternModel.MATCH_RULE_EQUALS);
        keys.put(PatternModel.MATCH_RULE_STARTSWITH,
                PatternModel.MATCH_RULE_STARTSWITH);
        return new LocalizableStringValue(keys, PatternModel.SEARCH_PREFIX, locale);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void updateLocaleState(Locale locale) {
        super.updateLocaleState(locale);
        searchCriteria.setRenderer(new DefaultListRenderer(createStringValue(locale)));
    }

    /**
     * build container by adding all components.
     * PRE: all components created.
     */
    private void build() {
        add(searchLabel);
        add(searchCriteria);
        add(columns);
        add(searchField);
        add(matchCheck);
    }

    /**
     * create contained components.
     */
    @Override
    protected void initComponents() {
        setLayout(new FlowLayout(FlowLayout.LEFT));
        super.initComponents();
        searchCriteria = new JXComboBox();

        columns = new JXButton(new ColumnButtonListener());
        columns.setEnabled(false);
    }

    class CheckStore {
        String id;
        Boolean state;
        int index;

        public CheckStore(String id, Boolean state, int index) {
            this.id = id;
            this.state = state;
            this.index = index;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            CheckStore that = (CheckStore) o;

            return !(id != null ? !id.equals(that.id) : that.id != null);
        }

        @Override
        public int hashCode() {
            return id != null ? id.hashCode() : 0;
        }

        @Override
        public String toString() {
            return id;
        }
    }

    private class ColumnButtonListener extends AbstractAction {
        private ColumnButtonListener() {
            putValue(Action.NAME, "columns");
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            showColumnsDialog();
        }
    }

    public void refreshFieldsList() {
        JTableHeader header = table.getTableHeader();
        columnsStoreMap.clear();
        CheckStore store;
        for (int i = 0, size = header.getColumnModel().getColumnCount(); i < size; i++) {
            store = new CheckStore(header.getColumnModel().getColumn(i).getHeaderValue().toString(), false, i);
            columnsStoreMap.put(store.id, store);
        }
        columns.setEnabled(columnsStoreMap.size() > 0);
    }

    private void showColumnsDialog() {
        JFrame parentFrame = (JFrame) SwingUtilities.windowForComponent(table);
        final JDialog dialog = new JDialog(parentFrame, true);
        Container cont = dialog.getContentPane();
        cont.setLayout(new BorderLayout());

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEADING));

        panel.setBorder(BorderFactory.createEtchedBorder());
        cont.add(panel, BorderLayout.CENTER);

        for (CheckStore checkStore : columnsStoreMap.values()) {
            final JCheckBox box = new JCheckBox(checkStore.id, checkStore.state);
            panel.add(box);
            box.addChangeListener(new ChangeListener() {
                @Override
                public void stateChanged(ChangeEvent e) {
                    columnsStoreMap.get(box.getText()).state = box.isSelected();
                }
            });
        }

        JPanel buttons = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttons.setBorder(BorderFactory.createEtchedBorder());
        cont.add(buttons, BorderLayout.SOUTH);
        JButton ok = new JButton("OK");
        ok.setIcon(new ImageIcon("icons/ok.png"));
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dialog.dispose();
            }
        });

        buttons.add(ok);

        dialog.pack();
        dialog.setLocationRelativeTo(parentFrame);
        dialog.setVisible(true);
    }

    private class TableMatcher implements PatternMatcher {
        private Pattern pattern;

        @Override
        public Pattern getPattern() {
            return pattern;
        }

        @Override
        public void setPattern(Pattern pattern) {
            this.pattern = pattern;
            if (pattern != null) {
                List<Integer> indexList = new ArrayList<Integer>();
                for (CheckStore store : columnsStoreMap.values()) {
                    if (store.state) {
                        indexList.add(store.index);
                    }
                }
                int[] result = new int[indexList.size()];
                for (Integer i : indexList) {
                    result[result.length - 1] = i;
                }
                RowFilter rowFilter = RowFilter.regexFilter(getPattern().pattern(), result);
                table.setRowFilter(rowFilter);
            } else {
                table.setRowFilter(null);
            }
        }
    }
}
