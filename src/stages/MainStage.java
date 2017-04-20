package stages;

import core.Main;
import exceptions.CompilingException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import javax.swing.border.LineBorder;

/**
 * Created by Mateusz on 19.04.2017.
 * Project MkSim51
 */
public class MainStage extends Application {
    public void start(Stage primaryStage) {
        mainGridPane = new GridPane();
        mainBorderPane = new BorderPane();
        mainGridPane.setGridLinesVisible(true);
        RowConstraints upperRow = new RowConstraints();
        upperRow.setPercentHeight(62.5);
        RowConstraints lowerRow = new RowConstraints();
        lowerRow.setPercentHeight(37.5);
        ColumnConstraints onlyColumn = new ColumnConstraints();
        onlyColumn.setPercentWidth(100);
        mainGridPane.getColumnConstraints().add(onlyColumn);
        mainGridPane.getRowConstraints().add(upperRow);
        mainGridPane.getRowConstraints().add(lowerRow);

        editorElementsGridPane = new GridPane();
        editorElementsGridPane.setGridLinesVisible(true);
        RowConstraints rowInEditorElementsGridPane = new RowConstraints();
        rowInEditorElementsGridPane.setPercentHeight(100);
        editorElementsGridPane.getRowConstraints().add(rowInEditorElementsGridPane);
        ColumnConstraints editorColumn = new ColumnConstraints();
        editorColumn.setPercentWidth(70);
        ColumnConstraints elementsColumn = new ColumnConstraints();
        elementsColumn.setPercentWidth(30);
        editorElementsGridPane.getColumnConstraints().add(editorColumn);
        editorElementsGridPane.getColumnConstraints().add(elementsColumn);
        mainGridPane.add(editorElementsGridPane,0,0,2,1);

        TabPane simulatorTabPane = new TabPane();
        Tab mainSimulatorTab = new Tab();
        mainSimulatorTab.setClosable(false);
        mainSimulatorTab.setText("Symulator");
        simulatorTabPane.getTabs().add(mainSimulatorTab);
        mainGridPane.add(simulatorTabPane,0,1);

        editorTextArea = new TextArea();
        editorElementsGridPane.add(editorTextArea,0,0);

        simulatorGridPane = new GridPane();
        //simulatorGridPane.setGridLinesVisible(true);
        RowConstraints rowInSimulator = new RowConstraints();
        rowInSimulator.setPercentHeight(10);
        for(int i = 0; i < 10; i ++) {
            simulatorGridPane.getRowConstraints().add(rowInSimulator);
        }
        ColumnConstraints columnInSimulator = new ColumnConstraints();
        columnInSimulator.setPercentWidth(5);
        for(int i = 0; i < 20; i++) {
            simulatorGridPane.getColumnConstraints().add(columnInSimulator);
        }
        mainSimulatorTab.setContent(simulatorGridPane);
        //KOMPONENTY PANELU SYMULATORA

        //AKUMULATOR

        accumulatorLabel = new Label("ACC");
        accumulatorLabel.setMaxWidth(Double.MAX_VALUE);
        accumulatorLabel.setAlignment(Pos.CENTER);
        accumulatorLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(accumulatorLabel,10,7);

        accumulatorTextField = new Label("00h");
        accumulatorTextField.setMaxWidth(Double.MAX_VALUE);
        accumulatorTextField.setAlignment(Pos.CENTER);
        accumulatorTextField.setFont(new Font("Arial",11));
        accumulatorTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(accumulatorTextField,10,8);

        r0Label = new Label("R0");
        r0Label.setMaxWidth(Double.MAX_VALUE);
        r0Label.setAlignment(Pos.CENTER);
        r0Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(r0Label,1,7);

        r0TextField = new Label("00h");
        r0TextField.setMaxWidth(Double.MAX_VALUE);
        r0TextField.setAlignment(Pos.CENTER);
        r0TextField.setFont(new Font("Arial",11));
        r0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r0TextField,1,8);

        r1Label = new Label("R1");
        r1Label.setMaxWidth(Double.MAX_VALUE);
        r1Label.setAlignment(Pos.CENTER);
        r1Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(r1Label,2,7);

