/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpuyosa91.posaplications.RestoPosWeb._02_Views._2_Inventario.Ingresar;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.ProductsAndSupplies.InventoryItem_Ingredients;
import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.MainFrame;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.Templates.WindowFrame;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views.VisualSetter.DeInventarioVisualSetter;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;

import static java.lang.StrictMath.round;

/**
 * @author MoisesE
 */
public class IngresarFrame extends WindowFrame {

    //------------------------------------------------------------------------------
    private final IngresarFrame             thisFrame;
    private final InventoryItem.Type        inventoryType;
    private final int                       initialX    = 30;
    private final int                       initialY    = 100;
    private final int                       xElements   = 2;
    public        boolean                   showMesgSys = false;
    //------------------------------------------------------------
    private       DeInventarioVisualSetter  ingredienteVisualSetter;
    private       DeInventarioVisualSetter  subProductoVisualSetter;
    private       DeInventarioVisualSetter  productoVisualSetter;
    private       DeInventarioVisualSetter  deLaCartaVisualSetter;
    private       JScrollPane               listItemScroll;
    //------------------------------------------------------------
    private       JPanel                    listItemPanel;
    private       InventoryItem             dto;
    private       JButton                   borrarButtonListItemPanel;
    private       JButton                   atrasButtonListItemPanel;
    //------------------------------------------------------------
    private       ArrayList<ActionListener> listenerList;
    private       ArrayList<JButton>        buttonList;
    //------------------------------------------------------------
    private       InventoryItem             ingredienteActual;
    private       InventoryItem             subProductoActual;
    private       InventoryItem             productoActual;
    private       InventoryItem             deLaCartaActual;
    private       ArrayList<InventoryItem>  ingredienteList;
    private       ArrayList<InventoryItem>  subProductoList;
    private       ArrayList<InventoryItem>  productoList;
    private       ArrayList<InventoryItem>  deLaCartaList;
    //------------------------------------------------------------
    private       boolean                   isNew;
    private       JCheckBox                 isFinalCheckBox;
    private       JScrollPane               singleItemScroll;
    //------------------------------------------------------------
    private       ArrayList<JLabel>         componentesLabelList;
    private       ArrayList<JTextArea>      componentesTextAreaList;
    private       JTextArea                 nombreTextArea;
    private       JComboBox                 typeComboBox;
    private       JLabel                    unidadJLabel;
    private       JComboBox                 unidadComboBox;
    private       JLabel                    cantidadLabel;
    private       JTextArea                 cantidadTextArea;
    private       JLabel                    precioJLabel;
    private       JTextArea                 precioTextArea;
    private       JButton                   addIngredButton;
    private       JButton                   addSubProButton;
    private       JButton                   addProducButton;
    private       JButton                   addDeLaCaButton;
    private       JButton                   cargarButton;
    private       Rectangle                 cargarButton_rectangle;
    private       JButton                   borrarButton;
    private       JButton                   atrasButton;
    private       JPanel                    singleItemPanel;

    public IngresarFrame(MainFrame contextFrame, InventoryItem.Type inventoryType) {
        super(contextFrame);
        this.thisFrame = this;
        this.inventoryType = inventoryType;
        createObjects();
        createPanels();
    }

    @Override
    public void startframe() {
        consoleFlush();
        informationLabel.setText("Menu " + inventoryType);
        setButtonsListItemPanel(GeneralController.getRootInventoryItem(inventoryType));
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
        listItemPanel = new JPanel();
        listenerList = new ArrayList<>();
        buttonList = new ArrayList<>();
        listItemScroll = new JScrollPane();
        singleItemPanel = new JPanel();
        componentesLabelList = new ArrayList<>();
        componentesTextAreaList = new ArrayList<>();
        nombreTextArea = new JTextArea(1, 1);
        cantidadLabel = new JLabel();
        cantidadTextArea = new JTextArea(1, 1);
        typeComboBox = new JComboBox();
        unidadComboBox = new JComboBox();
        precioTextArea = new JTextArea(1, 1);
        isFinalCheckBox = new JCheckBox();
        addIngredButton = new JButton("Agregar RawFood");
        addSubProButton = new JButton("Agregar Mixture");
        addProducButton = new JButton("Agregar Producto");
        addDeLaCaButton = new JButton("Agregar MenuPlate");
        cargarButton = new JButton("Cargar");
        atrasButton = new JButton("Atras");
        borrarButton = new JButton("Borrar");
        atrasButtonListItemPanel = new JButton("Atras");
        borrarButtonListItemPanel = new JButton("Borrar");
        singleItemScroll = new JScrollPane();
    }

    private void createPanels() {
        createListItemPanel();
        createSingleItemPanel();
        ingredienteVisualSetter = new DeInventarioVisualSetter(contextFrame,
                                                               thisFrame,
                                                               true,
                                                               InventoryItem.Type.RawFood);
        subProductoVisualSetter = new DeInventarioVisualSetter(contextFrame,
                                                               thisFrame,
                                                               true,
                                                               InventoryItem.Type.Mixture);
        productoVisualSetter = new DeInventarioVisualSetter(contextFrame,
                                                            thisFrame,
                                                            true,
                                                            InventoryItem.Type.Product);
        deLaCartaVisualSetter = new DeInventarioVisualSetter(contextFrame,
                                                             thisFrame,
                                                             true,
                                                             InventoryItem.Type.MenuPlate);
        setVisiblePanel(listItemScroll);
    }

