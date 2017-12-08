package components;

import core.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
/**
 * Created by Mateusz on 24.04.2017.
 * Project 8051 MCU Emulator
 */
public class Memory {
    public Memory(){
        mainMemory = new char[256][8];
        for(int i = 0; i < 256; i++) {
            mainMemory[i] = "00000000".toCharArray();
        }

        portsAddresses = new ArrayList<>();
        portsAddresses.add("10000000");//P0
        portsAddresses.add("10010000");//P1
        portsAddresses.add("10100000");//P2
        portsAddresses.add("10110000");//P3
        portsAddresses.add("11101000");//P4
        portsAddresses.add("11111000");//P5;

        memoryCellsNames.put("R0",0);
        memoryCellsNames.put("R1",1);
        memoryCellsNames.put("R2",2);
        memoryCellsNames.put("R3",3);
        memoryCellsNames.put("R4",4);
        memoryCellsNames.put("R5",5);
        memoryCellsNames.put("R6",6);
        memoryCellsNames.put("R7",7);
        memoryCellsNames.put("A",224);
        memoryCellsNames.put("B",240);
        memoryCellsNames.put("P0",128);
        memoryCellsNames.put("SP",129);
        memoryCellsNames.put("P1",144);
        memoryCellsNames.put("P2",160);
        memoryCellsNames.put("P3",176);
        memoryCellsNames.put("TCON",136);
        memoryCellsNames.put("TMOD",137);
        memoryCellsNames.put("TL0",138);
        memoryCellsNames.put("TL1",139);
        memoryCellsNames.put("TH0",140);
        memoryCellsNames.put("TH1",141);
        memoryCellsNames.put("IE",168);
        memoryCellsNames.put("DPL",130);
        memoryCellsNames.put("DPH",131);
        memoryCellsNames.put("P4",232);
        memoryCellsNames.put("P5",248);



        put("P0",255);
        put("P1",255);
        put("P2",255);
        put("P3",255);

        put("P4",255);
        put("P5",255);

        put("SP",7);

        latcherP0 = "11111111";
        latcherP1 = "11111111";
        latcherP2 = "11111111";
        latcherP3 = "11111111";

        latcherP4 = "11111111";
        latcherP5 = "11111111";

        bitAddressableBegginings = new int[]{128,136,144,152,160,168,176,184,208,224,232,240,248};
    }

    public int getFromLatch(int adres) {
        if(adres == 128)
            return Integer.parseInt(latcherP0,2);
        if(adres == 144)
            return Integer.parseInt(latcherP1,2);
        if(adres == 160)
            return Integer.parseInt(latcherP2,2);
        if(adres == 176)
            return Integer.parseInt(latcherP3,2);
        if(adres == 232)
            return Integer.parseInt(latcherP4,2);
        if(adres == 248)
            return Integer.parseInt(latcherP5,2);
        else
            return -1;
    }

    public boolean getBit(String address) throws NoSuchElementException {
        try {
                int numer = Integer.parseInt(address, 2);
                if (numer >=0 && numer <=127) {

                    int bajt = numer / 8 + 32;
                    int bit = numer % 8;
                    bit = 7 - bit;
                    return mainMemory[bajt][bit] == '1';
                }
                else {
                    for (int bitAddressableBeggining : bitAddressableBegginings) {
                        if (numer - bitAddressableBeggining >= 0 && numer - bitAddressableBeggining <= 7) {
                            int index = numer - bitAddressableBeggining;
                            index = 7 - index;
                            return mainMemory[bitAddressableBeggining][index] == '1';
                        }
                    }
                    throw new NoSuchElementException();
                }

        }
        catch (Exception e) {
            throw new NoSuchElementException();
        }
    }

