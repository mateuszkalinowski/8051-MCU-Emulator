package microcontroller;

import components.CodeMemory;
import core.Main;
import elements.Instruction;
import elements.Mnemonic;
import exceptions.InstructionException;
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
        registers.clear();
        registers.put("R0",0);
        registers.put("R1",0);
        registers.put("R2",0);
        registers.put("R3",0);
        registers.put("R4",0);
        registers.put("R5",0);
        registers.put("R6",0);
        registers.put("R7",0);
        registers.put("A",0);
        registers.put("B",0);
        ports.clear();
        ports.put("P0",255);
        ports.put("P1",255);
        ports.put("P2",255);
        ports.put("P3",255);

        psw.put("P",true);
        psw.put("OV",false);
        psw.put("RS0",false);
        psw.put("RS1",false);
        psw.put("F0",false);
        psw.put("AC",false);
        psw.put("C",false);


    }

    private void machineCycle(){
        refreshStatusRegister();
    }

    public void executeInstruction() throws InstructionException{

        String toExecute = codeMemory.getFromAddress(linePointer);
        if(toExecute.equals("00000010")) { //LJMP
            machineCycle();
            machineCycle();
            linePointer = Integer.parseInt(codeMemory.getFromAddress(linePointer+1) + codeMemory.getFromAddress(linePointer+2));
        }
        else if(toExecute.substring(0,5).equals("11011")) { //DJNZ Rx,label/adres (ale na wartosci a nie offsecie)
            machineCycle();
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,7),2);
            String rejestrString = "R" + rejestr;
            int wartosc = registers.get(rejestrString);
            wartosc--;
            if(wartosc==-1)
                wartosc=255;
            if(wartosc>0) {
                linePointer = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            }
            if(wartosc==0) {
                linePointer+=3;
            }
        }
        else if(toExecute.equals("01110100")) { // MOV A,#01h
            machineCycle();
            registers.put("A",Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2));
            linePointer+=2;
        }
        else if(toExecute.substring(0,5).equals("11101")) { //MOV A,Rx
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,7),2);
            String rejestrString = "R" + rejestr;
            int wartosc = registers.get(rejestrString);
            registers.put("A",wartosc);
            linePointer+=1;
        }
        else if(toExecute.equals("11100101")) { //MOV A,Px
            machineCycle();

            if(codeMemory.getFromAddress(linePointer+1).equals("10000000")) {
                registers.put("A",ports.get("P0"));
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10010000")) {
                registers.put("A",ports.get("P1"));
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10100000")) {
                registers.put("A",ports.get("P2"));
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10110000")) {
                registers.put("A",ports.get("P3"));
            }
            linePointer+=2;
        }
        else if(toExecute.substring(0,5).equals("01111")) {
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,7),2);
            String rejestrString = "R" + rejestr;
            int wartosc = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            registers.put(rejestrString,wartosc);
            linePointer+=2;
        }
        else if(toExecute.substring(0,5).equals("11111")) {
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,7),2);
            String rejestrString = "R" + rejestr;
            registers.put(rejestrString,registers.get("A"));
            linePointer+=1;
        }
        else if(toExecute.equals("11110101")) {
            machineCycle();
            if(codeMemory.getFromAddress(linePointer+1).equals("10000000")) {
                ports.put("P0",registers.get("A"));
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10010000")) {
                ports.put("P1",registers.get("A"));
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10100000")) {
                ports.put("P2",registers.get("A"));
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10110000")) {
                ports.put("P3",registers.get("A"));
            }
            linePointer+=2;
        }
        /*
        else if(toExecute.getMnemonic().equals("RL")) {
            if(toExecute.getParam1().equals("A")) {
                int wartosc = registers.get(toExecute.getParam1());
                String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
                String wynik = "";
                char tmp = stringWartosc.charAt(0);
                for (int i = 1; i < 8; i++) {
                    wynik += stringWartosc.charAt(i);
                }
                wynik += tmp;

                registers.put(toExecute.getParam1(),Integer.parseInt(wynik,2));

                int mnemonicSize = Main.cpu.getMnemonicSize("RL");
                linePointer += mnemonicSize;
            }
            else {
                System.out.println("Błąd w RL");
                throw new InstructionException();
            }
        }
        else if(toExecute.getMnemonic().equals("RLC")) {
            if(toExecute.getParam1().equals("A")) {
                int wartosc = registers.get(toExecute.getParam1());
                String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
                String wynik = "";
                char tmp = stringWartosc.charAt(0);
                for (int i = 1; i < 8; i++) {
                    wynik += stringWartosc.charAt(i);
                }
                if(psw.get("C"))
                    wynik += "1";
                else
                    wynik += "0";
                if(tmp=='1')
                    psw.put("C",true);
                else
                    psw.put("C",false);

                registers.put(toExecute.getParam1(),Integer.parseInt(wynik,2));

                int mnemonicSize = Main.cpu.getMnemonicSize("RL");
                linePointer += mnemonicSize;
            }
            else {
                System.out.println("Błąd w RL");
                throw new InstructionException();
            }
        }
        else if(toExecute.getMnemonic().equals("RR")) {
            if(toExecute.getParam1().equals("A")) {
                int wartosc = registers.get(toExecute.getParam1());
                String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
                String wynik = "";
                char tmp = stringWartosc.charAt(7);
                for (int i = 0; i < 7; i++) {
                    wynik += stringWartosc.charAt(i);
                }
                wynik = tmp + wynik;

                registers.put(toExecute.getParam1(),Integer.parseInt(wynik,2));

                int mnemonicSize = Main.cpu.getMnemonicSize("RL");
                linePointer += mnemonicSize;
            }
            else {
                System.out.println("Błąd w RL");
                throw new InstructionException();
            }
        }
        else if(toExecute.getMnemonic().equals("RRC")) {
            if(toExecute.getParam1().equals("A")) {
                int wartosc = registers.get(toExecute.getParam1());
                String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
                String wynik = "";
                char tmp = stringWartosc.charAt(7);
                for (int i = 0; i < 7; i++) {
                    wynik += stringWartosc.charAt(i);
                }
                if(psw.get("C"))
                    wynik = "1" + wynik;
                else
                    wynik = "0" + wynik;
                if(tmp=='1')
                    psw.put("C",true);
                else
                    psw.put("C",false);

                registers.put(toExecute.getParam1(),Integer.parseInt(wynik,2));

                int mnemonicSize = Main.cpu.getMnemonicSize("RL");
                linePointer += mnemonicSize;
            }
            else {
                System.out.println("Błąd w RL");
                throw new InstructionException();
            }
        }
        else if(toExecute.getMnemonic().equals("ADD")) {
            if(toExecute.getParam1().equals("A")) {
                if(toExecute.getParam2().charAt(0) == '#') {
                    String numberToAdd = toExecute.getParam2();
                    int numberToAddLength = numberToAdd.length();
                    int intNumberToAdd =-1;
                    if(numberToAdd.charAt(numberToAddLength-1)=='D') {
                        intNumberToAdd = Integer.parseInt(numberToAdd.substring(1,numberToAddLength-1));
                    }
                    else if(numberToAdd.charAt(numberToAddLength-1)=='H') {
                        intNumberToAdd = Integer.parseInt(numberToAdd.substring(1,numberToAddLength-1),16);
                    }
                    else if(numberToAdd.charAt(numberToAddLength-1)=='B') {
                        intNumberToAdd = Integer.parseInt(numberToAdd.substring(1,numberToAddLength-1),2);
                    }
                    else {
                        System.out.println("Niepomyślne analizowanie liczby");
                        throw new InstructionException();
                    }
                    if(intNumberToAdd!=-1) {
                        int akumulator = registers.get("A");
                        int wynik = akumulator + intNumberToAdd;
                        if(wynik>255) {
                            wynik-=255;
                            psw.put("C",true);
                        }
                        else {
                            psw.put("C",false);
                        }
                        registers.put("A",wynik);

                    }
                    else {
                        throw new InstructionException();
                    }
                }
                else {
                    int toAdd = -1;
                    if(registers.containsKey(toExecute.getParam2())) {
                        toAdd = registers.get(toExecute.getParam2());
                    }
                    else if(ports.containsKey(toExecute.getParam2())) {
                        toAdd = ports.get(toExecute.getParam2());
                    }
                    else {
                        throw new InstructionException();
                    }
                    int accInt = registers.get("A");
                    int wynik = accInt+toAdd;

                    if(wynik>255) {
                        wynik-=255;
                        psw.put("C",true);
                    }
                    else {
                        psw.put("C",false);
                    }
                    registers.put("A",wynik);

                }
            } else {
                throw new InstructionException();
            }

            int mnemonicSize = Main.cpu.getMnemonicSize("ADD");
            linePointer += mnemonicSize;

        } else if(toExecute.getMnemonic().equals("ADDC")) {
            int bitC = psw.get("C") ? 1 : 0;
            if(toExecute.getParam1().equals("A")) {
                if(toExecute.getParam2().charAt(0) == '#') {
                    String numberToAdd = toExecute.getParam2();
                    int numberToAddLength = numberToAdd.length();
                    int intNumberToAdd =-1;
                    if(numberToAdd.charAt(numberToAddLength-1)=='D') {
                        intNumberToAdd = Integer.parseInt(numberToAdd.substring(1,numberToAddLength-1));
                    }
                    else if(numberToAdd.charAt(numberToAddLength-1)=='H') {
                        intNumberToAdd = Integer.parseInt(numberToAdd.substring(1,numberToAddLength-1),16);
                    }
                    else if(numberToAdd.charAt(numberToAddLength-1)=='B') {
                        intNumberToAdd = Integer.parseInt(numberToAdd.substring(1,numberToAddLength-1),2);
                    }
                    else {
                        System.out.println("Niepomyślne analizowanie liczby");
                        throw new InstructionException();
                    }
                    if(intNumberToAdd!=-1) {
                        int akumulator = registers.get("A");
                        int wynik = akumulator + intNumberToAdd + bitC;
                        if(wynik>255) {
                            wynik-=255;
                            psw.put("C",true);
                        }
                        else {
                            psw.put("C",false);
                        }
                        registers.put("A",wynik);

                    }
                    else {
                        throw new InstructionException();
                    }
                }
                else {
                    int toAdd = -1;
                    if(registers.containsKey(toExecute.getParam2())) {
                        toAdd = registers.get(toExecute.getParam2());
                    }
                    else if(ports.containsKey(toExecute.getParam2())) {
                        toAdd = ports.get(toExecute.getParam2());
                    }
                    else {
                        throw new InstructionException();
                    }
                    int accInt = registers.get("A");
                    int wynik = accInt+toAdd + bitC;

                    if(wynik>255) {
                        wynik-=255;
                        psw.put("C",true);
                    }
                    else {
                        psw.put("C",false);
                    }
                    registers.put("A",wynik);

                }
            } else {
                throw new InstructionException();
            }

            int mnemonicSize = Main.cpu.getMnemonicSize("ADD");
            linePointer += mnemonicSize;

        }

        int machineCyclesNumber = Main.cpu.getMnemonicTime(toExecute.getMnemonic().toUpperCase());
        for (;machineCyclesNumber>0;machineCyclesNumber--) {
            machineCycle();
        }
*/
        refreshGui();
    }

    private void refreshStatusRegister(){
        String acc = Integer.toBinaryString(registers.get("A"));
        if(acc.length()>4)
            psw.put("AC",true);
        else
            psw.put("AC",false);
        int jedynek = 0;
        for(int i = 0; i < acc.length();i++) {
            if(acc.charAt(i) == '1')
                jedynek++;
        }
        if(jedynek%2==0)
            psw.put("P",true);
        else
            psw.put("P",false);
    }
    public void refreshGui(){
        Main.stage.r0TextField.setText(Integer.toHexString(registers.get("R0")).toUpperCase()+"h");
        Main.stage.r1TextField.setText(Integer.toHexString(registers.get("R1")).toUpperCase()+"h");
        Main.stage.r2TextField.setText(Integer.toHexString(registers.get("R2")).toUpperCase()+"h");
        Main.stage.r3TextField.setText(Integer.toHexString(registers.get("R3")).toUpperCase()+"h");
        Main.stage.r4TextField.setText(Integer.toHexString(registers.get("R4")).toUpperCase()+"h");
        Main.stage.r5TextField.setText(Integer.toHexString(registers.get("R5")).toUpperCase()+"h");
        Main.stage.r6TextField.setText(Integer.toHexString(registers.get("R6")).toUpperCase()+"h");
        Main.stage.r7TextField.setText(Integer.toHexString(registers.get("R7")).toUpperCase()+"h");

        Main.stage.p0TextField.setText(expandTo8Digits(Integer.toBinaryString(ports.get("P0"))) + "b");
        Main.stage.p1TextField.setText(expandTo8Digits(Integer.toBinaryString(ports.get("P1"))) + "b");
        Main.stage.p2TextField.setText(expandTo8Digits(Integer.toBinaryString(ports.get("P2"))) + "b");
        Main.stage.p3TextField.setText(expandTo8Digits(Integer.toBinaryString(ports.get("P3"))) + "b");

        Main.stage.accumulatorTextField.setText(Integer.toHexString(registers.get("A")).toUpperCase()+"h");
        Main.stage.bTextField.setText(Integer.toHexString(registers.get("B")).toUpperCase()+"h");

        boolean value = psw.get("C");
        if(value) {
            Main.stage.cTextField.setText("1");
            Main.stage.cTextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.cTextField.setText("0");
            Main.stage.cTextField.setTextFill(Color.RED);
        }

        value = psw.get("AC");
        if(value) {
            Main.stage.acTextField.setText("1");
            Main.stage.acTextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.acTextField.setText("0");
            Main.stage.acTextField.setTextFill(Color.RED);
        }

        value = psw.get("F0");
        if(value) {
            Main.stage.f0TextField.setText("1");
            Main.stage.f0TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.f0TextField.setText("0");
            Main.stage.f0TextField.setTextFill(Color.RED);
        }

        value = psw.get("RS1");
        if(value) {
            Main.stage.rs1TextField.setText("1");
            Main.stage.rs1TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.rs1TextField.setText("0");
            Main.stage.rs1TextField.setTextFill(Color.RED);
        }

        value = psw.get("RS0");
        if(value) {
            Main.stage.rs0TextField.setText("1");
            Main.stage.rs0TextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.rs0TextField.setText("0");
            Main.stage.rs0TextField.setTextFill(Color.RED);
        }

        value = psw.get("OV");
        if(value) {
            Main.stage.ovTextField.setText("1");
            Main.stage.ovTextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.ovTextField.setText("0");
            Main.stage.ovTextField.setTextFill(Color.RED);
        }

        value = psw.get("P");
        if(value) {
            Main.stage.pTextField.setText("1");
            Main.stage.pTextField.setTextFill(Color.GREEN);
        }
        else {
            Main.stage.pTextField.setText("0");
            Main.stage.pTextField.setTextFill(Color.RED);
        }

    }

    public static String expandTo8Digits(String number) {
        int howMany = 8 - number.length();
        for(;howMany>0;howMany--) {
            number = "0"+number;
        }
        return number;
    }

    private int linePointer;
    private Map<String,Integer> registers = new HashMap<>();
    private Map<String,Boolean> psw = new HashMap<>();
    public CodeMemory codeMemory = new CodeMemory();
    private Map<String,Integer> ports = new HashMap<>();
}
