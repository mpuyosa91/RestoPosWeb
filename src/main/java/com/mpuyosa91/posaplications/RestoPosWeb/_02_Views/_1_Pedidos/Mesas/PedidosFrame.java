/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpuyosa91.posaplications.RestoPosWeb._02_Views._1_Pedidos.Mesas;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.ICustomer;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.SalableItem;
import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.MainFrame;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.Templates.WindowFrame;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views._1_Pedidos.PedidosPanel;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views._1_Pedidos.SeleccionFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.*;

/**
 * @author MoisesE
 */
public class PedidosFrame extends WindowFrame {

    final int initialX = 60, initialY = 60, stepX = 250, stepY = 150, cant = 2;
    private final ICustomer          cliente;
    private final PedidosFrame       thisFrame;
    private       PedidosPanel       father;
    private       JPanel             clientePanel;
    private       Bill               bill;
    private       User               user;
    //------------------------------------------------------------
    private       JTextField         ingresarCodigoTextField;
    private       JButton            cerrarClienteButton;
    private       JButton            notificarCocinaButton;
    private       JButton            seleccionProductoButton;
    private       JButton            seleccionDeLaCartaButton;
    //------------------------------------------------------------
    private       ArrayList<JLabel>  labelList;
    private       ArrayList<JButton> buttonList;
    private       JPanel             productosPanel;
    private       JScrollPane        productosScrollPane;

    public PedidosFrame(MainFrame contextFrame, ICustomer cliente) {
        super(contextFrame);
        this.cliente = cliente;
        this.thisFrame = this;
        createObjects();
        createPanels();
        statusRefresh();
    }

    public void startframe(User user, PedidosPanel father) {
        this.user = user;
        this.father = father;
        boolean valid = true;
        if (cliente.getIdentifier().contains(ICustomer.CustomerTypes.ExternalCustomer.getShowableName())) {
            JPanel         panel = new JPanel();
            JLabel         label = new JLabel("Contraseña Para Cliente Externo:");
            JPasswordField pass  = new JPasswordField(10);
            panel.add(label);
            panel.add(pass);
            String[] options = new String[]{"OK", "Cancel"};
            int option = JOptionPane.showOptionDialog(null,
                                                      panel,
                                                      "Autenticacion de Seguridad",
                                                      JOptionPane.NO_OPTION,
                                                      JOptionPane.PLAIN_MESSAGE,
                                                      null,
                                                      options,
                                                      options[0]);
            if (option == 0) {
                String clienteexternoautent = GeneralController.getConfigurationValue(GeneralController.Label.ClienteExternoAutent.toString());
                char[] password             = pass.getPassword();
                char[] autent               = ("" + Integer.parseInt(clienteexternoautent)).toCharArray();
                valid = Arrays.equals(password, autent);
            } else {
                valid = false;
            }
        }
        if (valid) {
            consoleFlush();
            printActual();
            setVisible(true);
        }
    }

    @Override
    public void exit_hide() {
        father.setButtonsColors();
        setVisible(false);
        consoleFlush();
    }

    @Override
    protected void setVisiblePanel(Component panel) {
        clientePanel.setVisible(false);
        panel.setVisible(true);
    }
//------------------------------------------------------------------------------

    private void createObjects() {
        clientePanel = new JPanel();
        notificarCocinaButton = new JButton("Enviar Nuevas Comandas");
        notificarCocinaButton.setEnabled(false);
        cerrarClienteButton = new JButton("Cerrar " + cliente.getIdentifier());
        cerrarClienteButton.setEnabled(false);
        ingresarCodigoTextField = new JTextField();
        seleccionProductoButton = new JButton("Productos");
        seleccionDeLaCartaButton = new JButton("De La Carta");
        productosPanel = new JPanel();
        productosScrollPane = new JScrollPane(productosPanel);
        productosScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        productosScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    }

    private void createPanels() {
        createClientePanel();
        containerPanel.add(clientePanel);
    }

    private void statusRefresh() {
        if (cliente.isOccupied()) {
            informationLabel.setText("Ocupada");
            navigationPanel.setBackground(Color.LIGHT_GRAY);
        } else {
            informationLabel.setText("Disponible");
            navigationPanel.setBackground(Color.green);
        }
    }

