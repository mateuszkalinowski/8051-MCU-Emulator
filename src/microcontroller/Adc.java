package microcontroller;

import core.Main;

public class Adc {

    public void updateState() {

        boolean cs = !Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("P1.5"));
        boolean wr = !Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("P1.4"));
        boolean rd = !Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("P1.3"));

        if(conversionTime==-1) {
            int wynik = (int)Math.round(analogInput/5.0*255);
            String wynikString = Integer.toBinaryString(wynik);
            while(wynikString.length()<8) {
                wynikString = "0" + wynikString;
            }
            digitalOutput = wynikString;
            if(cs) {
                Main.board.setGround("P3.3",2);
            }
        }

        if(cs) {
            if(rd) {
                for(int i = 0; i < 8;i++) {
                    if(digitalOutput.charAt(7-i)=='1')
                        Main.board.setCurrent("P2." + i,2);
                    else
                        Main.board.setGround("P2." + i,2);
                }
            }
            else {
                Main.board.setCurrent("P2.0",2);
                Main.board.setCurrent("P2.1",2);
                Main.board.setCurrent("P2.2",2);
                Main.board.setCurrent("P2.3",2);
                Main.board.setCurrent("P2.4",2);
                Main.board.setCurrent("P2.5",2);
                Main.board.setCurrent("P2.6",2);
                Main.board.setCurrent("P2.7",2);
            }
            if(!lastWR && wr) {
                conversionTime = 7;
                //TODO
                analogInput = Main.stage.dcPowerSupplyPane.current;
                Main.board.setCurrent("P3.3",2);
            }

        }

        if(!cs) {
            Main.board.setCurrent("P3.3",2);
            Main.board.setCurrent("P2.0",2);
            Main.board.setCurrent("P2.1",2);
            Main.board.setCurrent("P2.2",2);
            Main.board.setCurrent("P2.3",2);
            Main.board.setCurrent("P2.4",2);
            Main.board.setCurrent("P2.5",2);
            Main.board.setCurrent("P2.6",2);
            Main.board.setCurrent("P2.7",2);
        }
    }
    public void cycle() {
        if(conversionTime>=0)
            conversionTime--;
        lastWR = !Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("P1.4"));
    }

    public void reset() {
        lastWR = false;
        digitalOutput = "00000000";
        analogInput = 0;
    }

    //AKTUALNA WARTOSC NA WYJSCIU CYFROWYM
    private String digitalOutput = "00000000";

    //WARTOSC ANALOGOWA ZAPISANA
    private double analogInput = 0;


    private boolean lastWR = false;
    private int conversionTime = -2;
}
