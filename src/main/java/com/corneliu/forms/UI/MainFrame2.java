package com.corneliu.forms.UI;

import com.corneliu.forms.service.DocumentProcessor;
import com.corneliu.forms.service.DocumentType;
import com.corneliu.forms.service.impl.DocumentProcessorImpl;
import com.hexidec.ekit.EkitCore;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

public class MainFrame2 extends javax.swing.JFrame {

    private DocumentProcessor textProcessor;
    private EkitCore editor1;
    private JScrollPane dictionaryScrollPane;
    private int totalDictionaryEntries;
    //    private HTMLEditorPane editor2;


    public MainFrame2() throws FileNotFoundException {
        textProcessor = new DocumentProcessorImpl();
        initComponents();

        ToolTipManager.sharedInstance().setInitialDelay(500);
        ToolTipManager.sharedInstance().setLightWeightPopupEnabled(true);
        ToolTipManager.sharedInstance().setDismissDelay(30000);
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

    private void processSecuritateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processSecuritateButtonActionPerformed
        Map<String, String> actualDictionary = computeActualDictionary();

        String processedText = textProcessor.process(editor1.getDocumentText(), actualDictionary, DocumentType.SECURITATE);
        editor1.setDocumentText(processedText);

    }//GEN-LAST:event_processSecuritateButtonActionPerformed

    private void processIncendiuButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processIncendiuButtonActionPerformed
        Map<String, String> actualDictionary = computeActualDictionary();

