package com.kevydn.redes.client.gui;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import java.awt.BorderLayout;

public class BasicWindow {
    private final ClientUIManager ui;
    private JTextArea chatArea;
    private JTextField inputField;

    public BasicWindow(ClientUIManager ui) {
        this.ui = ui;
    }

    public void show() {
        JFrame frame = new JFrame("JamChat - Cliente");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        chatArea = new JTextArea();
        chatArea.setEditable(false);
        frame.add(new JScrollPane(chatArea), BorderLayout.CENTER);

        inputField = new JTextField();
        inputField.addActionListener(e -> {
            String msg = inputField.getText().trim();
            if (!msg.isEmpty()) {
                ui.onUserMessage(msg);
                inputField.setText("");
            }
        });

        frame.add(inputField, BorderLayout.SOUTH);
        frame.setVisible(true);
    }

    public void appendMessage(String message) {
        SwingUtilities.invokeLater(() -> chatArea.append(message + "\n"));
    }
}
