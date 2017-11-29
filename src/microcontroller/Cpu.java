package microcontroller;

import components.CodeMemory;
import components.Memory;
import core.Main;
import exceptions.InstructionException;
import javafx.scene.paint.Color;
import java.util.ArrayList;


/**
 * Created by Mateusz on 18.04.2017.
 * Project MkSim51
 */
public class Cpu {

    public Cpu(){
        resetCpu();
    }

    public void resetCpu(){
        linePointer=0;
        timePassed = 0;
        mainMemory = new Memory();
        interrupts.clear();
        interrupts.add(false);
        interrupts.add(false);
        interrupts.add(false);
        interrupts.add(false);
        interrupts.add(false);
        higherInterrupt=5;
        LastP32 = true;
        LastP33 = true;
    }
    private void machineCycle(){
        timePassed++;
        interrupts.clear();
        interrupts.add(false);
        interrupts.add(false);
        interrupts.add(false);
        interrupts.add(false);
        interrupts.add(false);
        String port0String = expandTo8Digits(Integer.toBinaryString(mainMemory.get("P0")));
        char[] port0Char = port0String.toCharArray();
        System.arraycopy(Main.stage.port0History, 0, Main.stage.port0History, 1, Main.stage.portChartScale - 1);
        Main.stage.port0History[0] = port0Char;

        String port1String = expandTo8Digits(Integer.toBinaryString(mainMemory.get("P1")));
        char[] port1Char = port1String.toCharArray();
        System.arraycopy(Main.stage.port1History, 0, Main.stage.port1History, 1, Main.stage.portChartScale - 1);
        Main.stage.port1History[0] = port1Char;

        String port2String = expandTo8Digits(Integer.toBinaryString(mainMemory.get("P2")));
        char[] port2Char = port2String.toCharArray();
        System.arraycopy(Main.stage.port2History, 0, Main.stage.port2History, 1, Main.stage.portChartScale - 1);
        Main.stage.port2History[0] = port2Char;

        String port3String = expandTo8Digits(Integer.toBinaryString(mainMemory.get("P3")));
        char[] port3Char = port3String.toCharArray();
        System.arraycopy(Main.stage.port3History, 0, Main.stage.port3History, 1, Main.stage.portChartScale - 1);
        Main.stage.port3History[0] = port3Char;

        int tmod = mainMemory.get("TMOD");
        String tmodString = expandTo8Digits(Integer.toBinaryString(tmod));

        if(mainMemory.getBit(codeMemory.bitAddresses.get("TR0")) && tmodString.charAt(4)=='0' && tmodString.charAt(5)=='0') {
            int TL0int = mainMemory.get("TL0");
            int TH0int = mainMemory.get("TH0");
            TL0int+=1;
            if(tmodString.charAt(7) == '0' && tmodString.charAt(6) == '0') {
                if(TL0int>32) {
                    TL0int = 0;
                    TH0int += 1;
                }
                if(TH0int==256) {
                    TH0int=0;
                    mainMemory.setBit(codeMemory.bitAddresses.get("TF0"),true);
                }
            }
            if(tmodString.charAt(7) == '1' && tmodString.charAt(6) == '0') {
                if(TL0int==256) {
                    TL0int = 0;
                    TH0int += 1;
                }
                if(TH0int==256) {
                    TH0int=0;
                    mainMemory.setBit(codeMemory.bitAddresses.get("TF0"),true);
                }
            }
            if(tmodString.charAt(7) == '0' && tmodString.charAt(6) == '1') {
                if(TL0int==256) {
                    TL0int = TH0int;
                    mainMemory.setBit(codeMemory.bitAddresses.get("TF0"),true);
                }
            }

            mainMemory.put("TL0",TL0int);
            mainMemory.put("TH0",TH0int);
        }

        if(mainMemory.getBit(codeMemory.bitAddresses.get("TR1")) && tmodString.charAt(0)=='0' && tmodString.charAt(1)=='0') {
            int TL1int = mainMemory.get("TL1");
            int TH1int = mainMemory.get("TH1");
            TL1int+=1;
            if(tmodString.charAt(3) == '0' && tmodString.charAt(2) == '0') {
                if(TL1int==33) {
                    TL1int = 0;
                    TH1int += 1;
                }
                if(TH1int==256) {
                    TH1int=0;
                    mainMemory.setBit(codeMemory.bitAddresses.get("TF1"),true);
                }
            }
            if(tmodString.charAt(3) == '1' && tmodString.charAt(2) == '0') {
                if(TL1int==256) {
                    TL1int = 0;
                    TH1int += 1;
                }
                if(TH1int==256) {
                    TH1int=0;
                    mainMemory.setBit(codeMemory.bitAddresses.get("TF1"),true);
                }
            }
            if(tmodString.charAt(3) == '0' && tmodString.charAt(2) == '1') {
                if(TL1int==256) {
                    TL1int = TH1int;
                    mainMemory.setBit(codeMemory.bitAddresses.get("TF1"),true);
                }
            }

            mainMemory.put("TL1",TL1int);
            mainMemory.put("TH1",TH1int);
        }


        if(LastP32 && !mainMemory.getBit(codeMemory.bitAddresses.get("P3.2")) && mainMemory.getBit(codeMemory.bitAddresses.get("IT0"))) {
            mainMemory.setBit(codeMemory.bitAddresses.get("IE0"),true);
        }

        if(LastP33 && !mainMemory.getBit(codeMemory.bitAddresses.get("P3.3")) && mainMemory.getBit(codeMemory.bitAddresses.get("IT1"))) {
            mainMemory.setBit(codeMemory.bitAddresses.get("IE1"),true);
        }

        if(!mainMemory.getBit(codeMemory.bitAddresses.get("P3.2")) && !mainMemory.getBit(codeMemory.bitAddresses.get("IT0"))) {
            mainMemory.setBit(codeMemory.bitAddresses.get("IE0"),true);
        }
        if(!mainMemory.getBit(codeMemory.bitAddresses.get("P3.3")) && !mainMemory.getBit(codeMemory.bitAddresses.get("IT1"))) {
            mainMemory.setBit(codeMemory.bitAddresses.get("IE1"),true);
        }

        LastP32 = mainMemory.getBit(codeMemory.bitAddresses.get("P3.2"));
        LastP33 = mainMemory.getBit(codeMemory.bitAddresses.get("P3.3"));



        if(mainMemory.getBit(codeMemory.bitAddresses.get("EA"))) {
            if (mainMemory.getBit(codeMemory.bitAddresses.get("EX0")) && mainMemory.getBit(codeMemory.bitAddresses.get("IE0")) && !mainMemory.getBit(codeMemory.bitAddresses.get("P3.2")) && higherInterrupt>=1)
                interrupts.set(0, true);
            if (mainMemory.getBit(codeMemory.bitAddresses.get("TF0")) && mainMemory.getBit(codeMemory.bitAddresses.get("ET0")) && higherInterrupt>=2)
                interrupts.set(1, true);
            if (mainMemory.getBit(codeMemory.bitAddresses.get("EX1")) && mainMemory.getBit(codeMemory.bitAddresses.get("IE1")) && !mainMemory.getBit(codeMemory.bitAddresses.get("P3.3")) && higherInterrupt>=3)
                interrupts.set(2, true);
            if (mainMemory.getBit(codeMemory.bitAddresses.get("TF1")) && mainMemory.getBit(codeMemory.bitAddresses.get("ET1")) && higherInterrupt>=4)
                interrupts.set(3, true);

        }

    }

