package exceptions;

/**
 * Created by Mateusz on 19.04.2017.
 * Project MkSim51
 */
public class CompilingException extends Exception {
    public CompilingException(){
        super();
    }
    public CompilingException(String message) {
        super(message);
    }
}
