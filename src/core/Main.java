package core;

import components.Memory;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
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
    double initialWidth = 800;
    double initialHeight = 600;

    public static Cpu cpu = new Cpu();
}