    private void createListItemPanel() {
        listItemPanel.setBounds(LeftPanel_Rectangle);
        listItemPanel.setLayout(null);
        informationLabel.setText("Lista " + inventoryType);
        setButtonsListItemPanel(GeneralController.getRootInventoryItem(inventoryType));
        listItemScroll.setBounds(LeftPanel_Rectangle);
        listItemScroll.setViewportView(listItemPanel);
        listItemScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        listItemScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        containerPanel.add(listItemScroll);
    }

    private void setButtonsListItemPanel(InventoryItem tree) {
        if (listItemPanel.getComponentCount() == 0) {
            listItemPanel.add(atrasButtonListItemPanel);
            listItemPanel.add(borrarButtonListItemPanel);
            atrasButtonListItemPanel.setBounds(30, 25, 100, 50);
            borrarButtonListItemPanel.setBounds(140, 25, 390, 50);
        }
        atrasButtonListItemPanel.setEnabled(tree.getSerial() > 10);
        //borrarButtonListItemPanel.setEnabled(tree.getSerial() > 10);
        borrarButtonListItemPanel.setEnabled(false);
        switch (inventoryType) {
            case RawFood:
                ingredienteActual = tree;
                ingredienteList = GeneralController.getChildes(ingredienteActual);
                break;
            case Mixture:
                subProductoActual = tree;
                subProductoList = GeneralController.getChildes(subProductoActual);
                break;
            case Product:
                productoActual = tree;
                productoList = GeneralController.getChildes(productoActual);
                break;
            case MenuPlate:
                deLaCartaActual = tree;
                deLaCartaList = GeneralController.getChildes(deLaCartaActual);
                break;
        }
        setButtonsListenersListItemPanel(tree);
        ArrayList<InventoryItem> downChilds = GeneralController.getChildes(tree);
        if (!downChilds.isEmpty()) {
            this.consoleFlush();
            this.consoleAppend("Encontrados " + downChilds.size() + " " + inventoryType + "(s)");
            consoleAppend(GeneralController.treeToString(tree));
        } else {
            this.consoleFlush();
            this.consoleAppend("Lista <" + inventoryType + "> Vacia");
        }
        listItemPanel.repaint();
    }

    private void setButtonsListenersListItemPanel(InventoryItem tree) {
        ArrayList<InventoryItem> list = GeneralController.getChildes(tree);
        list.sort(Comparator.comparingInt(InventoryItem::getSerial));
        final int stepX = (listItemPanel.getWidth() - initialX - 2) / xElements, stepY = 100;
        for (ActionListener actionListener : atrasButton.getActionListeners())
            atrasButtonListItemPanel.removeActionListener(actionListener);
        for (ActionListener actionListener : borrarButton.getActionListeners())
            borrarButtonListItemPanel.removeActionListener(actionListener);
        atrasButtonListItemPanel.addActionListener(new AtrasActionListener(inventoryType));
        borrarButtonListItemPanel.addActionListener(new BorrarActionListener(inventoryType));
        if (buttonList.isEmpty()) {
            JButton auxButton;
            auxButton = new JButton("Nuevo <" + tree.getName() + ">");
            auxButton.setBounds(initialX + stepX * (0 % xElements),
                                initialY + stepY * (0 / xElements),
                                stepX - initialX,
                                stepY);
            auxButton.addActionListener(new DeInventarioActionListener(0));
            buttonList.add(0, auxButton);
            listenerList.add(0, auxButton.getActionListeners()[0]);
            listItemPanel.add(buttonList.get(0));
            for (int i = 0; i < list.size(); i++) {
                auxButton = new JButton(
                        "<html><center>" + list.get(i).getName() + "<br>" +
                                "&lt;" + String.valueOf(list.get(i).getSerial()) + "&gt;</center></html>");
                auxButton.setBounds(initialX + stepX * ((i + 1) % xElements),
                                    initialY + stepY * ((i + 1) / xElements),
                                    stepX - initialX,
                                    stepY);
                auxButton.addActionListener(new DeInventarioActionListener((i + 1)));
                buttonList.add(i + 1, auxButton);
                listenerList.add(i + 1, auxButton.getActionListeners()[0]);
                listItemPanel.add(buttonList.get(i + 1));
            }
        } else {
            int     buttonPosition;
            boolean found;
            JButton auxButton;
            if (!buttonList.get(0).getText().equals("Nuevo <" + tree.getName() + ">") || buttonList.size() != (list.size() + 1)) {
                listItemPanel.remove(buttonList.get(0));
                auxButton = new JButton("Nuevo <" + tree.getName() + ">");
                auxButton.setBounds(initialX + stepX * (0 % xElements),
                                    initialY + stepY * (0 / xElements),
                                    stepX - initialX,
                                    stepY);
                auxButton.addActionListener(new DeInventarioActionListener(0));
                buttonList.set(0, auxButton);
                listenerList.set(0, auxButton.getActionListeners()[0]);
                listItemPanel.add(buttonList.get(0));
            }
            for (int i = 0; i < list.size(); i++) {
                buttonPosition = i;
                found = false;
                for (int j = 1; j < buttonList.size(); j++)
                    if (buttonList.get(j).getText().equals(list.get(i).getName())) {
                        buttonPosition = j;
                        found = true;
                        j = buttonList.size();
                    }
                if (found) {
                    if (i != (buttonPosition - 1)) {
                        if (showMesgSys)
                            System.out.println("IngresarFrame.setButtonsListenersListItemPanel.isFoundForReplacement");
                        listItemPanel.remove(buttonList.get(buttonPosition));
                        buttonList.set(i, buttonList.get(buttonPosition));
                        buttonList.remove(buttonPosition);
                        listItemPanel.add(buttonList.get(i));
                    }
                } else {
                    if (showMesgSys)
                        System.out.println(
                                "IngresarFrame.setButtonsListenersListItemPanel.notFoundInButtonListForCreating");
                    auxButton = new JButton(
                            "<html><center>" + list.get(i).getName() + "<br>" +
                                    "&lt;" + String.valueOf(list.get(i).getSerial()) + "&gt;</center></html>");
                    auxButton.setBounds(initialX + stepX * ((i + 1) % xElements),
                                        initialY + stepY * ((i + 1) / xElements),
                                        stepX - initialX,
                                        stepY);
                    auxButton.addActionListener(new DeInventarioActionListener(i + 1));
                    if ((i + 1) < buttonList.size()) {
                        listItemPanel.remove(buttonList.get(i + 1));
                        buttonList.set(i + 1, auxButton);
                        listenerList.set(i + 1, auxButton.getActionListeners()[0]);
                        listItemPanel.add(buttonList.get(i + 1));
                    } else {
                        buttonList.add(auxButton);
                        listenerList.add(auxButton.getActionListeners()[0]);
                        listItemPanel.add(buttonList.get(i + 1));
                    }
                }
            }
            while (buttonList.size() > (list.size() + 1)) {
                if (showMesgSys)
                    System.out.println("IngresarFrame.setButtonsListenersListItemPanel.RemovingExcedent");
                listItemPanel.remove(buttonList.get(buttonList.size() - 1));
                listenerList.remove(listenerList.get(listenerList.size() - 1));
                buttonList.remove(buttonList.get(buttonList.size() - 1));
            }
        }
        listItemPanel.setPreferredSize(new Dimension(listItemPanel.getWidth(),
                                                     20 + initialY + stepY * ((list.size() + 2) / xElements)));
    }

