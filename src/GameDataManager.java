import java.util.ArrayList;
import java.util.Random;

public class GameDataManager {
    private int totalColumns;
    private int totalRows;
    private ArrayList<Integer> mineLocations;
    private int[] nearbyMines;

    // Full featured constructor
    GameDataManager(int gameColumns, int gameRows, double mineFraction) {
        totalColumns = gameColumns;
        totalRows = gameRows;
        int minesToGenerate = Double.valueOf(gameColumns * gameRows * mineFraction).intValue();
        mineLocations = new ArrayList<>(minesToGenerate);
        Random mineGenerator = new Random();
        while (minesToGenerate > 0) {
            final int rolledIndex = mineGenerator.nextInt(gameColumns * gameRows);
            if (mineLocations.contains(rolledIndex)) continue;
            mineLocations.add(rolledIndex);
            minesToGenerate--;
        }

        nearbyMines = new int[gameColumns * gameRows];
        for (int cellIndex = 0; cellIndex < nearbyMines.length; cellIndex++) {
            nearbyMines[cellIndex] = processNeighborMines(getNeighborIndices(cellIndex));
        }
    }

    // Return a list of valid neighbor indices provided a subject cell index
    public ArrayList<Integer> getNeighborIndices(int activeCellIndex) {
        final ArrayList<Integer> activeCellNeighbors = new ArrayList<>();

        final boolean atTopEdge = (activeCellIndex / totalColumns == 0);
        final boolean atRightEdge = ((activeCellIndex % totalColumns) == totalColumns - 1);
        final boolean atBottomEdge = ((activeCellIndex / totalColumns) == totalRows - 2);
        final boolean atLeftEdge = (activeCellIndex % totalColumns == 0);

        if (!atTopEdge) {
            if (!atLeftEdge) activeCellNeighbors.add(activeCellIndex - totalColumns - 1);
            activeCellNeighbors.add(activeCellIndex - totalColumns);
            if (!atRightEdge) activeCellNeighbors.add(activeCellIndex - totalColumns + 1);
        }
        if (!atLeftEdge) activeCellNeighbors.add(activeCellIndex - 1);
        if (!atRightEdge) activeCellNeighbors.add(activeCellIndex + 1);
        if (!atBottomEdge) {
            if (!atLeftEdge) activeCellNeighbors.add(activeCellIndex + totalColumns - 1);
            activeCellNeighbors.add(activeCellIndex + totalColumns);
            if (!atRightEdge) activeCellNeighbors.add(activeCellIndex + totalColumns + 1);
        }

        return activeCellNeighbors;
    }

    // Take an ArrayList of cell indices and return the total mines within
    private int processNeighborMines(ArrayList<Integer> neighborCells) {
        int neighborMines = 0;
        for (Integer cellIndex : neighborCells) 
            if (mineLocations.contains(cellIndex)) neighborMines++;
        return neighborMines;
    }

    // Unit test method - Generated a mine board and table of neighboring mine counts, and print them
    public static void main(String[] args) {
        final GameDataManager unitTestManager = new GameDataManager(
            GameDefaults.COLUMNS, GameDefaults.ROWS, GameDefaults.MINE_FRACTION);
        
        // Print generated mine locations to console
        System.out.print("Mine locations:");
        for (int cellIndex = 0; cellIndex < unitTestManager.nearbyMines.length; cellIndex++) {
            if (cellIndex % unitTestManager.totalColumns == 0) System.out.print("\n");
            if (unitTestManager.mineLocations.contains(cellIndex)) System.out.print("*");
            else System.out.print("-");
        }

        System.out.println();

        // Print generated nearbyMines to console
        System.out.print("Nearby Mines:");
        for (int cellIndex = 0; cellIndex < unitTestManager.nearbyMines.length; cellIndex++) {
            if (cellIndex % unitTestManager.totalColumns == 0) System.out.print("\n");
            System.out.print(unitTestManager.nearbyMines[cellIndex]);
        }

        System.out.println();
    }
}
