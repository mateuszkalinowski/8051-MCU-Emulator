package stages;

import core.Main;
import exceptions.CompilingException;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * Created by Mateusz on 19.04.2017.
 * Project MkSim51
 */
public class MainStage extends Application {
    public void start(Stage primaryStage) {
        mainGridPane = new GridPane();
        mainBorderPane = new BorderPane();
        mainGridPane.setGridLinesVisible(false);
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
        editorElementsGridPane.setGridLinesVisible(false);
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


        GridPane infoEditorAndButtonGridPane = new GridPane();
        ColumnConstraints columnFullSize = new ColumnConstraints();
        columnFullSize.setPercentWidth(100);
        infoEditorAndButtonGridPane.getColumnConstraints().add(columnFullSize);
        RowConstraints rowTenPercent = new RowConstraints();
        rowTenPercent.setPercentHeight(5);
        for(int i = 0; i < 20;i++)
            infoEditorAndButtonGridPane.getRowConstraints().add(rowTenPercent);


        compilationErrorsLabel = new Label("");
        compilationErrorsLabel.setPadding(new Insets(0,22,0,22));
        compilationErrorsLabel.setMaxWidth(Double.MAX_VALUE);
        compilationErrorsLabel.setAlignment(Pos.CENTER_LEFT);
        compilationErrorsLabel.setFont(new Font("Arial",11));
        compilationErrorsLabel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-background-insets: 0 20 0 20");
        infoEditorAndButtonGridPane.add(compilationErrorsLabel,0,0,1,1);

        editorTextArea = new TextArea();
        infoEditorAndButtonGridPane.add(editorTextArea,0,1,1,17);
        editorElementsGridPane.add(infoEditorAndButtonGridPane,0,0);

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
        //simulatorGridPane.setGridLinesVisible(true);
        //KOMPONENTY PANELU SYMULATORA

        //AKUMULATOR

        accumulatorLabel = new Label("ACC");
        accumulatorLabel.setMaxWidth(Double.MAX_VALUE);
        accumulatorLabel.setAlignment(Pos.CENTER);
        accumulatorLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(accumulatorLabel,11,5,2,1);

        accumulatorTextFieldHex = new Label("00h");
        accumulatorTextFieldHex.setMaxWidth(Double.MAX_VALUE);
        accumulatorTextFieldHex.setAlignment(Pos.CENTER);
        accumulatorTextFieldHex.setFont(new Font("Arial",11));
        accumulatorTextFieldHex.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(accumulatorTextFieldHex,10,6);

        accumulatorTextFieldBin = new Label("00000000b");
        accumulatorTextFieldBin.setMaxWidth(Double.MAX_VALUE);
        accumulatorTextFieldBin.setAlignment(Pos.CENTER);
        accumulatorTextFieldBin.setFont(new Font("Arial",11));
        accumulatorTextFieldBin.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(accumulatorTextFieldBin,11,6,2,1);

        accumulatorTextFieldDec = new Label("000d");
        accumulatorTextFieldDec.setMaxWidth(Double.MAX_VALUE);
        accumulatorTextFieldDec.setAlignment(Pos.CENTER);
        accumulatorTextFieldDec.setFont(new Font("Arial",11));
        accumulatorTextFieldDec.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(accumulatorTextFieldDec,13,6);

        bLabel = new Label("B");
        bLabel.setMaxWidth(Double.MAX_VALUE);
        bLabel.setAlignment(Pos.CENTER);
        bLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(bLabel,11,7,2,1);

        bTextFieldHex = new Label("00h");
        bTextFieldHex.setMaxWidth(Double.MAX_VALUE);
        bTextFieldHex.setAlignment(Pos.CENTER);
        bTextFieldHex.setFont(new Font("Arial",11));
        bTextFieldHex.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(bTextFieldHex,10,8);

        bTextFieldBin = new Label("00h");
        bTextFieldBin.setMaxWidth(Double.MAX_VALUE);
        bTextFieldBin.setAlignment(Pos.CENTER);
        bTextFieldBin.setFont(new Font("Arial",11));
        bTextFieldBin.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(bTextFieldBin,11,8,2,1);

        bTextFieldDec = new Label("000d");
        bTextFieldDec.setMaxWidth(Double.MAX_VALUE);
        bTextFieldDec.setAlignment(Pos.CENTER);
        bTextFieldDec.setFont(new Font("Arial",11));
        bTextFieldDec.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(bTextFieldDec,13,8);

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

        cLabel = new Label("C");
        cLabel.setMaxWidth(Double.MAX_VALUE);
        cLabel.setAlignment(Pos.CENTER);
        cLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(cLabel,2,5);

        cTextField = new Label("00h");
        cTextField.setMaxWidth(Double.MAX_VALUE);
        cTextField.setAlignment(Pos.CENTER);
        cTextField.setFont(new Font("Arial",11));
        cTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(cTextField,2,6);

        acLabel = new Label("AC");
        acLabel.setMaxWidth(Double.MAX_VALUE);
        acLabel.setAlignment(Pos.CENTER);
        acLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(acLabel,3,5);

        acTextField = new Label("00h");
        acTextField.setMaxWidth(Double.MAX_VALUE);
        acTextField.setAlignment(Pos.CENTER);
        acTextField.setFont(new Font("Arial",11));
        acTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(acTextField,3,6);

        f0Label = new Label("F0");
        f0Label.setMaxWidth(Double.MAX_VALUE);
        f0Label.setAlignment(Pos.CENTER);
        f0Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(f0Label,4,5);

        f0TextField = new Label("00h");
        f0TextField.setMaxWidth(Double.MAX_VALUE);
        f0TextField.setAlignment(Pos.CENTER);
        f0TextField.setFont(new Font("Arial",11));
        f0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(f0TextField,4,6);

        rs1Label = new Label("RS1");
        rs1Label.setMaxWidth(Double.MAX_VALUE);
        rs1Label.setAlignment(Pos.CENTER);
        rs1Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(rs1Label,5,5);

        rs1TextField = new Label("00h");
        rs1TextField.setMaxWidth(Double.MAX_VALUE);
        rs1TextField.setAlignment(Pos.CENTER);
        rs1TextField.setFont(new Font("Arial",11));
        rs1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(rs1TextField,5,6);

        rs0Label = new Label("RS0");
        rs0Label.setMaxWidth(Double.MAX_VALUE);
        rs0Label.setAlignment(Pos.CENTER);
        rs0Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(rs0Label,6,5);

        rs0TextField = new Label("00h");
        rs0TextField.setMaxWidth(Double.MAX_VALUE);
        rs0TextField.setAlignment(Pos.CENTER);
        rs0TextField.setFont(new Font("Arial",11));
        rs0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(rs0TextField,6,6);

        ovLabel = new Label("OV");
        ovLabel.setMaxWidth(Double.MAX_VALUE);
        ovLabel.setAlignment(Pos.CENTER);
        ovLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(ovLabel,7,5);

        ovTextField = new Label("00h");
        ovTextField.setMaxWidth(Double.MAX_VALUE);
        ovTextField.setAlignment(Pos.CENTER);
        ovTextField.setFont(new Font("Arial",11));
        ovTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(ovTextField,7,6);

        pLabel = new Label("P");
        pLabel.setMaxWidth(Double.MAX_VALUE);
        pLabel.setAlignment(Pos.CENTER);
        pLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(pLabel,8,5);

        pTextField = new Label("00h");
        pTextField.setMaxWidth(Double.MAX_VALUE);
        pTextField.setAlignment(Pos.CENTER);
        pTextField.setFont(new Font("Arial",11));
        pTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(pTextField,8,6);

        p0Label = new Label("P0");
        p0Label.setMaxWidth(Double.MAX_VALUE);
        p0Label.setAlignment(Pos.CENTER);
        p0Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(p0Label,16,2);

        p0TextField = new Label("11111111");
        p0TextField.setMaxWidth(Double.MAX_VALUE);
        p0TextField.setAlignment(Pos.CENTER);
        p0TextField.setFont(new Font("Arial",11));
        p0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(p0TextField,17,2,2,1);

        p1Label = new Label("P1");
        p1Label.setMaxWidth(Double.MAX_VALUE);
        p1Label.setAlignment(Pos.CENTER);
        p1Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(p1Label,16,4);

        p1TextField = new Label("11111111");
        p1TextField.setMaxWidth(Double.MAX_VALUE);
        p1TextField.setAlignment(Pos.CENTER);
        p1TextField.setFont(new Font("Arial",11));
        p1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(p1TextField,17,4,2,1);

        p2Label = new Label("P2");
        p2Label.setMaxWidth(Double.MAX_VALUE);
        p2Label.setAlignment(Pos.CENTER);
        p2Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(p2Label,16,6);

        p2TextField = new Label("11111111");
        p2TextField.setMaxWidth(Double.MAX_VALUE);
        p2TextField.setAlignment(Pos.CENTER);
        p2TextField.setFont(new Font("Arial",11));
        p2TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(p2TextField,17,6,2,1);

        p3Label = new Label("P3");
        p3Label.setMaxWidth(Double.MAX_VALUE);
        p3Label.setAlignment(Pos.CENTER);
        p3Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(p3Label,16,8);

        p3TextField = new Label("11111111");
        p3TextField.setMaxWidth(Double.MAX_VALUE);
        p3TextField.setAlignment(Pos.CENTER);
        p3TextField.setFont(new Font("Arial",11));
        p3TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(p3TextField,17,8,2,1);

        Label portsDesc = new Label("Porty We/Wy:");
        portsDesc.setMaxWidth(Double.MAX_VALUE);
        portsDesc.setAlignment(Pos.CENTER);
        portsDesc.setFont(new Font("Arial",11));
        simulatorGridPane.add(portsDesc,16,0,3,2);

        Label pswLabel = new Label("REG\nPSW:");
        pswLabel.setMaxWidth(Double.MAX_VALUE);
        pswLabel.setAlignment(Pos.CENTER);
        pswLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(pswLabel,1,5,1,2);

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
                    rysujRunButton.setDisable(true);
                    running = true;
                }
                catch (CompilingException e) {
                    Main.stage.compilationErrorsLabel.setText("Błąd: " + e.getMessage());
                    Main.stage.compilationErrorsLabel.setStyle("-fx-background-color: red; -fx-background-radius: 10; -fx-background-insets: 0 20 0 20");

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
                rysujRunButton.setDisable(false);
                running = false;
                String textToSet = "";
                for(String line : lines) {
                    textToSet = textToSet + line + "\n";
                }
                editorTextArea.setText(textToSet.substring(0,textToSet.length()-1));
                Main.cpu.resetCpu();
                Main.cpu.refreshGui();
                compilationErrorsLabel.setText("");
                compilationErrorsLabel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-background-insets: 0 20 0 20");
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


        translateToMemoryButton.setMaxWidth(Double.MAX_VALUE);
        stopSimulationButton.setMaxWidth(Double.MAX_VALUE);
        stopSimulationButton.setDisable(true);
        oneStepButton.setMaxWidth(Double.MAX_VALUE);
        oneStepButton.setDisable(true);


        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(0,10,0,10));
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(translateToMemoryButton,Priority.ALWAYS);
        HBox.setHgrow(stopSimulationButton,Priority.ALWAYS);
        HBox.setHgrow(oneStepButton,Priority.ALWAYS);
        buttonBox.setSpacing(5);