    private void deInventarioClickExecuter(InventoryItem dto) {
        switch (inventoryType) {
            case RawFood:
                ingredienteActual = dto;
                setButtonsListenersSingleItemPanel(ingredienteActual);
                break;
            case Mixture:
                subProductoActual = dto;
                setButtonsListenersSingleItemPanel(subProductoActual);
                break;
            case Product:
                productoActual = dto;
                precioTextArea.setText(Integer.toString((int) productoActual.getPrice()));
                setButtonsListenersSingleItemPanel(productoActual);
                break;
            case MenuPlate:
                deLaCartaActual = dto;
                precioTextArea.setText(Integer.toString((int) deLaCartaActual.getPrice()));
                setButtonsListenersSingleItemPanel(deLaCartaActual);
                break;
        }
        if (dto.isFinal_item()) {
            isNew = "".equals(dto.getName());
            nombreTextArea.setText(dto.getName());
            if (dto.getType() == InventoryItem.Type.Product) {
//                typeComboBox.setSelectedIndex(0);
//                typeComboBox.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
//                unidadComboBox.setSelectedIndex(0);
                //cantidadTextArea.setText(Double.toString(dto.getMeasure().getQuantity()));
            } else if (dto.getType() == InventoryItem.Type.RawFood || dto.getType() == InventoryItem.Type.Mixture) {
                //typeComboBox.setSelectedIndex(dto.getMeasure().getType().ordinal());
//                typeComboBox.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                //unidadComboBox.setSelectedIndex(dto.getMeasure().getFixedPos());
                //cantidadTextArea.setText(Double.toString(dto.getMeasure().getFixedQuantity()));
            }
            isFinalCheckBox.setSelected((!isNew) && dto.isFinal_item());
            actualizeSingleItemPanel(dto);
            setVisiblePanel(singleItemScroll);
        } else {
            setButtonsListItemPanel(dto);
            setVisiblePanel(listItemScroll);
        }
    }

    private void setButtonsListenersSingleItemPanel(InventoryItem dto) {
        for (ActionListener actionListener : atrasButton.getActionListeners())
            atrasButton.removeActionListener(actionListener);
        for (ActionListener actionListener : borrarButton.getActionListeners())
            borrarButton.removeActionListener(actionListener);
        for (ActionListener actionListener : addIngredButton.getActionListeners())
            addIngredButton.removeActionListener(actionListener);
        for (ActionListener actionListener : addSubProButton.getActionListeners())
            addSubProButton.removeActionListener(actionListener);
        for (ActionListener actionListener : addProducButton.getActionListeners())
            addProducButton.removeActionListener(actionListener);
        for (ActionListener actionListener : addDeLaCaButton.getActionListeners())
            addDeLaCaButton.removeActionListener(actionListener);
        for (ActionListener actionListener : cargarButton.getActionListeners())
            cargarButton.removeActionListener(actionListener);
        switch (inventoryType) {
            case RawFood:
                break;
            case Mixture:
                addIngredButton.addActionListener(new IngredienteAddActionListener(dto));
                addSubProButton.addActionListener(new SubProductoAddActionListener(dto));
                break;
            case Product:
                addIngredButton.addActionListener(new IngredienteAddActionListener(dto));
                addSubProButton.addActionListener(new SubProductoAddActionListener(dto));
                addProducButton.addActionListener(new ProductoAddActionListener(dto));
                break;
            case MenuPlate:
                addSubProButton.addActionListener(new SubProductoAddActionListener(dto));
                addProducButton.addActionListener(new ProductoAddActionListener(dto));
                addDeLaCaButton.addActionListener(new DeLaCartaAddActionListener(dto));
                break;
        }
        borrarButton.addActionListener(new BorrarActionListener(inventoryType));
        atrasButton.addActionListener(new AtrasActionListener(inventoryType));
        cargarButton.addActionListener(new CargarActionListener());
    }

