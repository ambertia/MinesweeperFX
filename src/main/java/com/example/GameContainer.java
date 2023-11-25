package com.example;

import java.util.ArrayList;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

public class GameContainer extends GridPane {
    private boolean gameOpeningCondition;
    private GameDataManager thisDataManager;
    private GameInteractionManager thisInteractionManager;
    private GameSpecification thisGameSpec;
    private ArrayList<Label> gameTiles;
    private static double CELL_SIZE = 25;

    // Full constructor for GameContainer
    GameContainer(GameSpecification gameSpec) {
        System.out.println("Start of GameContainer");
        thisGameSpec = gameSpec;
        gameOpeningCondition = true;
        thisDataManager = new GameDataManager(this, thisGameSpec);
        thisInteractionManager = new GameInteractionManager(thisGameSpec, thisDataManager);

        gameTiles = new ArrayList<>(gameSpec.boardSize());
        for (int cellIndex = 0; cellIndex < gameSpec.boardSize(); cellIndex++) {
            final Label newGameTile = gameLabelFactory();
            gameTiles.add(newGameTile);
            add(newGameTile, cellIndex % gameSpec.Columns, cellIndex / gameSpec.Columns);
            System.out.println(newGameTile.toString() + GridPane.getColumnIndex(newGameTile) + GridPane.getRowIndex(newGameTile));
        }

        addEventFilter(GameCellInteractionEvent.ANY, e -> {
            System.out.println("Caught GameCellInteractionEvent");
            if (gameOpeningCondition) processGameOpening(e);
            e.consume();
            thisInteractionManager.handle(e);
        });
        System.out.println("End of GameContainer");
    }
    // Default constructor for GameContainer
    GameContainer() {
        this (new GameSpecification());
    }

    private void processGameOpening(GameCellInteractionEvent e) {
        final int gameOpeningCell = thisGameSpec.getIndex(e.getLocation());
        while (gameOpeningCondition) {
            final int currentOpeningValue = thisDataManager.getAdjacentMines(gameOpeningCell);
            if (currentOpeningValue != 0) {
                thisDataManager = new GameDataManager(this, thisGameSpec);
                thisInteractionManager = new GameInteractionManager(thisGameSpec, thisDataManager);
            }
            else gameOpeningCondition = false;
        }

    }
    
    public ArrayList<Label> getTiles() {
        return gameTiles;
    }

    public void updateLabel(int cellIndex, String labelText) {
        gameTiles.get(cellIndex).setText(labelText);
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
