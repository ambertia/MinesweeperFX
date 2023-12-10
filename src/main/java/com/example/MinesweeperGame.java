package com.example;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;

public class MinesweeperGame extends Application {
    private GameContainer activeGame;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        ToolBar toolbar = new ToolBar();
        Button testButton = new Button("test");
        Label minesLabel = new Label("mines");
        toolbar.getItems().add(testButton);
        toolbar.getItems().add(minesLabel);
        
        activeGame = new GameContainer(new GameSpecification());
        ZoomableScrollPane gameBoardWrapper = new ZoomableScrollPane(activeGame);
        gameBoardWrapper.getStyleClass().add("edge-to-edge");
        BorderPane activeGameDisplay = new BorderPane();
        
        activeGameDisplay.setCenter(gameBoardWrapper);
        activeGameDisplay.setTop(toolbar);
        
        final Scene gameplayScene = new Scene(activeGameDisplay, 1280, 720);
        gameplayScene.getStylesheets().add("GameStyleControl.css");
        primaryStage.setMaximized(true);
        primaryStage.setTitle("MinesweeperFX");
        primaryStage.setScene(gameplayScene);
        primaryStage.show();

        
    }

    final private void startNewGame() {
        activeGame = new GameContainer();
    }

    private class GameAlert extends Popup {

        GameAlert() {
            final Label alert = new Label("Game alert!");
            final Button newGame = new Button("New game");
            final Button quit = new Button("Quit");

            final HBox buttonRow = new HBox(newGame, quit);
            final VBox mainLayout = new VBox();
            mainLayout.getChildren().addAll(alert, buttonRow);

            newGame.setOnMouseClicked(e -> {
                startNewGame();
                hide();
            });
        }
    }
}
