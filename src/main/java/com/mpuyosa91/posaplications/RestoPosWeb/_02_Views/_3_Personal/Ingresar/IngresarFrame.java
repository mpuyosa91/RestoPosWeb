/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpuyosa91.posaplications.RestoPosWeb._02_Views._3_Personal.Ingresar;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Crew.User;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.MainFrame;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.Templates.WindowFrame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

/**
 * @author MoisesE
 */
public class IngresarFrame extends WindowFrame {

    //------------------------------------------------------------------------------
    private final IngresarFrame             thisFrame;
    private final UUID                      site_id;
    private final int                       initialX  = 30;
    private final int                       initialY  = 100;
    private final int                       xElements = 2;
    //------------------------------------------------------------
    private       JScrollPane               listItemScroll;
    //------------------------------------------------------------
    private       JPanel                    listUserPanel;
    private       User                      user;
    private       JButton                   atrasButton;
    //------------------------------------------------------------
    private       ArrayList<User>           userList;
    private       ArrayList<ActionListener> listenerList;
    private       ArrayList<JButton>        buttonList;
    //------------------------------------------------------------
    private       boolean                   isNew;
    private       JScrollPane               singleItemScroll;
    //------------------------------------------------------------
    private       JTextArea                 nombreTextArea;
    private       JTextArea                 apellidoTextArea;
    private       JTextArea                 documentTypeTextArea;
    private       JTextArea                 documentNumberTextArea;
    private       JTextArea                 usernameTextArea;
    private       JPasswordField            passwordField1;
    private       JPasswordField            passwordField2;
    private       JTextArea                 emailTextArea;

    private JButton   cargarButton;
    private Rectangle cargarButton_rectangle;
    private JPanel    singleUserPanel;

    public IngresarFrame(MainFrame contextFrame, UUID site_id) {
        super(contextFrame);
        this.thisFrame = this;
        this.site_id = site_id;
        createObjects();
        createPanels();
    }

    @Override
    public void startframe() {
        consoleFlush();
        informationLabel.setText("Lista Usuarios");
        setButtonsListUserPanel(GeneralController.getSite(site_id));
        setVisiblePanel(listItemScroll);
        setVisible(true);
    }

    @Override
    public void exit_hide() {
        setVisible(false);
        consoleFlush();
    }

    @Override
    protected void setVisiblePanel(Component panel) {
        listItemScroll.setVisible(false);
        singleItemScroll.setVisible(false);
        panel.setVisible(true);
    }

    //------------------------------------------------------------------------------
    private void createObjects() {

        listUserPanel = new JPanel();
        listenerList = new ArrayList<>();
        buttonList = new ArrayList<>();
        listItemScroll = new JScrollPane();
        singleUserPanel = new JPanel();

        nombreTextArea = new JTextArea(1, 1);
        apellidoTextArea = new JTextArea(1, 1);
        usernameTextArea = new JTextArea(1, 1);
        documentTypeTextArea = new JTextArea(1, 1);
        documentNumberTextArea = new JTextArea(1, 1);
        passwordField1 = new JPasswordField();
        passwordField2 = new JPasswordField();
        emailTextArea = new JTextArea(1, 1);

        cargarButton = new JButton("Cargar");
        atrasButton = new JButton("Atras");
        singleItemScroll = new JScrollPane();

    }

    private void createPanels() {
        createListItemPanel();
        createSingleUserPanel();
        setVisiblePanel(listItemScroll);
    }

    private void createListItemPanel() {
        listUserPanel.setBounds(LeftPanel_Rectangle);
        listUserPanel.setLayout(null);
        informationLabel.setText("Lista Usuarios");
        setButtonsListUserPanel(GeneralController.getSite(site_id));
        listItemScroll.setBounds(LeftPanel_Rectangle);
        listItemScroll.setViewportView(listUserPanel);
        listItemScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listItemScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        containerPanel.add(listItemScroll);
    }

    private void setButtonsListUserPanel(Site tree) {
        setButtonsListenersListItemPanel(tree.getUsers());
        Set<User> users = GeneralController.getSite(site_id).getUsers();
        this.consoleFlush();
        this.consoleAppend("Encontrados " + tree.getUsers().size() + " Usuario(s)");
        consoleAppend(GeneralController.treeToString(users));
        listUserPanel.repaint();
    }

