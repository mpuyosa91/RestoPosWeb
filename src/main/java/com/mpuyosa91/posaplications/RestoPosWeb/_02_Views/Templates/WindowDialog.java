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
public class WindowDialog extends JDialog implements IWindowDialog {

    public final float              xFrameContextRatio    = (float) (10.0 / 10.0);
    public final float              yFrameContextRatio    = (float) (10.0 / 10.0);
    public final float              xPanel1SizeRatio      = (float) (11.0 / 20.0);
    public final float              xPanel2SizeRatio;
    public final int                navigationPanel_ySize = 60;
    public final int                btnExit_xySize        = 50;
    public final int                xSize;
    public final int                ySize;
    public final int                xPosition;
    public final int                yPosition;
    public final Rectangle          LeftPanel_Rectangle;
    public final MainFrame          contextFrame;
    public       boolean            showMesgInConsole     = false;
    protected    ArrayList<JButton> btnList;
    protected    ArrayList<JPanel>  panelList;
    protected    JLabel             informationLabel;
    protected    JPanel             navigationPanel;
    protected    Container          containerPanel;
    private      JPanel             consolePanel;
    private      JTextArea          consoleTextArea;
    private      JButton            btnExit;

    public WindowDialog(MainFrame contextFrame, javax.swing.JFrame frame, boolean modal) {
        super(frame, modal);
        this.contextFrame = contextFrame;
        this.xSize = round(contextFrame.xSize * xFrameContextRatio);
        this.ySize = round(contextFrame.ySize * yFrameContextRatio);
        this.xPosition = (int) round(contextFrame.xSize * (1 - xFrameContextRatio) / 2.0);
        this.yPosition = (int) round(contextFrame.ySize * (1 - yFrameContextRatio) / 2.0);
        this.xPanel2SizeRatio = (float) (1.0 - xPanel1SizeRatio);
        this.LeftPanel_Rectangle = new Rectangle(
                0,
                navigationPanel_ySize,
                (int) (xSize * xPanel1SizeRatio),
                ySize - navigationPanel_ySize
        );
        configureFrame();
        configureContainerPanels();
    }

    @Override
    public void startDialog() {
        setVisible(true);
        consoleFlush();
    }

    public void exit_hide() {
        setVisible(false);
        consoleFlush();
    }

    protected void consoleAppend(String text) {
        consoleTextArea.append(text + "\n");
    }

    protected void consoleFlush() {
        consoleTextArea.setText("");
    }

    protected void setVisiblePanel(Component panel) {
        panel.setVisible(true);
    }

    protected void createButton(JPanel panel, ArrayList list, String label, int i) {
        final int initialX  = 30, initialY = 30;
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

    protected void reSetContainerPanel() {
        containerPanel.removeAll();
        configureContainerPanels();
    }

    private void configureFrame() {
        this.setBounds(xPosition, yPosition, xSize, ySize);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
        Color color = UIManager.getColor("activeCaptionBorder");
        this.getRootPane().setBorder(BorderFactory.createLineBorder(color, 4));
    }

    private void createNavigationPanel() {
        navigationPanel = new JPanel();
        navigationPanel.setBounds(0, 0, xSize, navigationPanel_ySize);
        navigationPanel.setLayout(null);
        btnExit = new JButton("X");
        navigationPanel.add(btnExit);
        btnExit.setBounds(xSize - navigationPanel_ySize, 0, btnExit_xySize, btnExit_xySize);
        btnExit.addActionListener((ActionEvent e) -> {
            exit_hide();
        });
        informationLabel = new JLabel();
        navigationPanel.add(informationLabel);
        informationLabel.setBounds(0, 0, xSize - btnExit_xySize, navigationPanel_ySize);
    }

    private void createConsolePanel() {
        Rectangle r;
        consolePanel = new JPanel();
        consoleTextArea = new JTextArea(40, 55);
        consolePanel.add(consoleTextArea);
        consoleTextArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        consoleTextArea.setEditable(false);
        consoleTextArea.setLineWrap(true);         //Ajusta el objeto al tamaño inicial, corta las palabras hasta la proxima linea
        consoleTextArea.setWrapStyleWord(true);    //Da enter a la palabra para no cortarla con el ajuste de linea
        r = new Rectangle((int) (xSize * xPanel1SizeRatio),
                          navigationPanel_ySize,
                          (int) (xSize * xPanel2SizeRatio),
                          ySize - navigationPanel_ySize);
        consolePanel.setBounds(r);
    }

    private void configureContainerPanels() {
        containerPanel = new JPanel();
        this.add(containerPanel);
        containerPanel.setLayout(null);
        createNavigationPanel();
        containerPanel.add(navigationPanel);
        createConsolePanel();
        containerPanel.add(consolePanel);
    }

}
