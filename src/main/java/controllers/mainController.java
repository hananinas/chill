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


    /**
     * Initializes the main controller and sets up the search and menu functionality.
     *
     * @param arg0 the URL of the FXML file
     * @param arg1 the resource bundle
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            notFound1.setVisible(false);
            notFound.setVisible(false);

            main = this;


            /**
             * Set the style for the search field and scroll panes
             */
            searchField.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff; -fx-padding: 0.5em;");
            moviePane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            moviePane.setVbarPolicy(ScrollBarPolicy.NEVER);
            moviePane.setHbarPolicy(ScrollBarPolicy.NEVER);

            showPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            menuPane.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            menuPane.setVbarPolicy(ScrollBarPolicy.NEVER);

            /**
             * Perform a search with an empty query to display all movies and TV shows
             */
            String start = "";
            search(start);

            /**
             * Set up the search button to perform a search when clicked
             */
            searchButton.setOnAction(event -> {

                /**
                 *  get the search query from the text field
                  */
                String query = searchField.getText();

                /**
                 * search for movies and TV shows matching the query
                 */
                try {
                    search(query);
                } catch (IOException | SearchIsEmptyException e) {
                    notFound.setVisible(true);
                    notFound.setText(e.getMessage());
                    notFound1.setVisible(true);
                    notFound1.setText(e.getMessage());
                }
            });

            /**
             * set up the menu with the categories of movies and TV shows
             */
            setCategories(menuItem);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    /**
     * Sets the data for the favorite list and searches for movies and TV shows with an empty query.
     *
     * @param favlist the favorite list
     * @throws IOException if there is an error reading from the data files
     * @throws SearchIsEmptyException if the search query is empty
     */
    public void setData(HashSet<VideoObject> favlist) throws IOException, SearchIsEmptyException {
        this.favList = favlist;
        search("");
    }



    /**
     * Returns a {@code Display} object for movies.
     *
     * @return a {@code Display} object for movies
     */
    public Display movies(){
        DataAccess filmdata = new VideoData();
        Display film = new Display(filmdata, "src/main/resources/data/film.txt", "src/main/resources/images/filmplakater");
        film.videoData();
        return film;
    }

    /**
     * Returns a {@code Display} object for TV shows.
     *
     * @return a {@code Display} object for TV shows
     */
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

            /** set the stage with the shows and movies - home screen **/
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

    /**
     * Searches for movies and TV shows matching the given query.
     *
     * @param query the search query
     * @throws IOException if there is an error reading from the data files
     * @throws SearchIsEmptyException if the search query is empty
     */
    public void search(String query) throws SearchIsEmptyException, IOException {

        /** convert the search query to lower case **/
        String lowerCaseQuery = query.toLowerCase();

        /** Create Display objects for movies and TV shows **/
        Display film = movies();
        Display show = shows();

        /** // filter the list of movies and TV shows based on the search query **/
        List<VideoObject> filteredShows = show.videoSearch(lowerCaseQuery);

        /** filter the list of movies and TV shows based on the search query **/
        List<VideoObject> filteredMovies = film.videoSearch(lowerCaseQuery);


        /**
         *
         * If no matches are found, set the visibility of the notFound labels to true and set their text to an error message
         */
        if(filteredMovies.size() == 0 && filteredShows.size() == 0){
            cardLayoutFilm.getChildren().clear();
            cardLayoutShow.getChildren().clear();
            cardLayoutFilm.getChildren().add(notFound);
            cardLayoutShow.getChildren().add(notFound1);
            throw new SearchIsEmptyException();
        }

        /**
         * clear the shows and movies
         */
        cardLayoutFilm.getChildren().clear();
        cardLayoutShow.getChildren().clear();


        /**
         * update the scene with the filtered list of movies and TV shows
         */
        updateSceneWithMovies(setFav(filteredMovies,favList));
        updateSceneWithShows(setFav(filteredShows,favList));
    }

    /**
     * Filters a list of video objects based on whether they are in the favorite list.
     *
     * @param videoType the list of video objects to filter
     * @param favList the set of favorite video objects
     * @return a list of the video objects from `videoType` that are also in `favList`, with their `isFavorite` field set to `true`
     */
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

    /**
     * Updates the user interface with a list of movies.
     *
     * @param filteredMovies the list of movies to display
     * @throws IOException if there is an error loading the `card.fxml` file
     */
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

    /**
     * Updates the user interface with a list of TV shows.
     *
     * @param filteredShows the list of TV shows to display
     * @throws IOException if there is an error loading the `card.fxml` file
     */
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

    /**
     * Filters a list of movies and TV shows by category and updates the user interface with the results.
     *
     * @param category the category to filter by
     * @throws IOException if there is an error loading the `card.fxml` file
     */
    public void filterByCategory(String category) throws IOException {
        cardLayoutFilm.getChildren().clear();
        cardLayoutShow.getChildren().clear();

        // filter the list of TV shows based on the category selected.
        List<VideoObject> filteredShows = shows().getVideoByCategory(category);


        // filter the list of movies based on the category selected.
        List<VideoObject> filteredMovies = movies().getVideoByCategory(category);

        // clear the shows and movies
        cardLayoutFilm.getChildren().clear();
        cardLayoutShow.getChildren().clear();
        // update the scene with the filtered list of movies and TV shows
        updateSceneWithShows(filteredShows);
        updateSceneWithMovies(filteredMovies);

    }

    /**
     * Adds buttons for each category to a VBox.
     *
     * @param menuItem the VBox to add the buttons to
     * @throws IOException if there is an error loading the `categoryButton.fxml` file
     */
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

    /**
     * Adds or removes a movie or TV show from the favorites list and updates the "favorite" button on the user interface.
     *
     * @param season the season of the TV show (if applicable)
     * @param card the cardController associated with the card
     */
    public void fav(String season, cardController card ){
        if (season.equals("")) {
            // movie
            if (!card.video.getIsFavorite()) {
                // add movie to favorites
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/heart.png")));
                main.addToList(card.video, "movie");
                main.movies().getAll();
                card.heart.setImage(image);
                card.video.setIsFavorite(true);
            } else {
                // remove movie from favorites
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/heartEmpty.png")));
                main.movies().getAll();
                main.removeToList(card.video, "movie");
                card.heart.setImage(image);
                card.video.setIsFavorite(false);
            }

        } else {
            // TV show
            if (!card.video.getIsFavorite()) {
                // add TV show to favorites
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

    /**
     * Adds a movie or TV show to the favorites list.
     *
     * @param video the movie or TV show to add to the favorites list
     * @param type the type of video, either "movie" or "show"
     */
    public void addToList(VideoObject video,String type){
        if(type.equals("show")){
            shows().getAll();
            main.favList = shows().addFavList(video.getTitle(), favList);
        } else if(type.equals("movie")){
            movies().getAll();
            main.favList = movies().addFavList(video.getTitle() , favList);
        }

    }

    /**
     * Removes a movie or TV show from the favorites list.
     *
     * @param video the movie or TV show to remove from the favorites list
     * @param type the type of video, either "movie" or "show"
     */
    public void removeToList(VideoObject video,String type){
        if(type.equals("show")){
            shows().getAll();
           favList = shows().removeFavList(video.getTitle(), favList);

        } else if(type.equals("movie")){
            movies().getAll();
            favList = movies().removeFavList(video.getTitle(), favList);
        }

    }

    /**
     * Returns the favorite list.
     *
     * @return the favorite list
     */
    public HashSet<VideoObject> getList(){
        return favList;
    }

}