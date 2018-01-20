package stages;

import core.Main;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import microcontroller.Dac;

import java.util.ArrayList;

/**
 * Created by Mateusz on 24.09.2017.
 * Project InferenceEngine
 */
public class OscilloscopeStage extends Application {
     OscilloscopeStage() {
        createChart();

        Label portSelectLabel = new Label("Port: ");
        portSelectLabel.setMaxWidth(Double.MAX_VALUE);
        portSelectLabel.setAlignment(Pos.CENTER);
        portSelectLabel.setFont(new Font("Arial",14));

        Label intervalSelectLabel = new Label("Interwał: ");
        intervalSelectLabel.setMaxWidth(Double.MAX_VALUE);
        intervalSelectLabel.setAlignment(Pos.CENTER);
        intervalSelectLabel.setFont(new Font("Arial",14));

        Label rangeSelectLabel = new Label("Zakres: ");
        rangeSelectLabel.setMaxWidth(Double.MAX_VALUE);
        rangeSelectLabel.setAlignment(Pos.CENTER);
        rangeSelectLabel.setFont(new Font("Arial",14));

        intervalSelectionLabel = new Label();
        intervalSelectionLabel.setText("1 mikrosekunda");
        intervalSelectionLabel.setAlignment(Pos.CENTER);
        intervalSelectionLabel.setFont(new Font("Arial",14));
        intervalSelectionLabel.setMaxWidth(Double.MAX_VALUE);
        intervalSelectionLabel.setMinWidth(120);

        ComboBox<String> portSelectComboBox = new ComboBox<>();
        portSelectComboBox.getItems().addAll("P0","P1","P2","P3");
        portSelectComboBox.setMaxWidth(100);
        portSelectComboBox.getSelectionModel().selectFirst();

        portSelectComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            resetPrzebieg();
            createChart();
        });

        Slider intervalSlider = new Slider();
        intervalSlider.setMin(1);
        intervalSlider.setMax(10);
        intervalSlider.setValue(interval);
        intervalSlider.setShowTickLabels(true);
        intervalSlider.setShowTickMarks(true);
        intervalSlider.setMajorTickUnit(1);
        intervalSlider.setMinorTickCount(0);
        intervalSlider.setSnapToTicks(true);

        intervalSlider.valueProperty().addListener((observable, oldValue, newValue) -> {
            int tmpInterval = newValue.intValue();
            if(tmpInterval==1) {
                interval = 1;
                intervalSelectionLabel.setText("1 mikrosekunda");
            }
            if(tmpInterval==2) {
                interval = 10;
                intervalSelectionLabel.setText("10 mikrosekund");
            }
            if(tmpInterval==3) {
                interval = 50;
                intervalSelectionLabel.setText("50 mikrosekund");
            }
            if(tmpInterval==4) {
                interval = 100;
                intervalSelectionLabel.setText("100 mikrosekund");
            }
            if(tmpInterval==5) {
                interval = 1000;
                intervalSelectionLabel.setText("1 milisekunda");
            }
            if(tmpInterval==6) {
                interval = 10000;
                intervalSelectionLabel.setText("10 milisekund");
            }
            if(tmpInterval==7) {
                interval = 50000;
                intervalSelectionLabel.setText("50 milisekund");
            }
            if(tmpInterval==8) {
                interval = 100000;
                intervalSelectionLabel.setText("100 milisekund");
            }
            if(tmpInterval==9) {
                interval = 1000000;
                intervalSelectionLabel.setText("1 sekunda");
            }
            if(tmpInterval==10) {
                interval = 10000000;
                intervalSelectionLabel.setText("10 sekund");
            }

            resetPrzebieg();
            createChart();
        });

        HBox lowerButtonBox = new HBox();
        lowerButtonBox.setPadding(new Insets(10,10,10,10));
        lowerButtonBox.setAlignment(Pos.CENTER);

        lowerButtonBox.setSpacing(5);

         Label xRangeLabel = new Label("Zakres X: ");
        xRangeLabel.setMaxWidth(Double.MAX_VALUE);
        xRangeLabel.setAlignment(Pos.CENTER);
        xRangeLabel.setFont(new Font("Arial",14));

         Label yRangeLabel = new Label("Zakres Y: ");
        yRangeLabel.setMaxWidth(Double.MAX_VALUE);
        yRangeLabel.setAlignment(Pos.CENTER);
        yRangeLabel.setFont(new Font("Arial",14));

        HBox.setHgrow(xRangeLabel,Priority.ALWAYS);
        HBox.setHgrow(yRangeLabel,Priority.ALWAYS);

         Slider XRangeSelect = new Slider();
        XRangeSelect.setMin(100);
        XRangeSelect.setMax(900);
        XRangeSelect.setValue(XAxisRange);
        XRangeSelect.setShowTickLabels(true);
        XRangeSelect.setShowTickMarks(true);
        XRangeSelect.setMajorTickUnit(100);
        XRangeSelect.setMinorTickCount(1);
        XRangeSelect.setSnapToTicks(true);

        XRangeSelect.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                XAxisRange = newValue.doubleValue();
                scale = (int)newValue.doubleValue();
                resetPrzebieg();
                createChart();
            }
        });


         Slider YRangeSelect = new Slider();
        YRangeSelect.setMin(1);
        YRangeSelect.setMax(5);
        YRangeSelect.setValue(YAxisRange);
        YRangeSelect.setShowTickLabels(true);
        YRangeSelect.setShowTickMarks(true);
        YRangeSelect.setMajorTickUnit(1);
        YRangeSelect.setMinorTickCount(1);
        YRangeSelect.setSnapToTicks(true);

        YRangeSelect.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                YAxisRange = newValue.doubleValue();
                resetPrzebieg();
                createChart();
            }
        });

        VBox RightVBox = new VBox();
        RightVBox.setSpacing(5);
        RightVBox.setPadding(new Insets(10,10,10,10));

        RightVBox.getChildren().add(xRangeLabel);
        RightVBox.getChildren().add(XRangeSelect);

        RightVBox.getChildren().add(yRangeLabel);
        RightVBox.getChildren().add(YRangeSelect);

        RightVBox.getChildren().add(intervalSelectLabel);
        RightVBox.getChildren().add(intervalSlider);
        RightVBox.getChildren().add(intervalSelectionLabel);

        mainBorderPane.setRight(RightVBox);
        mainBorderPane.setPadding(new Insets(10,10,10,10));
        Scene mainScene = new Scene(mainBorderPane,700,450);
        mainScene.getStylesheets().add(MainStage.class.getResource("style.css").toExternalForm());
        mainStage.setScene(mainScene);
        mainStage.setTitle("Oscyloskop");
        mainStage.setResizable(true);
        mainStage.setMinHeight(450);
        mainStage.setMinWidth(700);
    }
    @Override
    public void start(Stage primaryStage) throws Exception {
        mainStage.show();
    }

    void updateChart() {
        if(usedScale==scale) {
            usedScale = 0;
            series.getData().clear();
        }
        if (Main.cpu.getTimePassed() - passedTime >= interval) {
            passedTime = Main.cpu.getTimePassed();
            series.getData().add(new XYChart.Data(usedScale,Double.parseDouble(Dac.getValue())));
            usedScale++;
        }
    }


    private void createChart(){
        final NumberAxis xAxis = new NumberAxis(0,XAxisRange,XAxisRange/10.0);
        final NumberAxis yAxis = new NumberAxis(-YAxisRange*0.1,YAxisRange*1.1,YAxisRange*1.2/10.0);

        final ScatterChart<Number,Number> lineChart =
                new ScatterChart<Number,Number>(xAxis,yAxis);
        lineChart.setLegendVisible(false);
        //lineChart.setTitle("Oscyloskop");
        series = new XYChart.Series();
        lineChart.getData().add(series);
        mainBorderPane.setCenter(lineChart);

    }
    private BorderPane mainBorderPane = new BorderPane();
    private Stage mainStage = new Stage();
    private XYChart.Series series;

    private long passedTime = 0;
    private int scale = 100;
    private int usedScale = 0;

    private double XAxisRange = 100;
    private double YAxisRange = 5;

    private int interval = 1;

    void resetPrzebieg() {
        passedTime = 0;
        usedScale = 0;
        series.getData().clear();
    }
    private Label intervalSelectionLabel;


}
