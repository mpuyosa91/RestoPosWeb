/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpuyosa91.posaplications.RestoPosWeb._02_Views._4_Ventas;

import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * @author MoisesE
 */
public class VentasPanel extends javax.swing.JPanel {

    private ArrayList<JButton>        buttonList;
    private ArrayList<ActionListener> listenList;

    public VentasPanel() {
        super();
        initComponents();
        moveComponents();
        createListenerList();
    }

    private void initComponents() {
        int     i = 0;
        JButton btnAux;
        buttonList = new ArrayList<>();
        btnAux = new JButton("<html><center>Ver<br>Facturas Anteriores</center></html>");
        buttonList.add(btnAux);
        this.add(buttonList.get(i));
        i++;
        btnAux.setEnabled(false);
        btnAux = new JButton("<html><center>Ver<br>Facturas del Dia</center></html>");
        buttonList.add(btnAux);
        this.add(buttonList.get(i));
        i++;
        btnAux.setEnabled(false);
        btnAux = new JButton("<html><center>Corte De Turno <br> 5am ~ 11am </center></html>");
        buttonList.add(btnAux);
        this.add(buttonList.get(i));
        i++;
        btnAux.setEnabled(true);
        btnAux = new JButton("<html><center>Corte De Turno <br> 11am ~ 4pm </center></html>");
        buttonList.add(btnAux);
        this.add(buttonList.get(i));
        i++;
        btnAux.setEnabled(true);
        btnAux = new JButton("<html><center>Corte De Turno <br> 4pm ~ 9pm </center></html>");
        buttonList.add(btnAux);
        this.add(buttonList.get(i));
        i++;
        btnAux.setEnabled(true);
        btnAux = new JButton("<html><center>Generar <br>Corte Diario</center></html>");
        buttonList.add(btnAux);
        this.add(buttonList.get(i));
    }

    private void moveComponents() {
        int initialX = 10, initialY = 10;
        int stepX    = 190, stepY = 190;
        for (int i = 0; i < buttonList.size(); i++) {
            buttonList.get(i).setBounds(stepX * (i % 4) + initialX, stepY * (i / 4) + initialY, 150, 150);
        }
    }

    private void createListenerList() {
        int            i = 0;
        ActionListener actionListener;
        listenList = new ArrayList<>();
        actionListener = (ActionEvent e) -> {

        };
        listenList.add(actionListener);
        buttonList.get(i).addActionListener(actionListener);
        i++;
        actionListener = (ActionEvent e) -> {

        };
        listenList.add(actionListener);
        buttonList.get(i).addActionListener(actionListener);
        i++;
        actionListener = (ActionEvent e) -> {
            Calendar start_time_calendar = Calendar.getInstance();
            Calendar end_time_calendar   = Calendar.getInstance();

            start_time_calendar.set(Calendar.MILLISECOND, 0);
            end_time_calendar.set(Calendar.MILLISECOND, 0);
            start_time_calendar.set(Calendar.SECOND, 0);
            end_time_calendar.set(Calendar.SECOND, 0);
            start_time_calendar.set(Calendar.MINUTE, 0);
            end_time_calendar.set(Calendar.MINUTE, 0);
            start_time_calendar.set(Calendar.HOUR_OF_DAY, 5);
            end_time_calendar.set(Calendar.HOUR_OF_DAY, 11);

            GeneralController.printShift(start_time_calendar, end_time_calendar);
        };
        listenList.add(actionListener);
        buttonList.get(i).addActionListener(actionListener);
        i++;
        actionListener = (ActionEvent e) -> {
            Calendar start_time_calendar = Calendar.getInstance();
            Calendar end_time_calendar   = Calendar.getInstance();

            start_time_calendar.set(Calendar.MILLISECOND, 0);
            end_time_calendar.set(Calendar.MILLISECOND, 0);
            start_time_calendar.set(Calendar.SECOND, 0);
            end_time_calendar.set(Calendar.SECOND, 0);
            start_time_calendar.set(Calendar.MINUTE, 0);
            end_time_calendar.set(Calendar.MINUTE, 0);
            start_time_calendar.set(Calendar.HOUR_OF_DAY, 11);
            end_time_calendar.set(Calendar.HOUR_OF_DAY, 16);

            GeneralController.printShift(start_time_calendar, end_time_calendar);
        };
        listenList.add(actionListener);
        buttonList.get(i).addActionListener(actionListener);
        i++;
        actionListener = (ActionEvent e) -> {
            Calendar start_time_calendar = Calendar.getInstance();
            Calendar end_time_calendar   = Calendar.getInstance();

            start_time_calendar.set(Calendar.MILLISECOND, 0);
            end_time_calendar.set(Calendar.MILLISECOND, 0);
            start_time_calendar.set(Calendar.SECOND, 0);
            end_time_calendar.set(Calendar.SECOND, 0);
            start_time_calendar.set(Calendar.MINUTE, 0);
            end_time_calendar.set(Calendar.MINUTE, 0);
            start_time_calendar.set(Calendar.HOUR_OF_DAY, 16);
            end_time_calendar.set(Calendar.HOUR_OF_DAY, 22);

            GeneralController.printShift(start_time_calendar, end_time_calendar);
        };
        listenList.add(actionListener);
        buttonList.get(i).addActionListener(actionListener);
        i++;
        actionListener = (ActionEvent e) -> {
            Calendar start_time_calendar = Calendar.getInstance();
            Calendar end_time_calendar   = Calendar.getInstance();

            start_time_calendar.set(Calendar.MILLISECOND, 0);
            end_time_calendar.set(Calendar.MILLISECOND, 0);
            start_time_calendar.set(Calendar.SECOND, 0);
            end_time_calendar.set(Calendar.SECOND, 0);
            start_time_calendar.set(Calendar.MINUTE, 0);
            end_time_calendar.set(Calendar.MINUTE, 0);
            start_time_calendar.set(Calendar.HOUR_OF_DAY, 5);
            end_time_calendar.set(Calendar.HOUR_OF_DAY, 22);

            GeneralController.printShift(start_time_calendar, end_time_calendar);
        };
        listenList.add(actionListener);
        buttonList.get(i).addActionListener(actionListener);
    }
}