        String processedText = textProcessor.process(editor1.getDocumentText(), actualDictionary, DocumentType.INCENDIU);
        editor1.setDocumentText(processedText);

    }//GEN-LAST:event_processIncendiuButtonActionPerformed

    private void addEntryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEntryButtonActionPerformed
        DictionaryEntryPanel2 panel = new DictionaryEntryPanel2("", "", totalDictionaryEntries++);
        dictionaryPannel.add(panel);
        panel.getKeyText().grabFocus();

        panel.getKeyText().getDocument().addDocumentListener(markUnsavedDictionary(this));
        panel.getValueText().getDocument().addDocumentListener(markUnsavedDictionary(this));

        this.pack();
    }//GEN-LAST:event_addEntryButtonActionPerformed

    private DocumentListener markUnsavedDictionary(MainFrame2 mainFrame) {
        return new DocumentListener() {
              public void changedUpdate(DocumentEvent e) {
                  ((TitledBorder)dictionaryPannel.getBorder()).setTitle("Dictionar *  -  Exista modificari nesalvate!");
                  ((TitledBorder)dictionaryPannel.getBorder()).setTitleColor(Color.RED);
                  ((TitledBorder)dictionaryPannel.getBorder()).setBorder(new LineBorder(Color.RED));
                  mainFrame.pack();
                  mainFrame.repaint();
              }
              public void removeUpdate(DocumentEvent e) {
                  ((TitledBorder)dictionaryPannel.getBorder()).setTitle("Dictionar *  -  Exista modificari nesalvate!");
                  ((TitledBorder)dictionaryPannel.getBorder()).setTitleColor(Color.RED);
                  ((TitledBorder)dictionaryPannel.getBorder()).setBorder(new LineBorder(Color.RED));
                  mainFrame.pack();
                  mainFrame.repaint();
              }
              public void insertUpdate(DocumentEvent e) {
                  ((TitledBorder)dictionaryPannel.getBorder()).setTitle("Dictionar *  -  Exista modificari nesalvate!");
                  ((TitledBorder)dictionaryPannel.getBorder()).setTitleColor(Color.RED);
                  ((TitledBorder)dictionaryPannel.getBorder()).setBorder(new LineBorder(Color.RED));
                  mainFrame.pack();
                  mainFrame.repaint();
              }
        };
    }

    private void saveDictionaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveDictionaryButtonActionPerformed
        Map<String, String> actualDictionary = computeActualDictionary();
        try {
            textProcessor.saveDictionary(actualDictionary);
            ((TitledBorder)dictionaryPannel.getBorder()).setTitle("Dictionar");
            ((TitledBorder)dictionaryPannel.getBorder()).setBorder(new LineBorder(Color.BLACK));
            ((TitledBorder)dictionaryPannel.getBorder()).setTitleColor(Color.BLACK);
            reloadDictionary();
        } catch (IOException e) {
            JOptionPane.showConfirmDialog(this, "Nu a reusit salvarea dictionarului." + e.getMessage());
        }
    }//GEN-LAST:event_saveDictionaryButtonActionPerformed

    private void reloadDictionaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadDictionaryButtonActionPerformed
        try {
            ((TitledBorder)dictionaryPannel.getBorder()).setTitle("Dictionar");
            ((TitledBorder)dictionaryPannel.getBorder()).setBorder(new LineBorder(Color.BLACK));
            ((TitledBorder)dictionaryPannel.getBorder()).setTitleColor(Color.BLACK);
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

    private void loadDictionary() throws FileNotFoundException {
        totalDictionaryEntries = 0;
        for (Map.Entry<String, String> dictionaryEntry : textProcessor.getDictionary().entrySet()) {
            DictionaryEntryPanel2 panel = new DictionaryEntryPanel2(dictionaryEntry.getKey(), dictionaryEntry.getValue(), totalDictionaryEntries++);
            dictionaryPannel.add(panel);

            panel.getKeyText().getDocument().addDocumentListener(markUnsavedDictionary(this));
            panel.getValueText().getDocument().addDocumentListener(markUnsavedDictionary(this));
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        processSecuritateButton = new javax.swing.JButton();
        processIncendiuButton = new javax.swing.JButton();
        textPannel = new javax.swing.JPanel();
        dictionaryPannel = new javax.swing.JPanel();
        addEntryButton = new javax.swing.JButton();
        saveDictionaryButton = new javax.swing.JButton();
        reloadDictionaryButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();

        findLabel = new JLabel("Cauta:");
        findText = new JTextField();
        findText.setMaximumSize(new Dimension(420, 15));
        findText.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                findEntry(findText.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                findEntry(findText.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                findEntry(findText.getText());
            }
        });

        dictionaryScrollPane = new JScrollPane(dictionaryPannel);
        dictionaryScrollPane.setPreferredSize(new Dimension(700, 800));

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMaximumSize(new java.awt.Dimension(1024, 800));
        setMinimumSize(new java.awt.Dimension(1024, 800));
        setSize(new java.awt.Dimension(1024, 800));

        processSecuritateButton.setText("Proceseaza Analiza Securitate");
        processSecuritateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processSecuritateButtonActionPerformed(evt);
            }
        });

        processIncendiuButton.setText("Proceseaza Analiza Incendiu");
        processIncendiuButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processIncendiuButtonActionPerformed(evt);
            }
        });

        textPannel.setBorder(javax.swing.BorderFactory.createTitledBorder( "Text"));
        textPannel.setLayout(new javax.swing.BoxLayout(textPannel, javax.swing.BoxLayout.Y_AXIS));

        dictionaryPannel.setBorder(javax.swing.BorderFactory.createTitledBorder(new LineBorder(Color.BLACK), "Dictionar"));
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
        jLabel1.setToolTipText("<html>\nProcesarea textului de mai jos se face in urmatorul fel:" +
                "\n<ol>\n" +
                    "<li>\nToate cheile din text de forma <b>[CHEIE]</b> vor fi inlocuite cu valoarea corespunzatoare din dictionar\n<br/>\nCautarea cheii in dictionar este \"case insensitive\"\n</li>\n\n" +
                    "<li>\nToate tabelele care au<b> exact 4 coloane</b>, vor fi procesate in felul urmator:\n" +
                        "<ul>\n" +
                            "<li>Primul rand este lasat neatins</li>\n<li>Pentru toate randurile urmatoare, valoarea coloanei 4 va fi calculata in felul urmator \"Col4 = Col3*Col2\"</li>\n" +
                            "<li>Pe ulrimul rand din tabel, in ultima coloana, se va calcula totalul de pe Coloana 4</li>\n" +
//                        "</ul>\n\nExemplu:\n\n    <table border=\"1\" cellspacing=\"2\" cellpadding=\"4\" width=\"100%\" valign=\"top\">\n      <tr>\n        <td>\n          <b>Produs\n</b>        </td>\n        <td>\n          <b>Cantitate\n</b>        </td>\n        <td>\n          <b>Pret/unitate\n</b>        </td>\n        <td>\n          <b>Total</b>\n        </td>\n      </tr>\n      <tr>\n        <td>\n          Masa\n        </td>\n        <td>\n          2\n        </td>\n        <td>\n          2.15<b><font color=\"#ff0000\">\n</font></b>        </td>\n        <td>\n          <b><font color=\"#ff0000\">4.30</font></b>\n        </td>\n      </tr>\n      <tr>\n        <td>\n          Scaun\n        </td>\n        <td>\n          5.21\n        </td>\n        <td>\n          2.12<b>\n</b>        </td>\n        <td>\n          <b><font color=\"#ff0000\">11.05</font></b><font color=\"#ff0000\">\n</font>        </td>\n      </tr>\n      <tr>\n        <td>\n          <font color=\"#ff0000\">\n</font>        </td>\n        <td>\n          <font color=\"#ff0000\">\n</font>        </td>\n        <td>\n          <b><font color=\"#ff0000\">\n</font></b>        </td>\n        <td>\n          <b><font color=\"#ff0000\">15.35</font></b>\n        </td>\n      </tr>\n    </table>\n\n" +
                        "</ul>\n\nExemplu:\n\n" +
                            "    <table cellspacing=\"0\" border=\"0\">\n" +
                            "      <tr>\n" +
                            "        <td height=\"64\" align=\"justify\" valign=\"top\" bgcolor=\"#CCFFFF\" style=\"border-top-color: #3c3c3c; border-top-style: solid; border-top-width: 1px; border-bottom-color: #3c3c3c; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #3c3c3c; border-left-style: solid; border-left-width: 1px; border-right-color: #3c3c3c; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <b><font size=\"2\">Consumator </font></b>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" bgcolor=\"#CCFFFF\" style=\"border-top-color: #3c3c3c; border-top-style: solid; border-top-width: 1px; border-bottom-color: #3c3c3c; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #3c3c3c; border-left-style: solid; border-left-width: 1px; border-right-color: #3c3c3c; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <b><font size=\"2\">Buc</font></b>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" bgcolor=\"#CCFFFF\" style=\"border-top-color: #3c3c3c; border-top-style: solid; border-top-width: 1px; border-bottom-color: #3c3c3c; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #3c3c3c; border-left-style: solid; border-left-width: 1px; border-right-color: #3c3c3c; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <b><font size=\"2\">Curent mediu consumat(mA)</font></b>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" bgcolor=\"#CCFFFF\" style=\"border-top-color: #3c3c3c; border-top-style: solid; border-top-width: 1px; border-bottom-color: #3c3c3c; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #3c3c3c; border-left-style: solid; border-left-width: 1px; border-right-color: #3c3c3c; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <b><font size=\"2\">Total consum (mA)</font></b>\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">Centrala instalatiei 1e</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"1\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">1</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"50\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">50</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"50\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          50.00\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">Tastatura 1g</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"1\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">1</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"30\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">30</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"30\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          30.00\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">Detector geam spart</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"0\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">0</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"40\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">40</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"0\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          0.00\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">Detector miscare lc 100</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"0\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">0</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"10\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">10</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"0\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          0.00\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">Detector de soc</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"1\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">1</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"25\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">25</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"25\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          25.00\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">Sirena exterioar&#259;</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"1\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">1</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"10\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">10</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"10\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          10.00\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">Tastatura 2c</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"0\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">0</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"30\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">30</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"0\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          0.00\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">Detector miscare lc 104 pimw</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"1\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">1</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"30\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">30</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"30\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          30.00\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">Modul gprs</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"0\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">0</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"80\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">80</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"0\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          0.00\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\"><br>\n" +
                            "          </font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\"><br>\n" +
                            "          </font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\"><br>\n" +
                            "          </font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\"><br>\n" +
                            "          </font>\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td colspan=\"3\" height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">TOTAL consum(mA)</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"145\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font color=\"#ff0000\">145.00</font>\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td colspan=\"3\" height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">TOTAL consum pentru 23,1/2(Ah)</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"3407.5\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font color=\"#ff0000\">3407.50</font>\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">Sirena interioara</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"right\" valign=\"middle\" sdval=\"2\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          2\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"400\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">400</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"800\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          800.00\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td colspan=\"3\" height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">TOTAL consum pentru 30min. Alarm&#259; (Ah)</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"400\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font color=\"#ff0000\">400.00</font>\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td colspan=\"3\" height=\"23\" align=\"justify\" valign=\"top\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font size=\"2\">TOTAL consum(mAh, prentru 23,1/2h+30min. alarma)</font>\n" +
                            "        </td>\n" +
                            "        <td align=\"justify\" valign=\"top\" sdval=\"3.8075\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          <font color=\"#ff0000\">3.81</font>\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "      <tr>\n" +
                            "        <td colspan=\"3\" height=\"23\" align=\"left\" valign=\"middle\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          TOTAL CONSUM Ah\n" +
                            "        </td>\n" +
                            "        <td align=\"right\" valign=\"middle\" bgcolor=\"#FF0000\" sdval=\"3.8075\" sdnum=\"1033;\" style=\"border-top-color: #000000; border-top-style: solid; border-top-width: 1px; border-bottom-color: #000000; border-bottom-style: solid; border-bottom-width: 1px; border-left-color: #000000; border-left-style: solid; border-left-width: 1px; border-right-color: #000000; border-right-style: solid; border-right-width: 1px\">\n" +
                            "          3.81\n" +
                            "        </td>\n" +
                            "      </tr>\n" +
                            "    </table>" +
                    "</li>\n" +
                "</ol>\n</html>");
        jLabel1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/info3.png"))); // NOI18N
        jLabel2.setToolTipText("<html>\nProcesarea textului de mai jos se face in urmatorul fel:");
        jLabel2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(textPannel, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(processSecuritateButton)
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel1)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(processIncendiuButton)
//                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(jLabel2)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(addEntryButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(saveDictionaryButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(reloadDictionaryButton)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(findLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(findText)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                        .addComponent(dictionaryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(processSecuritateButton)
                                        .addComponent(processIncendiuButton)
                                        .addComponent(addEntryButton)
                                        .addComponent(saveDictionaryButton)
                                        .addComponent(reloadDictionaryButton)
                                        .addComponent(jLabel1)
                                        .addComponent(jLabel2))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(findLabel)
                                        .addComponent(findText))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(textPannel, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
                                        .addComponent(dictionaryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void findEntry(String text) {
        for (Component component : dictionaryPannel.getComponents()) {
            if (component instanceof DictionaryEntryPanel2) {
                if (!((DictionaryEntryPanel2) component).getKeyText().getText().toLowerCase().contains(text.toLowerCase())) {
                    component.setVisible(false);
                } else {
                    component.setVisible(true);
                }
            }
        }
        this.pack();
        this.repaint();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dictionaryPannel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel textPannel;
    private javax.swing.JButton processSecuritateButton;
    private javax.swing.JButton processIncendiuButton;
    private javax.swing.JButton addEntryButton;
    private javax.swing.JButton reloadDictionaryButton;
    private javax.swing.JButton saveDictionaryButton;
    private javax.swing.JLabel findLabel;
    private javax.swing.JTextField findText;
    // End of variables declaration//GEN-END:variables


}
