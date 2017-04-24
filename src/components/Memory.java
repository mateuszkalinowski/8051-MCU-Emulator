package components;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mateusz on 24.04.2017.
 * Project 8051 MCU Emulator
 */
public class Memory {
    public Memory(){
        mainMemory = new char[255][8];
        for(int i = 0; i < 255; i++) {
            mainMemory[i] = "00000000".toCharArray();
        }
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
        memoryCellsNames.put("P1",144);
        memoryCellsNames.put("P2",160);
        memoryCellsNames.put("P3",176);

        put("P0",255);
        put("P1",255);
        put("P2",255);
        put("P3",255);
    }



    public void put(String label,int wartosc) {
        int index = memoryCellsNames.get(label);
        String binaryValue = Integer.toBinaryString(wartosc);
        char[] toPut = {'0','0','0','0','0','0','0','0'};
        int i = 7;
        int j = binaryValue.length()-1;

        while(j>=0) {
            toPut[i] = binaryValue.charAt(j);
            i--;
            j--;
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
        mainMemory[index] = toPut;
    }
    public int get(String label) {
        int index = memoryCellsNames.get(label);
        return Integer.parseInt(String.valueOf(mainMemory[index]),2);
    }

    public int get(int wartosc) {
        return Integer.parseInt(String.valueOf(mainMemory[wartosc]),2);
    }

    private Map<String,Integer> memoryCellsNames = new HashMap<>();

    char[][] mainMemory;
}
