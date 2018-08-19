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
public class MiniWindowDialog extends JDialog implements IWindowDialog {

    public final float              xFrameContextRatio    = (float) (5.0 / 10.0);
    public final float              yFrameContextRatio    = (float) (8.0 / 10.0);
    public final float              xPanel1SizeRatio      = (float) (20.0 / 20.0);
    public final int                navigationPanel_ySize = 60;
    public final int                btnExit_xySize        = 50;
    public final int                xSize;
    public final int                ySize;
    public final int                xPosition;
    public final int                yPosition;
    public final Rectangle          Panel_Rectangle;
    public final MainFrame          contextFrame;
    public       boolean            showMesgInConsole     = false;
    protected    ArrayList<JButton> btnList;
    protected    ArrayList<JPanel>  panelList;
    protected    JLabel             informationLabel;
    protected    JPanel             navigationPanel;
    protected    Container          containerPanel;
    protected    JButton            btnExit;

    public MiniWindowDialog(MainFrame contextFrame, javax.swing.JFrame frame, boolean modal) {
        super(frame, modal);
        this.contextFrame = contextFrame;
        this.xSize = round(contextFrame.xSize * xFrameContextRatio);
        this.ySize = round(contextFrame.ySize * yFrameContextRatio);
        this.xPosition = (int) round(contextFrame.xSize * (1 - xFrameContextRatio) / 2.0);
        this.yPosition = (int) round(contextFrame.ySize * (1 - yFrameContextRatio) / 2.0);
        this.Panel_Rectangle = new Rectangle(0,
                                             navigationPanel_ySize,
                                             (int) (xSize * xPanel1SizeRatio),
                                             ySize - navigationPanel_ySize);
        configureFrame();
        configureContainerPanels();
    }

    @Override
    public void startDialog() {
        setVisible(true);
    }

    public void exit_hide() {
        setVisible(false);
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

    private void configureContainerPanels() {
        containerPanel = new JPanel();
        this.add(containerPanel);
        containerPanel.setLayout(null);
        createNavigationPanel();
        containerPanel.add(navigationPanel);
    }

}
