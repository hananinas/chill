import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 * The main class of a JavaFX application.
 */
public class App extends Application {

    /**
     * Sets up the main stage of the application and displays it.
     *
     * @param primaryStage the main stage of the application
     */
    @Override
    public void start(Stage primaryStage) throws Exception {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/home.fxml"));
            primaryStage.setScene(new Scene(root, 1400, 800));
            primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("/img/Logo.png")));
            primaryStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws Exception {
        launch(args);
    }
}