    private boolean loadInActual() {
        boolean validData  = true;
        double  precioDoub = 0, cantDoub = 0;
        int     previousItems;
        if (!cantidadTextArea.getText().isEmpty()) {
            try {
                cantDoub = Double.parseDouble(cantidadTextArea.getText());
            } catch (NumberFormatException ex) {
                validData = false;
                String mess = "Ingrese dato valido en campo Cantidad: \n< ";
                mess = mess.concat("Error: " + ex.getMessage());
                JOptionPane.showMessageDialog(null, mess);
            }
        }
        if ((inventoryType.equals(InventoryItem.Type.Product) || inventoryType.equals(InventoryItem.Type.MenuPlate)) && !precioTextArea.getText().isEmpty()) {
            try {
                precioDoub = Double.parseDouble(precioTextArea.getText());
            } catch (NumberFormatException ex) {
                validData = false;
                String mess = "Ingrese dato valido en campo Precio: \n< ";
                mess = mess.concat("Error: " + ex.getMessage() + " >\n< ");
                JOptionPane.showMessageDialog(null, mess);
            }
        }
        if (validData) {
            String nombStrg;
            if (nombreTextArea.getText().length() > 30)
                nombStrg = nombreTextArea.getText().substring(0, 29);
            else nombStrg = nombreTextArea.getText();
            if (nombStrg.length() > 2)
                nombStrg = nombStrg.toUpperCase().charAt(0) + nombStrg.substring(1);
            switch (inventoryType) {
                case RawFood:
                    dto = ingredienteActual;
                    break;
                case Mixture:
                    dto = subProductoActual;
                    break;
                case Product:
                    dto = productoActual;
                    break;
                case MenuPlate:
                    dto = deLaCartaActual;
                    break;
            }
            dto.setName(nombStrg);
            dto.setFinal_item(isFinalCheckBox.isSelected());
            previousItems = 0;
            if (!inventoryType.equals(InventoryItem.Type.MenuPlate))
                //dto.setQuantity(cantDoub, unidadComboBox.getSelectedItem().toString());
                if (inventoryType.equals(InventoryItem.Type.Mixture) || inventoryType.equals(InventoryItem.Type.Product)) {
//                for (int i = 0; i < ((IRetainerRawFood) dto).getRawFoodList().size(); i++) {
//                    try {
//                        cantDoub = Double.parseDouble(componentesTextAreaList.get(i).getText());
//                    } catch (NumberFormatException ex) {
//                        cantDoub = 0.0;
//                        String mess = ex.getMessage();
//                        JOptionPane.showMessageDialog(null, mess);
//                    }
//                    ((IRetainerRawFood) dto).getRawFoodList().get(i).setQuantity(cantDoub);
//                }
//                previousItems += ((IRetainerRawFood) dto).getRawFoodList().size();
                }
            if (inventoryType.equals(InventoryItem.Type.Mixture) || inventoryType.equals(InventoryItem.Type.Product) || inventoryType.equals(
                    InventoryItem.Type.MenuPlate)) {
//                for (int i = 0; i < ((IRetainerMixture) dto).getMixtureList().size(); i++) {
//                    try {
//                        cantDoub = Double.parseDouble(componentesTextAreaList.get(i + previousItems).getText());
//                    } catch (NumberFormatException ex) {
//                        cantDoub = 0.0;
//                        String mess = ex.getMessage();
//                        JOptionPane.showMessageDialog(null, mess);
//                    }
//                    ((IRetainerMixture) dto).getMixtureList().get(i).setQuantity(cantDoub);
//                }
//                previousItems += ((IRetainerMixture) dto).getMixtureList().size();
            }
            if (inventoryType.equals(InventoryItem.Type.Product) || inventoryType.equals(InventoryItem.Type.MenuPlate)) {
                dto.setPrice(precioDoub);
//                for (int i = 0; i < ((IRetainerProduct) dto).getProductList().size(); i++) {
//                    try {
//                        cantDoub = Double.parseDouble(componentesTextAreaList.get(i + previousItems).getText());
//                    } catch (NumberFormatException ex) {
//                        cantDoub = 0.0;
//                        String mess = ex.getMessage();
//                        JOptionPane.showMessageDialog(null, mess);
//                    }
//                    ((IRetainerProduct) dto).getProductList().get(i).setQuantity(cantDoub);
//                }
//                previousItems += ((IRetainerProduct) dto).getProductList().size();
            }
            if (inventoryType.equals(InventoryItem.Type.MenuPlate)) {
//                for (int i = 0; i < ((IRetainerMenuPlate) dto).getMenuPlateList().size(); i++) {
//                    try {
//                        cantDoub = Double.parseDouble(componentesTextAreaList.get(i + previousItems).getText());
//                    } catch (NumberFormatException ex) {
//                        cantDoub = 0.0;
//                        String mess = ex.getMessage();
//                        JOptionPane.showMessageDialog(null, mess);
//                    }
//                    ((IRetainerMenuPlate) dto).getMenuPlateList().get(i).setQuantity(cantDoub);
//                }
//                previousItems += ((IRetainerMenuPlate) dto).getMenuPlateList().size();
            }
            this.consoleFlush();
            consoleAppend(GeneralController.treeToString(dto));
            if (showMesgSys)
                System.out.println("IngresarFrame.loadInActual: " + dto);
        } else {
            String mess = "Ingrese datos Validos";
            JOptionPane.showMessageDialog(null, mess);
        }
        return validData;
    }

