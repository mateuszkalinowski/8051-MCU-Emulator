package microcontroller;

import core.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
    public Board(){
        stanPinow.put("P2.0",new ArrayList<>());
        stanPinow.put("P2.1",new ArrayList<>());
        stanPinow.put("P2.2",new ArrayList<>());
        stanPinow.put("P2.3",new ArrayList<>());
        stanPinow.put("P2.4",new ArrayList<>());
        stanPinow.put("P2.5",new ArrayList<>());
        stanPinow.put("P2.6",new ArrayList<>());
        stanPinow.put("P2.7",new ArrayList<>());

        stanPinow.put("P3.0",new ArrayList<>());
        stanPinow.put("P3.1",new ArrayList<>());
        stanPinow.put("P3.2",new ArrayList<>());
        stanPinow.put("P3.3",new ArrayList<>());
        stanPinow.put("P3.4",new ArrayList<>());
        stanPinow.put("P3.5",new ArrayList<>());
        stanPinow.put("P3.6",new ArrayList<>());
        stanPinow.put("P3.7",new ArrayList<>());
    }

    public String getState(String s) {
        if(stanPinow.get(s).contains("0"))
            return "0";
        else
            return "1";
    }
    public void set(){
        for(String key : stanPinow.keySet()) {
            if(stanPinow.get(key).contains("0")) {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(key), false);
            }
            else {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(key), true);
            }
        }
    }


    private Map<String,ArrayList<String>> stanPinow = new HashMap<>();

}
