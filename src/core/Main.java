package core;

import javafx.application.Application;
import javafx.stage.Stage;
import microcontroller.Cpu;
import stages.MainStage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = new MainStage(initialWidth,initialHeight);
        stage.start(primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static MainStage stage;
    private double initialWidth = 800;
    private double initialHeight = 600;

    public static Cpu cpu = new Cpu();
}
