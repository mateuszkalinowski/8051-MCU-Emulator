package stages;

import converters.Converters;
import core.Main;
import exceptions.CompilingException;
import javafx.application.Application;
import javafx.application.Platform;

import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.ScatterChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Pair;

import javax.swing.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Optional;
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
        //upperRow.setPercentHeight(62.5);
        RowConstraints lowerRow = new RowConstraints();
        lowerRow.setPrefHeight(200);
        ColumnConstraints onlyColumn = new ColumnConstraints();
        onlyColumn.setPercentWidth(100);
        mainGridPane.getColumnConstraints().add(onlyColumn);
        mainGridPane.getRowConstraints().add(upperRow);
        mainGridPane.getRowConstraints().add(lowerRow);

        mainGridPane.heightProperty().addListener((observable, oldValue, newValue) -> mainGridPane.getRowConstraints().get(0).setPrefHeight(newValue.doubleValue() - 200));

        editorElementsGridPane = new GridPane();
        editorElementsGridPane.setGridLinesVisible(false);
        RowConstraints rowInEditorElementsGridPane = new RowConstraints();
        rowInEditorElementsGridPane.setPercentHeight(100);
        editorElementsGridPane.getRowConstraints().add(rowInEditorElementsGridPane);
        ColumnConstraints editorColumn = new ColumnConstraints();
        //editorColumn.setPercentWidth(70);
        ColumnConstraints elementsColumn = new ColumnConstraints();
        elementsColumn.setPrefWidth(240);
        editorElementsGridPane.getColumnConstraints().add(editorColumn);
        editorElementsGridPane.getColumnConstraints().add(elementsColumn);
        mainGridPane.add(editorElementsGridPane, 0, 0, 2, 1);

        mainGridPane.widthProperty().addListener((observable, oldValue, newValue) -> editorElementsGridPane.getColumnConstraints().get(0).setPrefWidth(newValue.doubleValue() - 240));

        TabPane simulatorTabPane = new TabPane();
        Tab mainSimulatorTab = new Tab();
        mainSimulatorTab.setClosable(false);
        mainSimulatorTab.setText("Symulator");
        simulatorTabPane.getTabs().add(mainSimulatorTab);
        mainGridPane.add(simulatorTabPane, 0, 1);

        compilationErrorsLabel = new Label("");
        compilationErrorsLabel.setPadding(new Insets(0, 22, 0, 22));
        compilationErrorsLabel.setMaxWidth(Double.MAX_VALUE);
        compilationErrorsLabel.setAlignment(Pos.CENTER_LEFT);
        compilationErrorsLabel.setFont(new Font("Arial", 14));
        compilationErrorsLabel.setPrefHeight(20);
        compilationErrorsLabel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-background-insets: 0 20 0 20");


        HBox compilationErrorsLabelHBox = new HBox();
        HBox.setHgrow(compilationErrorsLabel, Priority.ALWAYS);
        compilationErrorsLabelHBox.getChildren().add(compilationErrorsLabel);
        compilationErrorsLabelHBox.setPadding(new Insets(5, 0, 5, 0));

        editorTabPane = new TabPane();

        HBox editorTextAreaHBox = new HBox();
        HBox.setHgrow(editorTextAreaHBox, Priority.ALWAYS);
        editorTextAreaHBox.setPadding(new Insets(5, 0, 0, 0));

        editorTextAreaHBox.getChildren().add(editorTabPane);

        BorderPane editorBorderPane = new BorderPane();
        editorBorderPane.setBottom(compilationErrorsLabelHBox);
        editorBorderPane.setCenter(editorTextAreaHBox);

        simulatorGridPane = new GridPane();
        //simulatorGridPane.setGridLinesVisible(true);
        RowConstraints rowInSimulator = new RowConstraints();
        rowInSimulator.setPercentHeight(10);
        for (int i = 0; i < 10; i++) {
            simulatorGridPane.getRowConstraints().add(rowInSimulator);
        }
        ColumnConstraints columnInSimulator = new ColumnConstraints();
        columnInSimulator.setPercentWidth(5);
        for (int i = 0; i < 20; i++) {
            simulatorGridPane.getColumnConstraints().add(columnInSimulator);
        }
        mainSimulatorTab.setContent(simulatorGridPane);
        //simulatorGridPane.setGridLinesVisible(true);
        //KOMPONENTY PANELU SYMULATORA

        //AKUMULATOR

        accumulatorLabel = new Label("ACC");
        accumulatorLabel.setMaxWidth(Double.MAX_VALUE);
        accumulatorLabel.setAlignment(Pos.CENTER);
        accumulatorLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(accumulatorLabel, 11, 5, 2, 1);

        accumulatorTextFieldHex = new Label("00h");
        accumulatorTextFieldHex.setMaxWidth(Double.MAX_VALUE);
        accumulatorTextFieldHex.setAlignment(Pos.CENTER);
        accumulatorTextFieldHex.setFont(new Font("Arial", 11));
        accumulatorTextFieldHex.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(accumulatorTextFieldHex, 10, 6);

        accumulatorTextFieldBin = new Label("00000000b");
        accumulatorTextFieldBin.setMaxWidth(Double.MAX_VALUE);
        accumulatorTextFieldBin.setAlignment(Pos.CENTER);
        accumulatorTextFieldBin.setFont(new Font("Arial", 11));
        accumulatorTextFieldBin.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(accumulatorTextFieldBin, 11, 6, 2, 1);

        accumulatorTextFieldDec = new Label("000d");
        accumulatorTextFieldDec.setMaxWidth(Double.MAX_VALUE);
        accumulatorTextFieldDec.setAlignment(Pos.CENTER);
        accumulatorTextFieldDec.setFont(new Font("Arial", 11));
        accumulatorTextFieldDec.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(accumulatorTextFieldDec, 13, 6);

        bLabel = new Label("B");
        bLabel.setMaxWidth(Double.MAX_VALUE);
        bLabel.setAlignment(Pos.CENTER);
        bLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(bLabel, 11, 7, 2, 1);

        bTextFieldHex = new Label("00h");
        bTextFieldHex.setMaxWidth(Double.MAX_VALUE);
        bTextFieldHex.setAlignment(Pos.CENTER);
        bTextFieldHex.setFont(new Font("Arial", 11));
        bTextFieldHex.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(bTextFieldHex, 10, 8);

        bTextFieldBin = new Label("00h");
        bTextFieldBin.setMaxWidth(Double.MAX_VALUE);
        bTextFieldBin.setAlignment(Pos.CENTER);
        bTextFieldBin.setFont(new Font("Arial", 11));
        bTextFieldBin.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(bTextFieldBin, 11, 8, 2, 1);

        bTextFieldDec = new Label("000d");
        bTextFieldDec.setMaxWidth(Double.MAX_VALUE);
        bTextFieldDec.setAlignment(Pos.CENTER);
        bTextFieldDec.setFont(new Font("Arial", 11));
        bTextFieldDec.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(bTextFieldDec, 13, 8);

        r0Label = new Label("R0");
        r0Label.setMaxWidth(Double.MAX_VALUE);
        r0Label.setAlignment(Pos.CENTER);
        r0Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(r0Label, 1, 7);

        r0TextField = new Label("00h");
        r0TextField.setMaxWidth(Double.MAX_VALUE);
        r0TextField.setAlignment(Pos.CENTER);
        r0TextField.setFont(new Font("Arial", 11));
        r0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r0TextField, 1, 8);

        r1Label = new Label("R1");
        r1Label.setMaxWidth(Double.MAX_VALUE);
        r1Label.setAlignment(Pos.CENTER);
        r1Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(r1Label, 2, 7);

        r1TextField = new Label("00h");
        r1TextField.setMaxWidth(Double.MAX_VALUE);
        r1TextField.setAlignment(Pos.CENTER);
        r1TextField.setFont(new Font("Arial", 11));
        r1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r1TextField, 2, 8);

        r2Label = new Label("R2");
        r2Label.setMaxWidth(Double.MAX_VALUE);
        r2Label.setAlignment(Pos.CENTER);
        r2Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(r2Label, 3, 7);

        r2TextField = new Label("00h");
        r2TextField.setMaxWidth(Double.MAX_VALUE);
        r2TextField.setAlignment(Pos.CENTER);
        r2TextField.setFont(new Font("Arial", 11));
        r2TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r2TextField, 3, 8);

        r3Label = new Label("R3");
        r3Label.setMaxWidth(Double.MAX_VALUE);
        r3Label.setAlignment(Pos.CENTER);
        r3Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(r3Label, 4, 7);

        r3TextField = new Label("00h");
        r3TextField.setMaxWidth(Double.MAX_VALUE);
        r3TextField.setAlignment(Pos.CENTER);
        r3TextField.setFont(new Font("Arial", 11));
        r3TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r3TextField, 4, 8);

        r4Label = new Label("R4");
        r4Label.setMaxWidth(Double.MAX_VALUE);
        r4Label.setAlignment(Pos.CENTER);
        r4Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(r4Label, 5, 7);

        r4TextField = new Label("00h");
        r4TextField.setMaxWidth(Double.MAX_VALUE);
        r4TextField.setAlignment(Pos.CENTER);
        r4TextField.setFont(new Font("Arial", 11));
        r4TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r4TextField, 5, 8);

        r5Label = new Label("R5");
        r5Label.setMaxWidth(Double.MAX_VALUE);
        r5Label.setAlignment(Pos.CENTER);
        r5Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(r5Label, 6, 7);

        r5TextField = new Label("00h");
        r5TextField.setMaxWidth(Double.MAX_VALUE);
        r5TextField.setAlignment(Pos.CENTER);
        r5TextField.setFont(new Font("Arial", 11));
        r5TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r5TextField, 6, 8);

        r6Label = new Label("R6");
        r6Label.setMaxWidth(Double.MAX_VALUE);
        r6Label.setAlignment(Pos.CENTER);
        r6Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(r6Label, 7, 7);

        r6TextField = new Label("00h");
        r6TextField.setMaxWidth(Double.MAX_VALUE);
        r6TextField.setAlignment(Pos.CENTER);
        r6TextField.setFont(new Font("Arial", 11));
        r6TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r6TextField, 7, 8);

        r7Label = new Label("R7");
        r7Label.setMaxWidth(Double.MAX_VALUE);
        r7Label.setAlignment(Pos.CENTER);
        r7Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(r7Label, 8, 7);

        r7TextField = new Label("00h");
        r7TextField.setMaxWidth(Double.MAX_VALUE);
        r7TextField.setAlignment(Pos.CENTER);
        r7TextField.setFont(new Font("Arial", 11));
        r7TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(r7TextField, 8, 8);

        cLabel = new Label("C");
        cLabel.setMaxWidth(Double.MAX_VALUE);
        cLabel.setAlignment(Pos.CENTER);
        cLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(cLabel, 1, 5);

        cTextField = new Label("00h");
        cTextField.setMaxWidth(Double.MAX_VALUE);
        cTextField.setAlignment(Pos.CENTER);
        cTextField.setFont(new Font("Arial", 11));
        cTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(cTextField, 1, 6);

        acLabel = new Label("AC");
        acLabel.setMaxWidth(Double.MAX_VALUE);
        acLabel.setAlignment(Pos.CENTER);
        acLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(acLabel, 2, 5);

        acTextField = new Label("00h");
        acTextField.setMaxWidth(Double.MAX_VALUE);
        acTextField.setAlignment(Pos.CENTER);
        acTextField.setFont(new Font("Arial", 11));
        acTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(acTextField, 2, 6);

        f0Label = new Label("F0");
        f0Label.setMaxWidth(Double.MAX_VALUE);
        f0Label.setAlignment(Pos.CENTER);
        f0Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(f0Label, 3, 5);

        f0TextField = new Label("00h");
        f0TextField.setMaxWidth(Double.MAX_VALUE);
        f0TextField.setAlignment(Pos.CENTER);
        f0TextField.setFont(new Font("Arial", 11));
        f0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(f0TextField, 3, 6);

        f1Label = new Label("F1");
        f1Label.setMaxWidth(Double.MAX_VALUE);
        f1Label.setAlignment(Pos.CENTER);
        f1Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(f1Label, 7, 5);

        f1TextField = new Label("00h");
        f1TextField.setMaxWidth(Double.MAX_VALUE);
        f1TextField.setAlignment(Pos.CENTER);
        f1TextField.setFont(new Font("Arial", 11));
        f1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(f1TextField, 7, 6);

        rs1Label = new Label("RS1");
        rs1Label.setMaxWidth(Double.MAX_VALUE);
        rs1Label.setAlignment(Pos.CENTER);
        rs1Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(rs1Label, 4, 5);

        rs1TextField = new Label("00h");
        rs1TextField.setMaxWidth(Double.MAX_VALUE);
        rs1TextField.setAlignment(Pos.CENTER);
        rs1TextField.setFont(new Font("Arial", 11));
        rs1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(rs1TextField, 4, 6);

        rs0Label = new Label("RS0");
        rs0Label.setMaxWidth(Double.MAX_VALUE);
        rs0Label.setAlignment(Pos.CENTER);
        rs0Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(rs0Label, 5, 5);

        rs0TextField = new Label("00h");
        rs0TextField.setMaxWidth(Double.MAX_VALUE);
        rs0TextField.setAlignment(Pos.CENTER);
        rs0TextField.setFont(new Font("Arial", 11));
        rs0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(rs0TextField, 5, 6);

        ovLabel = new Label("OV");
        ovLabel.setMaxWidth(Double.MAX_VALUE);
        ovLabel.setAlignment(Pos.CENTER);
        ovLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(ovLabel, 6, 5);

        ovTextField = new Label("00h");
        ovTextField.setMaxWidth(Double.MAX_VALUE);
        ovTextField.setAlignment(Pos.CENTER);
        ovTextField.setFont(new Font("Arial", 11));
        ovTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(ovTextField, 6, 6);

        pLabel = new Label("P");
        pLabel.setMaxWidth(Double.MAX_VALUE);
        pLabel.setAlignment(Pos.CENTER);
        pLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(pLabel, 8, 5);

        pTextField = new Label("00h");
        pTextField.setMaxWidth(Double.MAX_VALUE);
        pTextField.setAlignment(Pos.CENTER);
        pTextField.setFont(new Font("Arial", 11));
        pTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(pTextField, 8, 6);

        p0Label = new Label("P0");
        p0Label.setMaxWidth(Double.MAX_VALUE);
        p0Label.setAlignment(Pos.CENTER);
        p0Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(p0Label, 16, 2);

        p0TextField = new Label("11111111");
        p0TextField.setMaxWidth(Double.MAX_VALUE);
        p0TextField.setAlignment(Pos.CENTER);
        p0TextField.setFont(new Font("Arial", 11));
        p0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(p0TextField, 17, 2, 2, 1);

        p1Label = new Label("P1");
        p1Label.setMaxWidth(Double.MAX_VALUE);
        p1Label.setAlignment(Pos.CENTER);
        p1Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(p1Label, 16, 4);

        p1TextField = new Label("11111111");
        p1TextField.setMaxWidth(Double.MAX_VALUE);
        p1TextField.setAlignment(Pos.CENTER);
        p1TextField.setFont(new Font("Arial", 11));
        p1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(p1TextField, 17, 4, 2, 1);

        p2Label = new Label("P2");
        p2Label.setMaxWidth(Double.MAX_VALUE);
        p2Label.setAlignment(Pos.CENTER);
        p2Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(p2Label, 16, 6);

        p2TextField = new Label("11111111");
        p2TextField.setMaxWidth(Double.MAX_VALUE);
        p2TextField.setAlignment(Pos.CENTER);
        p2TextField.setFont(new Font("Arial", 11));
        p2TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(p2TextField, 17, 6, 2, 1);

        p3Label = new Label("P3");
        p3Label.setMaxWidth(Double.MAX_VALUE);
        p3Label.setAlignment(Pos.CENTER);
        p3Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(p3Label, 16, 8);

        p3TextField = new Label("11111111");
        p3TextField.setMaxWidth(Double.MAX_VALUE);
        p3TextField.setAlignment(Pos.CENTER);
        p3TextField.setFont(new Font("Arial", 11));
        p3TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(p3TextField, 17, 8, 2, 1);

        portsDesc = new Label("Porty We/Wy:");
        portsDesc.setMaxWidth(Double.MAX_VALUE);
        portsDesc.setAlignment(Pos.CENTER);
        portsDesc.setFont(new Font("Arial", 11));
        simulatorGridPane.add(portsDesc, 16, 0, 3, 2);

        /*Label pswLabel = new Label("REG\nPSW:");
        pswLabel.setMaxWidth(Double.MAX_VALUE);
        pswLabel.setAlignment(Pos.CENTER);
        pswLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(pswLabel,1,5,1,2);*/

        timePassedLabel = new Label("Czas Symulacji:");
        timePassedLabel.setMaxWidth(Double.MAX_VALUE);
        timePassedLabel.setAlignment(Pos.CENTER);
        timePassedLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(timePassedLabel, 1, 1, 2, 1);

        timePassedTextField = new Label("0 mkS");
        timePassedTextField.setMaxWidth(Double.MAX_VALUE);
        timePassedTextField.setAlignment(Pos.CENTER);
        timePassedTextField.setFont(new Font("Arial", 11));
        timePassedTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(timePassedTextField, 1, 2, 2, 1);

        pcLabel = new Label("PC:");
        pcLabel.setMaxWidth(Double.MAX_VALUE);
        pcLabel.setAlignment(Pos.CENTER);
        pcLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(pcLabel, 1, 3, 2, 1);

        pcTextField = new Label("0h");
        pcTextField.setMaxWidth(Double.MAX_VALUE);
        pcTextField.setAlignment(Pos.CENTER);
        pcTextField.setFont(new Font("Arial", 11));
        pcTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(pcTextField, 1, 4, 2, 1);

        T0LLabel = new Label("TL0");
        T0LLabel.setMaxWidth(Double.MAX_VALUE);
        T0LLabel.setAlignment(Pos.CENTER);
        T0LLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(T0LLabel, 13, 1);

        T0LTextField = new Label("0h");
        T0LTextField.setMaxWidth(Double.MAX_VALUE);
        T0LTextField.setAlignment(Pos.CENTER);
        T0LTextField.setFont(new Font("Arial", 11));
        T0LTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(T0LTextField, 13, 2);

        T0HLabel = new Label("TH0");
        T0HLabel.setMaxWidth(Double.MAX_VALUE);
        T0HLabel.setAlignment(Pos.CENTER);
        T0HLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(T0HLabel, 12, 1);

        T0HTextField = new Label("0h");
        T0HTextField.setMaxWidth(Double.MAX_VALUE);
        T0HTextField.setAlignment(Pos.CENTER);
        T0HTextField.setFont(new Font("Arial", 11));
        T0HTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(T0HTextField, 12, 2);

        T1LLabel = new Label("TL1");
        T1LLabel.setMaxWidth(Double.MAX_VALUE);
        T1LLabel.setAlignment(Pos.CENTER);
        T1LLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(T1LLabel, 11, 1);

        T1LTtextField = new Label("0h");
        T1LTtextField.setMaxWidth(Double.MAX_VALUE);
        T1LTtextField.setAlignment(Pos.CENTER);
        T1LTtextField.setFont(new Font("Arial", 11));
        T1LTtextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(T1LTtextField, 11, 2);

        T1HLabel = new Label("TH1");
        T1HLabel.setMaxWidth(Double.MAX_VALUE);
        T1HLabel.setAlignment(Pos.CENTER);
        T1HLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(T1HLabel, 10, 1);

        T1HTextField = new Label("0h");
        T1HTextField.setMaxWidth(Double.MAX_VALUE);
        T1HTextField.setAlignment(Pos.CENTER);
        T1HTextField.setFont(new Font("Arial", 11));
        T1HTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(T1HTextField, 10, 2);

        TMODLabel = new Label("TMOD:");
        TMODLabel.setMaxWidth(Double.MAX_VALUE);
        TMODLabel.setAlignment(Pos.CENTER);
        TMODLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(TMODLabel, 10, 3, 1, 2);

        TMODTextField = new Label("0h");
        TMODTextField.setMaxWidth(Double.MAX_VALUE);
        TMODTextField.setAlignment(Pos.CENTER);
        TMODTextField.setFont(new Font("Arial", 11));
        TMODTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(TMODTextField, 11, 3, 1, 2);


        TCONLabel = new Label("TCON:");
        TCONLabel.setMaxWidth(Double.MAX_VALUE);
        TCONLabel.setAlignment(Pos.CENTER);
        TCONLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(TCONLabel, 12, 3, 1, 2);

        TCONTextField = new Label("0h");
        TCONTextField.setMaxWidth(Double.MAX_VALUE);
        TCONTextField.setAlignment(Pos.CENTER);
        TCONTextField.setFont(new Font("Arial", 11));
        TCONTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(TCONTextField, 13, 3, 1, 2);

        EX0Label = new Label("EX0");
        EX0Label.setMaxWidth(Double.MAX_VALUE);
        EX0Label.setAlignment(Pos.CENTER);
        EX0Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(EX0Label, 8, 2);

        EX0TextField = new Label("0");
        EX0TextField.setMaxWidth(Double.MAX_VALUE);
        EX0TextField.setAlignment(Pos.CENTER);
        EX0TextField.setFont(new Font("Arial", 11));
        EX0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(EX0TextField, 8, 3);

        ET0Label = new Label("ET0");
        ET0Label.setMaxWidth(Double.MAX_VALUE);
        ET0Label.setAlignment(Pos.CENTER);
        ET0Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(ET0Label, 7, 2);

        ET0TextField = new Label("0");
        ET0TextField.setMaxWidth(Double.MAX_VALUE);
        ET0TextField.setAlignment(Pos.CENTER);
        ET0TextField.setFont(new Font("Arial", 11));
        ET0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(ET0TextField, 7, 3);

        EX1Label = new Label("EX1");
        EX1Label.setMaxWidth(Double.MAX_VALUE);
        EX1Label.setAlignment(Pos.CENTER);
        EX1Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(EX1Label, 6, 2);

        EX1TextField = new Label("0");
        EX1TextField.setMaxWidth(Double.MAX_VALUE);
        EX1TextField.setAlignment(Pos.CENTER);
        EX1TextField.setFont(new Font("Arial", 11));
        EX1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(EX1TextField, 6, 3);

        ET1Label = new Label("ET1");
        ET1Label.setMaxWidth(Double.MAX_VALUE);
        ET1Label.setAlignment(Pos.CENTER);
        ET1Label.setFont(new Font("Arial", 11));
        simulatorGridPane.add(ET1Label, 5, 2);

        ET1TextField = new Label("0");
        ET1TextField.setMaxWidth(Double.MAX_VALUE);
        ET1TextField.setAlignment(Pos.CENTER);
        ET1TextField.setFont(new Font("Arial", 11));
        ET1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(ET1TextField, 5, 3);

        ESLabel = new Label("ES");
        ESLabel.setMaxWidth(Double.MAX_VALUE);
        ESLabel.setAlignment(Pos.CENTER);
        ESLabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(ESLabel, 4, 2);

        ESTextField = new Label("0");
        ESTextField.setMaxWidth(Double.MAX_VALUE);
        ESTextField.setAlignment(Pos.CENTER);
        ESTextField.setFont(new Font("Arial", 11));
        ESTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(ESTextField, 4, 3);

        EALabel = new Label("EA");
        EALabel.setMaxWidth(Double.MAX_VALUE);
        EALabel.setAlignment(Pos.CENTER);
        EALabel.setFont(new Font("Arial", 11));
        simulatorGridPane.add(EALabel, 3, 2);

        EATextField = new Label("0");
        EATextField.setMaxWidth(Double.MAX_VALUE);
        EATextField.setAlignment(Pos.CENTER);
        EATextField.setFont(new Font("Arial", 11));
        EATextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(EATextField, 3, 3);


        translateToMemoryButton = new Button("Asemblacja");
        translateToMemoryButton.setOnAction(event -> {
            //  lines = editorTextArea.getText().split("\n");
            lines = editorTabs.get(editorTabPane.getSelectionModel().getSelectedIndex()).ownTextArea.getText().split("\n");
            try {
                ArrayList<String> compilatedText = Main.cpu.codeMemory.setMemory(lines,true);
                Main.cpu.resetCpu();
                Main.cpu.refreshGui();
                translateToMemoryButton.setDisable(true);
                stopSimulationButton.setDisable(false);
                oneStepButton.setDisable(false);
                //editorTextArea.setEditable(false);
                editorTabs.get(editorTabPane.getSelectionModel().getSelectedIndex()).ownTextArea.setEditable(false);
                continuousRunButton.setDisable(false);
                running = true;
                changeValueInChangeValueButton.setDisable(false);
                setEditorText(compilatedText);

                if (portToggle7.isSelected())
                    Main.cpu.mainMemory.buttonsState[0] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[0] = '1';
                if (portToggle6.isSelected())
                    Main.cpu.mainMemory.buttonsState[1] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[1] = '1';
                if (portToggle5.isSelected())
                    Main.cpu.mainMemory.buttonsState[2] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[2] = '1';
                if (portToggle4.isSelected())
                    Main.cpu.mainMemory.buttonsState[3] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[3] = '1';
                if (portToggle3.isSelected())
                    Main.cpu.mainMemory.buttonsState[4] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[4] = '1';
                if (portToggle2.isSelected())
                    Main.cpu.mainMemory.buttonsState[5] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[5] = '1';
                if (portToggle1.isSelected())
                    Main.cpu.mainMemory.buttonsState[6] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[6] = '1';
                if (portToggle0.isSelected())
                    Main.cpu.mainMemory.buttonsState[7] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[7] = '1';


                StringBuilder content = new StringBuilder();
                content.append("\t 0\t");
                content.append(" 1\t");
                content.append(" 2\t");
                content.append(" 3\t");
                content.append(" 4\t");
                content.append(" 5\t");
                content.append(" 6\t");
                content.append(" 7\t");
                content.append(" 8\t");
                content.append(" 9\t");
                content.append(" A\t");
                content.append(" B\t");
                content.append(" C\t");
                content.append(" D\t");
                content.append(" E\t");
                content.append(" F\t");
                content.append("\n");
                for (int i = 0; i < 128; i++) {
                    content.append(Integer.toHexString(i)).append("\t");
                    for (int j = 0; j < 16; j++) {
                        if (Integer.toHexString(Integer.parseInt(Main.cpu.codeMemory.getFromAddress(i * 16 + j), 2)).length() == 1) {
                            content.append(" " + "0").append(Integer.toHexString(Integer.parseInt(Main.cpu.codeMemory.getFromAddress(i * 16 + j), 2)).toUpperCase()).append(" ");
                        } else
                            content.append(" ").append(Integer.toHexString(Integer.parseInt(Main.cpu.codeMemory.getFromAddress(i * 16 + j), 2)).toUpperCase()).append(" ");
                        content.append("\t");
                    }
                    if (i != 127)
                        content.append("\n");
                }


                programMemoryTextArea.setText(content.toString());
                currentlyRunTabName = editorTabPane.getSelectionModel().getSelectedItem().getText();
                editorTabPane.getSelectionModel().getSelectedItem().setClosable(false);
                Main.cpu.mainMemory.putFromExternal(160);
                Main.cpu.refreshGui();

            } catch (CompilingException e) {
                Main.stage.compilationErrorsLabel.setText("Błąd: " + e.getMessage());
                Main.stage.compilationErrorsLabel.setStyle("-fx-background-color: red; -fx-background-radius: 10; -fx-background-insets: 0 20 0 20");
                Main.stage.compilationErrorsLabel.setTooltip(new Tooltip("Błąd: " + e.getMessage()));
            }
        });

        stopSimulationButton = new Button("Reset");
        stopSimulationButton.setOnAction(event -> {
            translateToMemoryButton.setDisable(false);
            stopSimulationButton.setDisable(true);
            oneStepButton.setDisable(true);
            //editorTextArea.setEditable(true);
            //editorTextArea.setText("");
            int numerKarty = 0;
            for (; numerKarty < editorTabs.size(); numerKarty++) {
                if (editorTabs.get(numerKarty).ownTab.getText().equals(currentlyRunTabName)) break;
            }
            editorTabs.get(numerKarty).ownTab.setClosable(true);
            currentlyRunTabName = "";

            //editorTabs.get(editorTabPane.getSelectionModel().getSelectedIndex()).ownTextArea.setEditable(true);
            //editorTabs.get(editorTabPane.getSelectionModel().getSelectedIndex()).ownTextArea.setText("");

            editorTabs.get(numerKarty).ownTextArea.setEditable(true);
            editorTabs.get(numerKarty).ownTextArea.setText("");

            continuousRunFlag = false;
            continuousRunButton.setDisable(true);
            port0History = new char[portChartScale][8];
            port1History = new char[portChartScale][8];
            port2History = new char[portChartScale][8];
            port3History = new char[portChartScale][8];
            for (int i = 0; i < portChartScale; i++) {
                port0History[i] = "11111111".toCharArray();
            }
            for (int i = 0; i < portChartScale; i++) {
                port1History[i] = "11111111".toCharArray();
            }
            for (int i = 0; i < portChartScale; i++) {
                port2History[i] = "11111111".toCharArray();
            }
            for (int i = 0; i < portChartScale; i++) {
                port3History[i] = "11111111".toCharArray();
            }
            continuousRunButton.setText("Praca Ciągła");
            running = false;
            changeValueInChangeValueButton.setDisable(true);
            StringBuilder textToSet = new StringBuilder();
            for (String line : lines) {
                textToSet.append(line).append("\n");
            }
            //editorTextArea.setText(textToSet.substring(0,textToSet.length()-1));
            editorTabs.get(numerKarty).ownTextArea.setText(textToSet.substring(0, textToSet.length() - 1));
            Main.cpu.resetCpu();
            Main.cpu.refreshGui();
            compilationErrorsLabel.setText("");
            compilationErrorsLabel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-background-insets: 0 20 0 20");
            OscilloscopePane.resetPrzebieg();
        });

        oneStepButton = new Button("Krok");
        oneStepButton.setOnAction(event -> {
            try {
                Main.cpu.executeInstruction();
                Main.cpu.refreshGui();
                OscilloscopePane.updateChart();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd Wykonania");
                alert.setHeaderText("Wykonanie przebiegło nieudanie");
                alert.setContentText("Sprawdź kod jeszcze raz, informacja gdzie wystąpił błąd zostanie" +
                        "dodana w jednej z kolejnych wersji programu");
                alert.showAndWait();
            }
        });

        continuousRunButton = new Button("Praca Ciągła");
        continuousRunButton.setDisable(true);
        continuousRunButton.setOnAction(event -> {
            if (continuousRunButton.getText().equals("Praca Ciągła")) {
                continuousRunButton.setText("Stop");
                changeValueInChangeValueButton.setDisable(true);

                oneStepButton.setDisable(true);
                autoRun = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        continuousRunFlag = true;
                        continuousRunFlag = true;
                        long time = System.nanoTime();
                        while (continuousRunFlag) {
                            if (System.nanoTime() - time > 1000000000 / speedSelectComboBox.getSelectionModel().getSelectedItem()) {
                                Main.cpu.executeInstruction();
                                Platform.runLater(() -> OscilloscopePane.updateChart());
                                time = System.nanoTime();
                                Platform.runLater(() -> Main.cpu.refreshGui());
                            }
                        }
                        return null;
                    }
                };
                Thread continuousRunThread = new Thread(autoRun);
                continuousRunThread.start();
            } else {
                continuousRunFlag = false;
                oneStepButton.setDisable(false);
                continuousRunButton.setText("Praca Ciągła");
                changeValueInChangeValueButton.setDisable(false);
            }
        });

        translateToMemoryButton.setMaxWidth(100);
        stopSimulationButton.setMaxWidth(100);
        stopSimulationButton.setDisable(true);
        continuousRunButton.setMaxWidth(100);
        oneStepButton.setMaxWidth(100);
        oneStepButton.setDisable(true);

        speedSelectComboBox = new ComboBox<>();
        speedSelectComboBox.getItems().addAll(1, 5, 10, 50, 100, 500, 1000, 2000);
        speedSelectComboBox.setMaxWidth(100);
        speedSelectComboBox.getSelectionModel().selectFirst();

        speedSelectLabel = new Label("Prędkość Symulacji:");
        speedSelectLabel.setMaxWidth(Double.MAX_VALUE);
        speedSelectLabel.setAlignment(Pos.CENTER);
        speedSelectLabel.setFont(new Font("Arial", 11));

        VBox buttonBox = new VBox();
        buttonBox.setPadding(new Insets(0, 10, 0, 10));
        buttonBox.setAlignment(Pos.CENTER);
        VBox.setVgrow(translateToMemoryButton, Priority.ALWAYS);
        VBox.setVgrow(stopSimulationButton, Priority.ALWAYS);
        VBox.setVgrow(oneStepButton, Priority.ALWAYS);
        VBox.setVgrow(continuousRunButton, Priority.ALWAYS);
        buttonBox.setSpacing(5);

        buttonBox.getChildren().addAll(translateToMemoryButton, stopSimulationButton, oneStepButton, speedSelectLabel, speedSelectComboBox, continuousRunButton);
        buttonBox.setAlignment(Pos.TOP_CENTER);
        buttonBox.setPadding(new Insets(5, 5, 0, 5));

        buttonsBorderPane = new BorderPane();
        buttonsBorderPane.setCenter(buttonBox);
        buttonsBorderPane.setBottom(programImageCanvas);
        editorBorderPane.setLeft(buttonsBorderPane);



        editorElementsGridPane.add(editorBorderPane, 0, 0);

        MenuBar mainMenuBar = new MenuBar();
        mainBorderPane.setTop(mainMenuBar);

        Menu menuFile = new Menu("Plik");
        mainMenuBar.getMenus().add(menuFile);

        Menu menuOptions = new Menu("Konfiguracja");
        mainMenuBar.getMenus().add(menuOptions);

        Menu menuMemory = new Menu("Pamięć");
        mainMenuBar.getMenus().add(menuMemory);

        Menu menuTools = new Menu("Narzędzia");
        mainMenuBar.getMenus().add(menuTools);

        MenuItem menuItemOscilloscope = new MenuItem("Oscyloskop");

        Menu menuLowRam = new Menu("Low RAM");
        menuMemory.getItems().add(menuLowRam);

        MenuItem exportLowRamMenuItem = new MenuItem("Eksportuj");
        exportLowRamMenuItem.setOnAction(event -> {
            FileChooser chooseFile = new FileChooser();
            chooseFile.setTitle("Wybierz lokalizację zapisu");
            chooseFile.setInitialFileName("memory.txt");
            chooseFile.setInitialDirectory(new File(System.getProperty("user.home")));
            File saveFile = chooseFile.showSaveDialog(primaryStage);
            if (saveFile != null) {
                try {
                    PrintWriter in = new PrintWriter(saveFile);
                    String text;
                    for (int i = 0; i < 128; i++) {
                        text = Main.cpu.codeMemory.make8DigitsStringFromNumber(String.valueOf(Main.cpu.mainMemory.get(i)));
                        in.println(text);
                    }
                    in.close();
                } catch (FileNotFoundException e) {
                    System.out.println("File error");
                }
            }
        });
        MenuItem importLowRawMenuItem = new MenuItem("Importuj");
        importLowRawMenuItem.setOnAction(event -> {
            FileChooser chooseFile = new FileChooser();
            chooseFile.setTitle("Wybierz plik z pamięcią");
            File openFile = chooseFile.showOpenDialog(primaryStage);
            if (openFile != null) {
                String number;
                try {
                    Scanner in = new Scanner(openFile);
                    int i = 0;
                    while (in.hasNextLine()) {
                        number = in.nextLine();
                        try {
                            int w = Integer.valueOf(number, 2);
                            Main.cpu.mainMemory.put(i, w);
                            i++;
                        } catch (NumberFormatException ignored) {
                        }

                    }
                    Main.cpu.refreshGui();
                } catch (FileNotFoundException ignored) {
                }
            }
        });
        menuLowRam.getItems().addAll(exportLowRamMenuItem, importLowRawMenuItem);

        MenuItem paneConfigurationMenuItem = new MenuItem("Układ płytki prototypowej");
        menuOptions.getItems().add(paneConfigurationMenuItem);
        paneConfigurationMenuItem.setOnAction(event -> {
            PaneConfigStage paneConfigStage = new PaneConfigStage();
            try {
                paneConfigStage.start(primaryStage);
            } catch (Exception ignored) {
            }
        });

        menuItemOscilloscope.setOnAction(event -> {
            try {
                OscilloscopePane.start(primaryStage);
            } catch (Exception ignored) {
            }
        });

        MenuItem exitMenuItem = new MenuItem("Zamknij");
        exitMenuItem.setOnAction(event -> System.exit(0));
        exitMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        importFileMenuItem = new MenuItem("Otwórz");
        importFileMenuItem.setOnAction(event -> {
            if (!running) {
                FileChooser chooseFile = new FileChooser();
                chooseFile.setTitle("Wybierz plik z programem");
                File openFile = chooseFile.showOpenDialog(primaryStage);
                if (openFile != null) {
                    StringBuilder textToSet = new StringBuilder();
                    try {
                        Scanner in = new Scanner(openFile);
                        while (in.hasNextLine())
                            textToSet.append(in.nextLine()).append("\n");

                        for(int i = 0; i < editorTabs.size();i++) {
                            if(editorTabs.get(i).ownTab.getText().equals(openFile.getName())) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Błąd dodawania pliku");
                                alert.setHeaderText("Nie można było zaimportować pliku o nazwie '"  +openFile.getName()+"'");
                                alert.setContentText("Plik o tej nazwie już istnieje");
                                alert.showAndWait();
                                return;
                            }
                        }

                        editorTabs.add(new editorTab());
                        editorTabs.get(editorTabs.size() - 1).ownTextArea.setText(textToSet.substring(0, textToSet.length() - 1));
                        editorTabs.get(editorTabs.size() - 1).path = openFile.getPath();
                        editorTabs.get(editorTabs.size() - 1).ownTab.setText(openFile.getName());
                        editorTabs.get(editorTabs.size() - 1).previousText = editorTabs.get(editorTabs.size() - 1).ownTextArea.getText();
                        editorTabPane.getSelectionModel().selectLast();
                    } catch (FileNotFoundException ignored) {
                    }
                }
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Nie można otworzyć pliku");
                alert.setHeaderText("Aby wczytać plik zatrzymaj symulacja");
                alert.setContentText("Nie można wczytywać plików podczas pracy emulatora.");
                alert.showAndWait();
            }
        });


        saveAsFileMenuItem = new MenuItem("Zapisz jako");
        saveAsFileMenuItem.setOnAction(event -> {
            if (editorTabPane.getSelectionModel().getSelectedItem().getText().equals(currentlyRunTabName)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Błąd zapisywania");
                alert.setHeaderText(null);
                alert.setContentText("Nie możesz zapisać aktualnie uruchomionego programu");

                alert.showAndWait();
                return;
            }
            FileChooser chooseFile = new FileChooser();
            chooseFile.setTitle("Wybierz lokalizację zapisu");
            if (editorTabPane.getSelectionModel().getSelectedItem().getText().startsWith("*"))
                chooseFile.setInitialFileName(editorTabPane.getSelectionModel().getSelectedItem().getText().substring(1));
            else
                chooseFile.setInitialFileName(editorTabPane.getSelectionModel().getSelectedItem().getText());
            chooseFile.setInitialDirectory(new File(System.getProperty("user.home")));
            File saveFile = chooseFile.showSaveDialog(primaryStage);
            if (saveFile != null) {
                try {
                    PrintWriter in = new PrintWriter(saveFile);
                    // String text = editorTextArea.getText();
                    String text = editorTabs.get(editorTabPane.getSelectionModel().getSelectedIndex()).ownTextArea.getText();
                    in.println(text);
                    in.close();

                    int numerKarty = 0;
                    for (; numerKarty < editorTabs.size(); numerKarty++) {
                        if (editorTabs.get(numerKarty).ownTab.getText().equals(editorTabPane.getSelectionModel().getSelectedItem().getText()))
                            break;
                    }

                    editorTabs.get(numerKarty).edited = false;
                    editorTabs.get(numerKarty).ownTab.setText(saveFile.getName());
                    editorTabs.get(numerKarty).path = saveFile.getPath();
                    editorTabs.get(numerKarty).previousText = editorTabs.get(numerKarty).ownTextArea.getText();


                } catch (FileNotFoundException e) {
                    System.out.println("File error");
                }
            }
        });

        newFileMenuItem = new MenuItem("Nowy");
        newFileMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+N"));
        newFileMenuItem.setOnAction(event -> {

            TextInputDialog dialog = new TextInputDialog();
            dialog.setTitle("Tworzenie nowego pliku");
            dialog.setHeaderText("Podaj nazwę nowego pliku");
            while(true) {
                boolean exit = false;
                Optional<String> result = dialog.showAndWait();
                if (result.isPresent()) {
                    String name = result.get();

                    if(name.equals("")) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Błąd dodawania pliku");
                        alert.setHeaderText("Nie można było stworzyć pliku o nazwie '" + name + "'");
                        alert.setContentText("Nazwa pliku nie może być pusta");
                        alert.showAndWait();
                        continue;
                    }

                    if (name.charAt(0) == '*') {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Błąd dodawania pliku");
                        alert.setHeaderText("Nie można było stworzyć pliku o nazwie '" + name + "'");
                        alert.setContentText("Nazwa pliku nie może zaczynać się od znaku '*'");
                        alert.showAndWait();
                        continue;
                    }

                    for (int i = 0; i < editorTabs.size(); i++) {
                        if (editorTabs.get(i).ownTab.getText().equals(name)) {
                            Alert alert = new Alert(Alert.AlertType.ERROR);
                            alert.setTitle("Błąd dodawania pliku");
                            alert.setHeaderText("Nie można było stworzyć pliku o nazwie '" + name + "'");
                            alert.setContentText("Plik o tej nazwie już istnieje");
                            alert.showAndWait();
                            exit = true;
                        }
                    }
                    if(!exit) {
                        editorTabs.add(new editorTab());
                        editorTabs.get(editorTabs.size() - 1).ownTab.setText(name);
                        editorTabPane.getSelectionModel().selectLast();
                        break;
                    }
                }
                else
                    break;
            }
        });
        saveFileMenuItem = new MenuItem("Zapisz");
        saveFileMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+S"));
        saveFileMenuItem.setOnAction(event -> {
            String selectedCardName = editorTabPane.getSelectionModel().getSelectedItem().getText();
            int numerKarty = 0;
            for (; numerKarty < editorTabs.size(); numerKarty++) {
                if (editorTabs.get(numerKarty).ownTab.getText().equals(selectedCardName)) break;
            }

            if (selectedCardName.equals(currentlyRunTabName)) {
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Błąd zapisywania");
                alert.setHeaderText(null);
                alert.setContentText("Nie możesz zapisać aktualnie uruchomionego programu");

                alert.showAndWait();
                return;
            }

            if (editorTabs.get(numerKarty).path.equals("")) {

                FileChooser chooseFile = new FileChooser();
                chooseFile.setTitle("Wybierz lokalizację zapisu");
                if (editorTabPane.getSelectionModel().getSelectedItem().getText().startsWith("*"))
                    chooseFile.setInitialFileName(editorTabPane.getSelectionModel().getSelectedItem().getText().substring(1));
                else
                    chooseFile.setInitialFileName(editorTabPane.getSelectionModel().getSelectedItem().getText());
                chooseFile.setInitialDirectory(new File(System.getProperty("user.home")));
                File saveFile = chooseFile.showSaveDialog(primaryStage);
                if (saveFile != null) {
                    try {
                        PrintWriter in = new PrintWriter(saveFile);
                        // String text = editorTextArea.getText();
                        String text = editorTabs.get(editorTabPane.getSelectionModel().getSelectedIndex()).ownTextArea.getText();
                        in.println(text);
                        in.close();

                        editorTabs.get(numerKarty).edited = false;
                        editorTabs.get(numerKarty).ownTab.setText(saveFile.getName());
                        editorTabs.get(numerKarty).path = saveFile.getPath();
                        editorTabs.get(numerKarty).previousText = editorTabs.get(numerKarty).ownTextArea.getText();


                    } catch (FileNotFoundException e) {
                        System.out.println("File error");
                    }
                }
            } else {
                try {
                    PrintWriter in = new PrintWriter(editorTabs.get(numerKarty).path);
                    String text = editorTabs.get(numerKarty).ownTextArea.getText();
                    in.println(text);
                    in.close();

                    editorTabs.get(numerKarty).edited = false;
                    editorTabs.get(numerKarty).previousText = editorTabs.get(numerKarty).ownTextArea.getText();

                    if (editorTabs.get(numerKarty).ownTab.getText().startsWith("*")) {
                        editorTabs.get(numerKarty).ownTab.setText(editorTabs.get(numerKarty).ownTab.getText().substring(1));
                    }
                } catch (Exception e) {
                    //TODO ERROR MESSAGE
                }

            }


        });

        menuFile.getItems().addAll(newFileMenuItem, saveFileMenuItem, saveAsFileMenuItem, new SeparatorMenuItem(), importFileMenuItem, new SeparatorMenuItem(), exitMenuItem);
        menuTools.getItems().add(menuItemOscilloscope);


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
        columnInDiodesPane.setPercentWidth(100.0 / 8.0);
        for (int i = 0; i < 8; i++) {
            diodesPaneGridPane.getColumnConstraints().add(columnInDiodesPane);
        }
        RowConstraints rowInDiodesPane = new RowConstraints();
        rowInDiodesPane.setPercentHeight(10);
        for (int i = 0; i < 10; i++)
            diodesPaneGridPane.getRowConstraints().add(rowInDiodesPane);

        portToggle0 = new ToggleButton("0");
        portToggle0.setFont(new Font("Arial", 13));
        portToggle1 = new ToggleButton("1");
        portToggle1.setFont(new Font("Arial", 13));
        portToggle2 = new ToggleButton("2");
        portToggle2.setFont(new Font("Arial", 13));
        portToggle3 = new ToggleButton("3");
        portToggle3.setFont(new Font("Arial", 13));
        portToggle4 = new ToggleButton("4");
        portToggle4.setFont(new Font("Arial", 13));
        portToggle5 = new ToggleButton("5");
        portToggle5.setFont(new Font("Arial", 13));
        portToggle6 = new ToggleButton("6");
        portToggle6.setFont(new Font("Arial", 13));
        portToggle7 = new ToggleButton("7");
        portToggle7.setFont(new Font("Arial", 13));

        portToggle0.setMaxWidth(Double.MAX_VALUE);
        portToggle1.setMaxWidth(Double.MAX_VALUE);
        portToggle2.setMaxWidth(Double.MAX_VALUE);
        portToggle3.setMaxWidth(Double.MAX_VALUE);
        portToggle4.setMaxWidth(Double.MAX_VALUE);
        portToggle5.setMaxWidth(Double.MAX_VALUE);
        portToggle6.setMaxWidth(Double.MAX_VALUE);
        portToggle7.setMaxWidth(Double.MAX_VALUE);

        portButton0 = new Button("0");
        portButton0.setFont(new Font("Arial", 13));
        portButton1 = new Button("1");
        portButton1.setFont(new Font("Arial", 13));
        portButton2 = new Button("2");
        portButton2.setFont(new Font("Arial", 13));
        portButton3 = new Button("3");
        portButton3.setFont(new Font("Arial", 13));
        portButton4 = new Button("4");
        portButton4.setFont(new Font("Arial", 13));
        portButton5 = new Button("5");
        portButton5.setFont(new Font("Arial", 13));
        portButton6 = new Button("6");
        portButton6.setFont(new Font("Arial", 13));
        portButton7 = new Button("7");
        portButton7.setFont(new Font("Arial", 13));

        portButton0.setMaxWidth(Double.MAX_VALUE);
        portButton1.setMaxWidth(Double.MAX_VALUE);
        portButton2.setMaxWidth(Double.MAX_VALUE);
        portButton3.setMaxWidth(Double.MAX_VALUE);
        portButton4.setMaxWidth(Double.MAX_VALUE);
        portButton5.setMaxWidth(Double.MAX_VALUE);
        portButton6.setMaxWidth(Double.MAX_VALUE);
        portButton7.setMaxWidth(Double.MAX_VALUE);
        portToggle7.setOnAction(event -> {
            if (portToggle7.isSelected()) {
                Main.cpu.mainMemory.buttonsState[0] = '0';
                if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT1"))) {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE1"), true);
                }
                try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), false);
                } catch (Exception e){}
            } else {
                Main.cpu.mainMemory.buttonsState[0] = '1';
                if (!anyTogglePressed()) {
                    try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), true);
                } catch (Exception e){}
                }
            }
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle6.setOnAction(event -> {
            if (portToggle6.isSelected()) {
                Main.cpu.mainMemory.buttonsState[1] = '0';
                if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT1"))) {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE1"), true);
                }
                try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), false);
                } catch (Exception e){}
            } else {
                Main.cpu.mainMemory.buttonsState[1] = '1';
                if (!anyTogglePressed()) {
                    try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), true);
                } catch (Exception e){}
                }
            }
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle5.setOnAction(event -> {
            if (portToggle5.isSelected()) {
                Main.cpu.mainMemory.buttonsState[2] = '0';
                if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT1"))) {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE1"), true);
                }
                try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), false);
                } catch (Exception e){}
            } else {
                Main.cpu.mainMemory.buttonsState[2] = '1';
                if (!anyTogglePressed()) {
                    try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), true);
                } catch (Exception e){}
                }
            }
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle4.setOnAction(event -> {
            if (portToggle4.isSelected()) {
                Main.cpu.mainMemory.buttonsState[3] = '0';
                if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT1"))) {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE1"), true);
                }
                try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), false);
                } catch (Exception e){}
            } else {
                Main.cpu.mainMemory.buttonsState[3] = '1';
                if (!anyTogglePressed()) {
                    try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), true);
                } catch (Exception e){}
                }
            }
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle3.setOnAction(event -> {
            if (portToggle3.isSelected()) {
                Main.cpu.mainMemory.buttonsState[4] = '0';
                if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT1"))) {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE1"), true);
                }
                try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), false);
                } catch (Exception e){}
            } else {
                Main.cpu.mainMemory.buttonsState[4] = '1';
                if (!anyTogglePressed()) {
                    try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), true);
                } catch (Exception e){}
                }
            }
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle2.setOnAction(event -> {
            if (portToggle2.isSelected()) {
                Main.cpu.mainMemory.buttonsState[5] = '0';
                if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT1"))) {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE1"), true);
                }
                try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), false);
                } catch (Exception e){}
            } else {
                Main.cpu.mainMemory.buttonsState[5] = '1';
                if (!anyTogglePressed()) {
                    try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), true);
                } catch (Exception e){}
                }
            }
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle1.setOnAction(event -> {
            if (portToggle1.isSelected()) {
                Main.cpu.mainMemory.buttonsState[6] = '0';
                if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT1"))) {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE1"), true);
                }
                try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), false);
                } catch (Exception e){}
            } else {
                Main.cpu.mainMemory.buttonsState[6] = '1';
                if (!anyTogglePressed()) {
                    try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), true);
                } catch (Exception e){}
                }
            }
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle0.setOnAction(event -> {
            if (portToggle0.isSelected()) {
                Main.cpu.mainMemory.buttonsState[7] = '0';
                if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT1"))) {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE1"), true);
                }
                try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), false);
                } catch (Exception e){}
            } else {
                Main.cpu.mainMemory.buttonsState[7] = '1';
                if (!anyTogglePressed()) {
                    try {
                    Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(zadajnikiPrzerwania), true);
                } catch (Exception e){}
                }
            }
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });

        portButton7.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[0] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT0"))) {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE0"), true);
            } try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), false);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });
        portButton7.setOnMouseReleased(event -> {
            if (portToggle7.isSelected())
                Main.cpu.mainMemory.buttonsState[0] = '0';
            else
                Main.cpu.mainMemory.buttonsState[0] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), true);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });

        portButton6.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[1] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT0"))) {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE0"), true);
            }
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), false);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });
        portButton6.setOnMouseReleased(event -> {
            if (portToggle6.isSelected())
                Main.cpu.mainMemory.buttonsState[1] = '0';
            else
                Main.cpu.mainMemory.buttonsState[1] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), true);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });

        portButton5.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[2] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT0"))) {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE0"), true);
            }
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), false);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });
        portButton5.setOnMouseReleased(event -> {
            if (portToggle5.isSelected())
                Main.cpu.mainMemory.buttonsState[2] = '0';
            else
                Main.cpu.mainMemory.buttonsState[2] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), true);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });

        portButton4.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[3] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT0"))) {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE0"), true);
            }
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), false);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });
        portButton4.setOnMouseReleased(event -> {
            if (portToggle4.isSelected())
                Main.cpu.mainMemory.buttonsState[3] = '0';
            else
                Main.cpu.mainMemory.buttonsState[3] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), true);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });

        portButton3.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[4] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT0"))) {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE0"), true);
            }
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), false);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });
        portButton3.setOnMouseReleased(event -> {
            if (portToggle3.isSelected())
                Main.cpu.mainMemory.buttonsState[4] = '0';
            else
                Main.cpu.mainMemory.buttonsState[4] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), true);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });

        portButton2.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[5] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT0"))) {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE0"), true);
            }
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), false);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });
        portButton2.setOnMouseReleased(event -> {
            if (portToggle2.isSelected())
                Main.cpu.mainMemory.buttonsState[5] = '0';
            else
                Main.cpu.mainMemory.buttonsState[5] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), true);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });

        portButton1.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[6] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT0"))) {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE0"), true);
            }
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), false);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });
        portButton1.setOnMouseReleased(event -> {
            if (portToggle1.isSelected())
                Main.cpu.mainMemory.buttonsState[6] = '0';
            else
                Main.cpu.mainMemory.buttonsState[6] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), true);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });

        portButton0.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[7] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            if (!Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get("IT0"))) {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get("IE0"), true);
            }
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), false);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });
        portButton0.setOnMouseReleased(event -> {
            if (portToggle0.isSelected())
                Main.cpu.mainMemory.buttonsState[7] = '0';
            else
                Main.cpu.mainMemory.buttonsState[7] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            try {
                Main.cpu.mainMemory.setBit(Main.cpu.codeMemory.bitAddresses.get(przyciskiPrzerwania), true);
            } catch (Exception e) { }
            Main.cpu.refreshGui();
        });

        Label przyciskiLabel = new Label("Przyciski:");
        przyciskiLabel.setMaxWidth(Double.MAX_VALUE);
        przyciskiLabel.setAlignment(Pos.CENTER);
        przyciskiLabel.setFont(new Font("Arial", 14));

        Label zadajnikiLabel = new Label("Zadajniki:");
        zadajnikiLabel.setMaxWidth(Double.MAX_VALUE);
        zadajnikiLabel.setAlignment(Pos.CENTER);
        zadajnikiLabel.setFont(new Font("Arial", 14));

        BorderPane diodesBorderPane = new BorderPane();
        diodesBorderPane.setTop(seg7Canvas);

        VBox lowerBox = new VBox();

        diodesBorderPane.setBottom(lowerBox);

        HBox togglesHBox = new HBox();
        togglesHBox.setPadding(new Insets(10, 1, 10, 1));
        togglesHBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(portToggle0, Priority.NEVER);
        HBox.setHgrow(portToggle1, Priority.NEVER);
        HBox.setHgrow(portToggle2, Priority.NEVER);
        HBox.setHgrow(portToggle3, Priority.NEVER);
        HBox.setHgrow(portToggle4, Priority.NEVER);
        HBox.setHgrow(portToggle5, Priority.NEVER);
        HBox.setHgrow(portToggle6, Priority.NEVER);
        HBox.setHgrow(portToggle7, Priority.NEVER);

        togglesHBox.setSpacing(2);

        togglesHBox.getChildren().addAll(portToggle0, portToggle1, portToggle2, portToggle3, portToggle4, portToggle5, portToggle6, portToggle7);


        HBox buttonsHBox = new HBox();
        buttonsHBox.setPadding(new Insets(10, 1, 10, 1));
        buttonsHBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(portButton0, Priority.NEVER);
        HBox.setHgrow(portButton1, Priority.NEVER);
        HBox.setHgrow(portButton2, Priority.NEVER);
        HBox.setHgrow(portButton3, Priority.NEVER);
        HBox.setHgrow(portButton4, Priority.NEVER);
        HBox.setHgrow(portButton5, Priority.NEVER);
        HBox.setHgrow(portButton6, Priority.NEVER);
        HBox.setHgrow(portButton7, Priority.NEVER);

        buttonsHBox.setSpacing(2);

        buttonsHBox.getChildren().addAll(portButton0, portButton1, portButton2, portButton3, portButton4, portButton5, portButton6, portButton7);

        lowerBox.getChildren().addAll(zadajnikiLabel, togglesHBox, przyciskiLabel, buttonsHBox);

        final NumberAxis xAxis = new NumberAxis(0, 255, 64);
        final NumberAxis yAxis = new NumberAxis(0, 255, 64);
        BorderPane chartBorderPane = new BorderPane();
        //creating the chart
        final ScatterChart<Number, Number> lineChart =
                new ScatterChart<Number, Number>(xAxis, yAxis);
        lineChart.setLegendVisible(false);
        lineChart.setTitle("Przebieg - P0");
        series = new XYChart.Series();
        lineChart.getData().add(series);
        chartPane.setContent(chartBorderPane);
        chartBorderPane.setCenter(lineChart);
        chartPane.setContent(chartBorderPane);

        editorElementsGridPane.add(diodesBorderPane, 1, 0);

        Tab programMemoryInfoTab = new Tab("Pamięć Programu");
        programMemoryInfoTab.setClosable(false);

        programMemoryTextArea = new TextArea();
        programMemoryTextArea.setFont(new Font("Arial", 13));
        programMemoryTextArea.setEditable(false);
        programMemoryTextArea.setMaxWidth(530.0);

        BorderPane programMemoryBorderPane = new BorderPane();
        programMemoryBorderPane.setCenter(programMemoryTextArea);

        progrmMemoryLabel = new Label("Pamięć Programu");
        progrmMemoryLabel.setAlignment(Pos.CENTER);
        progrmMemoryLabel.setMaxWidth(Double.MAX_VALUE);
        programMemoryBorderPane.setTop(progrmMemoryLabel);
        programMemoryBorderPane.setCenter(programMemoryTextArea);

        programMemoryInfoTab.setContent(programMemoryBorderPane);

        StringBuilder content = new StringBuilder();
        content.append("\t 0\t");
        content.append(" 1\t");
        content.append(" 2\t");
        content.append(" 3\t");
        content.append(" 4\t");
        content.append(" 5\t");
        content.append(" 6\t");
        content.append(" 7\t");
        content.append(" 8\t");
        content.append(" 9\t");
        content.append(" A\t");
        content.append(" B\t");
        content.append(" C\t");
        content.append(" D\t");
        content.append(" E\t");
        content.append(" F\t");
        content.append("\n");

        for (int i = 0; i < 256; i++) {
            content.append(Integer.toHexString(i)).append("\t");
            for (int j = 0; j < 16; j++) {
                if (Integer.toHexString(Integer.parseInt(Main.cpu.codeMemory.getFromAddress(i * 16 + j), 2)).length() == 1) {
                    content.append(" " + "0").append(Integer.toHexString(Integer.parseInt(Main.cpu.codeMemory.getFromAddress(i * 16 + j), 2)).toUpperCase()).append(" ");
                } else
                    content.append(" ").append(Integer.toHexString(Integer.parseInt(Main.cpu.codeMemory.getFromAddress(i * 16 + j), 2)).toUpperCase()).append(" ");
                content.append("\t");
            }
            if (i != 256)
                content.append("\n");
        }

        programMemoryTextArea.setText(content.toString());


        Tab memoryInfoTab = new Tab("Pamięć RAM");
        memoryInfoTab.setClosable(false);
        simulatorTabPane.getTabs().addAll(memoryInfoTab);
        simulatorTabPane.getTabs().addAll(programMemoryInfoTab);

        GridPane mainMemoryGridPane = new GridPane();
        RowConstraints firstRow = new RowConstraints();
        firstRow.setPercentHeight(15);
        RowConstraints secondRow = new RowConstraints();
        secondRow.setPercentHeight(85);
        mainMemoryGridPane.getRowConstraints().addAll(firstRow, secondRow);

        ColumnConstraints columnInMemoryTab = new ColumnConstraints();
        columnInMemoryTab.setPercentWidth(5);
        for (int i = 0; i < 20; i++)
            mainMemoryGridPane.getColumnConstraints().addAll(columnInMemoryTab);

        lowerRamTextArea = new TextArea();
        lowerRamTextArea.setFont(new Font("Arial", 13));
        lowerRamTextArea.setEditable(false);
        mainMemoryGridPane.add(lowerRamTextArea, 1, 1, 8, 1);

        upperRawTextArea = new TextArea();
        upperRawTextArea.setFont(new Font("Arial", 13));
        upperRawTextArea.setEditable(false);
        mainMemoryGridPane.add(upperRawTextArea, 11, 1, 8, 1);

        Label lowerRamLabel = new Label("Ram 00-7F");
        lowerRamLabel.setAlignment(Pos.CENTER);
        lowerRamLabel.setMaxWidth(Double.MAX_VALUE);

        Label upperRamLabel = new Label("Ram 80-FF (Obszar SFR)");
        upperRamLabel.setAlignment(Pos.CENTER);
        upperRamLabel.setMaxWidth(Double.MAX_VALUE);

        mainMemoryGridPane.add(lowerRamLabel, 1, 0, 8, 1);
        mainMemoryGridPane.add(upperRamLabel, 11, 0, 8, 1);


        Label addressLabelInChangeMenu = new Label("Adres:");
        addressLabelInChangeMenu.setMaxWidth(Double.MAX_VALUE);
        addressLabelInChangeMenu.setAlignment(Pos.CENTER);
        Label valueLabelInChangeMenu = new Label("Wartość:");
        valueLabelInChangeMenu.setMaxWidth(Double.MAX_VALUE);
        valueLabelInChangeMenu.setAlignment(Pos.CENTER);

        addressInChangeValueTextField = new TextField();
        valueInChangeValueTextField = new TextField();
        addressInChangeValueTextField.setMaxWidth(Double.MAX_VALUE);
        addressInChangeValueTextField.setAlignment(Pos.CENTER);
        valueInChangeValueTextField.setMaxWidth(Double.MAX_VALUE);
        valueInChangeValueTextField.setAlignment(Pos.CENTER);
        changeValueInChangeValueButton = new Button("Ustaw");
        changeValueInChangeValueButton.setMaxWidth(Double.MAX_VALUE);
        changeValueInChangeValueButton.setDisable(true);
        changeValueInChangeValueButton.setOnAction(event -> {
            int adres = -1;
            int wartosc = -1;
            try {
                adres = Integer.parseInt(Main.cpu.codeMemory.make8DigitsStringFromNumber(addressInChangeValueTextField.getText()), 2);
            } catch (Exception ignored) {
            }
            try {
                wartosc = Integer.parseInt(Main.cpu.codeMemory.make8DigitsStringFromNumber(valueInChangeValueTextField.getText()), 2);
            } catch (Exception ignored) {
            }
            if (adres > 255) adres = -1;
            if (wartosc > 255) wartosc = -1;
            if (adres != -1 && wartosc != -1) {
                Main.cpu.mainMemory.put(adres, wartosc);
                Main.cpu.refreshGui();
            } else if (adres == -1 && wartosc != -1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd ustawiania wartości");
                alert.setHeaderText("Nie można było ustawić wybranej wartości.");
                alert.setContentText("Podany adres jest spoza zakresu ilości pamięci (00h-FFh)");
                alert.showAndWait();
            } else if (adres != 1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd ustawiania wartości");
                alert.setHeaderText("Nie można było ustawić wybranej wartości.");
                alert.setContentText("Podana wartość jest spoza zakresu możliwych wartości (00h-FFh)");
                alert.showAndWait();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd ustawiania wartości");
                alert.setHeaderText("Nie można było ustawić wybranej wartości.");
                alert.setContentText("Zarówno adres jak i wartość są z poza zakresu (00-FF)");
                alert.showAndWait();
            }
        });


        VBox changeValueInRamVBox = new VBox();
        VBox.setVgrow(addressLabelInChangeMenu, Priority.ALWAYS);
        VBox.setVgrow(valueLabelInChangeMenu, Priority.ALWAYS);
        changeValueInRamVBox.getChildren().addAll(addressLabelInChangeMenu, addressInChangeValueTextField, valueLabelInChangeMenu, valueInChangeValueTextField, changeValueInChangeValueButton);

        mainMemoryGridPane.add(changeValueInRamVBox, 9, 1, 2, 1);


        Tab portsStatus = new Tab("Stan Portów");
        simulatorTabPane.getTabs().add(portsStatus);
        portsStatus.setClosable(false);
        portsStatus.setContent(portsStatusCanvas);

        port0History = new char[portChartScale][8];
        for (int i = 0; i < portChartScale; i++) {
            port0History[i] = "11111111".toCharArray();
        }
        port1History = new char[portChartScale][8];
        for (int i = 0; i < portChartScale; i++) {
            port1History[i] = "11111111".toCharArray();
        }
        port2History = new char[portChartScale][8];
        for (int i = 0; i < portChartScale; i++) {
            port2History[i] = "11111111".toCharArray();
        }
        port3History = new char[portChartScale][8];
        for (int i = 0; i < portChartScale; i++) {
            port3History[i] = "11111111".toCharArray();
        }


        memoryInfoTab.setContent(mainMemoryGridPane);

        mainStage = primaryStage;
        mainStage.setTitle("8051 MCU Emulator - 0.12 Alpha");
        mainBorderPane.setCenter(mainGridPane);
        mainScene = new Scene(mainBorderPane, width, height);
        mainStage.getIcons().add(new Image(MainStage.class.getResourceAsStream("cpu.png")));
        mainScene.getStylesheets().add(MainStage.class.getResource("style.css").toExternalForm());
        mainStage.setScene(mainScene);
        mainStage.show();
        mainStage.setMinHeight(600);
        mainStage.setMinWidth(800);

        try {
            URL iconURL = MainStage.class.getResource("cpu.png");
            java.awt.Image image = new ImageIcon(iconURL).getImage();
             com.apple.eawt.Application.getApplication().setDockIconImage(image);
            // com.apple.eawt.Application.getApplication().setDockIconBadge("8051");
        } catch (Exception ignored) {

        }

        mainScene.setOnDragOver(event -> {
            if (event.getDragboard().hasFiles()) {
                event.acceptTransferModes(TransferMode.LINK);
            }
            event.consume();
        });

        mainScene.setOnDragDropped(event -> {
            Dragboard db = event.getDragboard();
            if(db.hasFiles()) {
                for(File file:db.getFiles()) {
                    StringBuilder textToSet = new StringBuilder();
                    try {
                        Scanner in = new Scanner(file);
                        while (in.hasNextLine())
                            textToSet.append(in.nextLine()).append("\n");
                        boolean breakFlag = false;
                        for(int i = 0; i < editorTabs.size();i++) {
                            if(editorTabs.get(i).ownTab.getText().equals(file.getName())) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("Błąd dodawania pliku");
                                alert.setHeaderText("Nie można było zaimportować pliku o nazwie '"  +file.getName()+"'");
                                alert.setContentText("Plik o tej nazwie już istnieje");
                                alert.showAndWait();
                                event.setDropCompleted(true);
                                event.consume();
                                breakFlag = true;
                            }
                        }

                        if(breakFlag)
                            continue;

                        String text = textToSet.substring(0,textToSet.length()-1);

                        editorTabs.add(new editorTab());
                        editorTabs.get(editorTabs.size() - 1).ownTextArea.setText(textToSet.substring(0, textToSet.length() - 1));
                        editorTabs.get(editorTabs.size() - 1).path = file.getPath();
                        editorTabs.get(editorTabs.size() - 1).ownTab.setText(file.getName());
                        editorTabs.get(editorTabs.size() - 1).previousText = editorTabs.get(editorTabs.size() - 1).ownTextArea.getText();
                        editorTabPane.getSelectionModel().selectLast();
                    } catch (Exception ignored) {
                    }
                    event.setDropCompleted(true);
                    event.consume();
                }
            }
        });

        Main.cpu.refreshGui();
        //resizeComponents();

        mainStage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
            drawFrame();
        });

        mainScene.heightProperty().addListener((observable, oldValue, newValue) -> {
            drawFrame();
        });
        mainScene.widthProperty().addListener((observable, oldValue, newValue) -> {
            drawFrame();
        });
        mainStage.setOnCloseRequest(event -> {

            boolean flag = false;
            for(int i = 0; i < editorTabs.size();i++) {
                if(editorTabs.get(i).edited) {
                    flag = true;
                    break;
                }
            }

            if(flag) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Potwierdzenie wyjścia");
                alert.setHeaderText("Masz niezapisane programy, na pewno chcesz wyjść?");
                alert.setContentText("Wszelkie niezapisane zmiany zostaną utracone i nie będzie możliwości i ich przywrócenia.");
                Optional<ButtonType> result = alert.showAndWait();
                if(result.get() == ButtonType.CANCEL) {
                    event.consume();
                    return;
                }
            }
            continuousRunFlag = false;
            Platform.exit();
            System.exit(0);
        });

        drawFrame();
    }

    private void resizeComponents() {
        double oneWidth = simulatorGridPane.getWidth() / 20.0;
        double oneHeight = mainStage.getHeight() / 3.0 / 10.0;
        double smaller = oneWidth < oneHeight ? oneWidth : oneHeight;
        lowerRamTextArea.setFont(new Font("Arial", smaller / 1.5));
        upperRawTextArea.setFont(new Font("Arial", smaller / 1.5));
        programMemoryTextArea.setFont(new Font("Arial", smaller / 1.5));
        r0Label.setFont(new Font("Arial", smaller / 2.0));
        r0TextField.setFont(new Font("Arial", smaller / 2.0));
        r1Label.setFont(new Font("Arial", smaller / 2.0));
        r1TextField.setFont(new Font("Arial", smaller / 2.0));
        r2Label.setFont(new Font("Arial", smaller / 2.0));
        r2TextField.setFont(new Font("Arial", smaller / 2.0));
        r3Label.setFont(new Font("Arial", smaller / 2.0));
        r3TextField.setFont(new Font("Arial", smaller / 2.0));
        r4Label.setFont(new Font("Arial", smaller / 2.0));
        r4TextField.setFont(new Font("Arial", smaller / 2.0));
        r5Label.setFont(new Font("Arial", smaller / 2.0));
        r5TextField.setFont(new Font("Arial", smaller / 2.0));
        r6Label.setFont(new Font("Arial", smaller / 2.0));
        r6TextField.setFont(new Font("Arial", smaller / 2.0));
        r7Label.setFont(new Font("Arial", smaller / 2.0));
        r7TextField.setFont(new Font("Arial", smaller / 2.0));

        accumulatorLabel.setFont(new Font("Arial", smaller / 2.0));
        accumulatorTextFieldBin.setFont(new Font("Arial", smaller / 2.0));
        accumulatorTextFieldDec.setFont(new Font("Arial", smaller / 2.0));
        accumulatorTextFieldHex.setFont(new Font("Arial", smaller / 2.0));

        bLabel.setFont(new Font("Arial", smaller / 2.0));
        bTextFieldBin.setFont(new Font("Arial", smaller / 2.0));
        bTextFieldDec.setFont(new Font("Arial", smaller / 2.0));
        bTextFieldHex.setFont(new Font("Arial", smaller / 2.0));

        cLabel.setFont(new Font("Arial", smaller / 2.0));
        cTextField.setFont(new Font("Arial", smaller / 2.0));
        acLabel.setFont(new Font("Arial", smaller / 2.0));
        acTextField.setFont(new Font("Arial", smaller / 2.0));
        f0Label.setFont(new Font("Arial", smaller / 2.0));
        f0TextField.setFont(new Font("Arial", smaller / 2.0));
        rs1Label.setFont(new Font("Arial", smaller / 2.0));
        rs1TextField.setFont(new Font("Arial", smaller / 2.0));
        rs0Label.setFont(new Font("Arial", smaller / 2.0));
        rs0TextField.setFont(new Font("Arial", smaller / 2.0));
        ovLabel.setFont(new Font("Arial", smaller / 2.0));
        ovTextField.setFont(new Font("Arial", smaller / 2.0));
        f1Label.setFont(new Font("Arial", smaller / 2.0));
        f1TextField.setFont(new Font("Arial", smaller / 2.0));
        pLabel.setFont(new Font("Arial", smaller / 2.0));
        pTextField.setFont(new Font("Arial", smaller / 2.0));

        p0Label.setFont(new Font("Arial", smaller / 2.0));
        p0TextField.setFont(new Font("Arial", smaller / 2.0));
        p1Label.setFont(new Font("Arial", smaller / 2.0));
        p1TextField.setFont(new Font("Arial", smaller / 2.0));
        p2Label.setFont(new Font("Arial", smaller / 2.0));
        p2TextField.setFont(new Font("Arial", smaller / 2.0));
        p3Label.setFont(new Font("Arial", smaller / 2.0));
        p3TextField.setFont(new Font("Arial", smaller / 2.0));

        timePassedLabel.setFont(new Font("Arial", smaller / 2.0));
        timePassedTextField.setFont(new Font("Arial", smaller / 2.0));
        pcLabel.setFont(new Font("Arial", smaller / 2.0));
        pcTextField.setFont(new Font("Arial", smaller / 2.0));

        EALabel.setFont(new Font("Arial", smaller / 2.0));
        EATextField.setFont(new Font("Arial", smaller / 2.0));
        ESLabel.setFont(new Font("Arial", smaller / 2.0));
        ESTextField.setFont(new Font("Arial", smaller / 2.0));
        ET1Label.setFont(new Font("Arial", smaller / 2.0));
        ET1TextField.setFont(new Font("Arial", smaller / 2.0));
        EX1Label.setFont(new Font("Arial", smaller / 2.0));
        EX1TextField.setFont(new Font("Arial", smaller / 2.0));
        ET0Label.setFont(new Font("Arial", smaller / 2.0));
        ET0TextField.setFont(new Font("Arial", smaller / 2.0));
        EX0Label.setFont(new Font("Arial", smaller / 2.0));
        EX0TextField.setFont(new Font("Arial", smaller / 2.0));

        T0HLabel.setFont(new Font("Arial", smaller / 2.0));
        T0HTextField.setFont(new Font("Arial", smaller / 2.0));
        T1HLabel.setFont(new Font("Arial", smaller / 2.0));
        T1HTextField.setFont(new Font("Arial", smaller / 2.0));
        T0LLabel.setFont(new Font("Arial", smaller / 2.0));
        T0LTextField.setFont(new Font("Arial", smaller / 2.0));
        T1LLabel.setFont(new Font("Arial", smaller / 2.0));
        T1LTtextField.setFont(new Font("Arial", smaller / 2.0));
        TMODLabel.setFont(new Font("Arial", smaller / 2.0));
        TMODTextField.setFont(new Font("Arial", smaller / 2.0));
        TCONLabel.setFont(new Font("Arial", smaller / 2.0));
        TCONTextField.setFont(new Font("Arial", smaller / 2.0));

        portsDesc.setFont(new Font("Arial", smaller / 2.0));
        portsDesc.setFont(new Font("Arial", smaller / 2.0));

        portButton0.setFont(new Font("Arial", smaller / 1.5));
        System.out.println(smaller / 1.5);
        portButton1.setFont(new Font("Arial", smaller / 1.5));
        portButton2.setFont(new Font("Arial", smaller / 1.5));
        portButton3.setFont(new Font("Arial", smaller / 1.5));
        portButton4.setFont(new Font("Arial", smaller / 1.5));
        portButton5.setFont(new Font("Arial", smaller / 1.5));
        portButton6.setFont(new Font("Arial", smaller / 1.5));
        portButton7.setFont(new Font("Arial", smaller / 1.5));

        portToggle0.setFont(new Font("Arial", smaller / 1.5));
        portToggle1.setFont(new Font("Arial", smaller / 1.5));
        portToggle2.setFont(new Font("Arial", smaller / 1.5));
        portToggle3.setFont(new Font("Arial", smaller / 1.5));
        portToggle4.setFont(new Font("Arial", smaller / 1.5));
        portToggle5.setFont(new Font("Arial", smaller / 1.5));
        portToggle6.setFont(new Font("Arial", smaller / 1.5));
        portToggle7.setFont(new Font("Arial", smaller / 1.5));

        compilationErrorsLabel.setFont(new Font("Arial", smaller / 2.0));

    }

    public MainStage(double width, double height) {
        this.height = height;
        this.width = width;
    }

    public void drawFrame() {
        double width = 240;
        double oneLedWidth = width / 8.0;

        height = 220;

        seg7Canvas.setWidth(width);
        seg7Canvas.setHeight(height);

        GraphicsContext gc = seg7Canvas.getGraphicsContext2D();

        Color mainBackgroud = Color.DARKGREEN;

        gc.setFill(mainBackgroud);
        gc.fillRect(0,0,width,height);

        gc.setStroke(Color.BLACK);
        gc.setLineWidth(5f);
        gc.strokeRect(0,0,width,height);

        double marginX = 10;
        double marginY = 80;
        double marginYUp = 20;
        height = 60;

       // gc.clearRect(0, 0, width, height);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.setFill(Color.RED);





        for (int i = 0; i < 8; i++) {
            String portName = ledsPort + "." + (7 - i);

            Color mainColor = Color.BLACK;

            if(ledsColor.equals("Czerwony")) {
                gc.setFill(Color.DARKRED);
                mainColor = Color.ORANGERED;
            }
            if(ledsColor.equals("Zielony")) {
                gc.setFill(Color.DARKGREEN);
                mainColor = Color.LIGHTGREEN;
            }
            if(ledsColor.equals("Niebieski")) {
                gc.setFill(Color.DARKBLUE);
                mainColor = Color.LIGHTBLUE;
            }

            double centerX = i * oneLedWidth + oneLedWidth / 2.0;
            double centerY = height / 2.0;
            double radius = (oneLedWidth >= height ? height - 2 : oneLedWidth) / 2.0 - 5;

            gc.setStroke(Color.YELLOW);

            gc.setLineWidth(4);

            gc.strokeLine(centerX-radius/2.0,centerY-2*radius-5,centerX-radius/2.0+2,centerY-2*radius);
            gc.strokeLine(centerX-radius/2.0,centerY+2*radius+5,centerX-radius/2.0+2,centerY+2*radius);

            gc.fillRect(centerX - radius,centerY-2*radius,radius+2,radius*4);

            if (Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get(portName)))
                if(ledsType.equals("Katoda"))
                    gc.setFill(mainColor);
                else
                    gc.setFill(Color.TRANSPARENT);
            else
                if(ledsType.equals("Katoda"))
                    gc.setFill(Color.TRANSPARENT);
                else
                    gc.setFill(mainColor);

            gc.fillRect(centerX - radius,centerY-radius,radius+2,radius*2);

            //gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        }

        height = 210.0;

        //gc.clearRect(0, 60, width, height);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        double shorter = 10;
        double longer = (height - marginYUp - marginY - 4 * shorter) / 2.0 + 10;

        if ((longer + shorter + shorter + marginX + 10) >= (width / 2))
            longer = width / 2 - shorter - shorter - marginX - 10;

        double breakHeight = (longer+longer+5*shorter - 2*shorter)/6;

        for(int i = 0; i < 7; i ++) {
            gc.setStroke(Color.YELLOW);
            gc.setLineWidth(4);

            gc.strokeLine(width/2.0 + longer + shorter + marginX+shorter+shorter,marginY+i*breakHeight,width/2.0 + longer + shorter + marginX+shorter+shorter+4,marginY+i*breakHeight);
            gc.strokeLine(width/2.0 - longer - shorter - marginX-shorter-shorter,marginY+i*breakHeight,width/2.0 - longer - shorter - marginX-shorter-shorter-4,marginY+i*breakHeight);

        }

        gc.setFill(Color.BLACK);

        Color seg7background = Color.web("0x454545");
        gc.setFill(seg7background);
        gc.fillRect(width / 2.0 - longer - shorter - marginX-shorter-shorter,marginY-shorter,longer+longer+6*shorter+marginX+marginX,longer+longer+5*shorter);


        //LICZBA PIERWSZA
        int[] wartosci;
        if(seg7ConnectionType==0) {
            String wartosc = microcontroller.Cpu.expandTo8Digits(Integer.toBinaryString(Main.cpu.mainMemory.get(seg7displayPort)));
            wartosci = Converters.bcdto7seg(wartosc.substring(0, 4));
        }
        else {
            String wartosc = microcontroller.Cpu.expandTo8Digits(Integer.toBinaryString(Main.cpu.mainMemory.get("P0")));
            wartosci = new int[8];
            for(int i = 0; i < 8;i++) {
                if(wartosc.charAt(7-i)=='0')
                    wartosci[i] = 0;
                else
                    wartosci[i] = 1;
            }
        }

        Color backgroud = Color.web("0x666666");

        Color seg7RealColor = Color.web("0x666666");

        if(seg7Color.equals("Czerwony")) {
            seg7RealColor = Color.RED;
        }
        if(seg7Color.equals("Zielony")) {
            seg7RealColor = Color.LIGHTGREEN;
        }
        if(seg7Color.equals("Niebieski")) {
            seg7RealColor = Color.LIGHTBLUE;
        }

        if (wartosci[0] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 - longer - shorter - marginX, marginY, longer, shorter);//a
        if (wartosci[6] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 - longer - shorter - marginX, marginY + longer + shorter, longer, shorter);//g
        if (wartosci[3] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 - longer - shorter - marginX, marginY + 2.0 * longer + 2.0 * shorter, longer, shorter);//d

        if (wartosci[5] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 - longer - shorter - marginX - shorter, marginY + shorter, shorter, longer);//f
        if (wartosci[4] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 - longer - shorter - marginX - shorter, marginY + shorter + longer + shorter, shorter, longer);//e

        if (wartosci[1] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 - marginX - shorter, marginY + shorter, shorter, longer);//b
        if (wartosci[2] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 - marginX - shorter, marginY + shorter + longer + shorter, shorter, longer);//c

        if(seg7ConnectionType==0) {
            String wartosc = microcontroller.Cpu.expandTo8Digits(Integer.toBinaryString(Main.cpu.mainMemory.get(seg7displayPort)));
            wartosci = Converters.bcdto7seg(wartosc.substring(4, 8));
        }
        else {
            String wartosc = microcontroller.Cpu.expandTo8Digits(Integer.toBinaryString(Main.cpu.mainMemory.get("P1")));
            wartosci = new int[8];
            for(int i = 0; i < 8;i++) {
                if(wartosc.charAt(7-i)=='0')
                    wartosci[i] = 0;
                else
                    wartosci[i] = 1;
            }
        }

        //LICZBA DRUGA
        if (wartosci[0] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 + shorter + marginX, marginY, longer, shorter);//a

        if (wartosci[6] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 + shorter + marginX, marginY + longer + shorter, longer, shorter);//g

        if (wartosci[3] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 + shorter + marginX, marginY + 2.0 * longer + 2.0 * shorter, longer, shorter);//d


        if (wartosci[1] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 + longer + shorter + marginX, marginY + shorter, shorter, longer);//b

        if (wartosci[2] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 + longer + shorter + marginX, marginY + shorter + longer + shorter, shorter, longer);//c


        if (wartosci[5] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 + marginX, marginY + shorter, shorter, longer);//f

        if (wartosci[4] == 1)
            gc.setFill(seg7RealColor);
        else
            gc.setFill(backgroud);
        gc.fillRect(width / 2.0 + marginX, marginY + shorter + longer + shorter, shorter, longer);//e

        width = mainStage.getWidth();
        //height = mainStage.getHeight() * (37.0 / 100.0);
        height = 200;
        portsStatusCanvas.setWidth(width);
        portsStatusCanvas.setHeight(height);
        gc = portsStatusCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        double upperMargin = 40;
        double XMargin = 30;
        double betweenMargin = 20;
        double breakValue = (height - upperMargin) / 12.0;
        gc.setStroke(Color.BLACK);
        double oneWidht = (width - 2 * XMargin - 3 * betweenMargin) / 4;
        gc.setLineWidth(1);
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < portChartScale; j++) {
                if (port0History[j][i] == '0') {
                    gc.setStroke(Color.GREEN);
                    gc.strokeLine(XMargin + (oneWidht / (portChartScale * 1.0)) * j, upperMargin + i * breakValue + breakValue / 3.0 * 2.0, XMargin + (oneWidht / (portChartScale * 1.0)) * (j + 1), upperMargin + i * breakValue + breakValue / 3.0 * 2.0);
                } else {
                    gc.setStroke(Color.RED);
                    gc.strokeLine(XMargin + (oneWidht / (portChartScale * 1.0)) * j, upperMargin + i * breakValue + breakValue / 3.0, XMargin + (oneWidht / (portChartScale * 1.0)) * (j + 1), upperMargin + i * breakValue + breakValue / 3.0);
                }

                if (port1History[j][i] == '0') {
                    gc.setStroke(Color.GREEN);
                    gc.strokeLine(XMargin + 1.0 * oneWidht + 1.0 * betweenMargin + (oneWidht / (portChartScale * 1.0)) * j, upperMargin + i * breakValue + breakValue / 3.0 * 2.0, XMargin + 1.0 * oneWidht + 1.0 * betweenMargin + (oneWidht / (portChartScale * 1.0)) * (j + 1), upperMargin + i * breakValue + breakValue / 3.0 * 2.0);
                } else {
                    gc.setStroke(Color.RED);
                    gc.strokeLine(XMargin + 1.0 * oneWidht + 1.0 * betweenMargin + (oneWidht / (portChartScale * 1.0)) * j, upperMargin + i * breakValue + breakValue / 3.0, XMargin + 1.0 * oneWidht + 1.0 * betweenMargin + (oneWidht / (portChartScale * 1.0)) * (j + 1), upperMargin + i * breakValue + breakValue / 3.0);
                }

                if (port2History[j][i] == '0') {
                    gc.setStroke(Color.GREEN);
                    gc.strokeLine(XMargin + 2.0 * oneWidht + 2.0 * betweenMargin + (oneWidht / (portChartScale * 1.0)) * j, upperMargin + i * breakValue + breakValue / 3.0 * 2.0, XMargin + 2.0 * oneWidht + 2.0 * betweenMargin + (oneWidht / (portChartScale * 1.0)) * (j + 1), upperMargin + i * breakValue + breakValue / 3.0 * 2.0);
                } else {
                    gc.setStroke(Color.RED);
                    gc.strokeLine(XMargin + 2.0 * oneWidht + 2.0 * betweenMargin + (oneWidht / (portChartScale * 1.0)) * j, upperMargin + i * breakValue + breakValue / 3.0, XMargin + 2.0 * oneWidht + 2.0 * betweenMargin + (oneWidht / (portChartScale * 1.0)) * (j + 1), upperMargin + i * breakValue + breakValue / 3.0);
                }

                if (port3History[j][i] == '0') {
                    gc.setStroke(Color.GREEN);
                    gc.strokeLine(XMargin + 3.0 * oneWidht + 3.0 * betweenMargin + (oneWidht / (portChartScale * 1.0)) * j, upperMargin + i * breakValue + breakValue / 3.0 * 2.0, XMargin + 3.0 * oneWidht + 3.0 * betweenMargin + (oneWidht / (portChartScale * 1.0)) * (j + 1), upperMargin + i * breakValue + breakValue / 3.0 * 2.0);
                } else {
                    gc.setStroke(Color.RED);
                    gc.strokeLine(XMargin + 3.0 * oneWidht + 3.0 * betweenMargin + (oneWidht / (portChartScale * 1.0)) * j, upperMargin + i * breakValue + breakValue / 3.0, XMargin + 3.0 * oneWidht + 3.0 * betweenMargin + (oneWidht / (portChartScale * 1.0)) * (j + 1), upperMargin + i * breakValue + breakValue / 3.0);
                }
            }
        }
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(XMargin, upperMargin, oneWidht, breakValue * 8);
        gc.strokeRect(XMargin + oneWidht + betweenMargin, upperMargin, oneWidht, breakValue * 8);
        gc.strokeRect(XMargin + 2.0 * oneWidht + 2.0 * betweenMargin, upperMargin, oneWidht, breakValue * 8);
        gc.strokeRect(XMargin + 3.0 * oneWidht + 3.0 * betweenMargin, upperMargin, oneWidht, breakValue * 8);

        gc.fillText("0", marginX, upperMargin + 8 * breakValue - breakValue / 2.0);
        gc.fillText("1", marginX, upperMargin + 7 * breakValue - breakValue / 2.0);
        gc.fillText("2", marginX, upperMargin + 6 * breakValue - breakValue / 2.0);
        gc.fillText("3", marginX, upperMargin + 5 * breakValue - breakValue / 2.0);
        gc.fillText("4", marginX, upperMargin + 4 * breakValue - breakValue / 2.0);
        gc.fillText("5", marginX, upperMargin + 3 * breakValue - breakValue / 2.0);
        gc.fillText("6", marginX, upperMargin + 2 * breakValue - breakValue / 2.0);
        gc.fillText("7", marginX, upperMargin + breakValue - breakValue / 2.0);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("PO", XMargin + oneWidht / 2, upperMargin / 2.0);
        gc.fillText("P1", XMargin + oneWidht + betweenMargin + oneWidht / 2, upperMargin / 2.0);
        gc.fillText("P2", XMargin + oneWidht + betweenMargin + oneWidht + betweenMargin + oneWidht / 2, upperMargin / 2.0);
        gc.fillText("P3", XMargin + oneWidht + betweenMargin + oneWidht + betweenMargin + oneWidht + betweenMargin + oneWidht / 2, upperMargin / 2.0);


        programImageCanvas.setWidth(buttonsBorderPane.getWidth());
        programImageCanvas.setHeight(buttonsBorderPane.getWidth()+25);

        //gc = programImageCanvas.getGraphicsContext2D();
        //gc.setStroke(Color.BLACK);
        //gc.setLineWidth(5);
        //gc.drawImage(new Image(MainStage.class.getResourceAsStream("cpu.png")),0,5,buttonsBorderPane.getWidth(),buttonsBorderPane.getWidth());
        //gc.strokeRoundRect(3.0/8.0 * buttonsBorderPane.getWidth() / 2.0,5,buttonsBorderPane.getWidth()-3.0/8.0 * buttonsBorderPane.getWidth(),buttonsBorderPane.getWidth(),10,10);

    }

    private Canvas seg7Canvas = new Canvas();
    private Canvas portsStatusCanvas = new Canvas();
    private Canvas programImageCanvas = new Canvas();

    private void setEditorText(ArrayList<String> toSet) {
        StringBuilder textToSet = new StringBuilder();
        for (int i = 0; i < toSet.size(); i++) {
            textToSet.append(toSet.get(i));
            if (i != toSet.size() - 1)
                textToSet.append("\n");
        }
        editorTabs.get(editorTabPane.getSelectionModel().getSelectedIndex()).ownTextArea.setText(textToSet.toString());
        //editorTextArea.setText(textToSet.toString());
    }

    private boolean anyTogglePressed() {
        for (int i = 0; i < Main.cpu.mainMemory.buttonsState.length; i++) {
            if (Main.cpu.mainMemory.buttonsState[i] == '0')
                return true;
        }
        return false;
    }

    private GridPane mainGridPane;
    private GridPane editorElementsGridPane;
    private GridPane simulatorGridPane;

    public char[][] port0History;
    public char[][] port1History;
    public char[][] port2History;
    public char[][] port3History;

    private BorderPane mainBorderPane;

    private double height;
    private double width;

    private Label accumulatorLabel;
    public Label accumulatorTextFieldHex;
    public Label accumulatorTextFieldBin;
    public Label accumulatorTextFieldDec;

    private Label bLabel;
    public Label bTextFieldHex;
    public Label bTextFieldBin;
    public Label bTextFieldDec;

    private Label r0Label;
    public Label r0TextField;

    private Label r1Label;
    public Label r1TextField;

    private Label r2Label;
    public Label r2TextField;

    private Label r3Label;
    public Label r3TextField;

    private Label r4Label;
    public Label r4TextField;

    private Label r5Label;
    public Label r5TextField;

    private Label r6Label;
    public Label r6TextField;

    private Label r7Label;
    public Label r7TextField;

    private Label p0Label;
    public Label p0TextField;

    private Label p1Label;
    public Label p1TextField;

    private Label p2Label;
    public Label p2TextField;

    private Label p3Label;
    public Label p3TextField;

    private Label pLabel;
    public Label pTextField;

    private Label ovLabel;
    public Label ovTextField;

    private Label rs0Label;
    public Label rs0TextField;

    private Label rs1Label;
    public Label rs1TextField;

    private Label f0Label;
    public Label f0TextField;

    private Label f1Label;
    public Label f1TextField;

    private Label acLabel;
    public Label acTextField;

    private Label cLabel;
    public Label cTextField;

    private Label timePassedLabel;
    public Label timePassedTextField;

    private Label pcLabel;
    public Label pcTextField;

    private Label T0LLabel;
    public Label T0LTextField;
    private Label T0HLabel;
    public Label T0HTextField;

    private Label T1LLabel;
    public Label T1LTtextField;
    private Label T1HLabel;
    public Label T1HTextField;

    private Label TMODLabel;
    public Label TMODTextField;

    private Label TCONLabel;
    public Label TCONTextField;

    private Label EX0Label;
    public Label EX0TextField;

    private Label ET0Label;
    public Label ET0TextField;

    private Label EX1Label;
    public Label EX1TextField;

    private Label ET1Label;
    public Label ET1TextField;

    private Label ESLabel;
    public Label ESTextField;

    private Label EALabel;
    public Label EATextField;

    private ToggleButton portToggle0;
    private ToggleButton portToggle1;
    private ToggleButton portToggle2;
    private ToggleButton portToggle3;
    private ToggleButton portToggle4;
    private ToggleButton portToggle5;
    private ToggleButton portToggle6;
    private ToggleButton portToggle7;

    private Button portButton0;
    private Button portButton1;
    private Button portButton2;
    private Button portButton3;
    private Button portButton4;
    private Button portButton5;
    private Button portButton6;
    private Button portButton7;

    public TextArea lowerRamTextArea;
    public TextArea upperRawTextArea;
    private TextArea programMemoryTextArea;

    public Label compilationErrorsLabel;

    private ComboBox<Integer> speedSelectComboBox;

    private Label portsDesc;
    private Label progrmMemoryLabel;


    private Button translateToMemoryButton;
    private Button stopSimulationButton;
    private Button oneStepButton;
    private Button continuousRunButton;

    //private TextArea editorTextArea;

    private Label speedSelectLabel;

    private Stage mainStage;
    private Scene mainScene;

    private boolean continuousRunFlag;

    private Task<Void> autoRun;

    private String lines[];

    private boolean running;

    private GridPane diodesPaneGridPane;

    String ledsPort = "P0";
    String seg7displayPort = "P1";
    String ledsType = "Katoda";
    String ledsColor = "Czerwony";
    String seg7Color = "Czerwony";
    int seg7ConnectionType  = 0;
    String przyciskiPrzerwania = "P3.2";
    String zadajnikiPrzerwania = "P3.3";


    private TextField addressInChangeValueTextField;
    private TextField valueInChangeValueTextField;

    private Button changeValueInChangeValueButton;

    public int portChartScale = 16;

    private MenuItem newFileMenuItem;
    private MenuItem saveFileMenuItem;
    private MenuItem saveAsFileMenuItem;
    private MenuItem importFileMenuItem;

    private XYChart.Series series;

    BorderPane buttonsBorderPane;

    private OscilloscopeStage OscilloscopePane = new OscilloscopeStage();

    private TabPane editorTabPane;

    private ArrayList<editorTab> editorTabs = new ArrayList<>();

    private String currentlyRunTabName = "";

    public String[] getLinesFromTabByName(String name) throws NoSuchElementException {
        int numerKarty = -1;
        for(int i =0; i < editorTabs.size();i++) {
            if(editorTabs.get(i).ownTab.getText().equals(name)) {
                numerKarty = i;
                break;
            }
            else if(editorTabs.get(i).ownTab.getText().substring(1).equals(name)) {
                numerKarty = i;
                break;
            }
        }
        if(numerKarty==-1)
            throw new NoSuchElementException();
        else
            return editorTabs.get(numerKarty).ownTextArea.getText().split("\n");

    }


    class editorTab {
        public editorTab() {
            this.path = "";
            this.ownTab = new Tab();
            ownTab.setClosable(true);


            int x = 1;
            for (int i = 0; i < editorTabs.size(); i++) {
                for (editorTab w : editorTabs) {
                    String name = "Untitled " + String.valueOf(x);
                    if (w.ownTab.getText().equals(name) || w.ownTab.getText().substring(1).equals(name)) {
                        x++;
                    } else
                        continue;
                }
            }
            ownTab.setText("Untitled " + String.valueOf(x));
            this.ownTextArea = new TextArea();
            ownTextArea.setPrefWidth(1000);

            ownTextArea = new TextArea();
            ownTab.setContent(ownTextArea);
            ownTextArea.setPrefColumnCount(1000);
            ownTextArea.setOnKeyReleased(event -> {
                if (!edited && !ownTextArea.getText().equals(previousText)) {
                    edited = true;
                    ownTab.setText("*" + ownTab.getText());
                }
                else if (edited && ownTextArea.getText().equals(previousText)) {
                    edited = false;
                    ownTab.setText(ownTab.getText().substring(1));
                }
            });
            ownTab.setOnCloseRequest(event -> {
                if(edited) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Potwierdzenie zamknięcia karty");
                    alert.setHeaderText("Zmiany nie zostały zapisane, na pewno chcesz zamknąć kartę?");
                    alert.setContentText("Wszelkie niezapisane zmiany zostaną utracone i nie będzie możliwości i ich przywrócenia.");
                    Optional<ButtonType> result = alert.showAndWait();
                    if (result.get() == ButtonType.CANCEL) {
                        event.consume();
                        return;
                    }
                }
                editorTabs.remove(this);
            });


            editorTabPane.getTabs().add(ownTab);
            this.edited = false;
        }

        public String previousText = "";
        public String path;
        public Tab ownTab;
        public TextArea ownTextArea;
        public boolean edited;
    }

}
