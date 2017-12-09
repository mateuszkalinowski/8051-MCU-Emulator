package components;

import core.Main;
import exceptions.CompilingException;
import javafx.util.Pair;
import microcontroller.Cpu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Created by Mateusz on 18.04.2017.
 * Project MkSim51
 */
public class CodeMemory {
    public CodeMemory() {
        emulatedCodeMemory = new ArrayList<>();
        for (int i = 0; i < Cpu.programMemory; i++) {
            emulatedCodeMemory.add("00000000");
        }

        bitAddresses.put("P0.0", "10000000");
        bitAddresses.put("P0.1", "10000001");
        bitAddresses.put("P0.2", "10000010");
        bitAddresses.put("P0.3", "10000011");
        bitAddresses.put("P0.4", "10000100");
        bitAddresses.put("P0.5", "10000101");
        bitAddresses.put("P0.6", "10000110");
        bitAddresses.put("P0.7", "10000111");

        bitAddresses.put("TCON.0", "10001000");
        bitAddresses.put("TCON.1", "10001001");
        bitAddresses.put("TCON.2", "10001010");
        bitAddresses.put("TCON.3", "10001011");
        bitAddresses.put("TCON.4", "10001100");
        bitAddresses.put("TCON.5", "10001101");
        bitAddresses.put("TCON.6", "10001110");
        bitAddresses.put("TCON.7", "10001111");

        bitAddresses.put("IT0", "10001000");
        bitAddresses.put("IE0", "10001001");
        bitAddresses.put("IT1", "10001010");
        bitAddresses.put("IE1", "10001011");
        bitAddresses.put("TR0", "10001100");
        bitAddresses.put("TF0", "10001101");
        bitAddresses.put("TR1", "10001110");
        bitAddresses.put("TF1", "10001111");

        bitAddresses.put("P1.0", "10010000");
        bitAddresses.put("P1.1", "10010001");
        bitAddresses.put("P1.2", "10010010");
        bitAddresses.put("P1.3", "10010011");
        bitAddresses.put("P1.4", "10010100");
        bitAddresses.put("P1.5", "10010101");
        bitAddresses.put("P1.6", "10010110");
        bitAddresses.put("P1.7", "10010111");

        bitAddresses.put("SCON.0", "10011000");
        bitAddresses.put("SCON.1", "10011001");
        bitAddresses.put("SCON.2", "10011010");
        bitAddresses.put("SCON.3", "10011011");
        bitAddresses.put("SCON.4", "10011100");
        bitAddresses.put("SCON.5", "10011101");
        bitAddresses.put("SCON.6", "10011110");
        bitAddresses.put("SCON.7", "10011111");

        bitAddresses.put("P2.0", "10100000");
        bitAddresses.put("P2.1", "10100001");
        bitAddresses.put("P2.2", "10100010");
        bitAddresses.put("P2.3", "10100011");
        bitAddresses.put("P2.4", "10100100");
        bitAddresses.put("P2.5", "10100101");
        bitAddresses.put("P2.6", "10100110");
        bitAddresses.put("P2.7", "10100111");

        bitAddresses.put("P3.0", "10110000");
        bitAddresses.put("P3.1", "10110001");
        bitAddresses.put("P3.2", "10110010");
        bitAddresses.put("P3.3", "10110011");
        bitAddresses.put("P3.4", "10110100");
        bitAddresses.put("P3.5", "10110101");
        bitAddresses.put("P3.6", "10110110");
        bitAddresses.put("P3.7", "10110111");

        bitAddresses.put("P4.0","11101000");
        bitAddresses.put("P4.1","11101001");
        bitAddresses.put("P4.2","11101010");
        bitAddresses.put("P4.3","11101011");
        bitAddresses.put("P4.4","11101100");
        bitAddresses.put("P4.5","11101101");
        bitAddresses.put("P4.6","11101110");
        bitAddresses.put("P4.7","11101111");

        bitAddresses.put("P5.0","11111000");
        bitAddresses.put("P5.1","11111001");
        bitAddresses.put("P5.2","11111010");
        bitAddresses.put("P5.3","11111011");
        bitAddresses.put("P5.4","11111100");
        bitAddresses.put("P5.5","11111101");
        bitAddresses.put("P5.6","11111110");
        bitAddresses.put("P5.7","11111111");

        bitAddresses.put("PSW.0", "11010000");
        bitAddresses.put("PSW.1", "11010001");
        bitAddresses.put("PSW.2", "11010010");
        bitAddresses.put("PSW.3", "11010011");
        bitAddresses.put("PSW.4", "11010100");
        bitAddresses.put("PSW.5", "11010101");
        bitAddresses.put("PSW.6", "11010110");
        bitAddresses.put("PSW.7", "11010111");

        bitAddresses.put("P", "11010000");
        bitAddresses.put("F1", "11010001");
        bitAddresses.put("OV", "11010010");
        bitAddresses.put("RS0", "11010011");
        bitAddresses.put("RS1", "11010100");
        bitAddresses.put("FO", "11010101");
        bitAddresses.put("AC", "11010110");
        bitAddresses.put("CY", "11010111");

        bitAddresses.put("ACC.0", "11100000");
        bitAddresses.put("ACC.1", "11100001");
        bitAddresses.put("ACC.2", "11100010");
        bitAddresses.put("ACC.3", "11100011");
        bitAddresses.put("ACC.4", "11100100");
        bitAddresses.put("ACC.5", "11100101");
        bitAddresses.put("ACC.6", "11100110");
        bitAddresses.put("ACC.7", "11100111");

        bitAddresses.put("B.0", "11110000");
        bitAddresses.put("B.1", "11100001");
        bitAddresses.put("B.2", "11110010");
        bitAddresses.put("B.3", "11110011");
        bitAddresses.put("B.4", "11110100");
        bitAddresses.put("B.5", "11110101");
        bitAddresses.put("B.6", "11110110");
        bitAddresses.put("B.7", "11110111");

        bitAddresses.put("IE.0", "10101000");
        bitAddresses.put("IE.1", "10101001");
        bitAddresses.put("IE.2", "10101010");
        bitAddresses.put("IE.3", "10101011");
        bitAddresses.put("IE.4", "10101100");
        bitAddresses.put("IE.7", "10101111");

        bitAddresses.put("EX0", "10101000");
        bitAddresses.put("ET0", "10101001");
        bitAddresses.put("EX1", "10101010");
        bitAddresses.put("ET1", "10101011");
        bitAddresses.put("ES", "10101100");
        bitAddresses.put("EA", "10101111");

    }

    public String getFromAddress(int line) {
        return emulatedCodeMemory.get(line);
    }

    private int getLineFromLabel(String label) {
        int line = -1;
        for (Pair pair : labels) {
            if (pair.getKey().equals(label)) {
                line = (int) pair.getValue();
                break;
            }
        }
        return line;
    }

