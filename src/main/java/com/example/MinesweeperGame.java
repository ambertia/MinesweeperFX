package com.example;

import javafx.application.Application;
import javafx.geometry.Dimension2D;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class MinesweeperGame extends Application {
    private Label minesLabel;
    private ToolBar toolbar;
    private GameContainer activeGame;
    private ZoomableScrollPane gameBoardWrapper;
    private GameSpecification gameSpec;
    private StackPane gameAreaStack;
    private Stage window;

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        toolbar = new ToolBar();
        Button newGame = new Button("New");
        minesLabel = new Label();
        toolbar.getItems().add(newGame);
        toolbar.getItems().add(minesLabel);

        newGame.setOnMouseClicked(e -> {
            if (!e.isStillSincePress()) return;
            if (!(e.getButton().equals(MouseButton.PRIMARY))) return;
            startNewGame();
        });
        
        gameAreaStack = new StackPane();
        gameSpec = new GameSpecification();
        BorderPane activeGameDisplay = new BorderPane();
        
        activeGameDisplay.setCenter(gameAreaStack);
        activeGameDisplay.setTop(toolbar);
        
        final Rectangle2D displayBounds = Screen.getPrimary().getBounds();
        final Scene gameplayScene = new Scene(activeGameDisplay, displayBounds.getWidth(), displayBounds.getHeight());
        gameplayScene.getStylesheets().add("GameStyleControl.css");
        window = primaryStage;
        primaryStage.setMaximized(true);
        primaryStage.setTitle("MinesweeperFX");
        primaryStage.setScene(gameplayScene);
        primaryStage.show();
        startNewGame();

        primaryStage.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            final GameCell thisCell = activeGame.getCell(e.getTarget());
            if (thisCell == null) return;
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (thisCell.isMine()) {
                    thisCell.setStyle("-fx-border-color: #ff0000;");
                    gameBoardWrapper.scaleToFit(gameAreaStack, new Rectangle2D(0, 0, (gameSpec.Columns + 2) * GameCell.CELL_SIZE, (gameSpec.Rows + 2) * GameCell.CELL_SIZE));
                    gameAreaStack.getChildren().add(gameAlert("You lose!"));
                    return;
                }
            }
            else if (e.getButton().equals(MouseButton.SECONDARY)) {
                
            }
            else return;
            if (activeGame.remainingMines() == 0) {
                final int revealedCells = activeGame.tallyRevealed();
                if (revealedCells == gameSpec.boardSize - gameSpec.gameMines) {
                    gameBoardWrapper.scaleToFit(gameAreaStack, new Rectangle2D(0, 0, (gameSpec.Columns + 2) * GameCell.CELL_SIZE, (gameSpec.Rows + 2) * GameCell.CELL_SIZE));
                    gameAreaStack.getChildren().add(gameAlert("You win!"));
                }
            }
            minesLabel.setText(String.valueOf(activeGame.remainingMines()));
        });
        
    }

    final private void startNewGame() {
        gameAreaStack.getChildren().removeAll(gameAreaStack.getChildren());
        activeGame = new GameContainer();
        gameBoardWrapper = new ZoomableScrollPane(activeGame);
        gameBoardWrapper.getStyleClass().add("edge-to-edge");
        gameAreaStack.getChildren().add(gameBoardWrapper);
        gameBoardWrapper.scaleToFit(gameAreaStack, new Rectangle2D(0, 0, (gameSpec.Columns + 2) * GameCell.CELL_SIZE, (gameSpec.Rows + 2) * GameCell.CELL_SIZE));
        updateMineLabel();
    }

    final protected void updateMineLabel() {
        minesLabel.setText(String.valueOf(activeGame.remainingMines()));
    }

    final private VBox gameAlert(String message) {

        final Label alert = new Label(message);
        final Button newGame = new Button("New game");
        final Button quit = new Button("Quit");

        final HBox buttonRow = new HBox(newGame, quit);
        final VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(alert, buttonRow);

        buttonRow.setAlignment(Pos.CENTER);
        mainLayout.setAlignment(Pos.CENTER);

        newGame.setOnMouseClicked(e -> {
            startNewGame();
        });

        quit.setOnMouseClicked(e -> {
            window.close();
        });

        return mainLayout;
    }
    final private VBox gameAlert() {
        return gameAlert("Game alert!");
    }
}
