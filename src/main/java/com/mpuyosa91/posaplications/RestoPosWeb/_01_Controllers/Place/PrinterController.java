package com.mpuyosa91.posaplications.RestoPosWeb._01_Controllers.Place;

import com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Printers.PrinterModel;

import java.util.Calendar;

public abstract class PrinterController {

    public static String billHeader() {
        PrinterModel printer = new PrinterModel();
        printer.setFont(4, true);
        printer.setTextCenter("The Panera");
        printer.newLine();
        printer.setTextCenter("Bakery and Food");
        printer.newLine();
        printer.setFont(2, false);
        printer.setTextCenter("NIT: 15.444.730-9");
        printer.newLine();
        printer.setTextCenter("REGIMEN SIMPLIFICADO");
        printer.newLine();
        printer.setTextCenter("Direccion: Cr.81 #43-19 Local 108");
        printer.newLine();
        printer.setTextCenter("Rionegro, Antioquia");
        printer.newLine();
        printer.setTextCenter("Domicilios: 562 9979");
        printer.newLine();
        return printer.finalCommandSet();
    }

    public static String stringToLeftAndFill(String string, int fillLength, String option) {
        String r;
        if (string.length() > fillLength) {
            switch (option) {
                case "truncate":
                    r = string.substring(0, fillLength - 1);
                    break;
                case "fill":
                    r = String.format("%1$" + fillLength + "s", " ");
                    r = r.replace(" ", "#");
                    break;
                default:
                    r = string.substring(0, fillLength - 1);
                    break;
            }
        } else {
            r = String.format("%1$-" + fillLength + "s", string);
        }
        return r;
    }

    public static String stringToRightAndFill(String string, int fillLength, String option) {
        String r;
        if (string.length() > fillLength) {
            switch (option) {
                case "truncate":
                    r = string.substring(0, fillLength - 1);
                    break;
                case "fill":
                    r = String.format("%1$-" + fillLength + "s", " ");
                    r = r.replace(" ", "#");
                    break;
                default:
                    r = string.substring(0, fillLength - 1);
                    break;
            }
        } else {
            r = String.format("%1$" + fillLength + "s", string);
        }
        return r;
    }

    public static String dateTimeHeader(Calendar date) {
        PrinterModel printerOptions = new PrinterModel();
        String       fecha, hora;
        hora = "Hora: " + hour(date);
        fecha = "Fecha: " + date(date);
        printerOptions.setTextLeft(hora);
        printerOptions.newLine();
        printerOptions.setTextRight(fecha);
        printerOptions.newLine();
        return printerOptions.finalCommandSet();
    }

    public static String hour(Calendar date) {
        String hora = String.valueOf(date.get(Calendar.HOUR)) + ":";
        if (date.get(Calendar.HOUR) == 0) hora = "12:";
        if (date.get(Calendar.MINUTE) < 10)
            hora += "0" + String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        else
            hora += String.valueOf(Calendar.getInstance().get(Calendar.MINUTE));
        if (Calendar.getInstance().get(Calendar.AM_PM) == 1) {
            hora += " p.m.";
        } else hora += " a.m.";
        return hora;
    }

    public static String date(Calendar date) {
        String fecha = String.valueOf(date.get(Calendar.DATE)) + "/" + String.valueOf(date.get(Calendar.MONTH) + 1) + "/" + String.valueOf(
                date.get(Calendar.YEAR));
        return fecha;
    }
}
