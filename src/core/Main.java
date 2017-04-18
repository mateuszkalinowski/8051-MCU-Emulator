package core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        GridPane mainGridPane = new GridPane();
        primaryStage.setTitle("MkSim51");
        primaryStage.setScene(new Scene(mainGridPane, 700, 500));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
