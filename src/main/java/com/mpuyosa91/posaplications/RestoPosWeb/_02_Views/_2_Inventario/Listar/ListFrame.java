/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpuyosa91.posaplications.RestoPosWeb._02_Views._2_Inventario.Listar;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.MainFrame;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.Templates.IFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

import static java.lang.Math.round;

/**
 * @author MoisesE
 */
public class ListFrame extends JFrame implements IFrame {

    public final float              xFrameContextRatio    = (float) (10.0 / 10.0);
    public final float              yFrameContextRatio    = (float) (10.0 / 10.0);
    public final int                navigationPanel_ySize = 60;
    public final int                btnExit_xySize        = 50;
    public final int                xSize;
    public final int                ySize;
    public final int                xPosition;
    public final int                yPosition;
    public final Rectangle          LeftPanel_Rectangle;
    public final MainFrame          contextFrame;
    protected    JLabel             informationLabel;
    protected    JScrollPane        listScroll;
    protected    JPanel             navigationPanel;
    protected    JPanel             listPanel;
    protected    Container          containerPanel;
    protected    InventoryItem.Type identifier;
    private      JButton            btnExit;
    private      JTextArea          consoleTextArea;

    public ListFrame(MainFrame contextFrame, InventoryItem.Type identifier) {
        super();
        this.contextFrame = contextFrame;
        this.xSize = round(contextFrame.xSize * xFrameContextRatio);
        this.ySize = round(contextFrame.ySize * yFrameContextRatio);
        this.xPosition = (int) round(contextFrame.xSize * (1 - xFrameContextRatio) / 2.0);
        this.yPosition = (int) round(contextFrame.ySize * (1 - yFrameContextRatio) / 2.0);
        this.LeftPanel_Rectangle =
                new Rectangle(0,
                              navigationPanel_ySize,
                              (xSize - 8),
                              ySize - navigationPanel_ySize);
        this.identifier = identifier;
        configureFrame();
        configureContainerPanels();
    }

    @Override
    public void startframe() {
        consoleTextArea.setText("");
        String treeToString = GeneralController.treeToString(GeneralController.getRootInventoryItem(identifier));
        consoleTextArea.setText(treeToString);
        listScroll.getVerticalScrollBar().setValue(listScroll.getVerticalScrollBar().getMinimum());
        listScroll.getHorizontalScrollBar().setValue(listScroll.getHorizontalScrollBar().getMinimum());
        setVisible(true);
    }

    @Override
    public void exit_hide() {
        setVisible(false);
    }

    private void configureFrame() {
        this.setBounds(xPosition, yPosition, xSize, ySize);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color color = UIManager.getColor("activeCaptionBorder");
        this.getRootPane().setBorder(BorderFactory.createLineBorder(color, 4));
    }

    private void configureContainerPanels() {
        containerPanel = new JPanel();
        this.add(containerPanel);
        containerPanel.setLayout(null);
        createNavigationPanel();
        containerPanel.add(navigationPanel);
        createContentPanel();
        containerPanel.add(listScroll);
    }

    private void createNavigationPanel() {
        navigationPanel = new JPanel();
        navigationPanel.setBounds(0, 0, xSize, navigationPanel_ySize);
        navigationPanel.setLayout(null);
        btnExit = new JButton("X");
        navigationPanel.add(btnExit);
        btnExit.setBounds(xSize - navigationPanel_ySize, 0, btnExit_xySize, btnExit_xySize);
        btnExit.addActionListener((ActionEvent e) -> exit_hide());
        informationLabel = new JLabel();
        informationLabel.setText("Lista " + identifier.name());
        navigationPanel.add(informationLabel);
        informationLabel.setBounds(0, 0, xSize - btnExit_xySize, navigationPanel_ySize);
    }

    private void createContentPanel() {
        String aux = GeneralController.treeToString(GeneralController.getRootInventoryItem(identifier));
        consoleTextArea = new JTextArea(aux);
        consoleTextArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        consoleTextArea.setEditable(false);
        consoleTextArea.setLineWrap(true);         //Ajusta el objeto al tamaño inicial, corta las palabras hasta la proxima linea
        consoleTextArea.setWrapStyleWord(true);    //Da enter a la palabra para no cortarla con el ajuste de linea       
        consoleTextArea.setVisible(true);
        consoleTextArea.setBounds(new Rectangle(0, 0, (xSize - 14), ySize - navigationPanel_ySize - 14));
        Dimension d;
        if (consoleTextArea.getLineCount() < 43)
            d = new Dimension(consoleTextArea.getWidth(), ((43) * 16));
        else
            d = new Dimension(consoleTextArea.getWidth(), ((consoleTextArea.getLineCount() + 1) * 16));
        consoleTextArea.setBounds(0, 0, d.width, d.height);

        listPanel = new JPanel();
        listPanel.add(consoleTextArea);
        listPanel.setLayout(null);
        listPanel.setPreferredSize(d);
        listPanel.repaint();

        listScroll = new JScrollPane(listPanel,
                                     JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
                                     JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        listScroll.setBounds(new Rectangle(0,
                                           navigationPanel_ySize,
                                           (xSize - 8),
                                           ySize - navigationPanel_ySize - 8));
        listScroll.getVerticalScrollBar().setUnitIncrement(10);
        listScroll.repaint();
    }
}
