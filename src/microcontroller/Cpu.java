package microcontroller;

import components.CodeMemory;
import core.Main;
import elements.Instruction;
import exceptions.InstructionException;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by Mateusz on 18.04.2017.
 * Project MkSim51
 */
public class Cpu {

    public void resetCounter(){
        linePointer=0;
        acc = 0;
        r0 = 0;
        r1 = 0;
        r2 = 0;
        r3 = 0;
        r4 = 0;
        r5 = 0;
        r6 = 0;
        r7 = 0;
    }

    public void machineCycle(){

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
            machineCycle();
            refreshGui();
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

                if(toExecute.getParam1().equals("A")) {
                    acc = intNumberToAdd;
                }

                if(toExecute.getParam1().equals("R0")) {
                    r0 = intNumberToAdd;
                }
                if(toExecute.getParam1().equals("R1")) {
                    r1 = intNumberToAdd;
                }
                if(toExecute.getParam1().equals("R2")) {
                    r2 = intNumberToAdd;
                }
                if(toExecute.getParam1().equals("R3")) {
                    r3 = intNumberToAdd;
                }
                if(toExecute.getParam1().equals("R4")) {
                    r4 = intNumberToAdd;
                }
                if(toExecute.getParam1().equals("R5")) {
                    r5 = intNumberToAdd;
                }
                if(toExecute.getParam1().equals("R6")) {
                    r6 = intNumberToAdd;
                }
                if(toExecute.getParam1().equals("R7")) {
                    r7 = intNumberToAdd;
                }

            }
            machineCycle();
            linePointer++;
            refreshGui();
        }
    }

    public Cpu(){
        knownMnemonics.add(new Pair<>("MOV",2));
        knownMnemonics.add(new Pair<>("DJNZ",2));
        knownMnemonics.add(new Pair<>("LJMP",1));//OK
        linePointer = 0;
    }
    public int isMnemonic(String name) {
        for (Pair pair : knownMnemonics) {
            if(pair.getKey().toString().toUpperCase().equals(name))
                return (int) pair.getValue();
        }
        return -1;
    }
    private int linePointer;

    private int acc = 0;

    private int r0 = 0;
    private int r1 = 0;
    private int r2 = 0;
    private int r3 = 0;
    private int r4 = 0;
    private int r5 = 0;
    private int r6 = 0;
    private int r7 = 0;

    public CodeMemory codeMemory = new CodeMemory();

    public int getLinePointer() {
        return linePointer;
    }

    public void setLinePointer(int linePointer) {
        this.linePointer = linePointer;
    }

    private ArrayList<Pair<String,Integer>> knownMnemonics = new ArrayList<>();

    public void refreshGui(){
        Main.stage.r0TextField.setText(Integer.toHexString(r0).toUpperCase()+"h");
        Main.stage.r1TextField.setText(Integer.toHexString(r1).toUpperCase()+"h");
        Main.stage.r2TextField.setText(Integer.toHexString(r2).toUpperCase()+"h");
        Main.stage.r3TextField.setText(Integer.toHexString(r3).toUpperCase()+"h");
        Main.stage.r4TextField.setText(Integer.toHexString(r4).toUpperCase()+"h");
        Main.stage.r5TextField.setText(Integer.toHexString(r5).toUpperCase()+"h");
        Main.stage.r6TextField.setText(Integer.toHexString(r6).toUpperCase()+"h");
        Main.stage.r7TextField.setText(Integer.toHexString(r7).toUpperCase()+"h");

        Main.stage.accumulatorTextField.setText(Integer.toHexString(acc).toUpperCase()+"h");

    }
}
