/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.corneliu.forms.UI;

import javax.swing.*;
import java.awt.*;

public class DictionaryEntryPanel2 extends javax.swing.JPanel {

    private MainFrame2 mainFrame;

    public DictionaryEntryPanel2(MainFrame2 mainFrame, String key, String value, int index) {
        this.mainFrame = mainFrame;

        initComponents();
        keyText.setText(key);
        valueText.setText(value);
        setIndex(index);
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
        moveTo = new JButton("TO");
        crtIndex = new JLabel("");

        moveUp.setMargin(new Insets(0,0,0,0));
        moveDown.setMargin(new Insets(0,0,0,0));
        moveTo.setMargin(new Insets(0,0,0,0));

        moveTo.addActionListener(e -> moveTo());
        moveUp.addActionListener(e -> moveUp());
        moveDown.addActionListener(e -> moveDown());

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(moveUp, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(moveDown, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(moveTo, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)

                .addComponent(crtIndex, javax.swing.GroupLayout.PREFERRED_SIZE, 25, 25)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(keyText, javax.swing.GroupLayout.PREFERRED_SIZE, 150, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(valueText, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(crtIndex)
                .addComponent(keyText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(valueText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(moveUp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(moveDown, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addComponent(moveTo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void moveDown() {
        int sourceIndex = Integer.valueOf(crtIndex.getText());
        int targetIndex = sourceIndex + 1;

        mainFrame.moveDictionary(sourceIndex, targetIndex);
    }

    private void moveUp() {
        int sourceIndex = Integer.valueOf(crtIndex.getText());
        int targetIndex = sourceIndex - 1;

        mainFrame.moveDictionary(sourceIndex, targetIndex);
    }


    private void moveTo() {
        String selectedLine = JOptionPane.showInputDialog(this, "Muta la linia: ", "0" );
        if (selectedLine == null) {
            return;
        }

        int targetIndex = Integer.valueOf(selectedLine);
        Integer sourceIndex = Integer.valueOf(crtIndex.getText());
        mainFrame.moveDictionary(sourceIndex, targetIndex);

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel crtIndex;
    private javax.swing.JTextField keyText;
    private javax.swing.JTextField valueText;
    private javax.swing.JButton moveUp;
    private javax.swing.JButton moveDown;
    private javax.swing.JButton moveTo;


    public void setIndex(int index) {
        crtIndex.setText(String.valueOf(index));
        if (index % 2 == 0) {
            keyText.setBackground(Color.WHITE);
            valueText.setBackground(Color.WHITE);
        } else {
            keyText.setBackground(Color.LIGHT_GRAY);
            valueText.setBackground(Color.LIGHT_GRAY);
        }
    }
    // End of variables declaration//GEN-END:variables
}
