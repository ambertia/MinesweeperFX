package com.example;

public class GameDefaults {
    // Default game settings
    public static final int COLUMNS = 40;
    public static final int ROWS = 20;
    public static final double MINE_FRACTION = 0.2;

    // String[] must be private to prevent modification of string objects inside
    private static final String[] labelColors = {"000000", "0000ff", "008400", "ff0000", "000084", "840000", "008484", "840084", "777777"};
    private static final String[] outlineClasses = {"n1", "n1", "n2", "n3", "n4", "n5", "n6", "n7", "n8"};

    // Color getters based on nearby mine count
    // Invalid indices simply return a default color
    public static String getColor(int nearbyMines) {
        try {
            return labelColors[nearbyMines];
        } catch (IndexOutOfBoundsException e) {
            return "000000";
        }
    }

    public static String getOutlineClass(int nearbyMines) {
        try {
            return outlineClasses[nearbyMines];
        } catch (IndexOutOfBoundsException e) {
            return "n1";
        }
    }
}
