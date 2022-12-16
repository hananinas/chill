package controllers;

import Model.SerieObject;
import Model.VideoObject;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Objects;

/**

 The cardController class is responsible for displaying movie and TV show information in a card layout.

 It displays the title, year, rating, genre, and poster image of a movie or TV show.

 It also allows the user to view more details about the movie or TV show in a pop-up window by clicking the "View More" button.

 The cardController class has a method to add the movie or TV show to the user's favorite list.

 @see mainController
 */
public class cardController {

    @FXML
    private Label genre;

    @FXML
    private ImageView poster;

    @FXML
    private Label name;

    @FXML
    private Label rating;

    @FXML
    private Label year;

    @FXML
    private Label season;

    @FXML
    private Label seasonstext;
    @FXML
    protected ImageView heart;

    protected mainController main = new mainController();


    protected VideoObject video;

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

        season.setVisible(false);
        seasonstext.setVisible(false);

        if(video.getIsFavorite()){
            setHeartToRed();
        }

        // add image to the movie or show
        Image image = new Image(new FileInputStream(video.getImage()));
        poster.setImage(image);

    }

    // set data for shows
    public void setDataShow(VideoObject show) throws FileNotFoundException {
        this.video = show;

        setData(show);

        season.setVisible(true);
        seasonstext.setVisible(true);

        SerieObject castedSerie = (SerieObject) show;

        season.setText("" + castedSerie.getSeasonsEpisodes().size());
    }

    public void popupViewMore() throws IOException {
        // Create a new Stage (window) for the pop-up
        Stage popupStage = new Stage();

        // Load the FXML file for the pop-up GUI
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/popWindow.fxml"));
        Parent root = fxmlLoader.load();

        // Get the controller for the pop-up GUI

        popController popupController = fxmlLoader.getController();

        // Set the video data in the pop-up controller

        if (season.getText().equals("")) {
            System.out.println(season.getText());
            popupController.setData(video);
        } else {
            popupController.setDataShow(video);
        }

        // Create a Scene with the pop-up GUI and set it as the Scene of the Stage
        Scene scene = new Scene(root);
        popupStage.setScene(scene);

        // Set the title of the pop-up window

        popupStage.setTitle(video.getTitle());

        // Show the pop-up window and wait for it to be closed
        popupStage.showAndWait();
    }

    public void favlist() {
        main.fav(season.getText(),this );
    }

    public void setHeartToRed(){
        Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/heart.png")));
        heart.setImage(image);
    }
}
