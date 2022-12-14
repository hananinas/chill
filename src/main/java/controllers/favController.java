package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import Model.*;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class favController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private final mainController main = new mainController();


    public void setData(ObservableList<VideoObject> favlist) {
        main.favList = favlist;
    }

    @FXML
    private GridPane VideoLayout;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private ScrollPane cardlayout;


    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        searchField.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff; -fx-padding: 0.5em;");
        cardlayout.setStyle("-fx-background: transparent; -fx-background-color: transparent;");



        try {

            String start = "";
            search(start);

            searchButton.setOnAction(event -> {
                // get the search query from the text field
                String query = searchField.getText();

                // search for movies and TV shows matching the query
                try {
                    search(query);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ObservableList<VideoObject>  returnFav() {
        return main.favList;
    }



    public void switchToHome(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            root = fxmlLoader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            mainController main = fxmlLoader.getController();
            main.setData(main.favList);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void switchToMovies(ActionEvent event) throws IOException {
        main.switchToMovies(event);
    }

    public void switchToShows(ActionEvent event) throws IOException {
        main.switchToShows(event);
    }
    public void switchToFav(ActionEvent event) throws IOException{
        main.switchToFav(event);
    }

    public void search(String query) throws IOException {
        // convert the search query to lower case

        String lowerCaseQuery = query.toLowerCase();

        List<VideoObject> allVideos = Stream.concat(main.shows().getAll().stream(), main.movies().getAll().stream())
                .filter(v -> v.getTitle().toLowerCase().contains(query.toLowerCase()))
                .toList();


        List<VideoObject> favVideos = main.shows().favSearch( query , main.shows().returnFavList(allVideos,main.getList()));



        // clear the shows and movies
        VideoLayout.getChildren().clear();
        // update the scene with the filtered list of movies and TV shows
        updateSceneWithShowsMovies(favVideos);
    }



    public void updateSceneWithShowsMovies(List<VideoObject> filteredMoviesShows) throws IOException {
        int row = 0;
        int column = 1;

        for (int i = 0; i < filteredMoviesShows.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/video.fxml"));
            VBox carBox = fxmlLoader.load();
            MovieCardController cardController = fxmlLoader.getController();
            cardController.setData(filteredMoviesShows.get(i));

            if (column == 5) {
                column = 0;
                row++;
            }

            VideoLayout.add(carBox, column++, row);

        }


    }
}
