package microcontroller;

import components.CodeMemory;
import core.Main;
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
        bitMap.clear();
        bitMap.put("A.0","11100000");
        bitMap.put("A.1","11100001");
        bitMap.put("A.2","11100010");
        bitMap.put("A.3","11100011");
        bitMap.put("A.4","11100100");
        bitMap.put("A.5","11100101");
        bitMap.put("A.6","11100110");
        bitMap.put("A.7","11100111");

        psw.put("P",true);
        psw.put("OV",false);
        psw.put("RS0",false);
        psw.put("RS1",false);
        psw.put("F0",false);
        psw.put("AC",false);
        psw.put("C",false);


    }

    private void machineCycle(){

    }

    public void executeInstruction() throws InstructionException{
        System.out.println(linePointer);
        String toExecute = codeMemory.getFromAddress(linePointer);
        //System.out.println(linePointer + " " + toExecute);
        if(toExecute.equals("00000010")) { //LJMP
            machineCycle();
            machineCycle();
            linePointer = Integer.parseInt(codeMemory.getFromAddress(linePointer+1) + codeMemory.getFromAddress(linePointer+2),2);
        }
        else if(toExecute.substring(0,5).equals("11011")) { //DJNZ Rx,label/adres (ale na wartosci a nie offsecie)
            machineCycle();
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc = registers.get(rejestrString);
            wartosc--;
            if(wartosc==-1)
                wartosc=255;

            registers.put(rejestrString,wartosc);

            if(wartosc>0) {

                int wynik = linePointer+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
            }
            if(wartosc==0) {
                linePointer+=2;
            }
        }
        else if(toExecute.equals("01000000")) { //JC
            machineCycle();
            machineCycle();
            if(psw.get("C")) {
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
            String rejestr = getKeyFromBitMap(codeMemory.getFromAddress(linePointer+1));
            String podzielone[] = rejestr.split("\\.");
            int wartosc = registers.get(podzielone[0]);
            String wartoscString = expandTo8Digits(Integer.toBinaryString(wartosc));
            int index = Integer.parseInt(podzielone[1]);
            if(wartoscString.charAt(7-index)=='1') {
                int wynik = linePointer+1+1+1+Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2);
                if(wynik>255)
                    wynik-=256;
                linePointer = wynik;
                System.out.println("tak");
            }
            else {
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
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
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
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int wartosc = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            registers.put(rejestrString,wartosc);
            linePointer+=2;
        }
        else if(toExecute.substring(0,5).equals("11111")) {
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            registers.put(rejestrString,registers.get("A"));
            linePointer+=1;
        }
        else if(toExecute.equals("11110101")) { // Px, a
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
        else if(toExecute.equals("10000101")) { //Px,Px
            machineCycle();
            machineCycle();
            String port1 = "";
            String port2 = "";
            if(codeMemory.getFromAddress(linePointer+1).equals("10000000")) {
                port1="P0";
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10010000")) {
                port1="P1";
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10100000")) {
                port1="P2";
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10110000")) {
                port1="P3";
            }

            if(codeMemory.getFromAddress(linePointer+2).equals("10000000")) {
                port2="P0";
            }
            if(codeMemory.getFromAddress(linePointer+2).equals("10010000")) {
                port2="P1";
            }
            if(codeMemory.getFromAddress(linePointer+2).equals("10100000")) {
                port2="P2";
            }
            if(codeMemory.getFromAddress(linePointer+2).equals("10110000")) {
                port2="P3";
            }
            ports.put(port1,ports.get(port2));
            linePointer+=3;

        }
        else if(toExecute.equals("01110101")) { //Px, #liczba
            machineCycle();
            machineCycle();
            String port1 = "";
            if(codeMemory.getFromAddress(linePointer+1).equals("10000000")) {
                port1="P0";
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10010000")) {
                port1="P1";
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10100000")) {
                port1="P2";
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10110000")) {
                port1="P3";
            }
            ports.put(port1,Integer.parseInt(codeMemory.getFromAddress(linePointer+2),2));
            linePointer+=3;
        }
        else if(toExecute.equals("00100011")) { //RL A
            machineCycle();
            int wartosc = registers.get("A");
            String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
            String wynik = "";
            char tmp = stringWartosc.charAt(0);
            for (int i = 1; i < 8; i++) {
                wynik += stringWartosc.charAt(i);
            }
            wynik += tmp;
            registers.put("A",Integer.parseInt(wynik,2));
            linePointer+=1;
        }
        else if(toExecute.equals("00000011")) { //RR A
            machineCycle();
            int wartosc = registers.get("A");
            String stringWartosc = expandTo8Digits(Integer.toString(wartosc,2));
            String wynik = "";
            char tmp = stringWartosc.charAt(7);
            for (int i = 0; i < 7; i++) {
                wynik += stringWartosc.charAt(i);
            }
            wynik = tmp + wynik;
            registers.put("A",Integer.parseInt(wynik,2));
            linePointer+=1;
        }
        else if(toExecute.equals("00110011")) { //RLC
            machineCycle();
            int wartosc = registers.get("A");
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

            registers.put("A",Integer.parseInt(wynik,2));
            linePointer+=1;
        }

        else if(toExecute.equals("00010011")) { // RRC
            machineCycle();
            int wartosc = registers.get("A");
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

            registers.put("A",Integer.parseInt(wynik,2));
            linePointer+=1;
        }
        else if(toExecute.equals("00100100")) { //ADD A,#01h
            machineCycle();
            int obecnaWartosc = registers.get("A");
            int doDodania = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            int wynik = obecnaWartosc + doDodania;
            if(wynik>255) {
                wynik -=256;
                psw.put("C",true);
            }
            else {
                psw.put("C",false);
            }
            registers.put("A",wynik);
            linePointer+=2;
        }

        else if(toExecute.equals("00110100")) { //ADDC A,#01h
            machineCycle();
            int obecnaWartosc = registers.get("A");
            int doDodania = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            int wynik = obecnaWartosc + doDodania;
            if(psw.get("C"))
                wynik+=1;
            if(wynik>255) {
                wynik -=256;
                psw.put("C",true);
            }
            else {
                psw.put("C",false);
            }
            registers.put("A",wynik);
            linePointer+=2;
        }

        else if(toExecute.substring(0,5).equals("00101")) {
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int doDodania = registers.get(rejestrString);
            int obecnaWartosc = registers.get("A");
            int wynik = obecnaWartosc + doDodania;
            if(wynik>255) {
                wynik -=256;
                psw.put("C",true);
            }
            else {
                psw.put("C",false);
            }
            registers.put("A",wynik);
            linePointer+=1;
        }
        else if(toExecute.substring(0,5).equals("00111")) {
            machineCycle();
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int doDodania = registers.get(rejestrString);
            int obecnaWartosc = registers.get("A");
            int wynik = obecnaWartosc + doDodania;
            if(psw.get("C"))
                wynik+=1;
            if(wynik>255) {
                wynik -=256;
                psw.put("C",true);
            }
            else {
                psw.put("C",false);
            }
            registers.put("A",wynik);
            linePointer+=1;
        }
        else if(toExecute.equals("00100101")) {
            int doDodania = 0;
            if(codeMemory.getFromAddress(linePointer+1).equals("10000000")) {
                doDodania = ports.get("P0");
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10010000")) {
                doDodania = ports.get("P1");
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10100000")) {
                doDodania = ports.get("P2");
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10110000")) {
                doDodania = ports.get("P3");
            }

            int obecnaWartosc = registers.get("A");
            int wynik = obecnaWartosc + doDodania;
            if(wynik>255) {
                wynik -=256;
                psw.put("C",true);
            }
            else {
                psw.put("C",false);
            }
            registers.put("A",wynik);
            linePointer+=2;
        }
        else if(toExecute.equals("00110101")) {
            int doDodania = 0;
            if(codeMemory.getFromAddress(linePointer+1).equals("10000000")) {
                doDodania = ports.get("P0");
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10010000")) {
                doDodania = ports.get("P1");
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10100000")) {
                doDodania = ports.get("P2");
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10110000")) {
                doDodania = ports.get("P3");
            }

            int obecnaWartosc = registers.get("A");
            int wynik = obecnaWartosc + doDodania;
            if(psw.get("C"))
                wynik+=1;
            if(wynik>255) {
                wynik -=256;
                psw.put("C",true);
            }
            else {
                psw.put("C",false);
            }
            registers.put("A",wynik);
            linePointer+=2;
        }
        else if(toExecute.equals("10010100")) { //SUBB A,#01h
            machineCycle();
            int c = psw.get("C") ? 1 : 0;
            int obecnaWartosc = registers.get("A");
            int doOdjecia = Integer.parseInt(codeMemory.getFromAddress(linePointer+1),2);
            int wynik = obecnaWartosc - c - doOdjecia;
            if(wynik<0) {
                wynik +=256;
                psw.put("C",true);
            }
            else {
                psw.put("C",false);
            }
            registers.put("A",wynik);
            linePointer+=2;
        }

        else if(toExecute.substring(0,5).equals("10011")) { //SUBB A,Rx
            machineCycle();
            int c = psw.get("C") ? 1 : 0;
            int rejestr = Integer.parseInt(toExecute.substring(5,8),2);
            String rejestrString = "R" + rejestr;
            int doOdjecia = registers.get(rejestrString);
            int obecnaWartosc = registers.get("A");
            int wynik = obecnaWartosc - c - doOdjecia;
            if(wynik<0) {
                wynik +=256;
                psw.put("C",true);
            }
            else {
                psw.put("C",false);
            }
            registers.put("A",wynik);
            linePointer+=1;
        }
        else if(toExecute.equals("10010101")) { //SUBB A,Px
            int doOdjecia = 0;
            int c = psw.get("C") ? 1 : 0;
            if(codeMemory.getFromAddress(linePointer+1).equals("10000000")) {
                doOdjecia = ports.get("P0");
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10010000")) {
                doOdjecia = ports.get("P1");
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10100000")) {
                doOdjecia = ports.get("P2");
            }
            if(codeMemory.getFromAddress(linePointer+1).equals("10110000")) {
                doOdjecia = ports.get("P3");
            }

            int obecnaWartosc = registers.get("A");
            int wynik = obecnaWartosc - c - doOdjecia;
            if(wynik<0) {
                wynik +=256;
                psw.put("C",true);
            }
            else {
                psw.put("C",false);
            }
            registers.put("A",wynik);
            linePointer+=2;
        }
        else if(toExecute.equals("11010011")) { //SETB C
            machineCycle();
            psw.put("C",true);
            linePointer++;
        }
        else if(toExecute.equals("11100100")) { //CLR A
            machineCycle();
            registers.put("A",0);
            linePointer++;
        }
        else if(toExecute.equals("11000011")) { //CLR C
            machineCycle();
            psw.put("C",false);
            linePointer++;
        }
        refreshStatusRegister();
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

    public void resetCounter(){
        linePointer = 0;
    }

    private int linePointer;
    private Map<String,Integer> registers = new HashMap<>();
    private Map<String,Boolean> psw = new HashMap<>();
    public CodeMemory codeMemory = new CodeMemory();
    public Map<String,Integer> ports = new HashMap<>();

    public Map<String,String> bitMap = new HashMap<>();

    public String getKeyFromBitMap(String toFind){
        for(String s:bitMap.keySet()) {
            if(bitMap.get(s).equals(toFind))
                return s;
        }
        return "";
    }
}
