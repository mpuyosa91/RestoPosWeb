/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpuyosa91.posaplications.RestoPosWeb._02_Views._1_Pedidos;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.MainFrame;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.Templates.MiniWindowDialog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Set;

/**
 * @author MoisesE
 */
public class SeleccionFrame extends MiniWindowDialog {

    final boolean showMesgSys = false;

    final int itemByRow = 2;
    final int stepX     = (xSize / itemByRow), stepY = (stepX * 9 / 16);
    final int height = (int) (stepY * 0.8), width = (int) (stepX * 0.8);
    final int initialX = (int) (stepY * 0.1), initialY = (int) (stepY * 0.1);
    private final InventoryItem             clase;
    //------------------------------------------------------------------------------
    public        InventoryItem             externalDto = null;
    private       ArrayList<JButton>        productosButtonList;
    private       ArrayList<ActionListener> productosListenerList;
    private       JPanel                    seleccionProductoPanel;
    private       JScrollPane               seleccionProductoScroll;

    public SeleccionFrame(MainFrame contextFrame, JFrame frame, boolean modal, InventoryItem clase) {
        super(contextFrame, frame, modal);
        this.clase = clase;
        createObjects();
        createPanels();
    }

    @Override
    public void startDialog() {
        setVisible(true);
    }

    @Override
    protected void setVisiblePanel(Component panel) {
        seleccionProductoScroll.setVisible(false);
        panel.setVisible(true);
    }
    //------------------------------------------------------------

    //------------------------------------------------------------------------------
    private void createObjects() {
        productosButtonList = new ArrayList<>();
        productosListenerList = new ArrayList<>();
        seleccionProductoPanel = new JPanel();
        seleccionProductoScroll = new JScrollPane();
    }

    private void createPanels() {
        createSeleccionProductoPanel();
        containerPanel.add(seleccionProductoScroll);
    }

    private void createSeleccionProductoPanel() {
        seleccionProductoPanel.setLayout(null);
        createButtons(clase);
        seleccionProductoPanel.repaint();

        seleccionProductoScroll = new JScrollPane(
                seleccionProductoPanel,
                JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
                JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        seleccionProductoScroll.setBounds(Panel_Rectangle);
        seleccionProductoScroll.getVerticalScrollBar().setUnitIncrement(10);
        seleccionProductoScroll.repaint();
    }

    private void createButtons(InventoryItem dto) {
        productosButtonList.forEach((button) -> {
            seleccionProductoPanel.remove(button);
        });
        productosButtonList = new ArrayList<>();
        productosListenerList = new ArrayList<>();
        seleccionProductoPanel.repaint();

        Set<InventoryItem> dtoDown = GeneralController.getChilds(dto);

        int i = 0;
        for (InventoryItem item : dtoDown) {
            String buttonString = "<html><center>" + item.getName() + "<br>" +
                    "&lt;" + String.valueOf(item.getId()) + "&gt;</center></html>";
            productosButtonList.add(new JButton(buttonString));
            seleccionProductoPanel.add(productosButtonList.get(i));
            seleccionProductoPanel.setPreferredSize(new Dimension(
                    seleccionProductoPanel.getWidth(),
                    initialY + stepY * ((i + 1) / itemByRow)));
            productosButtonList.get(i).setBounds(
                    initialX + stepX * (i % itemByRow),
                    initialY + stepY * (i / itemByRow),
                    width,
                    height);
            SeleccionadorISellable auxListener = new SeleccionadorISellable(item);
            productosListenerList.add(auxListener);
            productosButtonList.get(i).addActionListener(productosListenerList.get(i));
            i++;
        }

        if (showMesgSys) System.out.println("Quantity: " + i);
        seleccionProductoPanel.setPreferredSize(new Dimension(
                seleccionProductoPanel.getWidth(),
                initialY + stepY * ((i + 1) / itemByRow)));
        seleccionProductoScroll.setViewportView(seleccionProductoPanel);
        seleccionProductoScroll.repaint();
    }

    private class SeleccionadorISellable implements ActionListener {

        private final InventoryItem producto;

        public SeleccionadorISellable(InventoryItem dto) {
            this.producto = dto;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (!producto.isFinal_item())
                createButtons(producto);
            else {
                externalDto = GeneralController.getInventoryItem(producto.getSerial());
                if (showMesgSys) {
                    System.out.println("..................................");
                    System.out.println(externalDto);
                }
                exit_hide();
            }
        }
    }
}
