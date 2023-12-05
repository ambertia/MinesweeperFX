package com.example;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
        this.setGraphic(icon);
        icon.fitHeightProperty().bind(this.heightProperty());
        icon.fitWidthProperty().bind(this.widthProperty());
        icon.setScaleX(0.75);
        icon.setScaleY(0.75);
        icon.setTranslateX(-1);

        this.setText(" ");
        this.setTextFill(Color.web(GameDefaults.getColor(nearbyMines)));
        this.setContentDisplay(ContentDisplay.TEXT_ONLY);

        // Set cell GridPane constraints
        GridPane.setHalignment(this, HPos.CENTER);
        GridPane.setValignment(this, VPos.CENTER);
        this.setMinSize(USE_PREF_SIZE, USE_PREF_SIZE);
        this.setPrefSize(CELL_SIZE, CELL_SIZE);
        this.setAlignment(Pos.CENTER);
    }
    // Default to no mine, no nearby mines. Shouldn't need to be used.
    GameCell() {
        this(false, 0);
    }

    // Interactivity on label
    this.setOnMouseClicked( e -> {
        
    });

    // Update label content
    public void update() {
        // If flagged set image to flag and display graphic
        if (flag) {
            icon.setImage(new Image("flag.png"));
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return;
        }
        // If simply hidden, set label to space and hide any graphics
        else if (!revealed) {
            this.setText(" ");
            this.setContentDisplay(ContentDisplay.TEXT_ONLY);
            return;
        }
        // If revealed and a mine, set image to mine and display graphic
        else if (mine) {
            icon.setImage(new Image("mine.png"));
            this.setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
            return;
        }
        // If revealed and not a mine, show appropriate number label
        else {
            if (nearby == 0) {
                this.setText(" ");
            }
            else {
                this.setText(Integer.valueOf(nearby).toString());
            }
            this.setContentDisplay(ContentDisplay.TEXT_ONLY);
        }


    }

}
