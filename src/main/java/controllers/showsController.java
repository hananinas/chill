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
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class showsController implements Initializable {

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
    VBox menuItem;
    @FXML
    private ScrollPane cardlayout;
    private Set<String> categories;
    @FXML
    private ScrollPane menuPane;

    private List<VideoObject> favList = new ArrayList<>();

    public showsController() {
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {

        searchField.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff; -fx-padding: 0.5em;");
        cardlayout.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        menuPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);


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


            stage =  (Stage) ((Node) event.getSource()).getScene().getWindow();
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
        List<VideoObject> filteredShows = main.shows().videoSearch(lowerCaseQuery);

        // clear the shows from the scene
        VideoLayout.getChildren().clear();

        // update the scene with the filtered list of movies and TV shows
        updateSceneWithShows(main.setFav(filteredShows, favList));
    }

    public void updateSceneWithShows(List<VideoObject> filteredShows) throws IOException {
        int row = 0;
        int column = 1;

        for (VideoObject filteredShow : filteredShows) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/show.fxml"));
            VBox carBox = fxmlLoader.load();
            showCardController cardController = fxmlLoader.getController();
            cardController.setData(filteredShow);

            if (column == 5) {
                column = 0;
                row++;
            }

            VideoLayout.add(carBox, column++, row);

        }
    }

    public Set<String> categories() {
        return  main.shows().getAllCategories();
    }

    public void filterByCategory(String category) throws IOException {
        // filter the list of movies and TV shows based on the search query
        List<VideoObject> filteredShows = main.shows().getVideoByCategory(category);
        // clear the shows and movies
        VideoLayout.getChildren().clear();
        // update the scene with the filtered list of movies and TV shows
        updateSceneWithShows(filteredShows);
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
    public void fav(showCardController card){

            if (!card.video.getIsFavorite()) {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/heart.png")));
                main.shows().getAll();
                main.addToList(card.video, "show");
                card.heart.setImage(image);
                card.video.setIsFavorite(true);
            } else {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/heartEmpty.png")));
                main.shows().getAll();
                main.removeToList(card.video, "show");
                card.heart.setImage(image);
                card.video.setIsFavorite(false);
            }

    }
    public List<VideoObject> getList(){
        return favList;
    }

}
