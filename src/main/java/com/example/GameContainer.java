package com.example;

import java.awt.Point;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

import javafx.event.EventTarget;
import javafx.geometry.Insets;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

/*
 * This class represents the actual game board itself as an extension of GridPane. It handles
 * arranging the game tiles and some higher-level game processing with ramifications beyond a
 * single cell.
 */
public class GameContainer extends GridPane {

    // The core of the GameContainer revolves around managing these
    private ArrayList<GameCell> gameTiles;
    private final GameSpecification thisGameSpec;

    // Constant to control how many cells worth of space you can pan the ScrollPane off of the grid
    public static double BOARD_PADDING_CELLS = 4;

    // Gamestate metainformation
    private boolean openingCondition;
    private int remainingMines;

    // Construct a new game board from constraints in the gameSpec
    GameContainer(GameSpecification gameSpec) {

        // Assign basic properties
        thisGameSpec = gameSpec;
        openingCondition = true;
        remainingMines = thisGameSpec.gameMines;
        // The game starts with a dummy grid of completely empty cells until clicked on
        gameTiles = cellListFactory();

        // Modify GridPane properties here
        setPadding(new Insets(BOARD_PADDING_CELLS * GameCell.CELL_SIZE));
        setContent(gameTiles);

        // Game events should be captured here to be processed in the bubbling phase
        addEventHandler(MouseEvent.MOUSE_CLICKED, e -> {
            // TODO possibly another redundant check
            if (!e.isStillSincePress()) return;

            // Before anything else, try to get the index of the clicked on cell
            final int thisIndex = gameTiles.indexOf(e.getTarget());
            // If the cell can't be identified, do nothing
            if (thisIndex == -1) {
                e.consume();
                return;
            }

            // If the cell is identified, retrieve it for further use
            GameCell thisCell = gameTiles.get(thisIndex);

            // Right clicks will be for either group revealing or tracking flags
            if (e.getButton().equals(MouseButton.SECONDARY)) {
                // If the game hasn't started, don't even let the user place flags
                if (openingCondition) e.consume();
                // If not revealed, conduct flag toggling
                else if (!thisCell.isRevealed()) remainingMines += thisCell.isFlagged() ? -1 : 1;
                // If there are enough flagged neighbors, allow area reveal
                else if (checkEnoughFlags(thisIndex)) {
                    revealNeighbors(thisIndex);
                }
            }
            // Left clicks are always for revealing cells
            else if (e.getButton().equals(MouseButton.PRIMARY)) {

                // Intercept operation if this is the first cell of the game to be revealed
                // This is where generation of a real, valid minesweeper board happens
                if (openingCondition) {

                    // Declare some temp variables
                    boolean[] mineLocations;
                    int nearbyMines;

                    // The game must always attempt to generate a board at least once
                    do {
                        // Generate mine location values
                        mineLocations = minefieldFactory();
                        // Set nearbyMines to zero
                        nearbyMines = 0;
                        // Get valid neighbor cell indices
                        final LinkedList<Integer> neighbors = getNeighbors(thisIndex);
                        // Iterate over all neighbor cells to check for mines
                        for (Integer cell : neighbors) if (mineLocations[cell]) nearbyMines++;
                        // Repeat the process until a board is found with no mines directly around the clicked-upon cell
                    } while (nearbyMines != 0 || mineLocations[thisIndex]);

                    // Once a valid board is obtained, create a new list of tiles with the actual data
                    gameTiles = cellListFactory(mineLocations);
                    setContent(gameTiles);

                    // Modify gamestate properties
                    // TODO Is it necessary to set remainingMines here?
                    remainingMines = thisGameSpec.gameMines;
                    openingCondition = false;

                    // Get the cell at this index again and reveal it
                    // This is necessary as thisCell currently stores the cell at this position on the dummy board
                    thisCell = gameTiles.get(thisIndex);
                    thisCell.reveal();
                }

                // Any cell that is revealed, opening condition or otherwise, should check for possibility of recursive reveal
                if (thisCell.isRevealed()) {
                    if (thisCell.getNearby() == 0) revealNeighbors(thisIndex);
                }
            }

        });
        
    }
    // Class will default to medium difficulty
    GameContainer() {
        this(GameDefaults.MEDIUM);
    }