    private void cargar() {
        if (isNew) GeneralController.createInventoryItem(dto);
        else GeneralController.updateInventoryItem(dto);
    }

    private void showHideElementsByIsFinal() {
        if (isFinalCheckBox.isSelected()) {
            unidadJLabel.setVisible(true);
            unidadComboBox.setVisible(true);
            typeComboBox.setVisible(true);
            cantidadLabel.setVisible(true);
            cantidadTextArea.setVisible(true);
            precioJLabel.setVisible(true);
            precioTextArea.setVisible(true);
            addIngredButton.setVisible(true);
            addSubProButton.setVisible(true);
            addProducButton.setVisible(true);
            addDeLaCaButton.setVisible(true);
            cargarButton.setBounds(cargarButton_rectangle);
        } else {
            int previousComponents = 0;
            switch (inventoryType) {
                case RawFood:
                    previousComponents = 6;
                    break;
                case Mixture:
                    previousComponents = 6;
                    break;
                case Product:
                    previousComponents = 4;
                    break;
                case MenuPlate:
                    previousComponents = 4;
                    break;
            }
            int dy = 50, x = 250, y = (previousComponents + componentesLabelList.size()) * dy, w = 280, h = 50;
            unidadJLabel.setVisible(false);
            unidadComboBox.setVisible(false);
            typeComboBox.setVisible(false);
            cantidadLabel.setVisible(false);
            cantidadTextArea.setVisible(false);
            precioJLabel.setVisible(false);
            precioTextArea.setVisible(false);
            addIngredButton.setVisible(false);
            addSubProButton.setVisible(false);
            addProducButton.setVisible(false);
            addDeLaCaButton.setVisible(false);
            cargarButton.setBounds(x - 145, y, w + 100, h);
        }
        singleItemPanel.repaint();
    }

    private void createSingleItemPanel() {
        singleItemPanel.setBounds(LeftPanel_Rectangle);
        singleItemPanel.setLayout(null);

        JLabel nombreLabel = new JLabel("Nombre:");
        unidadJLabel = new JLabel("Unidad de Medida:");
        cantidadLabel = new JLabel("Cantidad:");
        switch (inventoryType) {
            case RawFood:
            case Mixture:
                //MeasureController.configureTypeComboBox(typeComboBox);
                //typeComboBox.addActionListener(new TypeActionListener());
                //MeasureController.configureUnitComboBox(IMeasurable.Type.Weight, unidadComboBox);
                //unidadComboBox.addActionListener(new UnidadActionListener());
                //unidadComboBox.setSelectedIndex(0);
                //typeComboBox.setSelectedIndex(0);
                //unidadComboBox.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                //typeComboBox.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                break;
            case Product:
                //typeComboBox.addItem(IMeasurable.Type.Quantity);
                //MeasureController.configureUnitComboBox(IMeasurable.Type.Quantity, unidadComboBox);
                //unidadComboBox.addActionListener(new UnidadActionListener());
                //typeComboBox.setSelectedIndex(0);
                //unidadComboBox.setSelectedIndex(0);
                //unidadComboBox.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                //typeComboBox.actionPerformed(new ActionEvent(this, ActionEvent.ACTION_PERFORMED, null));
                break;
        }
        precioJLabel = new JLabel("Precio:");
        JLabel isFinalLabel = new JLabel("¿Es Utilizable?:");

        int x = 50, y = 100, w = 200, h = 50, dx = 200, dw = 80, dy = 50;
        singleItemPanel.add(atrasButton);
        atrasButton.setBounds(30, 25, 100, 50);
        singleItemPanel.add(borrarButton);
        borrarButton.setBounds(140, 25, 390, 50);
        singleItemPanel.add(nombreLabel);
        nombreLabel.setBounds(x, y, w, h);
        singleItemPanel.add(nombreTextArea);
        nombreTextArea.setBounds(dx + x, y, dw + w, h);
        y += dy;
        singleItemPanel.add(isFinalLabel);
        isFinalLabel.setBounds(x, y, w, h);
        singleItemPanel.add(isFinalCheckBox);
        isFinalCheckBox.setBounds(dx + x, y, dw + w, h);
        y += dy;

        showHideElementsByIsFinal();
        isFinalCheckBox.addChangeListener((ChangeEvent ce) -> {
            showHideElementsByIsFinal();
        });

        if (inventoryType.equals(InventoryItem.Type.RawFood) || inventoryType.equals(InventoryItem.Type.Mixture) || inventoryType.equals(
                InventoryItem.Type.Product)) {
            singleItemPanel.add(unidadJLabel);
            unidadJLabel.setBounds(x, y, w, h);
            singleItemPanel.add(typeComboBox);
            typeComboBox.setBounds(dx + x, y, (dw + w) / 2, h);
            singleItemPanel.add(unidadComboBox);
            unidadComboBox.setBounds(dx + x + (dw + w) / 2, y, (dw + w) / 2, h);
            y += dy;
            singleItemPanel.add(cantidadLabel);
            cantidadLabel.setBounds(x, y, w, h);
            singleItemPanel.add(cantidadTextArea);
            cantidadTextArea.setBounds(dx + x, y, dw + w, h);
            y += dy;
        }
        if (inventoryType.equals(InventoryItem.Type.Product) || inventoryType.equals(InventoryItem.Type.MenuPlate)) {
            singleItemPanel.add(precioJLabel);
            precioJLabel.setBounds(x, y, w, h);
            singleItemPanel.add(precioTextArea);
            precioTextArea.setBounds(dx + x, y, dw + w, h);
            y += dy;
        }
        if (inventoryType.equals(InventoryItem.Type.Mixture) || inventoryType.equals(InventoryItem.Type.Product))
            singleItemPanel.add(addIngredButton);
        if (!inventoryType.equals(InventoryItem.Type.RawFood))
            singleItemPanel.add(addSubProButton);
        if (inventoryType.equals(InventoryItem.Type.Product) || inventoryType.equals(InventoryItem.Type.MenuPlate))
            singleItemPanel.add(addProducButton);
        if (inventoryType.equals(InventoryItem.Type.MenuPlate))
            singleItemPanel.add(addDeLaCaButton);
        singleItemPanel.add(cargarButton);
        moveSingleItemLastButtons();

        singleItemScroll.setBounds(LeftPanel_Rectangle);
        singleItemScroll.setViewportView(singleItemPanel);
        singleItemScroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        singleItemScroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        containerPanel.add(singleItemScroll);
    }

