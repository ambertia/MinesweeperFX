package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class MinesweeperGame extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        ToolBar toolbar = new ToolBar();
        Button testButton = new Button("test");
        Label minesLabel = new Label("mines");
        toolbar.getItems().add(testButton);
        toolbar.getItems().add(minesLabel);

        GameContainer activeGame = new GameContainer(new GameSpecification());
        ZoomableScrollPane gameBoardWrapper = new ZoomableScrollPane(activeGame);
        gameBoardWrapper.getStyleClass().add("edge-to-edge");
        // gameBoardWrapper.getStyleClass().add("root");
        BorderPane activeGameDisplay = new BorderPane();
        
        activeGameDisplay.setCenter(gameBoardWrapper);
        activeGameDisplay.setTop(toolbar);
        
        final Scene gameplayScene = new Scene(activeGameDisplay, 1280, 720);
        gameplayScene.getStylesheets().add("GameStyleControl.css");
        // gameplayScene.setFill(Paint.valueOf("#505050"));
        primaryStage.setMaximized(true);
        primaryStage.setTitle("MinesweeperFX");
        primaryStage.setScene(gameplayScene);
        primaryStage.show();
    }
}
