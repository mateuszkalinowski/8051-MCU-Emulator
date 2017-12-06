package stages;

import core.Main;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
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
        GridPane convertersGridPane = new GridPane();
        GridPane interruptsGridPane = new GridPane();
        ColumnConstraints column = new ColumnConstraints();
        column.setPercentWidth(100.0/10.0);
        for(int i = 0; i < 10 ; i ++) {
            mainGridPane.getColumnConstraints().add(column);
            convertersGridPane.getColumnConstraints().add(column);
            interruptsGridPane.getColumnConstraints().add(column);

        }
        RowConstraints row = new RowConstraints();
        row.setPercentHeight(100.0/7.0);
        for(int i = 0; i < 7;i++) {
            mainGridPane.getRowConstraints().add(row);
            convertersGridPane.getRowConstraints().add(row);
            interruptsGridPane.getRowConstraints().add(row);
        }

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

        Label adcLabel = new Label("Przetwornik ADC:");
        adcLabel.setAlignment(Pos.CENTER);
        adcLabel.setMaxWidth(Double.MAX_VALUE);
        adcLabel.setFont(new Font("Arial",15));

        Label adcPortLabel = new Label("Port:");
        adcPortLabel.setAlignment(Pos.CENTER);
        adcPortLabel.setMaxWidth(Double.MAX_VALUE);
        adcPortLabel.setFont(new Font("Arial",12));

        ComboBox<String> adcPortComboBox = new ComboBox<>();
        adcPortComboBox.getItems().addAll("P0","P1","P2");
        adcPortComboBox.setMaxWidth(Double.MAX_VALUE);
        adcPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikADCPort"));
        adcPortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.settingsMap.put("przetwornikADCPort",adcPortComboBox.getSelectionModel().getSelectedItem());
            Main.stage.drawFrame();
            Main.saveSettings();
        });

        Label rdAdcPortLabel = new Label("/RD:");
        rdAdcPortLabel.setAlignment(Pos.CENTER);
        rdAdcPortLabel.setMaxWidth(Double.MAX_VALUE);
        rdAdcPortLabel.setFont(new Font("Arial",12));

        ComboBox<String> rdAdcPortComboBox = new ComboBox<>();
        rdAdcPortComboBox.getItems().addAll("VCC","GND","P0.3","P1.3");
        rdAdcPortComboBox.setMaxWidth(Double.MAX_VALUE);
        rdAdcPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikADCRD"));
        rdAdcPortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.settingsMap.put("przetwornikADCRD",rdAdcPortComboBox.getSelectionModel().getSelectedItem());
            Main.stage.drawFrame();
            Main.saveSettings();
        });

        ComboBox<String> wrAdcPortComboBox = new ComboBox<>();
        wrAdcPortComboBox.getItems().addAll("VCC","GND","P0.4","P1.4");
        wrAdcPortComboBox.setMaxWidth(Double.MAX_VALUE);
        wrAdcPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikADCWR"));
        wrAdcPortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.settingsMap.put("przetwornikADCWR",wrAdcPortComboBox.getSelectionModel().getSelectedItem());
            Main.stage.drawFrame();
            Main.saveSettings();
        });

        ComboBox<String> csAdcPortComboBox = new ComboBox<>();
        csAdcPortComboBox.getItems().addAll("VCC","GND","P0.5","P1.5");
        csAdcPortComboBox.setMaxWidth(Double.MAX_VALUE);
        csAdcPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikADCCS"));
        csAdcPortComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
            Main.settingsMap.put("przetwornikADCCS",csAdcPortComboBox.getSelectionModel().getSelectedItem());
            Main.stage.drawFrame();
            Main.saveSettings();
        });



        Label wrAdcPortLabel = new Label("/WR:");
        wrAdcPortLabel.setAlignment(Pos.CENTER);
        wrAdcPortLabel.setMaxWidth(Double.MAX_VALUE);
        wrAdcPortLabel.setFont(new Font("Arial",12));

        Label csAdcPortLabel = new Label("/CS:");
        csAdcPortLabel.setAlignment(Pos.CENTER);
        csAdcPortLabel.setMaxWidth(Double.MAX_VALUE);
        csAdcPortLabel.setFont(new Font("Arial",12));





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

        Button resetToDefaultButton = new Button("Przywroć wartości domyślne");
        //resetToDefaultButton.setMaxWidth(Double.MAX_VALUE);
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
            adcPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikADCPort"));
            rdAdcPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikADCRD"));
            wrAdcPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikADCWR"));
            csAdcPortComboBox.getSelectionModel().select(Main.settingsMap.get("przetwornikADCCS"));


            Main.stage.drawFrame();
            Main.saveSettings();

        });

        mainGridPane.add(ledsLabel,0,0,4,2);
        mainGridPane.add(ledsPortLabel,0,2,2,1);
        mainGridPane.add(ledPortComboBox,2,2,2,1);
        mainGridPane.add(ledsCommonLabel,0,3,2,1);
        mainGridPane.add(ledCommonComboBox,2,3,2,1);
        mainGridPane.add(ledsColorLabel,0,4,2,1);
        mainGridPane.add(ledsColorPickerComboBox,2,4,2,1);

        mainGridPane.add(seg7Label,5,0,4,2);
        mainGridPane.add(seg7PortLabel,5,2,2,1);
        mainGridPane.add(seg7PortComboBox,7,2,2,1);
        mainGridPane.add(seg7TypeLabel,5,3,2,1);
        mainGridPane.add(seg7TypeComboBox,7,3,2,1);
        mainGridPane.add(seg7ColorLabel,5,4,2,1);
        mainGridPane.add(seg7ColorPickerComboBox,7,4,2,1);

        mainGridPane.add(externalInterruptsLabel,0,10,4,2);
        mainGridPane.add(przyciskiInterruptsLabel,0,13,2,1);
        mainGridPane.add(przyciskiPrzerwaniaComboBox,2,13,2,1);
        mainGridPane.add(zadajnikiInterruptsLabel,0,15,2,1);
        mainGridPane.add(zadajnikiPrzerwaniaComboBox,2,15,2,1);

        convertersGridPane.add(dacLabel,0,0,4,2);
        convertersGridPane.add(dacPortLabel,0,2,2,1);
        convertersGridPane.add(dacPortComboBox,2,2,2,1);
        convertersGridPane.add(wrPortLabel,0,3,2,1);
        convertersGridPane.add(wrPortComboBox,2,3,2,1);
        convertersGridPane.add(csPortLabel,0,4,2,1);
        convertersGridPane.add(csPortComboBox,2,4,2,1);

        convertersGridPane.add(adcLabel,5,0,4,2);
        convertersGridPane.add(adcPortLabel,5,2,2,1);
        convertersGridPane.add(adcPortComboBox,7,2,2,1);

        convertersGridPane.add(rdAdcPortLabel,5,3,2,1);
        convertersGridPane.add(rdAdcPortComboBox,7,3,2,1);
        convertersGridPane.add(wrAdcPortLabel,5,4,2,1);
        convertersGridPane.add(wrAdcPortComboBox,7,4,2,1);
        convertersGridPane.add(csAdcPortLabel,5,5,2,1);
        convertersGridPane.add(csAdcPortComboBox,7,5,2,1);

        interruptsGridPane.add(externalInterruptsLabel,0,0,4,2);
        interruptsGridPane.add(przyciskiInterruptsLabel,0,2,2,1);
        interruptsGridPane.add(przyciskiPrzerwaniaComboBox,2,2,2,1);
        interruptsGridPane.add(zadajnikiInterruptsLabel,0,3,2,1);
        interruptsGridPane.add(zadajnikiPrzerwaniaComboBox,2,3,2,1);


        TabPane mainTabPane = new TabPane();
        Tab generalSettingsTab = new Tab();
        generalSettingsTab.setClosable(false);
        generalSettingsTab.setText("Diody i Wyświetlacze");
        mainTabPane.getTabs().add(generalSettingsTab);
        generalSettingsTab.setContent(mainGridPane);

        Tab interrputsSettingsTab = new Tab();
        interrputsSettingsTab.setText("Przerwania i Timery");
        interrputsSettingsTab.setClosable(false);
        mainTabPane.getTabs().add(interrputsSettingsTab);
        interrputsSettingsTab.setContent(interruptsGridPane);

        Tab convertersSettingsTab = new Tab();
        convertersSettingsTab.setText("Przetworniki");
        convertersSettingsTab.setClosable(false);
        mainTabPane.getTabs().add(convertersSettingsTab);
        convertersSettingsTab.setContent(convertersGridPane);

        BorderPane mainBorderPane = new BorderPane();
        mainBorderPane.setCenter(mainTabPane);

        VBox lowVBox = new VBox();
        lowVBox.setPadding(new Insets(10,0,10,0));
        lowVBox.getChildren().add(resetToDefaultButton);
        VBox.setVgrow(resetToDefaultButton,Priority.ALWAYS);
        lowVBox.setAlignment(Pos.CENTER);
        mainBorderPane.setBottom(lowVBox);


        Scene mainScene = new Scene(mainBorderPane, 500, 300);
        mainStage.setScene(mainScene);
        mainStage.setTitle("Konfiguracja płytki prototypowej");
        mainStage.setResizable(false);
        mainStage.setResizable(false);
        mainStage.show();
    }
}
