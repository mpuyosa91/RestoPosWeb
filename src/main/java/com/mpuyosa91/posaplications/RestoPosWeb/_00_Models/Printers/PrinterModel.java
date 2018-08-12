package com.mpuyosa91.posaplications.RestoPosWeb._00_Models.Printers;

import javax.print.*;
import javax.print.attribute.AttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.standard.PrinterName;

import static java.lang.Math.pow;

public class PrinterModel {

    public static boolean showMesgSys = false;
    String commandSet = "";
    int    font       = 0;

    public static boolean feedPrinter(byte[] b, String printerName) {
        try {
            AttributeSet attrSet = new HashPrintServiceAttributeSet(
                    new PrinterName(printerName, null)); //EPSON TM-U220 ReceiptE4

            DocPrintJob job = PrintServiceLookup.lookupPrintServices(null, attrSet)[0].createPrintJob();

            DocFlavor flavor = DocFlavor.BYTE_ARRAY.AUTOSENSE;
            Doc       doc    = new SimpleDoc(b, flavor, null);

            job.print(doc, null);
            if (showMesgSys) System.out.println("Printed !");
        } catch (Exception e) {
            //SurveillanceReport.generic(Thread.currentThread().getStackTrace(), e);
            return false;
        }
        return true;
    }

    public void printAllFonts() {
        for (int i = 0; i < 8; i++) {
            setFont(i, false);
            addLineSeperator();
            newLine();
        }
    }

    public void setFont(int option, boolean bold) {
        int aux = 0;
        font = option;
        final byte[] test = {27, 33, 0};
        if (bold) test[2] += 8;
        aux = (int) pow(2, 0);
        if ((option & aux) == aux) test[2] += 16;
        aux = (int) pow(2, 1);
        if ((option & aux) != aux) test[2] += 1;
        aux = (int) pow(2, 2);
        if ((option & aux) == aux) test[2] += 32;
        commandSet += new String(test);
    }

    public void initialize() {
        final byte[] Init = {27, 64};
        commandSet += new String(Init);
    }

    public void feedBack(byte lines) {
        final byte[] Feed = {27, 101, lines};
        String       s    = new String(Feed);
        commandSet += s;
    }

    public void feed(byte lines) {
        final byte[] Feed = {27, 100, lines};
        String       s    = new String(Feed);
        commandSet += s;
    }

    public void setTextLeft(String text) {
        final byte[] AlignLeft = {27, 97, 48};
        String       s         = new String(AlignLeft);
        commandSet += s + text;
    }

    public void setTextCenter(String text) {
        final byte[] AlignCenter = {27, 97, 49};
        String       s           = new String(AlignCenter);
        commandSet += s + text;
    }

    public void setTextRight(String text) {
        final byte[] AlignRight = {27, 97, 50};
        String       s          = new String(AlignRight);
        commandSet += s + text;
    }

    public void newLine() {
        final byte[] LF = {10};
        String       s  = new String(LF);
        commandSet += s;
    }

    public String reverseColorMode(boolean enabled) {
        final byte[] ReverseModeColorOn  = {29, 66, 1};
        final byte[] ReverseModeColorOff = {29, 66, 0};

        String s = "";
        if (enabled)
            s = new String(ReverseModeColorOn);
        else
            s = new String(ReverseModeColorOff);

        commandSet += s;
        return s;
    }

    public String underLine(int Options) {
        final byte[] UnderLine2Dot = {27, 45, 50};
        final byte[] UnderLine1Dot = {27, 45, 49};
        final byte[] NoUnderLine   = {27, 45, 48};

        String s = "";
        switch (Options) {
            case 0:
                s = new String(NoUnderLine);
                break;

            case 1:
                s = new String(UnderLine1Dot);
                break;

            default:
                s = new String(UnderLine2Dot);
        }
        commandSet += s;
        return s;
    }

    public void finit() {
        final byte[] FeedAndCut = {29, 'V', 66, 0};
        String       s          = new String(FeedAndCut);

        commandSet += s;
    }

    public void finitWithDrawer() {
        final byte[] FeedAndCut = {27, 100, 5, 27, 109};
        String       s          = new String(FeedAndCut);

        final byte[] DrawerKick12 = {27, 112, 0, 55, 27, 112, 1, 55};
        s += new String(DrawerKick12);

        commandSet += s;
    }

    public void finitOnlyDrawer() {
        final byte[] DrawerKick12 = {27, 112, 0, 55, 27, 112, 1, 55};
        String       s            = new String(DrawerKick12);

        commandSet += s;
    }

    public void addLineSeperator() {
        final byte[] AlignLeft = {27, 97, 48};
        String       s         = new String(AlignLeft);
        commandSet += s;
        addLineSeparator(getLineSize());
    }

    public int getLineSize() {
        return getLineSize(font);
    }

    public void resetAll() {
        commandSet = "";
    }

    public String finalCommandSet() {
        return commandSet;
    }

    private void addLineSeparator(int lineSize) {
        StringBuilder lineSpace = new StringBuilder();
        for (int i = 0; i < lineSize; i++) {
            lineSpace.append("-");
        }
        commandSet += lineSpace;
    }

    private int getLineSize(int font) {
        int r = 22, aux;
        aux = (int) pow(2, 1);
        if ((font & aux) != aux) r += 6;
        aux = (int) pow(2, 2);
        if ((font & aux) != aux) r *= 2;
        return r;
    }

    //TODO: crear los fonts como enum
    /*enum fonts {

    }*/

}
