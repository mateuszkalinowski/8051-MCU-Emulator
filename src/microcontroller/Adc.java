package microcontroller;

import core.Main;

public class Adc {

    public void updateState() {

        boolean cs = !Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get(Main.settingsMap.get("przetwornikADCCS")));
        boolean wr = !Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get(Main.settingsMap.get("przetwornikADCWR")));
        boolean rd = !Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get(Main.settingsMap.get("przetwornikADCRD")));

        String port = Main.settingsMap.get("przetwornikADCPort");

        if(Main.cpu.getTimePassed()-conversionStart>7) {
            int wynik = (int)Math.round(analogInput/5.0*255);
            StringBuilder wynikString = new StringBuilder(Integer.toBinaryString(wynik));
            while(wynikString.length()<8) {
                wynikString.insert(0, "0");
            }
            digitalOutput = wynikString.toString();
            if(cs) {
                Main.board.setGround("P3.3",2);
            }
        }

        if(cs) {
            if(rd) {
                for(int i = 0; i < 8;i++) {
                    if(digitalOutput.charAt(7-i)=='1')
                        Main.board.setCurrent(port+ "." + i,2);
                    else
                        Main.board.setGround(port+"." + i,2);
                }
            }
            if(!rd) {
                Main.board.setCurrent(port+".0",2);
                Main.board.setCurrent(port+".1",2);
                Main.board.setCurrent(port+".2",2);
                Main.board.setCurrent(port+".3",2);
                Main.board.setCurrent(port+".4",2);
                Main.board.setCurrent(port+".5",2);
                Main.board.setCurrent(port+".6",2);
                Main.board.setCurrent(port+".7",2);
            }
            if(!lastWR && wr) {
                conversionStart = Main.cpu.getTimePassed();
                analogInput = Main.stage.dcPowerSupplyPane.current;
                Main.board.setCurrent("P3.3",2);
            }
        }

        if(!cs) {
            Main.board.setCurrent("P3.3",2);
            Main.board.setCurrent(port+".0",2);
            Main.board.setCurrent(port+".1",2);
            Main.board.setCurrent(port+".2",2);
            Main.board.setCurrent(port+".3",2);
            Main.board.setCurrent(port+".4",2);
            Main.board.setCurrent(port+".5",2);
            Main.board.setCurrent(port+".6",2);
            Main.board.setCurrent(port+".7",2);
        }
    }
    public void cycle() {
        lastWR = !Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get(Main.settingsMap.get("przetwornikADCWR")));
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

    private long conversionStart = 0;
}
