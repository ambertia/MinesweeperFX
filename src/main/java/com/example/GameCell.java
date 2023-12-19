package com.example;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;

/*
 * This class represents the atomic elements of the game - the tiles themselves, including
 * graphical and gameplay functions.
 */
public class GameCell extends Label {
    // Contain cell state information and graphics information to interplay with each other
    private boolean revealed;
    private boolean mine;
    private boolean flag;
    private int nearby;
    private ImageView icon;

    // Default for cell size
    public static double CELL_SIZE = 25;

    // Primary constructor
    GameCell(boolean hasMine, int nearbyMines) {
        // Fill out fields
        revealed = false;
        mine = hasMine;
        flag = false;
        nearby = nearbyMines;

        // Add specific CSS metadata
        getStyleClass().add("gamecell");

        // Configure label content
        // ImageView configured and disabled for later
        icon = new ImageView();
        setGraphic(icon);
        icon.fitHeightProperty().bind(heightProperty());
        icon.fitWidthProperty().bind(widthProperty());
        icon.setScaleX(0.75);
        icon.setScaleY(0.75);
        
        // Label set with " " and colored according to schema
        setText(" ");
        getStyleClass().add(GameDefaults.getTextClass(nearby));
        setContentDisplay(ContentDisplay.TEXT_ONLY);
        setAlignment(Pos.CENTER);

        // Set cell GridPane constraints
        GridPane.setHalignment(this, HPos.CENTER);
        GridPane.setValignment(this, VPos.CENTER);
        setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        setPrefSize(CELL_SIZE, CELL_SIZE);
        setAlignment(Pos.CENTER);

        // This filter processes clicks during the capture phase for consistency. Without
        // it, clicks on the actual text section of a tile cause problems because the
        // event target is technically a Text object, not GameCell
        addEventFilter(MouseEvent.MOUSE_CLICKED, e -> {
            // Any and all clicks on the game board that aren't StillSincePress are consumed immediately
            if (!e.isStillSincePress()) {
                e.consume();
                return;
            }
            // If the actual event target is the Text within this GameCell, "steal" the event back
            if (e.getTarget() instanceof Text) {
                e.consume();
                fireEvent(e);
            }
        });

        // Set mouse click logic during bubbling phase
        setOnMouseClicked(e -> {

            // If mouse has moved since press, do nothing
            // TODO This is probably redundant
            if (!e.isStillSincePress()) {
                e.consume();
                return;
            }

            // Functionality for flagging cells
            if (e.getButton().equals(MouseButton.SECONDARY) && !revealed) {
                flag = !flag;
                update();
            }

            // Left click always calls reveal() - has check for flag therein
            else if (e.getButton().equals(MouseButton.PRIMARY)) {
                // Even though reveal() has a check for flag condition, the event must be
                // consumed here as it could otherwise bubble up to the Container or Application
                if (flag) e.consume();
                reveal();
            }
        });
    }
    // Default to no mine & no nearby mines. This is used for convenience making the dummy boards.
    GameCell() {
        this(false, 0);
    }

    // Method to allow externally revealing cells
    public void reveal() {
        // Allows lazy call of reveal() function recursively while still protecting special cases
        if(revealed) return;
        if(flag) return;

        // If there aren't any special circumstances, change the cell styling and update its label content
        getStyleClass().add("revealed");
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
        // If revealed and a mine, set image to mine, set border red, and display graphic
        else if (mine) {
            icon.setImage(new Image("mine.png"));
            setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            setStyle("-fx-border-color: #ff0000;");
            return;
        }
        
        // If revealed and not a mine, show appropriate number label
        else {
            if (nearby == 0) {
                setText(" ");
            }
            else {
                setText(String.valueOf(nearby));
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
