package components;

import core.Main;
import elements.Instruction;
import exceptions.CompilingException;
import javafx.util.Pair;
import stages.MainStage;

import java.util.ArrayList;

/**
 * Created by Mateusz on 18.04.2017.
 * Project MkSim51
 */
public class CodeMemory {
    public CodeMemory(){
        emulatedCodeMemory = new ArrayList<>();
        for(int i = 0; i < 8192;i++) {
            emulatedCodeMemory.add(new Instruction(null,null,null));
        }
    }
    public Instruction getFromAddress(int line) {
        return emulatedCodeMemory.get(line);
    }
    public void inputToAddress(int line,Instruction instruction) {
        emulatedCodeMemory.set(line,instruction);
    }
    public void inputToAdress(String line,Instruction instruction) {
        //TODO Wpisywanie do pamięci po numerze linii w różnych systemach pozycyjnych (0000h,0000d itp)
    }
    private ArrayList<Instruction> emulatedCodeMemory;

    private ArrayList<Pair<String,Integer>> labels = new ArrayList<>();

    public int getLineFromLabel(String label) {
        int line = -1;
        for(Pair pair : labels) {
            if(pair.getKey().equals(label)) {
                line = (int)pair.getValue();
                break;
            }
        }
        return line;

    }

    private void show() {
        for(int i = 0; i < 10;i++) {
            System.out.println(emulatedCodeMemory.get(i).getMnemonic() + " " + emulatedCodeMemory.get(i).getParam1() + " " +emulatedCodeMemory.get(i).getParam2());
        }
        for(Pair pair : labels) {
            System.out.println(pair.getKey() + " " + pair.getValue());
        }
    }

    public void setMemory(String[] lines) throws CompilingException {
        String newEditorText="";
        int pointer = 0;
        emulatedCodeMemory = new ArrayList<>();
        for(int i = 0; i < 8192;i++) {
            emulatedCodeMemory.add(new Instruction(null,null,null));
        }
        labels.clear();
        for(String line : lines) {
            try {
                int komentarz = -1;
                for(int i = 0; i < line.length();i++) {
                    if(line.charAt(i) == '%') {
                        komentarz = i;
                        break;
                    }
                }
                if(komentarz >0 ) {
                    line = line.substring(0,komentarz);
                }
                if(komentarz == 0) {
                    line = "";
                }
                if(line.length()>0) {

                    line = line.replace(',', ' ');
                    String[] splittedLine = line.split(" ");
                    int result = Main.cpu.isMnemonic(splittedLine[0].toUpperCase());
                    if(result>=0 && splittedLine.length-1==result) {
                        if(splittedLine.length==3) {
                            emulatedCodeMemory.set(pointer, new Instruction(splittedLine[0].toUpperCase(), splittedLine[1].toUpperCase(), splittedLine[2].toUpperCase()));
                            int mnemonicSize = Main.cpu.getMnemonicSize(splittedLine[0].toUpperCase());
                            pointer = pointer + mnemonicSize;
                            newEditorText += "\t" + splittedLine[0].toUpperCase() + " " + splittedLine[1] + "," + splittedLine[2] + "\n";
                        }
                        if(splittedLine.length==2) {
                            emulatedCodeMemory.set(pointer, new Instruction(splittedLine[0].toUpperCase(), splittedLine[1].toUpperCase(), null));
                            int mnemonicSize = Main.cpu.getMnemonicSize(splittedLine[0].toUpperCase());
                            pointer = pointer + mnemonicSize;
                            newEditorText += "\t" + splittedLine[0].toUpperCase() + " " + splittedLine[1] + "\n";
                        }
                    }
                    else if(splittedLine.length==1 && splittedLine[0].charAt(splittedLine[0].length()-1)==':') {
                        if(getLineFromLabel(splittedLine[0].toUpperCase().substring(0,splittedLine[0].length()-1))==-1) {
                            labels.add(new Pair<>(splittedLine[0].toUpperCase().substring(0, splittedLine[0].length() - 1), pointer));
                            newEditorText += "\t" + splittedLine[0] + "\n";
                        }
                        else
                            throw new CompilingException();
                    }
                    else if(splittedLine[0].equals("ORG") && splittedLine.length==2) {
                        if(splittedLine[1].toUpperCase().charAt(splittedLine[1].length()-1)=='D') {
                            pointer = Integer.parseInt(splittedLine[1].substring(0,splittedLine[1].length()-1));
                            newEditorText += "\t" + "ORG " + splittedLine[1] + "\n";
                        }
                        if(splittedLine[1].toUpperCase().charAt(splittedLine[1].length()-1)=='H') {
                            pointer = Integer.parseInt(splittedLine[1].substring(0,splittedLine[1].length()-1),16);
                            newEditorText += "\t" + "ORG " + splittedLine[1] + "\n";
                        }
                        if(splittedLine[1].toUpperCase().charAt(splittedLine[1].length()-1)=='B') {
                            pointer = Integer.parseInt(splittedLine[1].substring(0,splittedLine[1].length()-1),2);
                            newEditorText += "\t" + "ORG " + splittedLine[1] + "\n";
                        }
                    }
                }


            } catch (Exception e) {
                throw new CompilingException();
            }
            Main.stage.setEditorText(newEditorText);
        }
        //show();
    }

}