    public void setBitExternal(String address,boolean value) throws NoSuchElementException {
        try {
            int numer = Integer.parseInt(address, 2);
            if (numer >=0 && numer <=127) {

                int bajt = numer / 8 + 32;
                int bit = numer % 8;
                bit = 7 - bit;
                if(value)
                    mainMemory[bajt][bit] = '1';
                else
                    mainMemory[bajt][bit] = '0';
            }
            else {
                for (int bitAddressableBeggining : bitAddressableBegginings) {
                    if (numer - bitAddressableBeggining >= 0 && numer - bitAddressableBeggining <= 7) {
                        int index = numer - bitAddressableBeggining;
                        index = 7 - index;
                        if (value) {
                            mainMemory[bitAddressableBeggining][index] = '1';
                            return;
                        } else {
                            mainMemory[bitAddressableBeggining][index] = '0';
                            return;
                        }
                    }
                }
                throw new NoSuchElementException();
            }

        }
        catch (Exception e) {
            throw new NoSuchElementException();
        }
    }


    public void setBit(String address,boolean value) throws NoSuchElementException {
        try {
            int numer = Integer.parseInt(address, 2);
            if (numer >=0 && numer <=127) {

                int bajt = numer / 8 + 32;
                int bit = numer % 8;
                bit = 7 - bit;
                if(value)
                    mainMemory[bajt][bit] = '1';
                else
                    mainMemory[bajt][bit] = '0';
            }
            else {
                for (int bitAddressableBeggining : bitAddressableBegginings) {
                    if (numer - bitAddressableBeggining >= 0 && numer - bitAddressableBeggining <= 7) {
                        int index = numer - bitAddressableBeggining;
                        index = 7 - index;
                        if (value) {
                            if (bitAddressableBeggining == 128) {
                                char latcherTemp[] = latcherP0.toCharArray();
                                latcherTemp[index] = '1';
                                latcherP0 = String.valueOf(latcherTemp);
                                if (Main.board.getState("P0." + (7 - index)).equals("1")) {
                                    mainMemory[bitAddressableBeggining][index] = '1';
                                } else {
                                    mainMemory[bitAddressableBeggining][index] = '0';
                                }
                            } else if (bitAddressableBeggining == 144) {
                                char latcherTemp[] = latcherP1.toCharArray();
                                latcherTemp[index] = '1';
                                latcherP1 = String.valueOf(latcherTemp);
                                if (Main.board.getState("P1." + (7 - index)).equals("1")) {
                                    mainMemory[bitAddressableBeggining][index] = '1';
                                } else {
                                    mainMemory[bitAddressableBeggining][index] = '0';
                                }
                            } else if (bitAddressableBeggining == 160) {
                                char latcherTemp[] = latcherP2.toCharArray();
                                latcherTemp[index] = '1';
                                latcherP2 = String.valueOf(latcherTemp);
                                if (Main.board.getState("P2." + (7 - index)).equals("1")) {
                                    mainMemory[bitAddressableBeggining][index] = '1';
                                } else {
                                    mainMemory[bitAddressableBeggining][index] = '0';
                                }
                            } else if (bitAddressableBeggining == 176) {
                                char latcherTemp[] = latcherP3.toCharArray();
                                latcherTemp[index] = '1';
                                latcherP3 = String.valueOf(latcherTemp);
                                if (Main.board.getState("P3." + (7 - index)).equals("1")) {
                                    mainMemory[bitAddressableBeggining][index] = '1';
                                } else {
                                    mainMemory[bitAddressableBeggining][index] = '0';
                                }
                            } else if (bitAddressableBeggining == 232) {
                                char latcherTemp[] = latcherP4.toCharArray();
                                latcherTemp[index] = '1';
                                latcherP4 = String.valueOf(latcherTemp);
                                if (Main.board.getState("P4." + (7 - index)).equals("1")) {
                                    mainMemory[bitAddressableBeggining][index] = '1';
                                } else {
                                    mainMemory[bitAddressableBeggining][index] = '0';
                                }
                            } else if (bitAddressableBeggining == 248) {
                                char latcherTemp[] = latcherP5.toCharArray();
                                latcherTemp[index] = '1';
                                latcherP5 = String.valueOf(latcherTemp);
                                if (Main.board.getState("P5." + (7 - index)).equals("1")) {
                                    mainMemory[bitAddressableBeggining][index] = '1';
                                } else {
                                    mainMemory[bitAddressableBeggining][index] = '0';
                                }
                            }else
                                mainMemory[bitAddressableBeggining][index] = '1';
                            return;
                        } else {
                            if (bitAddressableBeggining == 128) {
                                char latcherTemp[] = latcherP0.toCharArray();
                                latcherTemp[index] = '0';
                                latcherP0 = String.valueOf(latcherTemp);
                            }
                            if (bitAddressableBeggining == 144) {
                                char latcherTemp[] = latcherP1.toCharArray();
                                latcherTemp[index] = '0';
                                latcherP1 = String.valueOf(latcherTemp);
                            }
                            if (bitAddressableBeggining == 160) {
                                char latcherTemp[] = latcherP2.toCharArray();
                                latcherTemp[index] = '0';
                                latcherP2 = String.valueOf(latcherTemp);
                            }
                            if (bitAddressableBeggining == 176) {
                                char latcherTemp[] = latcherP3.toCharArray();
                                latcherTemp[index] = '0';
                                latcherP3 = String.valueOf(latcherTemp);
                            }

                            if (bitAddressableBeggining == 232) {
                                char latcherTemp[] = latcherP4.toCharArray();
                                latcherTemp[index] = '0';
                                latcherP4 = String.valueOf(latcherTemp);
                            }

                            if (bitAddressableBeggining == 248) {
                                char latcherTemp[] = latcherP5.toCharArray();
                                latcherTemp[index] = '0';
                                latcherP5 = String.valueOf(latcherTemp);
                            }
                            mainMemory[bitAddressableBeggining][index] = '0';
                            return;
                        }
                    }
                }
                throw new NoSuchElementException();
            }

        }
        catch (Exception e) {
            throw new NoSuchElementException();
        }
    }


