package microcontroller;

import core.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Board {
    public Board(){

        stanPinow.put("P0.0",new ArrayList<>());
        stanPinow.put("P0.1",new ArrayList<>());
        stanPinow.put("P0.2",new ArrayList<>());
        stanPinow.put("P0.3",new ArrayList<>());
        stanPinow.put("P0.4",new ArrayList<>());
        stanPinow.put("P0.5",new ArrayList<>());
        stanPinow.put("P0.6",new ArrayList<>());
        stanPinow.put("P0.7",new ArrayList<>());

        stanPinow.put("P1.0",new ArrayList<>());
        stanPinow.put("P1.1",new ArrayList<>());
        stanPinow.put("P1.2",new ArrayList<>());
        stanPinow.put("P1.3",new ArrayList<>());
        stanPinow.put("P1.4",new ArrayList<>());
        stanPinow.put("P1.5",new ArrayList<>());
        stanPinow.put("P1.6",new ArrayList<>());
        stanPinow.put("P1.7",new ArrayList<>());

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

        for(String key : stanPinow.keySet()) {
            stanPinow.get(key).add("1");
            stanPinow.get(key).add("1");
            stanPinow.get(key).add("1");
        }
    }

    public void setGround(String pin,int position) {
        try {
            stanPinow.get(pin).set(position,"0");
        } catch (Exception ignored) {
        }
    }

    public void setCurrent(String pin,int position) {
        try {
            stanPinow.get(pin).set(position,"1");
        } catch (Exception ignored) {
        }
    }

    public String getState(String s) {
        if(stanPinow.get(s).contains("0"))
            return "0";
        else
            return "1";
    }
    public String getPortState(String port) {
        StringBuilder state = new StringBuilder();
        try {
            for (int i = 7; i >= 0; i--) {
                if (stanPinow.get(port + "." + i).contains("0"))
                    state.append("0");
                else
                    state.append("1");
            }
            return state.toString();
        } catch (Exception e) {
            return null;
        }
    }

    public void reset() {
        for(String s : stanPinow.keySet()) {
            stanPinow.get(s).clear();
        }
        for(String key : stanPinow.keySet()) {
            stanPinow.get(key).add("1");
            stanPinow.get(key).add("1");
            stanPinow.get(key).add("1");
        }
    }

    public void set(){


        for(int i = 0; i < 8; i ++) {
            if(Main.cpu.mainMemory.latcherP0.charAt(i)=='0')
                Main.cpu.mainMemory.setBitExternal(Main.cpu.codeMemory.bitAddresses.get("P0." + (7-i)),false);
            else {
                if(stanPinow.get("P0." + (7-i)).contains("0")) {
                    Main.cpu.mainMemory.setBitExternal(Main.cpu.codeMemory.bitAddresses.get("P0." + (7-i)),false);
                }
                else {
                    Main.cpu.mainMemory.setBitExternal(Main.cpu.codeMemory.bitAddresses.get("P0." + (7-i)),true);
                }
            }
        }

        for(int i = 0; i < 8; i ++) {
            if(Main.cpu.mainMemory.latcherP1.charAt(i)=='0')
                Main.cpu.mainMemory.setBitExternal(Main.cpu.codeMemory.bitAddresses.get("P1." + (7-i)),false);
            else {
                if(stanPinow.get("P1." + (7-i)).contains("0")) {
                    Main.cpu.mainMemory.setBitExternal(Main.cpu.codeMemory.bitAddresses.get("P1." + (7-i)),false);
                }
                else {
                    Main.cpu.mainMemory.setBitExternal(Main.cpu.codeMemory.bitAddresses.get("P1." + (7-i)),true);
                }
            }
        }

        for(int i = 0; i < 8; i ++) {
            if(Main.cpu.mainMemory.latcherP2.charAt(i)=='0')
                Main.cpu.mainMemory.setBitExternal(Main.cpu.codeMemory.bitAddresses.get("P2." + (7-i)),false);
            else {
                if(stanPinow.get("P2." + (7-i)).contains("0")) {
                    Main.cpu.mainMemory.setBitExternal(Main.cpu.codeMemory.bitAddresses.get("P2." + (7-i)),false);
                }
                else {
                    Main.cpu.mainMemory.setBitExternal(Main.cpu.codeMemory.bitAddresses.get("P2." + (7-i)),true);
                }
            }
        }

        for(int i = 0; i < 8; i ++) {
            if(Main.cpu.mainMemory.latcherP3.charAt(i)=='0')
                Main.cpu.mainMemory.setBitExternal(Main.cpu.codeMemory.bitAddresses.get("P3." + (7-i)),false);
            else {
                if(stanPinow.get("P3." + (7-i)).contains("0")) {
                    Main.cpu.mainMemory.setBitExternal(Main.cpu.codeMemory.bitAddresses.get("P3." + (7-i)),false);
                }
                else {
                    Main.cpu.mainMemory.setBitExternal(Main.cpu.codeMemory.bitAddresses.get("P3." + (7-i)),true);
                }
            }
        }
    }


    private Map<String,ArrayList<String>> stanPinow = new HashMap<>();

}
