import java.util.Random;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class MinesweeperGame extends Application{

    // Game specification constants
    /* static int GAME_SIZE_COLUMNS = 40;
    static int GAME_SIZE_ROWS = 20;
    static double MINE_FRACTION = 0.15; */

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        GameBoard activeGameBoard = new GameBoard();
        final Scene gameplayScene = new Scene(activeGameBoard, 600, 400);
        primaryStage.setScene(gameplayScene);
        primaryStage.show();
    }
    
    // Method to assign and process labels for the initial game state
    /* private static void initializeBoard(GameCell[][] gameCellBoard, boolean[][] mineLocations) {
        // Loop over board, left-to-right, top-to-bottom
        for (int row = 0; row < gameCellBoard.length; row++) {
            for (int column = 0; column < gameCellBoard[0].length; column++) {
                // Init each location as a new cell with corresponding board state
                GameCell thisCell = new GameCell(mineLocations[row][column]);;
                GridPane.setRowIndex(thisCell, row);
                GridPane.setColumnIndex(thisCell, column);
                thisCell.setNeighbors(checkNeighbors(mineLocations, row, column));
            }
        }
    } */

    /* private static int checkNeighbors(boolean[][] mineLocations, int row, int column) {
        int neighbors = 0;
        for (int rowMod = -1; rowMod <= 1; rowMod++) {
            for (int colMod = -1; colMod <= 1; colMod++) {
                // Skip iterations for invalid neighbors i.e. out-of-bounds or self
                if ((row + rowMod) < 0 || (row + rowMod) >= GAME_SIZE_ROWS) continue;
                if ((column + colMod) < 0 || (column + colMod) >= GAME_SIZE_COLUMNS) continue;
                if ((row + rowMod) == 0 && (column + colMod) == 0) continue;

                neighbors++;
            }
        }
        return neighbors;
    } */

    // Utility method to print the mine locations to the console
    /* private static void printBoard(boolean[][] gameBoard) {
        for (boolean[] row : gameBoard) {
            for (boolean tile : row) {
                System.out.print(tile ? "*" : " ");
            }
            System.out.print("\n");
        }
    } */
}
