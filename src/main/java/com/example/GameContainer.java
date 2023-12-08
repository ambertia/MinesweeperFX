package com.example;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;
import javafx.geometry.Insets;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class GameContainer extends GridPane {
    private ArrayList<GameCell> gameTiles;
    private final GameSpecification thisGameSpec;

    private int remainingMines;
    private boolean openingCondition;

    // Construct a new game board from constraints in gameSpec
    GameContainer(GameSpecification gameSpec) {
        thisGameSpec = gameSpec;
        gameTiles = boardFactory();
        openingCondition = true;

        remainingMines = gameSpec.gameMines;

        setPadding(new Insets(100.));

        // Game events should be captured here to be processed
        addEventHandler(CellDataEvent.ANY, e -> {
            GameCell thisCell = e.getGameCell();
            final MouseEvent thisEvent = e.getEvent();
            final int thisIndex = gameTiles.indexOf(thisCell);
            // Right clicks will be for either group revealing or tracking flags
            if (thisEvent.getButton().equals(MouseButton.SECONDARY)) {
                // If not revealed, conduct flag toggling
                if (!thisCell.isRevealed()) {
                    remainingMines += (thisCell.isFlagged()) ? -1 : 1;
                }
                // If revealed, reveal surrounding cells
                else {
                    revealNeighbors(thisIndex);
                }
            }
            else if (thisEvent.getButton().equals(MouseButton.PRIMARY)) {
                // Intercept operation if this is the first cell of the game to be revealed
                if (openingCondition) {
                    while (gameTiles.get(thisIndex).getNearby() != 0) {
                        System.out.println(gameTiles.get(thisIndex).getNearby());
                        for (GameCell tile : gameTiles) {
                            getChildren().remove(tile);
                        }
                        System.out.println("Generate board...");
                        gameTiles = boardFactory();
                    }
                    System.out.println("openingcondition");
                    openingCondition = false;
                    thisCell = gameTiles.get(thisIndex);
                    thisCell.reveal();
                }
                if (thisCell.isRevealed()) {
                    if (thisCell.getNearby() == 0) revealNeighbors(thisIndex);
                }
            }
        });
        
    }
    // Build a game with default specifications
    GameContainer() {
        this(new GameSpecification());
    }

    private ArrayList<GameCell> boardFactory() {
        final ArrayList<GameCell> board = new ArrayList<>(thisGameSpec.boardSize);
        // Initialize temporary storage for mine locations and random generation
        final boolean[] mineLocations = new boolean[thisGameSpec.boardSize];
        int minesToGenerate = thisGameSpec.gameMines;
        Random mineGenerator = new Random();

        // Generate appropriate number of mines and store in mineLocations[]
        while (minesToGenerate > 0) {
            final int i = mineGenerator.nextInt(thisGameSpec.boardSize);
            if (mineLocations[i]) continue;
            mineLocations[i] = true;
            minesToGenerate--;
        }

        // Generate GameCells and arrange them on the board
        for (int i = 0; i < thisGameSpec.boardSize; i++) {
            // Get valid neighboring cells from helper function
            final LinkedList<Integer> neighbors = getNeighbors(i);
            // Iterate over neighbor cells to count number of mines
            int nearbyMines = 0;
            for (Integer neighborIndex : neighbors) {
                if (mineLocations[neighborIndex]) nearbyMines++;
            }
            // Create new GameCell with mine and nearby count data
            GameCell thisCell = new GameCell(mineLocations[i], nearbyMines);

            // Add the GameCell to the internal list and set its position in the GridPane
            final Point thisCellPoint = thisGameSpec.getPoint(i);
            board.add(thisCell);
            add(thisCell, thisCellPoint.x, thisCellPoint.y);
        }

        return board;
    }

    // Given a single cell index, return a list of valid indices in a 3x3 square centered on index
    private LinkedList<Integer> getNeighbors(int index) {
        // Create list object to return
        final LinkedList<Integer> thisCellNeighbors = new LinkedList<>();

        // Convert subject cell to a Point and generate an array of every point around it
        Point thisPoint = thisGameSpec.getPoint(index);
        final LinkedList<Point> candidates = new LinkedList<>();
        // Iterate across the 3x3 square
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                // Ignore special case of the original cell
                if (i == 0 && j == 0) continue;
                // Slot neighbor cell into list
                candidates.add(new Point(thisPoint.x + i, thisPoint.y + j));
            }
        }

        // Evaluate every neighboring cell for validity and add valid ones to new list
        for (Point neighbor : candidates) {
            if (neighbor.x < 0 || neighbor.x >= thisGameSpec.Columns) continue;
            if (neighbor.y < 0 || neighbor.y >= thisGameSpec.Rows) continue;
            thisCellNeighbors.add(Integer.valueOf(thisGameSpec.getIndex(neighbor)));
        }

        return thisCellNeighbors;
    }

    private void revealNeighbors(int index) {
        for (int neighbor : getNeighbors(index)) {
            final GameCell neighborTile = gameTiles.get(neighbor);
            if (neighborTile.isRevealed()) continue;
            neighborTile.reveal();
            if (neighborTile.getNearby() == 0) revealNeighbors(neighbor);
        }
    }
}
