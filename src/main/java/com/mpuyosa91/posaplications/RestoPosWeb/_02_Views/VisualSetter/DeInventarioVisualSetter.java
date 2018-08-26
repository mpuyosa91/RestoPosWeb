/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpuyosa91.posaplications.RestoPosWeb._02_Views.VisualSetter;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem_Ingredients;
import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.MainFrame;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.Templates.WindowDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * @author MoisesE
 */
public class DeInventarioVisualSetter extends WindowDialog {

    //------------------------------------------------------------------------------
    private final InventoryItem.Type        clase;
    //------------------------------------------------------------
    private       InventoryItem             dto;
    private       JScrollPane               listItemScroll;
    //------------------------------------------------------------
    private       JPanel                    listItemPanel;
    //------------------------------------------------------------
    private       ArrayList<ActionListener> listenerList;
    private       ArrayList<JButton>        buttonList;
    private       InventoryItem             ingredienteActual;
    private       InventoryItem             subProductoActual;
    private       InventoryItem             productoActual;
    private       InventoryItem             deLaCartaActual;
    private       ArrayList<InventoryItem>  ingredienteList;
    private       ArrayList<InventoryItem>  subProductoList;
    private       ArrayList<InventoryItem>  productoList;
    private       ArrayList<InventoryItem>  deLaCartaList;

    public DeInventarioVisualSetter(MainFrame contextFrame,
                                    JFrame frame,
                                    boolean modal,
                                    InventoryItem.Type clase) {
        super(contextFrame, frame, modal);
        this.clase = clase;
        createObjects();
        createPanels();
    }

    public <G extends InventoryItem> void startframe(G dto) {
        System.out.println("DeInventarioVisualSetter.startFrame: \n" + dto);
        consoleFlush();
        this.dto = dto;
        informationLabel.setText("Agrega un " + clase);
        createListItemPanel();
        setVisiblePanel(listItemScroll);
        setVisible(true);
    }

    @Override
    public void exit_hide() {
        setVisible(false);
        consoleFlush();
    }

    @Override
    protected void setVisiblePanel(Component panel) {
        listItemScroll.setVisible(false);
        panel.setVisible(true);
    }

    //------------------------------------------------------------------------------
    private void createObjects() {
        listItemPanel = new JPanel();
        listenerList = new ArrayList<>();
        buttonList = new ArrayList<>();
        listItemScroll = new JScrollPane();
    }

    private void createPanels() {
        createListItemPanel();
    }

    private void createListItemPanel() {
        listItemPanel = new JPanel();
        listItemPanel.setBounds(LeftPanel_Rectangle);
        listItemPanel.setLayout(null);
        informationLabel.setText("Lista " + clase);
        setButtonsListItemPanel(GeneralController.getRootInventoryItem(clase));
        listItemScroll.setBounds(LeftPanel_Rectangle);
        listItemScroll.setViewportView(listItemPanel);
        listItemScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listItemScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        containerPanel.add(listItemScroll);
    }

    private <G extends InventoryItem> void setButtonsListItemPanel(G tree) {
        ArrayList<InventoryItem> list = new ArrayList<>();
        listItemPanel.removeAll();
        switch (clase) {
            case RawFood:
                ingredienteActual = tree;
                ingredienteList = GeneralController.getChildes(ingredienteActual);
                list.addAll(GeneralController.getChildes(ingredienteActual));
                break;
            case Mixture:
                subProductoActual = tree;
                subProductoList = GeneralController.getChildes(subProductoActual);
                list.addAll(GeneralController.getChildes(subProductoActual));
                break;
            case Product:
                productoActual = tree;
                productoList = GeneralController.getChildes(productoActual);
                list.addAll(GeneralController.getChildes(productoActual));
                break;
            case MenuPlate:
                deLaCartaActual = tree;
                deLaCartaList = GeneralController.getChildes(deLaCartaActual);
                list.addAll(GeneralController.getChildes(deLaCartaActual));
                break;
        }

        listenerList.clear();
        buttonList.clear();

        for (int i = 0; i < list.size() + 1; i++) {
            if (i == 0)
                createButton(listItemPanel, buttonList, "Nuevo <" + clase + ">", 0);
            else
                createButton(listItemPanel, buttonList, list.get(i - 1).getName(), i);
            listenerList.add(new DeInventarioActionListener(i));
            buttonList.get(i).addActionListener(listenerList.get(i));
        }
        buttonList.get(0).setEnabled(false);

        if (!list.isEmpty()) {
            this.consoleFlush();
            this.consoleAppend("Encontrados " + list.size() + " " + clase + "(s)");
        } else {
            this.consoleAppend("Lista <" + clase + "> Vacia");
        }
        listItemPanel.repaint();
    }

    private class DeInventarioActionListener implements ActionListener {

        private final int i;

        public DeInventarioActionListener(int i) {
            this.i = i;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            switch (clase) {
                case RawFood:
                    addComponentToInventoryItem(new ArrayList<>(ingredienteList));
                    break;
                case Mixture:
                    addComponentToInventoryItem(new ArrayList<>(subProductoList));
                    break;
                case Product:
                    addComponentToInventoryItem(new ArrayList<>(productoList));
                    break;
                case MenuPlate:
                    addComponentToInventoryItem(new ArrayList<>(deLaCartaList));
                    break;
            }
        }

        private void addComponentToInventoryItem(ArrayList<InventoryItem> deLaCartaList) {
            if (deLaCartaList.get(i - 1).isFinal_item()) {
                InventoryItem dto = GeneralController.getInventoryItem(deLaCartaList.get(i - 1).getSerial());
                dto.getComposition().add(new InventoryItem_Ingredients(dto, dto));
                exit_hide();
            } else setButtonsListItemPanel(deLaCartaList.get(i - 1));
        }
    }

}
