package controllers;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import Model.*;

/**
 * The `popController` class is a controller for a JavaFX pop-up window that displays details about a movie or TV show.
 * It handles the display of the title, ratings, year, genre, and poster image of the video, as well as the ability to
 * play the video. If the video is a TV show, it also allows the user to select a season and episode to watch.
 */
public class popController implements Initializable {

    @FXML
    private ImageView poster;
    @FXML
    private Label name;
    @FXML
    private Label rating;
    @FXML
    private Label year;
    @FXML
    private Label genre;
    @FXML
    private ChoiceBox<Integer> EpisodesSelectore;
    @FXML
    private ChoiceBox<Integer> SeasonsEpisodes;
    @FXML
    private Text epsiodesText;
    @FXML
    private Text seasonsText;
    @FXML
    private Button playButton;
    private int Counter = 0;

    protected SerieObject castedSerie;

    /**

     This method is called when the pop up window is initialized.

     It sets the text color of the items in the ChoiceBox objects to red and white, respectively.

     It also sets an action event for the SeasonsEpisodes ChoiceBox object, which updates the EpisodesSelectore ChoiceBox object when a season is selected.

     @param arg0 The URL of the FXML file that was used to create the pop up window.

     @param arg1 The resources used to localize the root object of the FXML file.
     **/
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // set the text color of the items in the ChoiceBox to red
        EpisodesSelectore.setStyle("-fx-text-fill: red;");
        SeasonsEpisodes.setStyle("-fx-text-fill: #fff;");


        SeasonsEpisodes.setOnAction(this::episodes);

    }

    /**
     * The `setData` method sets the data for a movie or TV show in the pop-up window. It displays the title, ratings, year,
     * and genre of the video, and sets the poster image using the file path provided in the `VideoObject` object. If the
     * video is a TV show, it also makes the season and episode selection controls visible.
     *
     * @param video the `VideoObject` object containing the data for the movie or TV show
     * @throws FileNotFoundException if the file path for the poster image is invalid
     */
    public void setData(VideoObject video) throws FileNotFoundException {
        name.setText(video.getTitle());
        rating.setText(video.getRatings());
        year.setText(video.getYear());

        StringBuilder genres = new StringBuilder();

        for (String genre : video.getCategories()) {
            genres.append(", ").append(genre);
        }

        genre.setText(genres.substring(2));

        Image image = new Image(new FileInputStream(video.getImage()));
        poster.setImage(image);

        EpisodesSelectore.setVisible(false);
        epsiodesText.setVisible(false);
        SeasonsEpisodes.setVisible(false);
        seasonsText.setVisible(false);
    }

/**
 * The `setDataShow` method is similar to the `setData` method, but it is specifically for TV shows. It sets the data for
 * the TV show in the pop-up window, and populates the season selection control with the available seasons of the show.
 *
 * @param show the `VideoObject` object containing the data for the TV show
 * @throws FileNotFoundException if the file path for the poster image
 * **/
    public void setDataShow(VideoObject show) throws FileNotFoundException {
        setData(show);
        castedSerie = (SerieObject) show;

        EpisodesSelectore.setVisible(true);
        epsiodesText.setVisible(true);
        SeasonsEpisodes.setVisible(true);
        seasonsText.setVisible(true);

        for (Map.Entry<Integer, Integer> set : castedSerie.getSeasonsEpisodes().entrySet()) {
            SeasonsEpisodes.getItems().add(set.getKey());
        }

    }


    /**
     * Changes the episodes according to the selected season.
     * @param event ActionEvent
     */
    public void episodes(ActionEvent event) {

        EpisodesSelectore.getItems().clear();

        Map<Integer, Integer> seasonEpisodes = castedSerie.getSeasonsEpisodes();

        for (int i = 1; i < seasonEpisodes.get(SeasonsEpisodes.getSelectionModel().getSelectedItem()) + 1; i++) {
            EpisodesSelectore.getItems().add(i);
        }
    }

    /**
     * Changes the color and text of the play button.
     */
    public void play() {
        Counter++;

        if (Counter == 1) {
            playButton.setStyle("-fx-border-color: #bff8a2;");
            playButton.setText("playing");
        } else {
            Counter = 0;
            playButton.setStyle("-fx-border-color: #ddd;");
            playButton.setText("play");
        }
    }

}
