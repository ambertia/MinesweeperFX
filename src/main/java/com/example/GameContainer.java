package com.example;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class GameContainer extends GridPane {
    private ArrayList<GameCell> gameTiles;
    private final GameSpecification thisGameSpec;

    public static double BOARD_PADDING_CELLS = 4;

    private int remainingMines;
    private boolean openingCondition;

    // Construct a new game board from constraints in gameSpec
    GameContainer(GameSpecification gameSpec) {
        thisGameSpec = gameSpec;
        gameTiles = cellListFactory();
        openingCondition = true;

        remainingMines = thisGameSpec.gameMines;

        setPadding(new Insets(BOARD_PADDING_CELLS * GameCell.CELL_SIZE));
        setContent(gameTiles);

        // Game events should be captured here to be processed
        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            if (!e.isStillSincePress()) return;

            final int thisIndex = gameTiles.indexOf(e.getTarget());
            GameCell thisCell = gameTiles.get(thisIndex);
            // Right clicks will be for either group revealing or tracking flags
            if (e.getButton().equals(MouseButton.SECONDARY)) {
                if (openingCondition) e.consume();
                // If not revealed, conduct flag toggling
                else if (!thisCell.isRevealed()) remainingMines += thisCell.isFlagged() ? -1 : 1;
                // If revealed, reveal surrounding cells
                else revealNeighbors(thisIndex);
            }
            else if (e.getButton().equals(MouseButton.PRIMARY)) {
                // Intercept operation if this is the first cell of the game to be revealed
                if (openingCondition) {
                    boolean[] mineLocations;
                    int nearbyMines;
                    do {
                        mineLocations = minefieldFactory();
                        nearbyMines = 0;
                        final LinkedList<Integer> neighbors = getNeighbors(thisIndex);
                        for (Integer cell : neighbors) if (mineLocations[cell]) nearbyMines++;
                    } while (nearbyMines != 0 || mineLocations[thisIndex]);

                    gameTiles = cellListFactory(mineLocations);
                    setContent(gameTiles);

                    remainingMines = thisGameSpec.gameMines;
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
        this(GameDefaults.EASY);
    }

    // Generate an appropriately sized list of yes/no values representing the presence or absence of mines in the game tiles
    private boolean[] minefieldFactory() {
        // Initialize variables for mine locations and random generation
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

        return mineLocations;
    }

    // Create a properly sized list of game cells provided mine location data
    private ArrayList<GameCell> cellListFactory(boolean[] minefield) {
        final ArrayList<GameCell> cells = new ArrayList<>(thisGameSpec.boardSize);

        for (int i = 0, n = thisGameSpec.boardSize; i < n; i++) {
            // Get valid neighboring cells from helper function
            final LinkedList<Integer> neighbors = getNeighbors(i);
            // Iterate over neighbor cells to count number of mines
            int nearbyMines = 0;
            for (Integer neighborIndex : neighbors) {
                if (minefield[neighborIndex]) nearbyMines++;
            }
            // Create new GameCell with mine and nearby count data, and add it to the list
            cells.add(new GameCell(minefield[i], nearbyMines));
        }
        return cells;
    }

    // Create a properly sized list of generic, blank game cells
    private ArrayList<GameCell> cellListFactory() {
        final ArrayList<GameCell> cells = new ArrayList<>(thisGameSpec.boardSize);
        for (int i = 0, n = thisGameSpec.boardSize; i < n; i++) {
            cells.add(new GameCell());
        }
        return cells;
    }

    private void setContent(ArrayList<GameCell> cells) {
        // Remove all previously assigned cells from the grid, if present
        for (GameCell tile : gameTiles) getChildren().remove(tile);

        // Assign new cells to the grid
        for (int i = 0, n = thisGameSpec.boardSize; i < n; i++) {
            final Point thisCellPoint = thisGameSpec.getPoint(i);
            add(cells.get(i), thisCellPoint.x, thisCellPoint.y);
        }
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

    public GameCell getCell(EventTarget target) {
        if (!gameTiles.contains(target)) return null;
        else {
            int index = gameTiles.indexOf(target);
            return gameTiles.get(index);
        }
    }

    public GameSpecification getGameSpec() {
        return thisGameSpec;
    }

    public int tallyRevealed() {
        int revealed = 0;
        for (GameCell cell : gameTiles) {
            if (cell.isRevealed()) revealed++;
        }
        return revealed;
    }

    public int remainingMines() {
        return remainingMines;
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