    public void executeInstruction() {
        String toExecute = codeMemory.getFromAddress(linePointer);

        //WYKRYWANIE PRZERWAN

        if (interrupts.get(0) && higherInterrupt>=1) {
            int stackPointer = mainMemory.get("SP");
            try {
                String address = codeMemory.make16DigitsStringFromNumber(Integer.toBinaryString(linePointer)+"B");
                stackPointer += 1;
                if (stackPointer == 256)
                    stackPointer = 0;
                mainMemory.put(stackPointer, Integer.parseInt(address.substring(8, 16), 2));
                stackPointer += 1;
                if (stackPointer == 256)
                    stackPointer = 0;
                mainMemory.put(stackPointer, Integer.parseInt(address.substring(0, 8), 2));
                mainMemory.put("SP",stackPointer);
                mainMemory.setBit(codeMemory.bitAddresses.get("IE0"), false);
                interrupts.set(0,false);
                higherInterrupt=0;
                linePointer = 3;
                return;
            } catch (Exception ignored) {
            }
        }
        else if (interrupts.get(1) && higherInterrupt>=2) {
            int stackPointer = mainMemory.get("SP");
            try {
                String address = codeMemory.make16DigitsStringFromNumber(Integer.toBinaryString(linePointer)+"B");
                stackPointer += 1;
                if (stackPointer == 256)
                    stackPointer = 0;
                mainMemory.put(stackPointer, Integer.parseInt(address.substring(8, 16), 2));
                stackPointer += 1;
                if (stackPointer == 256)
                    stackPointer = 0;
                mainMemory.put(stackPointer, Integer.parseInt(address.substring(0, 8), 2));
                mainMemory.put("SP",stackPointer);
                mainMemory.setBit(codeMemory.bitAddresses.get("TF0"), false);
                interrupts.set(1,false);
                higherInterrupt=1;
                linePointer = 11;
                return;
            } catch (Exception ignored) {
            }

        } else if (interrupts.get(2) && higherInterrupt>=3) {
            int stackPointer = mainMemory.get("SP");
            try {
                String address = codeMemory.make16DigitsStringFromNumber(Integer.toBinaryString(linePointer)+"B");
                stackPointer += 1;
                if (stackPointer == 256)
                    stackPointer = 0;
                mainMemory.put(stackPointer, Integer.parseInt(address.substring(8, 16), 2));
                stackPointer += 1;
                if (stackPointer == 256)
                    stackPointer = 0;
                mainMemory.put(stackPointer, Integer.parseInt(address.substring(0, 8), 2));
                mainMemory.put("SP",stackPointer);
                mainMemory.setBit(codeMemory.bitAddresses.get("IE1"), false);
                interrupts.set(2,false);
                higherInterrupt=2;
                linePointer = 19;
                return;
            }   catch (Exception ignored) {
            }
        }
        else if (interrupts.get(3)&& higherInterrupt>=4) {
            int stackPointer = mainMemory.get("SP");
            try {
                String address = codeMemory.make16DigitsStringFromNumber(Integer.toBinaryString(linePointer)+"B");
                stackPointer += 1;
                if (stackPointer == 256)
                    stackPointer = 0;
                mainMemory.put(stackPointer, Integer.parseInt(address.substring(8, 16), 2));
                stackPointer += 1;
                if (stackPointer == 256)
                    stackPointer = 0;
                mainMemory.put(stackPointer, Integer.parseInt(address.substring(0, 8), 2));
                mainMemory.put("SP",stackPointer);
                mainMemory.setBit(codeMemory.bitAddresses.get("TF1"), false);
                interrupts.set(3,false);
                higherInterrupt=3;
                linePointer = 27;
                return;
            } catch (Exception ignored) {
            }
        }
        if(toExecute.equals("00000010")) { //LJMP
            machineCycle();
            machineCycle();
            linePointer = Integer.parseInt(codeMemory.getFromAddress(linePointer+1) + codeMemory.getFromAddress(linePointer+2),2);
        }
        else if(toExecute.equals("00010010")) { //LCALL
            int stackPointer = mainMemory.get("SP");
            try {
                String address = codeMemory.make16DigitsStringFromNumber(Integer.toBinaryString(linePointer+3)+"B");
                stackPointer += 1;
                if (stackPointer == 256)
                    stackPointer = 0;
                mainMemory.put(stackPointer, Integer.parseInt(address.substring(8, 16), 2));
                stackPointer += 1;
                if (stackPointer == 256)
                    stackPointer = 0;
                mainMemory.put(stackPointer, Integer.parseInt(address.substring(0, 8), 2));
                mainMemory.put("SP",stackPointer);
                mainMemory.setBit(codeMemory.bitAddresses.get("TF0"), false);
                interrupts.set(1,false);
                higherInterrupt=1;
                linePointer = Integer.parseInt(codeMemory.getFromAddress(linePointer+1) + codeMemory.getFromAddress(linePointer+2),2);
            } catch (Exception ignored) {
            }
        }
        else if(toExecute.equals("10010011")) {
            String dphString = expandTo8Digits(Integer.toBinaryString(mainMemory.get("DPH")));
            String dplString = expandTo8Digits(Integer.toBinaryString(mainMemory.get("DPL")));
            int wartosc = Integer.parseInt(dphString+dplString,2);
            wartosc+= mainMemory.get("A");
            mainMemory.put("A",Integer.parseInt(codeMemory.getFromAddress(wartosc),2));
            linePointer+=1;
        }
        else if(toExecute.equals("10000011")) {
            linePointer+=1;
            int wartosc = linePointer;
            wartosc+= mainMemory.get("A");
            mainMemory.put("A",Integer.parseInt(codeMemory.getFromAddress(wartosc),2));
        }
        else if(toExecute.equals("00000100")) {//INC A
            machineCycle();
            int wartosc = mainMemory.get("A");
            wartosc+=1;
            if(wartosc==256)
                wartosc=0;
            mainMemory.put("A",wartosc);
            linePointer+=1;
        }

        else if(toExecute.equals("10100011")) {//INC DPTR
            machineCycle();
            machineCycle();
            String number = expandTo8Digits(Integer.toString(mainMemory.get("DPH"),2));
            number = number + expandTo8Digits(Integer.toString(mainMemory.get("DPL"),2));
            int wartosc = Integer.parseInt(number,2);
            wartosc+=1;
            if(wartosc>65535)
                wartosc = 0;

            number = expandTo16Digits(Integer.toBinaryString(wartosc));

            mainMemory.put("DPH",Integer.valueOf(number.substring(0,8),2));
            mainMemory.put("DPL",Integer.valueOf(number.substring(8,16),2));
            linePointer+=1;
        }

        else if(toExecute.substring(0,5).equals("00001")) { //INC Rx
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc = mainMemory.get(rejestrString);
            wartosc+=1;
            if(wartosc==256)
                wartosc=0;
            mainMemory.put(rejestrString,wartosc);
            linePointer+=1;
        }
        else if(toExecute.equals("00000101")) { //INC Direct
            machineCycle();
            int wartosc = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            wartosc+=1;
            if(wartosc==256)
                wartosc=0;
            mainMemory.put(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2),wartosc);
            linePointer+=2;
        }
        else if(toExecute.substring(0,7).equals("0000011")) { //INC Ri
            int wartosc = mainMemory.get(mainMemory.get("R" + toExecute.charAt(7)));
            wartosc+=1;
            if(wartosc==256)
                wartosc=0;
            mainMemory.put(mainMemory.get("R" + toExecute.charAt(7)),wartosc);
            linePointer+=1;
        }
        else if(toExecute.equals("00010100")) {//DEC A
            machineCycle();
            int wartosc = mainMemory.get("A");
            wartosc-=1;
            if(wartosc==-1)
                wartosc=255;
            mainMemory.put("A",wartosc);
            linePointer+=1;
        }
        else if(toExecute.substring(0,5).equals("00011")) { //DEC Rx
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc = mainMemory.get(rejestrString);
            wartosc-=1;
            if(wartosc==-1)
                wartosc=255;
            mainMemory.put(rejestrString,wartosc);
            linePointer+=1;
        }
        else if(toExecute.equals("00010101")) { //DEC Direct
            machineCycle();
            int wartosc = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            wartosc-=1;
            if(wartosc==-1)
                wartosc=255;
            mainMemory.put(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2),wartosc);
            linePointer+=2;
        }
        else if(toExecute.substring(0,7).equals("0001011")) { //DEC Ri
            int wartosc = mainMemory.get(mainMemory.get("R" + toExecute.charAt(7)));
            wartosc-=1;
            if(wartosc==-1)
                wartosc=255;
            mainMemory.put(mainMemory.get("R" + toExecute.charAt(7)),wartosc);
            linePointer+=1;
        }
        else if(toExecute.substring(0,5).equals("11011")) { //DJNZ Rx,label/adres (offset)
            machineCycle();
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc = mainMemory.get(rejestrString);
            wartosc--;
            if(wartosc==-1)
                wartosc=255;

            mainMemory.put(rejestrString,wartosc);
           if(wartosc>0) {
               int wynik = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
               if(wynik>127)
                   wynik = wynik - 256;
               linePointer = linePointer + 1 + 1 + wynik;
           }
            if(wartosc==0) {
                linePointer+=2;
            }
        } else if(toExecute.equals("11010101")) { //DJNZ direct,offset
            machineCycle();
            machineCycle();
            int wartosc = mainMemory.getDirect(codeMemory.getFromAddress(linePointer+1));
            wartosc--;
            if(wartosc==-1)
                wartosc=255;
            mainMemory.putDirect(codeMemory.getFromAddress(linePointer+1),wartosc);

            if(wartosc>0) {
                int wynik = Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
                if(wynik>127)
                    wynik = wynik - 256;
                linePointer = linePointer+1+1+1+wynik;
            }
            if(wartosc==0) {
                linePointer+=3;
            }
        }
        else if(toExecute.equals("10110100")) { //CJNE A,#number,offset
            machineCycle();
            machineCycle();
            int wartosc = mainMemory.get("A");
            int wartosc2 = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            if(wartosc!=wartosc2) {
                int wynik = linePointer+1+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
            }
            else {
                linePointer+=3;
            }
            if(wartosc<wartosc2) {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"), true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"), false);
            }

        }
        else if(toExecute.equals("10110101")) { //CJNE A,direct,offset
            machineCycle();
            machineCycle();
            int wartosc = mainMemory.get("A");
            int wartosc2 = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer + 1),2));
            if(wartosc!=wartosc2) {
                int wynik = linePointer+1+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
            }
            else {
                linePointer+=3;
            }
            if(wartosc<wartosc2) {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"), true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"), false);
            }

        }
        else if(toExecute.substring(0,7).equals("1011011")) { //CJNE @Ri,#number,offset
            machineCycle();
            machineCycle();
            String rejestrString = "R" + toExecute.charAt(7);
            int wartosc = mainMemory.get(mainMemory.get(rejestrString));
            int wartosc2 = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            if(wartosc!=wartosc2) {
                int wynik = linePointer+1+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
            }
            else {
                linePointer+=3;
            }
            if(wartosc<wartosc2) {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"), true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"), false);
            }

        }
        else if(toExecute.substring(0,5).equals("10111")) { //CJNE Rn,#number,offset
            machineCycle();
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc = mainMemory.get(rejestrString);
            int wartosc2 = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            if(wartosc!=wartosc2) {
                int wynik = linePointer+1+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
            }
            else {
                linePointer+=3;
            }
            if(wartosc<wartosc2) {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"), true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"), false);
            }

        }
        /*
            ANL:
                wszystko bez Ri
         */
        else if(toExecute.equals("01010100")) { //ANL a,#01h
            machineCycle();
            int wartosc = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            int wynik = wartosc & mainMemory.get("A");
            mainMemory.put("A",wynik);
            linePointer+=2;
        }
        else if(toExecute.substring(0,5).equals("01011")) {
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc1 = mainMemory.get(rejestrString);
            int wartosc2 = mainMemory.get("A");
            int wynik = wartosc1&wartosc2;
            mainMemory.put("A",wynik);
            linePointer+=1;
        }
        else if(toExecute.substring(0,7).equals("0101011")) {
            machineCycle();
            int wartosc1 = mainMemory.get(mainMemory.get("R" + toExecute.charAt(7)));
            int wartosc2 = mainMemory.get("A");
            int wynik = wartosc1&wartosc2;
            mainMemory.put("A",wynik);
            linePointer+=1;
        }
        else if(toExecute.equals("01010101")) {
            machineCycle();
            int wartosc1 = mainMemory.get("A");
            int warotsc2 = mainMemory.getDirect(codeMemory.getFromAddress(linePointer+1));
            int wynik = wartosc1&warotsc2;
            mainMemory.put("A",wynik);
            linePointer+=2;
        }
        else if(toExecute.equals("10110000")) { // ANL C,/00h
            machineCycle();
            boolean firstValue = mainMemory.getBit(codeMemory.bitAddresses.get("CY"));
            boolean secondValue = !mainMemory.getBit(codeMemory.getFromAddress(linePointer+1));
            if(firstValue==secondValue) {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            linePointer+=2;
        }
        else if(toExecute.equals("10000010")) { // ANL C,00h
            machineCycle();
            boolean firstValue = mainMemory.getBit(codeMemory.bitAddresses.get("CY"));
            boolean secondValue = mainMemory.getBit(codeMemory.getFromAddress(linePointer+1));
            if(firstValue==secondValue) {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            linePointer+=2;
        }
        else if(toExecute.equals("01010010")) { //ANL direct,A
            machineCycle();
            int wartosc1 = mainMemory.get("A");
            int wartosc2 = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer + 1),2));

            int wynik = wartosc1 & wartosc2;

            mainMemory.put((Integer.parseInt(codeMemory.getFromAddress(linePointer + 1),2)), wynik);
            linePointer+=2;
        }
        else if(toExecute.equals("01010011")) { //ANL direct,#01
            machineCycle();
            machineCycle();
            int wartosc1 = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            int wartosc2 = Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
            int wynik = wartosc1&wartosc2;

            mainMemory.put(Integer.parseInt(codeMemory.getFromAddress(linePointer+1)),wynik);

            linePointer+=3;
        }

        /*
            ORL:
                wszystko bez Ri
         */
        else if(toExecute.equals("01000100")) { //ORL a,#01h
            machineCycle();
            int wartosc = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            int wynik = wartosc | mainMemory.get("A");
            mainMemory.put("A",wynik);
            linePointer+=2;
        }
        else if(toExecute.substring(0,5).equals("01001")) { //ORL a,Rx
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc1 = mainMemory.get(rejestrString);
            int wartosc2 = mainMemory.get("A");
            int wynik = wartosc1|wartosc2;
            mainMemory.put("A",wynik);
            linePointer+=1;
        }
        else if(toExecute.substring(0,7).equals("0100011")) {
            machineCycle();
            int wartosc1 = mainMemory.get(mainMemory.get("R" + toExecute.charAt(7)));
            int wartosc2 = mainMemory.get("A");
            int wynik = wartosc1|wartosc2;
            mainMemory.put("A",wynik);
            linePointer+=1;
        }
        else if(toExecute.equals("01000101")) {
            machineCycle();
            int wartosc1 = mainMemory.get("A");
            int warotsc2 = mainMemory.getDirect(codeMemory.getFromAddress(linePointer+1));
            int wynik = wartosc1|warotsc2;
            mainMemory.put("A",wynik);
            linePointer+=2;
        }
        else if(toExecute.equals("10100000")) { // ANL C,/00h
            machineCycle();
            boolean firstValue = mainMemory.getBit(codeMemory.bitAddresses.get("CY"));
            boolean secondValue = !mainMemory.getBit(codeMemory.getFromAddress(linePointer+1));
            if(firstValue || secondValue) {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            linePointer+=2;
        }
        else if(toExecute.equals("01110010")) { // ANL C,00h
            machineCycle();
            boolean firstValue = mainMemory.getBit(codeMemory.bitAddresses.get("CY"));
            boolean secondValue = mainMemory.getBit(codeMemory.getFromAddress(linePointer+1));
            if(firstValue||secondValue) {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            linePointer+=2;
        }
        else if(toExecute.equals("01000010")) { //ANL direct,A
            machineCycle();
            int wartosc1 = mainMemory.get("A");
            int wartosc2 = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer + 1),2));

            int wynik = wartosc1 | wartosc2;

            mainMemory.put((Integer.parseInt(codeMemory.getFromAddress(linePointer + 1),2)), wynik);
            linePointer+=2;
        }
        else if(toExecute.equals("01000011")) { //ANL direct,#01
            machineCycle();
            machineCycle();
            int wartosc1 = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            int wartosc2 = Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
            int wynik = wartosc1|wartosc2;

            mainMemory.put(Integer.parseInt(codeMemory.getFromAddress(linePointer+1)),wynik);

            linePointer+=3;
        }

        /*
            XRL:
                wszystko bez Ri
         */
        else if(toExecute.equals("01100100")) { //XRL a,#01h
            machineCycle();
            int wartosc = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            int wynik = wartosc ^ mainMemory.get("A");
            mainMemory.put("A",wynik);
            linePointer+=2;
        }
        else if(toExecute.substring(0,5).equals("01101")) { //XRL a,Rx
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc1 = mainMemory.get(rejestrString);
            int wartosc2 = mainMemory.get("A");
            int wynik = wartosc1^wartosc2;
            mainMemory.put("A",wynik);
            linePointer+=1;
        }
        else if(toExecute.substring(0,7).equals("0110011")) {
            machineCycle();
            int wartosc1 = mainMemory.get(mainMemory.get("R" + toExecute.charAt(7)));
            int wartosc2 = mainMemory.get("A");
            int wynik = wartosc1^wartosc2;
            mainMemory.put("A",wynik);
            linePointer+=1;
        }
        else if(toExecute.equals("01100101")) {
            machineCycle();
            int wartosc1 = mainMemory.get("A");
            int warotsc2 = mainMemory.getDirect(codeMemory.getFromAddress(linePointer+1));
            int wynik = wartosc1^warotsc2;
            mainMemory.put("A",wynik);
            linePointer+=2;
        }
        else if(toExecute.equals("01100010")) { //XRL direct,A
            machineCycle();
            int wartosc1 = mainMemory.get("A");
            int wartosc2 = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer + 1),2));

            int wynik = wartosc1 ^ wartosc2;

            mainMemory.put((Integer.parseInt(codeMemory.getFromAddress(linePointer + 1),2)), wynik);
            linePointer+=2;
        }
        else if(toExecute.equals("01100011")) { //XRL direct,#01
            machineCycle();
            machineCycle();
            int wartosc1 = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            int wartosc2 = Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
            int wynik = wartosc1^wartosc2;

            mainMemory.put(Integer.parseInt(codeMemory.getFromAddress(linePointer+1)),wynik);

            linePointer+=3;
        }

        /*
            CPL:
                CPL AND A
                CPL AND C
                CPL AND BIT
         */
        else if(toExecute.equals("11110100")) { //CPL A
            machineCycle();
            int wartosc = mainMemory.get("A");
            wartosc = ~wartosc;
            if(wartosc<0)
                wartosc+=256;
            if(wartosc>255)
                wartosc-=256;
            mainMemory.put("A",wartosc);
            linePointer+=1;
        }
        else if(toExecute.equals("10110011")) { //CPL C
            machineCycle();
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            else
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            linePointer+=1;
        }
        else if(toExecute.equals("10110010")) { //CPL Bit
            machineCycle();
            boolean bit = mainMemory.getBit(codeMemory.getFromAddress(linePointer+1));
            bit = !bit;
            mainMemory.setBit(codeMemory.getFromAddress(linePointer+1),bit);
            linePointer+=2;
        }
        /*
            END OF CPL
        */
        else if(toExecute.equals("01000000")) { //JC
            machineCycle();
            machineCycle();
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY"))) {
                int wynik = linePointer + 1 + 1 + Integer.parseInt(codeMemory.getFromAddress(linePointer + 1), 2);
                if (wynik > 255)
                    wynik -= 256;
                linePointer = wynik;
            }
            else
                linePointer+=2;
        }
        else if(toExecute.equals("01010000")) { //JNC
            machineCycle();
            machineCycle();
            if(!mainMemory.getBit(codeMemory.bitAddresses.get("CY"))) {
                int wynik = linePointer + 1 + 1 + Integer.parseInt(codeMemory.getFromAddress(linePointer + 1), 2);
                if (wynik > 255)
                    wynik -= 256;
                linePointer = wynik;
            }
            else
                linePointer+=2;
        }
        else if(toExecute.equals("00100000")) { //JB
            machineCycle();
            machineCycle();

            boolean bit = mainMemory.getBit(codeMemory.getFromAddress(linePointer+1));
            if(bit) {
                int wynik = linePointer+1+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
            }
            else {
                linePointer+=3;
            }
        }
        else if(toExecute.equals("01100000")) { // JZ
            machineCycle();
            machineCycle();
            if(mainMemory.get("A") == 0 ) {
                int wynik = linePointer + 1 + 1 + Integer.parseInt(codeMemory.getFromAddress(linePointer + 1), 2);
                if (wynik > 255)
                    wynik -= 256;
                linePointer = wynik;
            }
            else
                linePointer+=2;
        }
        else if(toExecute.equals("01110000")) { // JNZ
            machineCycle();
            machineCycle();
            if(mainMemory.get("A") != 0 ) {
                int wynik = linePointer + 1 + 1 + Integer.parseInt(codeMemory.getFromAddress(linePointer + 1), 2);
                if (wynik > 255)
                    wynik -= 256;
                linePointer = wynik;
            }
            else
                linePointer+=2;
        }
        else if(toExecute.equals("00110000")) { //JNB
            machineCycle();
            machineCycle();

            boolean bit = mainMemory.getBit(codeMemory.getFromAddress(linePointer+1));
            if(!bit) {
                int wynik = linePointer+1+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
            }
            else {
                linePointer+=3;
            }
        }
        else if(toExecute.substring(0,7).equals("0111011")) {
            machineCycle();
            int wartosc = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            int index = mainMemory.get("R" + toExecute.charAt(7));
            mainMemory.put(index,wartosc);
            linePointer+=2;
        }
        else if(toExecute.substring(0,7).equals("1111011")) {
            machineCycle();
            int wartosc = mainMemory.get("A");
            int index = mainMemory.get("R" + toExecute.charAt(7));
            mainMemory.put(index,wartosc);
            linePointer+= 1;
        }
        else if(toExecute.substring(0,7).equals("1010011")) {
            machineCycle();
            machineCycle();
            int index = mainMemory.get("R" + toExecute.charAt(7));
            int wartosc = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            mainMemory.put(index,wartosc);
            linePointer+=2;
        }
        else if(toExecute.equals("01110100")) { // MOV A,#01h
            machineCycle();
            mainMemory.put("A",Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            linePointer+=2;
        }
        else if(toExecute.equals("10010000")) { // MOV dptr,#wartosc
            machineCycle();
            machineCycle();
            mainMemory.put("DPH",Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            mainMemory.put("DPL",Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2));
            linePointer+=3;
        }



        else if(toExecute.substring(0,5).equals("11101")) { //MOV A,Rx
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc = mainMemory.get(rejestrString);
            mainMemory.put("A",wartosc);
            linePointer+=1;
        }
        else if(toExecute.substring(0,7).equals("1110011")) { //MOV A,@Ri
            machineCycle();
            int index = mainMemory.get("R" + toExecute.charAt(7));
            int wartosc = mainMemory.get(index);
            mainMemory.put("A",wartosc);
            linePointer+=1;
        }
        else if(toExecute.substring(0,7).equals("1000011")) { //MOV direct,@Ri
            machineCycle();
            machineCycle();
            int index = mainMemory.get("R" + toExecute.charAt(7));
            int wartosc = mainMemory.get(index);
            mainMemory.put(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2),wartosc);
            linePointer+=2;
        }
        else if(toExecute.equals("11100101")) { //MOV A,direct
            machineCycle();
            mainMemory.put("A",mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2)));

            linePointer+=2;
        }
        else if(toExecute.substring(0,5).equals("01111")) { //MOV R,#01
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            mainMemory.put(rejestrString,wartosc);
            linePointer+=2;
        }
        else if(toExecute.substring(0,5).equals("11111")) { //MOV R,A
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            mainMemory.put(rejestrString,mainMemory.get("A"));
            linePointer+=1;
        }
        else if(toExecute.substring(0,5).equals("10101")) { //MOV R,direct
            machineCycle();
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            mainMemory.put(rejestrString,mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2)));
            linePointer+=2;
        }
        else if(toExecute.equals("11110101")) { // direct, a
            machineCycle();
            mainMemory.put(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2),mainMemory.get("A"));

            linePointer+=2;
        }
        else if(toExecute.equals("10000101")) { //Px,Px
            machineCycle();
            machineCycle();
            mainMemory.put(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2),mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2)));
            linePointer+=3;

        }
        else if(toExecute.equals("01110101")) { //Px, #liczba
            machineCycle();
            machineCycle();
            mainMemory.put( Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2)  ,Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2));
            linePointer+=3;
        }
        else if(toExecute.equals("00100011")) { //RL A
            machineCycle();
            int wartosc = mainMemory.get("A");
            String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
            StringBuilder wynik = new StringBuilder();
            char tmp = stringWartosc.charAt(0);
            for (int i = 1; i < 8; i++) {
                wynik.append(stringWartosc.charAt(i));
            }
            wynik.append(tmp);
            mainMemory.put("A",Integer.parseInt(wynik.toString(),2));
            linePointer+=1;
        }
        else if(toExecute.equals("00000011")) { //RR A
            machineCycle();
            int wartosc = mainMemory.get("A");
            String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
            StringBuilder wynik = new StringBuilder();
            char tmp = stringWartosc.charAt(7);
            for (int i = 0; i < 7; i++) {
                wynik.append(stringWartosc.charAt(i));
            }
            wynik.insert(0, tmp);
            mainMemory.put("A",Integer.parseInt(wynik.toString(),2));
            linePointer+=1;
        }
        else if(toExecute.equals("00110011")) { //RLC
            machineCycle();
            int wartosc = mainMemory.get("A");
            String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
            StringBuilder wynik = new StringBuilder();
            char tmp = stringWartosc.charAt(0);
            for (int i = 1; i < 8; i++) {
                wynik.append(stringWartosc.charAt(i));
            }
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik.append("1");
            else
                wynik.append("0");
            if(tmp=='1')
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            else
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);

            mainMemory.put("A",Integer.parseInt(wynik.toString(),2));
            linePointer+=1;
            checkP();
        }

        else if(toExecute.equals("00010011")) { // RRC
            machineCycle();
            int wartosc = mainMemory.get("A");
            String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
            StringBuilder wynik = new StringBuilder();
            char tmp = stringWartosc.charAt(7);
            for (int i = 0; i < 7; i++) {
                wynik.append(stringWartosc.charAt(i));
            }
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik.insert(0, "1");
            else
                wynik.insert(0, "0");
            if(tmp=='1')
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            else
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);

            mainMemory.put("A",Integer.parseInt(wynik.toString(),2));
            linePointer+=1;
            checkP();
        }
        else if(toExecute.equals("00100100")) { //ADD A,#01h
            machineCycle();
            int obecnaWartosc = mainMemory.get("A");
            int doDodania = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            int wynik = obecnaWartosc + doDodania;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=2;
            checkAC();
            checkOVAdding(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),codeMemory.getFromAddress(linePointer+1));
            checkP();
        }
        else if(toExecute.substring(0,7).equals("0010011")) { // ADD Ri
            machineCycle();
            int obecnaWartosc = mainMemory.get("A");
            int doDodania = mainMemory.get(mainMemory.get("R" + toExecute.charAt(7)));
            int wynik = obecnaWartosc + doDodania;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=1;
            checkAC();
            checkOVAdding(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
            checkP();
        }

        else if(toExecute.equals("00110100")) { //ADDC A,#01h
            machineCycle();
            int obecnaWartosc = mainMemory.get("A");
            int doDodania = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            int wynik = obecnaWartosc + doDodania;
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik+=1;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=2;
            checkAC();
            checkOVAdding(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
            checkP();
        }
        else if(toExecute.substring(0,7).equals("0011011")) { //addc Ri
            machineCycle();
            int obecnaWartosc = mainMemory.get("A");
            int doDodania = mainMemory.get(mainMemory.get("R" + toExecute.charAt(7)));
            int wynik = obecnaWartosc + doDodania;
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik+=1;
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik+=1;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=1;
            checkAC();
            checkOVAdding(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
            checkP();
        }

        else if(toExecute.substring(0,5).equals("00101")) {
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int doDodania = mainMemory.get(rejestrString);
            int obecnaWartosc = mainMemory.get("A");
            int wynik = obecnaWartosc + doDodania;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=1;
            checkAC();
            checkOVAdding(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
            checkP();
        }
        else if(toExecute.substring(0,5).equals("00111")) {
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int doDodania = mainMemory.get(rejestrString);
            int obecnaWartosc = mainMemory.get("A");
            int wynik = obecnaWartosc + doDodania;
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik+=1;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=1;
            checkAC();
            checkOVAdding(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
            checkP();
        }
        else if(toExecute.equals("00100101")) {
            int doDodania = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            int obecnaWartosc = mainMemory.get("A");
            int wynik = obecnaWartosc + doDodania;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=2;
            checkAC();
            checkOVAdding(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
            checkP();
        }
        else if(toExecute.equals("00110101")) { //ADDC direct
            int doDodania;
            doDodania = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            int obecnaWartosc = mainMemory.get("A");
            int wynik = obecnaWartosc + doDodania;
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik+=1;
            if(wynik>255) {
                wynik -=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=2;
            checkAC();
            checkOVAdding(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
            checkP();
        }
        else if(toExecute.equals("10010100")) { //SUBB A,#01h
            machineCycle();
            int c = mainMemory.getBit(codeMemory.bitAddresses.get("CY")) ? 1 : 0;
            int obecnaWartosc = mainMemory.get("A");
            int doOdjecia = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            int wynik = obecnaWartosc - c - doOdjecia;
            if(wynik<0) {
                wynik +=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=2;
            checkAC();
            checkOVSubtract(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doOdjecia)));
            checkP();
        }

        else if(toExecute.substring(0,5).equals("10011")) { //SUBB A,Rx
            machineCycle();
            int c = mainMemory.getBit(codeMemory.bitAddresses.get("CY")) ? 1 : 0;
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int doOdjecia = mainMemory.get(rejestrString);
            int obecnaWartosc = mainMemory.get("A");
            int wynik = obecnaWartosc - c - doOdjecia;
            if(wynik<0) {
                wynik +=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=1;
            checkAC();
            checkOVSubtract(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doOdjecia)));
            checkP();
        }
        else if(toExecute.substring(0,7).equals("1001011")) { //SUBB A,Ri
            machineCycle();
            int c = mainMemory.getBit(codeMemory.bitAddresses.get("CY")) ? 1 : 0;
            int doOdjecia = mainMemory.get(mainMemory.get("R" + toExecute.charAt(7)));
            int obecnaWartosc = mainMemory.get("A");
            int wynik = obecnaWartosc - c - doOdjecia;
            if(wynik<0) {
                wynik +=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=1;
            checkAC();
            checkOVSubtract(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doOdjecia)));
            checkP();
        }
        else if(toExecute.equals("10010101")) { //SUBB A,Px
            int doOdjecia;
            int c = mainMemory.getBit(codeMemory.bitAddresses.get("CY")) ? 1 : 0;
            doOdjecia = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));

            int obecnaWartosc = mainMemory.get("A");
            int wynik = obecnaWartosc - c - doOdjecia;
            if(wynik<0) {
                wynik +=256;
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            }
            mainMemory.put("A",wynik);
            linePointer+=2;
            checkAC();
            checkOVSubtract(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doOdjecia)));
            checkP();
        }
        else if(toExecute.equals("11010011")) { //SETB C
            machineCycle();
            //psw.put("C",true);
            mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            linePointer++;
        }
        else if(toExecute.equals("11010010")) { //SETB BIT
            machineCycle();
            mainMemory.setBit(codeMemory.getFromAddress(linePointer+1),true);
            linePointer+=2;
        }

        else if(toExecute.equals("11000010")) { //CLR BIT
            machineCycle();
            mainMemory.setBit(codeMemory.getFromAddress(linePointer+1),false);
            linePointer+=2;
        }
        else if(toExecute.equals("11100100")) { //CLR A
            machineCycle();
            mainMemory.put("A",0);
            linePointer++;
        }
        else if(toExecute.equals("11000011")) { //CLR C
            machineCycle();
            mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
            linePointer++;
        }
        else if(toExecute.equals("11000100")) { //SWAP A
            machineCycle();
            String acc = expandTo8Digits(Integer.toBinaryString(mainMemory.get("A")));
            StringBuilder wynik = new StringBuilder();
            for(int i = 4; i < 8;i++) {
                wynik.append(acc.charAt(i));
            }
            for(int i = 0; i < 4;i++) {
                wynik.append(acc.charAt(i));
            }
            int intWynik = Integer.parseInt(wynik.toString(),2);
            mainMemory.put("A",intWynik);
            linePointer+=1;
        }
        else if(toExecute.equals("10100100")) { //MUL AB
            machineCycle();
            machineCycle();
            machineCycle();
            machineCycle();
            int wartosc1 = mainMemory.get("A");
            int wartosc2 = mainMemory.get("B");
            int wynik = wartosc1*wartosc2;
            String wynikString = Integer.toBinaryString(wynik);
            mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
                if(wynik>255)
                    mainMemory.setBit(codeMemory.bitAddresses.get("OV"),true);
                else
                    mainMemory.setBit(codeMemory.bitAddresses.get("OV"),false);
                wynikString = expandTo16Digits(wynikString);
                wartosc1 = Integer.parseInt(wynikString.substring(8,16),2);
                wartosc2 = Integer.parseInt(wynikString.substring(0,8),2);
                mainMemory.put("A",wartosc1);
                mainMemory.put("B",wartosc2);
            linePointer+=1;
        }
        else if(toExecute.equals("10000100")) { //DIV AB
            machineCycle();
            machineCycle();
            machineCycle();
            machineCycle();
            int wartosc1 = mainMemory.get("A");
            int wartosc2 = mainMemory.get("B");
            if(wartosc2!=0) {
                mainMemory.setBit(codeMemory.bitAddresses.get("OV"),false);
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);
                int wynik = wartosc1/wartosc2;
                int reszta = wartosc1 - wartosc2*wynik;

                mainMemory.put("A", wynik);
                mainMemory.put("B", reszta);
                linePointer += 1;
            }
            else {
                mainMemory.setBit(codeMemory.bitAddresses.get("OV"),true);
                linePointer +=1;
            }
        }
        else if(toExecute.equals("00000000")) { //NOP
            machineCycle();
            linePointer+=1;
        }
        else if(toExecute.equals("11000000")) { //PUSH
            int pointer = mainMemory.get("SP");
            pointer+=1;
            if(pointer==256)
                pointer=0;
            int wartosc = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            mainMemory.put(pointer,wartosc);
            mainMemory.put("SP",pointer);
            linePointer+=2;
        }
        else if(toExecute.equals("11010000")) { //POP
            int pointer = mainMemory.get("SP");
            int wartosc = mainMemory.get(pointer);
            mainMemory.put(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2),wartosc);
            pointer-=1;
            if(pointer==-1)
                pointer=255;
            mainMemory.put("SP",pointer);
            linePointer+=2;
        }
        else if(toExecute.equals("00100010")) {//RET
            int stackPointer = mainMemory.get("SP");
            int upperAddress = mainMemory.get(stackPointer);
            stackPointer-=1;
            if(stackPointer==-1)
                stackPointer=255;
            int lowerAddress = mainMemory.get(stackPointer);
            stackPointer-=1;
            if(stackPointer==-1)
                stackPointer=255;
            mainMemory.put("SP",stackPointer);
            String upperAddressString = expandTo8Digits(Integer.toBinaryString(upperAddress));
            String lowerAddressString = expandTo8Digits(Integer.toBinaryString(lowerAddress));
            linePointer = Integer.parseInt(upperAddressString + lowerAddressString,2);
        }
        else if(toExecute.equals("00110010")) {//RETI
            int stackPointer = mainMemory.get("SP");
            int upperAddress = mainMemory.get(stackPointer);
            stackPointer-=1;
            if(stackPointer==-1)
                stackPointer=255;
            int lowerAddress = mainMemory.get(stackPointer);
            stackPointer-=1;
            if(stackPointer==-1)
                stackPointer=255;
            mainMemory.put("SP",stackPointer);
            String upperAddressString = expandTo8Digits(Integer.toBinaryString(upperAddress));
            String lowerAddressString = expandTo8Digits(Integer.toBinaryString(lowerAddress));
            higherInterrupt = 5;
            linePointer = Integer.parseInt(upperAddressString + lowerAddressString,2);
            machineCycle();
            machineCycle();
        }
        else if(toExecute.substring(0,7).equals("1101011")) {
            machineCycle();
            int wartoscA = mainMemory.get("A");
            int wartoscB = mainMemory.get(mainMemory.get("R" + toExecute.charAt(7)));
            String wartoscAString = expandTo8Digits(Integer.toBinaryString(wartoscA));
            String wartoscBString = expandTo8Digits(Integer.toBinaryString(wartoscB));
            char[] wartoscAchar = wartoscAString.toCharArray();
            char[] wartoscBchar = wartoscBString.toCharArray();
            for(int i = 4; i <8;i++) {
                char tmp = wartoscAchar[i];
                wartoscAchar[i] = wartoscBchar[i];
                wartoscBchar[i] = tmp;
            }
            wartoscA = Integer.parseInt(String.valueOf(wartoscAchar),2);
            wartoscB = Integer.parseInt(String.valueOf(wartoscBchar),2);

            mainMemory.put("A",wartoscA);
            mainMemory.put(mainMemory.get("R" + toExecute.charAt(7)),wartoscB);
            linePointer+=1;

        }
        else if(toExecute.substring(0,7).equals("1100011")) { //XCH Ri
            machineCycle();
            int wartoscA = mainMemory.get("A");
            int wartoscB = mainMemory.get(mainMemory.get("R" + toExecute.charAt(7)));
            mainMemory.put("A",wartoscB);
            mainMemory.put(mainMemory.get("R" + toExecute.charAt(7)),wartoscA);
            linePointer+=1;
        }
        else if(toExecute.substring(0,5).equals("11001")) { //XCH Rn
            machineCycle();
            int wartoscA = mainMemory.get("A");
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartoscB = mainMemory.get(rejestrString);
            mainMemory.put("A",wartoscB);
            mainMemory.put(rejestrString,wartoscA);
            linePointer+=1;
        }
        else if(toExecute.equals("11000101")) {
            machineCycle();
            int wartoscA = mainMemory.get("A");
            int wartoscB = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            mainMemory.put("A",wartoscB);
            mainMemory.put(mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2)),wartoscA);
            linePointer+=2;
        }
        else if(toExecute.equals("10000000")) {
            machineCycle();
            machineCycle();
            int wynik = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            if(wynik>127)
                wynik-=256;
            linePointer = linePointer + 1 + 1 +wynik;
        }
        checkP();
        //refreshGui();
        Dac7524.convert();
        checkRegistersBank();
        if(linePointer>=programMemory)
            linePointer=0;
    }

    private int higherInterrupt = 5;

    private void checkAC(){
        String acc = Integer.toBinaryString(mainMemory.get("A"));
        if(acc.length()>4)
            mainMemory.setBit(codeMemory.bitAddresses.get("AC"),true);
        else
            mainMemory.setBit(codeMemory.bitAddresses.get("AC"),false);
    }
    private void checkOVAdding(String oneNumber,String secondNumber){
        String acc = Integer.toBinaryString(mainMemory.get("A"));
        acc = expandTo8Digits(acc);
        if((oneNumber.charAt(0) == '0' && secondNumber.charAt(0) == '0' && acc.charAt(0) == '1') || (oneNumber.charAt(0) == '1' && secondNumber.charAt(0) == '1' && acc.charAt(0) == '0'))
                mainMemory.setBit(codeMemory.bitAddresses.get("OV"), true);
        else
                mainMemory.setBit(codeMemory.bitAddresses.get("OV"), false);
    }
    private void checkOVSubtract(String oneNumber,String secondNumber){
        String acc = Integer.toBinaryString(mainMemory.get("A"));
        acc = expandTo8Digits(acc);
        if((oneNumber.charAt(0) == '0' && secondNumber.charAt(0) == '1' && acc.charAt(0) == '1') || (oneNumber.charAt(0) == '1' && secondNumber.charAt(0) == '0' && acc.charAt(0) == '0'))
            mainMemory.setBit(codeMemory.bitAddresses.get("OV"), true);
        else
            mainMemory.setBit(codeMemory.bitAddresses.get("OV"), false);
    }
    private void checkP(){
        String acc = Integer.toBinaryString(mainMemory.get("A"));
        int jedynek = 0;
        for(int i = 0; i < acc.length();i++) {
            if(acc.charAt(i) == '1')
                jedynek++;
        }
        if(jedynek%2==0)
            mainMemory.setBit(codeMemory.bitAddresses.get("P"),true);
        else
            mainMemory.setBit(codeMemory.bitAddresses.get("P"),false);
    }


    public void refreshGui(){

        Main.stage.timePassedTextField.setText(timePassed + " mks");

        StringBuilder hexLinePointer = new StringBuilder(Integer.toHexString(linePointer));

        int howMany = 4 - hexLinePointer.length();
        for(;howMany>0;howMany--) {
            hexLinePointer.insert(0, "0");
        }

        Main.stage.pcTextField.setText( hexLinePointer+ "h");

        StringBuilder content = new StringBuilder();
        content.append("\t 0\t");
        content.append(" 1\t");
        content.append(" 2\t");
        content.append(" 3\t");
        content.append(" 4\t");
        content.append(" 5\t");
        content.append(" 6\t");
        content.append(" 7\t");
        content.append(" 8\t");
        content.append(" 9\t");
        content.append(" A\t");
        content.append(" B\t");
        content.append(" C\t");
        content.append(" D\t");
        content.append(" E\t");
        content.append(" F\t");
        content.append("\n");

        for(int i = 0; i < 8;i++) {
            content.append(i).append("\t");
            for(int j = 0; j < 16;j++) {
                if(Integer.toHexString(mainMemory.get(i*16+j)).length()==1) {
                    content.append(" " + "0").append(Integer.toHexString(mainMemory.get(i * 16 + j)).toUpperCase()).append(" ");
                }
                else
                    content.append(" ").append(Integer.toHexString(mainMemory.get(i * 16 + j)).toUpperCase()).append(" ");
                content.append("\t");
            }
            if(i!=7)
                content.append("\n");
        }
        double pos = Main.stage.lowerRamTextArea.getScrollTop();
        double pos2 = Main.stage.lowerRamTextArea.getScrollLeft();
        Main.stage.lowerRamTextArea.setText(content.toString());
        Main.stage.lowerRamTextArea.setScrollTop(pos);
        Main.stage.lowerRamTextArea.setScrollLeft(pos2);

        content = new StringBuilder();
        content.append("\t 0\t");
        content.append(" 1\t");
        content.append(" 2\t");
        content.append(" 3\t");
        content.append(" 4\t");
        content.append(" 5\t");
        content.append(" 6\t");
        content.append(" 7\t");
        content.append(" 8\t");
        content.append(" 9\t");
        content.append(" A\t");
        content.append(" B\t");
        content.append(" C\t");
        content.append(" D\t");
        content.append(" E\t");
        content.append(" F\t");
        content.append("\n");

        for(int i = 8; i < 16;i++) {
            if(i<10)
             content.append(i).append("\t");
            else if(i==10)
                content.append("A" + "\t");
            else if(i==11)
                content.append("B" + "\t");
            else if(i==12)
                content.append("C" + "\t");
            else if(i==13)
                content.append("D" + "\t");
            else if(i==14)
                content.append("E" + "\t");
            else if(i==15)
                content.append("F" + "\t");
            for(int j = 0; j < 16;j++) {
                if(Integer.toHexString(mainMemory.get(i*16+j)).length()==1) {
                    content.append(" " + "0").append(Integer.toHexString(mainMemory.get(i * 16 + j)).toUpperCase()).append(" ");
                }
                else
                    content.append(" ").append(Integer.toHexString(mainMemory.get(i * 16 + j)).toUpperCase()).append(" ");
                content.append("\t");
            }
            if(i!=15)
                content.append("\n");
        }

        pos = Main.stage.upperRawTextArea.getScrollTop();
        pos2 = Main.stage.upperRawTextArea.getScrollLeft();
        Main.stage.upperRawTextArea.setText(content.toString());
        Main.stage.upperRawTextArea.setScrollTop(pos);
        Main.stage.upperRawTextArea.setScrollLeft(pos2);

        Main.stage.r0TextField.setText(Integer.toHexString(mainMemory.get("R0")).toUpperCase()+"h");
        Main.stage.r1TextField.setText(Integer.toHexString(mainMemory.get("R1")).toUpperCase()+"h");
        Main.stage.r2TextField.setText(Integer.toHexString(mainMemory.get("R2")).toUpperCase()+"h");
        Main.stage.r3TextField.setText(Integer.toHexString(mainMemory.get("R3")).toUpperCase()+"h");
        Main.stage.r4TextField.setText(Integer.toHexString(mainMemory.get("R4")).toUpperCase()+"h");
        Main.stage.r5TextField.setText(Integer.toHexString(mainMemory.get("R5")).toUpperCase()+"h");
        Main.stage.r6TextField.setText(Integer.toHexString(mainMemory.get("R6")).toUpperCase()+"h");
        Main.stage.r7TextField.setText(Integer.toHexString(mainMemory.get("R7")).toUpperCase()+"h");

        Main.stage.p0TextField.setText(expandTo8Digits(Integer.toBinaryString(mainMemory.get("P0"))) + "b");
        Main.stage.p1TextField.setText(expandTo8Digits(Integer.toBinaryString(mainMemory.get("P1"))) + "b");
        Main.stage.p2TextField.setText(expandTo8Digits(Integer.toBinaryString(mainMemory.get("P2"))) + "b");
        Main.stage.p3TextField.setText(expandTo8Digits(Integer.toBinaryString(mainMemory.get("P3"))) + "b");

        Main.stage.accumulatorTextFieldHex.setText(Integer.toHexString(mainMemory.get("A")).toUpperCase()+"h");
        Main.stage.accumulatorTextFieldBin.setText(expandTo8Digits(Integer.toBinaryString(mainMemory.get("A")).toUpperCase()) + "b");
        Main.stage.accumulatorTextFieldDec.setText(Integer.toString(mainMemory.get("A")).toUpperCase()+"d");
        Main.stage.bTextFieldHex.setText(Integer.toHexString(mainMemory.get("B")).toUpperCase()+"h");
        Main.stage.bTextFieldBin.setText(expandTo8Digits(Integer.toBinaryString(mainMemory.get("B")).toUpperCase()) + "b");
        Main.stage.bTextFieldDec.setText(Integer.toString(mainMemory.get("B")).toUpperCase()+"d");

        Main.stage.TMODTextField.setText(Integer.toHexString(mainMemory.get("TMOD"))+"h");
        Main.stage.TCONTextField.setText(Integer.toHexString(mainMemory.get("TCON"))+"h");
        Main.stage.T0LTextField.setText(Integer.toHexString(mainMemory.get("TL0"))+"h");
        Main.stage.T1LTtextField.setText(Integer.toHexString(mainMemory.get("TL1"))+"h");
        Main.stage.T0HTextField.setText(Integer.toHexString(mainMemory.get("TH0"))+"h");
        Main.stage.T1HTextField.setText(Integer.toHexString(mainMemory.get("TH1"))+"h");

        boolean value = mainMemory.getBit(codeMemory.bitAddresses.get("CY"));
        if(value) {
            Main.stage.cTextField.setText("1");
            Main.stage.cTextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.cTextField.setText("0");
            Main.stage.cTextField.setTextFill(Color.RED);
        }

        value = mainMemory.getBit(codeMemory.bitAddresses.get("AC"));
        if(value) {
            Main.stage.acTextField.setText("1");
            Main.stage.acTextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.acTextField.setText("0");
            Main.stage.acTextField.setTextFill(Color.RED);
        }

        value = mainMemory.getBit(codeMemory.bitAddresses.get("FO"));
        if(value) {
            Main.stage.f0TextField.setText("1");
            Main.stage.f0TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.f0TextField.setText("0");
            Main.stage.f0TextField.setTextFill(Color.RED);
        }
        value = mainMemory.getBit(codeMemory.bitAddresses.get("F1"));
        if(value) {
            Main.stage.f1TextField.setText("1");
            Main.stage.f1TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.f1TextField.setText("0");
            Main.stage.f1TextField.setTextFill(Color.RED);
        }

        value = mainMemory.getBit(codeMemory.bitAddresses.get("RS1"));
        if(value) {
            Main.stage.rs1TextField.setText("1");
            Main.stage.rs1TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.rs1TextField.setText("0");
            Main.stage.rs1TextField.setTextFill(Color.RED);
        }

        value = mainMemory.getBit(codeMemory.bitAddresses.get("RS0"));
        if(value) {
            Main.stage.rs0TextField.setText("1");
            Main.stage.rs0TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.rs0TextField.setText("0");
            Main.stage.rs0TextField.setTextFill(Color.RED);
        }

        value = mainMemory.getBit(codeMemory.bitAddresses.get("OV"));
        if(value) {
            Main.stage.ovTextField.setText("1");
            Main.stage.ovTextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.ovTextField.setText("0");
            Main.stage.ovTextField.setTextFill(Color.RED);
        }

        value = mainMemory.getBit(codeMemory.bitAddresses.get("P"));
        if(value) {
            Main.stage.pTextField.setText("1");
            Main.stage.pTextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.pTextField.setText("0");
            Main.stage.pTextField.setTextFill(Color.RED);
        }

        value = mainMemory.getBit(codeMemory.bitAddresses.get("EX0"));
        if(value) {
            Main.stage.EX0TextField.setText("1");
            Main.stage.EX0TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.EX0TextField.setText("0");
            Main.stage.EX0TextField.setTextFill(Color.RED);
        }
        value = mainMemory.getBit(codeMemory.bitAddresses.get("EX1"));
        if(value) {
            Main.stage.EX1TextField.setText("1");
            Main.stage.EX1TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.EX1TextField.setText("0");
            Main.stage.EX1TextField.setTextFill(Color.RED);
        }
        value = mainMemory.getBit(codeMemory.bitAddresses.get("ET0"));
        if(value) {
            Main.stage.ET0TextField.setText("1");
            Main.stage.ET0TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.ET0TextField.setText("0");
            Main.stage.ET0TextField.setTextFill(Color.RED);
        }
        value = mainMemory.getBit(codeMemory.bitAddresses.get("ET1"));
        if(value) {
            Main.stage.ET1TextField.setText("1");
            Main.stage.ET1TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.ET1TextField.setText("0");
            Main.stage.ET1TextField.setTextFill(Color.RED);
        }
        value = mainMemory.getBit(codeMemory.bitAddresses.get("ES"));
        if(value) {
            Main.stage.ESTextField.setText("1");
            Main.stage.ESTextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.ESTextField.setText("0");
            Main.stage.ESTextField.setTextFill(Color.RED);
        }
        value = mainMemory.getBit(codeMemory.bitAddresses.get("EA"));
        if(value) {
            Main.stage.EATextField.setText("1");
            Main.stage.EATextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.EATextField.setText("0");
            Main.stage.EATextField.setTextFill(Color.RED);
        }


        Main.stage.drawFrame();

    }

    public static String expandTo8Digits(String number) {
        int howMany = 8 - number.length();
        StringBuilder numberBuilder = new StringBuilder(number);
        for(; howMany>0; howMany--) {
            numberBuilder.insert(0, "0");
        }
        number = numberBuilder.toString();
        return number;
    }
    private static String expandTo16Digits(String number) {
        int howMany = 16 - number.length();
        StringBuilder numberBuilder = new StringBuilder(number);
        for(; howMany>0; howMany--) {
            numberBuilder.insert(0, "0");
        }
        number = numberBuilder.toString();
        return number;
    }

    private void checkRegistersBank() {
        int rs0 = 0;
        int rs1 = 0;
        try {
            if (Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("RS0")))
                rs0 = 1;
            if (Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("RS1")))
                rs1 = 1;

            if (rs0 == 0 && rs1 == 0) {
                Main.cpu.mainMemory.memoryCellsNames.put("R0", 0);
                Main.cpu.mainMemory.memoryCellsNames.put("R1", 1);
                Main.cpu.mainMemory.memoryCellsNames.put("R2", 2);
                Main.cpu.mainMemory.memoryCellsNames.put("R3", 3);
                Main.cpu.mainMemory.memoryCellsNames.put("R4", 4);
                Main.cpu.mainMemory.memoryCellsNames.put("R5", 5);
                Main.cpu.mainMemory.memoryCellsNames.put("R6", 6);
                Main.cpu.mainMemory.memoryCellsNames.put("R7", 7);
            }

            else if (rs0 == 1 && rs1 == 0) {
                Main.cpu.mainMemory.memoryCellsNames.put("R0", 8);
                Main.cpu.mainMemory.memoryCellsNames.put("R1", 9);
                Main.cpu.mainMemory.memoryCellsNames.put("R2", 10);
                Main.cpu.mainMemory.memoryCellsNames.put("R3", 11);
                Main.cpu.mainMemory.memoryCellsNames.put("R4", 12);
                Main.cpu.mainMemory.memoryCellsNames.put("R5", 13);
                Main.cpu.mainMemory.memoryCellsNames.put("R6", 14);
                Main.cpu.mainMemory.memoryCellsNames.put("R7", 15);
            }

            else if (rs0 == 0 && rs1 == 1) {
                Main.cpu.mainMemory.memoryCellsNames.put("R0", 16);
                Main.cpu.mainMemory.memoryCellsNames.put("R1", 17);
                Main.cpu.mainMemory.memoryCellsNames.put("R2", 18);
                Main.cpu.mainMemory.memoryCellsNames.put("R3", 19);
                Main.cpu.mainMemory.memoryCellsNames.put("R4", 20);
                Main.cpu.mainMemory.memoryCellsNames.put("R5", 21);
                Main.cpu.mainMemory.memoryCellsNames.put("R6", 22);
                Main.cpu.mainMemory.memoryCellsNames.put("R7", 23);

            }

            else if (rs0 == 1 && rs1 == 1) {
                Main.cpu.mainMemory.memoryCellsNames.put("R0", 24);
                Main.cpu.mainMemory.memoryCellsNames.put("R1", 25);
                Main.cpu.mainMemory.memoryCellsNames.put("R2", 26);
                Main.cpu.mainMemory.memoryCellsNames.put("R3", 27);
                Main.cpu.mainMemory.memoryCellsNames.put("R4", 28);
                Main.cpu.mainMemory.memoryCellsNames.put("R5", 29);
                Main.cpu.mainMemory.memoryCellsNames.put("R6", 30);
                Main.cpu.mainMemory.memoryCellsNames.put("R7", 31);
            }
        }
        catch (Exception ignored){
            System.out.println(ignored.fillInStackTrace());
        }
    }


    public long getTimePassed(){return timePassed;}

    private ArrayList<Boolean> interrupts = new ArrayList<>();

    private long timePassed;
    public int linePointer;
    public Memory mainMemory;
    public CodeMemory codeMemory = new CodeMemory();

    public boolean LastP32 = true;
    public boolean LastP33 = true;

    public static int programMemory = 16384;

}
