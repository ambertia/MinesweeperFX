import java.util.ArrayList;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;

public class GameContainer extends GridPane {
    private int totalColumns;
    private int totalRows;
    private GameDataManager thisDataManager;
    private GameInteractionManager thisInteractionManager;
    private ArrayList<Label> gameTiles;
    private static double CELL_SIZE = 25;

    // Full constructor for GameContainer
    GameContainer(int gameColumns, int gameRows) {
        System.out.println("Start of GameContainer");
        totalColumns = gameColumns;
        totalRows = gameRows;

        thisDataManager = new GameDataManager(gameColumns, gameRows, GameDefaults.MINE_FRACTION);
        thisInteractionManager = new GameInteractionManager();

        gameTiles = new ArrayList<>(gameColumns * gameRows);
        for (int cellIndex = 0; cellIndex < gameColumns * gameRows; cellIndex++) {
            final Label newGameTile = gameLabelFactory();
            gameTiles.add(newGameTile);
            add(newGameTile, cellIndex % gameColumns, cellIndex / gameColumns);
            System.out.println(newGameTile.toString() + GridPane.getColumnIndex(newGameTile) + GridPane.getRowIndex(newGameTile));
        }

        addEventFilter(GameCellInteractionEvent.ANY, e -> {
            System.out.println("Caught GameCellInteractionEvent");
            e.consume();
            handleInteractionEvent(e);
        });
        System.out.println("End of GameContainer");
    }
    // Default constructor for GameContainer
    GameContainer() {
        this(GameDefaults.COLUMNS, GameDefaults.ROWS);
    }

    private void handleInteractionEvent(GameCellInteractionEvent e) {
        final int activeCellIndex = (e.getLocation().y * totalColumns) + e.getLocation().x;
        if (thisDataManager.isRevealed(activeCellIndex)) return;
        if (e.getTrigger().getButton().equals(MouseButton.PRIMARY) && !thisDataManager.isFlagged(activeCellIndex))
            gameTiles.get(activeCellIndex).setText(thisDataManager.revealEvent(activeCellIndex));
        if (e.getTrigger().getButton().equals(MouseButton.SECONDARY))
            gameTiles.get(activeCellIndex).setText(thisDataManager.flagEvent(activeCellIndex));
    }
    
    // Factory method to produce a label with desired characteristics
    private static Label gameLabelFactory() {
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
            if (!e.isStillSincePress()) return;
            newLabel.fireEvent(new GameCellInteractionEvent(newLabel, e));
            System.out.println("Fired GameCellInteractionEvent");
        });

        return newLabel;
    }
}
