import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

public class GameContainer extends GridPane {
    private GameDataManager thisGameDataManager;
    private Label[][] gameTiles;
    private static double CELL_SIZE = 25;

    // Full constructor for GameContainer
    GameContainer(int gameColumns, int gameRows) {
        thisGameDataManager = new GameDataManager(gameColumns, gameRows, GameDefaults.MINE_FRACTION);

    }
    GameContainer() {
        this(GameDefaults.COLUMNS, GameDefaults.ROWS);
    }
    
    // Factory method to produce a label with desired characteristics
    public static Label gameLabelFactory() {
        // Create new label object
        final Label newLabel = new Label(" ");

        // Set graphics parameters of the label
        GridPane.setHalignment(newLabel, HPos.CENTER);
        GridPane.setValignment(newLabel, VPos.CENTER);
        newLabel.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        newLabel.setPrefSize(CELL_SIZE, CELL_SIZE);
        newLabel.setAlignment(Pos.CENTER);

        // Set interactive functionality of the label
        newLabel.setOnMouseClicked(e -> {
            if (!e.getButton().equals(MouseButton.PRIMARY)) return;
            if (!e.isStillSincePress()) return;
            newLabel.fireEvent(new GameCellInteractionEvent(newLabel));
        });

        return newLabel;
    }
}
