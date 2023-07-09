import javafx.scene.input.MouseButton;

public class GameInteractionManager {

    private GameSpecification thisGameSpec;
    private GameDataManager thisDataManager;

    GameInteractionManager(GameSpecification gameSpec, GameDataManager dataManager) {
        thisGameSpec = gameSpec;
        thisDataManager = dataManager;
    }

    public void handle(GameCellInteractionEvent e) {
        final int eventCellIndex = thisGameSpec.getIndex(e.getLocation());

        if (e.getTrigger().getButton().equals(MouseButton.SECONDARY)) {
            if (thisDataManager.isRevealed(eventCellIndex)) thisDataManager.revealNeighbors(eventCellIndex);
            else thisDataManager.flagEvent(eventCellIndex);
        }

        if (!e.getTrigger().getButton().equals(MouseButton.PRIMARY)) return;

        thisDataManager.revealEvent(eventCellIndex);

        if (thisDataManager.isMine(eventCellIndex)) {
            System.out.println("GAME LOST");
            // TODO implement game loss
        }

        if (thisGameSpec.boardSize() - thisGameSpec.getGameMines() == thisDataManager.checkRevealedCells()) {
            System.out.println("GAME WON");
            // TODO implement game win
        }

    }
}
