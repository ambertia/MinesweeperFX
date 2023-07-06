import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.paint.Color;

public class GameCell extends Label {

    public static double MAX_CELL_SIZE = 25;
    public static double MIN_CELL_SIZE = 25;
    // public static BorderStroke cellBorderStroke = new BorderStroke(Color.BLACK, null, null, BorderStroke.MEDIUM);
    // public static Border cellBorder = new Border(cellBorderStroke);

    GameCell() {
        super("*");
        GridPane.setHgrow(this, Priority.ALWAYS);
        GridPane.setVgrow(this, Priority.ALWAYS);
        GridPane.setHalignment(this, HPos.CENTER);
        GridPane.setValignment(this, VPos.CENTER);
        setMinSize(MIN_CELL_SIZE, USE_PREF_SIZE);
        setMaxSize(MAX_CELL_SIZE, USE_PREF_SIZE);
        prefHeightProperty().bind(widthProperty());
        setAlignment(Pos.CENTER);
    }
    /* private boolean mine;
    private boolean shown;
    private int neighbors;

    GameCell(boolean cellState) {
        super(" ");
        mine = cellState;
        shown = false;
        neighbors = 0;
    }
    GameCell() {
        this(false);
    }

    public String toString() {
        if (!shown) return " ";
        if (mine) return "*";
        if (neighbors > 0) return String.valueOf(neighbors);
        return " ";
    }

    public void updateLabel() {
        this.setText(this.toString());
    }

    public boolean isMine() {
        return mine;
    }
    public void setMine(boolean mine) {
        this.mine = mine;
    }
    public boolean isShown() {
        return shown;
    }
    public void setShown(boolean shown) {
        this.shown = shown;
    }
    public int getNeighbors() {
        return neighbors;
    }
    public void setNeighbors(int neighbors) {
        this.neighbors = neighbors;
    }         */
}