    private void actualizeSingleItemPanel(InventoryItem dto) {
        this.consoleFlush();
        if ("".equals(dto.getName())) consoleAppend("Nuevo " + inventoryType);
        consoleAppend(GeneralController.treeToString(dto));
        for (int i = 0; i < componentesLabelList.size(); i++) {
            singleItemPanel.remove(componentesLabelList.get(i));
            singleItemPanel.remove(componentesTextAreaList.get(i));
        }
        componentesLabelList.clear();
        componentesTextAreaList.clear();
        switch (inventoryType) {
            case RawFood:
                break;
            case Mixture:
                setLabelAndTextArea(subProductoActual.getComposition());
                break;
            case Product:
                setLabelAndTextArea(productoActual.getComposition());
                break;
            case MenuPlate:
                setLabelAndTextArea(deLaCartaActual.getComposition());
                break;
        }
        moveSingleItemLastButtons();
        singleItemScroll.revalidate();
    }

    private void setLabelAndTextArea(Set<InventoryItem_Ingredients> dtoList) {
        String    msg;
        JLabel    aux1;
        JTextArea aux2;
        int       previousComponents = 5, dy = 50;
        switch (inventoryType) {
            case RawFood:
                previousComponents = 5;
                break;
            case Mixture:
                previousComponents = 5;
                break;
            case Product:
                previousComponents = 6;
                break;
            case MenuPlate:
                previousComponents = 5;
                break;
        }
        for (InventoryItem_Ingredients inventoryItem_ingredients : dtoList) {
            InventoryItem item = inventoryItem_ingredients.getComponent();
            msg = " Cont. de " + item.getName() + " en ";
            //msg = msg + item.getUnidadBase();
            aux1 = new JLabel(msg);
            componentesLabelList.add(aux1);
            aux1.setBounds(50, (previousComponents + componentesLabelList.size()) * dy, 200, 50);
            singleItemPanel.add(aux1);
            aux2 = new JTextArea(String.valueOf(round(item.getQuantity())));
            aux2.setBounds(250, (previousComponents + componentesLabelList.size()) * dy, 280, 50);
            componentesTextAreaList.add(aux2);
            singleItemPanel.add(aux2);
        }
    }

