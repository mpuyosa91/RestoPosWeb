/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpuyosa91.posaplications.RestoPosWeb._02_Views.Templates;

import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.MainFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.ArrayList;

import static java.lang.Math.round;

/**
 * @author MoisesE
 */
public class WindowFrame extends JFrame implements IFrame {

    public final float              xFrameContextRatio    = (float) (10.0 / 10.0);
    public final float              yFrameContextRatio    = (float) (10.0 / 10.0);
    public final float              xPanel1SizeRatio      = (float) (11.0 / 20.0);
    public final float              xPanel2SizeRatio      = (float) (1.0 - xPanel1SizeRatio);
    public final int                navigationPanel_ySize = 60;
    public final int                btnExit_xySize        = 50;
    public final int                xSize;
    public final int                ySize;
    public final int                xPosition;
    public final int                yPosition;
    public final Rectangle          LeftPanel_Rectangle;
    public final Rectangle          RightPanel_Rectangle;
    public final MainFrame          contextFrame;
    public       boolean            showMesgInConsole     = false;
    protected    ArrayList<JButton> btnList;
    protected    ArrayList<JPanel>  panelList;
    protected    JLabel             informationLabel;
    protected    JPanel             navigationPanel;
    protected    Container          containerPanel;
    private      JPanel             consolePanel;
    private      JScrollPane        consoleScrollPane;
    private      JTextArea          consoleTextArea;
    private      JButton            btnExit;

    public WindowFrame(MainFrame contextFrame) {
        super();
        int x, y, w, h;
        this.contextFrame = contextFrame;
        this.xSize = round(contextFrame.xSize * xFrameContextRatio);
        this.ySize = round(contextFrame.ySize * yFrameContextRatio);
        this.xPosition = (int) round(contextFrame.xSize * (1 - xFrameContextRatio) / 2.0);
        this.yPosition = (int) round(contextFrame.ySize * (1 - yFrameContextRatio) / 2.0);

        x = 0;
        y = navigationPanel_ySize;
        w = (int) (xSize * xPanel1SizeRatio);
        h = ySize - navigationPanel_ySize;
        this.LeftPanel_Rectangle = new Rectangle(x, y, w, h);

        x = (int) (xSize * xPanel1SizeRatio);
        y = navigationPanel_ySize;
        w = (int) (xSize * xPanel2SizeRatio);
        h = ySize - navigationPanel_ySize;
        this.RightPanel_Rectangle = new Rectangle(x, y, w, h);

        createObjects();
        configureFrame();
        configureContainerPanels();
    }

    @Override
    public void startframe() {
        setVisible(true);
        consoleFlush();
    }

    @Override
    public void exit_hide() {
        setVisible(false);
        consoleFlush();
    }

    protected void consoleAppend(String text) {
        consoleTextArea.append(text + "\n");
        consoleScrollPane.getVerticalScrollBar().setValue(consoleScrollPane.getVerticalScrollBar().getMinimum());
    }

    protected void consoleFlush() {
        consoleTextArea.setText("");
    }

    protected void setVisiblePanel(Component panel) {
        panel.setVisible(true);
    }

    protected void createButton(JPanel panel, ArrayList list, String label, int i) {
        final int initialX  = 30, initialY = 100;
        final int xElements = 2;
        final int stepX     = (panel.getWidth() - initialX - 2) / xElements, stepY = 100;

        JButton btnAux;

        btnAux = new JButton(label);
        list.add(btnAux);
        panel.add(btnAux);

        btnAux.setBounds(initialX + stepX * (i % xElements),
                         initialY + stepY * (i / xElements),
                         stepX - initialX,
                         stepY);
        panel.setPreferredSize(new Dimension(panel.getWidth(), 20 + initialY + stepY * ((i + 2) / xElements)));
    }

    private void createObjects() {
        navigationPanel = new JPanel();
        informationLabel = new JLabel();
        btnExit = new JButton("X");
        consolePanel = new JPanel();
        consoleScrollPane = new JScrollPane();
        consoleTextArea = new JTextArea(40, 60);
    }

    private void configureFrame() {
        this.setBounds(xPosition, yPosition, xSize, ySize);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color color = UIManager.getColor("activeCaptionBorder");
        this.getRootPane().setBorder(BorderFactory.createLineBorder(color, 4));
    }

    private void createNavigationPanel() {
        navigationPanel.setBounds(0, 0, xSize, navigationPanel_ySize);
        navigationPanel.setLayout(null);
        navigationPanel.add(btnExit);
        btnExit.setBounds(xSize - navigationPanel_ySize, 0, btnExit_xySize, btnExit_xySize);
        btnExit.addActionListener((ActionEvent e) -> {
            exit_hide();
        });
        navigationPanel.add(informationLabel);
        informationLabel.setBounds(0, 0, xSize - btnExit_xySize, navigationPanel_ySize);
    }

    private void createConsolePanel() {
        Rectangle r;

        consoleTextArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        consoleTextArea.setEditable(false);
        consoleTextArea.setLineWrap(true);         //Ajusta el objeto al tamaño inicial, corta las palabras hasta la proxima linea
        consoleTextArea.setWrapStyleWord(true);    //Da enter a la palabra para no cortarla con el ajuste de linea

        consolePanel.add(consoleTextArea);

        consoleScrollPane.setBounds(RightPanel_Rectangle);
        consoleScrollPane.setViewportView(consolePanel);
        consoleScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        consoleScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        containerPanel.add(consoleScrollPane);

    }

    private void configureContainerPanels() {
        containerPanel = new JPanel();
        this.add(containerPanel);
        containerPanel.setLayout(null);
        createNavigationPanel();
        containerPanel.add(navigationPanel);
        createConsolePanel();
        containerPanel.add(consoleScrollPane);
    }
}