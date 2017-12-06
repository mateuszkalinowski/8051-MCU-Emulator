package stages;

import core.Main;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import microcontroller.Dac;

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
        row.setPercentHeight(100.0/21.0);
        for(int i = 0; i < 21;i++)
            mainGridPane.getRowConstraints().add(row);

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

        ComboBox<String> ledsColorPickerComboBox = new ComboBox<>();
        ledsColorPickerComboBox.setValue(Main.settingsMap.get("ledsColor"));
        ledsColorPickerComboBox.setMaxWidth(Double.MAX_VALUE);
        ledsColorPickerComboBox.getItems().addAll("Czerwony","Zielony","Niebieski");
        ledsColorPickerComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            Main.settingsMap.put("ledsColor",ledsColorPickerComboBox.getSelectionModel().getSelectedItem());
            Main.stage.drawFrame();
            Main.saveSettings();
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
        ledPortComboBox.getItems().addAll("VCC","GND","P0","P1","P2","P3");
        ledPortComboBox.setMaxWidth(Double.MAX_VALUE);
        ledPortComboBox.getSelectionModel().select(Main.settingsMap.get("ledsPort"));
        ledPortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.settingsMap.put("ledsPort",ledPortComboBox.getSelectionModel().getSelectedItem());
            Main.stage.drawFrame();
            Main.saveSettings();
        });

        ComboBox<String> ledCommonComboBox = new ComboBox<>();
        ledCommonComboBox.getItems().addAll("Katoda","Anoda");
        ledCommonComboBox.setMaxWidth(Double.MAX_VALUE);
        ledCommonComboBox.getSelectionModel().select(Main.settingsMap.get("ledsType"));
        ledCommonComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.settingsMap.put("ledsType",ledCommonComboBox.getSelectionModel().getSelectedItem());
            Main.stage.drawFrame();
            Main.saveSettings();
        });


        ComboBox<String> seg7PortComboBox = new ComboBox<>();
        seg7PortComboBox.getItems().addAll("VCC","GND","P0","P1","P2","P3");
        seg7PortComboBox.setMaxWidth(Double.MAX_VALUE);
        seg7PortComboBox.getSelectionModel().select(Main.settingsMap.get("seg7DisplayPort"));

        Label seg7TypeLabel = new Label("Podłączenie:");
        seg7TypeLabel.setAlignment(Pos.CENTER);
        seg7TypeLabel.setMaxWidth(Double.MAX_VALUE);
        seg7TypeLabel.setFont(new Font("Arial",12));

        ComboBox<String> seg7TypeComboBox = new ComboBox<>();
        seg7TypeComboBox.getItems().addAll("Konwertery bin-7seg","Bezpośrednio");
        seg7TypeComboBox.setMaxWidth(Double.MAX_VALUE);
        seg7TypeComboBox.getSelectionModel().select(Integer.parseInt(Main.settingsMap.get("seg7ConnectionType")));
        seg7TypeComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {

            if(seg7TypeComboBox.getSelectionModel().getSelectedIndex()==0) {
                Main.settingsMap.put("seg7ConnectionType","0");
            }
            else {
                Main.settingsMap.put("seg7ConnectionType","1");
            }
            Main.stage.drawFrame();
            Main.saveSettings();
        });

        seg7PortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.settingsMap.put("seg7DisplayPort",seg7PortComboBox.getSelectionModel().getSelectedItem());
            Main.stage.drawFrame();
            Main.saveSettings();
        });

        ComboBox<String> seg7ColorPickerComboBox = new ComboBox<>();
        seg7ColorPickerComboBox.setValue(Main.settingsMap.get("seg7Color"));
        seg7ColorPickerComboBox.setMaxWidth(Double.MAX_VALUE);
        seg7ColorPickerComboBox.getItems().addAll("Czerwony","Zielony","Niebieski");
        seg7ColorPickerComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.settingsMap.put("seg7Color",seg7ColorPickerComboBox.getSelectionModel().getSelectedItem());
            Main.stage.drawFrame();
            Main.saveSettings();
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
        przyciskiPrzerwaniaComboBox.getSelectionModel().select(Main.settingsMap.get("przyciskiPrzerwania"));
        przyciskiPrzerwaniaComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.settingsMap.put("przyciskiPrzerwania",przyciskiPrzerwaniaComboBox.getSelectionModel().getSelectedItem());
            Main.stage.drawFrame();
            Main.saveSettings();
        });

        ComboBox<String> zadajnikiPrzerwaniaComboBox = new ComboBox<>();
        zadajnikiPrzerwaniaComboBox.getItems().addAll("-","P3.2","P3.3");
        zadajnikiPrzerwaniaComboBox.setMaxWidth(Double.MAX_VALUE);
        zadajnikiPrzerwaniaComboBox.getSelectionModel().select(Main.settingsMap.get("zadajnikiPrzerwania"));
        zadajnikiPrzerwaniaComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.settingsMap.put("zadajnikiPrzerwania",zadajnikiPrzerwaniaComboBox.getSelectionModel().getSelectedItem());
            Main.stage.drawFrame();
            Main.saveSettings();
        });

        Label dacLabel = new Label("Przetwornik DAC:");
        dacLabel.setAlignment(Pos.CENTER);
        dacLabel.setMaxWidth(Double.MAX_VALUE);
        dacLabel.setFont(new Font("Arial",15));

        Label dacPortLabel = new Label("Port:");
        dacPortLabel.setAlignment(Pos.CENTER);
        dacPortLabel.setMaxWidth(Double.MAX_VALUE);
        dacPortLabel.setFont(new Font("Arial",12));

        ComboBox<String> dacPortComboBox = new ComboBox<>();
        dacPortComboBox.getItems().addAll("VCC","GND","P0","P1");
        dacPortComboBox.setMaxWidth(Double.MAX_VALUE);
        dacPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikDACPort"));
        dacPortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.settingsMap.put("przetwornikDACPort",dacPortComboBox.getSelectionModel().getSelectedItem());
            Dac.convert();
            Main.stage.drawFrame();
            Main.saveSettings();
        });

        Label wrPortLabel = new Label("/WR:");
        wrPortLabel.setAlignment(Pos.CENTER);
        wrPortLabel.setMaxWidth(Double.MAX_VALUE);
        wrPortLabel.setFont(new Font("Arial",12));

        Label csPortLabel = new Label("/CS:");
        csPortLabel.setAlignment(Pos.CENTER);
        csPortLabel.setMaxWidth(Double.MAX_VALUE);
        csPortLabel.setFont(new Font("Arial",12));

        ComboBox<String> wrPortComboBox = new ComboBox<>();
        wrPortComboBox.getItems().addAll("VCC","GND","P0.6","P1.6");
        wrPortComboBox.setMaxWidth(Double.MAX_VALUE);
        wrPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikDACWR"));
        wrPortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.settingsMap.put("przetwornikDACWR",wrPortComboBox.getSelectionModel().getSelectedItem());
            Dac.convert();
            Main.stage.drawFrame();
            Main.saveSettings();
        });

        ComboBox<String> csPortComboBox = new ComboBox<>();
        csPortComboBox.getItems().addAll("VCC","GND","P0.7","P1.7");
        csPortComboBox.setMaxWidth(Double.MAX_VALUE);
        csPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikDACCS"));
        csPortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.settingsMap.put("przetwornikDACCS",csPortComboBox.getSelectionModel().getSelectedItem());
            Dac.convert();
            Main.stage.drawFrame();
            Main.saveSettings();
        });

        Label adcLabel = new Label("Przetwornik ADC:");
        adcLabel.setAlignment(Pos.CENTER);
        adcLabel.setMaxWidth(Double.MAX_VALUE);
        adcLabel.setFont(new Font("Arial",15));




        Button resetToDefaultButton = new Button("Przywroć wartości domyślne");
        resetToDefaultButton.setMaxWidth(Double.MAX_VALUE);
        resetToDefaultButton.setOnAction(event -> {

            Main.loadDefaultSettings();
            ledPortComboBox.getSelectionModel().select(Main.settingsMap.get("ledsPort"));
            ledCommonComboBox.getSelectionModel().select(Main.settingsMap.get("ledsType"));
            ledsColorPickerComboBox.getSelectionModel().select(Main.settingsMap.get("ledsColor"));
            seg7PortComboBox.getSelectionModel().select(Main.settingsMap.get("seg7DisplayPort"));
            seg7TypeComboBox.getSelectionModel().select(Integer.parseInt(Main.settingsMap.get("seg7ConnectionType")));
            seg7ColorPickerComboBox.getSelectionModel().select(Main.settingsMap.get("seg7Color"));
            przyciskiPrzerwaniaComboBox.getSelectionModel().select(Main.settingsMap.get("przyciskiPrzerwania"));
            zadajnikiPrzerwaniaComboBox.getSelectionModel().select(Main.settingsMap.get("zadajnikiPrzerwania"));
            dacPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikDACPort"));
            wrPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikDACWR"));
            csPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikDACCS"));
            Main.stage.drawFrame();
            Main.saveSettings();

        });

        mainGridPane.add(ledsLabel,0,0,4,2);
        mainGridPane.add(ledsPortLabel,0,3,2,1);
        mainGridPane.add(ledPortComboBox,2,3,2,1);
        mainGridPane.add(ledsCommonLabel,0,5,2,1);
        mainGridPane.add(ledCommonComboBox,2,5,2,1);
        mainGridPane.add(ledsColorLabel,0,7,2,1);
        mainGridPane.add(ledsColorPickerComboBox,2,7,2,1);
        mainGridPane.add(seg7Label,5,0,4,2);
        mainGridPane.add(seg7PortLabel,5,3,2,1);
        mainGridPane.add(seg7PortComboBox,7,3,2,1);
        mainGridPane.add(seg7TypeLabel,5,5,2,1);
        mainGridPane.add(seg7TypeComboBox,7,5,2,1);
        mainGridPane.add(seg7ColorLabel,5,7,2,1);
        mainGridPane.add(seg7ColorPickerComboBox,7,7,2,1);

        mainGridPane.add(externalInterruptsLabel,0,10,4,2);
        mainGridPane.add(przyciskiInterruptsLabel,0,13,2,1);
        mainGridPane.add(przyciskiPrzerwaniaComboBox,2,13,2,1);
        mainGridPane.add(zadajnikiInterruptsLabel,0,15,2,1);
        mainGridPane.add(zadajnikiPrzerwaniaComboBox,2,15,2,1);

        mainGridPane.add(dacLabel,5,10,4,2);
        mainGridPane.add(dacPortLabel,5,13,2,1);
        mainGridPane.add(dacPortComboBox,7,13,2,1);
        mainGridPane.add(wrPortLabel,5,15,2,1);
        mainGridPane.add(wrPortComboBox,7,15,2,1);
        mainGridPane.add(csPortLabel,5,17,2,1);
        mainGridPane.add(csPortComboBox,7,17,2,1);

       // mainGridPane.add(adcLabel,5,20,4,2);

        mainGridPane.add(resetToDefaultButton,3,19,4,1);

        TabPane mainTabPane = new TabPane();
        Tab generalSettingsTab = new Tab();
        generalSettingsTab.setText("Ogólne");
        mainTabPane.getTabs().add(generalSettingsTab);
       // generalSettingsTab.setContent(mainGridPane);


        Scene mainScene = new Scene(mainGridPane, 500, 320);
        mainStage.setScene(mainScene);
        mainStage.setTitle("Konfiguracja płytki prototypowej");
        mainStage.setResizable(false);
        mainStage.setResizable(false);
        mainStage.show();
    }
}
