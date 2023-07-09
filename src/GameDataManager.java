import java.util.ArrayList;
import java.util.Random;

public class GameDataManager {

    // Instance properties
    private GameContainer thisGameContainer;
    private GameSpecification thisGameSpec;
    private ArrayList<Integer> mineLocations;
    private GameCell[] cellData;

    // Full featured constructor
    GameDataManager(GameContainer gameContainer, GameSpecification gameSpec) {
        System.out.println("Start of GameDataManager");

        // Assign dimensions
        thisGameContainer = gameContainer;
        thisGameSpec = gameSpec;

        // Retreive number of mines & prepare ArrayList and Random to generate mine lcoations
        int minesToGenerate = gameSpec.getGameMines();
        mineLocations = new ArrayList<>(minesToGenerate);
        Random mineGenerator = new Random();

        // Roll locations and record unique locations until enough mine locations have been marked
        while (minesToGenerate > 0) {
            final int rolledIndex = mineGenerator.nextInt(gameSpec.boardSize());
            if (mineLocations.contains(rolledIndex)) continue;
            mineLocations.add(rolledIndex);
            minesToGenerate--;
        }

        // Prepare appropriately sized array for cell state data
        cellData = new GameCell[gameSpec.boardSize()];
        // Initialize all cells & store properties to be retreived later
        for (int cellIndex = 0; cellIndex < cellData.length; cellIndex++) {
            cellData[cellIndex] = new GameCell();
            cellData[cellIndex].nearbyMines = processNeighborMines(getNeighborIndices(cellIndex));
            if (mineLocations.contains(cellIndex)) cellData[cellIndex].hasMine = true;
        }
        System.out.println("End of GameDataManager");
    }

    // Return a list of valid neighbor indices provided a subject cell index
    private ArrayList<Integer> getNeighborIndices(int activeCellIndex) {
        // Initialize a list for cell indices
        final ArrayList<Integer> activeCellNeighbors = new ArrayList<>();

        // Use cell index & board parameters to determine whether a cell rests on any edges of the board
        final boolean atTopEdge = (activeCellIndex / thisGameSpec.Columns == 0);
        final boolean atRightEdge = ((activeCellIndex % thisGameSpec.Columns) == thisGameSpec.Columns - 1);
        final boolean atBottomEdge = ((activeCellIndex / thisGameSpec.Columns) == thisGameSpec.Rows - 1);
        final boolean atLeftEdge = (activeCellIndex % thisGameSpec.Columns == 0);

        // Add all valid neighbor cells depending on which edges the cell is or is not resting on
        if (!atTopEdge) {
            if (!atLeftEdge) activeCellNeighbors.add(activeCellIndex - thisGameSpec.Columns - 1);
            activeCellNeighbors.add(activeCellIndex - thisGameSpec.Columns);
            if (!atRightEdge) activeCellNeighbors.add(activeCellIndex - thisGameSpec.Columns + 1);
        }
        if (!atLeftEdge) activeCellNeighbors.add(activeCellIndex - 1);
        if (!atRightEdge) activeCellNeighbors.add(activeCellIndex + 1);
        if (!atBottomEdge) {
            if (!atLeftEdge) activeCellNeighbors.add(activeCellIndex + thisGameSpec.Columns - 1);
            activeCellNeighbors.add(activeCellIndex + thisGameSpec.Columns);
            if (!atRightEdge) activeCellNeighbors.add(activeCellIndex + thisGameSpec.Columns + 1);
        }

        activeCellNeighbors.add(activeCellIndex);

        // Return the final list of cell indices
        return activeCellNeighbors;
    }

    // Take an ArrayList of cell indices and return the total mines within
    private int processNeighborMines(ArrayList<Integer> neighborCells) {
        int neighborMines = 0;
        for (Integer cellIndex : neighborCells) 
            if (mineLocations.contains(cellIndex)) neighborMines++;
        return neighborMines;
    }

    public int checkRevealedCells() {
        int revealedCells = 0;
        for (GameCell cell : cellData) if (cell.revealed) revealedCells++;
        return revealedCells;
    }

    
    public boolean isMine(int cellIndex) {
        return mineLocations.contains(cellIndex);
    }
    
    public boolean isFlagged(int cellIndex) {
        return cellData[cellIndex].flagged;
    }
    
    public boolean isRevealed(int cellIndex) {
        return cellData[cellIndex].revealed;
    }
    
    public int getAdjacentMines(int cellIndex) {
        return cellData[cellIndex].nearbyMines;
    }
    
    public void flagEvent(int cellIndex) {
        System.out.println("Flag event " + cellIndex);
        cellData[cellIndex].flagged = cellData[cellIndex].flagged ? false : true;
        thisGameContainer.getTiles().get(cellIndex).setText(cellData[cellIndex].getLabel());
    }

    public void revealEvent(int cellIndex) {
        if (cellData[cellIndex].flagged) return;
        System.out.println("Revealed " + cellIndex);
        cellData[cellIndex].revealed = true;
        thisGameContainer.getTiles().get(cellIndex).setText(cellData[cellIndex].getLabel());
        thisGameContainer.getTiles().get(cellIndex).setId("revealed-cell");
        if (cellData[cellIndex].nearbyMines == 0) revealNeighbors(cellIndex);
    }

    public void revealNeighbors(int cellIndex) {
        final ArrayList<Integer> neighborIndices = getNeighborIndices(cellIndex);
            for (Integer neighborCell : neighborIndices) if (!cellData[neighborCell].revealed) revealEvent(neighborCell);
    }

    // Unit test method - Generated a mine board and table of neighboring mine counts, and print them
    public static void main(String[] args) {
        final GameDataManager unitTestManager = new GameDataManager(null, new GameSpecification());
        
        // Print generated mine locations to console
        System.out.print("Mine locations:");
        for (int cellIndex = 0; cellIndex < unitTestManager.cellData.length; cellIndex++) {
            if (cellIndex % unitTestManager.thisGameSpec.Columns == 0) System.out.print("\n");
            if (unitTestManager.mineLocations.contains(cellIndex)) System.out.print("*");
            else System.out.print("-");
        }

        System.out.println();

        // Print generated nearbyMines to console
        System.out.print("Nearby Mines:");
        for (int cellIndex = 0; cellIndex < unitTestManager.cellData.length; cellIndex++) {
            if (cellIndex % unitTestManager.thisGameSpec.Columns == 0) System.out.print("\n");
            System.out.print(unitTestManager.cellData[cellIndex].nearbyMines);
        }

        System.out.println();
    }

    /* Internal class to represent state information of a particular cell */
    private class GameCell {
        private boolean revealed;
        private boolean hasMine;
        private boolean flagged;
        private int nearbyMines;

        GameCell() {
            revealed = false;
            hasMine = false;
            flagged = false;
            nearbyMines = 0;
        }

        private String getLabel() {
            if (flagged) return "F";
            if (!revealed) return " ";
            if (hasMine) return "*";
            if (nearbyMines == 0) return " ";
            return String.valueOf(nearbyMines);
        }
    }
}