    private void setButtonsListenersListItemPanel(Set<User> tree) {
        userList = new ArrayList<>(tree);
        userList.sort(Comparator.comparingInt(User::getDocumentNumber));
        for (JButton button : buttonList) {
            listUserPanel.remove(button);
        }
        buttonList = new ArrayList<>();
        final int stepX = (listUserPanel.getWidth() - initialX - 2) / xElements, stepY = 100;
        if (buttonList.isEmpty()) {
            JButton auxButton;
            auxButton = new JButton("Nuevo <Usuario>");
            auxButton.setBounds(initialX + stepX * (0 % xElements),
                                initialY + stepY * (0 / xElements),
                                stepX - initialX,
                                stepY);
            auxButton.addActionListener(new UserAtionListener(0));
            buttonList.add(0, auxButton);
            listenerList.add(0, auxButton.getActionListeners()[0]);
            listUserPanel.add(buttonList.get(0));
            for (int i = 0; i < userList.size(); i++) {
                auxButton = new JButton("<html><center>" +
                                        userList.get(i).getFirstName() +
                                        "<br>" +
                                        userList.get(i).getLastName() +
                                        "</center></html>");
                auxButton.setBounds(initialX + stepX * ((i + 1) % xElements),
                                    initialY + stepY * ((i + 1) / xElements),
                                    stepX - initialX,
                                    stepY);
                auxButton.addActionListener(new UserAtionListener((i + 1)));
                buttonList.add(i + 1, auxButton);
                listenerList.add(i + 1, auxButton.getActionListeners()[0]);
                listUserPanel.add(buttonList.get(i + 1));
            }
        }
        listUserPanel.setPreferredSize(new Dimension(listUserPanel.getWidth(),
                                                     20 + initialY + stepY * ((userList.size() + 2) / xElements)));
    }

    private void userListClickExecuter() {
        setButtonsListUserPanel(GeneralController.getSite(site_id));
        setVisiblePanel(listItemScroll);
    }

    private void deInventarioClickExecuter(User user) {
        this.user = user;
        isNew = user.getId() == null;
        if (user.getFirstName() != null) nombreTextArea.setText(user.getFirstName());
        if (user.getLastName() != null) apellidoTextArea.setText(user.getLastName());
        if (user.getDocumentNumber() != null) documentNumberTextArea.setText(user.getDocumentNumber().toString());
        if (user.getUsername() != null) usernameTextArea.setText(user.getUsername());
        if (user.getEmail() != null) emailTextArea.setText(user.getEmail());
        actualizeSingleItemPanel(user);
        setVisiblePanel(singleItemScroll);
    }

    private void setButtonsListenersSingleItemPanel(InventoryItem dto) {
        for (ActionListener actionListener : cargarButton.getActionListeners())
            cargarButton.removeActionListener(actionListener);
        atrasButton.addActionListener(new AtrasActionListener());
        cargarButton.addActionListener(new CargarActionListener());
    }

    private boolean loadInActual() {
        boolean validData = true;

        String firstName      = nombreTextArea.getText();
        String lastName       = apellidoTextArea.getText();
        String username       = usernameTextArea.getText();
        String documentType   = null; // documentTypeTextArea.getText();
        String documentNumber = documentNumberTextArea.getText();
        String email          = emailTextArea.getText();

        String nameRegex     = "^[a-zA-Z]+[\\s]?[a-zA-Z]*$";
        String documentRegex = "^[0-9]+$";
        String usernameRegex = "^[a-zA-Z][a-zA-Z0-9_]+$";
        String emailRegex    = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        validData = Pattern.compile(nameRegex).matcher(firstName).matches() &&
                    Pattern.compile(nameRegex).matcher(lastName).matches() &&
                    Pattern.compile(usernameRegex).matcher(username).matches() &&
                    Pattern.compile(documentRegex).matcher(documentNumber).matches() &&
                    Pattern.compile(emailRegex).matcher(email).matches();

        if (validData) {
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setUsername(username);
            user.setDocumentType(User.DocumentType.valueOf(User.DocumentType.CC.name()));
            user.setDocumentNumber(Integer.parseInt(documentNumber));
            user.setEmail(email);
        }

        return validData;
    }

    private void cargar() {
        if (isNew) GeneralController.createUser(user);
        else GeneralController.updateUser(user);
    }

