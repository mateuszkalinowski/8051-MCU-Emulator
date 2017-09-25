package stages;

import core.Main;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Created by Mateusz on 24.09.2017.
 * Project InferenceEngine
 */
public class OscilloscopeStage extends Application {
    public OscilloscopeStage() {

        createChart();

        portSelectLabel = new Label("Port: ");
        portSelectLabel.setMaxWidth(Double.MAX_VALUE);
        portSelectLabel.setAlignment(Pos.CENTER);
        portSelectLabel.setFont(new Font("Arial",14));

        intervalSelectLabel = new Label("Interwał: ");
        intervalSelectLabel.setMaxWidth(Double.MAX_VALUE);
        intervalSelectLabel.setAlignment(Pos.CENTER);
        intervalSelectLabel.setFont(new Font("Arial",14));

        rangeSelectLabel = new Label("Zakres: ");
        rangeSelectLabel.setMaxWidth(Double.MAX_VALUE);
        rangeSelectLabel.setAlignment(Pos.CENTER);
        rangeSelectLabel.setFont(new Font("Arial",14));


        portSelectComboBox = new ComboBox<>();
        portSelectComboBox.getItems().addAll("P0","P1","P2","P3");
        portSelectComboBox.setMaxWidth(100);
        portSelectComboBox.getSelectionModel().selectFirst();

        intervalSlider = new Slider();
        intervalSlider.setMin(1);
        intervalSlider.setMax(100);
        intervalSlider.setValue(interval);

        intervalSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                interval = newValue.intValue();
                resetPrzebieg();
                createChart();
            }
        });

        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(10,10,10,10));
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(portSelectLabel,Priority.ALWAYS);
        HBox.setHgrow(portSelectComboBox,Priority.ALWAYS);
        HBox.setHgrow(intervalSelectLabel,Priority.ALWAYS);

        buttonBox.setSpacing(5);

        buttonBox.getChildren().addAll(portSelectLabel,portSelectComboBox,intervalSelectLabel,intervalSlider);

        mainBorderPane.setBottom(buttonBox);

        Scene mainScene = new Scene(mainBorderPane, 600, 500);
        mainStage.setScene(mainScene);
        mainStage.setTitle("Oscyloskop");
        mainStage.setResizable(false);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage.show();
    }

    public void updateChart() {
        if(usedScale==scale) {
            usedScale = 0;
            series.getData().clear();
        }
        if (Main.cpu.getTimePassed() - passedTime >= interval) {
            passedTime = Main.cpu.getTimePassed();
            int wartoscp0 = Main.cpu.mainMemory.get(portSelectComboBox.getSelectionModel().getSelectedItem());
            series.getData().add(new XYChart.Data(usedScale,wartoscp0));
            usedScale++;
        }
    }

    private void createChart(){
        final NumberAxis xAxis = new NumberAxis(0,100,10);
        final NumberAxis yAxis = new NumberAxis(0,255,10);

        final ScatterChart<Number,Number> lineChart =
                new ScatterChart<Number,Number>(xAxis,yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setTitle("Oscyloskop");
        series = new XYChart.Series();
        lineChart.getData().add(series);
        mainBorderPane.setCenter(lineChart);

    }
    BorderPane mainBorderPane = new BorderPane();
    Stage mainStage = new Stage();
    private XYChart.Series series;

    private long passedTime = 0;
    private int scale = 100;
    private int usedScale = 0;

    private int interval = 1;

    public void resetPrzebieg() {
        passedTime = 0;
        usedScale = 0;
        series.getData().clear();
    }

    Label portSelectLabel;
    Label intervalSelectLabel;
    Label rangeSelectLabel;
    ComboBox<String> portSelectComboBox;
    Slider intervalSlider;
    Slider rangeSelect;


}
