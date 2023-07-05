import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/* Custom class to encompass all the main functions & layout arrangement of the game
 * board so that it can be passed to game application in a straightforward manner
 */

public class GameBoard extends GridPane {
    // private double mineFraction;
    private int totalColumns;
    private int totalRows;
    // private ArrayList<Integer> mineLocations;
    // private SimpleDoubleProperty tileSize;

    private static int DEFAULT_COLUMNS = 40;
    private static int DEFAULT_ROWS = 20;

    GameBoard() {
        // super(Orientation.HORIZONTAL);
        totalColumns = DEFAULT_COLUMNS;
        totalRows = DEFAULT_ROWS;
        // this.setPrefColumns(DEFAULT_COLUMNS);
        // this.mineFraction = 0.15;
        // this.generateMineLocations();
        initGameCells();
        // this.setPrefTileHeight(20);
        // this.setPrefTileWidth(20);
        // this.tileSize = new SimpleDoubleProperty();
        // refreshTileSize();
        // this.prefTileHeightProperty().bind(tileSize);
        // this.prefTileWidthProperty().bind(tileSize);
        // this.widthProperty().addListener((v, oldValue, newValue) -> refreshTileSize());
        setAlignment(Pos.CENTER);
        setGridLinesVisible(false);

        setMinWidth(totalColumns * GameCell.MIN_CELL_SIZE);
        setMinHeight(totalRows * GameCell.MIN_CELL_SIZE);
        setMaxWidth(totalColumns * GameCell.MAX_CELL_SIZE);
        setMaxHeight(totalRows * GameCell.MAX_CELL_SIZE);
    }

    /* private void generateMineLocations() {
        
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
    } */

    private void initGameCells() {
        for (int cellIndex = 0; cellIndex < (this.totalColumns * this.totalRows); cellIndex++) {
            final GameCell newCell = new GameCell();
            GridPane.setConstraints(newCell, cellIndex % totalColumns, cellIndex / totalColumns);
            this.getChildren().add(newCell);
        }
    }

    /* private void refreshTileSize() {
        // If ratio of desired board size to 
        if ((totalColumns/totalRows) >= this.getWidth()/this.getHeight()) {

        }
    } */

    public void zoomIn() {
        setScaleX(Math.min(getScaleX() + 0.2, 1.6));
        setScaleY(Math.min(getScaleY() + 0.2, 1.6));
    }

    public void zoomOut() {
        setScaleX(Math.max(getScaleX() - 0.2, 0.6));
        setScaleY(Math.max(getScaleY() - 0.2, 0.6));
    }

    public String getScales() {
        StringBuilder formattedScales = new StringBuilder(",");
        formattedScales.insert(0, Double.valueOf(getScaleX()).toString());
        formattedScales.append(Double.valueOf(getScaleY()).toString());
        return formattedScales.toString();
    }
}
