package com.example;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.scene.input.MouseEvent;

// Class to wrap a MouseEvent with a dedicate GameCell attribute 
public class CellDataEvent extends Event {

    // Define types for the event
    public static final EventType<CellDataEvent> ANY = new EventType<CellDataEvent>("ANY");
    private final GameCell source;
    private final MouseEvent event;

    // Various constructors to handle the functionality of firing events with various input
    CellDataEvent(GameCell s, MouseEvent e) {
        super(s, s, CellDataEvent.ANY);
        source = s;
        event = e;
    }

    public MouseEvent getEvent() {
        return event;
    }
    public GameCell getGameCell() {
        return source;
    }
}