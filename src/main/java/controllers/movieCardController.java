package controllers;

import Model.VideoObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;
/**

 The movieCardController class is a JavaFX controller class for the video.fxml card GUI. It displays the movie information

 (title, year, rating, genres, and poster image) for a given {@link VideoObject} object and provides a button for

 opening a pop-up window with more information about the movie. It also has a "favorite" button for adding or removing

 the movie from the user's favorite list.

 @see VideoObject

 @see movieController

 @see popController
 **/
public class movieCardController {
    @FXML
    private ImageView poster;

    @FXML
    private Label rating;

    @FXML
    private Label year;

    @FXML
    private Label name;
    @FXML
    private Label genre;

    @FXML
    protected ImageView heart;

    @FXML
    private Button heartButton;

    protected movieController movieController = new movieController();


    public VideoObject video;

    public void setData(VideoObject video) throws FileNotFoundException {
        this.video = video;

        name.setText(video.getTitle());
        rating.setText(video.getRatings());
        year.setText(video.getYear());

        StringBuilder genres = new StringBuilder();

        for (String genre : video.getCategories()) {
            genres.append(", ").append(genre);
        }

        genre.setText(genres.substring(2));


        if(video.getIsFavorite()){
            setHeartToRed();
        }

        Image image = new Image(new FileInputStream(video.getImage()));
        poster.setImage(image);

    }

    public void popupViewMore() throws IOException {
        // Create a new Stage (window) for the pop-up
        Stage popupStage = new Stage();

        // Load the FXML file for the pop-up GUI
        // Load the FXML file for the pop-up GUI
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/popWindow.fxml"));
        Parent root = fxmlLoader.load();

        // Get the controller for the pop-up GUI

        popController popupController = fxmlLoader.getController();

        // Set the video data in the pop-up controller
        popupController.setData(video);

        // Create a Scene with the pop-up GUI and set it as the Scene of the Stage
        Scene scene = new Scene(root);
        popupStage.setScene(scene);

        // Set the title of the pop-up window

        popupStage.setTitle(video.getTitle());

        // Show the pop-up window and wait for it to be closed
        popupStage.showAndWait();
    }


    public void favlist() {

        movieController.fav(this);
    }

    public void setHeartToRed(){
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/heart.png")));
        heart.setImage(image);
    }

}
