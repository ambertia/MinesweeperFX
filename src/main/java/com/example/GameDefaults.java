package com.example;

public class GameDefaults {
    // Default game settings
    public static final int COLUMNS = 30;
    public static final int ROWS = 20;
    public static final double MINE_FRACTION = 0.15;

    // String[] must be private to prevent modification of string objects inside
    private static final String[] outlineClasses = {"n1", "n1", "n2", "n3", "n4", "n5", "n6", "n7", "n8"};

    // Color getters based on nearby mine count
    // Invalid indices simply return a default color

    public static String getTextClass(int nearbyMines) {
        try {
            return outlineClasses[nearbyMines];
        } catch (IndexOutOfBoundsException e) {
            return "n1";
        }
    }
}
