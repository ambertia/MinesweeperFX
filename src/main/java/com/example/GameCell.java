package com.example;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class GameCell extends Label {
    // Contain cell state information and graphics information to interplay with each other
    private boolean revealed;
    private boolean mine;
    private boolean flag;
    private int nearby;
    private ImageView icon;

    private static double CELL_SIZE = 25;

    GameCell(boolean hasMine, int nearbyMines) {
        // Fill out fields
        revealed = false;
        mine = hasMine;
        flag = false;
        nearby = nearbyMines;

        // Configure label content
        // ImageView configured and disabled for later
        // Label set with " " and colored according to schema
        icon = new ImageView();
        setGraphic(icon);
        icon.fitHeightProperty().bind(heightProperty());
        icon.fitWidthProperty().bind(widthProperty());
        icon.setScaleX(0.75);
        icon.setScaleY(0.75);

        setText(" ");
        setTextFill(Color.web(GameDefaults.getColor(nearbyMines)));
        setContentDisplay(ContentDisplay.TEXT_ONLY);
        setAlignment(Pos.CENTER);

        // Set cell GridPane constraints
        GridPane.setHalignment(this, HPos.CENTER);
        GridPane.setValignment(this, VPos.CENTER);
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setPrefSize(CELL_SIZE, CELL_SIZE);
        setAlignment(Pos.CENTER);

        // Set mouse click logic
        setOnMouseClicked(e -> {

            // If mouse has moved since press, do nothing
            if (!e.isStillSincePress()) {
                e.consume();
                return;
            }

            // Special behavior for flag events
            if (e.getButton().equals(MouseButton.SECONDARY) && !revealed) {
                flag = !flag;
                update();
            }
            // Left click always calls reveal() - has check for flag therein
            else if (e.getButton().equals(MouseButton.PRIMARY)) {
                reveal();
            }
            // Event should be sent either way to be processed
            fireEvent(new CellDataEvent(this, e));
        });
    }
    // Default to no mine & no nearby mines. Shouldn't need to be used.
    GameCell() {
        this(false, 0);
    }

    // Method to allow externally revealing cells
    public void reveal() {
        // Allows lazy call of reveal() function recursively while still protecting special cases
        if(revealed) return;
        if(flag) return;

        getStyleClass().add("revealed");
        getStyleClass().add(GameDefaults.getOutlineClass(nearby));
        revealed = true;
        update();
    }

    // Update label content to match gamestate
    public void update() {
        // If flagged set image to flag and display graphic
        if (flag) {
            icon.setImage(new Image("flag.png"));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return;
        }
        // If simply hidden, set label to space and hide any graphics
        else if (!revealed) {
            setText(" ");
            setContentDisplay(ContentDisplay.TEXT_ONLY);
            return;
        }
        // If revealed and a mine, set image to mine and display graphic
        else if (mine) {
            icon.setImage(new Image("mine.png"));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return;
        }
        // If revealed and not a mine, show appropriate number label
        else {
            if (nearby == 0) {
                setText(" ");
            }
            else {
                setText(Integer.valueOf(nearby).toString());
            }
            setContentDisplay(ContentDisplay.TEXT_ONLY);
        }
    }

    // Getter methods
    public int getNearby() {
        return nearby;
    }
    public boolean isFlagged() {
        return flag;
    }
    public boolean isMine() {
        return mine;
    }
    public boolean isRevealed() {
        return revealed;
    }

}
