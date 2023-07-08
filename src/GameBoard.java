import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/*  Custom class to encompass all the main functions & layout arrangement of the game
    board so that it can be passed to game application in a straightforward manner
 */

public class GameBoard extends GridPane {

    // Default parameters
    private static int DEFAULT_COLUMNS = 40;
    private static int DEFAULT_ROWS = 20;
    private static double DEFAULT_MINE_FRACTION = 0.2;

    // Instance parameters
    private double mineFraction;
    private int totalColumns;
    private int totalRows;
    private ArrayList<Integer> mineLocations;
    private ArrayList<GameCell> allCells;

    // Full featured constructor
    GameBoard(int newColumns, int newRows, double newMineFraction) {

        // Set instance parameters
        totalColumns = newColumns;
        totalRows = newRows;
        mineFraction = newMineFraction;
        mineLocations = generateMineLocations();
        allCells = new ArrayList<>(totalColumns * totalRows);

        // Instance methods to initialize the game state
        initGameCells();

        // JavaFX GridPane parameters
        setMinWidth(totalColumns * GameCell.CELL_SIZE);
        setMinHeight(totalRows * GameCell.CELL_SIZE);
        setMaxWidth(totalColumns * GameCell.CELL_SIZE);
        setMaxHeight(totalRows * GameCell.CELL_SIZE);

        addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            System.out.println(e.toString());
            if (!e.getButton().equals(MouseButton.PRIMARY)) return;
            if (!e.isStillSincePress()) return;
            final Object eventNode = e.getSource();
            if (eventNode.getClass().isInstance(allCells.get(0))) {
                System.out.println(GridPane.getColumnIndex(eventNode));
            }
        });
    }
    // Shortcut constructor - only board size
    GameBoard(int newColumns, int newRows) {
        this(newColumns, newRows, DEFAULT_MINE_FRACTION);
    }
    // Shortcut constructor - fully default board
    GameBoard() {
        this (DEFAULT_COLUMNS, DEFAULT_ROWS);
    }

    // Generate the mine locations for this gameboard instance
    private ArrayList<Integer> generateMineLocations() {
        
        // Initialize parameters for mine generation using instance properties
        Integer minesToGenerate = Double.valueOf(this.totalColumns*this.totalRows*this.mineFraction).intValue();
        final ArrayList<Integer> generatedMines = new ArrayList<>(minesToGenerate);

        // Use a random number generator to create mine locations.
        Random locationGenerator = new Random();
        // While mines still need to be generated...
        while (minesToGenerate > 0) {
            // Pick a random index up to the total board size
            final int rolledLocation = locationGenerator.nextInt(this.totalColumns*this.totalRows);
            // If this index has already been declared a mine, go back and roll a new one
            if (this.mineLocations.contains(rolledLocation)) continue;
            // Otherwise, add this index to the list of mines & subtract one from number of mines to be generated
            generatedMines.add(rolledLocation);
            minesToGenerate--;
        }
        // Sort the mine locations
        generatedMines.sort(Comparator.naturalOrder());
        return generatedMines;
    }

    // Initialize all of the game cells for this instance
    private void initGameCells() {
        // For every board index...
        for (int cellIndex = 0; cellIndex < (this.totalColumns * this.totalRows); cellIndex++) {
            // Initialize a new cell including if this index is a mine in mineLocations
            final GameCell newCell = new GameCell(mineLocations.contains(cellIndex), 0);
            // Set this cell's location in the GridPane
            GridPane.setConstraints(newCell, cellIndex % totalColumns, cellIndex / totalColumns);
            // Create bidirectional references between this object and the new cell
            allCells.add(newCell);
            newCell.setParent(this);
            // 
            for (Integer neighboringIndex : getNeighborIndices(cellIndex)) {
                if (mineLocations.contains(neighboringIndex)) newCell.setAdjacentMines(newCell.getAdjacentMines() + 1);
            }
        }
        getChildren().addAll(allCells);
    }

    public ArrayList<Integer> getNeighborIndices(GameCell activeCell) {
        return getNeighborIndices(allCells.indexOf(activeCell));
    }
    public ArrayList<Integer> getNeighborIndices(int activeCellIndex) {
        final ArrayList<Integer> activeCellNeighbors = new ArrayList<>();

        final boolean atTopEdge = (activeCellIndex < totalColumns);
        final boolean atRightEdge = ((activeCellIndex % totalColumns) == totalColumns - 1);
        final boolean atBottomEdge = ((activeCellIndex / totalColumns) == totalRows - 2);
        final boolean atLeftEdge = (activeCellIndex % totalColumns == 0);

        if (!atTopEdge) {
            if (!atLeftEdge) activeCellNeighbors.add(activeCellIndex - totalColumns - 1);
            activeCellNeighbors.add(activeCellIndex - totalColumns);
            if (!atRightEdge) activeCellNeighbors.add(activeCellIndex - totalColumns + 1);
        }
        if (!atLeftEdge) activeCellNeighbors.add(activeCellIndex - 1);
        if (!atRightEdge) activeCellNeighbors.add(activeCellIndex + 1);
        if (!atBottomEdge) {
            if (!atLeftEdge) activeCellNeighbors.add(activeCellIndex + totalColumns - 1);
            activeCellNeighbors.add(activeCellIndex + totalColumns);
            if (!atRightEdge) activeCellNeighbors.add(activeCellIndex + totalColumns + 1);
        }

        return activeCellNeighbors;
    }

    public void waterfallNeighbors(GameCell activeCell) {
        final AbstractSet<GameCell> waterfallPool = new AbstractSet<GameCell>();
        for (Integer neighborCellIndex : getNeighborIndices(activeCell)) waterfallPool.add(allCells.get(neighborCellIndex));
    }
}
