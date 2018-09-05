/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpuyosa91.posaplications.RestoPosWeb._02_Views._1_Pedidos.Mesas;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Accounting.Bill;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Customers.Customer;
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
    private final UUID               cliente_id;
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

    public PedidosFrame(MainFrame contextFrame, UUID cliente_id) {
        super(contextFrame);
        this.cliente_id = cliente_id;
        Customer customer = GeneralController.getCustomer(cliente_id);
        this.thisFrame = this;
        createObjects(customer);
        createPanels(customer);
        statusRefresh(customer);
    }

    public void startframe(User user, PedidosPanel father) {
        Customer cliente = GeneralController.getCustomer(cliente_id);
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
            printActual(cliente);
            setVisible(true);
        }
        setPedidoListLabelAndButtons();
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

    private void createObjects(Customer cliente) {
        clientePanel = new JPanel();
        notificarCocinaButton = new JButton("Enviar Nuevas Comandas");
        notificarCocinaButton.setEnabled(false);
        cerrarClienteButton = new JButton("Cerrar " + cliente.getIdentifier());
        cerrarClienteButton.setEnabled(false);
        ingresarCodigoTextField = new JTextField();
        seleccionProductoButton = new JButton("Productos");
        seleccionDeLaCartaButton = new JButton("De La Carta");
        productosPanel = new JPanel();
        productosScrollPane = new JScrollPane();
    }

    private void createPanels(Customer customer) {
        createClientePanel(customer);
        containerPanel.add(clientePanel);
    }

    private void statusRefresh(Customer cliente) {
        if (cliente.isOccupied()) {
            informationLabel.setText("Ocupada");
            navigationPanel.setBackground(Color.LIGHT_GRAY);
            cerrarClienteButton.setEnabled(true);
            for (ActionListener actionListener : cerrarClienteButton.getActionListeners()) {
                cerrarClienteButton.removeActionListener(actionListener);
            }
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
                pagoTextField.getInputMap().put(KeyStroke.getKeyStroke(KeyEvent.VK_BACK_SPACE, 0, true),
                                                abstractAction);

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
            if (!cliente.getType().equals(ICustomer.CustomerTypes.ExternalCustomer) &&
                !cliente.getType().equals(ICustomer.CustomerTypes.PointOfService)) {
                notificarCocinaButton.setEnabled(true);
            }
        } else {
            informationLabel.setText("Disponible");
            navigationPanel.setBackground(Color.green);
            cerrarClienteButton.setEnabled(false);
            notificarCocinaButton.setEnabled(false);
        }
    }

    private void createClientePanel(Customer cliente) {
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
                    InventoryItem aux = GeneralController.getInventoryItem(Integer.parseInt(ingresarCodigoTextField.getText()));
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
                    GeneralController.getRootInventoryItem(InventoryItem.Type.Product));
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
                    GeneralController.getRootInventoryItem(InventoryItem.Type.MenuPlate));
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
        productosPanel.setLayout(null);
        productosScrollPane.setBounds(0, 150, LeftPanel_Rectangle.width, LeftPanel_Rectangle.height - 150);
        productosScrollPane.setBackground(Color.red);
        productosScrollPane.setViewportView(productosPanel);
        productosScrollPane.getVerticalScrollBar().setUnitIncrement(10);
        productosScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        productosScrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        clientePanel.add(ingresarCodigoTextLabel);
        clientePanel.add(ingresarCodigoTextField);
        clientePanel.add(seleccionProductoButton);
        clientePanel.add(seleccionDeLaCartaButton);
        clientePanel.add(cerrarClienteButton);
        clientePanel.add(notificarCocinaButton);
        clientePanel.add(productosScrollPane);

        clientePanel.setBounds(LeftPanel_Rectangle);
        clientePanel.setLayout(null);

    }

    private void createButton(Customer cliente, SalableItem dto, int x, int y, int w, int w2, int h, long quantity) {
        JButton button = new JButton("X");
        button.addActionListener(new buttonProductoActionListener(dto, cliente, quantity));
        button.setBounds(x, y, w2, h);
        buttonList.add(button);
        productosPanel.add(button);

        JLabel label;
        if (dto.getNotes().equals("")) {
            label = new JLabel(
                    "<" + dto.getSerial() + "> x" + quantity + " " +
                            " " + dto.getName());
        } else {
            label = new JLabel(
                    "<" + dto.getSerial() + "> x" + quantity + " " +
                            " " + dto.getName() +
                            ". Notas: " + dto.getNotes() + ".");
        }

        label.setBounds(x + w2, y, w, h);
        labelList.add(label);
        productosPanel.add(label);
    }

    private void setPedidoListLabelAndButtons() {
        Customer cliente = GeneralController.getCustomer(cliente_id);
        int      x       = 60, y = 20, w = 300, h = 50, dx = 100, w2 = 50, dy = 50;
        labelList.forEach((label) -> {
            productosPanel.remove(label);
        });
        buttonList.forEach((button) -> {
            productosPanel.remove(button);
        });

        ArrayList<SalableItem> orderedList = new ArrayList<>(cliente.getItemListUnBilled());
        orderedList.sort(Comparator.comparingInt(SalableItem::getSerial).reversed());

        Set<Integer> showedItems = new HashSet<>();
        for (SalableItem dto : orderedList) {
            if (dto.getSerial().toString().substring(0, 1).equals("3")) {
                if (!showedItems.contains(dto.getSerial())) {
                    long quantity = orderedList.stream()
                            .filter(item -> dto.getSerial().equals(item.getSerial())).count();
                    createButton(cliente, dto, x, y, w, w2, h, quantity);
                    y += dy;
                    showedItems.add(dto.getSerial());
                }
            } else if (dto.getSerial().toString().substring(0, 1).equals("4")) {
                createButton(cliente, dto, x, y, w, w2, h, 1);
                y += dy;
            }

        }

        productosPanel.setPreferredSize(new Dimension(LeftPanel_Rectangle.width, y));
        productosPanel.repaint();
        productosScrollPane.revalidate();

        consoleFlush();
        printActual(cliente);
        statusRefresh(cliente);
    }

    private void printActual(Customer cliente) {

        if (cliente.isOccupied()) {

            consoleAppend(cliente.getIdentifier());

            Set<Integer> showedItems = new HashSet<>();
            for (SalableItem salableItem : cliente.getItemListBilled()) {
                if (!showedItems.contains(salableItem.getSerial())) {
                    long quantity = cliente.getItemListBilled().stream()
                            .filter(item -> salableItem.getSerial().equals(item.getSerial())).count();
                    String consoleString =
                            "    <" + salableItem.getSerial() + "> x" +
                                    String.format("%03d", (int) quantity) + " " +
                                    salableItem.getName() + " " + (int) (salableItem.getPrice() * quantity);
                    consoleAppend(consoleString);
                    showedItems.add(salableItem.getSerial());
                }
            }

            showedItems = new HashSet<>();
            for (SalableItem salableItem : cliente.getItemListUnBilled()) {
                if (!showedItems.contains(salableItem.getSerial())) {
                    long quantity = cliente.getItemListUnBilled().stream()
                            .filter(item -> salableItem.getSerial().equals(item.getSerial())).count();
                    String consoleString =
                            "  -><" + salableItem.getSerial() + "> " +
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
        Customer cliente  = GeneralController.getCustomer(cliente_id);
        int      cantidad = 1;

        switch (inventoryItem.getType()) {
            case Product:

                boolean valid, addNew;
                JPanel panel = new JPanel();
                JLabel label = new JLabel("Ingrese Cantidad de " + inventoryItem.getName() + ":");
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

                if (addNew) {
                    for (int i = 0; i < cantidad; i++) {
                        if (GeneralController.addItemToCustomer(cliente, user, inventoryItem, "")) {
                            continue;
                        } else {
                            // TODO Mostrar el fallo
                            break;
                        }
                    }
                }

                break;
            case MenuPlate:
                SalableItem salableItem = new SalableItem(inventoryItem, "");
                AgregarProductoADeLaCarta auxFrame =
                        new AgregarProductoADeLaCarta(contextFrame, thisFrame, true, salableItem);
                auxFrame.startFrame();
                if (!GeneralController.addItemToCustomer(cliente, user, salableItem)) {
                    // TODO Mostrar el fallo
                    break;
                }
                break;
        }
        setPedidoListLabelAndButtons();
        ingresarCodigoTextField.setText("");
    }

    private class buttonProductoActionListener implements ActionListener {

        SalableItem dto;
        ICustomer   cliente;
        long        quantity;

        public buttonProductoActionListener(SalableItem dto, ICustomer mesa, long quantity) {
            this.dto = dto;
            this.cliente = mesa;
            this.quantity = quantity;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if (dto.getSerial().toString().substring(0, 1).equals("3")) {
                for (SalableItem salableItem : cliente.getItemListUnBilled()) {
                    if (salableItem.getSerial().equals(dto.getSerial())) {
                        GeneralController.removeItemFromCustomer((Customer) cliente, salableItem);
                    }
                }
            } else if (dto.getSerial().toString().substring(0, 1).equals("4")) {
                GeneralController.removeItemFromCustomer((Customer) cliente, dto);
            }
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