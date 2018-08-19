/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpuyosa91.posaplications.RestoPosWeb._02_Views._1_Pedidos;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.Customer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.ICustomer;
import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.MainFrame;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views._1_Pedidos.Mesas.PedidosFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.HashMap;
import java.util.Map;

/**
 * @author MoisesE
 */
public class PedidosPanel extends javax.swing.JPanel {

    private final PedidosPanel thisPanel = this;

    private final MainFrame                   mainFrame;
    private       Map<String, JButton>        buttonsCustomersMap;
    private       Map<String, PedidosFrame>   frameCustomersMap;
    private       Map<String, ActionListener> listenersButtonsMap;

    public PedidosPanel(MainFrame mainFrame) {
        super();
        this.mainFrame = mainFrame;
        initComponents();
        moveComponents();
        createListenerList();
    }

    public void show_hide(boolean option_show) {
        if (option_show) {
            initComponents();
            moveComponents();
            createListenerList();
        }
        this.setVisible(option_show);
    }

    public void setButtonsColors() {
        for (Customer customer : GeneralController.getCustomerList()) {
            JButton jButton = buttonsCustomersMap.get(customer.getId().toString());
            if (customer.getType() == ICustomer.CustomerTypes.Table) {
                if (customer.isOccupied()) {
                    jButton.setBackground(Color.lightGray);
                    jButton.setText("<html><center>" + customer.getIdentifier() + "<br>Ocupado" + "</center></html>");
                } else {
                    jButton.setBackground(new Color(0x5CE25C));
                    jButton.setText("<html><center>" + customer.getIdentifier() + "<br>Libre" + "</center></html>");
                }
            } else {
                jButton.setBackground(Color.lightGray);
            }

        }
    }

    private void initComponents() {

        if (buttonsCustomersMap != null) {
            // TODO: Solo borrar si son diferentes.
            for (Map.Entry<String, JButton> entry : buttonsCustomersMap.entrySet()) {
                this.remove(entry.getValue());
            }
        }

        buttonsCustomersMap = new HashMap<>();
        for (Customer customer : GeneralController.getCustomerList()) {
            JButton jButton = new JButton(customer.getIdentifier());
            buttonsCustomersMap.put(customer.getId().toString(), jButton);
            this.add(jButton);
        }
        frameCustomersMap = new HashMap<>();
        for (Customer customer : GeneralController.getCustomerList()) {
            PedidosFrame pedidosFrame = new PedidosFrame(mainFrame, customer);
            frameCustomersMap.put(customer.getId().toString(), pedidosFrame);
        }
        setButtonsColors();

        this.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                setButtonsColors();
            }

            @Override
            public void focusLost(FocusEvent e) {
            }
        });
    }

    private void moveComponents() {

        double size_step = 0.80;

        int initialX, initialY;
        int stepX, stepY;
        int width, height;
        int maxCols = -1, maxRows = -1;

        int panelWidth  = mainFrame.horizontalSeparation;
        int panelHeight = mainFrame.ySize;

        for (Customer customer : GeneralController.getCustomerList()) {
            if (customer.getPosition_col() > maxCols) maxCols = customer.getPosition_col();
            if (customer.getPosition_row() > maxRows) maxRows = customer.getPosition_row();
        }

        stepX = (panelWidth) / (maxCols + 1);
        stepY = (panelHeight) / (maxRows + 1);
        width = (int) (stepX * size_step);
        height = (int) (stepY * size_step);
        initialX = (stepX - width);
        initialY = (stepY - height);

        stepX = (panelWidth - initialX) / (maxCols + 1);
        stepY = (panelHeight - initialY) / (maxRows + 1);
        width = (int) (stepX * size_step);
        height = (int) (stepY * size_step);

        for (Customer customer : GeneralController.getCustomerList()) {
            JButton jButton = buttonsCustomersMap.get(customer.getId().toString());
            int     x       = stepX * customer.getPosition_col() + initialX;
            int     y       = stepY * customer.getPosition_row() + initialY;
            jButton.setBounds(x, y, width, height);
        }

    }

    private void createListenerList() {

        listenersButtonsMap = new HashMap<>();

        for (Customer customer : GeneralController.getCustomerList()) {
            String             customer_id        = customer.getId().toString();
            PedidosFrame       pedidosFrame       = frameCustomersMap.get(customer_id);
            PedidosFrameButton pedidosFrameButton = new PedidosFrameButton(pedidosFrame);
            listenersButtonsMap.put(customer_id, pedidosFrameButton);
            buttonsCustomersMap.get(customer_id).addActionListener(pedidosFrameButton);
        }

    }

    public class PedidosFrameButton implements ActionListener {

        private final PedidosFrame pedidosFrame;

        public PedidosFrameButton(PedidosFrame pedidosFrame) {
            this.pedidosFrame = pedidosFrame;
        }

        @Override
        public void actionPerformed(ActionEvent e) {

            JPanel     panel     = new JPanel();
            JLabel     userLabel = new JLabel("Usuario: ");
            JTextField userField = new JTextField(10);

            userField.setMinimumSize(new Dimension(100, 20));

            panel.add(userLabel);
            panel.add(userField);
            String[] options = new String[]{"Ingresar", "Cancelar"};
            int option = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    "Usuario que Atiende",
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);

            if (option == 0) {
                if (!userField.getText().isEmpty()) {
                    User user = GeneralController.getUser(userField.getText());
                    if (user != null) {
                        pedidosFrame.startframe(user, thisPanel);
                    }
                }
            }

        }
    }

}
