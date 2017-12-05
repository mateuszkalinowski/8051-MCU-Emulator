package stages;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class DCPowerSupplyStage extends Application {
    public DCPowerSupplyStage(){

        VBox mainVBox = new VBox();
        mainVBox.setSpacing(5);
        mainVBox.setPadding(new Insets(10,10,10,10));
        mainBorderPane.setCenter(mainVBox);

        Label currentLabel = new Label("0.000");
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
            currentLabel.setText(textToSet.toString());
        });

        intervalSlider.setMaxWidth(Double.MAX_VALUE);

        mainVBox.getChildren().add(intervalSlider);
        VBox.setVgrow(intervalSlider,Priority.ALWAYS);

        Scene mainScene = new Scene(mainBorderPane,350,120);
        mainStage.setScene(mainScene);
        mainStage.setTitle("Zasilacz DC");
        mainStage.setWidth(350);
        mainStage.setHeight(120);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage.show();
    }

    private BorderPane mainBorderPane = new BorderPane();
    private Stage mainStage = new Stage();

    public double current = 0.0;
}
