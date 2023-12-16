package com.example;

public class GameDefaults {

    // Settings defaults
    public static final GameSpecification EASY = new GameSpecification(20, 10, 0.15);
    public static final GameSpecification MEDIUM = new GameSpecification(25, 15, 0.175);
    public static final GameSpecification HARD = new GameSpecification(30, 20, 0.2);

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
