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
        knownMnemonics.add(new Mnemonic("MOV",2,2,2));//OK
        knownMnemonics.add(new Mnemonic("DJNZ",2,2,2));//OK
        knownMnemonics.add(new Mnemonic("LJMP",1,2,3));//OK
        knownMnemonics.add(new Mnemonic("RL",1,1,1));//OK
        knownMnemonics.add(new Mnemonic("RR",1,1,1));//OK
        knownMnemonics.add(new Mnemonic("RLC",1,1,1));
        knownMnemonics.add(new Mnemonic("RRC",1,1,1));
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
        Instruction toExecute = codeMemory.getFromAddress(linePointer);
        if(toExecute.getMnemonic().equals("LJMP")) {
            int numberLenght = toExecute.getParam1().length();
            int jump = 0;
            String number = toExecute.getParam1();
            if(number.charAt(numberLenght-1) == 'D') {
                jump = Integer.parseInt(number.substring(0,numberLenght-1));
            }
            else if(number.charAt(numberLenght-1) == 'H') {
                jump = Integer.parseInt(number.substring(0,numberLenght-1),16);
            }
            else if(number.charAt(numberLenght-1) == 'B') {
                jump = Integer.parseInt(number.substring(0,numberLenght-1),2);
            }
            else {
                int jumpNumber = codeMemory.getLineFromLabel(toExecute.getParam1());
                if (jumpNumber==-1) {
                    throw new InstructionException();
                }
                else
                    jump = jumpNumber;

            }
            linePointer = jump;
        }
        else if (toExecute.getMnemonic().equals("MOV")) {
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

                if(registers.containsKey(toExecute.getParam1())) {
                    registers.put(toExecute.getParam1(),intNumberToAdd);
                }
                else {
                    if(ports.containsKey(toExecute.getParam1())) {
                        ports.put(toExecute.getParam1(),intNumberToAdd);
                    }
                    else
                        throw new InstructionException();
                }

            }
            else if(toExecute.getParam1().equals("A")) {
                try {
                    if(registers.containsKey(toExecute.getParam2())) {
                        int intNumberToAdd = registers.get(toExecute.getParam2());
                        registers.put("A", intNumberToAdd);
                    }
                    else if(ports.containsKey(toExecute.getParam2())) {
                        int intNumberToAdd = ports.get(toExecute.getParam2());
                        registers.put("A", intNumberToAdd);
                    }

                } catch (Exception e) {
                    System.out.println("Błąd w mov");
                    throw new InstructionException();
                }
            }
            else if(toExecute.getParam2().equals("A")) {
                try {
                    int intNumberToAdd = registers.get("A");
                    if(registers.containsKey(toExecute.getParam1())) {
                        registers.put(toExecute.getParam1(), intNumberToAdd);
                    }
                    else if(ports.containsKey(toExecute.getParam1())) {
                        ports.put(toExecute.getParam1(),intNumberToAdd);
                    }
                } catch (Exception e) {
                    System.out.println("Błąd w mov");
                    throw new InstructionException();
                }
            }
            else {
                System.out.println("Błąd w mov");
                throw new InstructionException();
            }
            int mnemonicSize = Main.cpu.getMnemonicSize("MOV");
            linePointer = linePointer + mnemonicSize;
        }
        else if(toExecute.getMnemonic().equals("DJNZ")) {
            try {
                int wartosc = registers.get(toExecute.getParam1());
                wartosc--;
                if(wartosc==-1)
                    wartosc=255;

                registers.put(toExecute.getParam1(),wartosc);

                if(wartosc!=0) {
                    linePointer = codeMemory.getLineFromLabel(toExecute.getParam2());
                }
                if(wartosc==0) {
                    int mnemonicSize = Main.cpu.getMnemonicSize("DJNZ");
                    linePointer += mnemonicSize;
                }
            }
            catch (Exception e) {
                throw new InstructionException();
            }
        }
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

        int machineCyclesNumber = Main.cpu.getMnemonicTime(toExecute.getMnemonic().toUpperCase());
        for (;machineCyclesNumber>0;machineCyclesNumber--) {
            machineCycle();
        }

        refreshGui();
    }

    public int isMnemonic(String name) {
        for(Mnemonic mnemonic : knownMnemonics) {
            if(mnemonic.getName().toUpperCase().equals(name))
                return (int) mnemonic.getParamsNumber();
        }
        return -1;
    }
    public int getMnemonicTime(String name) {
        for(Mnemonic mnemonic : knownMnemonics) {
            if(mnemonic.getName().toUpperCase().equals(name))
                return (int) mnemonic.getTime();
        }
        return 0;
    }
    public int getMnemonicSize(String name) {
        for(Mnemonic mnemonic : knownMnemonics) {
            if(mnemonic.getName().toUpperCase().equals(name))
                return (int) mnemonic.getSize();
        }
        return 0;
    }
    private int linePointer;


    private Map<String,Integer> registers = new HashMap<>();

    private Map<String,Boolean> psw = new HashMap<>();

    public CodeMemory codeMemory = new CodeMemory();

    public int getLinePointer() {
        return linePointer;
    }

    public void setLinePointer(int linePointer) {
        this.linePointer = linePointer;
    }

    private Map<String,Integer> ports = new HashMap<>();

    private ArrayList<Mnemonic> knownMnemonics = new ArrayList<>();

    public void refreshStatusRegister(){
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
}