    public ArrayList<String> setMemory(String[] lines, boolean isMain) throws CompilingException {
        int pointer = 0;
        int rozmiarTabeliAdresow = 0;
        int rozmiarPoczatkowy = addresses.size();
        if (isMain) {
            emulatedCodeMemory = new ArrayList<>();
            for (int i = 0; i < Cpu.programMemory; i++) {
                emulatedCodeMemory.add("00000000");
                addresses.clear();
                rozmiarPoczatkowy = 0;
            }
            labels.clear();
        }
        ArrayList<String> linieZNumerami = new ArrayList<>();
        int numeracjaLinii = -1;
        for (String line : lines) {
            numeracjaLinii++;
            String backupLinii = line;
            line = line.trim();
            int komentarz = -1;
            for (int i = 0; i < line.length(); i++) {
                if (line.charAt(i) == ';') {
                    komentarz = i;
                    break;
                }
            }
            if (komentarz > 0) {
                line = line.substring(0, komentarz);
            }
            if (komentarz == 0) {
                line = "";
                linieZNumerami.add(backupLinii.trim());
            }
            if (line.length() > 0) {
                line = line.replace(',', ' ');
                line = line.trim();
                String[] splittedLine = line.split("\\s+");
                if (splittedLine[0].charAt(splittedLine[0].length() - 1) == ':') {
                    if (getLineFromLabel(splittedLine[0].toUpperCase().substring(0, splittedLine[0].length() - 1)) == -1) {

                        boolean goodLabel;
                        goodLabel = possibleLabel(splittedLine[0].toUpperCase().substring(0, splittedLine[0].length() - 1));
                        if(goodLabel) {
                            labels.add(new Pair<>(splittedLine[0].toUpperCase().substring(0, splittedLine[0].length() - 1), pointer));
                            linieZNumerami.add(backupLinii);
                            String[] splittedLine2 = new String[splittedLine.length - 1];
                            System.arraycopy(splittedLine, 1, splittedLine2, 0, splittedLine.length - 1);
                            splittedLine = splittedLine2;
                        }
                        else {
                            throw new CompilingException(numeracjaLinii,"Etykieta nie jest poprawna");
                        }
                    } else
                        throw new CompilingException(numeracjaLinii,"Etykieta: '" + splittedLine[0].substring(0, splittedLine[0].length() - 1) + "' już istnieje");
                }
                if (splittedLine.length == 0)
                    continue;
                if (splittedLine[0].toUpperCase().equals("ORG") && splittedLine.length == 2) {
                    try {
                        if (rozmiarTabeliAdresow != 0) {
                            addresses.add(pointer);
                            rozmiarTabeliAdresow++;
                        }
                        if (splittedLine[1].toUpperCase().charAt(splittedLine[1].length() - 1) == 'H') {
                            pointer = Integer.parseInt(splittedLine[1].substring(0, splittedLine[1].length() - 1), 16);
                            linieZNumerami.add(backupLinii);
                        } else if (splittedLine[1].toUpperCase().charAt(splittedLine[1].length() - 1) == 'B') {
                            pointer = Integer.parseInt(splittedLine[1].substring(0, splittedLine[1].length() - 1), 2);
                            linieZNumerami.add(backupLinii);
                        } else if (splittedLine[1].toUpperCase().charAt(splittedLine[1].length() - 1) == 'D') {
                            pointer = Integer.parseInt(splittedLine[1].substring(0, splittedLine[1].length() - 1));
                            linieZNumerami.add(backupLinii);
                        } else {
                            pointer = Integer.parseInt(splittedLine[1]);
                            linieZNumerami.add(backupLinii);
                        }
                        addresses.add(pointer);
                        rozmiarTabeliAdresow++;
                    } catch (NumberFormatException e) {
                        throw new CompilingException(numeracjaLinii, "Błędny adres: '" + splittedLine[1] + ", linia: " + backupLinii + "'");
                    }
                } else if (splittedLine[0].toUpperCase().equals("CODE") && splittedLine.length == 3) {
                    if (splittedLine[1].toUpperCase().equals("AT")) {
                        try {
                            if (rozmiarTabeliAdresow != 0) {
                                addresses.add(pointer);
                                rozmiarTabeliAdresow++;
                            }
                            if (splittedLine[2].toUpperCase().charAt(splittedLine[2].length() - 1) == 'H') {
                                pointer = Integer.parseInt(splittedLine[2].substring(0, splittedLine[2].length() - 1), 16);
                                linieZNumerami.add(backupLinii);
                            } else if (splittedLine[2].toUpperCase().charAt(splittedLine[2].length() - 1) == 'B') {
                                pointer = Integer.parseInt(splittedLine[2].substring(0, splittedLine[2].length() - 1), 2);
                                linieZNumerami.add(backupLinii);
                            } else if (splittedLine[2].toUpperCase().charAt(splittedLine[2].length() - 1) == 'D') {
                                pointer = Integer.parseInt(splittedLine[2].substring(0, splittedLine[2].length() - 1));
                                linieZNumerami.add(backupLinii);
                            } else {
                                pointer = Integer.parseInt(splittedLine[2]);
                                linieZNumerami.add(backupLinii);
                            }

                            addresses.add(pointer);
                            rozmiarTabeliAdresow++;
                        } catch (NumberFormatException e) {
                            throw new CompilingException(numeracjaLinii, "Błędny adres: '" + splittedLine[2] + ", linia: " + backupLinii + "'");
                        }
                    } else {
                        throw new CompilingException(numeracjaLinii, "Błędna instukcja: '" + backupLinii + "'");

                    }
                } else if (splittedLine[0].toUpperCase().equals("DB")) {
                    String address;
                    try {
                        for (int i = 1; i < splittedLine.length; i++) {
                            address = make8DigitsStringFromNumber(splittedLine[i]);
                            emulatedCodeMemory.set(pointer, address);
                            pointer += 1;
                        }
                        linieZNumerami.add(backupLinii);
                    } catch (Exception e) {
                        throw new CompilingException(numeracjaLinii, "Błędny bit: '" + backupLinii + "'");
                    }
                } else if (splittedLine[0].toUpperCase().equals("INCLUDE")) {
                    String fileName = line.substring(7).trim();
                    try {
                        String[] linesInside = Main.stage.getLinesFromTabByName(fileName);
                        ArrayList<String> lineWewnetrzne = Main.cpu.codeMemory.setMemory(linesInside, false);
                        int size = linieZNumerami.size();
                        for (int i = 0; i < lineWewnetrzne.size(); i++) {
                            linieZNumerami.add(size + i, lineWewnetrzne.get(i));
                        }
                    } catch (NoSuchElementException e) {
                        throw new CompilingException(numeracjaLinii, "Nie odnaleziono pliku: '" + fileName + "'");
                    }

                } else {
                    int backupPointer = pointer;
                        for(int i = 0; i < splittedLine.length;i++) {

                            splittedLine[i] = splittedLine[i].toUpperCase();

                           boolean isBitAddress = false;

                           if(splittedLine.length>=2) {
                               if(splittedLine[0].equals("SETB") || splittedLine[0].equals("CLR") || splittedLine[0].equals("CPL") || splittedLine[0].equals("JB") || splittedLine[0].equals("JNB"))
                                   isBitAddress=true;
                           }
                           if(splittedLine.length>=3) {
                               if(splittedLine[0].equals("ANL") || splittedLine[0].equals("ORL")) {
                                   if(splittedLine[1].equals("C") && i==2)
                                       isBitAddress=true;
                               }
                           }
                           if(splittedLine.length>=3) {
                               if(splittedLine[0].equals("MOV")) {
                                   if(i==1 && splittedLine[2].equals("C"))
                                       isBitAddress=true;
                                   if(i==2 && splittedLine[1].equals("C"))
                                       isBitAddress=true;
                               }
                           }


                            if (!isBitAddress) {

                               if (splittedLine[i].equals("B"))
                                   splittedLine[i] = "F0h";
                               else if (splittedLine[i].equals("SP"))
                                   splittedLine[i] = "81h";
                               else if (splittedLine[i].equals("TCON"))
                                   splittedLine[i] = "88h";
                               else if (splittedLine[i].equals("TMOD"))
                                   splittedLine[i] = "89h";

                               else if (splittedLine[i].equals("TL0"))
                                   splittedLine[i] = "8Ah";
                               else if (splittedLine[i].equals("TH0"))
                                   splittedLine[i] = "8Ch";
                               else if (splittedLine[i].equals("TL1"))
                                   splittedLine[i] = "8Bh";
                               else if (splittedLine[i].equals("TH1"))
                                   splittedLine[i] = "8Dh";

                               else if (splittedLine[i].equals("IE"))
                                   splittedLine[i] = "A8h";

                               else if (splittedLine[i].equals("DPL"))
                                   splittedLine[i] = "82h";
                               else if (splittedLine[i].equals("DPH"))
                                   splittedLine[i] = "83h";

                               else if (splittedLine[i].equals("P0"))
                                   splittedLine[i] = "80h";
                               else if (splittedLine[i].equals("P1"))
                                   splittedLine[i] = "90h";
                               else if (splittedLine[i].equals("P2"))
                                   splittedLine[i] = "A0h";
                               else if (splittedLine[i].equals("P3"))
                                   splittedLine[i] = "B0h";
                               else if (splittedLine[i].equals("P4"))
                                   splittedLine[i] = "E8h";
                               else if (splittedLine[i].equals("P5"))
                                   splittedLine[i] = "F8h";

                            }
                            if(isBitAddress) {
                               if (splittedLine[i].equals("P0.0"))
                                   splittedLine[i] = "80h";
                               else if (splittedLine[i].equals("P0.1"))
                                   splittedLine[i] = "81h";
                               else if (splittedLine[i].equals("P0.2"))
                                   splittedLine[i] = "82h";
                               else if (splittedLine[i].equals("P0.3"))
                                   splittedLine[i] = "83h";
                               else if (splittedLine[i].equals("P0.4"))
                                   splittedLine[i] = "84h";
                               else if (splittedLine[i].equals("P0.5"))
                                   splittedLine[i] = "85h";
                               else if (splittedLine[i].equals("P0.6"))
                                   splittedLine[i] = "86h";
                               else if (splittedLine[i].equals("P0.7"))
                                   splittedLine[i] = "87h";


                               else if (splittedLine[i].equals("P1.0"))
                                   splittedLine[i] = "90h";
                               else if (splittedLine[i].equals("P1.1"))
                                   splittedLine[i] = "91h";
                               else if (splittedLine[i].equals("P1.2"))
                                   splittedLine[i] = "92h";
                               else if (splittedLine[i].equals("P1.3"))
                                   splittedLine[i] = "93h";
                               else if (splittedLine[i].equals("P1.4"))
                                   splittedLine[i] = "94h";
                               else if (splittedLine[i].equals("P1.5"))
                                   splittedLine[i] = "95h";
                               else if (splittedLine[i].equals("P1.6"))
                                   splittedLine[i] = "96h";
                               else if (splittedLine[i].equals("P1.7"))
                                   splittedLine[i] = "97h";


                               else if (splittedLine[i].equals("P2.0"))
                                   splittedLine[i] = "A0h";
                               else if (splittedLine[i].equals("P2.1"))
                                   splittedLine[i] = "A1h";
                               else if (splittedLine[i].equals("P2.2"))
                                   splittedLine[i] = "A2h";
                               else if (splittedLine[i].equals("P2.3"))
                                   splittedLine[i] = "A3h";
                               else if (splittedLine[i].equals("P2.4"))
                                   splittedLine[i] = "A4h";
                               else if (splittedLine[i].equals("P2.5"))
                                   splittedLine[i] = "A5h";
                               else if (splittedLine[i].equals("P2.6"))
                                   splittedLine[i] = "A6h";
                               else if (splittedLine[i].equals("P2.7"))
                                   splittedLine[i] = "A7h";


                               else if (splittedLine[i].equals("P3.0"))
                                   splittedLine[i] = "B0h";
                               else if (splittedLine[i].equals("P3.1"))
                                   splittedLine[i] = "B1h";
                               else if (splittedLine[i].equals("P3.2"))
                                   splittedLine[i] = "B2h";
                               else if (splittedLine[i].equals("P3.3"))
                                   splittedLine[i] = "B3h";
                               else if (splittedLine[i].equals("P3.4"))
                                   splittedLine[i] = "B4h";
                               else if (splittedLine[i].equals("P3.5"))
                                   splittedLine[i] = "B5h";
                               else if (splittedLine[i].equals("P3.6"))
                                   splittedLine[i] = "B6h";
                               else if (splittedLine[i].equals("P3.7"))
                                   splittedLine[i] = "B7h";


                               else if (splittedLine[i].equals("P4.0"))
                                   splittedLine[i] = "E8h";
                               else if (splittedLine[i].equals("P2.1"))
                                   splittedLine[i] = "E9h";
                               else if (splittedLine[i].equals("P2.2"))
                                   splittedLine[i] = "EAh";
                               else if (splittedLine[i].equals("P2.3"))
                                   splittedLine[i] = "EBh";
                               else if (splittedLine[i].equals("P2.4"))
                                   splittedLine[i] = "ECh";
                               else if (splittedLine[i].equals("P2.5"))
                                   splittedLine[i] = "EDh";
                               else if (splittedLine[i].equals("P2.6"))
                                   splittedLine[i] = "EEh";
                               else if (splittedLine[i].equals("P2.7"))
                                   splittedLine[i] = "EFh";

                               else if (splittedLine[i].equals("P5.0"))
                                   splittedLine[i] = "F8h";
                               else if (splittedLine[i].equals("P5.1"))
                                   splittedLine[i] = "F9h";
                               else if (splittedLine[i].equals("P5.2"))
                                   splittedLine[i] = "FAh";
                               else if (splittedLine[i].equals("P5.3"))
                                   splittedLine[i] = "FBh";
                               else if (splittedLine[i].equals("P5.4"))
                                   splittedLine[i] = "FCh";
                               else if (splittedLine[i].equals("P5.5"))
                                   splittedLine[i] = "Fh";
                               else if (splittedLine[i].equals("P5.6"))
                                   splittedLine[i] = "FEh";
                               else if (splittedLine[i].equals("P5.7"))
                                   splittedLine[i] = "FFh";

                               else if (splittedLine[i].equals("PSW.0") || splittedLine[i].equals("P"))
                                   splittedLine[i] = "D0h";
                               else if (splittedLine[i].equals("PSW.1") || splittedLine[i].equals("F1"))
                                   splittedLine[i] = "D1h";
                               else if (splittedLine[i].equals("PSW.2") || splittedLine[i].equals("OV"))
                                   splittedLine[i] = "D2h";
                               else if (splittedLine[i].equals("PSW.3") || splittedLine[i].equals("RS0"))
                                   splittedLine[i] = "D3h";
                               else if (splittedLine[i].equals("PSW.4") || splittedLine[i].equals("RS1"))
                                   splittedLine[i] = "D4h";
                               else if (splittedLine[i].equals("PSW.5") || splittedLine[i].equals("F0"))
                                   splittedLine[i] = "D5h";
                               else if (splittedLine[i].equals("PSW.6") || splittedLine[i].equals("AC"))
                                   splittedLine[i] = "D6h";
                               else if (splittedLine[i].equals("PSW.7") || splittedLine[i].equals("CY"))
                                   splittedLine[i] = "D7h";


                               else if (splittedLine[i].equals("TCON.0") || splittedLine[i].equals("IT0"))
                                   splittedLine[i] = "88h";
                               else if (splittedLine[i].equals("TCON.1") || splittedLine[i].equals("IE0"))
                                   splittedLine[i] = "89h";
                               else if (splittedLine[i].equals("TCON.2") || splittedLine[i].equals("IT1"))
                                   splittedLine[i] = "8Ah";
                               else if (splittedLine[i].equals("TCON.3") || splittedLine[i].equals("IE1"))
                                   splittedLine[i] = "8Bh";
                               else if (splittedLine[i].equals("TCON.4") || splittedLine[i].equals("TR0"))
                                   splittedLine[i] = "8Ch";
                               else if (splittedLine[i].equals("TCON.5") || splittedLine[i].equals("TF0"))
                                   splittedLine[i] = "8Dh";
                               else if (splittedLine[i].equals("TCON.6") || splittedLine[i].equals("TR1"))
                                   splittedLine[i] = "8Eh";
                               else if (splittedLine[i].equals("TCON.7") || splittedLine[i].equals("TF1"))
                                   splittedLine[i] = "8Fh";

                               else if (splittedLine[i].equals("SCON.0"))
                                   splittedLine[i] = "98h";
                               else if (splittedLine[i].equals("SCON.1"))
                                   splittedLine[i] = "99h";
                               else if (splittedLine[i].equals("SCON.2"))
                                   splittedLine[i] = "9Ah";
                               else if (splittedLine[i].equals("SCON.3"))
                                   splittedLine[i] = "9Bh";
                               else if (splittedLine[i].equals("SCON.4"))
                                   splittedLine[i] = "9Ch";
                               else if (splittedLine[i].equals("SCON.5"))
                                   splittedLine[i] = "9Dh";
                               else if (splittedLine[i].equals("SCON.6"))
                                   splittedLine[i] = "9Eh";
                               else if (splittedLine[i].equals("SCON.7"))
                                   splittedLine[i] = "9Fh";

                               else if (splittedLine[i].equals("ACC.0"))
                                   splittedLine[i] = "E0h";
                               else if (splittedLine[i].equals("ACC.1"))
                                   splittedLine[i] = "E1h";
                               else if (splittedLine[i].equals("ACC.2"))
                                   splittedLine[i] = "E2h";
                               else if (splittedLine[i].equals("ACC.3"))
                                   splittedLine[i] = "E3h";
                               else if (splittedLine[i].equals("ACC.4"))
                                   splittedLine[i] = "E4h";
                               else if (splittedLine[i].equals("ACC.5"))
                                   splittedLine[i] = "E5h";
                               else if (splittedLine[i].equals("ACC.6"))
                                   splittedLine[i] = "E6h";
                               else if (splittedLine[i].equals("ACC.7"))
                                   splittedLine[i] = "E7h";

                               else if (splittedLine[i].equals("B.0"))
                                   splittedLine[i] = "F0h";
                               else if (splittedLine[i].equals("B.1"))
                                   splittedLine[i] = "F1h";
                               else if (splittedLine[i].equals("B.2"))
                                   splittedLine[i] = "F2h";
                               else if (splittedLine[i].equals("B.3"))
                                   splittedLine[i] = "F3h";
                               else if (splittedLine[i].equals("B.4"))
                                   splittedLine[i] = "F4h";
                               else if (splittedLine[i].equals("B.5"))
                                   splittedLine[i] = "F5h";
                               else if (splittedLine[i].equals("B.6"))
                                   splittedLine[i] = "F6h";
                               else if (splittedLine[i].equals("B.7"))
                                   splittedLine[i] = "F7h";


                               else if (splittedLine[i].equals("IE.0") || splittedLine[i].equals("EX0"))
                                   splittedLine[i] = "A8h";
                               else if (splittedLine[i].equals("IE.1") || splittedLine[i].equals("ET0"))
                                   splittedLine[i] = "A9h";
                               else if (splittedLine[i].equals("IE.2") || splittedLine[i].equals("EX1"))
                                   splittedLine[i] = "AAh";
                               else if (splittedLine[i].equals("IE.3") || splittedLine[i].equals("ET1"))
                                   splittedLine[i] = "ABh";
                               else if (splittedLine[i].equals("IE.4") || splittedLine[i].equals("ES"))
                                   splittedLine[i] = "ACh";
                               else if (splittedLine[i].equals("IE.7") || splittedLine[i].equals("EA"))
                                   splittedLine[i] = "AFh";
                           }

                        }

                        //TODO
                    if (splittedLine[0].toUpperCase().equals("AJMP")) {
                        if(splittedLine.length != 2) {
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilosc argumentow instrukcji AJMP: " + backupLinii + "'");
                        }

                        try {
                            String destination = make16DigitsStringFromNumber(splittedLine[1]);
                            int destinationInt = Integer.parseInt(destination,2);
                            if(pointer/2048 == destinationInt/2048) {
                                destination = destination.substring(5);
                                emulatedCodeMemory.set(pointer,destination.substring(0,3) + "00001");
                                emulatedCodeMemory.set(pointer+1,destination.substring(3,11));
                            }
                            else
                                throw new CompilingException(numeracjaLinii, "Przekroczono zakres skoku AJMP");
                        }
                       catch (NumberFormatException e){
                            emulatedCodeMemory.set(pointer,"00000001");
                            emulatedCodeMemory.set(pointer+1,splittedLine[1].toUpperCase());
                        }

                        pointer = pointer+2;
                    }
                    else if (splittedLine[0].toUpperCase().equals("LCALL")) {

                        if (splittedLine.length != 2) {
                            throw new CompilingException(numeracjaLinii, "Niepoprawne uzycie LCALL: " + backupLinii + "'");
                        }
                        emulatedCodeMemory.set(pointer, "00010010");

                        try {
                            String destination = make16DigitsStringFromNumber(splittedLine[1]);
                            emulatedCodeMemory.set(pointer + 1, destination.substring(0, 8));
                            emulatedCodeMemory.set(pointer + 2, destination.substring(8, 16));
                        } catch (Exception e) {
                            emulatedCodeMemory.set(pointer + 1, splittedLine[1].toUpperCase());
                        }
                        pointer += 3;
                    } else if (splittedLine[0].toUpperCase().equals("MOVC")) {
                        if (splittedLine.length != 3) {
                            throw new CompilingException(numeracjaLinii, "Niepoprawne uzycie MOVC: " + backupLinii + "'");
                        }
                        if (splittedLine[1].toUpperCase().equals("A") && splittedLine[2].toUpperCase().equals("@A+DPTR")) {
                            emulatedCodeMemory.set(pointer, "10010011");
                            pointer += 1;
                        } else if (splittedLine[1].toUpperCase().equals("A") && splittedLine[2].toUpperCase().equals("@A+PC")) {
                            emulatedCodeMemory.set(pointer, "10000011");
                            pointer += 1;
                        } else {
                            throw new CompilingException(numeracjaLinii, "Niepoprawne użycie MOVC: '" + backupLinii + "'");
                        }
                    } else if (splittedLine[0].toUpperCase().equals("LJMP")) {
                        emulatedCodeMemory.set(pointer, "00000010");

                        if (splittedLine.length != 2) {
                            throw new CompilingException(numeracjaLinii, "Niepoprawne uzycie LJMP: " + backupLinii + "'");
                        }

                        try {
                            String destination = make16DigitsStringFromNumber(splittedLine[1]);
                            emulatedCodeMemory.set(pointer + 1, destination.substring(0, 8));
                            emulatedCodeMemory.set(pointer + 2, destination.substring(8, 16));
                        } catch (Exception e) {
                            emulatedCodeMemory.set(pointer + 1, splittedLine[1].toUpperCase());
                        }
                        pointer += 3;
                    } else if (splittedLine[0].toUpperCase().equals("DJNZ")) {
                        if (splittedLine.length != 3) {
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentów DJNZ: '" + backupLinii + "'");
                        }

                        String numer = getRAddress(splittedLine[1].toUpperCase());

                        if (numer.equals("")) {
                            try {
                                String wartosc = make8DigitsStringFromNumber(splittedLine[1]);
                                emulatedCodeMemory.set(pointer, "11010101");
                                emulatedCodeMemory.set(pointer + 1, wartosc);
                                emulatedCodeMemory.set(pointer + 2, splittedLine[2].toUpperCase());
                                pointer += 3;
                            } catch (Exception e) {
                                throw new CompilingException(numeracjaLinii, "Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                            }
                        } else {
                            emulatedCodeMemory.set(pointer, "11011" + numer);
                            emulatedCodeMemory.set(pointer + 1, splittedLine[2].toUpperCase());
                            pointer += 2;
                        }
                    } else if (splittedLine[0].toUpperCase().equals("CJNE")) {
                        if (splittedLine.length == 4) {
                            if (splittedLine[2].charAt(0) == '#') {
                                try {
                                    String number = make8DigitsStringFromNumber(splittedLine[2].substring(1));
                                    if (splittedLine[1].toUpperCase().equals("A")) {
                                        emulatedCodeMemory.set(pointer, "10110100");
                                        emulatedCodeMemory.set(pointer + 1, number);
                                        emulatedCodeMemory.set(pointer + 2, splittedLine[3].toUpperCase());
                                        pointer += 3;
                                    } else if (splittedLine[1].toUpperCase().equals("@R0") || splittedLine[1].toUpperCase().equals("@R1")) {
                                        emulatedCodeMemory.set(pointer, "1011011" + splittedLine[1].charAt(2));
                                        emulatedCodeMemory.set(pointer + 1, number);
                                        emulatedCodeMemory.set(pointer + 2, splittedLine[3].toUpperCase());
                                        pointer += 3;
                                    } else {
                                        String numer = getRAddress(splittedLine[1].toUpperCase());
                                        if (numer.equals("")) {
                                            throw new CompilingException(numeracjaLinii, "Błędnie użyta komenda CJNE: '" + backupLinii + "'");
                                        }
                                        emulatedCodeMemory.set(pointer, "10111" + numer);
                                        emulatedCodeMemory.set(pointer + 1, number);
                                        emulatedCodeMemory.set(pointer + 2, splittedLine[3].toUpperCase());
                                        pointer += 3;
                                    }
                                } catch (Exception e) {
                                    throw new CompilingException(numeracjaLinii, "Niepoprawna wartość liczbowa '" + splittedLine[2] + "', linia: '" + backupLinii + "");
                                }

                            } else if (splittedLine[1].equals("A")) {
                                String address;
                                    try {
                                        address = make8DigitsStringFromNumber(splittedLine[2]);
                                        emulatedCodeMemory.set(pointer, "10110101");
                                        emulatedCodeMemory.set(pointer + 1, address);
                                        emulatedCodeMemory.set(pointer + 2, splittedLine[3].toUpperCase());
                                        pointer += 3;
                                    } catch (Exception e1) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                    }
                            } else
                                throw new CompilingException(numeracjaLinii, "Błędnie użyta komenda CJNE: '" + backupLinii + "'");
                        } else
                            throw new CompilingException(numeracjaLinii, "Błędnie użyta komenda CJNE: '" + backupLinii + "'");
                    } else if (splittedLine[0].toUpperCase().equals("INC")) {
                        if (splittedLine.length != 2)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentów funkcji INC: '" + backupLinii + "'");
                        if (splittedLine[1].toUpperCase().equals("A")) {
                            emulatedCodeMemory.set(pointer, "00000100");
                            pointer += 1;
                        } else if (splittedLine[1].toUpperCase().equals("DPTR")) {
                            emulatedCodeMemory.set(pointer, "10100011");
                            pointer += 1;
                        } else if (splittedLine[1].toUpperCase().charAt(0) == 'R') {
                            String numer = getRAddress(splittedLine[1].toUpperCase());
                            if (numer.equals("")) {
                                throw new CompilingException(numeracjaLinii, "Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                            }
                            emulatedCodeMemory.set(pointer, "00001" + numer);
                            pointer += 1;
                        } else if (splittedLine[1].toUpperCase().equals("@R0") || splittedLine[1].toUpperCase().equals("@R1")) {
                            emulatedCodeMemory.set(pointer, "0000011" + splittedLine[1].charAt(2));
                            pointer += 1;
                        } else {
                                try {
                                    emulatedCodeMemory.set(pointer, "00000101");
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[1]));
                                    pointer += 2;
                                } catch (Exception e) {
                                    throw new CompilingException(numeracjaLinii, "Nierozpoznany Bajt: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                }
                        }
                    } else if (splittedLine[0].toUpperCase().equals("DEC")) {
                        if (splittedLine.length != 2)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentów funkcji DEC: '" + backupLinii + "'");
                        if (splittedLine[1].toUpperCase().equals("A")) {
                            emulatedCodeMemory.set(pointer, "00010100");
                            pointer += 1;
                        } else if (splittedLine[1].toUpperCase().charAt(0) == 'R') {
                            String numer = getRAddress(splittedLine[1].toUpperCase());
                            if (numer.equals("")) {
                                throw new CompilingException(numeracjaLinii, "Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                            }
                            emulatedCodeMemory.set(pointer, "00011" + numer);
                            pointer += 1;
                        } else if (splittedLine[1].toUpperCase().equals("@R0") || splittedLine[1].toUpperCase().equals("@R1")) {
                            emulatedCodeMemory.set(pointer, "0001011" + splittedLine[1].charAt(2));
                            pointer += 1;
                        } else {
                                try {
                                    emulatedCodeMemory.set(pointer, "00010101");
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[1]));
                                    pointer += 2;
                                } catch (Exception e) {
                                    throw new CompilingException(numeracjaLinii, "Nierozpoznany Bajt: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                }
                        }
                    } else if (splittedLine[0].toUpperCase().equals("ANL")) {
                        if (splittedLine.length != 3)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentów funkcji ANL: '" + backupLinii + "'");

                        if (splittedLine[1].toUpperCase().equals("A")) {
                            if (splittedLine[2].charAt(0) == '#') {
                                emulatedCodeMemory.set(pointer, "01010100");
                                try {
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                } catch (NumberFormatException e) {
                                    throw new CompilingException(numeracjaLinii, "Nieznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                }
                                pointer += 2;
                            } else if (splittedLine[2].toUpperCase().charAt(0) == 'R') {
                                String numer = getRAddress(splittedLine[2].toUpperCase());
                                if (numer.equals("")) {
                                    throw new CompilingException(numeracjaLinii, "Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                }
                                emulatedCodeMemory.set(pointer, "01011" + numer);
                                pointer += 1;
                            } else if (splittedLine[2].toUpperCase().equals("@R1") || splittedLine[2].toUpperCase().equals("@R0")) {
                                emulatedCodeMemory.set(pointer, "0101011" + splittedLine[2].charAt(2));
                                pointer += 1;
                            } else {
                                    try {
                                        emulatedCodeMemory.set(pointer, "01010101");
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2]));
                                        pointer += 2;
                                    } catch (Exception e) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany bajt: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                    }
                            }
                        } else if (splittedLine[1].toUpperCase().equals("C") || splittedLine[1].toUpperCase().equals("CY")) {
                            if (splittedLine[2].charAt(0) == '/') {
                                String value = splittedLine[2].substring(1, splittedLine[2].length()).toUpperCase();
                                    try {
                                        emulatedCodeMemory.set(pointer, "10110000");
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(value));
                                        pointer += 2;
                                    } catch (Exception e) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Bit: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                    }
                            } else {
                                String value = splittedLine[2].toUpperCase();
                                    try {
                                        int numer = Integer.parseInt(make8DigitsStringFromNumber(value), 2);
                                        emulatedCodeMemory.set(pointer, "10000010");
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(value));
                                        pointer += 2;
                                    } catch (Exception e) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Bit: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                    }
                            }
                        } else {
                            if (splittedLine[2].toUpperCase().equals("A")) {

                                    try {
                                        String liczba = make8DigitsStringFromNumber(splittedLine[1]);
                                        emulatedCodeMemory.set(pointer, "01010010");
                                        emulatedCodeMemory.set(pointer + 1, liczba);
                                        pointer += 2;
                                    } catch (Exception e) {
                                        throw new CompilingException(numeracjaLinii, "Błędny bajt: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                    }


                            } else {
                                if (splittedLine[2].charAt(0) == '#') {
                                    try {
                                            try {
                                                String liczba = make8DigitsStringFromNumber(splittedLine[1]);
                                                emulatedCodeMemory.set(pointer, "01010011");
                                                emulatedCodeMemory.set(pointer + 1, liczba);
                                                emulatedCodeMemory.set(pointer + 2, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                                pointer += 3;
                                            } catch (Exception e) {
                                                throw new CompilingException(numeracjaLinii, "Błędny bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                            }

                                    } catch (NumberFormatException e) {
                                        throw new CompilingException(numeracjaLinii, "Nieznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                    }
                                }
                            }
                        }
                    } else if (splittedLine[0].toUpperCase().equals("ORL")) {

                        if (splittedLine.length != 3)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentów funkcji ORL: '" + backupLinii + "'");


                        if (splittedLine[1].toUpperCase().equals("A")) {
                            if (splittedLine[2].charAt(0) == '#') {
                                emulatedCodeMemory.set(pointer, "01000100");
                                try {
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                } catch (NumberFormatException e) {
                                    throw new CompilingException(numeracjaLinii, "Nieznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                }
                                pointer += 2;
                            } else if (splittedLine[2].toUpperCase().charAt(0) == 'R') {
                                String numer = getRAddress(splittedLine[2].toUpperCase());
                                if (numer.equals("")) {
                                    throw new CompilingException(numeracjaLinii, "Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                }
                                emulatedCodeMemory.set(pointer, "01001" + numer);
                                pointer += 1;
                            } else if (splittedLine[2].toUpperCase().equals("@R1") || splittedLine[2].toUpperCase().equals("@R0")) {
                                emulatedCodeMemory.set(pointer, "0100011" + splittedLine[2].charAt(2));
                                pointer += 1;
                            } else {
                                    try {
                                        int numer = Integer.parseInt(make8DigitsStringFromNumber(splittedLine[2]), 2);
                                        emulatedCodeMemory.set(pointer, "01000101");
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2]));
                                        pointer += 2;
                                    } catch (Exception e) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Bit: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                    }
                            }
                        } else if (splittedLine[1].toUpperCase().equals("C") || splittedLine[1].toUpperCase().equals("CY")) {
                            if (splittedLine[2].charAt(0) == '/') {
                                String value = splittedLine[2].substring(1, splittedLine[2].length()).toUpperCase();
                                    try {
                                        emulatedCodeMemory.set(pointer, "10100000");
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(value));
                                        pointer += 2;
                                    } catch (Exception e) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Bit: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                    }
                            } else {
                                String value = splittedLine[2].toUpperCase();
                                    try {
                                        emulatedCodeMemory.set(pointer, "01110010");
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(value));
                                        pointer += 2;
                                    } catch (Exception e) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Bit: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                    }
                            }
                        } else {
                            if (splittedLine[2].toUpperCase().equals("A")) {

                                    try {
                                        String liczba = make8DigitsStringFromNumber(splittedLine[1]);
                                        emulatedCodeMemory.set(pointer, "01000010");
                                        emulatedCodeMemory.set(pointer + 1, liczba);
                                        pointer += 2;
                                    } catch (Exception e) {
                                        throw new CompilingException(numeracjaLinii, "Błędny bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                    }


                            } else {

                                if (splittedLine[2].charAt(0) == '#') {
                                    try {
                                            try {
                                                String liczba = make8DigitsStringFromNumber(splittedLine[1]);
                                                emulatedCodeMemory.set(pointer, "01000011");
                                                emulatedCodeMemory.set(pointer + 1, liczba);
                                                emulatedCodeMemory.set(pointer + 2, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                                pointer += 3;
                                            } catch (Exception e) {
                                                throw new CompilingException(numeracjaLinii, "Błędny bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                            }
                                    } catch (NumberFormatException e) {
                                        throw new CompilingException(numeracjaLinii, "Nieznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                    }
                                }
                            }
                        }
                    } else if (splittedLine[0].toUpperCase().equals("XRL")) {

                        if (splittedLine.length != 3)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentów funkcji XRL: '" + backupLinii + "'");


                        if (splittedLine[1].toUpperCase().equals("A")) {
                            if (splittedLine[2].charAt(0) == '#') {
                                emulatedCodeMemory.set(pointer, "01100100");
                                try {
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                } catch (NumberFormatException e) {
                                    throw new CompilingException(numeracjaLinii, "Nieznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                }
                                pointer += 2;
                            } else if (splittedLine[2].toUpperCase().charAt(0) == 'R') {
                                String numer = getRAddress(splittedLine[2].toUpperCase());
                                if (numer.equals("")) {
                                    throw new CompilingException(numeracjaLinii, "Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                }
                                emulatedCodeMemory.set(pointer, "01101" + numer);
                                pointer += 1;
                            } else if (splittedLine[2].toUpperCase().equals("@R1") || splittedLine[2].toUpperCase().equals("@R0")) {
                                emulatedCodeMemory.set(pointer, "0110011" + splittedLine[2].charAt(2));
                                pointer += 1;
                            } else {
                                    try {
                                        emulatedCodeMemory.set(pointer, "01100101");
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2]));
                                        pointer += 2;
                                    } catch (Exception e) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Bit: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                    }
                            }
                        } else {
                            if (splittedLine[2].toUpperCase().equals("A")) {
                                    try {
                                        emulatedCodeMemory.set(pointer, "01100010");
                                        emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[1]));
                                        pointer += 2;
                                    } catch (Exception e) {
                                        throw new CompilingException(numeracjaLinii, "Błędny bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                    }
                            } else {

                                if (splittedLine[2].charAt(0) == '#') {
                                    try {
                                            try {
                                                String liczba = make8DigitsStringFromNumber(splittedLine[1]);
                                                emulatedCodeMemory.set(pointer, "01100011");
                                                emulatedCodeMemory.set(pointer + 1, liczba);
                                                emulatedCodeMemory.set(pointer + 2, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                                pointer += 3;
                                            } catch (Exception e) {
                                                throw new CompilingException(numeracjaLinii, "Błędny bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                            }


                                    } catch (NumberFormatException e) {
                                        throw new CompilingException(numeracjaLinii, "Nieznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                    }
                                }
                            }
                        }
                    } else if (splittedLine[0].toUpperCase().equals("CPL")) {

                        if (splittedLine.length != 2)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentów funkcji CPL: '" + backupLinii + "'");

                        if (splittedLine[1].toUpperCase().equals("A")) {
                            emulatedCodeMemory.set(pointer, "11110100");
                            pointer++;
                        } else if (splittedLine[1].toUpperCase().equals("C")) {
                            emulatedCodeMemory.set(pointer, "10110011");
                            pointer++;
                        } else {
                                try {
                                    emulatedCodeMemory.set(pointer, "10110010");
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[1]));
                                    pointer += 2;
                                } catch (Exception e) {
                                    throw new CompilingException(numeracjaLinii, "Nierozpoznany Bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                }
                        }
                    } else if (splittedLine[0].toUpperCase().equals("JC")) {
                        if (splittedLine.length != 2) {
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilosć argumentów: '" + backupLinii + "'");
                        }
                        emulatedCodeMemory.set(pointer, "01000000");
                        emulatedCodeMemory.set(pointer + 1, splittedLine[1].toUpperCase());
                        pointer += 2;
                    } else if (splittedLine[0].toUpperCase().equals("JCN")) {
                        if (splittedLine.length != 2) {
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilosć argumentów: '" + backupLinii + "'");
                        }
                        emulatedCodeMemory.set(pointer, "01010000");
                        emulatedCodeMemory.set(pointer + 1, splittedLine[1].toUpperCase());
                        pointer += 2;
                    } else if (splittedLine[0].toUpperCase().equals("JB")) {
                        if (splittedLine.length != 3) {
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilosć argumentów: '" + backupLinii + "'");
                        }
                            try {
                                String liczba = make8DigitsStringFromNumber(splittedLine[1]);
                                emulatedCodeMemory.set(pointer, "00100000");
                                emulatedCodeMemory.set(pointer + 1, liczba);
                                emulatedCodeMemory.set(pointer + 2, splittedLine[2].toUpperCase());
                                pointer += 3;
                            } catch (Exception e) {
                                throw new CompilingException(numeracjaLinii, "Błędny bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                            }

                    } else if (splittedLine[0].toUpperCase().equals("JNB")) {
                        if (splittedLine.length != 3) {
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilosć argumentów: '" + backupLinii + "'");
                        }
                            try {
                                String liczba = make8DigitsStringFromNumber(splittedLine[1]);
                                emulatedCodeMemory.set(pointer, "00110000");
                                emulatedCodeMemory.set(pointer + 1, liczba);
                                emulatedCodeMemory.set(pointer + 2, splittedLine[2].toUpperCase());
                                pointer += 3;
                            } catch (Exception e) {
                                throw new CompilingException(numeracjaLinii, "Błędny bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                            }

                    } else if (splittedLine[0].toUpperCase().equals("JZ")) {
                        if (splittedLine.length != 2)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilosć argumentów: '" + backupLinii + "'");
                        emulatedCodeMemory.set(pointer, "01100000");
                        emulatedCodeMemory.set(pointer + 1, splittedLine[1].toUpperCase());
                        pointer += 2;
                    } else if (splittedLine[0].toUpperCase().equals("JNZ")) {
                        if (splittedLine.length != 2)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilosć argumentów: '" + backupLinii + "'");
                        emulatedCodeMemory.set(pointer, "01110000");
                        emulatedCodeMemory.set(pointer + 1, splittedLine[1].toUpperCase());
                        pointer += 2;
                    } else if (splittedLine[0].toUpperCase().equals("MOV")) {
                        if (splittedLine.length != 3)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentow, linia: '" + backupLinii + "'");
                        if (splittedLine[1].toUpperCase().equals("@R0") || splittedLine[1].toUpperCase().equals("@R1")) {
                            if (splittedLine[2].charAt(0) == '#') {
                                emulatedCodeMemory.set(pointer, "0111011" + splittedLine[1].charAt(2));
                                try {
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                } catch (NumberFormatException e) {
                                    throw new CompilingException(numeracjaLinii, "Nieznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                }
                                pointer += 2;
                            } else if (splittedLine[2].equals("A")) {
                                emulatedCodeMemory.set(pointer, "1111011" + splittedLine[1].charAt(2));
                                pointer += 1;
                            } else {
                                    try {
                                        String number = make8DigitsStringFromNumber(splittedLine[2]);
                                        emulatedCodeMemory.set(pointer, "1010011" + splittedLine[1].charAt(2));
                                        emulatedCodeMemory.set(pointer + 1, number);
                                        pointer += 2;
                                    } catch (Exception e1) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                    }

                            }
                        } else if (splittedLine[1].toUpperCase().equals("A")) {
                            if (splittedLine[2].charAt(0) == '#') {
                                emulatedCodeMemory.set(pointer, "01110100");
                                try {
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                } catch (NumberFormatException e) {
                                    throw new CompilingException(numeracjaLinii, "Nieznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                }
                                pointer += 2;
                            } else if (splittedLine[2].toUpperCase().equals("@R1") || splittedLine[2].toUpperCase().equals("@R0")) {
                                emulatedCodeMemory.set(pointer, "1110011" + splittedLine[2].charAt(2));
                                pointer += 1;
                            } else if (splittedLine[2].toUpperCase().charAt(0) == 'R') {

                                String numer = getRAddress(splittedLine[2].toUpperCase());

                                if (numer.equals("")) {
                                    throw new CompilingException(numeracjaLinii, "Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                }

                                emulatedCodeMemory.set(pointer, "11101" + numer);
                                pointer += 1;
                            } else {
                                    try {
                                        String number = make8DigitsStringFromNumber(splittedLine[2]);
                                        emulatedCodeMemory.set(pointer, "11100101");
                                        emulatedCodeMemory.set(pointer + 1, number);
                                        pointer += 2;
                                    } catch (Exception e1) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");

                                    }
                            }


                        } else if (splittedLine[1].toUpperCase().charAt(0) == 'R') {
                            if (splittedLine[2].charAt(0) == '#') {

                                String numer = getRAddress(splittedLine[1].toUpperCase());

                                if (numer.equals(""))
                                    throw new CompilingException(numeracjaLinii, "Nieznany Rejestr: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");

                                emulatedCodeMemory.set(pointer, "01111" + numer);

                                try {
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                } catch (NumberFormatException e) {
                                    throw new CompilingException(numeracjaLinii, "Nierozpoznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                }
                                pointer += 2;
                            } else if (splittedLine[2].toUpperCase().equals("A")) {

                                String numer = getRAddress(splittedLine[1].toUpperCase());

                                if (numer.equals(""))
                                    throw new CompilingException(numeracjaLinii, "Nieznany Rejestr: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");

                                emulatedCodeMemory.set(pointer, "11111" + numer);
                                pointer += 1;
                            } else {
                                String numer = getRAddress(splittedLine[1].toUpperCase());

                                if (numer.equals(""))
                                    throw new CompilingException(numeracjaLinii, "Nieznany Rejestr: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");

                                    try {
                                        String number = make8DigitsStringFromNumber(splittedLine[2]);
                                        emulatedCodeMemory.set(pointer, "10101" + numer);
                                        emulatedCodeMemory.set(pointer + 1, number);
                                        pointer += 2;
                                    } catch (Exception e1) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                    }


                            }
                        } else if (splittedLine[1].toUpperCase().equals("DPTR")) {
                            if (splittedLine[2].charAt(0) != '#') {
                                throw new CompilingException(numeracjaLinii, "Niepoprawna wartosc liczbowa ustawienia rejestru dptr: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                            }
                            try {
                                String number = make16DigitsStringFromNumber(splittedLine[2].substring(1));
                                emulatedCodeMemory.set(pointer, "10010000");
                                emulatedCodeMemory.set(pointer + 1, number.substring(0, 8));
                                emulatedCodeMemory.set(pointer + 2, number.substring(8, 16));
                                pointer += 3;

                            } catch (Exception e) {
                                throw new CompilingException(numeracjaLinii, "Niepoprawna wartosc liczbowa ustawienia rejestru dptr: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                            }

                        } else {
                            if (splittedLine[2].toUpperCase().equals("A")) {
                                    try {
                                        String number = make8DigitsStringFromNumber(splittedLine[1]);
                                        emulatedCodeMemory.set(pointer, "11110101");
                                        emulatedCodeMemory.set(pointer + 1, number);
                                        pointer += 2;
                                    } catch (Exception e1) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");

                                    }
                            } else if (splittedLine[2].toUpperCase().equals("@R0") || splittedLine[2].toUpperCase().equals("@R1")) {
                                    try {
                                        String number = make8DigitsStringFromNumber(splittedLine[1]);
                                        emulatedCodeMemory.set(pointer, "1000011" + splittedLine[2].charAt(2));
                                        emulatedCodeMemory.set(pointer + 1, number);
                                        pointer += 2;
                                    } catch (Exception e1) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");

                                    }
                            } else if (splittedLine[2].charAt(0) == '#') {
                                    try {
                                        String number = make8DigitsStringFromNumber(splittedLine[1]);
                                        emulatedCodeMemory.set(pointer, "01110101");
                                        emulatedCodeMemory.set(pointer + 1, number);
                                        try {
                                            emulatedCodeMemory.set(pointer + 2, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                            pointer += 3;
                                        } catch (NumberFormatException e1) {
                                            throw new CompilingException(numeracjaLinii, "Nieznana Liczba: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                        }
                                    } catch (Exception e1) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");

                                    }
                            } else {
                                String address1;
                                String address2;

                                    try {
                                        address1 = make8DigitsStringFromNumber(splittedLine[1]);
                                    } catch (Exception e1) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                    }

                                    try {
                                        address2 = make8DigitsStringFromNumber(splittedLine[2]);
                                    } catch (Exception e1) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                    }

                                emulatedCodeMemory.set(pointer, "10000101");
                                emulatedCodeMemory.set(pointer + 1, address1);
                                emulatedCodeMemory.set(pointer + 2, address2);
                                pointer += 3;

                            }
                        }

                    } else if (splittedLine[0].toUpperCase().equals("RL")) {
                        if (splittedLine.length != 2) {
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentow, linia: '" + backupLinii + "'");
                        }
                        if (splittedLine[1].toUpperCase().equals("A")) {
                            emulatedCodeMemory.set(pointer, "00100011");
                            pointer += 1;
                        } else {
                            throw new CompilingException(numeracjaLinii, "Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                        }
                    } else if (splittedLine[0].toUpperCase().equals("RLC")) {
                        if (splittedLine.length != 2) {
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentow, linia: '" + backupLinii + "'");
                        }
                        if (splittedLine[1].toUpperCase().equals("A")) {
                            emulatedCodeMemory.set(pointer, "00110011");
                            pointer += 1;
                        } else {
                            throw new CompilingException(numeracjaLinii, "Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                        }
                    } else if (splittedLine[0].toUpperCase().equals("RR")) {
                        if (splittedLine.length != 2) {
                            throw new CompilingException(numeracjaLinii, "Nierozpoznana linia: '" + backupLinii + "'");
                        }
                        if (splittedLine[1].toUpperCase().equals("A")) {
                            emulatedCodeMemory.set(pointer, "00000011");
                            pointer += 1;
                        } else {
                            throw new CompilingException(numeracjaLinii, "Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                        }
                    } else if (splittedLine[0].toUpperCase().equals("RRC")) {
                        if (splittedLine.length != 2) {
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentow, linia: '" + backupLinii + "'");
                        }
                        if (splittedLine[1].toUpperCase().equals("A")) {
                            emulatedCodeMemory.set(pointer, "00010011");
                            pointer += 1;
                        } else {
                            throw new CompilingException(numeracjaLinii, "Nieznana Wartosc: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                        }
                    } else if (splittedLine[0].toUpperCase().equals("ADD")) {
                        if (splittedLine.length != 3)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentow, linia: '" + backupLinii + "'");
                        if (splittedLine[1].toUpperCase().equals("A")) {
                            if (splittedLine[2].charAt(0) == '#') {
                                emulatedCodeMemory.set(pointer, "00100100");
                                try {
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                } catch (NumberFormatException e) {
                                    throw new CompilingException(numeracjaLinii, "Nierozpoznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                }
                                pointer += 2;
                            } else if (splittedLine[2].toUpperCase().charAt(0) == 'R') {

                                String numer = getRAddress(splittedLine[2].toUpperCase());

                                if (numer.equals(""))
                                    throw new CompilingException(numeracjaLinii, "Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                emulatedCodeMemory.set(pointer, "00101" + numer);
                                pointer += 1;
                            } else if (splittedLine[2].toUpperCase().equals("@R0") || splittedLine[2].toUpperCase().equals("@R1")) {
                                emulatedCodeMemory.set(pointer, "0010011" + splittedLine[2].charAt(2));
                                pointer += 1;
                            } else {
                                String address;
                                    try {
                                        address = make8DigitsStringFromNumber(splittedLine[2]);
                                        emulatedCodeMemory.set(pointer, "00100101");
                                        emulatedCodeMemory.set(pointer + 1, address);
                                        pointer += 2;
                                    } catch (Exception e1) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                    }
                            }
                        } else
                            throw new CompilingException(numeracjaLinii, "Dodawać można tylko do akumulatora, linia: '" + backupLinii + "'");
                    } else if (splittedLine[0].toUpperCase().equals("ADDC")) {
                        if (splittedLine.length != 3)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentow, linia: '" + backupLinii + "'");
                        if (splittedLine[1].toUpperCase().equals("A")) {

                            if (splittedLine[2].charAt(0) == '#') {
                                emulatedCodeMemory.set(pointer, "00110100");
                                try {
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                } catch (NumberFormatException e) {
                                    throw new CompilingException(numeracjaLinii, "Nierozpoznana Wartosc: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                }
                                pointer += 2;
                            } else if (splittedLine[2].toUpperCase().charAt(0) == 'R') {

                                String numer = getRAddress(splittedLine[2].toUpperCase());

                                if (numer.equals(""))
                                    throw new CompilingException(numeracjaLinii, "Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                emulatedCodeMemory.set(pointer, "00111" + numer);
                                pointer += 1;
                            } else if (splittedLine[2].toUpperCase().equals("@R0") || splittedLine[2].toUpperCase().equals("@R1")) {
                                emulatedCodeMemory.set(pointer, "0011011" + splittedLine[2].charAt(2));
                                pointer += 1;
                            } else {
                                String address;
                                    try {
                                        address = make8DigitsStringFromNumber(splittedLine[2]);
                                        emulatedCodeMemory.set(pointer, "00110101");
                                        emulatedCodeMemory.set(pointer + 1, address);
                                        pointer += 2;
                                    } catch (Exception e1) {
                                        throw new CompilingException(numeracjaLinii, "Dodawać można tylko do akumulatora, linia: '" + backupLinii + "'");
                                    }
                            }
                        } else
                            throw new CompilingException(numeracjaLinii, "Nierozpoznana etykieta: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                    } else if (splittedLine[0].toUpperCase().equals("SUBB")) {

                        if (splittedLine.length != 3)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentów, linia: '" + backupLinii + "'");


                        if (splittedLine[1].toUpperCase().equals("A")) {
                            if (splittedLine[2].charAt(0) == '#') {
                                emulatedCodeMemory.set(pointer, "10010100");
                                try {
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[2].substring(1)));
                                } catch (NumberFormatException e) {
                                    throw new CompilingException(numeracjaLinii, "Nierozpoznana Liczba: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                }
                                pointer += 2;
                            } else if (splittedLine[2].toUpperCase().charAt(0) == 'R') {

                                String numer = getRAddress(splittedLine[2].toUpperCase());

                                if (numer.equals(""))
                                    throw new CompilingException(numeracjaLinii, "Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");

                                emulatedCodeMemory.set(pointer, "10011" + numer);
                                pointer += 1;
                            } else if (splittedLine[2].toUpperCase().equals("@R0") || splittedLine[2].toUpperCase().equals("@R1")) {
                                emulatedCodeMemory.set(pointer, "1001011" + splittedLine[2].charAt(2));
                                pointer += 1;
                            } else {
                                String address;
                                    try {
                                        address = make8DigitsStringFromNumber(splittedLine[2]);
                                        emulatedCodeMemory.set(pointer, "10010101");
                                        emulatedCodeMemory.set(pointer + 1, address);
                                        pointer += 2;
                                    } catch (Exception e1) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                    }
                            }
                        } else {
                            throw new CompilingException(numeracjaLinii, "Odejmować można tylko od akumulatora, linia: '" + backupLinii + "'");
                        }
                    } else if (splittedLine[0].toUpperCase().equals("SETB")) {
                        if (splittedLine.length != 2)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentow, linia: '" + backupLinii + "'");
                        if (splittedLine[1].toUpperCase().equals("CY") || splittedLine[1].toUpperCase().equals("C")) {
                            emulatedCodeMemory.set(pointer, "11010011");
                            pointer += 1;
                        } else {
                                try {
                                    emulatedCodeMemory.set(pointer, "11010010");
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[1]));
                                    pointer += 2;
                                } catch (Exception e) {
                                    throw new CompilingException(numeracjaLinii, "Nierozpoznany Bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                }
                        }
                    } else if (splittedLine[0].toUpperCase().equals("CLR")) {
                        if (splittedLine.length != 2)
                            throw new CompilingException(numeracjaLinii, "Niepoprawna ilość argumentow, linia: '" + backupLinii + "'");
                        if (splittedLine[1].toUpperCase().equals("CY") || splittedLine[1].toUpperCase().equals("C")) {
                            emulatedCodeMemory.set(pointer, "11000011");
                            pointer += 1;
                        } else if (splittedLine[1].toUpperCase().equals("A")) {
                            emulatedCodeMemory.set(pointer, "11100100");
                        } else {
                                try {
                                    emulatedCodeMemory.set(pointer, "11000010");
                                    emulatedCodeMemory.set(pointer + 1, make8DigitsStringFromNumber(splittedLine[1]));
                                    pointer += 2;
                                } catch (Exception e) {
                                    throw new CompilingException(numeracjaLinii, "Nierozpoznany Bit: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");
                                }
                        }

                    } else if (splittedLine[0].toUpperCase().equals("SWAP")) {
                        if (splittedLine[1].toUpperCase().equals("A")) {
                            emulatedCodeMemory.set(pointer, "11000100");
                            pointer += 1;
                        } else {
                            throw new CompilingException(numeracjaLinii, "Komenda swap wymaga akumulatora parametru, linia: '" + backupLinii + "'");
                        }
                    } else if (splittedLine[0].toUpperCase().equals("NOP")) {
                        emulatedCodeMemory.set(pointer, "00000000");
                        pointer += 1;
                    } else if (splittedLine[0].toUpperCase().equals("PUSH")) {
                            try {
                                String number = make8DigitsStringFromNumber(splittedLine[1]);
                                emulatedCodeMemory.set(pointer, "11000000");
                                emulatedCodeMemory.set(pointer + 1, number);
                                pointer += 2;
                            } catch (Exception e1) {
                                throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");

                            }
                    } else if (splittedLine[0].toUpperCase().equals("POP")) {
                            try {
                                String number = make8DigitsStringFromNumber(splittedLine[1]);
                                emulatedCodeMemory.set(pointer, "11010000");
                                emulatedCodeMemory.set(pointer + 1, number);
                                pointer += 2;
                            } catch (Exception e1) {
                                throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[1] + "', linia: '" + backupLinii + "'");

                            }
                    } else if (splittedLine[0].toUpperCase().equals("RET")) {
                        if (splittedLine.length != 1)
                            throw new CompilingException(numeracjaLinii, "'RET' nie ma żadnych argumentów, linia: '" + backupLinii + "'");
                        else {
                            emulatedCodeMemory.set(pointer, "00100010");
                            pointer += 1;
                        }
                    } else if (splittedLine[0].toUpperCase().equals("RETI")) {
                        if (splittedLine.length != 1)
                            throw new CompilingException(numeracjaLinii, "'RETI' nie ma żadnych argumentów, linia: '" + backupLinii + "'");
                        else {
                            emulatedCodeMemory.set(pointer, "00110010");
                            pointer += 1;
                        }
                    } else if (splittedLine[0].toUpperCase().equals("MUL")) {
                        if (splittedLine[1].toUpperCase().equals("AB")) {
                            emulatedCodeMemory.set(pointer, "10100100");
                            pointer += 1;
                        } else
                            throw new CompilingException(numeracjaLinii, "Niepoprawny argument mnożenia: '" + backupLinii + "'");
                    } else if (splittedLine[0].toUpperCase().equals("DIV")) {
                        if (splittedLine[1].toUpperCase().equals("AB")) {
                            emulatedCodeMemory.set(pointer, "10000100");
                            pointer += 1;
                        } else
                            throw new CompilingException(numeracjaLinii, "Niepoprawny argument dzielenia: '" + backupLinii + "'");
                    } else if (splittedLine[0].toUpperCase().equals("XCHD")) {
                        if ((splittedLine[1].toUpperCase().equals("A")) && ((splittedLine[2].toUpperCase().equals("@R0")) || (splittedLine[2].toUpperCase().equals("@R1")))) {
                            emulatedCodeMemory.set(pointer, "1101011" + splittedLine[2].charAt(2));
                            pointer += 1;
                        } else
                            throw new CompilingException(numeracjaLinii, "Błędne użycie instrukcji, linia: '" + backupLinii + "'");
                    } else if (splittedLine[0].toUpperCase().equals("XCH")) {
                        if (splittedLine[1].toUpperCase().equals("A")) {
                            if (splittedLine[2].toUpperCase().equals("@R0") || splittedLine[2].toUpperCase().equals("@R1")) {
                                emulatedCodeMemory.set(pointer, "1100011" + splittedLine[2].charAt(2));
                                pointer += 1;
                            } else if (splittedLine[2].toUpperCase().charAt(0) == 'R') {
                                String numer = getRAddress(splittedLine[2].toUpperCase());

                                if (numer.equals(""))
                                    throw new CompilingException(numeracjaLinii, "Nieznany Rejestr: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                emulatedCodeMemory.set(pointer, "11001" + numer);
                                pointer += 1;
                            } else {
                                String address;
                                    try {
                                        address = make8DigitsStringFromNumber(splittedLine[2]);
                                        emulatedCodeMemory.set(pointer, "11000101");
                                        emulatedCodeMemory.set(pointer + 1, address);
                                        pointer += 2;
                                    } catch (Exception e1) {
                                        throw new CompilingException(numeracjaLinii, "Nierozpoznany Adres: '" + splittedLine[2] + "', linia: '" + backupLinii + "'");
                                    }
                            }
                        } else
                            throw new CompilingException(numeracjaLinii, "Pierwszy argumentem powinien być akumulator: '" + backupLinii + "'");
                    } else if (splittedLine[0].toUpperCase().equals("SJMP")) {
                        emulatedCodeMemory.set(pointer, "10000000");
                        emulatedCodeMemory.set(pointer + 1, splittedLine[1].toUpperCase());
                        pointer += 2;

                    } else {
                        throw new CompilingException(numeracjaLinii, "Nierozpoznana komenda, linia: '" + backupLinii + "'");
                    }
                    StringBuilder hexPointer = new StringBuilder(Integer.toHexString(backupPointer));
                    while (hexPointer.length() < 4)
                        hexPointer.insert(0, "0");
                    linieZNumerami.add(hexPointer + " \t" + String.valueOf(backupLinii.trim()));
                }
            }
        }
        for (int i = 0; i < 65536; i++) {
            String s = emulatedCodeMemory.get(i);
            if (s.charAt(s.length() - 1) != '0' && s.charAt(s.length() - 1) != '1') {
                int numer = getLineFromLabel(s);
                if (numer != -1) {
                    if (emulatedCodeMemory.get(i - 1).equals("00000010") || emulatedCodeMemory.get(i - 1).equals("00010010")) {
                        emulatedCodeMemory.set(i, make16DigitsStringFromNumber(Integer.toString(numer) + "d").substring(0, 8));
                        emulatedCodeMemory.set(i + 1, make16DigitsStringFromNumber(Integer.toString(numer) + "d").substring(8, 16));
                    } else if(emulatedCodeMemory.get(i-1).substring(3).equals("00001")) {
                        String destination = make16DigitsStringFromNumber(Integer.toString(numer) + "d");
                        int destinationInt = Integer.parseInt(destination,2);
                        if((i-1)/2048 == destinationInt/2048) {
                            destination = destination.substring(5);
                            emulatedCodeMemory.set(i-1,destination.substring(0,3) + "00001");
                            emulatedCodeMemory.set(i,destination.substring(3,11));
                        }
                        else
                            throw new CompilingException(i-1, "Przekroczono zakres skoku AJMP");


                    }
                    else {
                        //offset
                        int wynik = numer - i - 1;
                        if (wynik < 0)
                            wynik += 256;
                        if (wynik < 0 || wynik > 255)
                            throw new CompilingException(i-1, "Przekroczono zakres skoku: '" + s + "'");
                        if (numer > i && wynik > 128)
                            throw new CompilingException(i-1, "Przekroczono zakres skoku: '" + s + "'");
                        if (numer < i && wynik < 128)
                            throw new CompilingException(i-1, "Przekroczono zakres skoku: '" + s + "'");
                        emulatedCodeMemory.set(i, make8DigitsStringFromNumber(Integer.toString(wynik) + "d"));
                    }
                } else {
                    throw new CompilingException(numeracjaLinii, "Nieznana Etykieta: '" + s + "'");
                }
            }
        }
        addresses.add(pointer);
        rozmiarTabeliAdresow++;
        if (rozmiarTabeliAdresow % 2 == 1)
            addresses.add(rozmiarPoczatkowy, 0);

        if (isMain) {
            for (int g = addresses.size() - 1; g > 0; g -= 2) {
                if (addresses.get(g).equals(addresses.get(g - 1))) {
                    addresses.remove(g);
                    addresses.remove(g - 1);
                }
            }
            int i = 0;
            while (addresses.size() >= 2) {
                int x = addresses.get(i);
                int y = addresses.get(i + 1);
                if (y - x > 16) {
                    addresses.add(i + 1, x + 16);
                    addresses.add(i + 1, x + 16);
                }
                i += 2;
                if (i >= addresses.size())
                    break;
            }


            wynik.clear();
            for (int k = 0; k < addresses.size(); k += 2) {
                StringBuilder linia = new StringBuilder(":");
                int x = addresses.get(k);
                int y = addresses.get(k + 1);

                int iloscBajtow = y - x;
                String iloscBajtowString = Integer.toHexString(iloscBajtow).toUpperCase();
                if (iloscBajtowString.length() == 1)
                    iloscBajtowString = "0" + iloscBajtowString;

                linia.append(iloscBajtowString);

                StringBuilder adresString = new StringBuilder(Integer.toHexString(x));
                while (adresString.length() < 4) {
                    adresString.insert(0, "0");
                }
                linia.append(adresString).append("00");


                int licznik = x;
                for (; licznik < y; licznik++) {
                    String hexToAdd = Integer.toHexString(Integer.parseInt(emulatedCodeMemory.get(licznik), 2)).toUpperCase();
                    if (hexToAdd.length() == 1)
                        hexToAdd = "0" + hexToAdd;
                    linia.append(hexToAdd);
                }

                int wartosc = 0;
                for (int w = 1; w < linia.length(); w += 2) {
                    wartosc += Integer.parseInt(linia.substring(w, w + 2), 16);
                }
                wartosc = 256 - wartosc;

                String checkSum = Integer.toHexString(wartosc).toUpperCase();
                if(checkSum.length()>2) {
                    checkSum = checkSum.substring(Integer.toHexString(wartosc).length() - 2, Integer.toHexString(wartosc).length());
                }

                if (checkSum.length() == 1)
                    checkSum = "0" + checkSum;
                linia.append(checkSum);
                wynik.add(linia.toString());
            }
            wynik.add(":00000001FF");
        }

        Main.stage.compilationErrorsLabel.setText("Asemblacja przebiegła pomyślnie");
        Main.stage.compilationErrorsLabel.setStyle("-fx-background-color: lightgreen; -fx-background-radius: 10; -fx-background-insets: 0 20 0 20");
        //show();
        return linieZNumerami;
    }

    private ArrayList<String> emulatedCodeMemory;
    private ArrayList<Pair<String, Integer>> labels = new ArrayList<>();


    public String make8DigitsStringFromNumber(String number) throws NumberFormatException {
        StringBuilder result;
        char lastSymbol = number.charAt(number.length() - 1);
        if (lastSymbol == 'd' || lastSymbol == 'D')
            number = number.substring(0, number.length() - 1);
        int wartosc;
        if (lastSymbol == 'b' || lastSymbol == 'B') {
            try {
                number = number.substring(0, number.length() - 1);
                wartosc = Integer.parseInt(number, 2);
            } catch (Exception e) {
                throw new NumberFormatException();
            }
        } else if (lastSymbol == 'h' || lastSymbol == 'H') {
            try {
                number = number.substring(0, number.length() - 1);
                wartosc = Integer.parseInt(number, 16);
            } catch (Exception e) {
                throw new NumberFormatException();
            }
        } else {
            try {
                wartosc = Integer.parseInt(number);
            } catch (Exception e) {
                throw new NumberFormatException();
            }
        }
        if (wartosc > 255 || wartosc < 0)
            throw new NumberFormatException();
        result = new StringBuilder(Integer.toBinaryString(wartosc));
        int length = result.length();
        for (int i = length; i < 8; i++) {
            result.insert(0, "0");
        }
        return result.toString();
    }

    public String make16DigitsStringFromNumber(String number) throws NumberFormatException {
        StringBuilder result;
        char lastSymbol = number.charAt(number.length() - 1);
        if (lastSymbol == 'd' || lastSymbol == 'D')
            number = number.substring(0, number.length() - 1);
        int wartosc;
        if (lastSymbol == 'b' || lastSymbol == 'B') {
            try {
                number = number.substring(0, number.length() - 1);
                wartosc = Integer.parseInt(number, 2);
            } catch (Exception e) {
                throw new NumberFormatException();
            }
        } else if (lastSymbol == 'h' || lastSymbol == 'H') {
            try {
                number = number.substring(0, number.length() - 1);
                wartosc = Integer.parseInt(number, 16);
            } catch (Exception e) {
                throw new NumberFormatException();
            }
        } else {
            try {
                wartosc = Integer.parseInt(number);
            } catch (Exception e) {
                throw new NumberFormatException();
            }
        }

        result = new StringBuilder(Integer.toBinaryString(wartosc));
        int length = result.length();
        if(length>16)
            throw new NumberFormatException();
        for (int i = length; i < 16; i++) {
            result.insert(0, "0");
        }
        return result.toString();
    }

    boolean possibleLabel(String number) {

        if(Character.isDigit(number.charAt(0)))
            return false;

        char lastSymbol = number.charAt(number.length() - 1);
        int wartosc;
        if (lastSymbol == 'h' || lastSymbol == 'H') {
                number = number.substring(0, number.length() - 1);
        }

        try {
            wartosc = Integer.parseInt(number,16);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    private String getRAddress(String label) {
        switch (label) {
            case "R0":
                return "000";
            case "R1":
                return "001";
            case "R2":
                return "010";
            case "R3":
                return "011";
            case "R4":
                return "100";
            case "R5":
                return "101";
            case "R6":
                return "110";
            case "R7":
                return "111";
            default:
                return "";
        }
    }

    public Map<String, String> bitAddresses = new HashMap<>();

    private ArrayList<Integer> addresses = new ArrayList<>();

    private ArrayList<String> wynik = new ArrayList<>();

    public ArrayList<String> getIntelHex() {
        return wynik;
    }

}