   /*String get8BitAddress(String label) throws NoSuchElementException{
        try {
            if(!memoryCellsNames.containsKey(label))
             throw new NoSuchElementException();
            int wartosc = memoryCellsNames.get(label);
            StringBuilder result = new StringBuilder(Integer.toBinaryString(wartosc));

            for (int i = result.length(); i < 8; i++) {
                result.insert(0, "0");
            }
            return result.toString();
        }
        catch (Exception e) {
            throw new NoSuchElementException();
        }
    }*/

    public void put(String label,int wartosc) {
            int index = memoryCellsNames.get(label);
            String binaryValue = Integer.toBinaryString(wartosc);
            char[] toPut = { '0', '0', '0', '0', '0', '0', '0', '0' };
            int i = 7;
            int j = binaryValue.length() - 1;

            while (j >= 0) {
                toPut[i] = binaryValue.charAt(j);
                i--;
                j--;
            }

        if (index == 128) {
            latcherP0 = String.valueOf(toPut);
            String port0;
            try { port0 = Main.board.getPortState("P0"); }
            catch (Exception e) {
                port0 = "11111111";
            }
            for (int k = 0; k < 8; k++) {
                if (latcherP0.charAt(k) == '1' && port0.charAt(k) == '1')
                    toPut[k] = '1';
                else
                    toPut[k] = '0';
            }
        }

        if (index == 144) {
            latcherP1 = String.valueOf(toPut);
            String port1;
            try { port1 = Main.board.getPortState("P1"); }
            catch (Exception e) {
                port1 = "11111111";
            }
            for (int k = 0; k < 8; k++) {
                if (latcherP1.charAt(k) == '1' && port1.charAt(k) == '1')
                    toPut[k] = '1';
                else
                    toPut[k] = '0';
            }
        }

        if (index == 160) {
            latcherP2 = String.valueOf(toPut);
            String port2;
            try { port2 = Main.board.getPortState("P2"); }
            catch (Exception e) {
                port2 = "11111111";
            }
            for (int k = 0; k < 8; k++) {
                if (latcherP2.charAt(k) == '1' && port2.charAt(k) == '1')
                    toPut[k] = '1';
                else
                    toPut[k] = '0';
            }
        }
        if (index == 176) {
            latcherP3 = String.valueOf(toPut);
            String port3;
            try { port3 = Main.board.getPortState("P3"); }
            catch (Exception e) {
                port3 = "11111111";
            }
            for (int k = 0; k < 8; k++) {
                if (latcherP3.charAt(k) == '1' && port3.charAt(k) == '1')
                    toPut[k] = '1';
                else
                    toPut[k] = '0';
            }
        }
        if (index == 232) {
            latcherP4 = String.valueOf(toPut);
            String port4;
            try { port4 = Main.board.getPortState("P4"); }
            catch (Exception e) {
                port4 = "11111111";
            }
            for (int k = 0; k < 8; k++) {
                if (latcherP4.charAt(k) == '1' && port4.charAt(k) == '1')
                    toPut[k] = '1';
                else
                    toPut[k] = '0';
            }
        }

        if (index == 248) {
            latcherP5 = String.valueOf(toPut);
            String port5;
            try { port5 = Main.board.getPortState("P5"); }
            catch (Exception e) {
                port5 = "11111111";
            }
            for (int k = 0; k < 8; k++) {
                if (latcherP5.charAt(k) == '1' && port5.charAt(k) == '1')
                    toPut[k] = '1';
                else
                    toPut[k] = '0';
            }
        }


            mainMemory[index] = toPut;


    }
    public void put(int index,int wartosc) {
        String binaryValue = Integer.toBinaryString(wartosc);
        char[] toPut = {'0','0','0','0','0','0','0','0'};
        int i = 7;
        int j = binaryValue.length()-1;

        while(j>=0) {
            toPut[i] = binaryValue.charAt(j);
            i--;
            j--;
        }

        if (index == 128) {
            latcherP0 = String.valueOf(toPut);
            String port0;
            try { port0 = Main.board.getPortState("P0"); }
            catch (Exception e) {
                port0 = "11111111";
            }
            for (int k = 0; k < 8; k++) {
                if (latcherP0.charAt(k) == '1' && port0.charAt(k) == '1')
                    toPut[k] = '1';
                else
                    toPut[k] = '0';
            }
        }

        if (index == 144) {
            latcherP1 = String.valueOf(toPut);
            String port1;
            try { port1 = Main.board.getPortState("P1"); }
            catch (Exception e) {
                port1 = "11111111";
            }
            for (int k = 0; k < 8; k++) {
                if (latcherP1.charAt(k) == '1' && port1.charAt(k) == '1')
                    toPut[k] = '1';
                else
                    toPut[k] = '0';
            }
        }

        if (index == 160) {
            latcherP2 = String.valueOf(toPut);
            String port2;
            try { port2 = Main.board.getPortState("P2"); }
            catch (Exception e) {
                port2 = "11111111";
            }
            for (int k = 0; k < 8; k++) {
                if (latcherP2.charAt(k) == '1' && port2.charAt(k) == '1')
                    toPut[k] = '1';
                else
                    toPut[k] = '0';
            }
        }
        if (index == 176) {
            latcherP3 = String.valueOf(toPut);
            String port3;
            try { port3 = Main.board.getPortState("P3"); }
            catch (Exception e) {
                port3 = "11111111";
            }
            for (int k = 0; k < 8; k++) {
                if (latcherP3.charAt(k) == '1' && port3.charAt(k) == '1')
                    toPut[k] = '1';
                else
                    toPut[k] = '0';
            }
        }
        if (index == 232) {
            latcherP4 = String.valueOf(toPut);
            String port4;
            try { port4 = Main.board.getPortState("P4"); }
            catch (Exception e) {
                port4 = "11111111";
            }
            for (int k = 0; k < 8; k++) {
                if (latcherP4.charAt(k) == '1' && port4.charAt(k) == '1')
                    toPut[k] = '1';
                else
                    toPut[k] = '0';
            }
        }

        if (index == 248) {
            latcherP5 = String.valueOf(toPut);
            String port5;
            try { port5 = Main.board.getPortState("P5"); }
            catch (Exception e) {
                port5 = "11111111";
            }
            for (int k = 0; k < 8; k++) {
                if (latcherP5.charAt(k) == '1' && port5.charAt(k) == '1')
                    toPut[k] = '1';
                else
                    toPut[k] = '0';
            }
        }
        mainMemory[index] = toPut;
    }

