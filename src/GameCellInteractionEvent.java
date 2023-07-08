import java.awt.Point;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;

public class GameCellInteractionEvent extends Event {
    public static final EventType<GameCellInteractionEvent> ANY = new EventType<>("ANY");
    private Point cellLocation;
    private MouseEvent triggerEvent;

    GameCellInteractionEvent(Label target, MouseEvent trigger) {
        super(target, target, ANY);
        cellLocation = new Point(GridPane.getColumnIndex(target), GridPane.getRowIndex(target));
        triggerEvent = trigger;
    }

    public Point getLocation() {
        return new Point(cellLocation);
    }

    public MouseEvent getTrigger() {
        return triggerEvent;
    }
    
}
