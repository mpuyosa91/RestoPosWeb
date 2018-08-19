package com.mpuyosa91.posaplications.RestoPosWeb._02_Views._1_Pedidos.Mesas;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem_Ingredients;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;
import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.MainFrame;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.Templates.MiniWindowDialog;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * @author MoisesE
 */
public class AgregarProductoADeLaCarta extends MiniWindowDialog {

    //------------------------------------------------------------------------------
    private SalableItem        dto;
    private JPanel             mainPanel;
    //------------------------------------------------------------
    private JTextField         ingresarCodigoTextField;
    private JTextField         notaComandaTextField;
    //------------------------------------------------------------
    private ArrayList<JLabel>  labelList;
    private ArrayList<JButton> buttonList;

    public AgregarProductoADeLaCarta(MainFrame contextFrame, javax.swing.JFrame frame, boolean modal) {
        super(contextFrame, frame, modal);
        createObjects();
        createPanels();
        this.btnExit.setVisible(false);
    }

    public void startFrame(SalableItem dto) {
        this.dto = dto;
        informationLabel.setText(dto.getName());
        //setDeLaCartaListLabelAndButtons();
        setVisible(true);
    }

    //------------------------------------------------------------------------------
    private void createObjects() {
        mainPanel = new JPanel();
        ingresarCodigoTextField = new JTextField();
        notaComandaTextField = new JTextField();
    }

    private void createPanels() {
        createMainPanel();
        containerPanel.add(mainPanel);
    }

    private void createMainPanel() {
        int x = 20, y = 10, w = 450, h = 50, dx = 200, dw = 20, dy = 50;

        labelList = new ArrayList<>();
        buttonList = new ArrayList<>();

        JLabel ingresarCodigoTextLabel = new JLabel("Ingrese Producto Por Codigo:");

        ingresarCodigoTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ingresarCodigoTextField.getText().isEmpty()) {
                    dto.setNotes(notaComandaTextField.getText());
                    exit_hide();
                }
                /*else {
                    System.out.println("AgregarProductoADeLaCarta.ingresarCodigoTextField.actionPerformed: " + ingresarCodigoTextField.getText());
                    Inventory aux = GeneralController.getProduct(Integer.parseInt(ingresarCodigoTextField.getText()));
                    System.out.println("AgregarProductoADeLaCarta.ingresarCodigoTextField.actionPerformed: " + aux);
                    if (aux.getInventoryType() == Inventory.InventoryType.Mixture) {
                        ingresarCodigoTextField.setText("");//Inventory.InventoryType
                        aux = new Mixture((Mixture) aux);
                        aux.setQuantity(1);
                        dto.addToMixtureList((Mixture) aux);
                    }
                    if (aux.getInventoryType().equals(Inventory.InventoryType.Product)) {
                        ingresarCodigoTextField.setText("");
                        aux = new Product((Product) aux);
                        aux.setQuantity(1);
                        dto.addToProductList((Product) aux);
                    }
                    System.out.println("AgregarProductoADeLaCarta.ingresarCodigoTextField.actionPerformed: " + dto);
                    setDeLaCartaListLabelAndButtons();
                    mainPanel.repaint();
                }*/
            }
        });

        ingresarCodigoTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dto.setNotes(notaComandaTextField.getText());
                exit_hide();
            }
        });

        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                ingresarCodigoTextField.requestFocus();
            }
        });

        setDeLaCartaListLabelAndButtons();

        ingresarCodigoTextLabel.setBounds(x, y, w, h / 2);
        ingresarCodigoTextField.setBounds(x, y + dy / 2, w, h / 2);
        x += (w + dw);

        mainPanel.add(ingresarCodigoTextLabel);
        mainPanel.add(ingresarCodigoTextField);
        mainPanel.setBounds(Panel_Rectangle);
        mainPanel.setLayout(null);
    }

    private void setDeLaCartaListLabelAndButtons() {
        int x = 60, y = 100, w = 350, h = 50, dx = 100, w2 = 50, dy = 50;
        labelList.forEach((label) -> {
            mainPanel.remove(label);
        });
        buttonList.forEach((button) -> {
            mainPanel.remove(button);
        });
        mainPanel.repaint();
        InventoryItem inventoryItem = GeneralController.getInventoryItem(dto.getSerial());
        if (!inventoryItem.getComposition().isEmpty()) {
            for (InventoryItem_Ingredients inventoryItem_ingredients : inventoryItem.getComposition()) {

                InventoryItem compositionDTO = inventoryItem_ingredients.getInventoryItem();
                JButton       button         = new JButton("X");
                button.addActionListener(new buttonEliminarActionListener(dto, compositionDTO));
                button.setBounds(x, y, w2, h);
                buttonList.add(button);
                mainPanel.add(button);

                JLabel label = new JLabel("<" + compositionDTO.getId() + "> " + compositionDTO.getName());
                label.setBounds(x + w2, y, w, h);
                labelList.add(label);
                mainPanel.add(label);

                y += dy;

            }
        }
        JLabel notaComandaLabel = new JLabel("Nota de Comanda");
        labelList.add(notaComandaLabel);
        notaComandaLabel.setBounds(x, y, w, h);
        y += dy * 2 / 3;
        notaComandaTextField.setBounds(x, y, w, h);
        mainPanel.add(notaComandaLabel);
        mainPanel.add(notaComandaTextField);
        mainPanel.repaint();
    }

    private class buttonEliminarActionListener implements ActionListener {

        SalableItem   dto;
        InventoryItem composition;

        public buttonEliminarActionListener(SalableItem dto, InventoryItem composition) {
            this.dto = dto;
            this.composition = composition;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            // TODO: Eliminar el InventoryItem (el agregado como un vaso de jugo de naranja) del SalableItem
            /*
            if (composition.getInventoryType() == Inventory.InventoryType.Mixture)
                dto.deleteFromMixtureList((Mixture) composition);
            if (composition.getInventoryType() == Inventory.InventoryType.Product)
                dto.deleteFromProductList((Product) composition);
            */
            setDeLaCartaListLabelAndButtons();
        }
    }
}