    public void putDirect(String number,int wartosc) throws NumberFormatException{
        try {
            int index = Integer.parseInt(number,2);
            String binaryValue = Integer.toBinaryString(wartosc);
            char[] toPut = {'0','0','0','0','0','0','0','0'};
            int i = 7;
            int j = binaryValue.length()-1;

            while(j>=0) {
                toPut[i] = binaryValue.charAt(j);
                i--;
                j--;
            }

            if (index == 128) {
                latcherP0 = String.valueOf(toPut);
                String port0;
                try { port0 = Main.board.getPortState("P0"); }
                catch (Exception e) {
                    port0 = "11111111";
                }
                for (int k = 0; k < 8; k++) {
                    if (latcherP0.charAt(k) == '1' && port0.charAt(k) == '1')
                        toPut[k] = '1';
                    else
                        toPut[k] = '0';
                }
            }

            if (index == 144) {
                latcherP1 = String.valueOf(toPut);
                String port1;
                try { port1 = Main.board.getPortState("P1"); }
                catch (Exception e) {
                    port1 = "11111111";
                }
                for (int k = 0; k < 8; k++) {
                    if (latcherP1.charAt(k) == '1' && port1.charAt(k) == '1')
                        toPut[k] = '1';
                    else
                        toPut[k] = '0';
                }
            }

            if (index == 160) {
                latcherP2 = String.valueOf(toPut);
                String port2;
                try { port2 = Main.board.getPortState("P2"); }
                catch (Exception e) {
                    port2 = "11111111";
                }
                for (int k = 0; k < 8; k++) {
                    if (latcherP2.charAt(k) == '1' && port2.charAt(k) == '1')
                        toPut[k] = '1';
                    else
                        toPut[k] = '0';
                }
            }
            if (index == 176) {
                latcherP3 = String.valueOf(toPut);
                String port3;
                try { port3 = Main.board.getPortState("P3"); }
                catch (Exception e) {
                    port3 = "11111111";
                }
                for (int k = 0; k < 8; k++) {
                    if (latcherP3.charAt(k) == '1' && port3.charAt(k) == '1')
                        toPut[k] = '1';
                    else
                        toPut[k] = '0';
                }
            }

            if (index == 232) {
                latcherP4 = String.valueOf(toPut);
                String port4;
                try { port4 = Main.board.getPortState("P4"); }
                catch (Exception e) {
                    port4 = "11111111";
                }
                for (int k = 0; k < 8; k++) {
                    if (latcherP4.charAt(k) == '1' && port4.charAt(k) == '1')
                        toPut[k] = '1';
                    else
                        toPut[k] = '0';
                }
            }

            if (index == 248) {
                latcherP5 = String.valueOf(toPut);
                String port5;
                try { port5 = Main.board.getPortState("P5"); }
                catch (Exception e) {
                    port5 = "11111111";
                }
                for (int k = 0; k < 8; k++) {
                    if (latcherP5.charAt(k) == '1' && port5.charAt(k) == '1')
                        toPut[k] = '1';
                    else
                        toPut[k] = '0';
                }
            }

            mainMemory[index] = toPut;

        }catch (Exception e) {
            throw new NumberFormatException();
        }
    }

    public int get(String label) {
        int index = memoryCellsNames.get(label);
        return Integer.parseInt(String.valueOf(mainMemory[index]),2);
    }
    public int getDirect(String number) throws NumberFormatException {
        int index;
        try {
            index = Integer.parseInt(number,2);
        } catch (Exception e) {
            throw new NumberFormatException();
        }
        return Integer.parseInt(String.valueOf(mainMemory[index]),2);

    }


    public int get(int wartosc) {
        return Integer.parseInt(String.valueOf(mainMemory[wartosc]),2);
    }

    public Map<String,Integer> memoryCellsNames = new HashMap<>();

    private int[] bitAddressableBegginings;


    private char[][] mainMemory;

    public String latcherP0;
    public String latcherP1;
    public String latcherP2;
    public String latcherP3;
    public String latcherP4;
    public String latcherP5;

    public ArrayList<String> portsAddresses;

}