        r1TextField = new Label("00h");
        r1TextField.setMaxWidth(Double.MAX_VALUE);
        r1TextField.setAlignment(Pos.CENTER);
        r1TextField.setFont(new Font("Arial",11));
        r1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r1TextField,2,8);

        r2Label = new Label("R2");
        r2Label.setMaxWidth(Double.MAX_VALUE);
        r2Label.setAlignment(Pos.CENTER);
        r2Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(r2Label,3,7);

        r2TextField = new Label("00h");
        r2TextField.setMaxWidth(Double.MAX_VALUE);
        r2TextField.setAlignment(Pos.CENTER);
        r2TextField.setFont(new Font("Arial",11));
        r2TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r2TextField,3,8);

        r3Label = new Label("R3");
        r3Label.setMaxWidth(Double.MAX_VALUE);
        r3Label.setAlignment(Pos.CENTER);
        r3Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(r3Label,4,7);

        r3TextField = new Label("00h");
        r3TextField.setMaxWidth(Double.MAX_VALUE);
        r3TextField.setAlignment(Pos.CENTER);
        r3TextField.setFont(new Font("Arial",11));
        r3TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r3TextField,4,8);

        r4Label = new Label("R4");
        r4Label.setMaxWidth(Double.MAX_VALUE);
        r4Label.setAlignment(Pos.CENTER);
        r4Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(r4Label,5,7);

        r4TextField = new Label("00h");
        r4TextField.setMaxWidth(Double.MAX_VALUE);
        r4TextField.setAlignment(Pos.CENTER);
        r4TextField.setFont(new Font("Arial",11));
        r4TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r4TextField,5,8);

        r5Label = new Label("R5");
        r5Label.setMaxWidth(Double.MAX_VALUE);
        r5Label.setAlignment(Pos.CENTER);
        r5Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(r5Label,6,7);

        r5TextField = new Label("00h");
        r5TextField.setMaxWidth(Double.MAX_VALUE);
        r5TextField.setAlignment(Pos.CENTER);
        r5TextField.setFont(new Font("Arial",11));
        r5TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r5TextField,6,8);

        r6Label = new Label("R6");
        r6Label.setMaxWidth(Double.MAX_VALUE);
        r6Label.setAlignment(Pos.CENTER);
        r6Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(r6Label,7,7);

        r6TextField = new Label("00h");
        r6TextField.setMaxWidth(Double.MAX_VALUE);
        r6TextField.setAlignment(Pos.CENTER);
        r6TextField.setFont(new Font("Arial",11));
        r6TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r6TextField,7,8);

        r7Label = new Label("R7");
        r7Label.setMaxWidth(Double.MAX_VALUE);
        r7Label.setAlignment(Pos.CENTER);
        r7Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(r7Label,8,7);

        r7TextField = new Label("00h");
        r7TextField.setMaxWidth(Double.MAX_VALUE);
        r7TextField.setAlignment(Pos.CENTER);
        r7TextField.setFont(new Font("Arial",11));
        r7TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r7TextField,8,8);

        p0Label = new Label("P0");
        p0Label.setMaxWidth(Double.MAX_VALUE);
        p0Label.setAlignment(Pos.CENTER);
        p0Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(p0Label,13,1);

        p0TextField = new Label("11111111");
        p0TextField.setMaxWidth(Double.MAX_VALUE);
        p0TextField.setAlignment(Pos.CENTER);
        p0TextField.setFont(new Font("Arial",11));
        p0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(p0TextField,14,1,2,1);

        p1Label = new Label("P1");
        p1Label.setMaxWidth(Double.MAX_VALUE);
        p1Label.setAlignment(Pos.CENTER);
        p1Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(p1Label,13,3);

        p1TextField = new Label("11111111");
        p1TextField.setMaxWidth(Double.MAX_VALUE);
        p1TextField.setAlignment(Pos.CENTER);
        p1TextField.setFont(new Font("Arial",11));
        p1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(p1TextField,14,3,2,1);

        p2Label = new Label("P2");
        p2Label.setMaxWidth(Double.MAX_VALUE);
        p2Label.setAlignment(Pos.CENTER);
        p2Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(p2Label,13,5);

        p2TextField = new Label("11111111");
        p2TextField.setMaxWidth(Double.MAX_VALUE);
        p2TextField.setAlignment(Pos.CENTER);
        p2TextField.setFont(new Font("Arial",11));
        p2TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(p2TextField,14,5,2,1);

        p3Label = new Label("P3");
        p3Label.setMaxWidth(Double.MAX_VALUE);
        p3Label.setAlignment(Pos.CENTER);
        p3Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(p3Label,13,7);

        p3TextField = new Label("11111111");
        p3TextField.setMaxWidth(Double.MAX_VALUE);
        p3TextField.setAlignment(Pos.CENTER);
        p3TextField.setFont(new Font("Arial",11));
        p3TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(p3TextField,14,7,2,1);

        translateToMemoryButton = new Button("Uruchom");
        translateToMemoryButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                lines = editorTextArea.getText().split("\n");
                try {
                    Main.cpu.codeMemory.setMemory(lines);
                    Main.cpu.resetCpu();
                    Main.cpu.refreshGui();
                    translateToMemoryButton.setDisable(true);
                    stopSimulationButton.setDisable(false);
                    oneStepButton.setDisable(false);
                    editorTextArea.setEditable(false);
                }
                catch (CompilingException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd Kompilacji");
                    alert.setHeaderText("Kompilacja przebiegła nieudanie");
                    alert.setContentText("Sprawdź kod jeszcze raz, informacja gdzie wystąpił błąd zostanie" +
                            "dodana w jednej z kolejnych wersji programu");
                    alert.showAndWait();
                }
            }
        });

        stopSimulationButton = new Button("Zatrzymaj");
        stopSimulationButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                translateToMemoryButton.setDisable(false);
                stopSimulationButton.setDisable(true);
                oneStepButton.setDisable(true);
                editorTextArea.setEditable(true);
                editorTextArea.setText("");
                String textToSet = "";
                for(String line : lines) {
                    textToSet = textToSet + line + "\n";
                }
                editorTextArea.setText(textToSet.substring(0,textToSet.length()-1));
                Main.cpu.resetCpu();
                Main.cpu.refreshGui();
            }
        });

        oneStepButton = new Button("Krok");
        oneStepButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    Main.cpu.executeInstruction();
                }
                catch (Exception e){
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Błąd Wykonania");
                    alert.setHeaderText("Wykonanie przebiegło nieudanie");
                    alert.setContentText("Sprawdź kod jeszcze raz, informacja gdzie wystąpił błąd zostanie" +
                            "dodana w jednej z kolejnych wersji programu");
                    alert.showAndWait();
                }
            }
        });

        translateToMemoryButton.setPrefWidth(Double.MAX_VALUE);
        stopSimulationButton.setPrefWidth(Double.MAX_VALUE);
        stopSimulationButton.setDisable(true);
        oneStepButton.setPrefWidth(Double.MAX_VALUE);
        oneStepButton.setDisable(true);
        simulatorGridPane.add(translateToMemoryButton,17,1,2,2);
        simulatorGridPane.add(stopSimulationButton,17,3,2,2);
        simulatorGridPane.add(oneStepButton,17,5,2,2);
        MenuBar mainMenuBar = new MenuBar();
        mainBorderPane.setTop(mainMenuBar);

        Menu menuFile = new Menu("Plik");
        Menu editorFile = new Menu("Edytor");
        mainMenuBar.getMenus().add(menuFile);
        mainMenuBar.getMenus().add(editorFile);


        MenuItem exitMenuItem = new MenuItem("Zamknij");
        exitMenuItem.setOnAction(event -> System.exit(0));
        exitMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        menuFile.getItems().add(exitMenuItem);

        MenuItem importFileMenuItem = new MenuItem("Importuj kod");
        importFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO
            }
        });
        MenuItem exportFileMenuItem = new MenuItem("Eksportuj kod");
        exportFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO
            }
        });
        editorFile.getItems().addAll(importFileMenuItem,exportFileMenuItem);

        mainStage = primaryStage;
        mainStage.setTitle("8051 MCU Emulator");
        mainBorderPane.setCenter(mainGridPane);
        mainScene = new Scene(mainBorderPane,width,height);
        mainStage.setScene(mainScene);
        mainStage.show();
        Main.cpu.refreshGui();
    }

    public MainStage(double width,double height) {
        this.height = height;
        this.width = width;
    }

    public void setEditorText(String text) {
        editorTextArea.setText(text);
    }

    GridPane mainGridPane;
    GridPane editorElementsGridPane;
    GridPane simulatorGridPane;

    BorderPane mainBorderPane;

    double height;
    double width;

    public Label accumulatorLabel;
    public Label accumulatorTextField;

    public Label r0Label;
    public Label r0TextField;

    public Label r1Label;
    public Label r1TextField;

    public Label r2Label;
    public Label r2TextField;

    public Label r3Label;
    public Label r3TextField;

    public Label r4Label;
    public Label r4TextField;

    public Label r5Label;
    public Label r5TextField;

    public Label r6Label;
    public Label r6TextField;

    public Label r7Label;
    public Label r7TextField;

    public Label p0Label;
    public Label p0TextField;

    public Label p1Label;
    public Label p1TextField;

    public Label p2Label;
    public Label p2TextField;

    public Label p3Label;
    public Label p3TextField;

    private Button translateToMemoryButton;
    private Button stopSimulationButton;
    private Button oneStepButton;

    private TextArea editorTextArea;

    Stage mainStage;
    Scene mainScene;

    private String lines[];
}
