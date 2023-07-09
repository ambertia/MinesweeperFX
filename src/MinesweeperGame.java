import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.MenuBar;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class MinesweeperGame extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        GameContainer activeGame = new GameContainer();
        ZoomableScrollPane gameBoardWrapper = new ZoomableScrollPane(activeGame);
        BorderPane activeGameDisplay = new BorderPane();

        activeGameDisplay.setCenter(gameBoardWrapper);

        final Scene gameplayScene = new Scene(activeGameDisplay, 1280, 720);
        gameplayScene.setUserAgentStylesheet("GameStyleControl.css");
        primaryStage.setTitle("MinesweeperFX");
        primaryStage.setScene(gameplayScene);
        primaryStage.show();
    }
}
