import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

public class GameCell extends Label {

    public static double MAX_CELL_SIZE = 25;
    public static double MIN_CELL_SIZE = 25;
    public static double CELL_SIZE = 25;
    private boolean hasMine;
    private boolean revealed;
    /* private GameCell upCell;
    private GameCell downCell;
    private GameCell leftCell;
    private GameCell rightCell; */
    // public static BorderStroke cellBorderStroke = new BorderStroke(Color.BLACK, null, null, BorderStroke.MEDIUM);
    // public static Border cellBorder = new Border(cellBorderStroke);

    GameCell(boolean hasMine) {
        super(hasMine ? "*" : " ");
        GridPane.setHalignment(this, HPos.CENTER);
        GridPane.setValignment(this, VPos.CENTER);
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setPrefSize(CELL_SIZE, CELL_SIZE);
        setAlignment(Pos.CENTER);
        this.hasMine = hasMine;
        revealed = false;

        setOnMouseClicked(e -> {
            System.out.println(e.toString());
            if (!e.getButton().equals(MouseButton.PRIMARY)) return;
            if (!e.isStillSincePress()) return;            revealed = true;
            updateLabel();
        });
    }
    GameCell() {
        this(true);
    }

    private void updateLabel() {
        if (hasMine) setText("M");
        else setText("E");
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
    } */
    /* public ArrayList<GameCell> getNeighbors() {
        return neighbors;
    }
    public void setNeighbors(ArrayList<GameCell> neighbors) {
        this.neighbors = neighbors;
    } */
}