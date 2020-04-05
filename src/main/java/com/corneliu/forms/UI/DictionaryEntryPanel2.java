/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corneliu.forms.UI;

import javax.swing.*;
import java.awt.*;

public class DictionaryEntryPanel2 extends javax.swing.JPanel {

    public DictionaryEntryPanel2(String key, String value, int index) {
        initComponents();
        keyText.setText(key);
        valueText.setText(value);
        if (index % 2 == 0) {
            keyText.setBackground(Color.WHITE);
            valueText.setBackground(Color.WHITE);
        } else {
            keyText.setBackground(Color.LIGHT_GRAY);
            valueText.setBackground(Color.LIGHT_GRAY);
        }
    }

    public JTextField getKeyText() {
        return keyText;
    }

    public JTextField getValueText() {
        return valueText;
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        keyText = new javax.swing.JTextField();
        valueText = new javax.swing.JTextField();
        moveUp = new JButton("/\\");
        moveDown = new JButton("\\/");

        moveUp.setMargin(new Insets(0,0,0,0));
        moveDown.setMargin(new Insets(0,0,0,0));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(keyText, javax.swing.GroupLayout.PREFERRED_SIZE, 158, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valueText, javax.swing.GroupLayout.DEFAULT_SIZE, 350, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(moveUp, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(moveDown, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(keyText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(valueText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(moveUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(moveDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField keyText;
    private javax.swing.JTextField valueText;
    private javax.swing.JButton moveUp;
    private javax.swing.JButton moveDown;
    // End of variables declaration//GEN-END:variables
}
