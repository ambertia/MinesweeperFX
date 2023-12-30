package com.example;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.RadioMenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
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
    
    // Application constructor to fill out properties and build scenes
    public MinesweeperGame() {
        /*
         * Build Toolbar
         */
        // Label to display the number of mines the player has yet to flag
        minesLabel = new Label();
        minesLabel.getStyleClass().add("mineLabel");

        // Difficulty selector using spinners
        Spinner<Integer> columns = new Spinner<Integer>(1, 100, 30);
        columns.setPromptText("Columns");
        columns.setEditable(true);
        columns.setPrefWidth(90);
        Spinner<Integer> rows = new Spinner<Integer>(1, 100, 20);
        rows.setPromptText("Rows");
        rows.setEditable(true);
        rows.setPrefWidth(90);
        Spinner<Double> mines = new Spinner<Double>(0., 1., 0.17);
        mines.setPromptText("Mines");
        mines.setEditable(true);
        mines.setPrefWidth(90);

        HBox customDifficultyWrapper = new HBox(columns, rows, mines);
        customDifficultyWrapper.setSpacing(5);
        customDifficultyWrapper.setVisible(false);
        
        // Drop-down menu to select difficulty
        MenuButton difficultySelector = new MenuButton("Medium");
        // Manipulating the spinner values is a straightforward way to encode the difficulty,
        // so that the New Game button can simply look at the spinner each time and check the values.
        // Also allows default values to be read from GameDefaults
        ToggleGroup difficultyToggleGroup = new ToggleGroup();
        RadioMenuItem easy = new RadioMenuItem("Easy");
        easy.setToggleGroup(difficultyToggleGroup);
        easy.setOnAction(e -> {
            difficultySelector.setText("Easy");
            customDifficultyWrapper.setVisible(false);
            columns.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, GameDefaults.EASY.Columns));
            rows.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, GameDefaults.EASY.Rows));
            mines.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0., 1., GameDefaults.EASY.MineFraction, 0.01));
        });
        RadioMenuItem medium = new RadioMenuItem("Medium");
        medium.setToggleGroup(difficultyToggleGroup);
        medium.setOnAction(e -> {
            difficultySelector.setText("Medium");
            customDifficultyWrapper.setVisible(false);
            columns.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, GameDefaults.MEDIUM.Columns));
            rows.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, GameDefaults.MEDIUM.Rows));
            mines.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0., 1., GameDefaults.MEDIUM.MineFraction, 0.01));
        });
        RadioMenuItem hard = new RadioMenuItem("Hard");
        hard.setToggleGroup(difficultyToggleGroup);
        hard.setOnAction(e -> {
            difficultySelector.setText("Hard");
            customDifficultyWrapper.setVisible(false);
            columns.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, GameDefaults.HARD.Columns));
            rows.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, GameDefaults.HARD.Rows));
            mines.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0., 1., GameDefaults.HARD.MineFraction, 0.01));
        });
        RadioMenuItem custom = new RadioMenuItem("Custom");
        custom.setToggleGroup(difficultyToggleGroup);
        custom.setOnAction(e -> {
            difficultySelector.setText("Custom");
            customDifficultyWrapper.setVisible(true);
            columns.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, difficulty.Columns));
            rows.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 100, difficulty.Rows));
            mines.setValueFactory(new SpinnerValueFactory.DoubleSpinnerValueFactory(0., 1., difficulty.MineFraction, 0.01));
        });
        // Add and configure items
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
        toolbar.setPrefHeight(50);

        /*
         * Fetch Display Information
         */
        final Rectangle2D displayBounds = Screen.getPrimary().getBounds();

        /*
         * Build Gameplay Scene
         */
        // StackPane for game & popups
        gameAreaStack = new StackPane();
        final BorderPane activeGameDisplay = new BorderPane();

        // Assign components to layout
        activeGameDisplay.setTop(toolbar);
        activeGameDisplay.setCenter(gameAreaStack);

        // Build scene & style
        gameplayScene = new Scene(activeGameDisplay, displayBounds.getWidth(), displayBounds.getHeight());
        gameplayScene.getStylesheets().add("GameStyleControl.css");

        /*
         * Build Menu Scene
         */
        // Main menu button to close the application
        final Button quit = new Button("Quit");
        // quit.setPrefSize(150, 50);
        quit.setOnAction(e -> {
            Platform.exit();
        });

        // Game title
        final Label titleLabel = new Label("MinesweeperFX");
        titleLabel.setStyle("-fx-font-size: 600%; -fx-font-family: \"Russo One\"");

        // Set up the buttons, stealing some from the Toolbar

        final VBox buttons = new VBox(newGame, difficultySelector, quit);
        buttons.setPadding(new Insets(25));
        buttons.setAlignment(Pos.CENTER);
        buttons.setSpacing(10);
        buttons.setPrefWidth(200);
        buttons.setFillWidth(true);
        // Assemble the overarching layout & scene
        final VBox menuLayout = new VBox(titleLabel, buttons);
        menuLayout.setAlignment(Pos.CENTER);
        menuScene = new Scene(menuLayout, displayBounds.getWidth(), displayBounds.getHeight());
        menuScene.getStylesheets().add("GameStyleControl.css");
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Launch the program
    public void start(Stage primaryStage) {
    
        // Configure primary window
        gameWindow = primaryStage;
        primaryStage.setMaximized(true);
        primaryStage.setTitle("MinesweeperFX");
        primaryStage.setScene(menuScene);
        primaryStage.show();
        difficulty = GameDefaults.MEDIUM;

        // Primary handler for in-game mouse clicks
        gameplayScene.addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {

            // Try to parse the event target from the list of cells
            final GameCell thisCell = activeGame.getCell(e.getTarget());
            // If it isn't present (or e.g. user clicked on something that isn't a game tile) return
            if (thisCell == null) return;

            // Scene controls game popups on win/lose conditions
            if (e.getButton().equals(MouseButton.PRIMARY)) {
                if (thisCell.isMine()) {
                    thisCell.setStyle("-fx-border-color: #ff0000;");
                    gameAreaStack.getChildren().add(gameAlert("You lose!"));
                    return;
                }
            }
            // Right click reveals that reveal a mine are handled here
            else if (e.getButton().equals(MouseButton.SECONDARY)) {
                if (thisCell.isRevealed()) {
                    if (activeGame.checkRevealedMines(thisCell)) {
                        gameAreaStack.getChildren().add(gameAlert("You lose!"));
                        return;
                    }
                }
            }
            // If not the left or right mouse button, do nothing
            else return;

            // Count how many of the cells on the board are actually revealed
            final int revealedCells = activeGame.tallyRevealed();

            // If the player has revealed every cell that isn't a mine, they win
            if (revealedCells == activeGame.getGameSpec().boardSize - activeGame.getGameSpec().gameMines) {
                gameBoardWrapper.scaleToFit(gameAreaStack, new Rectangle2D(0, 0, (activeGame.getGameSpec().Columns + 2) * GameCell.CELL_SIZE, (activeGame.getGameSpec().Rows + 2) * GameCell.CELL_SIZE));
                gameAreaStack.getChildren().add(gameAlert("You win!"));
            }

            // Any time a click occurs on the grid, update the counter in the toolbar
            minesLabel.setText(String.valueOf(activeGame.getRemainingMines()));
        });
        
    }

    // Initialize a new game inside the game scene's StackPan
    final private void startNewGame() {
        gameWindow.setScene(gameplayScene);
        // Remove any and all children of the stackpane - including win/lose popups, and the game board itself
        gameAreaStack.getChildren().removeAll(gameAreaStack.getChildren());
        // Create a new game container and discard the old one
        activeGame = new GameContainer(difficulty);
        // Wrap the game board in a ZoomableScrollPane
        gameBoardWrapper = new ZoomableScrollPane(activeGame);
        gameBoardWrapper.getStyleClass().add("edge-to-edge");
        // Add the wrapped game container to the StackPane
        gameAreaStack.getChildren().add(gameBoardWrapper);
        // Scale to fit the game inside the area
        // TODO this has wonky behavior
        gameBoardWrapper.scaleToFit(gameAreaStack, new Rectangle2D(0, 0, (activeGame.getGameSpec().Columns + 2) * GameCell.CELL_SIZE, (activeGame.getGameSpec().Rows + 2) * GameCell.CELL_SIZE));
        // Update the mine counter to the number of mines in the puzzle
        minesLabel.setText(String.valueOf(activeGame.getRemainingMines()));
    }

    // Construct a game popup such as the ones when you win or lose the game
    final private VBox gameAlert(String message) {

        // Three base UI components
        final Label alert = new Label(message);
        final Button newGame = new Button("New game");
        final Button quit = new Button("Quit");

        // Layout has buttons wrapped in an HBox, then all assembled into a VBox
        final HBox buttonRow = new HBox(newGame, quit);
        final VBox mainLayout = new VBox();
        mainLayout.getChildren().addAll(alert, buttonRow);
        buttonRow.setAlignment(Pos.CENTER);
        mainLayout.setAlignment(Pos.CENTER);

        // Button functionality
        newGame.setOnMouseClicked(e -> {
            startNewGame();
        });

        quit.setOnMouseClicked(e -> {
            Platform.exit();
        });

        // Hand back the new popup object
        return mainLayout;
    }
}
