import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class GameCell extends Label {

    GameCell() {
        super("*");
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