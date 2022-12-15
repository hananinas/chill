package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.*;

import Model.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

public class movieController implements Initializable {

    private Stage stage;
    private Parent root;

    mainController main = new mainController();


    @FXML
    private GridPane VideoLayout;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private ScrollPane cardlayout;
    @FXML
    VBox menuItem;
    @FXML
    private ScrollPane menuPane;
    private Set<String> categories;

    public List<VideoObject> favList = new ArrayList<>();

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        // set styles for UI elements
        searchField.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff; -fx-padding: 0.5em;");
        cardlayout.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        menuPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


        try {
            // perform initial search with no query

            String start = "";
            search(start);

            // set up event listener for search button
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

            setCategories(menuItem);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }



    public void setData(List<VideoObject> favlist) throws IOException {
        this.favList = favlist;
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


            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void search(String query) throws IOException {
        // convert the search query to lower case
    
        String lowerCaseQuery = query.toLowerCase();


        // filter the list of movies and TV shows based on the search query
        List<VideoObject> filteredMovies =  main.movies().videoSearch(lowerCaseQuery);


        // clear the shows and movies
        VideoLayout.getChildren().clear();
        // update the scene with the filtered list of movies and TV shows
        updateScene(main.setFav(filteredMovies,favList));
    }

    public void updateScene(List<VideoObject> filteredMovies) throws IOException {
        int row = 0;
        int column = 1;

        for (VideoObject filteredMovie : filteredMovies) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/video.fxml"));
            VBox carBox = fxmlLoader.load();
            movieCardController cardController = fxmlLoader.getController();
            cardController.setData(filteredMovie);

            if (column == 5) {
                column = 0;
                row++;
            }

            VideoLayout.add(carBox, column++, row);

        }
    }
    public Set<String> categories() {
        return  main.movies().getAllCategories();
    }

    public void filterByCategory(String category) throws IOException {
        // filter the list of movies and TV shows based on the search query
        List<VideoObject> filteredMovies = main.movies().getVideoByCategory(category);
        // clear the shows and movies
        VideoLayout.getChildren().clear();
        // update the scene with the filtered list of movies and TV shows
        updateScene(filteredMovies);
    }
    public  void  setCategories(VBox menuItem) throws IOException{
        categories = new HashSet<>(categories());

        for (String category : categories) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/categoryButton.fxml"));
            Button catbButton = fxmlLoader.load();
            catController catController = fxmlLoader.getController();
            catController.getController(this);
            catController.setText(category);
            menuItem.getChildren().add(catbButton);
        }
    }

    //fav list code
    public void fav(movieCardController card){

            if (!card.video.getIsFavorite()) {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/heart.png")));
                main.addToList(card.video, "movie");
                main.movies().getAll();
                card.heart.setImage(image);
                card.video.setIsFavorite(true);
            } else {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/heartEmpty.png")));
                main.movies().getAll();
                main.removeToList(card.video, "movie");
                card.heart.setImage(image);
                card.video.setIsFavorite(false);
            }

    }
    public List<VideoObject> getList(){
        return favList;
    }
}
