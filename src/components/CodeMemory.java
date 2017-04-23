package components;

import core.Main;
import exceptions.CompilingException;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by Mateusz on 18.04.2017.
 * Project MkSim51
 */
public class CodeMemory {
    public CodeMemory(){
        emulatedCodeMemory = new ArrayList<>();
        for(int i = 0; i < 8192;i++) {
            emulatedCodeMemory.add("00000000");
        }
    }
    public String getFromAddress(int line) {
        return emulatedCodeMemory.get(line);
    }

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
        for(int i = 0; i < 20;i++) {
            System.out.println(Integer.toHexString(Integer.parseInt(emulatedCodeMemory.get(i),2)));
        }
        for(Pair pair : labels) {
            System.out.println(pair.getKey() + " "+pair.getValue());
        }
    }

    public void setMemory(String[] lines) throws CompilingException {
        String newEditorText="";
        int pointer = 0;
        emulatedCodeMemory = new ArrayList<>();
        for(int i = 0; i < 8192;i++) {
            emulatedCodeMemory.add("00000000");
        }
        labels.clear();
        for(String line : lines) {
                String backupLinii = line;
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

                    if(splittedLine.length==1 && splittedLine[0].charAt(splittedLine[0].length()-1)==':') {
                        if(getLineFromLabel(splittedLine[0].toUpperCase().substring(0,splittedLine[0].length()-1))==-1) {
                            labels.add(new Pair<>(splittedLine[0].toUpperCase().substring(0, splittedLine[0].length() - 1), pointer));
                        }
                        else
                            throw new CompilingException();
                    }
                    else if(splittedLine[0].equals("ORG") && splittedLine.length==2) {
                        if(splittedLine[1].toUpperCase().charAt(splittedLine[1].length()-1)=='D') {
                            pointer = Integer.parseInt(splittedLine[1].substring(0,splittedLine[1].length()-1));
                        }
                        if(splittedLine[1].toUpperCase().charAt(splittedLine[1].length()-1)=='H') {
                            pointer = Integer.parseInt(splittedLine[1].substring(0,splittedLine[1].length()-1),16);
                        }
                        if(splittedLine[1].toUpperCase().charAt(splittedLine[1].length()-1)=='B') {
                            pointer = Integer.parseInt(splittedLine[1].substring(0,splittedLine[1].length()-1),2);
                        }
                    }
                    else {
                        if(splittedLine[0].toUpperCase().equals("LJMP")) {
                            emulatedCodeMemory.set(pointer,"00000010");
                            if(splittedLine[1].charAt(splittedLine[1].length()-1)=='b' || splittedLine[1].charAt(splittedLine[1].length()-1)=='d' || splittedLine[1].charAt(splittedLine[1].length()-1)=='h') {
                                String destination = make16DigitsStringFromNumber(splittedLine[1]);
                                emulatedCodeMemory.set(pointer+1,destination.substring(0,8));
                                emulatedCodeMemory.set(pointer+2,destination.substring(8,16));
                            }
                            else {
                                emulatedCodeMemory.set(pointer+1,splittedLine[1].toUpperCase());
                            }
                            pointer+=3;
                        }
                        else if(splittedLine[0].toUpperCase().equals("DJNZ")) {
                            if(splittedLine[1].toUpperCase().equals("R0"))
                                emulatedCodeMemory.set(pointer,"11011000");
                            else if(splittedLine[1].toUpperCase().equals("R1"))
                                emulatedCodeMemory.set(pointer,"11011001");
                            else if(splittedLine[1].toUpperCase().equals("R2"))
                                emulatedCodeMemory.set(pointer,"11011010");
                            else if(splittedLine[1].toUpperCase().equals("R3"))
                                emulatedCodeMemory.set(pointer,"11011011");
                            else if(splittedLine[1].toUpperCase().equals("R4"))
                                emulatedCodeMemory.set(pointer,"11011100");
                            else if(splittedLine[1].toUpperCase().equals("R5"))
                                emulatedCodeMemory.set(pointer,"11011101");
                            else if(splittedLine[1].toUpperCase().equals("R6"))
                                emulatedCodeMemory.set(pointer,"11011110");
                            else if(splittedLine[1].toUpperCase().equals("R7"))
                                emulatedCodeMemory.set(pointer,"11011111");
                            else {
                                throw new CompilingException("Nieznany Rejestr: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                            }

                            emulatedCodeMemory.set(pointer+1,splittedLine[2].toUpperCase());
                            pointer+=2;
                        } else if(splittedLine[0].toUpperCase().equals("JC")) {
                            if(splittedLine.length!=2) {
                                throw new CompilingException("Niepoprawna ilosć argumentów: '" + backupLinii + "'" );
                            }
                            emulatedCodeMemory.set(pointer,"01000000");
                            emulatedCodeMemory.set(pointer+1,splittedLine[1].toUpperCase());
                            pointer+=2;
                        } else if(splittedLine[0].toUpperCase().equals("JB")) {
                            if(splittedLine.length!=3) {
                                throw new CompilingException("Niepoprawna ilosć argumentów: '" + backupLinii + "'" );
                            }
                            emulatedCodeMemory.set(pointer,"00100000");
                            if(Main.cpu.bitMap.containsKey(splittedLine[1].toUpperCase())) {
                                emulatedCodeMemory.set(pointer+1,Main.cpu.bitMap.get(splittedLine[1].toUpperCase()));
                                emulatedCodeMemory.set(pointer+2,splittedLine[2].toUpperCase());
                            }
                            else {
                                throw new CompilingException("Niepoprawna bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'" );
                            }
                            pointer+=3;

                        }
                        else if(splittedLine[0].toUpperCase().equals("MOV")) {
                            if(splittedLine.length!=3)
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");

                            if(splittedLine[1].toUpperCase().equals("A")) {
                                if(splittedLine[2].charAt(0)=='#') {
                                    emulatedCodeMemory.set(pointer,"01110100");
                                    try {
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                    }
                                    catch (NumberFormatException e) {
                                        throw new CompilingException("Nieznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                    }
                                    pointer+=2;
                                }
                                else if(splittedLine[2].toUpperCase().charAt(0)=='R') {
                                    if(splittedLine[2].toUpperCase().equals("R0"))
                                        emulatedCodeMemory.set(pointer,"11101000");
                                    else if(splittedLine[2].toUpperCase().equals("R1"))
                                        emulatedCodeMemory.set(pointer,"11101001");
                                    else if(splittedLine[2].toUpperCase().equals("R2"))
                                        emulatedCodeMemory.set(pointer,"11101010");
                                    else if(splittedLine[2].toUpperCase().equals("R3"))
                                        emulatedCodeMemory.set(pointer,"11101011");
                                    else if(splittedLine[2].toUpperCase().equals("R4"))
                                        emulatedCodeMemory.set(pointer,"11101100");
                                    else if(splittedLine[2].toUpperCase().equals("R5"))
                                        emulatedCodeMemory.set(pointer,"11101101");
                                    else if(splittedLine[2].toUpperCase().equals("R6"))
                                        emulatedCodeMemory.set(pointer,"11101110");
                                    else if(splittedLine[2].toUpperCase().equals("R7"))
                                        emulatedCodeMemory.set(pointer,"11101111");
                                    else
                                        throw new CompilingException("Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                    pointer+=1;
                                }
                                else if(splittedLine[2].toUpperCase().charAt(0)=='P') {
                                    emulatedCodeMemory.set(pointer,"11100101");
                                    if(splittedLine[2].toUpperCase().equals("P0"))
                                        emulatedCodeMemory.set(pointer+1,"10000000");
                                    else if(splittedLine[2].toUpperCase().equals("P1"))
                                        emulatedCodeMemory.set(pointer+1,"10010000");
                                    else if(splittedLine[2].toUpperCase().equals("P2"))
                                        emulatedCodeMemory.set(pointer+1,"10100000");
                                    else if(splittedLine[2].toUpperCase().equals("P3"))
                                        emulatedCodeMemory.set(pointer+1,"10110000");
                                    else
                                        throw new CompilingException("Nieznany Port: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                    pointer+=2;
                                }
                                else {
                                    throw new CompilingException("Nieznany Argument: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                }

                            }
                            else if(splittedLine[1].toUpperCase().charAt(0)=='R') {
                                if(splittedLine[2].charAt(0)=='#') {
                                    if(splittedLine[1].toUpperCase().equals("R0"))
                                        emulatedCodeMemory.set(pointer,"01111000");
                                    else if(splittedLine[1].toUpperCase().equals("R1"))
                                        emulatedCodeMemory.set(pointer,"01111001");
                                    else if(splittedLine[1].toUpperCase().equals("R2"))
                                        emulatedCodeMemory.set(pointer,"01111010");
                                    else if(splittedLine[1].toUpperCase().equals("R3"))
                                        emulatedCodeMemory.set(pointer,"01111011");
                                    else if(splittedLine[1].toUpperCase().equals("R4"))
                                        emulatedCodeMemory.set(pointer,"01111100");
                                    else if(splittedLine[1].toUpperCase().equals("R5"))
                                        emulatedCodeMemory.set(pointer,"01111101");
                                    else if(splittedLine[1].toUpperCase().equals("R6"))
                                        emulatedCodeMemory.set(pointer,"01111101");
                                    else if(splittedLine[1].toUpperCase().equals("R7"))
                                        emulatedCodeMemory.set(pointer,"01111111");
                                    else
                                        throw new CompilingException("Nieznany Rejestr: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                                    try {
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                    } catch (NumberFormatException e) {
                                        throw new CompilingException("Nierozpoznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                    }
                                    pointer+=2;
                                }
                                else if(splittedLine[2].toUpperCase().equals("A")) {
                                    if(splittedLine[1].toUpperCase().equals("R0"))
                                        emulatedCodeMemory.set(pointer,"11111000");
                                    else if(splittedLine[1].toUpperCase().equals("R1"))
                                        emulatedCodeMemory.set(pointer,"11111001");
                                    else if(splittedLine[1].toUpperCase().equals("R2"))
                                        emulatedCodeMemory.set(pointer,"11111010");
                                    else if(splittedLine[1].toUpperCase().equals("R3"))
                                        emulatedCodeMemory.set(pointer,"11111011");
                                    else if(splittedLine[1].toUpperCase().equals("R4"))
                                        emulatedCodeMemory.set(pointer,"11111100");
                                    else if(splittedLine[1].toUpperCase().equals("R5"))
                                        emulatedCodeMemory.set(pointer,"11111101");
                                    else if(splittedLine[1].toUpperCase().equals("R6"))
                                        emulatedCodeMemory.set(pointer,"11111101");
                                    else if(splittedLine[1].toUpperCase().equals("R7"))
                                        emulatedCodeMemory.set(pointer,"11111111");
                                    else
                                        throw new CompilingException("Nieznany Rejestr: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");

                                    pointer+=1;
                                } else {
                                        throw new CompilingException("Nieznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                }
                            }  else if(splittedLine[1].toUpperCase().charAt(0)=='P') {
                                if(splittedLine[2].toUpperCase().equals("A")) {
                                    emulatedCodeMemory.set(pointer,"11110101");
                                    if(splittedLine[1].toUpperCase().equals("P0"))
                                        emulatedCodeMemory.set(pointer+1,"10000000");
                                    else if(splittedLine[1].toUpperCase().equals("P1"))
                                        emulatedCodeMemory.set(pointer+1,"10010000");
                                    else if(splittedLine[1].toUpperCase().equals("P2"))
                                        emulatedCodeMemory.set(pointer+1,"10100000");
                                    else if(splittedLine[1].toUpperCase().equals("P3"))
                                        emulatedCodeMemory.set(pointer+1,"10110000");
                                    else
                                        throw new CompilingException("Nieznany Port: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                    pointer+=2;
                                }
                                else if(splittedLine[2].toUpperCase().charAt(0)=='P') {
                                    emulatedCodeMemory.set(pointer,"10000101");
                                    if(splittedLine[1].toUpperCase().equals("P0"))
                                        emulatedCodeMemory.set(pointer+1,"10000000");
                                    else if(splittedLine[1].toUpperCase().equals("P1"))
                                        emulatedCodeMemory.set(pointer+1,"10010000");
                                    else if(splittedLine[1].toUpperCase().equals("P2"))
                                        emulatedCodeMemory.set(pointer+1,"10100000");
                                    else if(splittedLine[1].toUpperCase().equals("P3"))
                                        emulatedCodeMemory.set(pointer+1,"10110000");
                                    else
                                        throw new CompilingException("Nieznany Port: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");

                                    if(splittedLine[2].toUpperCase().equals("P0"))
                                        emulatedCodeMemory.set(pointer+2,"10000000");
                                    else if(splittedLine[2].toUpperCase().equals("P1"))
                                        emulatedCodeMemory.set(pointer+2,"10010000");
                                    else if(splittedLine[2].toUpperCase().equals("P2"))
                                        emulatedCodeMemory.set(pointer+2,"10100000");
                                    else if(splittedLine[2].toUpperCase().equals("P3"))
                                        emulatedCodeMemory.set(pointer+2,"10110000");
                                    else
                                        throw new CompilingException("Nieznany Port: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");
                                    pointer+=3;
                                }
                                else if(splittedLine[2].charAt(0)=='#') {
                                    emulatedCodeMemory.set(pointer,"01110101");
                                    String port = "";
                                    if(splittedLine[1].toUpperCase().equals("P0"))
                                        emulatedCodeMemory.set(pointer+1,"10000000");
                                    else if(splittedLine[1].toUpperCase().equals("P1"))
                                        emulatedCodeMemory.set(pointer+1,"10010000");
                                    else if(splittedLine[1].toUpperCase().equals("P2"))
                                        emulatedCodeMemory.set(pointer+1,"10100000");
                                    else if(splittedLine[1].toUpperCase().equals("P3"))
                                        emulatedCodeMemory.set(pointer+1,"10110000");
                                    else
                                        throw new CompilingException("Nieznany Port: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                                    try {
                                        emulatedCodeMemory.set(pointer+2,make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                    }catch (NumberFormatException e) {
                                     throw new CompilingException("Nieznana Liczba: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                    }
                                    pointer+=3;
                                }
                                else {
                                    throw new CompilingException("Nieznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                }

                            }

                        }
                        else if(splittedLine[0].toUpperCase().equals("RL")) {
                            if(splittedLine.length!=2) {
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            }
                            if(splittedLine[1].toUpperCase().equals("A")) {
                                emulatedCodeMemory.set(pointer,"00100011");
                                pointer+=1;
                            }
                            else {
                                throw new CompilingException("Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                            }
                        }

                        else if(splittedLine[0].toUpperCase().equals("RLC")) {
                            if(splittedLine.length!=2) {
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            }
                            if(splittedLine[1].toUpperCase().equals("A")) {
                                emulatedCodeMemory.set(pointer,"00110011");
                                pointer+=1;
                            }
                            else {
                                throw new CompilingException("Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                            }
                        }

                        else if(splittedLine[0].toUpperCase().equals("RR")) {
                            if(splittedLine.length!=2) {
                                throw new CompilingException("Nierozpoznana linia: '" + backupLinii+"'");
                            }
                            if(splittedLine[1].toUpperCase().equals("A")) {
                                emulatedCodeMemory.set(pointer,"00000011");
                                pointer+=1;
                            }
                            else {
                                throw new CompilingException("Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                            }
                        }

                        else if(splittedLine[0].toUpperCase().equals("RRC")) {
                            if(splittedLine.length!=2) {
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            }
                            if(splittedLine[1].toUpperCase().equals("A")) {
                                emulatedCodeMemory.set(pointer,"00010011");
                                pointer+=1;
                            }
                            else {
                                throw new CompilingException("Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                            }
                        }
                        else if(splittedLine[0].toUpperCase().equals("ADD")) {
                            if(splittedLine.length!=3)
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            if(splittedLine[1].toUpperCase().equals("A")) {

                                if(splittedLine[1].toUpperCase().equals("A")) {
                                    if(splittedLine[2].charAt(0)=='#') {
                                        emulatedCodeMemory.set(pointer,"00100100");
                                        try {
                                            emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                        } catch (NumberFormatException e) {
                                            throw new CompilingException("Nierozpoznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");
                                        }
                                        pointer+=2;
                                    }
                                    else if(splittedLine[2].toUpperCase().charAt(0)=='R') {
                                        if(splittedLine[2].toUpperCase().equals("R0"))
                                            emulatedCodeMemory.set(pointer,"00101000");
                                        else if(splittedLine[2].toUpperCase().equals("R1"))
                                            emulatedCodeMemory.set(pointer,"00101001");
                                        else if(splittedLine[2].toUpperCase().equals("R2"))
                                            emulatedCodeMemory.set(pointer,"00101010");
                                        else if(splittedLine[2].toUpperCase().equals("R3"))
                                            emulatedCodeMemory.set(pointer,"00101011");
                                        else if(splittedLine[2].toUpperCase().equals("R4"))
                                            emulatedCodeMemory.set(pointer,"00101100");
                                        else if(splittedLine[2].toUpperCase().equals("R5"))
                                            emulatedCodeMemory.set(pointer,"00101101");
                                        else if(splittedLine[2].toUpperCase().equals("R6"))
                                            emulatedCodeMemory.set(pointer,"00101110");
                                        else if(splittedLine[2].toUpperCase().equals("R7"))
                                            emulatedCodeMemory.set(pointer,"00101111");
                                        else
                                            throw new CompilingException("Nierozpoznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                        pointer+=1;
                                    }
                                    else if(splittedLine[2].toUpperCase().charAt(0)=='P') {
                                        emulatedCodeMemory.set(pointer,"00100101");
                                        if(splittedLine[2].toUpperCase().equals("P0"))
                                            emulatedCodeMemory.set(pointer+1,"10000000");
                                        else if(splittedLine[2].toUpperCase().equals("P1"))
                                            emulatedCodeMemory.set(pointer+1,"10010000");
                                        else if(splittedLine[2].toUpperCase().equals("P2"))
                                            emulatedCodeMemory.set(pointer+1,"10100000");
                                        else if(splittedLine[2].toUpperCase().equals("P3"))
                                            emulatedCodeMemory.set(pointer+1,"10110000");
                                        else
                                            throw new CompilingException("Nierozpoznany Port: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                        pointer+=2;
                                    } else {
                                        throw new CompilingException("Nierozpoznana wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                    }
                                }
                                else
                                    throw new CompilingException("Nierozpoznana etykieta: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                            }
                        }
                        else if(splittedLine[0].toUpperCase().equals("ADDC")) {
                            if(splittedLine.length!=3)
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            if(splittedLine[1].toUpperCase().equals("A")) {

                                if(splittedLine[1].toUpperCase().equals("A")) {
                                    if(splittedLine[2].charAt(0)=='#') {
                                        emulatedCodeMemory.set(pointer,"00110100");
                                        try {
                                            emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                        }
                                        catch (NumberFormatException e) {
                                            throw new CompilingException("Nierozpoznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                        }
                                        pointer+=2;
                                    }
                                    else if(splittedLine[2].toUpperCase().charAt(0)=='R') {
                                        if(splittedLine[2].toUpperCase().equals("R0"))
                                            emulatedCodeMemory.set(pointer,"00111000");
                                        else if(splittedLine[2].toUpperCase().equals("R1"))
                                            emulatedCodeMemory.set(pointer,"00111001");
                                        else if(splittedLine[2].toUpperCase().equals("R2"))
                                            emulatedCodeMemory.set(pointer,"00111010");
                                        else if(splittedLine[2].toUpperCase().equals("R3"))
                                            emulatedCodeMemory.set(pointer,"00111011");
                                        else if(splittedLine[2].toUpperCase().equals("R4"))
                                            emulatedCodeMemory.set(pointer,"00111100");
                                        else if(splittedLine[2].toUpperCase().equals("R5"))
                                            emulatedCodeMemory.set(pointer,"00111101");
                                        else if(splittedLine[2].toUpperCase().equals("R6"))
                                            emulatedCodeMemory.set(pointer,"00111110");
                                        else if(splittedLine[2].toUpperCase().equals("R7"))
                                            emulatedCodeMemory.set(pointer,"00111111");
                                        else
                                            throw new CompilingException("Nierozpoznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");
                                        pointer+=1;
                                    }
                                    else if(splittedLine[2].toUpperCase().charAt(0)=='P') {
                                        emulatedCodeMemory.set(pointer,"00110101");
                                        if(splittedLine[2].toUpperCase().equals("P0"))
                                            emulatedCodeMemory.set(pointer+1,"10000000");
                                        else if(splittedLine[2].toUpperCase().equals("P1"))
                                            emulatedCodeMemory.set(pointer+1,"10010000");
                                        else if(splittedLine[2].toUpperCase().equals("P2"))
                                            emulatedCodeMemory.set(pointer+1,"10100000");
                                        else if(splittedLine[2].toUpperCase().equals("P3"))
                                            emulatedCodeMemory.set(pointer+1,"10110000");
                                        else
                                            throw new CompilingException("Nierozpoznany Port: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");
                                        pointer+=2;
                                    }
                                }
                            }
                            else
                                throw new CompilingException("Nierozpoznana etykieta: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                        }
                        else if(splittedLine[0].toUpperCase().equals("SUBB")) {

                            if(splittedLine.length!=3)
                                throw new CompilingException("Niepoprawna ilość argumentów, linia: '" + backupLinii+"'");


                            if(splittedLine[1].toUpperCase().equals("A")) {
                                if(splittedLine[2].charAt(0)=='#') {
                                    emulatedCodeMemory.set(pointer,"10010100");
                                    try {
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                    } catch (NumberFormatException e) {
                                        throw new CompilingException("Nierozpoznana Liczba: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");

                                    }
                                    pointer+=2;
                                }
                                else if(splittedLine[2].toUpperCase().charAt(0)=='R') {
                                    if(splittedLine[2].toUpperCase().equals("R0"))
                                        emulatedCodeMemory.set(pointer,"10011000");
                                    else if(splittedLine[2].toUpperCase().equals("R1"))
                                        emulatedCodeMemory.set(pointer,"10011001");
                                    else if(splittedLine[2].toUpperCase().equals("R2"))
                                        emulatedCodeMemory.set(pointer,"10011010");
                                    else if(splittedLine[2].toUpperCase().equals("R3"))
                                        emulatedCodeMemory.set(pointer,"10011011");
                                    else if(splittedLine[2].toUpperCase().equals("R4"))
                                        emulatedCodeMemory.set(pointer,"10011100");
                                    else if(splittedLine[2].toUpperCase().equals("R5"))
                                        emulatedCodeMemory.set(pointer,"10011101");
                                    else if(splittedLine[2].toUpperCase().equals("R6"))
                                        emulatedCodeMemory.set(pointer,"10011110");
                                    else if(splittedLine[2].toUpperCase().equals("R7"))
                                        emulatedCodeMemory.set(pointer,"10011111");
                                    else {
                                        throw new CompilingException("Nierozpoznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");
                                    }
                                    pointer+=1;
                                }
                                else if(splittedLine[2].toUpperCase().charAt(0)=='P') {
                                    emulatedCodeMemory.set(pointer,"10010101");
                                    if(splittedLine[2].toUpperCase().equals("P0"))
                                        emulatedCodeMemory.set(pointer+1,"10000000");
                                    else if(splittedLine[2].toUpperCase().equals("P1"))
                                        emulatedCodeMemory.set(pointer+1,"10010000");
                                    else if(splittedLine[2].toUpperCase().equals("P2"))
                                        emulatedCodeMemory.set(pointer+1,"10100000");
                                    else if(splittedLine[2].toUpperCase().equals("P3"))
                                        emulatedCodeMemory.set(pointer+1,"10110000");
                                    else
                                        throw new CompilingException("Nierozpoznany Port: '" + splittedLine[2] + "', linia: '" + backupLinii+"'");
                                    pointer+=2;
                                }
                            }
                        }
                        else if(splittedLine[0].toUpperCase().equals("SETB")) {
                            if(splittedLine.length!=2)
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            if(splittedLine[1].toUpperCase().equals("C")) {
                                emulatedCodeMemory.set(pointer,"11010011");
                                pointer+=1;
                            } else {
                                throw new CompilingException("Nierozpoznany Bit: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                            }
                        }
                        else if(splittedLine[0].toUpperCase().equals("CLR")) {
                            if(splittedLine.length!=2)
                                throw new CompilingException("Niepoprawna ilość argumentow, linia: '" + backupLinii+"'");
                            if(splittedLine[1].toUpperCase().equals("C")) {
                                emulatedCodeMemory.set(pointer,"11000011");
                                pointer+=1;
                            }
                            else if(splittedLine[1].toUpperCase().equals("A")) {
                                emulatedCodeMemory.set(pointer,"11100100");
                            }
                            else {
                                throw new CompilingException("Nierozpoznany Bit: '" + splittedLine[1] + "', linia: '" + backupLinii+"'");
                            }
                        }
                    }
                }
        }
        for(int i = 0; i < 8192;i++) {
            String s = emulatedCodeMemory.get(i);
            if(s.charAt(s.length()-1)!='0' && s.charAt(s.length()-1)!='1') {
                int numer = getLineFromLabel(s);
                if(numer != -1) {
                    if (emulatedCodeMemory.get(i - 1).equals("00000010")) {
                        emulatedCodeMemory.set(i, make16DigitsStringFromNumber(Integer.toString(numer) + "d").substring(0, 8));
                        emulatedCodeMemory.set(i + 1, make16DigitsStringFromNumber(Integer.toString(numer) + "d").substring(8, 16));
                    } else {
                        //offset
                        int wynik = numer - i - 1;
                        if(wynik<0)
                            wynik+=256;
                        if(wynik<0 || wynik>255)
                            throw new CompilingException("Przekroczono zakres skoku djnz: '" + s + "'");

                        if(numer>i && wynik>128)
                            throw new CompilingException("Przekroczono zakres skoku djnz: '" + s + "'");
                        if(numer<i && wynik<128)
                            throw new CompilingException("Przekroczono zakres skoku djnz: '" + s + "'");
                        emulatedCodeMemory.set(i, make8DigitsStringFromNumber(Integer.toString(wynik) + "d"));
                    }
                }
                else {
                    throw new CompilingException("Nieznana Etykieta: '" + s + "'");
                }
            }
        }

        Main.stage.compilationErrorsLabel.setText("Kompilacja przebiegła pomyślnie");
        Main.stage.compilationErrorsLabel.setStyle("-fx-background-color: lightgreen; -fx-background-radius: 10; -fx-background-insets: 0 20 0 20");
        //show();
    }

    private ArrayList<String> emulatedCodeMemory;
    private ArrayList<Pair<String,Integer>> labels = new ArrayList<>();



    public String make8DigitsStringFromNumber(String number) throws NumberFormatException {
        String result = "";
        char lastSymbol = number.charAt(number.length()-1);
        if(lastSymbol =='b' || lastSymbol == 'd' || lastSymbol == 'h') {
            number = number.substring(0,number.length()-1);
            int wartosc = -1;
            if (lastSymbol == 'b') {
                wartosc = Integer.parseInt(number,2);
            }
            if (lastSymbol == 'd') {
                wartosc = Integer.parseInt(number);
            }
            if(lastSymbol == 'h') {
                wartosc = Integer.parseInt(number,16);
            }
            result = Integer.toBinaryString(wartosc);
            int length = result.length();
            for(int i = length;i<8;i++) {
                result = "0" + result;
            }
            return result;
        }
        else {
           throw new NumberFormatException();
        }
    }

    public String make16DigitsStringFromNumber(String number) throws CompilingException {
        String result = "";
        char lastSymbol = number.charAt(number.length()-1);
        if(lastSymbol =='b' || lastSymbol == 'd' || lastSymbol == 'h') {
            number = number.substring(0,number.length()-1);
            int wartosc = -1;
            if (lastSymbol == 'b') {
                wartosc = Integer.parseInt(number,2);
            }
            if (lastSymbol == 'd') {
                wartosc = Integer.parseInt(number);
            }
            if(lastSymbol == 'h') {
                wartosc = Integer.parseInt(number,16);
            }
            result = Integer.toBinaryString(wartosc);
            int length = result.length();
            for(int i = length;i<16;i++) {
                result = "0" + result;
            }
            return result;
        }
        else {
            throw new CompilingException();
        }
    }

}
