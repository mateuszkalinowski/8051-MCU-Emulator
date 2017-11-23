package stages;

import core.Main;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
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
        column.setPercentWidth(10);
        for(int i = 0; i < 10 ; i ++)
            mainGridPane.getColumnConstraints().add(column);
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100.0/18.0);
        for(int i = 0; i < 18;i++)
            mainGridPane.getRowConstraints().addAll(row);

        //mainGridPane.setGridLinesVisible(true);

        Label ledsLabel = new Label("Diody:");
        ledsLabel.setAlignment(Pos.CENTER);
        ledsLabel.setMaxWidth(Double.MAX_VALUE);
        ledsLabel.setFont(new Font("Arial",15));

        Label ledsPortLabel = new Label("Port:");
        ledsPortLabel.setAlignment(Pos.CENTER);
        ledsPortLabel.setMaxWidth(Double.MAX_VALUE);
        ledsPortLabel.setFont(new Font("Arial",12));

        Label ledsCommonLabel = new Label("Wspólna:");
        ledsCommonLabel.setAlignment(Pos.CENTER);
        ledsCommonLabel.setMaxWidth(Double.MAX_VALUE);
        ledsCommonLabel.setFont(new Font("Arial",12));

        Label ledsColorLabel = new Label("Kolor:");
        ledsColorLabel.setAlignment(Pos.CENTER);
        ledsColorLabel.setMaxWidth(Double.MAX_VALUE);
        ledsColorLabel.setFont(new Font("Arial",12));

        ColorPicker ledsColorPicker = new ColorPicker();
        ledsColorPicker.setValue(Main.stage.ledsColor);
        ledsColorPicker.setMaxWidth(Double.MAX_VALUE);
        ledsColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.stage.ledsColor = ledsColorPicker.getValue();
                Main.stage.drawFrame();
            }
        });


        Label seg7Label = new Label("Wyświetlacz 7-seg:");
        seg7Label.setAlignment(Pos.CENTER);
        seg7Label.setMaxWidth(Double.MAX_VALUE);
        seg7Label.setFont(new Font("Arial",15));

        Label seg7PortLabel = new Label("Port:");
        seg7PortLabel.setAlignment(Pos.CENTER);
        seg7PortLabel.setMaxWidth(Double.MAX_VALUE);
        seg7PortLabel.setFont(new Font("Arial",12));

        Label seg7ColorLabel = new Label("Kolor:");
        seg7ColorLabel.setAlignment(Pos.CENTER);
        seg7ColorLabel.setMaxWidth(Double.MAX_VALUE);
        seg7ColorLabel.setFont(new Font("Arial",12));

        ComboBox<String> ledPortComboBox = new ComboBox<>();
        ledPortComboBox.getItems().addAll("P0","P1","P2","P3");
        ledPortComboBox.setMaxWidth(Double.MAX_VALUE);
        ledPortComboBox.getSelectionModel().select(Main.stage.ledsPort);
        ledPortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.stage.ledsPort = ledPortComboBox.getSelectionModel().getSelectedItem();
            Main.stage.drawFrame();
        });

        ComboBox<String> ledCommonComboBox = new ComboBox<>();
        ledCommonComboBox.getItems().addAll("Katoda","Anoda");
        ledCommonComboBox.setMaxWidth(Double.MAX_VALUE);
        ledCommonComboBox.getSelectionModel().select(Main.stage.ledsType);
        ledCommonComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.stage.ledsType = ledCommonComboBox.getSelectionModel().getSelectedItem();
            Main.stage.drawFrame();
        });


        ComboBox<String> seg7PortComboBox = new ComboBox<>();
        seg7PortComboBox.getItems().addAll("P0","P1","P2","P3");
        seg7PortComboBox.setMaxWidth(Double.MAX_VALUE);
        seg7PortComboBox.getSelectionModel().select(Main.stage.seg7displayPort);

        Label seg7TypeLabel = new Label("Podłączenie:");
        seg7TypeLabel.setAlignment(Pos.CENTER);
        seg7TypeLabel.setMaxWidth(Double.MAX_VALUE);
        seg7TypeLabel.setFont(new Font("Arial",12));

        ComboBox<String> seg7TypeComboBox = new ComboBox<>();
        seg7TypeComboBox.getItems().addAll("Konwertery bin-7seg","Bezpośrednio");
        seg7TypeComboBox.setMaxWidth(Double.MAX_VALUE);
        seg7TypeComboBox.getSelectionModel().select(Main.stage.seg7ConnectionType);
        seg7TypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.stage.seg7ConnectionType = seg7TypeComboBox.getSelectionModel().getSelectedIndex();
            Main.stage.drawFrame();
        });

        seg7PortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.stage.seg7displayPort = seg7PortComboBox.getSelectionModel().getSelectedItem();
            Main.stage.drawFrame();
        });

        ColorPicker seg7ColorPicker = new ColorPicker();
        seg7ColorPicker.setValue(Main.stage.seg7Color);
        seg7ColorPicker.setMaxWidth(Double.MAX_VALUE);
        seg7ColorPicker.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Main.stage.seg7Color = seg7ColorPicker.getValue();
                Main.stage.drawFrame();
            }
        });


        Label externalInterruptsLabel = new Label("Przerwania zewnętrzne:");
        externalInterruptsLabel.setAlignment(Pos.CENTER);
        externalInterruptsLabel.setMaxWidth(Double.MAX_VALUE);
        externalInterruptsLabel.setFont(new Font("Arial",15));

        Label przyciskiInterruptsLabel = new Label("Przyciski");
        przyciskiInterruptsLabel.setAlignment(Pos.CENTER);
        przyciskiInterruptsLabel.setMaxWidth(Double.MAX_VALUE);
        przyciskiInterruptsLabel.setFont(new Font("Arial",12));

        Label zadajnikiInterruptsLabel = new Label("Zadajniki");
        zadajnikiInterruptsLabel.setAlignment(Pos.CENTER);
        zadajnikiInterruptsLabel.setMaxWidth(Double.MAX_VALUE);
        zadajnikiInterruptsLabel.setFont(new Font("Arial",12));

        ComboBox<String> przyciskiPrzerwaniaComboBox = new ComboBox<>();
        przyciskiPrzerwaniaComboBox.getItems().addAll("-","P3.2","P3.3");
        przyciskiPrzerwaniaComboBox.setMaxWidth(Double.MAX_VALUE);
        przyciskiPrzerwaniaComboBox.getSelectionModel().select(Main.stage.przyciskiPrzerwania);
        przyciskiPrzerwaniaComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                Main.stage.przyciskiPrzerwania = przyciskiPrzerwaniaComboBox.getSelectionModel().getSelectedItem();
                Main.stage.drawFrame();
        });

        ComboBox<String> zadajnikiPrzerwaniaComboBox = new ComboBox<>();
        zadajnikiPrzerwaniaComboBox.getItems().addAll("-","P3.2","P3.3");
        zadajnikiPrzerwaniaComboBox.setMaxWidth(Double.MAX_VALUE);
        zadajnikiPrzerwaniaComboBox.getSelectionModel().select(Main.stage.zadajnikiPrzerwania);
        zadajnikiPrzerwaniaComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                 Main.stage.zadajnikiPrzerwania = zadajnikiPrzerwaniaComboBox.getSelectionModel().getSelectedItem();
                 Main.stage.drawFrame();
        });




        mainGridPane.add(ledsLabel,0,0,4,2);
        mainGridPane.add(ledsPortLabel,0,3,2,1);
        mainGridPane.add(ledPortComboBox,2,3,2,1);
        mainGridPane.add(ledsCommonLabel,0,5,2,1);
        mainGridPane.add(ledCommonComboBox,2,5,2,1);
        mainGridPane.add(ledsColorLabel,0,7,2,2);
        mainGridPane.add(ledsColorPicker,2,7,2,2);
        mainGridPane.add(seg7Label,5,0,4,2);
        mainGridPane.add(seg7PortLabel,5,3,2,1);
        mainGridPane.add(seg7PortComboBox,7,3,2,1);
        mainGridPane.add(seg7TypeLabel,5,5,2,1);
        mainGridPane.add(seg7TypeComboBox,7,5,2,1);
        mainGridPane.add(seg7ColorLabel,5,7,2,2);
        mainGridPane.add(seg7ColorPicker,7,7,2,2);

        mainGridPane.add(externalInterruptsLabel,0,10,4,2);
        mainGridPane.add(przyciskiInterruptsLabel,0,13,2,1);
        mainGridPane.add(przyciskiPrzerwaniaComboBox,2,13,2,1);
        mainGridPane.add(zadajnikiInterruptsLabel,0,15,2,1);
        mainGridPane.add(zadajnikiPrzerwaniaComboBox,2,15,2,1);


        Scene mainScene = new Scene(mainGridPane, 500, 300);
        mainStage.setScene(mainScene);
        mainStage.setTitle("Konfiguracja płytki prototypowej");
        mainStage.setResizable(false);
        mainStage.setResizable(false);
        mainStage.show();
    }
}
