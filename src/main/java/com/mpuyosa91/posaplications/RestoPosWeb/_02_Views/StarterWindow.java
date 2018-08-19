package com.mpuyosa91.posaplications.RestoPosWeb._02_Views;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Entities.Site;
import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.SettingsAndProperties.LocalSettings;
import com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place.GeneralController;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views._0_Configuracion.CreateSiteForm;
import com.mpuyosa91.posaplications.RestoPosWeb._02_Views._1_Pedidos.Mesas.PedidosFrame;

import javax.swing.*;
import java.awt.*;
import java.util.UUID;

public class StarterWindow implements Runnable {

    @Override
    public void run() {
        boolean started = false;
        do {
            try {
                for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                    if ("Nimbus".equals(info.getName())) {
                        javax.swing.UIManager.setLookAndFeel(info.getClassName());
                        started = true;
                        break;
                    }
                }
            } catch (ClassNotFoundException |
                    InstantiationException |
                    IllegalAccessException |
                    javax.swing.UnsupportedLookAndFeelException ex) {
                String mess = "Ha Ocurrido un error Iniciando el programa: \n";
                mess += "Error: " + ex.getMessage();
                JOptionPane.showMessageDialog(null, mess);
                started = false;
                java.util.logging.Logger.getLogger(
                        PedidosFrame.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
            }
        } while (!started);

        while (!GeneralController.springReady) {
        }

        Site site = null;
        site = searchSite();

        if (site != null) {
            MainFrame window = new MainFrame();
            window.repaint();
        } else {
            createSite();
        }

    }

    private Site searchSite() {
        LocalSettings localSettings = new LocalSettings();
        UUID          site_id;
        Site          site;
        try {
            site_id = UUID.fromString(localSettings.getProperty("site_id"));
            site = GeneralController.getSite(site_id);
        } catch (NullPointerException e) {
            System.out.println("Creation Failed:\n" + e);
            site_id = null;
            site = null;
        }

        if (site == null) System.out.println("NO_SITE");

        return site;
    }

    private void createSite() {
        JFrame jFrame = new JFrame("Create Site");
        jFrame.setContentPane(new CreateSiteForm().getPanel1());
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        jFrame.pack();
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jFrame.setLocation(
                dim.width / 2 - jFrame.getSize().width / 2,
                dim.height / 2 - jFrame.getSize().height / 2);
        jFrame.setVisible(true);
    }
}