    private void moveSingleItemLastButtons() {
        int previousComponents = 0;
        switch (inventoryType) {
            case RawFood:
                previousComponents = 6;
                break;
            case Mixture:
                previousComponents = 6;
                break;
            case Product:
                previousComponents = 6;
                break;
            case MenuPlate:
                previousComponents = 6;
                break;
        }
        int dy = 50, x = 250, y = (previousComponents + componentesLabelList.size()) * dy, w = 280, h = 50;

        if (inventoryType.equals(InventoryItem.Type.Product)) y += dy;
        if (inventoryType.equals(InventoryItem.Type.MenuPlate)) y -= dy;
        if (inventoryType.equals(InventoryItem.Type.Mixture) || inventoryType.equals(InventoryItem.Type.Product)) {
            addIngredButton.setBounds(x, y, w, h);
            y += dy;
        }
        if (inventoryType.equals(InventoryItem.Type.Mixture) || inventoryType.equals(InventoryItem.Type.Product) || inventoryType.equals(
                InventoryItem.Type.MenuPlate)) {
            addSubProButton.setBounds(x, y, w, h);
            y += dy;
        }
        if (inventoryType.equals(InventoryItem.Type.Product) || inventoryType.equals(InventoryItem.Type.MenuPlate)) {
            addProducButton.setBounds(x, y, w, h);
            y += dy;
        }
        if (inventoryType.equals(InventoryItem.Type.MenuPlate)) {
            addDeLaCaButton.setBounds(x, y, w, h);
            y += dy;
        }
        cargarButton_rectangle = new Rectangle(x - 145, y, w + 100, h);
        cargarButton.setBounds(cargarButton_rectangle);
        y += dy;

        switch (inventoryType) {
            case RawFood:
                singleItemPanel.setPreferredSize(new Dimension(LeftPanel_Rectangle.width,
                                                               (9 + componentesLabelList.size()) * dy));
                break;
            case Mixture:
                singleItemPanel.setPreferredSize(new Dimension(LeftPanel_Rectangle.width,
                                                               (10 + componentesLabelList.size()) * dy));
                break;
            case Product:
                singleItemPanel.setPreferredSize(new Dimension(LeftPanel_Rectangle.width,
                                                               (12 + componentesLabelList.size()) * dy));
                break;
            case MenuPlate:
                singleItemPanel.setPreferredSize(new Dimension(LeftPanel_Rectangle.width,
                                                               (11 + componentesLabelList.size()) * dy));
                break;
        }
        showHideElementsByIsFinal();
        singleItemPanel.repaint();
    }

    private class DeInventarioActionListener implements ActionListener {

        private final InventoryItem dto;

        public DeInventarioActionListener(int i) {
            if (i != 0) {
                switch (inventoryType) {
                    case RawFood:
                        dto = GeneralController.getInventoryItem(ingredienteList.get(i - 1).getSerial());
                        break;
                    case Mixture:
                        dto = GeneralController.getInventoryItem(subProductoList.get(i - 1).getSerial());
                        break;
                    case Product:
                        dto = GeneralController.getInventoryItem(productoList.get(i - 1).getSerial());
                        break;
                    case MenuPlate:
                        dto = GeneralController.getInventoryItem(deLaCartaList.get(i - 1).getSerial());
                        break;
                    default:
                        dto = null;
                        break;
                }
            } else {
                switch (inventoryType) {
                    case RawFood:
                        dto = new InventoryItem();
                        dto.copyFrom(ingredienteActual);
                        dto.setSerial(GeneralController.getAviableID(ingredienteActual.getSerial()));
                        break;
                    case Mixture:
                        dto = new InventoryItem();
                        dto.copyFrom(subProductoActual);
                        dto.setSerial(GeneralController.getAviableID(subProductoActual.getSerial()));
                        break;
                    case Product:
                        dto = new InventoryItem();
                        dto.copyFrom(productoActual);
                        dto.setSerial(GeneralController.getAviableID(productoActual.getSerial()));
                        break;
                    case MenuPlate:
                        dto = new InventoryItem();
                        dto.copyFrom(deLaCartaActual);
                        dto.setSerial(GeneralController.getAviableID(deLaCartaActual.getSerial()));
                        break;
                    default:
                        dto = null;
                        break;
                }
                dto.setName("");
                dto.setFinal_item(true);
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            deInventarioClickExecuter(dto);
        }
    }


    private class AtrasActionListener implements ActionListener {

        InventoryItem dto;

        public AtrasActionListener(InventoryItem.Type clase) {
            switch (clase) {
                case RawFood:
                    dto = ingredienteActual;
                    break;
                case Mixture:
                    dto = subProductoActual;
                    break;
                case Product:
                    dto = productoActual;
                    break;
                case MenuPlate:
                    dto = deLaCartaActual;
                    break;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            InventoryItem father = GeneralController.getFather(dto);
            if (father != null) {
                deInventarioClickExecuter(father);
            }
        }
    }


    private class BorrarActionListener implements ActionListener {

        InventoryItem dto;

        public BorrarActionListener(InventoryItem.Type clase) {
            switch (clase) {
                case RawFood:
                    dto = ingredienteActual;
                    break;
                case Mixture:
                    dto = subProductoActual;
                    break;
                case Product:
                    dto = productoActual;
                    break;
                case MenuPlate:
                    dto = deLaCartaActual;
                    break;
            }
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            int desition = JOptionPane.showConfirmDialog(null,
                                                         "Realmente desea borrar el " + inventoryType + " " + dto.getName() + "?",
                                                         "Borrar " + inventoryType,
                                                         JOptionPane.YES_NO_OPTION,
                                                         JOptionPane.QUESTION_MESSAGE);
            if (desition == JOptionPane.OK_OPTION) {
                String mess = "Los siguientes elementos seran modificados: ";
                mess += GeneralController.getProductUsages(dto);
                mess += "\nDesea Continuar?";
                desition = JOptionPane.showConfirmDialog(null,
                                                         mess,
                                                         "Borrar " + inventoryType,
                                                         JOptionPane.YES_NO_OPTION,
                                                         JOptionPane.QUESTION_MESSAGE);
                if (desition == JOptionPane.OK_OPTION) {
                    JOptionPane.showMessageDialog(null, "Borrado");
                    GeneralController.delete(dto);
                    exit_hide();
                }
            }
        }
    }


