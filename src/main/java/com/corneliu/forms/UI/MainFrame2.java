package com.corneliu.forms.UI;

import com.corneliu.forms.service.TextProcessor;
import com.corneliu.forms.service.impl.TextProcessorImpl;
import com.hexidec.ekit.EkitCore;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainFrame2 extends javax.swing.JFrame {

    private TextProcessor textProcessor;
    private EkitCore editor1;
//    private HTMLEditorPane editor2;


    public MainFrame2() throws FileNotFoundException {
        textProcessor = new TextProcessorImpl();
        initComponents();

        initEditor1();
//        initEditor2();

        loadDictionary();
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
        editor1 = new EkitCore(false, null, null, null, null, null, true, false, true, true, "en", "US", false, true, false, true, "FN|CT|CP|PS|PX|UN|RE|*|BL|IT|UD|SK|SU|SB|UL|OL|AL|AC|AR|AJ|SP|UC|UM|LK|TI|TE|CE|RI|CI|RD|CD|SR|FO", true, true);
        editor1.setPreferredSize(new Dimension(800, 600));
        editor1.setSize(new Dimension(800, 600));

        JMenuBar menuBar = editor1.getMenuBar();
        menuBar.remove(0);  // remove file menu
        menuBar.remove(8);  // remuve help menu
        menuBar.remove(8);  // remove debug menu
        menuBar.remove(1);  // remove view menu
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

    private void processButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processButtonActionPerformed
        Map<String, String> actualDictionary = computeActualDictionary();

        String processedText = textProcessor.process(editor1.getDocumentText(), actualDictionary);
        editor1.setDocumentText(processedText);

//        String processedText = textProcessor.process(editor.getText(), actualDictionary);
//        editor.setText(processedText);
    }//GEN-LAST:event_processButtonActionPerformed

    private void addEntryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEntryButtonActionPerformed
        DictionaryEntryPannel2 panel = new DictionaryEntryPannel2("", "");
        dictionaryPannel.add(panel);
        panel.getKeyText().grabFocus();
        this.pack();
    }//GEN-LAST:event_addEntryButtonActionPerformed

    private void saveDictionaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDictionaryButtonActionPerformed
        Map<String, String> actualDictionary = computeActualDictionary();
        try {
            textProcessor.saveDictionary(actualDictionary);
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
        clearDictionary();
        loadDictionary();
        this.pack();
        this.repaint();
    }

    private Map<String, String> computeActualDictionary() {
        Map<String, String> actualDictionary = new HashMap<>();

        for (Component component : dictionaryPannel.getComponents()) {
            if (component instanceof DictionaryEntryPannel2) {
                DictionaryEntryPannel2 dictionaryPannel = (DictionaryEntryPannel2) component;
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
            if (component instanceof DictionaryEntryPannel2) {
                dictionaryPannel.remove(component);
            }
        }
    }

    private void loadDictionary() throws FileNotFoundException {
        for (Map.Entry<String, String> dictionaryEntry : textProcessor.getDictionary().entrySet()) {
            DictionaryEntryPannel2 panel = new DictionaryEntryPannel2(dictionaryEntry.getKey(), dictionaryEntry.getValue());
            dictionaryPannel.add(panel);
        }
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

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textPannel, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(processButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(addEntryButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveDictionaryButton)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(reloadDictionaryButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(dictionaryPannel, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(processButton)
                    .addComponent(addEntryButton)
                    .addComponent(saveDictionaryButton)
                    .addComponent(reloadDictionaryButton))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(textPannel, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
                    .addComponent(dictionaryPannel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addEntryButton;
    private javax.swing.JPanel dictionaryPannel;
    private javax.swing.JButton processButton;
    private javax.swing.JButton reloadDictionaryButton;
    private javax.swing.JButton saveDictionaryButton;
    private javax.swing.JPanel textPannel;
    // End of variables declaration//GEN-END:variables


}
