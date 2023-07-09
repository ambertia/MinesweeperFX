import java.awt.Point;
/* Class to contain basic game specification information for convenience and organization */
public class GameSpecification {
    public final int Columns;
    public final int Rows;
    public final double MineFraction;

    GameSpecification(int gameColumns, int gameRows, double gameMineFraction) {
        Columns = gameColumns;
        Rows = gameRows;
        MineFraction = gameMineFraction;
    }
    GameSpecification() {
        this(GameDefaults.COLUMNS, GameDefaults.ROWS, GameDefaults.MINE_FRACTION);
    }

    // Convenience method to return the size of the board
    public int boardSize() {
        return Columns * Rows;
    }
    // Convenience method to return the number of mines the game should have based on the board dimensions and mine fraction
    public int getGameMines() {
        return Double.valueOf(Columns * Rows * MineFraction).intValue();
    }

    // Convenience method to change an x,y point into a linear cell index given the number of columns
    public int getIndex(Point coordinate) {
        return (coordinate.y * Columns) + coordinate.x;
    }

    // Convenience method to change a linear cell index into an x,y point given the number of columns
    /* public Point getPoint(int cellIndex) {
        return new Point(cellIndex % Columns, cellIndex / Columns);
    } */
}
