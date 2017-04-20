package elements;

/**
 * Created by Mateusz on 20.04.2017.
 * Project 8051 MCU Emulator
 */
public class Mnemonic {

    public Mnemonic(String name,int paramsNumber,int time,int size) {
        this.name = name;
        this.paramsNumber = paramsNumber;
        this.time = time;
        this.size = size;
    }

    public String getName() {
        return name;
    }

    public int getParamsNumber() {
        return paramsNumber;
    }

    public int getTime() {
        return time;
    }

    public int getSize() {
        return size;
    }

    String name;
    int paramsNumber;
    int time;
    int size;
}
