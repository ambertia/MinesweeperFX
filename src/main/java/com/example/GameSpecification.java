package com.example;

import java.awt.Point;
/* Class to contain basic game specification information for convenience and organization */
public class GameSpecification {
    public final int Columns;
    public final int Rows;
    public final double MineFraction;
    public final int boardSize;
    public final int gameMines;

    GameSpecification(int gameColumns, int gameRows, double gameMineFraction) {
        Columns = gameColumns;
        Rows = gameRows;
        MineFraction = gameMineFraction;
        boardSize = Columns * Rows;
        gameMines = Double.valueOf(Columns * Rows * MineFraction).intValue();
    }
    GameSpecification() {
        this(GameDefaults.MEDIUM.Columns, GameDefaults.MEDIUM.Rows, GameDefaults.MEDIUM.MineFraction);
    }

    // Convenience method to change an x,y point into a linear cell index given the number of columns
    public int getIndex(Point coordinate) {
        return (coordinate.y * Columns) + coordinate.x;
    }

    public Point getPoint(int cellIndex) {
        return new Point(cellIndex % Columns, cellIndex / Columns);
    }
}