    private void createSingleUserPanel() {
        for (ActionListener actionListener : atrasButton.getActionListeners())
            atrasButton.removeActionListener(actionListener);
        for (ActionListener actionListener : cargarButton.getActionListeners())
            cargarButton.removeActionListener(actionListener);

        singleUserPanel.setBounds(LeftPanel_Rectangle);
        singleUserPanel.setLayout(null);

        JLabel nombreLabel         = new JLabel("Nombre:");
        JLabel apellidoLabel       = new JLabel("Apellido");
        JLabel usernameLabel       = new JLabel("Usuario");
        JLabel documentTypeLabel   = new JLabel("Tipo de Documento");
        JLabel documentNumberLabel = new JLabel("Numero de Documento");
        JLabel passwordLabel1      = new JLabel("Indique su contraseña.");
        JLabel passwordLabel2      = new JLabel("Repita su contraseña.");
        JLabel emailLabel          = new JLabel("Correo Electronico");

        int x = 50, y = 100, w = 200, h = 50, dx = 200, dw = 80, dy = 50;
        singleUserPanel.add(atrasButton);
        atrasButton.setBounds(30, 25, 100, 50);
        atrasButton.addActionListener(new AtrasActionListener());
        singleUserPanel.add(nombreLabel);
        nombreLabel.setBounds(x, y, w, h);
        singleUserPanel.add(nombreTextArea);
        nombreTextArea.setBounds(dx + x, y, dw + w, h);
        y += dy;
        singleUserPanel.add(apellidoLabel);
        apellidoLabel.setBounds(x, y, w, h);
        singleUserPanel.add(apellidoTextArea);
        apellidoTextArea.setBounds(dx + x, y, dw + w, h);
        y += dy;
        singleUserPanel.add(usernameLabel);
        usernameLabel.setBounds(x, y, w, h);
        singleUserPanel.add(usernameTextArea);
        usernameTextArea.setBounds(dx + x, y, dw + w, h);
        y += dy;
        singleUserPanel.add(documentNumberLabel);
        documentNumberLabel.setBounds(x, y, w, h);
        singleUserPanel.add(documentNumberTextArea);
        documentNumberTextArea.setBounds(dx + x, y, dw + w, h);
        y += dy;
        singleUserPanel.add(passwordLabel1);
        passwordLabel1.setBounds(x, y, w, h);
        singleUserPanel.add(passwordField1);
        passwordField1.setBounds(dx + x, y, dw + w, h);
        passwordField1.setEnabled(false);
        y += dy;
        singleUserPanel.add(passwordLabel2);
        passwordLabel2.setBounds(x, y, w, h);
        singleUserPanel.add(passwordField2);
        passwordField2.setBounds(dx + x, y, dw + w, h);
        passwordField2.setEnabled(false);
        y += dy;
        singleUserPanel.add(emailLabel);
        emailLabel.setBounds(x, y, w, h);
        singleUserPanel.add(emailTextArea);
        emailTextArea.setBounds(dx + x, y, dw + w, h);
        y += dy;

        dy = 50;
        x = 250;
        w = 280;
        h = 50;
        cargarButton_rectangle = new Rectangle(x - 145, y, w + 100, h);
        cargarButton.setBounds(cargarButton_rectangle);
        cargarButton.addActionListener(new CargarActionListener());
        singleUserPanel.add(cargarButton);

        singleItemScroll.setBounds(LeftPanel_Rectangle);
        singleItemScroll.setViewportView(singleUserPanel);
        singleItemScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        singleItemScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        containerPanel.add(singleItemScroll);
    }

    private void actualizeSingleItemPanel(User user) {
        this.consoleFlush();
        if ("".equals(user.getFirstName())) consoleAppend("Nuevo Usuario");
        consoleAppend(user.toConsole());
        singleItemScroll.revalidate();
    }

    private class UserAtionListener implements ActionListener {

        private final User user;

        public UserAtionListener(int i) {
            if (i != 0) {
                this.user = userList.get(i - 1);
            } else {
                this.user = new User();
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            deInventarioClickExecuter(user);
        }
    }


    private class AtrasActionListener implements ActionListener {

        public AtrasActionListener() {
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            userListClickExecuter();
        }
    }


    private class CargarActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (loadInActual()) {
                cargar();
                userListClickExecuter();
                setVisiblePanel(listItemScroll);
            } else {
                String message = "Alguno de los campos esta erroneo o faltante, por favor revise e intentelo de nuevo.";
                JOptionPane.showMessageDialog(null, message);
            }
        }
    }
}
