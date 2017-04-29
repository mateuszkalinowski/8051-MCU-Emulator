package stages;

import core.Main;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * Created by Mateusz on 29.04.2017.
 * Project 8051 MCU Emulator
 */
public class PaneConfigStage extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        Stage mainStage = new Stage();
        GridPane mainGridPane = new GridPane();
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(50);
        mainGridPane.getColumnConstraints().addAll(column,column);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(14.2);
        for(int i = 0; i < 7;i++)
            mainGridPane.getRowConstraints().addAll(row);

        Label ledsLabel = new Label("Diody:");
        ledsLabel.setAlignment(Pos.CENTER);
        ledsLabel.setMaxWidth(Double.MAX_VALUE);
        ledsLabel.setFont(new Font("Arial",15));

        Label ledsPortLabel = new Label("Port:");
        ledsPortLabel.setAlignment(Pos.CENTER);
        ledsPortLabel.setMaxWidth(Double.MAX_VALUE);
        ledsPortLabel.setFont(new Font("Arial",12));

        Label seg7Label = new Label("Wyświetlacz 7-seg:");
        seg7Label.setAlignment(Pos.CENTER);
        seg7Label.setMaxWidth(Double.MAX_VALUE);
        seg7Label.setFont(new Font("Arial",15));

        Label seg7PortLabel = new Label("Port:");
        seg7PortLabel.setAlignment(Pos.CENTER);
        seg7PortLabel.setMaxWidth(Double.MAX_VALUE);
        seg7PortLabel.setFont(new Font("Arial",12));

        ComboBox<String> ledPortComboBox = new ComboBox<>();
        ledPortComboBox.getItems().addAll("P0","P1","P2","P3");
        ledPortComboBox.getSelectionModel().select(Main.stage.ledsPort);
        ledPortComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Main.stage.ledsPort = ledPortComboBox.getSelectionModel().getSelectedItem();
            }
        });

        ComboBox<String> seg7PortComboBox = new ComboBox<>();
        seg7PortComboBox.getItems().addAll("P0","P1","P2","P3");
        seg7PortComboBox.getSelectionModel().select(Main.stage.seg7displayPort);

        seg7PortComboBox.valueProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                Main.stage.seg7displayPort = seg7PortComboBox.getSelectionModel().getSelectedItem();
            }
        });

        mainGridPane.add(ledsLabel,0,0,2,2);
        mainGridPane.add(ledsPortLabel,0,2);
        mainGridPane.add(ledPortComboBox,1,2);
        mainGridPane.add(seg7Label,0,3,2,2);
        mainGridPane.add(seg7PortLabel,0,5);
        mainGridPane.add(seg7PortComboBox,1,5);

        Scene mainScene = new Scene(mainGridPane, 200, 180);
        mainStage.setScene(mainScene);
        mainStage.setTitle("Konfiguracja Panelu");
        mainStage.setResizable(false);
        //mainStage.initModality(Modality.APPLICATION_MODAL);
        mainStage.setResizable(false);
        // mainScene.getStylesheets().add(MainStage.class.getResource("css/style.css").toExternalForm());
        mainStage.show();
    }
}