        buttonBox.getChildren().addAll(translateToMemoryButton,stopSimulationButton,oneStepButton);
        infoEditorAndButtonGridPane.add(buttonBox,0,18,1,2);

        MenuBar mainMenuBar = new MenuBar();
        mainBorderPane.setTop(mainMenuBar);

        Menu menuFile = new Menu("Plik");
        mainMenuBar.getMenus().add(menuFile);


        MenuItem exitMenuItem = new MenuItem("Zamknij");
        exitMenuItem.setOnAction(event -> System.exit(0));
        exitMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));

        MenuItem importFileMenuItem = new MenuItem("Otwórz");
        importFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if(!running) {
                    FileChooser chooseFile = new FileChooser();
                    chooseFile.setTitle("Wybierz plik z posiadanymi składnikami");
                    File openFile = chooseFile.showOpenDialog(primaryStage);
                    if (openFile != null) {
                        String textToSet = "";
                        try {
                            Scanner in = new Scanner(openFile);
                            while (in.hasNextLine())
                                textToSet += in.nextLine() + "\n";
                        } catch (FileNotFoundException ignored) {
                        }
                        editorTextArea.setText(textToSet.substring(0, textToSet.length() - 1));
                    }
                }
                else {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Nie można otworzyć pliku");
                    alert.setHeaderText("Aby wczytać plik zatrzymaj symulacja");
                    alert.setContentText("Nie można wczytywać plików podczas pracy emulatora.");
                    alert.showAndWait();
                }
            }

        });
        MenuItem exportFileMenuItem = new MenuItem("Zapisz jako");
        exportFileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser chooseFile = new FileChooser();
                chooseFile.setTitle("Wybierz lokalizację zapisu");
                chooseFile.setInitialFileName("file.txt");
                chooseFile.setInitialDirectory(new File(System.getProperty("user.home")));
                File saveFile = chooseFile.showSaveDialog(primaryStage);
                if(saveFile!=null) {
                    try {
                        PrintWriter in = new PrintWriter(saveFile);
                        String text = editorTextArea.getText();
                        in.println(text);
                        in.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("File error");
                    }
                }
            }
        });
        menuFile.getItems().addAll(importFileMenuItem,exportFileMenuItem,new SeparatorMenuItem(),exitMenuItem);


        //PRAWY OBSZAR
        TabPane elementsTabPane = new TabPane();
        Tab diodesPane = new Tab("Panel");
        diodesPane.setClosable(false);
        Tab chartPane = new Tab("Przebieg");
        chartPane.setClosable(false);
        elementsTabPane.getTabs().add(diodesPane);
        elementsTabPane.getTabs().add(chartPane);


        diodesPaneGridPane = new GridPane();
        ColumnConstraints columnInDiodesPane = new ColumnConstraints();
        columnInDiodesPane.setPercentWidth(100/5);
        for (int i = 0; i < 5;i++) {
            diodesPaneGridPane.getColumnConstraints().add(columnInDiodesPane);
        }
        RowConstraints rowInDiodesPane = new RowConstraints();
        rowInDiodesPane.setPercentHeight(10);
        for(int i = 0; i < 10; i++)
            diodesPaneGridPane.getRowConstraints().add(rowInDiodesPane);



        portButton0 = new ToggleButton("0");
        portButton1 = new ToggleButton("1");
        portButton2 = new ToggleButton("2");
        portButton3 = new ToggleButton("3");
        portButton4 = new ToggleButton("4");
        portButton5 = new ToggleButton("5");
        portButton6 = new ToggleButton("6");
        portButton7 = new ToggleButton("7");

        HBox userButtonsHBox = new HBox();
        userButtonsHBox.setPadding(new Insets(1,2,1,2));
        userButtonsHBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(portButton0,Priority.ALWAYS);
        HBox.setHgrow(portButton1,Priority.ALWAYS);
        HBox.setHgrow(portButton2,Priority.ALWAYS);
        HBox.setHgrow(portButton3,Priority.ALWAYS);
        HBox.setHgrow(portButton4,Priority.ALWAYS);
        HBox.setHgrow(portButton5,Priority.ALWAYS);
        HBox.setHgrow(portButton6,Priority.ALWAYS);
        HBox.setHgrow(portButton7,Priority.ALWAYS);
        userButtonsHBox.setSpacing(1);

        userButtonsHBox.getChildren().addAll(portButton7,portButton6,portButton5,portButton4,portButton3,portButton2,portButton1,portButton0);
        diodesPaneGridPane.add(ledCanvas,0,0,8,2);
        diodesPaneGridPane.add(userButtonsHBox,0,8,8,2);
        diodesPaneGridPane.setGridLinesVisible(false);
        diodesPane.setContent(diodesPaneGridPane);

        final NumberAxis xAxis = new NumberAxis(0,255,64);
        final NumberAxis yAxis = new NumberAxis(0,255,64);
        BorderPane chartBorderPane = new BorderPane();
        //creating the chart
        final ScatterChart<Number,Number> lineChart =
                new ScatterChart<Number,Number>(xAxis,yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setTitle("Przebieg - p1(p0)");
        XYChart.Series series = new XYChart.Series();
        lineChart.getData().add(series);
        chartPane.setContent(chartBorderPane);

        chartBorderPane.setCenter(lineChart);

        chartPane.setContent(chartBorderPane);


        editorElementsGridPane.add(elementsTabPane,1,0);

        rysujRunButton = new Button("Generuj Przebieg");
        rysujRunButton.setDisable(false);
        rysujRunButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                lines = editorTextArea.getText().split("\n");
                try {
                    Main.cpu.codeMemory.setMemory(lines);
                    Main.cpu.resetCpu();
                    Main.cpu.refreshGui();
                }
                catch (CompilingException e) {
                    Main.stage.compilationErrorsLabel.setText("Błąd: " + e.getMessage());
                    Main.stage.compilationErrorsLabel.setStyle("-fx-background-color: red; -fx-background-radius: 10; -fx-background-insets: 0 20 0 20");
                    return;

                }
                series.getData().clear();
                Main.cpu.resetCounter();
                for(int i = 0; i < 255;i++){
                    int counter = 0;
                    //Main.cpu.ports.put("P0",i);
                    Main.cpu.mainMemory.put("P0",i);
                    while(true) {
                        counter++;
                        try {
                            Main.cpu.executeInstruction();
                        }
                        catch (Exception e) {

                        }
                        if(Main.cpu.mainMemory.get("P3")==0) {
                            int wartoscp1 = Main.cpu.mainMemory.get("P1");
                            series.getData().add(new XYChart.Data(i,wartoscp1));
                            Main.cpu.resetCpu();
                            break;
                        }
                        if(counter>100) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Błąd Generacji Przebiegu");
                            alert.setHeaderText("Nie wykryto stanu aktywującego zapis przebiegu");
                            alert.setContentText("Aby rejestrator przebiegów zapisał parę (P0,P1) ustaw wartość" +
                                    " '00h' na porcie trzecim. Rejestrator podaje na p0 kolejne wartości i czeka na ten " +
                                    "stan, aby zapisać pomiar.");
                            alert.showAndWait();
                            return;
                        }
                    }
                }
            }
        });

        HBox generateButtonBox = new HBox();
        generateButtonBox.setPadding(new Insets(10,10,10,10));
        generateButtonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(rysujRunButton,Priority.ALWAYS);
        generateButtonBox.setSpacing(5);

        generateButtonBox.getChildren().addAll(rysujRunButton);

        chartBorderPane.setBottom(generateButtonBox);

        mainStage = primaryStage;
        mainStage.setTitle("8051 MCU Emulator - 0.2 Alpha");
        mainBorderPane.setCenter(mainGridPane);
        mainScene = new Scene(mainBorderPane,width,height);
        mainScene.getStylesheets().add(MainStage.class.getResource("style.css").toExternalForm());
        mainStage.setScene(mainScene);
        mainStage.show();
        mainStage.setMinHeight(600);
        mainStage.setMinWidth(800);
        Main.cpu.refreshGui();

        mainStage.fullScreenProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                drawFrame();
            }
        });

        mainScene.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                drawFrame();
            }
        });
        mainScene.widthProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                drawFrame();
            }
        });
        drawFrame();
    }


    public MainStage(double width,double height) {
        this.height = height;
        this.width = width;
    }

    public void drawFrame(){
        double width = mainStage.getWidth() * 3.0/10.0;
        double height = mainScene.getHeight()*1.0/10.0;

        ledCanvas.setWidth(width);
        ledCanvas.setHeight(height);

        double oneLedWidth = width/8.0;

        GraphicsContext gc = ledCanvas.getGraphicsContext2D();
        gc.clearRect(0,0,width,height);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(0,0,width,height);
        gc.setFill(Color.RED);

        for(int i = 0; i<8;i++) {
            String portName = "P0." + (7 - i);
            if(Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get(portName)))
                gc.setFill(Color.RED);
            else
                gc.setFill(Color.BLACK);

            double centerX = i*oneLedWidth+oneLedWidth/2.0;
            double centerY = height/2.0;
            double radius = (oneLedWidth >= height ? height : oneLedWidth)/2.0;

            gc.fillOval(centerX-radius,centerY-radius,radius*2,radius*2);
        }
    }


    private Canvas ledCanvas = new Canvas();

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
    public Label accumulatorTextFieldHex;
    public Label accumulatorTextFieldBin;
    public Label accumulatorTextFieldDec;

    public Label bLabel;
    public Label bTextFieldHex;
    public Label bTextFieldBin;
    public Label bTextFieldDec;

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

    public Label pLabel;
    public Label pTextField;

    public Label ovLabel;
    public Label ovTextField;

    public Label rs0Label;
    public Label rs0TextField;

    public Label rs1Label;
    public Label rs1TextField;

    public Label f0Label;
    public Label f0TextField;

    public Label acLabel;
    public Label acTextField;

    public Label cLabel;
    public Label cTextField;

    public ToggleButton portButton0;
    public ToggleButton portButton1;
    public ToggleButton portButton2;
    public ToggleButton portButton3;
    public ToggleButton portButton4;
    public ToggleButton portButton5;
    public ToggleButton portButton6;
    public ToggleButton portButton7;
    public Label compilationErrorsLabel;


    private Button translateToMemoryButton;
    private Button stopSimulationButton;
    private Button oneStepButton;

    private TextArea editorTextArea;

    Stage mainStage;
    Scene mainScene;

    private String lines[];

    private Button rysujRunButton;

    public boolean running;

    private GridPane diodesPaneGridPane;
}
