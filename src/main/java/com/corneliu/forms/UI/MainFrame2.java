package com.corneliu.forms.UI;

import com.corneliu.forms.service.TextProcessor;
import com.corneliu.forms.service.impl.TextProcessorImpl;
import com.hexidec.ekit.EkitCore;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TreeMap;

public class MainFrame2 extends javax.swing.JFrame {

    private TextProcessor textProcessor;
    private EkitCore editor1;
    private JScrollPane dictionaryScrollPane;
    private Map<String, String> currendDictionary;
//    private HTMLEditorPane editor2;


    public MainFrame2() throws FileNotFoundException {
        textProcessor = new TextProcessorImpl();
        initComponents();

        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(true);
        ToolTipManager.sharedInstance().setDismissDelay(30000);
        initEditor1();
//        initEditor2();

        currendDictionary = textProcessor.getDictionary();
        repaintDictionary(false, null);

        jTextField1.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                repaintDictionary(sortAlpabetically.isSelected(), jTextField1.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                repaintDictionary(sortAlpabetically.isSelected(), jTextField1.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                repaintDictionary(sortAlpabetically.isSelected(), jTextField1.getText());
            }
        });
    }

//    private void initEditor2() {
//        editor2 = new HTMLEditorPane();
//        textPannel.add(editor2);
//
//        JMenuBar menuBar = new JMenuBar();
//        menuBar.add(editor2.getEditMenu());
//        menuBar.add(editor2.getFormatMenu());
//        menuBar.add(editor2.getInsertMenu());
//        this.setJMenuBar(menuBar);
//    }

    private void initEditor1() {
        editor1 = new EkitCore(false, null, null, null, null, null, true, false, true, true, "en", "US", false, true, false, true,
                "PR|FN|CT|CP|PS|PX|UN|RE|*|BL|IT|UD|SK|SU|SB|UL|OL|AL|AC|AR|AJ|SP|UC|UM|LK|TI|TE|CE|RI|CI|RD|CD|SR|FO", true, true);
        editor1.setPreferredSize(new Dimension(800, 600));
        editor1.setSize(new Dimension(800, 600));

        JMenuBar menuBar = editor1.getMenuBar();
        menuBar.remove(10);  // remove debug menu
        menuBar.remove(9);  // remove help menu
        menuBar.remove(2);  // remove view menu
//        menuBar.remove(0);  // remove file menu
        this.setJMenuBar(menuBar);

        editor1.getToolBarMain(true).setAlignmentX(Component.LEFT_ALIGNMENT);
        editor1.getToolBarFormat(true).setAlignmentX(Component.LEFT_ALIGNMENT);
        editor1.getToolBarStyles(true).setAlignmentX(Component.LEFT_ALIGNMENT);
        textPannel.add(editor1.getToolBarMain(true));
        textPannel.add(editor1.getToolBarFormat(true));
        textPannel.add(editor1.getToolBarStyles(true));

        textPannel.add(editor1);
        editor1.getTextPane().grabFocus();
    }

    public boolean updateDictionaryKey(String originalKey, String newKey, String newValue) {
        if (currendDictionary.containsKey(newKey.toUpperCase())) {
            JOptionPane.showMessageDialog(null, "Duplicate key");
            return false;
        } else {
            currendDictionary.remove(originalKey);
             if (StringUtils.isNotBlank(newKey)) {
                 currendDictionary.put(newKey.toUpperCase(), newValue);
             }
        }

        return true;
    }

    public void updateDictionaryEntry(String key, String value) {
        if (StringUtils.isNotBlank(key)) {
            currendDictionary.put(key, value);
        }
    }

    private void processButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processButtonActionPerformed
        String processedText = textProcessor.process(editor1.getDocumentText(), currendDictionary);
        editor1.setDocumentText(processedText);

