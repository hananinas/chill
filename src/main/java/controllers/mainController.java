package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import Model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.ScrollPane.ScrollBarPolicy;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

public class mainController implements Initializable {

    @FXML
    HBox cardLayoutFilm;
    @FXML
    HBox cardLayoutShow;
    @FXML
    VBox menuItem;
    @FXML
    private TextField searchField;
    @FXML
    private Button searchButton;
    @FXML
    private ScrollPane moviePane;
    @FXML
    private ScrollPane showPane;
    @FXML
    private ScrollPane menuPane;


    private Set<String> categories;

    private Stage stage;
    private Scene scene;
    private Parent root;
    private String query;
    private int counter = 0;


    public ObservableList<VideoObject> favList = FXCollections.observableArrayList();

    public void setData(ObservableList<VideoObject> favlist) {
        main.favList = favlist;
    }
    private static mainController main;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        main = this;



        searchField.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff; -fx-padding: 0.5em;");
        moviePane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        moviePane.setVbarPolicy(ScrollBarPolicy.NEVER);
        moviePane.setHbarPolicy(ScrollBarPolicy.NEVER);

        showPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        menuPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
        menuPane.setVbarPolicy(ScrollBarPolicy.NEVER);


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

    public Display movies(){
        DataAccess filmdata = new VideoData();
        Display film = new Display(filmdata, "src/main/resources/data/film.txt", "src/main/resources/images/filmplakater");
        film.VideoData();
        return film;
    }

    public Display shows(){
        DataAccess showdata = new VideoData();
        Display show = new Display(showdata, "src/main/resources/data/serier.txt", "src/main/resources/images/serieforsider");
        show.VideoData();
        return show;
    }

    public Set<String> categories() {
        Set <String> allcategories = shows().getAllCategories();
        for (String categories : movies().getAllCategories()){
            allcategories.add(categories);
        }
        return  allcategories;
    }

    public void switchToHome(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            root = fxmlLoader.load();
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
            mainController main = fxmlLoader.getController();
            main.setData(favList);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void switchToMovies(ActionEvent event) throws IOException {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/movies.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void switchToShows(ActionEvent event) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/view/shows.fxml"));
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void switchToFav(ActionEvent event) throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/favlist.fxml"));
            root = fxmlLoader.load();
            favController fav = fxmlLoader.getController();
            fav.setData(favList);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);

            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public ObservableList<VideoObject>  returnFav() {
        return favList;
    }

    public void search(String query) throws IOException {
        // convert the search query to lower case
        String lowerCaseQuery = query.toLowerCase();



        // filter the list of movies and TV shows based on the search query
        List<VideoObject> filteredShows = shows().videoSearch(lowerCaseQuery);
        //add heart for fav if it is in the fav list

        // filter the list of movies and TV shows based on the search query
        List<VideoObject> filteredMovies = movies().videoSearch(lowerCaseQuery);



        // clear the shows and movies
        cardLayoutFilm.getChildren().clear();
        cardLayoutShow.getChildren().clear();
        // update the scene with the filtered list of movies and TV shows
        updateSceneWithMovies(filteredMovies);
        updateSceneWithShows(filteredShows);
    }

    public void updateSceneWithMovies(List<VideoObject> filteredMovies) throws IOException {
        for (int i = 0; i < filteredMovies.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/card.fxml"));
            HBox carBox = fxmlLoader.load();
            CardController cardController = fxmlLoader.getController();
            cardController.setData(filteredMovies.get(i));
            cardLayoutFilm.getChildren().add(carBox);
        }

    }

    private void updateSceneWithShows(List<VideoObject> filteredShows) throws IOException {
        for (int i = 0; i < filteredShows.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/card.fxml"));
            HBox carBox = fxmlLoader.load();
            CardController cardController = fxmlLoader.getController();
            cardController.setDataShow((SerieObject) filteredShows.get(i));
            cardLayoutShow.getChildren().add(carBox);
        }
    }

    public void filterByCategory(String category) throws IOException {
        cardLayoutFilm.getChildren().clear();
        cardLayoutShow.getChildren().clear();

        // filter the list of movies and TV shows based on the search query
        List<VideoObject> filteredShows = shows().getVideoByCategory(category);

        // filter the list of movies and TV shows based on the search query
        List<VideoObject> filteredMovies = movies().getVideoByCategory(category);

        // clear the shows and movies
        cardLayoutFilm.getChildren().clear();
        cardLayoutShow.getChildren().clear();
        // update the scene with the filtered list of movies and TV shows
        updateSceneWithMovies(filteredMovies);
        updateSceneWithShows(filteredShows);
    }

    public  void  setCategories(VBox menuItem) throws IOException{
        categories = new HashSet<String>(categories());


        Iterator value = categories.iterator();

        while (value.hasNext()) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/categoryButton.fxml"));
            Button catbButton = fxmlLoader.load();
            catController catController = fxmlLoader.getController();
            catController.getController(this);
            catController.setText(value.next().toString());
            menuItem.getChildren().add(catbButton);
        }
    }

    public void fav(String season, CardController card){
        if (season.equals("")) {

            counter++;

            if (counter == 1) {
                Image image = new Image(getClass().getResourceAsStream("/img/heart.png"));
                main.addToList(card.video, "movie");
                main.movies().getAll();
                card.heart.setImage(image);

            } else if (counter == 2) {
                Image image = new Image(getClass().getResourceAsStream("/img/heartEmpty.png"));
                main.movies().getAll();
                main.removeToList(card.video, "movie");
                card.heart.setImage(image);
                counter = 0;
            }

        } else {

            counter++;

            if (counter == 1) {
                Image image = new Image(getClass().getResourceAsStream("/img/heart.png"));
                shows().getAll();
                main.addToList(card.video, "show");
                card.heart.setImage(image);
            } else if (counter == 2) {
                Image image = new Image(getClass().getResourceAsStream("/img/heartEmpty.png"));
                shows().getAll();
                main.removeToList(card.video, "show");
                card.heart.setImage(image);
                counter = 0;
            }
        }

    }


    public void addToList(VideoObject video,String type){
        if(type.equals("show")){
            shows().getAll();
            main.favList.add(shows().addFavList(video.getTitle(), main.getList()));
            movies().getMyFavList(getList());
        } else if(type.equals("movie")){
            movies().getAll();
            main.favList.add(movies().addFavList(video.getTitle(), main.getList()));
            movies().getMyFavList(getList());

        }

    }

    public void removeToList(VideoObject video,String type){
        if(type.equals("show")){
            shows().getAll();
           favList = (ObservableList<VideoObject>) shows().removeFavList(video.getTitle(), main.getList());
            movies().getMyFavList(getList());
        } else if(type.equals("movie")){
            movies().getAll();
            favList = (ObservableList<VideoObject>) movies().removeFavList(video.getTitle(), main.getList());
            movies().getMyFavList(getList());
        }

    }

    public List<VideoObject> getList(){
        return main.favList;
    }

    // method to get the list from fav
    public void getList(favController fav, List<VideoObject> favList){
        Platform.runLater(() -> {
            this.favList = (ObservableList<VideoObject>) favList;
        });
    }

    // method to get the list from movies
    public void getList(MovieController fav, List<VideoObject> favList){
        this.favList = (ObservableList<VideoObject>) favList;
    }

    // method to get the list from shows
    public void getList(showsController fav, List<VideoObject> favList){
        this.favList = (ObservableList<VideoObject>) favList;
    }

    // method to get the list from home
    public void getList(mainController fav, List<VideoObject> favList){
        this.favList = (ObservableList<VideoObject>) favList;
    }

}