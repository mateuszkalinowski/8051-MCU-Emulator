package core;

import javafx.application.Application;
import javafx.stage.Stage;
import microcontroller.Board;
import microcontroller.Cpu;
import stages.MainStage;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        double initialWidth = 800;
        double initialHeight = 620;
        stage = new MainStage(initialWidth, initialHeight);

      //  ArrayList<String> testowa = new ArrayList<>();

       // testowa.add("0");
       // testowa.add("0");
       // testowa.add("0");
       // testowa.remove("0");
       // System.out.printf("" + testowa.contains("0"));

     //   for(String s : testowa)
     //       System.out.println(s);

        path = System.getProperty("user.home") + "/Documents/8051MCUEmulator";
        File resources = new File(path);
        try {
            if (!resources.exists()) {
                resources.mkdir();
            }
            if (!new File(path + "/options").exists()) {
                new File(path + "/options").createNewFile();
                PrintWriter createCfg = new PrintWriter(new File(path + "/options"));

                createCfg.println("ledsPort=P0");
                createCfg.println("ledsType=Katoda");
                createCfg.println("ledsColor=Czerwony");

                createCfg.println("seg7DisplayPort=P1");
                createCfg.println("seg7ConnectionType=0");
                createCfg.println("seg7Color=Czerwony");

                createCfg.println("przyciskiPrzerwania=P3.2");
                createCfg.println("zadajnikiPrzerwania=P3.3");

                createCfg.println("przetwornikDACPort=P0");
                createCfg.println("przetwornikDACWR=P1.6");
                createCfg.println("przetwornikDACCS=P1.7");

                createCfg.close();
                loadDefaultSettings();
            }
            else {
                Scanner in = new Scanner(new File(path + "/options"));
                while(in.hasNextLine()) {
                    String line = in.nextLine();
                    String[] divided = line.split("=");
                    settingsMap.put(divided[0],divided[1]);
                }
            }
        }catch (Exception e) {
            loadDefaultSettings();
        }


        stage.start(primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void loadDefaultSettings() {

        settingsMap.put("ledsPort","P0");
        settingsMap.put("ledsType","Katoda");
        settingsMap.put("ledsColor","Czerwony");

        settingsMap.put("seg7DisplayPort","P1");
        settingsMap.put("seg7ConnectionType","0");
        settingsMap.put("seg7Color","Czerwony");

        settingsMap.put("przyciskiPrzerwania","P3.2");
        settingsMap.put("zadajnikiPrzerwania","P3.3");

        settingsMap.put("przetwornikDACPort","P0");
        settingsMap.put("przetwornikDACWR","P1.6");
        settingsMap.put("przetwornikDACCS","P1.7");

    }
    public static void saveSettings() {
        try {
            PrintWriter cfgWriter = new PrintWriter(new File(path + "/options"));
            for(String s : settingsMap.keySet()) {
                cfgWriter.println(s + "=" + settingsMap.get(s));
            }
            cfgWriter.close();

        }
        catch (Exception ignored){}



    }

    public static MainStage stage;

    public static Cpu cpu = new Cpu();

    public static Board board = new Board();

    public static String path;

    public static Map<String,String> settingsMap = new HashMap<String,String>();
}
