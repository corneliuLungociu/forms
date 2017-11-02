/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corneliu.forms.UI;

import javax.swing.JTextField;

public class DictionaryEntryPanel2 extends javax.swing.JPanel {

    private MainFrame2 mainFrame;
    private String originalKey;

    public DictionaryEntryPanel2(MainFrame2 mainFrame, String key, String value) {
        initComponents();

        this.mainFrame = mainFrame;
        originalKey = key;
        keyText.setText(key);
        valueText.setText(value);
    }

    public JTextField getKeyText() {
        return keyText;
    }

    public JTextField getValueText() {
        return valueText;
    }

    public String getOriginalKey() {
        return originalKey;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        keyText = new javax.swing.JTextField();
        valueText = new javax.swing.JTextField();

        keyText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                keyTextFocusLost(evt);
            }
        });
        keyText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                keyTextActionPerformed(evt);
            }
        });

        valueText.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusLost(java.awt.event.FocusEvent evt) {
                valueTextFocusLost(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(keyText, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valueText, javax.swing.GroupLayout.DEFAULT_SIZE, 427, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(keyText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(valueText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void keyTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_keyTextActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_keyTextActionPerformed

    private void keyTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_keyTextFocusLost
        String newKey = keyText.getText();
        if (!newKey.equalsIgnoreCase(originalKey)) {
            boolean success = mainFrame.updateDictionaryKey(originalKey, newKey, valueText.getText());
            if (success) {
                originalKey = newKey;
            } else {
                keyText.grabFocus();
            }
        }
    }//GEN-LAST:event_keyTextFocusLost

    private void valueTextFocusLost(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_valueTextFocusLost
        mainFrame.updateDictionaryEntry(keyText.getText(), valueText.getText());
    }//GEN-LAST:event_valueTextFocusLost


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField keyText;
    private javax.swing.JTextField valueText;
    // End of variables declaration//GEN-END:variables
}
