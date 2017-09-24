package stages;

import core.Main;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * Created by Mateusz on 24.09.2017.
 * Project InferenceEngine
 */
public class OscilloscopeStage extends Application {
    public OscilloscopeStage() {

        createChart();

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
        }
        if (Main.cpu.getTimePassed() - passedTime >= interval) {
            passedTime = Main.cpu.getTimePassed();
            int wartoscp0 = Main.cpu.mainMemory.get("P0");
            series.getData().add(new XYChart.Data(usedScale,wartoscp0));
            usedScale++;
        }
    }

    private void createChart(){
        final NumberAxis xAxis = new NumberAxis(0,100,10);
        final NumberAxis yAxis = new NumberAxis(0,100,10);

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
        scale = 100;
        usedScale = 0;
        series.getData().clear();
    }
}
