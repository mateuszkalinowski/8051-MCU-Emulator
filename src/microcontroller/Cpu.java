package microcontroller;

import components.CodeMemory;
import components.Memory;
import core.Main;
import exceptions.InstructionException;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    }

    private void machineCycle(){
        timePassed++;
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

        if(mainMemory.getBit(codeMemory.bitAddresses.get("EA"))) {

            if (mainMemory.getBit(codeMemory.bitAddresses.get("TF0")) && mainMemory.getBit(codeMemory.bitAddresses.get("ET0")) && higherInterrupt>=2)
                interrupts.set(1, true);
            if (mainMemory.getBit(codeMemory.bitAddresses.get("TF1")) && mainMemory.getBit(codeMemory.bitAddresses.get("ET1")) && higherInterrupt>=4)
                interrupts.set(3, true);

        }

    }

    public void executeInstruction() throws InstructionException{
        String toExecute = codeMemory.getFromAddress(linePointer);
        //System.out.println(linePointer);
        //System.out.println(linePointer + " " + toExecute);
        if(toExecute.equals("00000010")) { //LJMP
            machineCycle();
            machineCycle();
            linePointer = Integer.parseInt(codeMemory.getFromAddress(linePointer+1) + codeMemory.getFromAddress(linePointer+2),2);
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
                int wynik = linePointer+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
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
                int wynik = linePointer+1+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
            }
            if(wartosc==0) {
                linePointer+=3;
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
            mainMemory.put(rejestrString,Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
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
            String wynik = "";
            char tmp = stringWartosc.charAt(0);
            for (int i = 1; i < 8; i++) {
                wynik += stringWartosc.charAt(i);
            }
            wynik += tmp;
            mainMemory.put("A",Integer.parseInt(wynik,2));
            linePointer+=1;
        }
        else if(toExecute.equals("00000011")) { //RR A
            machineCycle();
            int wartosc = mainMemory.get("A");
            String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
            String wynik = "";
            char tmp = stringWartosc.charAt(7);
            for (int i = 0; i < 7; i++) {
                wynik += stringWartosc.charAt(i);
            }
            wynik = tmp + wynik;
            mainMemory.put("A",Integer.parseInt(wynik,2));
            linePointer+=1;
        }
        else if(toExecute.equals("00110011")) { //RLC
            machineCycle();
            int wartosc = mainMemory.get("A");
            String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
            String wynik = "";
            char tmp = stringWartosc.charAt(0);
            for (int i = 1; i < 8; i++) {
                wynik += stringWartosc.charAt(i);
            }
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik += "1";
            else
                wynik += "0";
            if(tmp=='1')
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            else
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);

            mainMemory.put("A",Integer.parseInt(wynik,2));
            linePointer+=1;
            checkP();
        }

        else if(toExecute.equals("00010011")) { // RRC
            machineCycle();
            int wartosc = mainMemory.get("A");
            String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
            String wynik = "";
            char tmp = stringWartosc.charAt(7);
            for (int i = 0; i < 7; i++) {
                wynik += stringWartosc.charAt(i);
            }
            if(mainMemory.getBit(codeMemory.bitAddresses.get("CY")))
                wynik = "1" + wynik;
            else
                wynik = "0" + wynik;
            if(tmp=='1')
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),true);
            else
                mainMemory.setBit(codeMemory.bitAddresses.get("CY"),false);

            mainMemory.put("A",Integer.parseInt(wynik,2));
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
            checkOV(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),codeMemory.getFromAddress(linePointer+1));
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
            checkOV(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
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
            checkOV(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
            checkP();
        }
        else if(toExecute.substring(0,7).equals("0011011")) { //addc Ri
            machineCycle();
            int obecnaWartosc = mainMemory.get("A");
            int doDodania = mainMemory.get(mainMemory.get("R" + toExecute.charAt(7)));
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
            checkOV(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
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
            checkOV(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
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
            checkOV(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
            checkP();
        }
        else if(toExecute.equals("00100101")) {
            int doDodania = 0;

            doDodania = mainMemory.get(Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));

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
            checkOV(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
            checkP();
        }
        else if(toExecute.equals("00110101")) {
            int doDodania = 0;
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
            checkOV(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)),expandTo8Digits(Integer.toBinaryString(doDodania)));
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
            //checkOVSubtraction(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)));
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
            //checkOVSubtraction(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)));
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
           // checkOVSubtraction(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)));
            checkP();
        }
        else if(toExecute.equals("10010101")) { //SUBB A,Px
            int doOdjecia = 0;
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
           // checkOVSubtraction(expandTo8Digits(Integer.toBinaryString(obecnaWartosc)));
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
            String wynik = "";
            for(int i = 4; i < 8;i++) {
                wynik+=acc.charAt(i);
            }
            for(int i = 0; i < 4;i++) {
                wynik+=acc.charAt(i);
            }
            int intWynik = Integer.parseInt(wynik,2);
            mainMemory.put("A",intWynik);
            linePointer+=1;
        }
        else if(toExecute.equals("00000000")) { //NOP
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
        }


            if (interrupts.get(1)) {
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
                } catch (Exception ignored) {
                }

            } else if (interrupts.get(3)) {
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
                } catch (Exception ignored) {
                }
            }
        refreshStatusRegister();
        //refreshGui();
    }

    private int higherInterrupt = 5;

    private void refreshStatusRegister(){
        checkP();
        checkAC();
    }
    private void checkAC(){
        String acc = Integer.toBinaryString(mainMemory.get("A"));
        if(acc.length()>4)
            mainMemory.setBit(codeMemory.bitAddresses.get("AC"),true);
        else
            mainMemory.setBit(codeMemory.bitAddresses.get("AC"),false);
    }
    private void checkOV(String oneNumber,String secondNumber){
        String acc = Integer.toBinaryString(mainMemory.get("A"));
        acc = expandTo8Digits(acc);
        if((oneNumber.charAt(0) == '0' && secondNumber.charAt(0) == '0' && acc.charAt(0) == '1') || (oneNumber.charAt(0) == '1' && secondNumber.charAt(0) == '1' && acc.charAt(0) == '0'))
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

        String hexLinePointer = Integer.toHexString(linePointer);

        int howMany = 4 - hexLinePointer.length();
        for(;howMany>0;howMany--) {
            hexLinePointer = "0"+hexLinePointer;
        }

        Main.stage.pcTextField.setText( hexLinePointer+ "h");

        String content = "";
        content +="\t 0\t";
        content +=" 1\t";
        content +=" 2\t";
        content +=" 3\t";
        content +=" 4\t";
        content +=" 5\t";
        content +=" 6\t";
        content +=" 7\t";
        content +=" 8\t";
        content +=" 9\t";
        content +=" A\t";
        content +=" B\t";
        content +=" C\t";
        content +=" D\t";
        content +=" E\t";
        content +=" F\t";
        content +="\n";

        for(int i = 0; i < 8;i++) {
            content+=i+"\t";
            for(int j = 0; j < 16;j++) {
                if(Integer.toHexString(mainMemory.get(i*16+j)).length()==1) {
                    content+=" " + "0" + Integer.toHexString(mainMemory.get(i*16+j)).toUpperCase() + " ";
                }
                else
                    content+=" " + Integer.toHexString(mainMemory.get(i*16+j)).toUpperCase() + " ";
                content+="\t";
            }
            if(i!=7)
                content+="\n";
        }
        double pos = Main.stage.lowerRamTextArea.getScrollTop();
        double pos2 = Main.stage.lowerRamTextArea.getScrollLeft();
        Main.stage.lowerRamTextArea.setText(content);
        Main.stage.lowerRamTextArea.setScrollTop(pos);
        Main.stage.lowerRamTextArea.setScrollLeft(pos2);

        content = "";
        content +="\t 0\t";
        content +=" 1\t";
        content +=" 2\t";
        content +=" 3\t";
        content +=" 4\t";
        content +=" 5\t";
        content +=" 6\t";
        content +=" 7\t";
        content +=" 8\t";
        content +=" 9\t";
        content +=" A\t";
        content +=" B\t";
        content +=" C\t";
        content +=" D\t";
        content +=" E\t";
        content +=" F\t";
        content +="\n";

        for(int i = 8; i < 16;i++) {
            content+=i+"\t";
            for(int j = 0; j < 16;j++) {
                if(Integer.toHexString(mainMemory.get(i*16+j)).length()==1) {
                    content+=" " + "0" + Integer.toHexString(mainMemory.get(i*16+j)).toUpperCase() + " ";
                }
                else
                    content+=" " + Integer.toHexString(mainMemory.get(i*16+j)).toUpperCase() + " ";
                content+="\t";
            }
            if(i!=15)
                content+="\n";
        }

        pos = Main.stage.upperRawTextArea.getScrollTop();
        pos2 = Main.stage.upperRawTextArea.getScrollLeft();
        Main.stage.upperRawTextArea.setText(content);
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
        for(;howMany>0;howMany--) {
            number = "0"+number;
        }
        return number;
    }

    public void resetCounter(){
        linePointer = 0;
    }

    private ArrayList<Boolean> interrupts = new ArrayList<>();

    public long timePassed;
    private int linePointer;
    public Memory mainMemory;
    public CodeMemory codeMemory = new CodeMemory();

}
