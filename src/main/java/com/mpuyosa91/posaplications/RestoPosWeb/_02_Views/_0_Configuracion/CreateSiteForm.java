package com.mpuyosa91.posaplications.RestoPosWeb._02_Views._0_Configuracion;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;

import javax.swing.*;

public class CreateSiteForm {

    private JPanel     panel1;
    private JButton    createButton;
    private JTextField nitJTextField;
    private JTextField legalAddressJTextField;
    private JTextField businessNameJTextField;
    private JTextField tradeNameJTextField;

    public CreateSiteForm() {
        createButton.addActionListener(e -> {
            Site site = new Site();
            site.setNit(nitJTextField.getText());
            site.setBilling_name(businessNameJTextField.getText());
            site.setTradeName(tradeNameJTextField.getText());
            site.setLegalAddress(legalAddressJTextField.getText());

            GeneralController.createSite(site);

            createButton.setEnabled(false);
            nitJTextField.setEnabled(false);
            legalAddressJTextField.setEnabled(false);
            businessNameJTextField.setEnabled(false);
            tradeNameJTextField.setEnabled(false);
            JOptionPane.showMessageDialog(
                    new JFrame(),
                    "Sitio Creado. Por favor, cierre y vuelva a abrir la aplicacion.",
                    "",
                    JOptionPane.INFORMATION_MESSAGE);
        });
    }

    public JPanel getPanel1() {
        return panel1;
    }
}
