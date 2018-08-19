/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mpuyosa91.posaplications.RestoPosWeb._02_Views._1_Pedidos.Mesas;

import javax.swing.*;
import java.awt.*;

/**
 * @author MoisesE
 */
public class PedidoCocinaPanel extends JPanel {

    //ArrayList y JTable
    private JTextArea pedidoCocinaTextArea;

    public PedidoCocinaPanel() {
        super();
        pedidoCocinaTextArea = new JTextArea(27, 55);
        add(pedidoCocinaTextArea);
        pedidoCocinaTextArea.setFont(new Font("Consolas", Font.PLAIN, 13));
        pedidoCocinaTextArea.setEditable(false);
        pedidoCocinaTextArea.setLineWrap(true);         //Ajusta el objeto al tamaño inicial, corta las palabras hasta la proxima linea
        pedidoCocinaTextArea.setWrapStyleWord(true);    //Da enter a la palabra para no cortarla con el ajuste de linea

    }

    public void append(String text) {
        pedidoCocinaTextArea.append(text + "\n");

    }

    public void flush() {
        pedidoCocinaTextArea.setText("");
    }
}
