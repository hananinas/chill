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
import javafx.scene.control.Label;
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
    @FXML
    private Label notFound;

    @FXML
    private Label notFound1;

    private Stage stage;
    private Parent root;
    protected HashSet<VideoObject> favList = new HashSet<>();

    private static mainController main;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            notFound1.setVisible(false);
            notFound.setVisible(false);

            main = this;



            searchField.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff; -fx-padding: 0.5em;");
            moviePane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            moviePane.setVbarPolicy(ScrollBarPolicy.NEVER);
            moviePane.setHbarPolicy(ScrollBarPolicy.NEVER);

            showPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            menuPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            menuPane.setVbarPolicy(ScrollBarPolicy.NEVER);

            String start = "";
            search(start);

            searchButton.setOnAction(event -> {
                // get the search query from the text field
                String query = searchField.getText();

                // search for movies and TV shows matching the query
                try {
                    search(query);
                } catch (IOException | SearchIsEmptyException e) {
                    notFound.setVisible(true);
                    notFound.setText(e.getMessage());
                    notFound1.setVisible(true);
                    notFound1.setText(e.getMessage());
                }
            });

            setCategories(menuItem);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void setData(HashSet<VideoObject> favlist) throws IOException, SearchIsEmptyException {
        this.favList = favlist;
        search("");
    }



    public Display movies(){
        DataAccess filmdata = new VideoData();
        Display film = new Display(filmdata, "src/main/resources/data/film.txt", "src/main/resources/images/filmplakater");
        film.videoData();
        return film;
    }

    public Display shows(){
        DataAccess showdata = new VideoData();
        Display show = new Display(showdata, "src/main/resources/data/serier.txt", "src/main/resources/images/serieforsider");
        show.videoData();
        return show;
    }

    /**
     * Used to get all the categories for both shows and movies
     * @return allcategories
     */
    public Set<String> categories() {
        /**
         * initialize New Set of allcategories with all the shows.
         */
        Set <String> allcategories = shows().getAllCategories();
        /**
         * Add all the movies to the Set all categories containing all shows.
         */
        allcategories.addAll(movies().getAllCategories());

        return  allcategories;

    }

    /**
     * ActionEvent triggered when home icon is clicked
     * @param event ActionEvent
     */
    public void switchToHome(ActionEvent event) {
        /**
         * try-catch any errors while switching scenes
         */
        try {
            /** Load  the view for home page using the FXMLLOADER **/

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/home.fxml"));
            root = fxmlLoader.load();

            /**
             * Get the mainController
             */
            mainController mainController = fxmlLoader.getController();
            mainController.setData(getList());

            /** set the stage with the shows and movies / home screen **/
            stage =  (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ActionEvent triggered when Movie Button is clicked
     * @param event ActionEvent
     */
    public void switchToMovies(ActionEvent event) {
        /**
         * try-catch any errors while switching scenes
         */
        try {
            /** Load  the view for movies page  using the FXMLLOADER **/

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/movies.fxml"));
            root = fxmlLoader.load();

            /** get the movieConroller for the view **/
            movieController movieController = fxmlLoader.getController();
            movieController.setData(getList());

            /** set the stage with the movies  **/
            stage =  (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ActionEvent triggered when shows Button is clicked
     * @param event ActionEvent
     */
    public void switchToShows(ActionEvent event) {
        /**
         * try-catch any errors while switching scenes
         */
        try {
            /** Load  the view for shows page using the FXMLLOADER **/
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/shows.fxml"));
            root = fxmlLoader.load();

            /** get the showConroller for the view **/
            showsController showsController = fxmlLoader.getController();
            showsController.setData(getList());

            /** set the stage with the shows **/
            stage =  (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * ActionEvent triggered when favList icon is clicked
     * @param event ActionEvent
     */
    public void switchToFav(ActionEvent event) {
        /**
         * try-catch any errors while switching scenes
         */
        try {

            /** Load the the view for favList using the FXMLLOADER **/
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/favlist.fxml"));
            root = fxmlLoader.load();


            /** get the favConroller for the view **/
            favController favController = fxmlLoader.getController();

            /** set data for favList in the Controller **/
            favController.setData(getList());

            /** set the stage with the  **/
            stage =  (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void search(String query) throws SearchIsEmptyException, IOException {
        /** // convert the search query to lower case **/
        String lowerCaseQuery = query.toLowerCase();

        /** // filter the list of movies and TV shows based on the search query **/
        List<VideoObject> filteredShows = shows().videoSearch(lowerCaseQuery);

        /** filter the list of movies and TV shows based on the search query **/
        List<VideoObject> filteredMovies = movies().videoSearch(lowerCaseQuery);

        if(filteredMovies.size() == 0 && filteredShows.size() == 0){
            cardLayoutFilm.getChildren().clear();
            cardLayoutShow.getChildren().clear();
            cardLayoutFilm.getChildren().add(notFound);
            cardLayoutShow.getChildren().add(notFound1);
            throw new SearchIsEmptyException();
        }

        // clear the shows and movies
        cardLayoutFilm.getChildren().clear();
        cardLayoutShow.getChildren().clear();
        // update the scene with the filtered list of movies and TV shows

        updateSceneWithMovies(setFav(filteredMovies,favList));
        updateSceneWithShows(setFav(filteredShows,favList));
    }

    // add heart if movie or show is in fav list
    public List<VideoObject> setFav(List<VideoObject> videoType, Set<VideoObject> favList ){
        List<VideoObject> favFiltered = new ArrayList<>();
        for (VideoObject media: videoType
        ) {
            for (VideoObject favvideo: favList
            ) {
                if(favvideo.getTitle().contains(media.getTitle())){
                    media.setIsFavorite(true);
                }
            }
            favFiltered.add(media);
        }
        return favFiltered;
    }
    public void updateSceneWithMovies(List<VideoObject> filteredMovies) throws IOException {
        for (VideoObject filteredMovie : filteredMovies) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/card.fxml"));
            HBox carBox = fxmlLoader.load();
            cardController cardController = fxmlLoader.getController();
            cardController.setData(filteredMovie);
            cardLayoutFilm.getChildren().add(carBox);
        }

    }

    private void updateSceneWithShows(List<VideoObject> filteredShows) throws IOException {
        for (VideoObject filteredShow : filteredShows) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/card.fxml"));
            HBox carBox = fxmlLoader.load();
            cardController cardController = fxmlLoader.getController();
            cardController.setDataShow((SerieObject) filteredShow);
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
        updateSceneWithShows(filteredShows);
        updateSceneWithMovies(filteredMovies);

    }

    public  void  setCategories(VBox menuItem) throws IOException{
        Set<String> categories = new HashSet<>(categories());

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
    public void fav(String season, cardController card){
        if (season.equals("")) {

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

        } else {

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

    }

    public void addToList(VideoObject video,String type){
        if(type.equals("show")){
            shows().getAll();
            main.favList = shows().addFavList(video.getTitle(), favList);
        } else if(type.equals("movie")){
            movies().getAll();
            main.favList = movies().addFavList(video.getTitle() , favList);
        }

    }

    public void removeToList(VideoObject video,String type){
        if(type.equals("show")){
            shows().getAll();
           favList = shows().removeFavList(video.getTitle(), favList);

        } else if(type.equals("movie")){
            movies().getAll();
            favList = movies().removeFavList(video.getTitle(), favList);
        }

    }

    public HashSet<VideoObject> getList(){
        return favList;
    }

}