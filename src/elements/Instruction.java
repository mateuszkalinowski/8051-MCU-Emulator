package elements;

/**
 * Created by Mateusz on 18.04.2017.
 * Project InferenceEngine
 */
public class Instruction {
    public Instruction(String mnemonic,String param1,String param2){
        this.mnemonic = mnemonic;
        this.param1 = param1;
        this.param2 = param2;
    }

    private String mnemonic;
    private String param1;
    private String param2;
}
