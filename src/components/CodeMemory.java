package components;

import elements.Instruction;

import java.util.ArrayList;

/**
 * Created by Mateusz on 18.04.2017.
 * Project InferenceEngine
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
}
