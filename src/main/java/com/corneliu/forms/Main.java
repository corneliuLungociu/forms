package com.corneliu.forms;

import com.corneliu.forms.UI.MainFrame2;

import javax.swing.*;

public class Main {

    public static void main(String[] args) {
        try {
            new MainFrame2().setVisible(true);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Failed to start the application: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }
    }

}
