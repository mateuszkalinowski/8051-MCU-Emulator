package stages;

import converters.Converters;
import core.Main;
import exceptions.CompilingException;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
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
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
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
        simulatorGridPane.add(cLabel,1,5);

        cTextField = new Label("00h");
        cTextField.setMaxWidth(Double.MAX_VALUE);
        cTextField.setAlignment(Pos.CENTER);
        cTextField.setFont(new Font("Arial",11));
        cTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(cTextField,1,6);

        acLabel = new Label("AC");
        acLabel.setMaxWidth(Double.MAX_VALUE);
        acLabel.setAlignment(Pos.CENTER);
        acLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(acLabel,2,5);

        acTextField = new Label("00h");
        acTextField.setMaxWidth(Double.MAX_VALUE);
        acTextField.setAlignment(Pos.CENTER);
        acTextField.setFont(new Font("Arial",11));
        acTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(acTextField,2,6);

        f0Label = new Label("F0");
        f0Label.setMaxWidth(Double.MAX_VALUE);
        f0Label.setAlignment(Pos.CENTER);
        f0Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(f0Label,3,5);

        f0TextField = new Label("00h");
        f0TextField.setMaxWidth(Double.MAX_VALUE);
        f0TextField.setAlignment(Pos.CENTER);
        f0TextField.setFont(new Font("Arial",11));
        f0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(f0TextField,3,6);

        f1Label = new Label("F1");
        f1Label.setMaxWidth(Double.MAX_VALUE);
        f1Label.setAlignment(Pos.CENTER);
        f1Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(f1Label,7,5);

        f1TextField = new Label("00h");
        f1TextField.setMaxWidth(Double.MAX_VALUE);
        f1TextField.setAlignment(Pos.CENTER);
        f1TextField.setFont(new Font("Arial",11));
        f1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(f1TextField,7,6);

        rs1Label = new Label("RS1");
        rs1Label.setMaxWidth(Double.MAX_VALUE);
        rs1Label.setAlignment(Pos.CENTER);
        rs1Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(rs1Label,4,5);

        rs1TextField = new Label("00h");
        rs1TextField.setMaxWidth(Double.MAX_VALUE);
        rs1TextField.setAlignment(Pos.CENTER);
        rs1TextField.setFont(new Font("Arial",11));
        rs1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(rs1TextField,4,6);

        rs0Label = new Label("RS0");
        rs0Label.setMaxWidth(Double.MAX_VALUE);
        rs0Label.setAlignment(Pos.CENTER);
        rs0Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(rs0Label,5,5);

        rs0TextField = new Label("00h");
        rs0TextField.setMaxWidth(Double.MAX_VALUE);
        rs0TextField.setAlignment(Pos.CENTER);
        rs0TextField.setFont(new Font("Arial",11));
        rs0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(rs0TextField,5,6);

        ovLabel = new Label("OV");
        ovLabel.setMaxWidth(Double.MAX_VALUE);
        ovLabel.setAlignment(Pos.CENTER);
        ovLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(ovLabel,6,5);

        ovTextField = new Label("00h");
        ovTextField.setMaxWidth(Double.MAX_VALUE);
        ovTextField.setAlignment(Pos.CENTER);
        ovTextField.setFont(new Font("Arial",11));
        ovTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(ovTextField,6,6);

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

        portsDesc = new Label("Porty We/Wy:");
        portsDesc.setMaxWidth(Double.MAX_VALUE);
        portsDesc.setAlignment(Pos.CENTER);
        portsDesc.setFont(new Font("Arial",11));
        simulatorGridPane.add(portsDesc,16,0,3,2);

        /*Label pswLabel = new Label("REG\nPSW:");
        pswLabel.setMaxWidth(Double.MAX_VALUE);
        pswLabel.setAlignment(Pos.CENTER);
        pswLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(pswLabel,1,5,1,2);*/

        timePassedLabel = new Label("Czas Symulacji:");
        timePassedLabel.setMaxWidth(Double.MAX_VALUE);
        timePassedLabel.setAlignment(Pos.CENTER);
        timePassedLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(timePassedLabel,1,1,2,1);

        timePassedTextField = new Label("0 mkS");
        timePassedTextField.setMaxWidth(Double.MAX_VALUE);
        timePassedTextField.setAlignment(Pos.CENTER);
        timePassedTextField.setFont(new Font("Arial",11));
        timePassedTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(timePassedTextField,1,2,2,1);

        pcLabel = new Label("PC:");
        pcLabel.setMaxWidth(Double.MAX_VALUE);
        pcLabel.setAlignment(Pos.CENTER);
        pcLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(pcLabel,1,3,2,1);

        pcTextField = new Label("0h");
        pcTextField.setMaxWidth(Double.MAX_VALUE);
        pcTextField.setAlignment(Pos.CENTER);
        pcTextField.setFont(new Font("Arial",11));
        pcTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(pcTextField,1,4,2,1);

        T0LLabel = new Label("TL0");
        T0LLabel.setMaxWidth(Double.MAX_VALUE);
        T0LLabel.setAlignment(Pos.CENTER);
        T0LLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(T0LLabel,13,1);

        T0LTextField = new Label("0h");
        T0LTextField.setMaxWidth(Double.MAX_VALUE);
        T0LTextField.setAlignment(Pos.CENTER);
        T0LTextField.setFont(new Font("Arial",11));
        T0LTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(T0LTextField,13,2);

        T0HLabel = new Label("TH0");
        T0HLabel.setMaxWidth(Double.MAX_VALUE);
        T0HLabel.setAlignment(Pos.CENTER);
        T0HLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(T0HLabel,12,1);

        T0HTextField = new Label("0h");
        T0HTextField.setMaxWidth(Double.MAX_VALUE);
        T0HTextField.setAlignment(Pos.CENTER);
        T0HTextField.setFont(new Font("Arial",11));
        T0HTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(T0HTextField,12,2);

        T1LLabel = new Label("TL1");
        T1LLabel.setMaxWidth(Double.MAX_VALUE);
        T1LLabel.setAlignment(Pos.CENTER);
        T1LLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(T1LLabel,11,1);

        T1LTtextField = new Label("0h");
        T1LTtextField.setMaxWidth(Double.MAX_VALUE);
        T1LTtextField.setAlignment(Pos.CENTER);
        T1LTtextField.setFont(new Font("Arial",11));
        T1LTtextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(T1LTtextField,11,2);

        T1HLabel = new Label("TH1");
        T1HLabel.setMaxWidth(Double.MAX_VALUE);
        T1HLabel.setAlignment(Pos.CENTER);
        T1HLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(T1HLabel,10,1);

        T1HTextField = new Label("0h");
        T1HTextField.setMaxWidth(Double.MAX_VALUE);
        T1HTextField.setAlignment(Pos.CENTER);
        T1HTextField.setFont(new Font("Arial",11));
        T1HTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(T1HTextField,10,2);

        TMODLabel = new Label("TMOD:");
        TMODLabel.setMaxWidth(Double.MAX_VALUE);
        TMODLabel.setAlignment(Pos.CENTER);
        TMODLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(TMODLabel,10,3,1,2);

        TMODTextField = new Label("0h");
        TMODTextField.setMaxWidth(Double.MAX_VALUE);
        TMODTextField.setAlignment(Pos.CENTER);
        TMODTextField.setFont(new Font("Arial",11));
        TMODTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(TMODTextField,11,3,1,2);


        TCONLabel = new Label("TCON:");
        TCONLabel.setMaxWidth(Double.MAX_VALUE);
        TCONLabel.setAlignment(Pos.CENTER);
        TCONLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(TCONLabel,12,3,1,2);

        TCONTextField = new Label("0h");
        TCONTextField.setMaxWidth(Double.MAX_VALUE);
        TCONTextField.setAlignment(Pos.CENTER);
        TCONTextField.setFont(new Font("Arial",11));
        TCONTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(TCONTextField,13,3,1,2);

        EX0Label = new Label("EX0");
        EX0Label.setMaxWidth(Double.MAX_VALUE);
        EX0Label.setAlignment(Pos.CENTER);
        EX0Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(EX0Label,8,2);

        EX0TextField = new Label("0");
        EX0TextField.setMaxWidth(Double.MAX_VALUE);
        EX0TextField.setAlignment(Pos.CENTER);
        EX0TextField.setFont(new Font("Arial",11));
        EX0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(EX0TextField,8,3);

        ET0Label = new Label("ET0");
        ET0Label.setMaxWidth(Double.MAX_VALUE);
        ET0Label.setAlignment(Pos.CENTER);
        ET0Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(ET0Label,7,2);

        ET0TextField = new Label("0");
        ET0TextField.setMaxWidth(Double.MAX_VALUE);
        ET0TextField.setAlignment(Pos.CENTER);
        ET0TextField.setFont(new Font("Arial",11));
        ET0TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(ET0TextField,7,3);

        EX1Label = new Label("EX1");
        EX1Label.setMaxWidth(Double.MAX_VALUE);
        EX1Label.setAlignment(Pos.CENTER);
        EX1Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(EX1Label,6,2);

        EX1TextField = new Label("0");
        EX1TextField.setMaxWidth(Double.MAX_VALUE);
        EX1TextField.setAlignment(Pos.CENTER);
        EX1TextField.setFont(new Font("Arial",11));
        EX1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(EX1TextField,6,3);

        ET1Label = new Label("ET1");
        ET1Label.setMaxWidth(Double.MAX_VALUE);
        ET1Label.setAlignment(Pos.CENTER);
        ET1Label.setFont(new Font("Arial",11));
        simulatorGridPane.add(ET1Label,5,2);

        ET1TextField = new Label("0");
        ET1TextField.setMaxWidth(Double.MAX_VALUE);
        ET1TextField.setAlignment(Pos.CENTER);
        ET1TextField.setFont(new Font("Arial",11));
        ET1TextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(ET1TextField,5,3);

        ESLabel = new Label("ES");
        ESLabel.setMaxWidth(Double.MAX_VALUE);
        ESLabel.setAlignment(Pos.CENTER);
        ESLabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(ESLabel,4,2);

        ESTextField = new Label("0");
        ESTextField.setMaxWidth(Double.MAX_VALUE);
        ESTextField.setAlignment(Pos.CENTER);
        ESTextField.setFont(new Font("Arial",11));
        ESTextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(ESTextField,4,3);

        EALabel = new Label("EA");
        EALabel.setMaxWidth(Double.MAX_VALUE);
        EALabel.setAlignment(Pos.CENTER);
        EALabel.setFont(new Font("Arial",11));
        simulatorGridPane.add(EALabel,3,2);

        EATextField = new Label("0");
        EATextField.setMaxWidth(Double.MAX_VALUE);
        EATextField.setAlignment(Pos.CENTER);
        EATextField.setFont(new Font("Arial",11));
        EATextField.setStyle("-fx-background-color: white; -fx-background-radius: 10");
        simulatorGridPane.add(EATextField,3,3);





        translateToMemoryButton = new Button("Asemblacja");
        translateToMemoryButton.setOnAction(event -> {
            lines = editorTextArea.getText().split("\n");
            try {
                ArrayList<String> compilatedText = Main.cpu.codeMemory.setMemory(lines);
                Main.cpu.resetCpu();
                Main.cpu.refreshGui();
                translateToMemoryButton.setDisable(true);
                stopSimulationButton.setDisable(false);
                oneStepButton.setDisable(false);
                editorTextArea.setEditable(false);
                rysujRunButton.setDisable(true);
                continuousRunButton.setDisable(false);
                exportFileMenuItem.setDisable(true);
                importFileMenuItem.setDisable(true);
                running = true;
                changeValueInChangeValueButton.setDisable(false);
                setEditorText(compilatedText);

                if(portToggle7.isSelected())
                    Main.cpu.mainMemory.buttonsState[0] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[0] = '1';
                if(portToggle6.isSelected())
                    Main.cpu.mainMemory.buttonsState[1] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[1] = '1';
                if(portToggle5.isSelected())
                    Main.cpu.mainMemory.buttonsState[2] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[2] = '1';
                if(portToggle4.isSelected())
                    Main.cpu.mainMemory.buttonsState[3] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[3] = '1';
                if(portToggle3.isSelected())
                    Main.cpu.mainMemory.buttonsState[4] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[4] = '1';
                if(portToggle2.isSelected())
                    Main.cpu.mainMemory.buttonsState[5] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[5] = '1';
                if(portToggle1.isSelected())
                    Main.cpu.mainMemory.buttonsState[6] = '0';
                else
                    Main.cpu.mainMemory.buttonsState[6] = '1';
                if(portToggle0.isSelected())
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
                        content.append(i).append("\t");
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

                Main.cpu.mainMemory.putFromExternal(160);
                Main.cpu.refreshGui();

            }
            catch (CompilingException e) {
                Main.stage.compilationErrorsLabel.setText("Błąd: " + e.getMessage());
                Main.stage.compilationErrorsLabel.setStyle("-fx-background-color: red; -fx-background-radius: 10; -fx-background-insets: 0 20 0 20");

            }
        });

        stopSimulationButton = new Button("Reset");
        stopSimulationButton.setOnAction(event -> {
            translateToMemoryButton.setDisable(false);
            stopSimulationButton.setDisable(true);
            oneStepButton.setDisable(true);
            editorTextArea.setEditable(true);
            editorTextArea.setText("");
            rysujRunButton.setDisable(false);
            continuousRunFlag = false;
            continuousRunButton.setDisable(true);
            exportFileMenuItem.setDisable(false);
            importFileMenuItem.setDisable(false);
            port0History = new char[portChartScale][8];
            port1History = new char[portChartScale][8];
            port2History = new char[portChartScale][8];
            port3History = new char[portChartScale][8];
            for(int i = 0; i < portChartScale;i++) {
                port0History[i] = "11111111".toCharArray();
            }
            for(int i = 0; i < portChartScale;i++) {
                port1History[i] = "11111111".toCharArray();
            }
            for(int i = 0; i < portChartScale;i++) {
                port2History[i] = "11111111".toCharArray();
            }
            for(int i = 0; i < portChartScale;i++) {
                port3History[i] = "11111111".toCharArray();
            }
            continuousRunButton.setText("Praca Ciągła");
            running = false;
            changeValueInChangeValueButton.setDisable(true);
            StringBuilder textToSet = new StringBuilder();
            for(String line : lines) {
                textToSet.append(line).append("\n");
            }
            editorTextArea.setText(textToSet.substring(0,textToSet.length()-1));
            Main.cpu.resetCpu();
            Main.cpu.refreshGui();
            compilationErrorsLabel.setText("");
            compilationErrorsLabel.setStyle("-fx-background-color: white; -fx-background-radius: 10; -fx-background-insets: 0 20 0 20");
        });

        oneStepButton = new Button("Krok");
        oneStepButton.setOnAction(event -> {
            try {
                Main.cpu.executeInstruction();
                Main.cpu.refreshGui();
            }
            catch (Exception e){
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
            if(continuousRunButton.getText().equals("Praca Ciągła")) {
                continuousRunButton.setText("Stop");
                changeValueInChangeValueButton.setDisable(true);

                oneStepButton.setDisable(true);
                autoRun = new Task<Void>() {
                    @Override
                    protected Void call() throws Exception {
                        continuousRunFlag=true;
                        continuousRunFlag=true;
                        long time = System.nanoTime();
                        while(continuousRunFlag) {
                            if(System.nanoTime() - time > 1000000000/speedSelectComboBox.getSelectionModel().getSelectedItem()) {
                                Main.cpu.executeInstruction();
                                time = System.nanoTime();
                                Platform.runLater(() -> Main.cpu.refreshGui());
                            }
                        }
                        return null;
                    }
                };
                Thread continuousRunThread = new Thread(autoRun);
                continuousRunThread.start();
            }
            else {
                continuousRunFlag = false;
                oneStepButton.setDisable(false);
                continuousRunButton.setText("Praca Ciągła");
                changeValueInChangeValueButton.setDisable(false);
            }
        });

        translateToMemoryButton.setMaxWidth(Double.MAX_VALUE);
        stopSimulationButton.setMaxWidth(Double.MAX_VALUE);
        stopSimulationButton.setDisable(true);
        continuousRunButton.setMaxWidth(Double.MAX_VALUE);
        oneStepButton.setMaxWidth(Double.MAX_VALUE);
        oneStepButton.setDisable(true);

        speedSelectComboBox = new ComboBox<>();
        speedSelectComboBox.getItems().addAll(1,5,10,50,100,500,1000,2000);
        speedSelectComboBox.setMaxWidth(Double.MAX_VALUE);
        speedSelectComboBox.getSelectionModel().selectFirst();

        speedSelectLabel = new Label("Prędkość Symulacji:");
        speedSelectLabel.setMaxWidth(Double.MAX_VALUE);
        speedSelectLabel.setAlignment(Pos.CENTER);
        speedSelectLabel.setFont(new Font("Arial",11));

        HBox buttonBox = new HBox();
        buttonBox.setPadding(new Insets(0,10,0,10));
        buttonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(translateToMemoryButton,Priority.ALWAYS);
        HBox.setHgrow(stopSimulationButton,Priority.ALWAYS);
        HBox.setHgrow(oneStepButton,Priority.ALWAYS);
        HBox.setHgrow(continuousRunButton,Priority.ALWAYS);
        buttonBox.setSpacing(5);

        buttonBox.getChildren().addAll(translateToMemoryButton,stopSimulationButton,oneStepButton,speedSelectLabel,speedSelectComboBox,continuousRunButton);
        infoEditorAndButtonGridPane.add(buttonBox,0,18,1,2);

        MenuBar mainMenuBar = new MenuBar();
        mainBorderPane.setTop(mainMenuBar);

        Menu menuFile = new Menu("Plik");
        mainMenuBar.getMenus().add(menuFile);

        Menu menuOptions = new Menu("Konfiguracja");
        mainMenuBar.getMenus().add(menuOptions);

        Menu menuMemory = new Menu("Pamięć");
        mainMenuBar.getMenus().add(menuMemory);

        Menu menuLowRam = new Menu("Low RAM");
        menuMemory.getItems().add(menuLowRam);

        MenuItem exportLowRamMenuItem = new MenuItem("Eksportuj");
        exportLowRamMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser chooseFile = new FileChooser();
                chooseFile.setTitle("Wybierz lokalizację zapisu");
                chooseFile.setInitialFileName("memory.txt");
                chooseFile.setInitialDirectory(new File(System.getProperty("user.home")));
                File saveFile = chooseFile.showSaveDialog(primaryStage);
                if(saveFile!=null) {
                    try {
                        PrintWriter in = new PrintWriter(saveFile);
                        String text = "";
                        for(int i = 0; i < 128;i++) {
                            text =  Main.cpu.codeMemory.make8DigitsStringFromNumber(String.valueOf(Main.cpu.mainMemory.get(i)));
                            in.println(text);
                        }
                        in.close();
                    } catch (FileNotFoundException e) {
                        System.out.println("File error");
                    }
                }
            }
        });
        MenuItem importLowRawMenuItem = new MenuItem("Importuj");
        importLowRawMenuItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                FileChooser chooseFile = new FileChooser();
                chooseFile.setTitle("Wybierz plik z pamięcią");
                File openFile = chooseFile.showOpenDialog(primaryStage);
                if (openFile != null) {
                    String number = "";
                    try {
                        Scanner in = new Scanner(openFile);
                        int i = 0;
                        while (in.hasNextLine()) {
                            number = in.nextLine();
                            try {
                                int w = Integer.valueOf(number, 2);
                                Main.cpu.mainMemory.put(i, w);
                                i++;
                            }
                            catch (NumberFormatException ignored) {
                            }

                        }
                        Main.cpu.refreshGui();
                    } catch (FileNotFoundException ignored) {
                    }
                }
            }
        });
        menuLowRam.getItems().addAll(exportLowRamMenuItem,importLowRawMenuItem);





        MenuItem paneConfigurationMenuItem = new MenuItem("Panel");
        menuOptions.getItems().add(paneConfigurationMenuItem);
        paneConfigurationMenuItem.setOnAction(event -> {
            PaneConfigStage paneConfigStage = new PaneConfigStage();
            try {
                paneConfigStage.start(primaryStage);
            }
            catch (Exception ignored) {
            }
        });

        MenuItem exitMenuItem = new MenuItem("Zamknij");
        exitMenuItem.setOnAction(event -> System.exit(0));
        exitMenuItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        importFileMenuItem = new MenuItem("Otwórz");
        importFileMenuItem.setOnAction(event -> {
            if(!running) {
                FileChooser chooseFile = new FileChooser();
                chooseFile.setTitle("Wybierz plik z programem");
                File openFile = chooseFile.showOpenDialog(primaryStage);
                if (openFile != null) {
                    StringBuilder textToSet = new StringBuilder();
                    try {
                        Scanner in = new Scanner(openFile);
                        while (in.hasNextLine())
                            textToSet.append(in.nextLine()).append("\n");
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
        });
        exportFileMenuItem = new MenuItem("Zapisz jako");
        exportFileMenuItem.setOnAction(event -> {
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
        columnInDiodesPane.setPercentWidth(100.0/8.0);
        for (int i = 0; i < 8;i++) {
            diodesPaneGridPane.getColumnConstraints().add(columnInDiodesPane);
        }
        RowConstraints rowInDiodesPane = new RowConstraints();
        rowInDiodesPane.setPercentHeight(10);
        for(int i = 0; i < 10; i++)
            diodesPaneGridPane.getRowConstraints().add(rowInDiodesPane);



        portToggle0 = new ToggleButton("0");
        portToggle1 = new ToggleButton("1");
        portToggle2 = new ToggleButton("2");
        portToggle3 = new ToggleButton("3");
        portToggle4 = new ToggleButton("4");
        portToggle5 = new ToggleButton("5");
        portToggle6 = new ToggleButton("6");
        portToggle7 = new ToggleButton("7");

        portToggle0.setMaxWidth(Double.MAX_VALUE);
        portToggle1.setMaxWidth(Double.MAX_VALUE);
        portToggle2.setMaxWidth(Double.MAX_VALUE);
        portToggle3.setMaxWidth(Double.MAX_VALUE);
        portToggle4.setMaxWidth(Double.MAX_VALUE);
        portToggle5.setMaxWidth(Double.MAX_VALUE);
        portToggle6.setMaxWidth(Double.MAX_VALUE);
        portToggle7.setMaxWidth(Double.MAX_VALUE);

        portButton0 = new Button("0");
        portButton1 = new Button("1");
        portButton2 = new Button("2");
        portButton3 = new Button("3");
        portButton4 = new Button("4");
        portButton5 = new Button("5");
        portButton6 = new Button("6");
        portButton7 = new Button("7");

        portButton0.setMaxWidth(Double.MAX_VALUE);
        portButton1.setMaxWidth(Double.MAX_VALUE);
        portButton2.setMaxWidth(Double.MAX_VALUE);
        portButton3.setMaxWidth(Double.MAX_VALUE);
        portButton4.setMaxWidth(Double.MAX_VALUE);
        portButton5.setMaxWidth(Double.MAX_VALUE);
        portButton6.setMaxWidth(Double.MAX_VALUE);
        portButton7.setMaxWidth(Double.MAX_VALUE);

        portToggle7.setOnAction(event -> {
            if(portToggle7.isSelected())
                Main.cpu.mainMemory.buttonsState[0] = '0';
            else
                Main.cpu.mainMemory.buttonsState[0] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle6.setOnAction(event -> {
            if(portToggle6.isSelected())
                Main.cpu.mainMemory.buttonsState[1] = '0';
            else
                Main.cpu.mainMemory.buttonsState[1] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle5.setOnAction(event -> {
            if(portToggle5.isSelected())
                Main.cpu.mainMemory.buttonsState[2] = '0';
            else
                Main.cpu.mainMemory.buttonsState[2] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle4.setOnAction(event -> {
            if(portToggle4.isSelected())
                Main.cpu.mainMemory.buttonsState[3] = '0';
            else
                Main.cpu.mainMemory.buttonsState[3] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle3.setOnAction(event -> {
            if(portToggle3.isSelected())
                Main.cpu.mainMemory.buttonsState[4] = '0';
            else
                Main.cpu.mainMemory.buttonsState[4] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle2.setOnAction(event -> {
            if(portToggle2.isSelected())
                Main.cpu.mainMemory.buttonsState[5] = '0';
            else
                Main.cpu.mainMemory.buttonsState[5] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle1.setOnAction(event -> {
            if(portToggle1.isSelected())
                Main.cpu.mainMemory.buttonsState[6] = '0';
            else
                Main.cpu.mainMemory.buttonsState[6] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portToggle0.setOnAction(event -> {
            if(portToggle0.isSelected())
                Main.cpu.mainMemory.buttonsState[7] = '0';
            else
                Main.cpu.mainMemory.buttonsState[7] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });

        portButton7.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[0] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portButton7.setOnMouseReleased(event -> {
            if(portToggle7.isSelected())
                Main.cpu.mainMemory.buttonsState[0] = '0';
            else
                Main.cpu.mainMemory.buttonsState[0] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });

        portButton6.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[1] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portButton6.setOnMouseReleased(event -> {
            if(portToggle6.isSelected())
                Main.cpu.mainMemory.buttonsState[1] = '0';
            else
                Main.cpu.mainMemory.buttonsState[1] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });

        portButton5.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[2] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portButton5.setOnMouseReleased(event -> {
            if(portToggle5.isSelected())
                Main.cpu.mainMemory.buttonsState[2] = '0';
            else
                Main.cpu.mainMemory.buttonsState[2] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });

        portButton4.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[3] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portButton4.setOnMouseReleased(event -> {
            if(portToggle4.isSelected())
                Main.cpu.mainMemory.buttonsState[3] = '0';
            else
                Main.cpu.mainMemory.buttonsState[3] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });

        portButton3.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[4] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portButton3.setOnMouseReleased(event -> {
            if(portToggle3.isSelected())
                Main.cpu.mainMemory.buttonsState[4] = '0';
            else
                Main.cpu.mainMemory.buttonsState[4] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });

        portButton2.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[5] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portButton2.setOnMouseReleased(event -> {
            if(portToggle2.isSelected())
                Main.cpu.mainMemory.buttonsState[5] = '0';
            else
                Main.cpu.mainMemory.buttonsState[5] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });

        portButton1.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[6] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portButton1.setOnMouseReleased(event -> {
            if(portToggle1.isSelected())
                Main.cpu.mainMemory.buttonsState[6] = '0';
            else
                Main.cpu.mainMemory.buttonsState[6] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });

        portButton0.setOnMousePressed(event -> {
            Main.cpu.mainMemory.buttonsState[7] = '0';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });
        portButton0.setOnMouseReleased(event -> {
            if(portToggle0.isSelected())
                Main.cpu.mainMemory.buttonsState[7] = '0';
            else
                Main.cpu.mainMemory.buttonsState[7] = '1';
            Main.cpu.mainMemory.putFromExternal(160);
            Main.cpu.refreshGui();
        });

        Label przyciskiLabel = new Label("Przyciski:");
        przyciskiLabel.setMaxWidth(Double.MAX_VALUE);
        przyciskiLabel.setAlignment(Pos.CENTER);
        przyciskiLabel.setFont(new Font("Arial",14));

        Label zadajnikiLabel = new Label("Zadajniki:");
        zadajnikiLabel.setMaxWidth(Double.MAX_VALUE);
        zadajnikiLabel.setAlignment(Pos.CENTER);
        zadajnikiLabel.setFont(new Font("Arial",14));

        diodesPaneGridPane.add(portButton0,7,7);
        diodesPaneGridPane.add(portButton1,6,7);
        diodesPaneGridPane.add(portButton2,5,7);
        diodesPaneGridPane.add(portButton3,4,7);
        diodesPaneGridPane.add(portButton4,3,7);
        diodesPaneGridPane.add(portButton5,2,7);
        diodesPaneGridPane.add(portButton6,1,7);
        diodesPaneGridPane.add(portButton7,0,7);

        diodesPaneGridPane.add(portToggle0,7,9);
        diodesPaneGridPane.add(portToggle1,6,9);
        diodesPaneGridPane.add(portToggle2,5,9);
        diodesPaneGridPane.add(portToggle3,4,9);
        diodesPaneGridPane.add(portToggle4,3,9);
        diodesPaneGridPane.add(portToggle5,2,9);
        diodesPaneGridPane.add(portToggle6,1,9);
        diodesPaneGridPane.add(portToggle7,0,9);

        diodesPaneGridPane.add(ledCanvas,0,0,8,2);
        diodesPaneGridPane.add(seg7Canvas,0,2,8,4);
        diodesPaneGridPane.add(zadajnikiLabel,0,8,8,1);
        diodesPaneGridPane.add(przyciskiLabel,0,6,8,1);
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

        //editorElementsGridPane.add(diodesPaneGridPane,1,0);
        editorElementsGridPane.add(elementsTabPane,1,0);

        rysujRunButton = new Button("Generuj Przebieg");
        rysujRunButton.setDisable(false);
        rysujRunButton.setOnAction(event -> {

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
                    catch (Exception ignored) { }
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
        });

        HBox generateButtonBox = new HBox();
        generateButtonBox.setPadding(new Insets(10,10,10,10));
        generateButtonBox.setAlignment(Pos.CENTER);
        HBox.setHgrow(rysujRunButton,Priority.ALWAYS);
        generateButtonBox.setSpacing(5);

        generateButtonBox.getChildren().addAll(rysujRunButton);

        chartBorderPane.setBottom(generateButtonBox);


        Tab programMemoryInfoTab = new Tab("Pamięć Programu");
        programMemoryInfoTab.setClosable(false);

        GridPane programMemoryGridPane = new GridPane();
        RowConstraints firstRowInProgramMemory = new RowConstraints();
        firstRowInProgramMemory.setPercentHeight(15);
        RowConstraints secondRowInProgramMemory = new RowConstraints();
        secondRowInProgramMemory.setPercentHeight(85);
        programMemoryGridPane.getRowConstraints().addAll(firstRowInProgramMemory,secondRowInProgramMemory);

        ColumnConstraints columnInProgramMemoryTab = new ColumnConstraints();
        columnInProgramMemoryTab.setPercentWidth(5);
        for(int i = 0; i < 20; i++)
            programMemoryGridPane.getColumnConstraints().addAll(columnInProgramMemoryTab);

        programMemoryTextArea = new TextArea();
        programMemoryTextArea.setFont(new Font("Arial",13));
        programMemoryTextArea.setEditable(false);
        programMemoryGridPane.add(programMemoryTextArea,4,1,12,1);

        progrmMemoryLabel = new Label("Pamięć Programu");
        progrmMemoryLabel.setAlignment(Pos.CENTER);
        progrmMemoryLabel.setMaxWidth(Double.MAX_VALUE);
        programMemoryGridPane.add(progrmMemoryLabel,4,0,12,1);

        programMemoryInfoTab.setContent(programMemoryGridPane);

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

        for(int i = 0; i < 128;i++) {
            content.append(i).append("\t");
            for(int j = 0; j < 16;j++) {
                if(Integer.toHexString(Integer.parseInt(Main.cpu.codeMemory.getFromAddress(i*16+j),2)).length()==1) {
                    content.append(" " + "0").append(Integer.toHexString(Integer.parseInt(Main.cpu.codeMemory.getFromAddress(i * 16 + j), 2)).toUpperCase()).append(" ");
                }
                else
                    content.append(" ").append(Integer.toHexString(Integer.parseInt(Main.cpu.codeMemory.getFromAddress(i * 16 + j), 2)).toUpperCase()).append(" ");
                content.append("\t");
            }
            if(i!=127)
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
        mainMemoryGridPane.getRowConstraints().addAll(firstRow,secondRow);

        ColumnConstraints columnInMemoryTab = new ColumnConstraints();
        columnInMemoryTab.setPercentWidth(5);
        for(int i = 0; i < 20; i++)
            mainMemoryGridPane.getColumnConstraints().addAll(columnInMemoryTab);

        lowerRamTextArea = new TextArea();
        lowerRamTextArea.setFont(new Font("Arial",13));
        lowerRamTextArea.setEditable(false);
        mainMemoryGridPane.add(lowerRamTextArea,1,1,8,1);

        upperRawTextArea = new TextArea();
        upperRawTextArea.setFont(new Font("Arial",13));
        upperRawTextArea.setEditable(false);
        mainMemoryGridPane.add(upperRawTextArea,11,1,8,1);

       /* Text text1 = new Text("Big italic red text\n");
        text1.setFill(Color.RED);
        text1.setFont(Font.font("Helvetica", FontPosture.ITALIC, 40));
        Text text2 = new Text(" little bold blue text");
        text2.setFill(Color.BLUE);
        text2.setFont(Font.font("Helvetica", FontWeight.BOLD, 10));
        TextFlow textFlow = new TextFlow(text1, text2);
        mainMemoryGridPane.add(textFlow,11,1,9,1);*/

        Label lowerRamLabel = new Label("Ram 00-7F");
        lowerRamLabel.setAlignment(Pos.CENTER);
        lowerRamLabel.setMaxWidth(Double.MAX_VALUE);

        Label upperRamLabel = new Label("Ram 80-FF (Obszar SFR)");
        upperRamLabel.setAlignment(Pos.CENTER);
        upperRamLabel.setMaxWidth(Double.MAX_VALUE);

        mainMemoryGridPane.add(lowerRamLabel,1,0,8,1);
        mainMemoryGridPane.add(upperRamLabel,11,0,8,1);


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
            }
            catch (Exception ignored){
            }
            try {
                wartosc = Integer.parseInt(Main.cpu.codeMemory.make8DigitsStringFromNumber(valueInChangeValueTextField.getText()), 2);
            }
            catch (Exception ignored){
            }
            if(adres>255)adres=-1;
            if(wartosc>255)wartosc=-1;
            if(adres!=-1 && wartosc!=-1) {
                Main.cpu.mainMemory.put(adres,wartosc);
                Main.cpu.refreshGui();
            }
            else if(adres==-1 && wartosc !=-1) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd ustawiania wartości");
                alert.setHeaderText("Nie można było ustawić wybranej wartości.");
                alert.setContentText("Podany adres jest spoza zakresu ilości pamięci (00h-FFh)");
                alert.showAndWait();
            }
            else if(adres!=1 ) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd ustawiania wartości");
                alert.setHeaderText("Nie można było ustawić wybranej wartości.");
                alert.setContentText("Podana wartość jest spoza zakresu możliwych wartości (00h-FFh)");
                alert.showAndWait();
            }
            else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Błąd ustawiania wartości");
                alert.setHeaderText("Nie można było ustawić wybranej wartości.");
                alert.setContentText("Zarówno adres jak i wartość są z poza zakresu (00-FF)");
                alert.showAndWait();
            }
        });


        VBox changeValueInRamVBox = new VBox();
        VBox.setVgrow(addressLabelInChangeMenu,Priority.ALWAYS);
        VBox.setVgrow(valueLabelInChangeMenu,Priority.ALWAYS);
        changeValueInRamVBox.getChildren().addAll(addressLabelInChangeMenu,addressInChangeValueTextField,valueLabelInChangeMenu,valueInChangeValueTextField,changeValueInChangeValueButton);

        mainMemoryGridPane.add(changeValueInRamVBox,9,1,2,1);


        Tab portsStatus = new Tab("Stan Portów");
        simulatorTabPane.getTabs().add(portsStatus);
        portsStatus.setClosable(false);
        portsStatus.setContent(portsStatusCanvas);

        port0History = new char[portChartScale][8];
        for(int i = 0; i < portChartScale;i++) {
            port0History[i] = "11111111".toCharArray();
        }
        port1History = new char[portChartScale][8];
        for(int i = 0; i < portChartScale;i++) {
            port1History[i] = "11111111".toCharArray();
        }
        port2History = new char[portChartScale][8];
        for(int i = 0; i < portChartScale;i++) {
            port2History[i] = "11111111".toCharArray();
        }
        port3History = new char[portChartScale][8];
        for(int i = 0; i < portChartScale;i++) {
            port3History[i] = "11111111".toCharArray();
        }


        memoryInfoTab.setContent(mainMemoryGridPane);

        mainStage = primaryStage;
        mainStage.setTitle("8051 MCU Emulator - 0.6 Alpha");
        mainBorderPane.setCenter(mainGridPane);
        mainScene = new Scene(mainBorderPane,width,height);
        mainScene.getStylesheets().add(MainStage.class.getResource("style.css").toExternalForm());
        mainStage.setScene(mainScene);
        mainStage.show();
        mainStage.setMinHeight(600);
        mainStage.setMinWidth(800);
        Main.cpu.refreshGui();
        resizeComponents();

        mainStage.fullScreenProperty().addListener((observable, oldValue, newValue) -> {
            drawFrame();
            resizeComponents();
        });

        mainScene.heightProperty().addListener((observable, oldValue, newValue) -> {
            drawFrame();
            resizeComponents();
        });
        mainScene.widthProperty().addListener((observable, oldValue, newValue) -> {
            drawFrame();
            resizeComponents();
        });
        mainStage.setOnCloseRequest(event -> continuousRunFlag=false);
        drawFrame();
    }


    private void resizeComponents(){
        double oneWidth =simulatorGridPane.getWidth()/20.0;
        double oneHeight = mainStage.getHeight()/3.0/10.0;
        double smaller = oneWidth < oneHeight ? oneWidth : oneHeight;
        lowerRamTextArea.setFont(new Font("Arial", smaller/1.5));
        upperRawTextArea.setFont(new Font("Arial", smaller/1.5));
        programMemoryTextArea.setFont(new Font("Arial",smaller/1.5));
        r0Label.setFont(new Font("Arial", smaller/2.0));
        r0TextField.setFont(new Font("Arial",smaller/2.0));
        r1Label.setFont(new Font("Arial", smaller/2.0));
        r1TextField.setFont(new Font("Arial",smaller/2.0));
        r2Label.setFont(new Font("Arial", smaller/2.0));
        r2TextField.setFont(new Font("Arial",smaller/2.0));
        r3Label.setFont(new Font("Arial", smaller/2.0));
        r3TextField.setFont(new Font("Arial",smaller/2.0));
        r4Label.setFont(new Font("Arial", smaller/2.0));
        r4TextField.setFont(new Font("Arial",smaller/2.0));
        r5Label.setFont(new Font("Arial", smaller/2.0));
        r5TextField.setFont(new Font("Arial",smaller/2.0));
        r6Label.setFont(new Font("Arial", smaller/2.0));
        r6TextField.setFont(new Font("Arial",smaller/2.0));
        r7Label.setFont(new Font("Arial", smaller/2.0));
        r7TextField.setFont(new Font("Arial",smaller/2.0));

        accumulatorLabel.setFont(new Font("Arial", smaller/2.0));
        accumulatorTextFieldBin.setFont(new Font("Arial",smaller/2.0));
        accumulatorTextFieldDec.setFont(new Font("Arial",smaller/2.0));
        accumulatorTextFieldHex.setFont(new Font("Arial",smaller/2.0));

        bLabel.setFont(new Font("Arial", smaller/2.0));
        bTextFieldBin.setFont(new Font("Arial",smaller/2.0));
        bTextFieldDec.setFont(new Font("Arial",smaller/2.0));
        bTextFieldHex.setFont(new Font("Arial",smaller/2.0));

        cLabel.setFont(new Font("Arial", smaller/2.0));
        cTextField.setFont(new Font("Arial",smaller/2.0));
        acLabel.setFont(new Font("Arial", smaller/2.0));
        acTextField.setFont(new Font("Arial",smaller/2.0));
        f0Label.setFont(new Font("Arial", smaller/2.0));
        f0TextField.setFont(new Font("Arial",smaller/2.0));
        rs1Label.setFont(new Font("Arial", smaller/2.0));
        rs1TextField.setFont(new Font("Arial",smaller/2.0));
        rs0Label.setFont(new Font("Arial", smaller/2.0));
        rs0TextField.setFont(new Font("Arial",smaller/2.0));
        ovLabel.setFont(new Font("Arial", smaller/2.0));
        ovTextField.setFont(new Font("Arial",smaller/2.0));
        f1Label.setFont(new Font("Arial", smaller/2.0));
        f1TextField.setFont(new Font("Arial",smaller/2.0));
        pLabel.setFont(new Font("Arial", smaller/2.0));
        pTextField.setFont(new Font("Arial",smaller/2.0));

        p0Label.setFont(new Font("Arial", smaller/2.0));
        p0TextField.setFont(new Font("Arial",smaller/2.0));
        p1Label.setFont(new Font("Arial", smaller/2.0));
        p1TextField.setFont(new Font("Arial",smaller/2.0));
        p2Label.setFont(new Font("Arial", smaller/2.0));
        p2TextField.setFont(new Font("Arial",smaller/2.0));
        p3Label.setFont(new Font("Arial", smaller/2.0));
        p3TextField.setFont(new Font("Arial",smaller/2.0));

        timePassedLabel.setFont(new Font("Arial", smaller/2.0));
        timePassedTextField.setFont(new Font("Arial",smaller/2.0));
        pcLabel.setFont(new Font("Arial", smaller/2.0));
        pcTextField.setFont(new Font("Arial",smaller/2.0));

        EALabel.setFont(new Font("Arial", smaller/2.0));
        EATextField.setFont(new Font("Arial",smaller/2.0));
        ESLabel.setFont(new Font("Arial", smaller/2.0));
        ESTextField.setFont(new Font("Arial",smaller/2.0));
        ET1Label.setFont(new Font("Arial", smaller/2.0));
        ET1TextField.setFont(new Font("Arial",smaller/2.0));
        EX1Label.setFont(new Font("Arial", smaller/2.0));
        EX1TextField.setFont(new Font("Arial",smaller/2.0));
        ET0Label.setFont(new Font("Arial", smaller/2.0));
        ET0TextField.setFont(new Font("Arial",smaller/2.0));
        EX0Label.setFont(new Font("Arial", smaller/2.0));
        EX0TextField.setFont(new Font("Arial",smaller/2.0));

        T0HLabel.setFont(new Font("Arial", smaller/2.0));
        T0HTextField.setFont(new Font("Arial",smaller/2.0));
        T1HLabel.setFont(new Font("Arial", smaller/2.0));
        T1HTextField.setFont(new Font("Arial",smaller/2.0));
        T0LLabel.setFont(new Font("Arial", smaller/2.0));
        T0LTextField.setFont(new Font("Arial",smaller/2.0));
        T1LLabel.setFont(new Font("Arial", smaller/2.0));
        T1LTtextField.setFont(new Font("Arial",smaller/2.0));
        TMODLabel.setFont(new Font("Arial", smaller/2.0));
        TMODTextField.setFont(new Font("Arial",smaller/2.0));
        TCONLabel.setFont(new Font("Arial", smaller/2.0));
        TCONTextField.setFont(new Font("Arial",smaller/2.0));

        portsDesc.setFont(new Font("Arial", smaller/2.0));
        portsDesc.setFont(new Font("Arial",smaller/2.0));

        portButton0.setFont(new Font("Arial", smaller/1.5));
        portButton1.setFont(new Font("Arial", smaller/1.5));
        portButton2.setFont(new Font("Arial", smaller/1.5));
        portButton3.setFont(new Font("Arial", smaller/1.5));
        portButton4.setFont(new Font("Arial", smaller/1.5));
        portButton5.setFont(new Font("Arial", smaller/1.5));
        portButton6.setFont(new Font("Arial", smaller/1.5));
        portButton7.setFont(new Font("Arial", smaller/1.5));

        portToggle0.setFont(new Font("Arial", smaller/1.5));
        portToggle1.setFont(new Font("Arial", smaller/1.5));
        portToggle2.setFont(new Font("Arial", smaller/1.5));
        portToggle3.setFont(new Font("Arial", smaller/1.5));
        portToggle4.setFont(new Font("Arial", smaller/1.5));
        portToggle5.setFont(new Font("Arial", smaller/1.5));
        portToggle6.setFont(new Font("Arial", smaller/1.5));
        portToggle7.setFont(new Font("Arial", smaller/1.5));

        compilationErrorsLabel.setFont(new Font("Arial",smaller/2.0));

    }

    public MainStage(double width,double height) {
        this.height = height;
        this.width = width;
    }

    public void drawFrame() {
        double width = mainStage.getWidth() * 3.0 / 10.0;
        double height = mainScene.getHeight() * 1.0 / 10.0;

        ledCanvas.setWidth(width);
        ledCanvas.setHeight(height);

        double oneLedWidth = width / 8.0;

        GraphicsContext gc = ledCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        //gc.strokeRect(0,0,width,height);
        gc.setFill(Color.RED);

        for (int i = 0; i < 8; i++) {
            String portName = ledsPort + "." + (7 - i);
            if (Main.cpu.mainMemory.getBit(Main.cpu.codeMemory.bitAddresses.get(portName)))
                gc.setFill(Color.RED);
            else
                gc.setFill(Color.LIGHTGRAY);

            double centerX = i * oneLedWidth + oneLedWidth / 2.0;
            double centerY = height / 2.0;
            double radius = (oneLedWidth >= height ? height - 2 : oneLedWidth) / 2.0 - 5;

            gc.fillOval(centerX - radius, centerY - radius, radius * 2, radius * 2);
        }

        height = mainScene.getHeight() * 2.5 / 10;

        seg7Canvas.setWidth(width);
        seg7Canvas.setHeight(height);

        gc = seg7Canvas.getGraphicsContext2D();

        double marginX = 10;
        double marginY = 20;

        gc.clearRect(0, 0, width, height);
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        double shorter = 10;
        double longer = (height - 2 * marginY - 4 * shorter) / 2.0;

        gc.setFill(Color.BLACK);

        //LICZBA PIERWSZA
        String wartosc = microcontroller.Cpu.expandTo8Digits(Integer.toBinaryString(Main.cpu.mainMemory.get(seg7displayPort)));
        int[] wartosci = Converters.bcdto7seg(wartosc.substring(0, 4));

        if (wartosci[0] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(width / 2.0 - longer - shorter - marginX, marginY, longer, shorter, 10, 10);//a
        if (wartosci[6] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(width / 2.0 - longer - shorter - marginX, marginY + longer + shorter, longer, shorter, 10, 10);//g
        if (wartosci[3] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(width / 2.0 - longer - shorter - marginX, marginY + 2.0 * longer + 2.0 * shorter, longer, shorter, 10, 10);//d

        if (wartosci[5] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(width / 2.0 - longer - shorter - marginX - shorter, marginY + shorter, shorter, longer, 10, 10);//f
        if (wartosci[4] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(width / 2.0 - longer - shorter - marginX - shorter, marginY + shorter + longer + shorter, shorter, longer, 10, 10);//e

        if (wartosci[1] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(width / 2.0 - marginX - shorter, marginY + shorter, shorter, longer, 10, 10);//b
        if (wartosci[2] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(width / 2.0 - marginX - shorter, marginY + shorter + longer + shorter, shorter, longer, 10, 10);//c


        wartosci = Converters.bcdto7seg(wartosc.substring(4, 8));

        //LICZBA DRUGA
        if (wartosci[0] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(width / 2.0 + shorter + marginX, marginY, longer, shorter, 10, 10);//a

        if (wartosci[6] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGRAY);
        gc.fillRoundRect(width / 2.0 + shorter + marginX, marginY + longer + shorter, longer, shorter, 10, 10);//g

        if (wartosci[3] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(width / 2.0 + shorter + marginX, marginY + 2.0 * longer + 2.0 * shorter, longer, shorter, 10, 10);//d


        if (wartosci[1] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(width / 2.0 + longer + shorter + marginX, marginY + shorter, shorter, longer, 10, 10);//b

        if (wartosci[2] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(width / 2.0 + longer + shorter + marginX, marginY + shorter + longer + shorter, shorter, longer, 10, 10);//c


        if (wartosci[5] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(width / 2.0 + marginX, marginY + shorter, shorter, longer, 10, 10);//f

        if (wartosci[4] == 1)
            gc.setFill(Color.RED);
        else
            gc.setFill(Color.LIGHTGREY);
        gc.fillRoundRect(width / 2.0 + marginX, marginY + shorter + longer + shorter, shorter, longer, 10, 10);//e

        width = mainStage.getWidth();
        height = mainStage.getHeight() * (37.0 / 100.0);

        portsStatusCanvas.setWidth(width);
        portsStatusCanvas.setHeight(height);
        gc = portsStatusCanvas.getGraphicsContext2D();
        gc.clearRect(0, 0, width, height);
        double upperMargin = 40;
        double XMargin = 30;
        double betweenMargin = 20;
        double breakValue = (height-upperMargin)/12.0;
        gc.setStroke(Color.BLACK);
        double oneWidht = (width-2*XMargin - 3*betweenMargin)/4;
        gc.setLineWidth(1);
        for(int i = 0; i < 8; i++) {
            for(int j = 0; j < portChartScale;j++) {
                if(port0History[j][i]=='0') {
                    gc.setStroke(Color.GREEN);
                    gc.strokeLine(XMargin+(oneWidht/(portChartScale*1.0))*j,upperMargin+i*breakValue+breakValue/3.0*2.0,XMargin+(oneWidht/(portChartScale*1.0))*(j+1),upperMargin+i*breakValue+breakValue/3.0*2.0);
                }
                else {
                    gc.setStroke(Color.RED);
                    gc.strokeLine(XMargin+(oneWidht/(portChartScale*1.0))*j,upperMargin+i*breakValue+breakValue/3.0,XMargin+(oneWidht/(portChartScale*1.0))*(j+1),upperMargin+i*breakValue+breakValue/3.0);
                }

                if(port1History[j][i]=='0') {
                    gc.setStroke(Color.GREEN);
                    gc.strokeLine(XMargin+1.0*oneWidht+1.0*betweenMargin+(oneWidht/(portChartScale*1.0))*j,upperMargin+i*breakValue+breakValue/3.0*2.0,XMargin+1.0*oneWidht+1.0*betweenMargin+(oneWidht/(portChartScale*1.0))*(j+1),upperMargin+i*breakValue+breakValue/3.0*2.0);
                }
                else {
                    gc.setStroke(Color.RED);
                    gc.strokeLine(XMargin+1.0*oneWidht+1.0*betweenMargin+(oneWidht/(portChartScale*1.0))*j,upperMargin+i*breakValue+breakValue/3.0,XMargin+1.0*oneWidht+1.0*betweenMargin+(oneWidht/(portChartScale*1.0))*(j+1),upperMargin+i*breakValue+breakValue/3.0);
                }

                if(port2History[j][i]=='0') {
                    gc.setStroke(Color.GREEN);
                    gc.strokeLine(XMargin+2.0*oneWidht+2.0*betweenMargin+(oneWidht/(portChartScale*1.0))*j,upperMargin+i*breakValue+breakValue/3.0*2.0,XMargin+2.0*oneWidht+2.0*betweenMargin+(oneWidht/(portChartScale*1.0))*(j+1),upperMargin+i*breakValue+breakValue/3.0*2.0);
                }
                else {
                    gc.setStroke(Color.RED);
                    gc.strokeLine(XMargin+2.0*oneWidht+2.0*betweenMargin+(oneWidht/(portChartScale*1.0))*j,upperMargin+i*breakValue+breakValue/3.0,XMargin+2.0*oneWidht+2.0*betweenMargin+(oneWidht/(portChartScale*1.0))*(j+1),upperMargin+i*breakValue+breakValue/3.0);
                }

                if(port3History[j][i]=='0') {
                    gc.setStroke(Color.GREEN);
                    gc.strokeLine(XMargin+3.0*oneWidht+3.0*betweenMargin+(oneWidht/(portChartScale*1.0))*j,upperMargin+i*breakValue+breakValue/3.0*2.0,XMargin+3.0*oneWidht+3.0*betweenMargin+(oneWidht/(portChartScale*1.0))*(j+1),upperMargin+i*breakValue+breakValue/3.0*2.0);
                }
                else {
                    gc.setStroke(Color.RED);
                    gc.strokeLine(XMargin+3.0*oneWidht+3.0*betweenMargin+(oneWidht/(portChartScale*1.0))*j,upperMargin+i*breakValue+breakValue/3.0,XMargin+3.0*oneWidht+3.0*betweenMargin+(oneWidht/(portChartScale*1.0))*(j+1),upperMargin+i*breakValue+breakValue/3.0);
                }
            }
        }
        gc.setStroke(Color.BLACK);
        gc.setLineWidth(2);
        gc.strokeRect(XMargin,upperMargin,oneWidht,breakValue*8);
        gc.strokeRect(XMargin+oneWidht+betweenMargin,upperMargin,oneWidht,breakValue*8);
        gc.strokeRect(XMargin+2.0*oneWidht+2.0*betweenMargin,upperMargin,oneWidht,breakValue*8);
        gc.strokeRect(XMargin+3.0*oneWidht+3.0*betweenMargin,upperMargin,oneWidht,breakValue*8);

        gc.fillText("0",marginX,upperMargin+8*breakValue-breakValue/2.0);
        gc.fillText("1",marginX,upperMargin+7*breakValue-breakValue/2.0);
        gc.fillText("2",marginX,upperMargin+6*breakValue-breakValue/2.0);
        gc.fillText("3",marginX,upperMargin+5*breakValue-breakValue/2.0);
        gc.fillText("4",marginX,upperMargin+4*breakValue-breakValue/2.0);
        gc.fillText("5",marginX,upperMargin+3*breakValue-breakValue/2.0);
        gc.fillText("6",marginX,upperMargin+2*breakValue-breakValue/2.0);
        gc.fillText("7",marginX,upperMargin+breakValue-breakValue/2.0);
        gc.setTextAlign(TextAlignment.CENTER);
        gc.setTextBaseline(VPos.CENTER);
        gc.fillText("PO",XMargin+oneWidht/2,upperMargin/2.0);
        gc.fillText("P1",XMargin+oneWidht+betweenMargin+oneWidht/2,upperMargin/2.0);
        gc.fillText("P3",XMargin+oneWidht+betweenMargin+oneWidht+betweenMargin+oneWidht/2,upperMargin/2.0);
        gc.fillText("P4",XMargin+oneWidht+betweenMargin+oneWidht+betweenMargin+oneWidht+betweenMargin+oneWidht/2,upperMargin/2.0);
    }


    private Canvas ledCanvas = new Canvas();
    private Canvas seg7Canvas =  new Canvas();
    private Canvas portsStatusCanvas = new Canvas();

    private void setEditorText(ArrayList<String> toSet) {
        StringBuilder textToSet = new StringBuilder();
        for(int i = 0; i < toSet.size();i++) {
            textToSet.append(toSet.get(i));
            if(i!=toSet.size()-1)
                textToSet.append("\n");
        }
        editorTextArea.setText(textToSet.toString());
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

    private TextArea editorTextArea;

    private Label speedSelectLabel;

    private Stage mainStage;
    private Scene mainScene;

    private boolean continuousRunFlag;

    private Task<Void> autoRun;

    private String lines[];

    private Button rysujRunButton;

    private boolean running;

    private GridPane diodesPaneGridPane;

    String ledsPort = "P0";
    String seg7displayPort = "P1";

    private TextField addressInChangeValueTextField;
    private TextField valueInChangeValueTextField;

    private Button changeValueInChangeValueButton;

    public int portChartScale = 16;

    private MenuItem exportFileMenuItem;
    private MenuItem importFileMenuItem;

}
