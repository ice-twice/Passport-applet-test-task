package com.java;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class Main extends Applet {
    private static final String INPUT = "Input ";
    private static final String[] PASSPORT_FILED_NAMES = new String[]{"passportNo", "surname", "given names", "patronymic",
            "date birth", "place birth", "authority", "date of issue"};
    private JPanel rootJPanel;

    @Override
    public void init() {
        setSize(500, 500);
        JPanel topButtonJPanel = initializeTopButtons();
        rootJPanel = new JPanel();
        rootJPanel.setLayout(new BoxLayout(rootJPanel, BoxLayout.Y_AXIS));
        rootJPanel.add(topButtonJPanel);
        this.add(rootJPanel);
    }

    private JPanel initializeTopButtons() {
        Button openWindowInputPassportDataButton = new Button("Open a new window for entering the passport data");
        openWindowInputPassportDataButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openNewJFrameInputPassportData();
            }
        });

        Button importFileButton = new Button("Import file");
        importFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // empty
            }
        });

        Button exportFileButton = new Button("Export file");
        exportFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // empty
            }
        });

        JPanel topButtonJPanel = new JPanel();
        topButtonJPanel.setLayout(new BoxLayout(topButtonJPanel, BoxLayout.X_AXIS));
        topButtonJPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        topButtonJPanel.add(openWindowInputPassportDataButton);
        topButtonJPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        topButtonJPanel.add(importFileButton);
        topButtonJPanel.add(Box.createRigidArea(new Dimension(5, 0)));
        topButtonJPanel.add(exportFileButton);
        return topButtonJPanel;
    }

    private void openNewJFrameInputPassportData() {
        final JFrame frame = new JFrame("Input Passport Data");

        final ArrayList<Label> labels = initializeLabels();
        final ArrayList<TextField> textFields = initializeTextFields();

        Button okButton = new Button("Ok");
        okButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleOnClickOkButton(labels, textFields, frame);
            }
        });
        Button closeButton = new Button("Close");
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                frame.dispose();
            }
        });

        JPanel jPanelRoot = initializeJPanels(labels, textFields, new Button[]{okButton, closeButton});

        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(jPanelRoot);
        frame.pack();
        frame.setVisible(true);
    }

    private ArrayList<Label> initializeLabels() {
        ArrayList<Label> labels = new ArrayList<>();
        for (String labelNames : PASSPORT_FILED_NAMES) {
            labels.add(new Label(INPUT.concat(labelNames).concat(":")));
        }
        // label error must be last element in array
        final Label errorEmptyFields = new Label("Error! One or more fields are not filled!");
        errorEmptyFields.setVisible(false);
        errorEmptyFields.setForeground(Color.RED);
        labels.add(errorEmptyFields);
        return labels;
    }

    private ArrayList<TextField> initializeTextFields() {
        final int columnsTextField = 40;
        ArrayList<TextField> textFields = new ArrayList<>();
        textFields.add(new TextField(columnsTextField));
        textFields.add(new TextField(columnsTextField));
        textFields.add(new TextField(columnsTextField));
        textFields.add(new TextField(columnsTextField));
        textFields.add(new TextField(columnsTextField));
        textFields.add(new TextField(columnsTextField));
        textFields.add(new TextField(columnsTextField));
        textFields.add(new TextField(columnsTextField));
        return textFields;
    }

    private JPanel initializeJPanels(ArrayList<Label> labels, ArrayList<TextField> textFields, Button[] buttons) {
        JPanel jPanelLabelAndTextFields = new JPanel();
        jPanelLabelAndTextFields.setLayout(new BoxLayout(jPanelLabelAndTextFields, BoxLayout.Y_AXIS));

        int biggerArraySize = labels.size() >= textFields.size() ? labels.size() : textFields.size();
        for (int i = 0; i < biggerArraySize; i++) {
            if (i < labels.size()) {
                jPanelLabelAndTextFields.add(labels.get(i));
            }
            if (i < textFields.size()) {
                jPanelLabelAndTextFields.add(textFields.get(i));
            }
        }

        JPanel jPanelButtons = new JPanel();
        jPanelButtons.setLayout(new BoxLayout(jPanelButtons, BoxLayout.X_AXIS));
        for (Button button : buttons) {
            jPanelButtons.add(button);

        }

        JPanel jPanelRootPassportWindow = new JPanel();
        jPanelRootPassportWindow.setLayout(new BoxLayout(jPanelRootPassportWindow, BoxLayout.Y_AXIS));
        jPanelRootPassportWindow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        jPanelRootPassportWindow.add(jPanelLabelAndTextFields);
        jPanelRootPassportWindow.add(jPanelButtons);

        return jPanelRootPassportWindow;
    }

    private void handleOnClickOkButton(ArrayList<Label> labels, ArrayList<TextField> textFields, JFrame frame) {
        boolean isEmptyField = isEmptyFieldExist(labels, textFields, frame);
        if (!isEmptyField) {
            drawTableWithPassportData(textFields);
        }
    }

    private boolean isEmptyFieldExist(ArrayList<Label> labels, ArrayList<TextField> textFields, JFrame frame) {
        Label errorEmptyLabel = labels.get(labels.size() - 1);
        boolean isEmptyField = false;
        for (TextField field : textFields) {
            if (field.getText().isEmpty()) {
                errorEmptyLabel.setVisible(true);
                frame.pack();
                isEmptyField = true;
                break;
            }
        }
        if (!isEmptyField) {
            if (errorEmptyLabel.isVisible()) {
                errorEmptyLabel.setVisible(false);
                frame.pack();
            }
        }
        return isEmptyField;
    }

    private void drawTableWithPassportData(ArrayList<TextField> textFields) {
        TablePassportData tablePassportData = new TablePassportData(textFields);
        rootJPanel.add(tablePassportData);
        rootJPanel.updateUI();
    }

    private class TablePassportData extends JPanel {
        public TablePassportData(ArrayList<TextField> textFields) {
            setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
            String[][] tableData = new String[textFields.size()][textFields.size()];
            for (int i = 0; i < tableData.length; i++) {
                for (int j = 0; j < 2; j++) {
                    // first column
                    if (j == 0) {
                        tableData[i][j] = PASSPORT_FILED_NAMES[i];
                    } else {
                        tableData[i][j] = textFields.get(i).getText();
                    }
                }
            }
            String[] columnNames = {"Labels", "Data"};
            JTable jTable = new JTable(tableData, columnNames);
            add(jTable);
        }
    }
}