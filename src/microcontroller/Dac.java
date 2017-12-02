package microcontroller;

import core.Main;

public class Dac {

    public static void convert(){
        boolean wr = false;
        boolean cs = false;
        if(Main.settingsMap.get("przetwornikDACWR").equals("GND")) {
            wr = true;
        }
        else if(Main.settingsMap.get("przetwornikDACWR").equals("VCC")) {
            wr = false;
        }
        else {
            wr = !Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get(Main.settingsMap.get("przetwornikDACWR")));
        }
        if(Main.settingsMap.get("przetwornikDACCS").equals("GND")) {
            cs = true;
        }
        else if(Main.settingsMap.get("przetwornikDACCS").equals("VCC")) {
            cs = false;
        }
        else {
            cs = !Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get(Main.settingsMap.get("przetwornikDACCS")));
        }

        if(!cs)
            return;
        else {
            if(!wr)
                return;
            else {
                int wartoscportu;
                if(Main.settingsMap.get("przetwornikDACPort").equals("VCC")) {
                    wartoscportu = 255;
                }
                else if(Main.settingsMap.get("przetwornikDACPort").equals("GND")) {
                    wartoscportu = 0;
                }
                else {
                    wartoscportu = Main.cpu.mainMemory.get(Main.settingsMap.get("przetwornikDACPort"));
                }
                double wynik = 5 * (wartoscportu/255.0);
                value = Double.toString(wynik);
                if(value.length()>5)
                    value = Double.toString(wynik).substring(0,5);

                Main.stage.dacStateLabel.setText("Wyjście DAC: "+value+" V");
            }
        }


    }
    private static String value = "0.000";

    public static String getValue(){
        return value;
    }

    public static void reset(){
        value = "0.000";
        Main.stage.dacStateLabel.setText("Wyjście DAC: "+value+" V");
    }


}
