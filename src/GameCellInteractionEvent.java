import java.awt.Point;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GameCellInteractionEvent extends Event {
    private Point cellLocation;

    GameCellInteractionEvent(Label target) {
        super(new EventType<>("GameCellInteractionEvent"));
        cellLocation = new Point(GridPane.getColumnIndex(target), GridPane.getRowIndex(target));
    }

    public int getColumn() {
        return cellLocation.x;
    }

    public int getRow() {
        return cellLocation.y;
    }
    
}