//        String processedText = textProcessor.process(editor.getText(), actualDictionary);
//        editor.setText(processedText);
    }//GEN-LAST:event_processButtonActionPerformed

    private void addEntryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEntryButtonActionPerformed
        DictionaryEntryPanel2 panel = new DictionaryEntryPanel2(this, "", "");
        dictionaryPannel.add(panel);
        panel.getKeyText().grabFocus();
        this.pack();
    }//GEN-LAST:event_addEntryButtonActionPerformed

    private void saveDictionaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDictionaryButtonActionPerformed
        try {
            textProcessor.saveDictionary(currendDictionary);
            reloadDictionary();
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(this, "Nu a reusit salvarea dictionarului." + e.getMessage());
        }
    }//GEN-LAST:event_saveDictionaryButtonActionPerformed

    private void reloadDictionaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadDictionaryButtonActionPerformed
        try {
            reloadDictionary();
        } catch (FileNotFoundException e) {
            JOptionPane.showMessageDialog(this, "Failed to reload the dictionary." + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_reloadDictionaryButtonActionPerformed

    private void reloadDictionary() throws FileNotFoundException {
        currendDictionary = textProcessor.getDictionary();
        jTextField1.setText("");
        repaintDictionary(sortAlpabetically.isSelected(), null);
    }

    private Map<String, String> computeActualDictionary() {
        Map<String, String> actualDictionary = new LinkedHashMap<>();

        for (Component component : dictionaryPannel.getComponents()) {
            if (component instanceof DictionaryEntryPanel2) {
                DictionaryEntryPanel2 dictionaryPannel = (DictionaryEntryPanel2) component;
                if (StringUtils.isEmpty(dictionaryPannel.getKeyText().getText())) {
                    continue;
                }
                actualDictionary.put(dictionaryPannel.getKeyText().getText().toUpperCase(), dictionaryPannel.getValueText().getText());
            }
        }
        return actualDictionary;
    }

    private void clearDictionary() {
        for (Component component : dictionaryPannel.getComponents()) {
            if (component instanceof DictionaryEntryPanel2) {
                dictionaryPannel.remove(component);
            }
        }
    }

    private void repaintDictionary(boolean sorted, String filterValue) {
        boolean shouldFilter = StringUtils.isNotBlank(filterValue);
        clearDictionary();

        Map<String, String> dictionaryToPaint;
        if (sorted) {
            dictionaryToPaint = new TreeMap<>(currendDictionary);
        } else {
            dictionaryToPaint = currendDictionary;
        }

        for (Map.Entry<String, String> dictionaryEntry : dictionaryToPaint.entrySet()) {
            if (!shouldFilter || dictionaryEntry.getKey().contains(filterValue.toUpperCase())) {
                DictionaryEntryPanel2 panel = new DictionaryEntryPanel2(this, dictionaryEntry.getKey(), dictionaryEntry.getValue());
                dictionaryPannel.add(panel);
            }
        }

        this.pack();
        this.repaint();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        processButton = new javax.swing.JButton();
        textPannel = new javax.swing.JPanel();
        dictionaryPannel = new javax.swing.JPanel();
        addEntryButton = new javax.swing.JButton();
        saveDictionaryButton = new javax.swing.JButton();
        reloadDictionaryButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jTextField1 = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        sortAlpabetically = new javax.swing.JToggleButton();

        dictionaryScrollPane = new JScrollPane(dictionaryPannel);
        dictionaryScrollPane.setPreferredSize(new Dimension(700, 800));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1024, 800));
        setMinimumSize(new java.awt.Dimension(1024, 800));
        setSize(new java.awt.Dimension(1024, 800));

        processButton.setText("Proceseaza");
        processButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processButtonActionPerformed(evt);
            }
        });

        textPannel.setBorder(javax.swing.BorderFactory.createTitledBorder("Text"));
        textPannel.setLayout(new javax.swing.BoxLayout(textPannel, javax.swing.BoxLayout.Y_AXIS));

        dictionaryPannel.setBorder(javax.swing.BorderFactory.createTitledBorder("Dictionar"));
        dictionaryPannel.setLayout(new javax.swing.BoxLayout(dictionaryPannel, javax.swing.BoxLayout.Y_AXIS));

        addEntryButton.setText("Adauga Intrare");
        addEntryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                addEntryButtonActionPerformed(evt);
            }
        });

        saveDictionaryButton.setText("Salveaza Dictionar");
        saveDictionaryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveDictionaryButtonActionPerformed(evt);
            }
        });

        reloadDictionaryButton.setText("Rincarca Dictionar");
        reloadDictionaryButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reloadDictionaryButtonActionPerformed(evt);
            }
        });

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/info3.png"))); // NOI18N
        jLabel1.setToolTipText("<html>\nProcesarea textului de mai jos se face in urmatorul fel:\n<ol>\n<li>\nToate cheile din text de forma <b>[CHEIE]</b> vor fi inlocuite cu valoarea corespunzatoare din dictionar\n<br/>\nCautarea cheii in dictionar este \"case insensitive\"\n</li>\n\n<li>\nToate tabelele care au<b> exact 4 coloane</b>, vor fi procesate in felul urmator:\n<ul>\n<li>Primul rand este lasat neatins</li>\n<li>Pentru toate randurile urmatoare, valoarea coloanei 4 va fi calculata in felul urmator \"Col4 = Col3*Col2\"</li>\n<li>Pe ulrimul rand din tabel, in ultima coloana, se va calcula totalul de pe Coloana 4</li>\n</ul>\n\nExemplu:\n\n    <table border=\"1\" cellspacing=\"2\" cellpadding=\"4\" width=\"100%\" valign=\"top\">\n      <tr>\n        <td>\n          <b>Produs\n</b>        </td>\n        <td>\n          <b>Cantitate\n</b>        </td>\n        <td>\n          <b>Pret/unitate\n</b>        </td>\n        <td>\n          <b>Total</b>\n        </td>\n      </tr>\n      <tr>\n        <td>\n          Masa\n        </td>\n        <td>\n          2\n        </td>\n        <td>\n          2.15<b><font color=\"#ff0000\">\n</font></b>        </td>\n        <td>\n          <b><font color=\"#ff0000\">4.30</font></b>\n        </td>\n      </tr>\n      <tr>\n        <td>\n          Scaun\n        </td>\n        <td>\n          5.21\n        </td>\n        <td>\n          2.12<b>\n</b>        </td>\n        <td>\n          <b><font color=\"#ff0000\">11.05</font></b><font color=\"#ff0000\">\n</font>        </td>\n      </tr>\n      <tr>\n        <td>\n          <font color=\"#ff0000\">\n</font>        </td>\n        <td>\n          <font color=\"#ff0000\">\n</font>        </td>\n        <td>\n          <b><font color=\"#ff0000\">\n</font></b>        </td>\n        <td>\n          <b><font color=\"#ff0000\">15.35</font></b>\n        </td>\n      </tr>\n    </table>\n\n</li>\n</ol>\n</html>");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/find.png"))); // NOI18N

        sortAlpabetically.setText("A-Z");
        sortAlpabetically.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent e) {
                sortAlpabeticallyActionPerformed(e);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textPannel, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(processButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel1)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(dictionaryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addEntryButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveDictionaryButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reloadDictionaryButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(sortAlpabetically)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel2)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(processButton)
                        .addComponent(addEntryButton)
                        .addComponent(saveDictionaryButton)
                        .addComponent(reloadDictionaryButton)
                        .addComponent(jLabel1)
                        .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(sortAlpabetically)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textPannel, javax.swing.GroupLayout.DEFAULT_SIZE, 773, Short.MAX_VALUE)
                    .addComponent(dictionaryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void sortAlpabeticallyActionPerformed(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_sortAlpabeticallyActionPerformed
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            repaintDictionary(true, jTextField1.getText());
        } else if (evt.getStateChange() == ItemEvent.DESELECTED) {
            repaintDictionary(false, jTextField1.getText());
        }
    }//GEN-LAST:event_sortAlpabeticallyActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addEntryButton;
    private javax.swing.JPanel dictionaryPannel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JButton processButton;
    private javax.swing.JButton reloadDictionaryButton;
    private javax.swing.JButton saveDictionaryButton;
    private javax.swing.JToggleButton sortAlpabetically;
    private javax.swing.JPanel textPannel;
    // End of variables declaration//GEN-END:variables


}
