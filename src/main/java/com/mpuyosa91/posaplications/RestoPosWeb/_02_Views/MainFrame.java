/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpuyosa91.posaplications.RestoPosWeb._02_Views;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.SettingsAndProperties.LocalSettings;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views._1_Pedidos.PedidosPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

/**
 * @author MoisesE
 */
public class MainFrame extends JFrame {

    public final int          xSize                = 1024;
    public final int          ySize                = 768;
    public final int          horizontalSeparation = 750;
    private      JButton      btnPedidos;
    private      JButton      btnInventario;
    private      JButton      btnPersonal;
    private      JButton      btnVentas;
    private      JButton      btnEstadisticas;
    private      JButton      btnConfiguracion;
    private      JPanel       mainPanel;
    private      PedidosPanel panelPedidos;
//    private      InventarioPanel    panelInventario;
//    private      VentasPanel        panelVentas;
//    private      PersonalPanel      panelPersonal;
    //private EstadisticasPanel panelEstadisticas;
//    private      ConfiguracionPanel panelConfiguracion;

    public MainFrame() {
        super();
        LocalSettings localSettings = new LocalSettings();
        this.setTitle(localSettings.getProperty("site_tradeName"));
        configureFrame();
        initPanels();
        initButtons();
    }

    private void configureFrame() {
        this.setBounds(0, 0, this.xSize, this.ySize);
        this.setResizable(false);
        this.setUndecorated(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Color color = UIManager.getColor("activeCaptionBorder");
        this.getRootPane().setBorder(BorderFactory.createLineBorder(color, 4));
        this.setVisible(true);
    }

    private void initPanels() {
        mainPanel = new JPanel();
        mainPanel.setLayout(null);
        panelPedidos = new PedidosPanel(this);
        panelPedidos.setLayout(null);
//        panelInventario = new InventarioPanel(this);
//        panelInventario.setLayout(null);
//        panelPersonal = new PersonalPanel(this);
//        panelPersonal.setLayout(null);
//        panelVentas = new VentasPanel();
//        panelVentas.setLayout(null);
        //panelEstadisticas   = new EstadisticasPanel(this)       panelEstadisticas.setLayout(null);
//        panelConfiguracion = new ConfiguracionPanel();
//        panelConfiguracion.setLayout(null);

        this.add(mainPanel);
        this.add(panelPedidos);
//        this.add(panelInventario);
//        this.add(panelPersonal);
//        this.add(panelVentas);
        //this.add(panelEstadisticas);
        //this.add(panelConfiguracion);

        panelPedidos.setBounds(0, 0, this.horizontalSeparation, this.ySize);
/*        panelInventario.setBounds(0, 0, this.horizontalSeparation, this.ySize);
        panelPersonal.setBounds(0, 0, this.horizontalSeparation, this.ySize);
        panelVentas.setBounds(0, 0, this.horizontalSeparation, this.ySize);*/
        //panelEstadisticas.setBounds (   0                           ,   0   ,  this.horizontalSeparation  ,   this.ySize   );
        //panelConfiguracion.setBounds(0, 0, this.horizontalSeparation, this.ySize);
        mainPanel.setBounds(this.horizontalSeparation, 0, this.horizontalSeparation, this.ySize);
    }

    private void setView(int i) {
        panelPedidos.setVisible(false);
        //panelInventario.setVisible(false);
        //panelPersonal.setVisible(false);
        //panelVentas.setVisible(false);
        //panelEstadisticas.setVisible(false);
        //panelConfiguracion.setVisible(false);
        switch (i) {
            case 1:
                panelPedidos.show_hide(true);
                break;
            case 2:
                //panelInventario.setVisible(true);
                break;
            case 3:
                //panelPersonal.setVisible(true);
                break;
            case 4:
                //panelVentas.setVisible(true);
                break;
            case 5:
                //panelEstadisticas.setVisible(true);
                break;
            case 6:
                //panelConfiguracion.setVisible(true);
                break;
        }
    }

    private void initButtons() {
        btnPedidos = new JButton("Pedidos");
        btnInventario = new JButton("Inventario");
        btnPersonal = new JButton("Personal");
        btnVentas = new JButton("Ventas");
        btnEstadisticas = new JButton("Estadisticas");
        btnConfiguracion = new JButton("Configuracion");
        mainPanel.add(btnPedidos);
        mainPanel.add(btnInventario);
        mainPanel.add(btnPersonal);
        mainPanel.add(btnVentas);
        mainPanel.add(btnEstadisticas);
        mainPanel.add(btnConfiguracion);
        int spaceY   = (ySize / 6);
        int height   = (int) (spaceY * 0.8), width = 250;
        int initialX = 10, initialY = (int) (spaceY * 0.1);
        btnPedidos.setBounds(initialX, initialY + (0 * spaceY), width, height);
        btnInventario.setBounds(initialX, initialY + (1 * spaceY), width, height);
        btnPersonal.setBounds(initialX, initialY + (2 * spaceY), width, height);
        btnVentas.setBounds(initialX, initialY + (3 * spaceY), width, height);
        btnEstadisticas.setBounds(initialX, initialY + (4 * spaceY), width, height);
        btnConfiguracion.setBounds(initialX, initialY + (5 * spaceY), width, height);
        btnPedidos.setEnabled(true);
        btnInventario.setEnabled(true);
        btnPersonal.setEnabled(true);
        btnVentas.setEnabled(true);
        btnEstadisticas.setEnabled(false);
        btnConfiguracion.setEnabled(true);
        btnPedidos.addActionListener((ActionEvent e) -> {
            setView(1);
        });
        btnInventario.addActionListener((ActionEvent e) -> {
            setView(2);
        });
        btnPersonal.addActionListener((ActionEvent e) -> {
            setView(3);
        });
        btnVentas.addActionListener((ActionEvent e) -> {
            setView(4);
        });
        btnEstadisticas.addActionListener((ActionEvent e) -> {
            setView(5);
        });
        btnConfiguracion.addActionListener((ActionEvent e) -> {
            setView(6);
        });
    }
}