    private class IngredienteAddActionListener implements ActionListener {

        private final InventoryItem ingredienteDTO;

        public IngredienteAddActionListener(InventoryItem ingredienteDTO) {
            this.ingredienteDTO = ingredienteDTO;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loadInActual();
            ingredienteVisualSetter.startframe(ingredienteDTO);
            actualizeSingleItemPanel(ingredienteDTO);
        }
    }


    private class SubProductoAddActionListener implements ActionListener {

        private final InventoryItem subProductoDTO;

        public SubProductoAddActionListener(InventoryItem subProductoDTO) {
            this.subProductoDTO = subProductoDTO;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loadInActual();
            subProductoVisualSetter.startframe(subProductoDTO);
            actualizeSingleItemPanel(subProductoDTO);
        }
    }


    private class ProductoAddActionListener implements ActionListener {

        private final InventoryItem productoDTO;

        public ProductoAddActionListener(InventoryItem productoDTO) {
            this.productoDTO = productoDTO;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loadInActual();
            productoVisualSetter.startframe(productoDTO);
            actualizeSingleItemPanel(productoDTO);
        }
    }


    private class DeLaCartaAddActionListener implements ActionListener {

        private final InventoryItem deLaCartaDTO;

        public DeLaCartaAddActionListener(InventoryItem deLaCartaDTO) {
            this.deLaCartaDTO = deLaCartaDTO;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            loadInActual();
            deLaCartaVisualSetter.startframe(deLaCartaDTO);
            actualizeSingleItemPanel(deLaCartaDTO);
        }
    }


    private class CargarActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (loadInActual()) {
                cargar();
                int addLvl = 100;
                switch (inventoryType) {
                    case RawFood:
                        if (ingredienteActual.getSerial() < 100) addLvl = 10;
                        setButtonsListItemPanel(
                                GeneralController.getInventoryItem(ingredienteActual.getSerial() / addLvl));
                        break;
                    case Mixture:
                        if (subProductoActual.getSerial() < 100) addLvl = 10;
                        setButtonsListItemPanel(
                                GeneralController.getInventoryItem(subProductoActual.getSerial() / addLvl));
                        break;
                    case Product:
                        if (productoActual.getSerial() < 100) addLvl = 10;
                        setButtonsListItemPanel(
                                GeneralController.getInventoryItem(productoActual.getSerial() / addLvl));
                        break;
                    case MenuPlate:
                        if (deLaCartaActual.getSerial() < 100) addLvl = 10;
                        setButtonsListItemPanel(
                                GeneralController.getInventoryItem(deLaCartaActual.getSerial() / addLvl));
                        break;
                }
                setVisiblePanel(listItemScroll);
            }
        }
    }


    private class UnidadActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JComboBox cb   = (JComboBox) e.getSource();
            double    cant = 0, cant_ant;
            try {
                cant = Double.parseDouble(cantidadTextArea.getText());
            } catch (NumberFormatException ignored) {
            }
            try {
                switch (inventoryType) {
                    case RawFood:
                        //cant_ant = ingredienteActual.getQuantity(unidadComboBox.getSelectedItem().toString());
                        //cantidadTextArea.setText(String.valueOf(cant_ant));
                        //ingredienteActual.setQuantity(cant_ant, unidadComboBox.getSelectedItem().toString());
                        cantidadLabel.setText("Total Inventariado en " + cb.getSelectedItem());
                        break;
                    case Mixture:
                        //cant_ant = subProductoActual.getQuantity(unidadComboBox.getSelectedItem().toString());
                        //cantidadTextArea.setText(String.valueOf(cant_ant));
                        //subProductoActual.setQuantity(cant_ant, unidadComboBox.getSelectedItem().toString());
                        //cantidadLabel.setText("Total " + IMeasurable.Type.values()[typeComboBox.getSelectedIndex()] + " en " + (String) cb.getSelectedItem());
                        break;
                    case Product:
                        //cant_ant = productoActual.getQuantity(unidadComboBox.getSelectedItem().toString());
                        //cantidadTextArea.setText(String.valueOf(cant_ant));
                        //productoActual.setQuantity(cant_ant, unidadComboBox.getSelectedItem().toString());
                        cantidadLabel.setText("Cantidad Existente en " + cb.getSelectedItem());
                        break;
                    case MenuPlate:
                        //cant_ant = deLaCartaActual.getQuantity(unidadComboBox.getSelectedItem().toString());
                        //cantidadTextArea.setText(String.valueOf(cant_ant));
                        //deLaCartaActual.setQuantity(cant_ant, unidadComboBox.getSelectedItem().toString());
                        cantidadLabel.setText("Cantidad Existente en " + cb.getSelectedItem());
                        break;
                }
            } catch (NullPointerException ignored) {
            }
        }
    }


    private class TypeActionListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
//            MeasureController.configureUnitComboBox(
//                    IMeasurable.Type.values()[typeComboBox.getSelectedIndex()], unidadComboBox);
        }
    }
}
