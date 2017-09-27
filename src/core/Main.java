package core;

import javafx.application.Application;
import javafx.stage.Stage;
import microcontroller.Cpu;
import stages.MainStage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        double initialWidth = 800;
        double initialHeight = 600;
        stage = new MainStage(initialWidth, initialHeight);
        stage.start(primaryStage);

    }

    public static void main(String[] args) {
        launch(args);
    }

    public static MainStage stage;

    public static Cpu cpu = new Cpu();
}
