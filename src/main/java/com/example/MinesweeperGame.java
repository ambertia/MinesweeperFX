package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
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
    // Toolbar elements that must be referenced in different methods
    private Label minesLabel;
    private ToolBar toolbar;

    // Region nodes that must be referenced in different methods
    private GameContainer activeGame;
    private ZoomableScrollPane gameBoardWrapper;
    private StackPane gameAreaStack;
    private Scene gameplayScene;
    private Scene menuScene;
    private Stage gameWindow;

    // Metainformation
    private GameSpecification difficulty;
    
    // Application constructor to assign UI elements & complete fields
    public MinesweeperGame() {
        /*
         * Build Toolbar
         */

        // Label to display the number of mines the player has yet to flag
        minesLabel = new Label();
        minesLabel.getStyleClass().add("mineLabel");

        // Custom difficulty selector using spinners
        Spinner<Integer> columns = new Spinner<Integer>(1, 100, 30);
        columns.setPromptText("Columns");
        columns.setEditable(true);
        Spinner<Integer> rows = new Spinner<Integer>(1, 100, 20);
        rows.setPromptText("Rows");
        rows.setEditable(true);
        Spinner<Double> mines = new Spinner<Double>(0., 1., 0.17);
        mines.setPromptText("Mines");
        mines.setEditable(true);
        HBox customDifficultyWrapper = new HBox(columns, rows, mines);
        customDifficultyWrapper.setVisible(false);
        
        // Drop-down menu to select difficulty
        MenuButton difficultySelector = new MenuButton("Medium");
        // Manipulating the spinner values is a straightforward way to encode the difficulty,
        // so that the New Game button can simply look at the spinner each time and check the values.
        // Also still allows default values to be read from GameDefaults
        MenuItem easy = new MenuItem("Easy");
        easy.setOnAction(e -> {
            difficultySelector.setText("Easy");
            customDifficultyWrapper.setVisible(false);
            columns.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, GameDefaults.EASY.Columns));
            rows.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, GameDefaults.EASY.Rows));
            mines.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0., 1., GameDefaults.EASY.MineFraction, 0.01));
        });
        MenuItem medium = new MenuItem("Medium");
        medium.setOnAction(e -> {
            difficultySelector.setText("Medium");
            customDifficultyWrapper.setVisible(false);
            columns.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, GameDefaults.MEDIUM.Columns));
            rows.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, GameDefaults.MEDIUM.Rows));
            mines.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0., 1., GameDefaults.MEDIUM.MineFraction, 0.01));
        });
        MenuItem hard = new MenuItem("Hard");
        hard.setOnAction(e -> {
            difficultySelector.setText("Hard");
            customDifficultyWrapper.setVisible(false);
            columns.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, GameDefaults.HARD.Columns));
            rows.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, GameDefaults.HARD.Rows));
            mines.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0., 1., GameDefaults.HARD.MineFraction, 0.01));
        });
        MenuItem custom = new MenuItem("Custom");
        custom.setOnAction(e -> {
            difficultySelector.setText("Custom");
            customDifficultyWrapper.setVisible(true);
            columns.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, difficulty.Columns));
            rows.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, difficulty.Rows));
            mines.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0., 1., difficulty.MineFraction, 0.01));
        });
        difficultySelector.getItems().addAll(easy, medium, hard, custom);

        // Button to allow the user to start a new game
        Button newGame = new Button();
        newGame.setText("New Game");
        newGame.setOnMouseClicked(e -> {
            if (!e.isStillSincePress()) return;
            if (!(e.getButton().equals(MouseButton.PRIMARY))) return;
            try {
                difficulty = new GameSpecification(columns.getValue(), rows.getValue(), mines.getValue());
            } catch (NumberFormatException error) {
                System.out.println(error);
            }
            startNewGame();
        });

        // Add elements to toolbar
        toolbar = new ToolBar(minesLabel, newGame, difficultySelector, customDifficultyWrapper);

        /*
         * Fetch Display Information
         */
        final Rectangle2D displayBounds = Screen.getPrimary().getBounds();

        /*
         * Build Gameplay Scene
         */
        gameAreaStack = new StackPane();
        final BorderPane activeGameDisplay = new BorderPane();
        activeGameDisplay.setTop(toolbar);
        activeGameDisplay.setCenter(gameAreaStack);
        gameplayScene = new Scene(activeGameDisplay, displayBounds.getWidth(), displayBounds.getHeight());
        gameplayScene.getStylesheets().add("GameStyleControl.css");

        /*
         * Build Menu Scene
         */
        final Button newGameMenu = new Button("New Game");
        newGameMenu.setOnAction(e -> {
            gameWindow.setScene(gameplayScene);
            startNewGame();
        });
        final Button quit = new Button("Quit");
        quit.setOnAction(e -> {
            Platform.exit();
        });
        final Label titleLabel = new Label("Minesweeper");
        final HBox buttonRow = new HBox(newGameMenu, quit);
        final VBox menuLayout = new VBox(titleLabel, buttonRow);
        menuScene = new Scene(menuLayout, displayBounds.getWidth(), displayBounds.getHeight());
        menuScene.getStylesheets().add("GameStyleControl.css");
    }
    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
    
        // Configure primary window
        gameWindow = primaryStage;
        primaryStage.setMaximized(true);
        primaryStage.setTitle("MinesweeperFX");
        primaryStage.setScene(menuScene);
        primaryStage.show();
        difficulty = GameDefaults.MEDIUM;

        primaryStage.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            final GameCell thisCell = activeGame.getCell(e.getTarget());
            if (thisCell == null) return;
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (thisCell.isMine()) {
                    thisCell.setStyle("-fx-border-color: #ff0000;");
                    gameAreaStack.getChildren().add(gameAlert("You lose!"));
                    return;
                }
            }
            else if (e.getButton().equals(MouseButton.SECONDARY)) {
                if (thisCell.isRevealed()) {
                    if (activeGame.checkRevealedMines(thisCell)) {
                        gameAreaStack.getChildren().add(gameAlert("You lose!"));
                        return;
                    }
                }
            }
            else return;
            if (activeGame.remainingMines() == 0) {
                final int revealedCells = activeGame.tallyRevealed();
                if (revealedCells == activeGame.getGameSpec().boardSize - activeGame.getGameSpec().gameMines) {
                    gameBoardWrapper.scaleToFit(gameAreaStack, new Rectangle2D(0, 0, (activeGame.getGameSpec().Columns + 2) * GameCell.CELL_SIZE, (activeGame.getGameSpec().Rows + 2) * GameCell.CELL_SIZE));
                    gameAreaStack.getChildren().add(gameAlert("You win!"));
                }
            }
            minesLabel.setText(String.valueOf(activeGame.remainingMines()));
        });
        
    }

    final private void startNewGame() {
        gameAreaStack.getChildren().removeAll(gameAreaStack.getChildren());
        activeGame = new GameContainer(difficulty);
        gameBoardWrapper = new ZoomableScrollPane(activeGame);
        gameBoardWrapper.getStyleClass().add("edge-to-edge");
        gameAreaStack.getChildren().add(gameBoardWrapper);
        gameBoardWrapper.scaleToFit(gameAreaStack, new Rectangle2D(0, 0, (activeGame.getGameSpec().Columns + 2) * GameCell.CELL_SIZE, (activeGame.getGameSpec().Rows + 2) * GameCell.CELL_SIZE));
        minesLabel.setText(String.valueOf(activeGame.remainingMines()));
    }

    // Construct a game popup such as the ones when you win or lose the game
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
            Platform.exit();
        });

        return mainLayout;
    }
}
