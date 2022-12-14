package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class ShowCardController {
    @FXML
    private Label genre;

    @FXML
    private ImageView poster;

    @FXML
    private Label rating;

    @FXML
    private Label year;

    @FXML
    private Label name;

    @FXML
    private Label season;
    @FXML
    private Button popupButton;

    private VideoObject video;

    public void setData(VideoObject show) throws FileNotFoundException {
        this.video = show;

        name.setText(show.getTitle());
        rating.setText(show.getRatings());
        year.setText(show.getYear());

        String genres = "";

        for (String genre : show.getCategories()) {
            genres += ", " + genre;
        }

        genre.setText(genres.substring(2));

        SerieObject castedSerie = (SerieObject) show;

        season.setText("" + castedSerie.getSeasonsEpisodes().size());

        Image image = new Image(new FileInputStream(show.getImage()));
        poster.setImage(image);
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

}