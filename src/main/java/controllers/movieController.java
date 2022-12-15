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
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.Node;

public class movieController implements Initializable {

    private Stage stage;
    private Parent root;

   protected mainController main = new mainController();


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

    @FXML
    private Label notFound;
    private Set<String> categories;

    protected HashSet<VideoObject> favList = new HashSet<>();

    /**
     * This function is called when the controller is initialized
     * @param arg0
     * @param arg1
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {




        /**
         * Try-Catch block to catch any errors during start
         */
        try {


            /**
             * set styles for UI elements
             */
            notFound.setVisible(false);

            searchField.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff; -fx-padding: 0.5em;");
            cardlayout.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            menuPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

            /**
             * perform initial search with no query
             */
            String start = "";
            search(start);

            // set up event listener for search button
            searchButton.setOnAction(event -> {
                // get the search query from the text field
                String query = searchField.getText();

                // search for movies and TV shows matching the query
                try {
                    search(query);
                } catch (IOException | SearchIsEmptyException e) {
                    notFound.setVisible(true);
                    notFound.setText(e.getMessage());
                }
            });

            setCategories(menuItem);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     *  SetData is a method called when the controller is loaded by the
     * @param favList
     * @throws IOException
     */
    public void setData(HashSet<VideoObject> favList) throws IOException, SearchIsEmptyException {
        this.favList = favList;
        search("");
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

    public void search(String query) throws IOException, SearchIsEmptyException {
        // convert the search query to lower case
    
        String lowerCaseQuery = query.toLowerCase();

        // filter the list of movies and TV shows based on the search query
        List<VideoObject> filteredMovies =  main.movies().videoSearch(lowerCaseQuery);

        if(filteredMovies.size() == 0){
            VideoLayout.getChildren().clear();
            VideoLayout.getChildren().add(notFound);
            throw new SearchIsEmptyException();
        }

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

    /**
     * Method to getAllCategories
     * @return categories
     */
    public Set<String> categories() {
        /** uses the method movies to get all movies and the method getAllCategories from display to get the unique categories in movies. **/
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

    /**
     * This method is a controller for the fav list that uses the methods addToList and removeToList to remove and change the state of the heart image on the heartButton in Card
     * @param card
     */
    public void fav(movieCardController card){

        /**
         * If the video in movieCardController is not fav then change the image to heart.png
         */
             if (!card.video.getIsFavorite()) {
                Image image = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/img/heart.png")));
                addToList(card.video);
                main.movies().getAll();
                card.heart.setImage(image);
                card.video.setIsFavorite(true);
            }

    }

    public void addToList(VideoObject video){
            main.movies().getAll();
            favList = main.movies().addFavList(video.getTitle(), favList);
    }

    public void removeToList(VideoObject video){
            main.movies().getAll();
            favList = main.movies().removeFavList(video.getTitle(), favList);
    }
    public HashSet<VideoObject> getList(){
        return favList;
    }
}
