import java.util.ArrayList;
import java.util.Comparator;
import java.util.Random;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Orientation;
import javafx.scene.layout.TilePane;

public class GameBoard extends TilePane {
    // private double mineFraction;
    private int totalColumns;
    private int totalRows;
    // private ArrayList<Integer> mineLocations;
    // private SimpleDoubleProperty tileSize;

    private static int DEFAULT_COLUMNS = 40;
    private static int DEFAULT_ROWS = 20;

    GameBoard() {
        super(Orientation.HORIZONTAL);
        this.setPrefColumns(DEFAULT_COLUMNS);
        // this.mineFraction = 0.15;
        // this.generateMineLocations();
        this.initGameCells();
        /* this.setPrefTileHeight(10);
        this.setPrefTileWidth(10); */
        /* this.tileSize = new SimpleDoubleProperty();
        refreshTileSize();
        this.prefTileHeightProperty().bind(tileSize);
        this.prefTileWidthProperty().bind(tileSize); */
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
        for (int cellIndex = 0; cellIndex < (this.totalColumns*this.totalRows); cellIndex++) {
            System.out.println("adding cell " + cellIndex);
            final GameCell newCell = new GameCell();
            this.getChildren().add(newCell);
        }
    }

    /* private void refreshTileSize() {
        final double maxHeightConstraint = this.getHeight()/this.getPrefColumns();
        final double maxWidthConstraint = this.getWidth()/((this.totalColumns*this.totalRows)/this.getPrefColumns());
        this.tileSize.set(Math.min(maxHeightConstraint, maxWidthConstraint));
    } */
}
