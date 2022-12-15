package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import Model.*;
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
    private Parent root;
    private final mainController main = new mainController();

    private List<VideoObject> fav = new LinkedList<>();

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

    public void setData(List<VideoObject> favlist) throws IOException {
        this.fav = favlist;
        search("");
    }

    public void switchToHome(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            root = fxmlLoader.load();


            mainController mainController = fxmlLoader.getController();
            mainController.setData(getList());


            stage =  (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void switchToMovies(ActionEvent event) {

        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/movies.fxml"));
            root = fxmlLoader.load();


            movieController movieController = fxmlLoader.getController();
            movieController.setData(getList());


            stage =  (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void switchToShows(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/shows.fxml"));
            root = fxmlLoader.load();


            showsController showsController = fxmlLoader.getController();
            showsController.setData(getList());


            stage =  (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void switchToFav(ActionEvent event) {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/favlist.fxml"));
            root = fxmlLoader.load();


            favController favController = fxmlLoader.getController();
            favController.setData(getList());


            stage =  (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public List<VideoObject> getList(){
        return fav;
    }




    public void search(String query) throws IOException {
        // convert the search query to lower case

        // get a list of all shows
        List<VideoObject> allVideos = main.shows().combian(main.shows().getAll(), main.movies().getAll());


        List<VideoObject> favVideos = main.shows().favSearch( query , main.shows().returnFavList(allVideos, getList()));

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
            movieCardController cardController = fxmlLoader.getController();
            cardController.hide();
            cardController.setData(filteredMoviesShows.get(i));

            if (column == 6) {
                column = 0;
                row++;
            }

            VideoLayout.add(carBox, column++, row);

        }


    }
}