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

        loadDictionary(getDocumentType());
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

    private void processDocumentButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_processSecuritateButtonActionPerformed
        try {
            DocumentType documentType = getDocumentType();
            if (documentType == null) {
                throw new RuntimeException("Selectati tipul documentului.");
            }

            Map<String, String> actualDictionary = computeActualDictionary();

            String processedText = textProcessor.process(editor1.getDocumentText(), actualDictionary, documentType);
            editor1.setDocumentText(processedText);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
            e.printStackTrace();
        }

    }//GEN-LAST:event_processSecuritateButtonActionPerformed

    private void addEntryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_addEntryButtonActionPerformed
        DictionaryEntryPanel2 panel = new DictionaryEntryPanel2(this, "", "", totalDictionaryEntries++);
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
            DocumentType documentType = getDocumentType();
            if (documentType == null) {
                throw new RuntimeException("Selectati tipul documentului.");
            }

            textProcessor.saveDictionary(documentType, actualDictionary);
            reloadDictionary();
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Nu a reusit salvarea dictionarului." + e.getMessage());
        }
    }//GEN-LAST:event_saveDictionaryButtonActionPerformed

    private void reloadDictionaryButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reloadDictionaryButtonActionPerformed
        reloadDictionary();
    }//GEN-LAST:event_reloadDictionaryButtonActionPerformed

    public void reloadDictionary() {
        try {
            ((TitledBorder)dictionaryPannel.getBorder()).setTitle("Dictionar");
            ((TitledBorder)dictionaryPannel.getBorder()).setBorder(new LineBorder(Color.BLACK));
            ((TitledBorder)dictionaryPannel.getBorder()).setTitleColor(Color.BLACK);

            clearDictionary();
            DocumentType documentType = getDocumentType();
            if (documentType != null) {
                loadDictionary(documentType);
            }
            this.pack();
            this.repaint();
        } catch (Exception e) {
            clearDictionary();
            this.pack();
            this.repaint();
            JOptionPane.showMessageDialog(this, "Nu a reusit incarcarea dictionarului." + e.getMessage());
        }
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

    private void loadDictionary(DocumentType documentType) throws FileNotFoundException {
        totalDictionaryEntries = 0;
        for (Map.Entry<String, String> dictionaryEntry : textProcessor.getDictionary(documentType).entrySet()) {
            DictionaryEntryPanel2 panel = new DictionaryEntryPanel2(this, dictionaryEntry.getKey(), dictionaryEntry.getValue(), totalDictionaryEntries++);
            dictionaryPannel.add(panel);

            panel.getKeyText().getDocument().addDocumentListener(markUnsavedDictionary(this));
            panel.getValueText().getDocument().addDocumentListener(markUnsavedDictionary(this));
        }
    }

    private DocumentType getDocumentType() {
        if (computingDocumentRadio.isSelected()) {
            String selectedItem = (String) computingDocumentSelectionPanel.getComputingDocTypeCombo().getSelectedItem();
            if (selectedItem.equalsIgnoreCase(DocumentType.INCENDIU.name())) {
                return DocumentType.INCENDIU;
            } else {
                return DocumentType.SECURITATE;
            }
        } else {
            String selectedItem = (String) simpleDocumentSelectionPanel.getSimpleDocTypeCombo().getSelectedItem();
            if (selectedItem == null) {
                return null;
            }
            DocumentType.SIMPLE.setName(selectedItem.toUpperCase().replace(" ", "_"));
            return DocumentType.SIMPLE;
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        documentSelectionPanel = new JPanel();
        computingDocumentSelectionPanel = new ComputingDocumentTypePanel(this);
        simpleDocumentSelectionPanel = new SimpleDocumentTypePanel(this);
//        documentSelectionPanel.add(simpleDocumentSelectionPanel);
        documentSelectionPanel.add(computingDocumentSelectionPanel);

        processDocumentButton = new javax.swing.JButton();
        computingDocumentRadio = new JRadioButton("Cu calcule", true);
        simpleDocumentRadio = new JRadioButton("Simplu", false);

        documentTypeGroup = new ButtonGroup();
        documentTypeGroup.add(computingDocumentRadio);
        documentTypeGroup.add(simpleDocumentRadio);

        jLabel2 = new JLabel();
        computingDocumentRadio.addActionListener(e -> switchDocumentTypePanel());
        simpleDocumentRadio.addActionListener(e -> switchDocumentTypePanel());

        textPannel = new javax.swing.JPanel();
        dictionaryPannel = new javax.swing.JPanel();
        addEntryButton = new javax.swing.JButton();
        saveDictionaryButton = new javax.swing.JButton();
        reloadDictionaryButton = new javax.swing.JButton();

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

        processDocumentButton.setText("Proceseaza Document");
        processDocumentButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                processDocumentButtonActionPerformed(evt);
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

        jLabel2.setText("Tipul Documentului: ");


        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(jLabel2)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(computingDocumentRadio)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(simpleDocumentRadio)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(documentSelectionPanel)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(processDocumentButton)
                                        )
                                        .addComponent(textPannel, javax.swing.GroupLayout.PREFERRED_SIZE, 860, javax.swing.GroupLayout.PREFERRED_SIZE)
                                )
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(addEntryButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(saveDictionaryButton)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(reloadDictionaryButton)
                                        )
                                        .addGroup(layout.createSequentialGroup()
                                                .addComponent(findLabel)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(findText)
                                        )
                                        .addComponent(dictionaryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, 587, Short.MAX_VALUE))
                                .addContainerGap())
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addContainerGap()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(simpleDocumentRadio)
                                        .addComponent(computingDocumentRadio)
                                        .addComponent(addEntryButton)
                                        .addComponent(saveDictionaryButton)
                                        .addComponent(reloadDictionaryButton)
                                )
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(documentSelectionPanel)
                                        .addComponent(findLabel)
                                        .addComponent(findText)
                                )
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(processDocumentButton)
                                )
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                        .addComponent(textPannel, javax.swing.GroupLayout.DEFAULT_SIZE, 757, Short.MAX_VALUE)
                                        .addComponent(dictionaryScrollPane, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void switchDocumentTypePanel() {
        documentSelectionPanel.removeAll();
        if (computingDocumentRadio.isSelected()) {
            documentSelectionPanel.add(computingDocumentSelectionPanel);
        } else {
            documentSelectionPanel.add(simpleDocumentSelectionPanel);
        }

        reloadDictionary();

        this.pack();
        this.repaint();
    }

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

    public void moveDictionary(int sourceIndex, int targetIndex) {
        Component component = dictionaryPannel.getComponent(sourceIndex);
        dictionaryPannel.remove(sourceIndex);
        dictionaryPannel.add(component, targetIndex);

        int index = 0;
        for (Component crtComponent : dictionaryPannel.getComponents()) {
            if (crtComponent instanceof DictionaryEntryPanel2) {
                ((DictionaryEntryPanel2)crtComponent).setIndex(index++);
            }
        }

        ((TitledBorder)dictionaryPannel.getBorder()).setTitle("Dictionar *  -  Exista modificari nesalvate!");
        ((TitledBorder)dictionaryPannel.getBorder()).setTitleColor(Color.RED);
        ((TitledBorder)dictionaryPannel.getBorder()).setBorder(new LineBorder(Color.RED));

        this.pack();
        this.repaint();
    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel dictionaryPannel;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel textPannel;

    private javax.swing.JPanel documentSelectionPanel;
    private ComputingDocumentTypePanel computingDocumentSelectionPanel;
    private SimpleDocumentTypePanel simpleDocumentSelectionPanel;

    private javax.swing.JButton processDocumentButton;

    private javax.swing.JRadioButton computingDocumentRadio;
    private javax.swing.JRadioButton simpleDocumentRadio;
    private javax.swing.ButtonGroup documentTypeGroup;

    private javax.swing.JButton addEntryButton;
    private javax.swing.JButton reloadDictionaryButton;
    private javax.swing.JButton saveDictionaryButton;
    private javax.swing.JLabel findLabel;
    private javax.swing.JTextField findText;
    // End of variables declaration//GEN-END:variables

}
