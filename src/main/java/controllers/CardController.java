package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.util.converter.IntegerStringConverter;

import java.util.EventListener;
import java.util.List;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;

import Model.*;

public class CardController {
    @FXML
    private HBox box;

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
    private Button popupButton;
    @FXML
    private ImageView heart;
    @FXML
    private Button heartButton;

    List<VideoObject> favList;

    public VideoObject video;
    private int counter = 0;

    public void setData(VideoObject video) throws FileNotFoundException {
        this.video = video;

        name.setText(video.getTitle());
        rating.setText(video.getRatings());
        year.setText(video.getYear());

        String genres = "";

        for (String genre : video.getCategories()) {
            genres += ", " + genre;
        }

        genre.setText(genres.substring(2));

        season.setVisible(false);
        seasonstext.setVisible(false);

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

    public void popupViewMore(ActionEvent event) throws IOException {
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

    public List<VideoObject> getFavList() {
        return favList;
    }

    public void favlist(ActionEvent event) {

        if (season.getText().equals("")) {

            DataAccess filmdata = new VideoData();
            Display film = new Display(filmdata, "src/main/resources/data/film.txt", "src/main/resources/images/filmplakater");
            film.VideoData();

            film.favListControl(name.getText());

            counter++;

            if (counter == 1) {
                Image image = new Image(getClass().getResourceAsStream("/img/heart.png"));
                heart.setImage(image);
            } else if (counter == 2) {
                Image image = new Image(getClass().getResourceAsStream("/img/heartEmpty.png"));
                heart.setImage(image);
                counter = 0;
            }

        } else {

            DataAccess showdata = new VideoData();
            Display show = new Display(showdata, "src/main/resources/data/serier.txt", "src/main/resources/images/serieforsider");
            show.VideoData();

            show.favListControl(name.getText());

            counter++;

            if (counter == 1) {
                Image image = new Image(getClass().getResourceAsStream("/img/heart.png"));
                heart.setImage(image);
            } else if (counter == 2) {
                Image image = new Image(getClass().getResourceAsStream("/img/heartEmpty.png"));
                heart.setImage(image);
                counter = 0;
            }
        }

    }

}