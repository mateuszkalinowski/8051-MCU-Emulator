package stages;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DCPowerSupplyStage extends Application {
    DCPowerSupplyStage(){

        VBox mainVBox = new VBox();
        mainVBox.setSpacing(5);
        mainVBox.setPadding(new Insets(10,10,10,10));
        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setCenter(mainVBox);

        Label currentLabel = new Label("0.000 V");
        currentLabel.setFont(new Font("Courier New",24));
        currentLabel.setMaxWidth(Double.MAX_VALUE);
        currentLabel.setAlignment(Pos.CENTER);
        mainVBox.getChildren().add(currentLabel);
        VBox.setVgrow(currentLabel, Priority.ALWAYS);

        Slider intervalSlider = new Slider();
        intervalSlider.setMin(0);
        intervalSlider.setMax(5);
        intervalSlider.setValue(current);
        intervalSlider.setShowTickLabels(true);
        intervalSlider.setShowTickMarks(true);
        intervalSlider.setMajorTickUnit(1);
        intervalSlider.setMinorTickCount(10);
        intervalSlider.setSnapToTicks(false);

        intervalSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            current = newValue.doubleValue();
            StringBuilder textToSet = new StringBuilder(Double.toString(current));
            while(textToSet.length()<5)
                textToSet.append("0");
            textToSet = new StringBuilder(textToSet.substring(0, 5));
            currentLabel.setText(textToSet.toString() + " V");
        });

        intervalSlider.setMaxWidth(Double.MAX_VALUE);

        mainVBox.getChildren().add(intervalSlider);
        VBox.setVgrow(intervalSlider,Priority.ALWAYS);

        HBox buttonsHBox = new HBox();
        buttonsHBox.setSpacing(5);
        buttonsHBox.setAlignment(Pos.CENTER);
        mainVBox.getChildren().add(buttonsHBox);

        Button prog0Volt = new Button();
        prog0Volt.setFont(new Font("Courier New",14));
        prog0Volt.setText("0 V");
        prog0Volt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                current = 0.0;
                StringBuilder textToSet = new StringBuilder(Double.toString(current));
                while(textToSet.length()<5)
                    textToSet.append("0");
                textToSet = new StringBuilder(textToSet.substring(0, 5));
                currentLabel.setText(textToSet.toString() + " V");
                intervalSlider.setValue(current);
            }
        });

        Button prog1Volt = new Button();
        prog1Volt.setFont(new Font("Courier New",14));
        prog1Volt.setText("1 V");
        prog1Volt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                current = 1.0;
                StringBuilder textToSet = new StringBuilder(Double.toString(current));
                while(textToSet.length()<5)
                    textToSet.append("0");
                textToSet = new StringBuilder(textToSet.substring(0, 5));
                currentLabel.setText(textToSet.toString() + " V");
                intervalSlider.setValue(current);
            }
        });

        Button prog2Volt = new Button();
        prog2Volt.setFont(new Font("Courier New",14));
        prog2Volt.setText("2 V");
        prog2Volt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                current = 2.0;
                StringBuilder textToSet = new StringBuilder(Double.toString(current));
                while(textToSet.length()<5)
                    textToSet.append("0");
                textToSet = new StringBuilder(textToSet.substring(0, 5));
                currentLabel.setText(textToSet.toString() + " V");
                intervalSlider.setValue(current);
            }
        });

        Button prog3Volt = new Button();
        prog3Volt.setFont(new Font("Courier New",14));
        prog3Volt.setText("3 V");
        prog3Volt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                current = 3.0;
                StringBuilder textToSet = new StringBuilder(Double.toString(current));
                while(textToSet.length()<5)
                    textToSet.append("0");
                textToSet = new StringBuilder(textToSet.substring(0, 5));
                currentLabel.setText(textToSet.toString() + " V");
                intervalSlider.setValue(current);
            }
        });

        Button prog4Volt = new Button();
        prog4Volt.setFont(new Font("Courier New",14));
        prog4Volt.setText("4 V");
        prog4Volt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                current = 4.0;
                StringBuilder textToSet = new StringBuilder(Double.toString(current));
                while(textToSet.length()<5)
                    textToSet.append("0");
                textToSet = new StringBuilder(textToSet.substring(0, 5));
                currentLabel.setText(textToSet.toString() + " V");
                intervalSlider.setValue(current);
            }
        });

        Button prog5Volt = new Button();
        prog5Volt.setFont(new Font("Courier New",14));
        prog5Volt.setText("5 V");
        prog5Volt.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                current = 5.0;
                StringBuilder textToSet = new StringBuilder(Double.toString(current));
                while(textToSet.length()<5)
                    textToSet.append("0");
                textToSet = new StringBuilder(textToSet.substring(0, 5));
                currentLabel.setText(textToSet.toString() + " V");
                intervalSlider.setValue(current);
            }
        });


        buttonsHBox.getChildren().addAll(prog0Volt,prog1Volt,prog2Volt,prog3Volt,prog4Volt,prog5Volt);

        Scene mainScene = new Scene(mainBorderPane,350,120);
        mainStage.setScene(mainScene);
        mainStage.setTitle("Zasilacz DC");
        mainStage.setWidth(350);
        mainStage.setHeight(150);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage.show();
    }

    private Stage mainStage = new Stage();

    public double current = 0.0;
}
