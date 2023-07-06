import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/* Custom class to encompass all the main functions & layout arrangement of the game
 * board so that it can be passed to game application in a straightforward manner
 */

public class GameBoard extends GridPane {
    private double mineFraction;
    private int totalColumns;
    private int totalRows;
    private ArrayList<Integer> mineLocations;
    private static int DEFAULT_COLUMNS = 40;
    private static int DEFAULT_ROWS = 20;

    GameBoard() {
        totalColumns = DEFAULT_COLUMNS;
        totalRows = DEFAULT_ROWS;
        mineFraction = 0.2;
        generateMineLocations();
        initGameCells();
        setGridLinesVisible(false);

        setMinWidth(totalColumns * GameCell.MIN_CELL_SIZE);
        setMinHeight(totalRows * GameCell.MIN_CELL_SIZE);
        setMaxWidth(totalColumns * GameCell.MAX_CELL_SIZE);
        setMaxHeight(totalRows * GameCell.MAX_CELL_SIZE);
    }

    private void generateMineLocations() {
        
        Integer minesToGenerate = Double.valueOf(this.totalColumns*this.totalRows*this.mineFraction).intValue();
        this.mineLocations = new ArrayList<>(minesToGenerate);

        Random locationGenerator = new Random();
        while (minesToGenerate > 0) {
            final int rolledLocation = locationGenerator.nextInt(this.totalColumns*this.totalRows);
            if (this.mineLocations.contains(rolledLocation)) continue;
            this.mineLocations.add(rolledLocation);
            minesToGenerate--;
        }
        this.mineLocations.sort(Comparator.naturalOrder());
    }

    private void initGameCells() {
        for (int cellIndex = 0; cellIndex < (this.totalColumns * this.totalRows); cellIndex++) {
            final GameCell newCell = new GameCell(mineLocations.contains(cellIndex));
            GridPane.setConstraints(newCell, cellIndex % totalColumns, cellIndex / totalColumns);
            this.getChildren().add(newCell);
        }
    }
}