    // Method to see if there are enough flags placed around a cell to satisfy its count of nearby mines
    // This is intentionally designed to only validate how many flags there are, not whether they're placed correctly.
    // It is primarily a safeguard against misclicks
    private boolean checkEnoughFlags(int cellIndex) {
        // Get the cell at the index
        final GameCell thisCell = gameTiles.get(cellIndex);
        // Check how many mines are nearby
        int nearby = thisCell.getNearby();
        // Iterate over every nearby cell and decrement nearby mines every time a flag is encountered
        for (Integer nearbyCell : getNeighbors(cellIndex)) {
            nearby += (gameTiles.get(nearbyCell).isFlagged()) ? -1 : 0;
        }
        // If there are at least enough flags to satisfy the node, return true
        if (nearby <= 0) return true;
        else return false;
    }

    // Takes in a game cell and checks whether there are any revealed mines around it
    public boolean checkRevealedMines(GameCell cell) {
        for (Integer index : getNeighbors(gameTiles.indexOf(cell))) if (gameTiles.get(index).isRevealed() && gameTiles.get(index).isMine()) return true;
        return false;
    }

    // Generate an appropriately sized list of yes/no values representing the presence or absence of mines in the game tiles
    private boolean[] minefieldFactory() {
        // Initialize variables for mine locations and random generation
        final boolean[] mineLocations = new boolean[thisGameSpec.boardSize];
        int minesToGenerate = thisGameSpec.gameMines;
        Random mineGenerator = new Random();

        // Generate appropriate number of mines and store in mineLocations[]
        while (minesToGenerate > 0) {
            // Generate a random number within [0, boardSize)
            final int i = mineGenerator.nextInt(thisGameSpec.boardSize);
            // If there's already a mine here, skip ahead and try again
            if (mineLocations[i]) continue;
            // Otherwise, set a mine here and decrement the counter
            mineLocations[i] = true;
            minesToGenerate--;
        }

        // Returned the filled-out boolean[]
        return mineLocations;
    }

    // Create a properly sized list of game cells provided mine location data
    private ArrayList<GameCell> cellListFactory(boolean[] minefield) {
        // Create an ArrayList pre-allocated to exactly boardSize GameCells
        final ArrayList<GameCell> cells = new ArrayList<>(thisGameSpec.boardSize);

        // Iterate over every cell index on the board
        for (int i = 0, n = thisGameSpec.boardSize; i < n; i++) {
            // Get valid neighboring indices from helper function
            final LinkedList<Integer> neighbors = getNeighbors(i);
            // Iterate over neighbor cells to count number of mines
            int nearbyMines = 0;
            for (Integer neighborIndex : neighbors) {
                if (minefield[neighborIndex]) nearbyMines++;
            }
            // Create new GameCell with mine and nearby count data, and add it to the list
            cells.add(new GameCell(minefield[i], nearbyMines));
        }
        // Return completed list
        return cells;
    }

    // Create a properly sized list of dummy cells
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

        // Convert subject cell to a Point and generate a list of every point around it
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
        // Evaluate every neighboring cell for validity and add valid ones to the final list
        for (Point neighbor : candidates) {
            // Ignore cells outside horizontal bounds
            if (neighbor.x < 0 || neighbor.x >= thisGameSpec.Columns) continue;
            // Ignore cells outside vertical bounds
            if (neighbor.y < 0 || neighbor.y >= thisGameSpec.Rows) continue;
            thisCellNeighbors.add(Integer.valueOf(thisGameSpec.getIndex(neighbor)));
        }

        return thisCellNeighbors;
    }

    // Method to attempt retrieval of the cell targeted by the Event
    public GameCell getCell(EventTarget target) {
        // If the event target isn't a cell in the list, return null
        if (!gameTiles.contains(target)) return null;
        // Otherwise fetch the cell and return it
        else {
            int index = gameTiles.indexOf(target);
            return gameTiles.get(index);
        }
    }

    public GameSpecification getGameSpec() {
        return thisGameSpec;
    }

    // Count every revealed cell on the board
    public int tallyRevealed() {
        int revealed = 0;
        for (GameCell cell : gameTiles) {
            if (cell.isRevealed()) revealed++;
        }
        return revealed;
    }

    public int getRemainingMines() {
        return remainingMines;
    }

    // Reveal all neighbors of the cell at index
    private void revealNeighbors(int index) {
        // For every valid neighbor index
        for (int neighbor : getNeighbors(index)) {
            // Retrieve the cell at that index
            final GameCell neighborTile = gameTiles.get(neighbor);
            // Recursive base case
            if (neighborTile.isRevealed()) continue;
            // Otherwise, reveal the cell
            neighborTile.reveal();
            // Recursive call - If there are no mines around this neighboring cell, reveal its neighbors in turn
            if (neighborTile.getNearby() == 0) revealNeighbors(neighbor);
        }
    }
}