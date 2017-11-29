package microcontroller;

import core.Main;

public class Dac7524 {

    public static void convert(){
        boolean wr = false;
        boolean cs = false;
        if(Main.stage.przetwornikDACWR.equals("GND")) {
            wr = true;
        }
        else if(Main.stage.przetwornikDACWR.equals("VCC")) {
            wr = false;
        }
        else {
            wr = !Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get(Main.stage.przetwornikDACWR));
        }
        if(Main.stage.przetwornikDACCS.equals("GND")) {
            cs = true;
        }
        else if(Main.stage.przetwornikDACCS.equals("VCC")) {
            cs = false;
        }
        else {
            cs = !Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get(Main.stage.przetwornikDACCS));
        }

        if(!cs)
            return;
        else {
            if(!wr)
                return;
            else {
                int wartoscportu = Main.cpu.mainMemory.get(Main.stage.przetwornikDACPort);
                double wynik = 5 * (wartoscportu/255.0);
                value = Double.toString(wynik);
                if(value.length()>5)
                    value = Double.toString(wynik).substring(0,5);
            }
        }


    }
    private static String value = "0.000";

    public static String getValue(){
        return value;
    }


}
