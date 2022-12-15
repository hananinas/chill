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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // set the text color of the items in the ChoiceBox to red
        EpisodesSelectore.setStyle("-fx-text-fill: red;");
        SeasonsEpisodes.setStyle("-fx-text-fill: #fff;");


        SeasonsEpisodes.setOnAction(this::episodes);

    }

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

    // set data for shows
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

    // change the episodes according to the selected season
    public void episodes(ActionEvent event) {

        EpisodesSelectore.getItems().clear();

        Map<Integer, Integer> seasonEpisodes = castedSerie.getSeasonsEpisodes();

        for (int i = 1; i < seasonEpisodes.get(SeasonsEpisodes.getSelectionModel().getSelectedItem()) + 1; i++) {
            EpisodesSelectore.getItems().add(i);
        }
    }

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