    private void createClientePanel() {
        int x = 20, y = 100, w = 350, h = 50, dx = 200, dw = 20, dy = 50;

        labelList = new ArrayList();
        buttonList = new ArrayList();

        JLabel ingresarCodigoTextLabel = new JLabel("Ingrese Producto Por Codigo:");
        ingresarCodigoTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (ingresarCodigoTextField.getText().isEmpty()) {
                    GeneralController.sendToKitchen(cliente);
                    setPedidoListLabelAndButtons();
                    ingresarCodigoTextField.setText("");
                } else {
                    InventoryItem aux =
                            GeneralController.getInventoryItem(Integer.parseInt(ingresarCodigoTextField.getText()));
                    if (aux != null) ingresarProducto(aux);
                }
            }
        });
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowOpened(WindowEvent e) {
                ingresarCodigoTextField.requestFocus();
            }
        });
        cerrarClienteButton.addActionListener((ActionEvent e) -> {
            boolean            valid               = false;
            JPanel             panel               = new JPanel(new GridBagLayout()); //SpringLayout
            GridBagConstraints gridBagConstraints  = new GridBagConstraints();
            JLabel             totalLabel          = new JLabel("Total:  ");
            JTextField         totalTextField      = new JTextField(10);
            JLabel             pagoLabel           = new JLabel("Pago:   ");
            JTextField         pagoTextField       = new JTextField(10);
            JLabel             devolutionLabel     = new JLabel("Devol:  ");
            JTextField         devolutionTextField = new JTextField(10);

            totalTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(cliente.getConsumption()));
            long a, b;
            try {
                a = (long) NumberFormat.getNumberInstance(Locale.GERMAN).parse(pagoTextField.getText());
            } catch (ParseException ex) {
                a = 0;
            }
            b = (int) cliente.getConsumption();
            totalTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(b));
            pagoTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(a));
            devolutionTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(a - b));

            gridBagConstraints.fill = GridBagConstraints.VERTICAL;
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 1;
            panel.add(totalLabel, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 1;
            panel.add(totalTextField, gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 2;
            panel.add(pagoLabel, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 2;
            panel.add(pagoTextField, gridBagConstraints);
            gridBagConstraints.gridx = 0;
            gridBagConstraints.gridy = 3;
            panel.add(devolutionLabel, gridBagConstraints);
            gridBagConstraints.gridx = 1;
            gridBagConstraints.gridy = 3;
            panel.add(devolutionTextField, gridBagConstraints);

            totalTextField.setEditable(false);
            devolutionTextField.setEditable(false);
            AbstractAction abstractAction = new AbstractAction() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    long a, b;
                    try {
                        a = (long) NumberFormat.getNumberInstance(Locale.GERMAN).parse(pagoTextField.getText());
                    } catch (ParseException ex) {
                        a = 0;
                    }
                    b = (int) cliente.getConsumption();
                    totalTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(b));
                    pagoTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(a));
                    devolutionTextField.setText(NumberFormat.getNumberInstance(Locale.GERMAN).format(a - b));
                }
            };
            for (int i = 48; i < 58; i++)
                pagoTextField.getInputMap().put(KeyStroke.getKeyStroke(i, 0, true), abstractAction);
            for (int i = 96; i < 106; i++)
                pagoTextField.getInputMap().put(KeyStroke.getKeyStroke(i, 0, true), abstractAction);
            pagoTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0, true), abstractAction);

            String[] options = new String[]{"Ok", "Factura", "Cancel"};
            int option = JOptionPane.showOptionDialog(null,
                                                      panel,
                                                      cliente.getIdentifier(),
                                                      JOptionPane.YES_NO_CANCEL_OPTION,
                                                      JOptionPane.PLAIN_MESSAGE,
                                                      null,
                                                      options,
                                                      options[0]);
            switch (option) {
                case 0:
                    bill = GeneralController.saveBill(cliente);
                    setPedidoListLabelAndButtons();
                    ingresarCodigoTextField.setText("");
                    cerrarClienteButton.setEnabled(false);
                    notificarCocinaButton.setEnabled(false);
                    clientePanel.setVisible(true);
                    consoleAppend("<<" + cliente.getIdentifier() + " Cerrada>> Consumo: " + bill.getConsumption());
                    break;
                case 1:
                    bill = GeneralController.printBill(cliente);
                    setPedidoListLabelAndButtons();
                    ingresarCodigoTextField.setText("");
                    cerrarClienteButton.setEnabled(false);
                    notificarCocinaButton.setEnabled(false);
                    clientePanel.setVisible(true);
                    consoleAppend("<<" + cliente.getIdentifier() + " Cerrada>> Consumo: " + bill.getConsumption());
                    break;
                default:
                    setPedidoListLabelAndButtons();
                    ingresarCodigoTextField.setText("");
                    break;
            }
        });
        notificarCocinaButton.addActionListener((ActionEvent e) -> {
            if (GeneralController.sendToKitchen(cliente)) {
                setPedidoListLabelAndButtons();
                ingresarCodigoTextField.setText("");
            }
        });
        seleccionProductoButton.addActionListener((ActionEvent e) -> {
            SeleccionFrame seleccionador = new SeleccionFrame(
                    contextFrame,
                    thisFrame,
                    true,
                    GeneralController.getMainProduct());
            seleccionador.startDialog();
            if (seleccionador.externalDto != null) {
                ingresarProducto(seleccionador.externalDto);
            }
        });
        seleccionDeLaCartaButton.addActionListener((ActionEvent e) -> {
            SeleccionFrame seleccionador = new SeleccionFrame(
                    contextFrame,
                    thisFrame,
                    true,
                    GeneralController.getMainMenuPlate());
            seleccionador.startDialog();
            if (seleccionador.externalDto != null) {
                ingresarProducto(seleccionador.externalDto);
            }
        });
        seleccionProductoButton.setEnabled(true);
        seleccionDeLaCartaButton.setEnabled(true);
        cerrarClienteButton.setEnabled(false);
        notificarCocinaButton.setEnabled(false);

        ingresarCodigoTextLabel.setBounds(x, y, w, h / 2);
        ingresarCodigoTextField.setBounds(x, y + dy / 2, w, h / 2);
        x += (w + dw);
        notificarCocinaButton.setBounds(x, y, w / 2, h);
        x = 20;
        y -= dy;
        cerrarClienteButton.setBounds(x, y, w / 2, h);
        x += w * 2 / 4 + 10;
        seleccionProductoButton.setBounds(x, y, w / 2, h);
        x += w * 2 / 4 + 10;
        seleccionDeLaCartaButton.setBounds(x, y, w / 2, h);
        x += w * 2 / 4 + 10;
        productosScrollPane.setBounds(0, 150, LeftPanel_Rectangle.width, LeftPanel_Rectangle.height - 150);
        productosPanel.setLayout(null);
        productosScrollPane.setBackground(Color.red);

        clientePanel.add(ingresarCodigoTextLabel);
        clientePanel.add(ingresarCodigoTextField);
        clientePanel.add(seleccionProductoButton);
        clientePanel.add(seleccionDeLaCartaButton);
        clientePanel.add(cerrarClienteButton);
        clientePanel.add(notificarCocinaButton);
        clientePanel.add(productosScrollPane);
        //clientePanel.add(productosPanel);

        clientePanel.setBounds(LeftPanel_Rectangle);
        clientePanel.setLayout(null);

    }

    private void setPedidoListLabelAndButtons() {
        int x = 60, y = 20, w = 300, h = 50, dx = 100, w2 = 50, dy = 50;
        labelList.forEach((label) -> {
            productosPanel.remove(label);
        });
        buttonList.forEach((button) -> {
            productosPanel.remove(button);
        });
        Set<Integer> showedItems = new HashSet<>();
        for (SalableItem dto : cliente.getItemListUnBilled()) {
            JButton button = new JButton("X");
            button.addActionListener(new buttonProductoActionListener(dto, cliente));
            button.setBounds(x, y, w2, h);
            buttonList.add(button);
            productosPanel.add(button);

            if (!showedItems.contains(dto.getSerial())) {
                long quantity = cliente.getItemListBilled().stream()
                        .filter(item -> dto.getSerial().equals(item.getSerial())).count();
                JLabel label = new JLabel(
                        "<" + dto.getId() + "> " +
                                "x" + (int) quantity +
                                " " + dto.getName());
                label.setBounds(x + w2, y, w, h);
                labelList.add(label);
                productosPanel.add(label);
                showedItems.add(dto.getSerial());
                y += dy;
            }

        }
        productosPanel.setPreferredSize(new Dimension(LeftPanel_Rectangle.width, y));
        productosPanel.repaint();

        consoleFlush();
        printActual();
        statusRefresh();
    }

    private void printActual() {

        if (cliente.isOccupied()) {

            consoleAppend(cliente.getIdentifier());

            Set<Integer> showedItems = new HashSet<>();
            for (SalableItem salableItem : cliente.getItemListBilled()) {
                if (!showedItems.contains(salableItem.getSerial())) {
                    long quantity = cliente.getItemListBilled().stream()
                            .filter(item -> salableItem.getSerial().equals(item.getSerial())).count();
                    String consoleString =
                            "    <" + salableItem.getId() + "> x" +
                                    String.format("%03d", (int) quantity) + " " +
                                    salableItem.getName() + " " + (int) (salableItem.getPrice() * quantity);
                    consoleAppend(consoleString);
                    showedItems.add(salableItem.getSerial());
                }
            }

            showedItems = new HashSet<>();
            for (SalableItem salableItem : cliente.getItemListUnBilled()) {
                if (!showedItems.contains(salableItem.getSerial())) {
                    long quantity = cliente.getItemListBilled().stream()
                            .filter(item -> salableItem.getSerial().equals(item.getSerial())).count();
                    String consoleString =
                            "  -><" + salableItem.getId() + "> " +
                                    "x" + String.format("%03d", (int) quantity) + " " +
                                    salableItem.getName() + " " + (int) (salableItem.getPrice() * quantity);
                    consoleAppend(consoleString);
                    showedItems.add(salableItem.getSerial());
                }
            }

            consoleAppend("\n****** TOTAL: " + (int) cliente.getConsumption() + " *******");

        } else {
            consoleAppend(cliente.getIdentifier() + " Disponible");
        }
    }

    private void ingresarProducto(InventoryItem inventoryItem) {
        boolean            valid, addNew;
        int                cantidad = 1;
        JPanel             panel    = new JPanel();
        JLabel             label    = new JLabel("Ingrese Cantidad de " + inventoryItem.getName() + ":");
        EnterableTextField quantity = new EnterableTextField(10);
        panel.add(label);
        panel.add(quantity);
        String[] options = new String[]{"Agregar", "Cancelar"};
        do {
            int option = JOptionPane.showOptionDialog(
                    null,
                    panel,
                    "Cantidad de " + inventoryItem.getName(),
                    JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE,
                    null,
                    options,
                    options[0]);
            if (option == 0) {
                if (!quantity.getText().isEmpty()) {
                    try {
                        cantidad = Integer.valueOf(quantity.getText());
                        valid = true;
                    } catch (NumberFormatException e) {
                        valid = false;
                    }
                } else {
                    valid = true;
                }
                addNew = true;
            } else {
                cantidad = 0;
                valid = true;
                addNew = false;
            }
            quantity.setText("");
        } while (!valid);

        if (addNew &&
                (inventoryItem.getType() == InventoryItem.Type.Product ||
                        inventoryItem.getType() == InventoryItem.Type.MenuPlate)) {

            if (!cliente.isOccupied()) {
                cerrarClienteButton.setEnabled(true);
                if (!cliente.getType().equals(ICustomer.CustomerTypes.ExternalCustomer) &&
                        !cliente.getType().equals(ICustomer.CustomerTypes.PointOfService)) {
                    notificarCocinaButton.setEnabled(true);
                }
            }

            for (int i = 0; i < cantidad; i++) {

                SalableItem salableItem = null;

                if (inventoryItem.getType() == InventoryItem.Type.Product)
                    salableItem = new SalableItem(inventoryItem, "");

                if (inventoryItem.getType() == InventoryItem.Type.MenuPlate) {
                    salableItem = new SalableItem(inventoryItem, "");
                    AgregarProductoADeLaCarta auxFrame =
                            new AgregarProductoADeLaCarta(contextFrame, thisFrame, true);
                    auxFrame.startFrame(salableItem);
                }

                if (salableItem != null)
                    cliente.addItem(user, salableItem);

            }

            setPedidoListLabelAndButtons();
            ingresarCodigoTextField.setText("");
            statusRefresh();

        }
    }

    private class buttonProductoActionListener implements ActionListener {

        SalableItem dto;
        ICustomer   cliente;

        public buttonProductoActionListener(SalableItem dto, ICustomer mesa) {
            this.dto = dto;
            this.cliente = mesa;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            cliente.removeItem(dto);
            setPedidoListLabelAndButtons();
        }
    }


    class EnterableTextField extends JTextField {

        EnterableTextField(int len) {
            super(len);
            addKeyListener(new KeyAdapter() {
                @Override
                public void keyPressed(KeyEvent evt) {
                    int key = evt.getKeyCode();
                    if (key == KeyEvent.VK_ENTER) transferFocus();
                }
            });
        }
    }

}