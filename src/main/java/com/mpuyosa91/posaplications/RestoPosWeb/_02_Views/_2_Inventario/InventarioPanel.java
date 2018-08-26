/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpuyosa91.posaplications.RestoPosWeb._02_Views._2_Inventario;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.MainFrame;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.Templates.IFrame;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views._2_Inventario.Ingresar.IngresarFrame;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views._2_Inventario.Listar.ListFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author MoisesE
 */
public class InventarioPanel extends javax.swing.JPanel {

    private final MainFrame                 contextFrame;
    private       ArrayList<JButton>        btnList;
    private       ArrayList<IFrame>         frameList;
    private       ArrayList<ActionListener> listenList;

    public InventarioPanel(MainFrame mainFrame) {
        super();
        this.contextFrame = mainFrame;
        initComponents();
        moveComponents();
        createListenerList();
    }

    private void initComponents() {
        int i;
        i = 0;
        btnList = new ArrayList<>();
        btnList.add(new JButton("Ingredientes"));
        this.add(btnList.get(i));
        i++;
        btnList.add(new JButton("SubProductos"));
        this.add(btnList.get(i));
        i++;
        btnList.add(new JButton("Productos"));
        this.add(btnList.get(i));
        i++;
        btnList.add(new JButton("De La Carta"));
        this.add(btnList.get(i));
        i++;
        btnList.add(new JButton("Lista Ingredientes"));
        this.add(btnList.get(i));
        i++;
        btnList.add(new JButton("Lista Subproductos"));
        this.add(btnList.get(i));
        i++;
        btnList.add(new JButton("Lista Productos"));
        this.add(btnList.get(i));
        i++;
        btnList.add(new JButton("Lista MenuPlate"));
        this.add(btnList.get(i));
        i++;
        btnList.add(new JButton("Imprimir Ingredientes"));
        this.add(btnList.get(i));
        i++;
        btnList.add(new JButton("Imprimir SubProductos"));
        this.add(btnList.get(i));
        i++;
        btnList.add(new JButton("Imprimir Productos"));
        this.add(btnList.get(i));
        i++;
        btnList.add(new JButton("Imprimir MenuPlate"));
        this.add(btnList.get(i));

        i = 0;
        frameList = new ArrayList<>();
        frameList.add(new IngresarFrame(contextFrame, InventoryItem.Type.RawFood));
        i++;
        frameList.add(new IngresarFrame(contextFrame, InventoryItem.Type.Mixture));
        i++;
        frameList.add(new IngresarFrame(contextFrame, InventoryItem.Type.Product));
        i++;
        frameList.add(new IngresarFrame(contextFrame, InventoryItem.Type.MenuPlate));
        i++;
        frameList.add(new ListFrame(contextFrame, InventoryItem.Type.RawFood));
        i++;
        frameList.add(new ListFrame(contextFrame, InventoryItem.Type.Mixture));
        i++;
        frameList.add(new ListFrame(contextFrame, InventoryItem.Type.Product));
        i++;
        frameList.add(new ListFrame(contextFrame, InventoryItem.Type.MenuPlate));
        i++;
        btnList.get(i).setEnabled(false);
        i++;
        btnList.get(i).addActionListener((ActionEvent e) -> {
            //InventoryController.toThermalPrinter(GeneralController.MIXTURE);
        });
        i++;
        btnList.get(i).addActionListener((ActionEvent e) -> {
            //InventoryController.toThermalPrinter(GeneralController.PRODUCT);
        });
        i++;
        btnList.get(i).addActionListener((ActionEvent e) -> {
            //InventoryController.toThermalPrinter(GeneralController.MENUPLATE);
        });

    }

    private void moveComponents() {
        int initialX = 10, initialY = 10;
        int stepX    = 190, stepY = 190;
        for (int i = 0; i < btnList.size(); i++) {
            btnList.get(i).setBounds(stepX * (i % 4) + initialX, stepY * (i / 4) + initialY, 150, 150);
        }
    }

    private void createListenerList() {
        listenList = new ArrayList<>();
        btnActionListener aux;
        for (int i = 0; i < frameList.size(); i++) {
            aux = new btnActionListener(frameList.get(i));
            listenList.add(aux);
            btnList.get(i).addActionListener(listenList.get(i));
        }
    }

    public class btnActionListener implements ActionListener {

        private final IFrame inventarioFrame;

        public btnActionListener(IFrame Frame) {
            this.inventarioFrame = Frame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            inventarioFrame.startframe();
        }
    }
}
