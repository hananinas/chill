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
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
 /**
 *The showsController class is responsible for displaying a list of TV shows, as well as allowing the user to search for TV shows and filter the list by categories.
 **/
public class showsController implements Initializable {

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
    private VBox menuItem;
    @FXML
    private ScrollPane cardlayout;
    private Set<String> categories;
    @FXML
    private ScrollPane menuPane;
    @FXML
    private Label notFound;
    
    private HashSet<VideoObject> favList = new HashSet<>();

    public showsController() {
    }

    /**
     * Initializes the main controller and sets up the search and menu functionality.
     *
     * @param arg0 the URL of the FXML file
     * @param arg1 the resource bundle
     */
    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {



        try {
            /**
             * Set the style for the search field and scroll panes
             */
            searchField.setStyle("-fx-font-size: 14px; -fx-text-fill: #fff; -fx-padding: 0.5em;");
            cardlayout.setStyle("-fx-background: transparent; -fx-background-color: transparent;");
            menuPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            notFound.setVisible(false);

            /**
             * Perform a search with an empty query to display all movies and TV shows
             */
            String start = "";
            search(start);

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
                    notFound.setMinWidth(400);
                    notFound.setVisible(true);
                    notFound.setText(e.getMessage());
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

     Switches to the home screen.

     @param event the event triggered by the action
     */
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

    /**

     Switches to the movies screen.

     @param event the event triggered by the action
     */
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

    /**

     Switches to the TV shows screen.

     @param event the event triggered by the action
     */
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

    /**

     Switches to the favorite list screen.

     @param event the event triggered by the action
     */
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

    /**
     *
     *Searches for TV shows matching the given query.
     *@param query the search query
     * @throws IOException if an I/O error occurs
     * @throws SearchIsEmptyException if no TV shows match the given query
     */
    public void search(String query) throws IOException, SearchIsEmptyException {
        // convert the search query to lower case
        String lowerCaseQuery = query.toLowerCase();


            // filter the list of movies and TV shows based on the search query
        List<VideoObject> filteredShows = main.shows().videoSearch(lowerCaseQuery);

        if(filteredShows.size() == 0){
            VideoLayout.getChildren().clear();
            VideoLayout.getChildren().add(notFound);
            throw new SearchIsEmptyException();
        }

        // clear the shows from the scene
        VideoLayout.getChildren().clear();

            // update the scene with the filtered list of movies and TV shows
        updateSceneWithShows(main.setFav(filteredShows, favList));

    }

    /**

     Updates the scene with the provided list of TV shows.

     @param filteredShows the list of TV shows to display

     @throws IOException if the FXML file for the TV show card cannot be loaded
     */
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

    /**
     *Returns a set of unique categories for TV shows.
     *@return a set of categories for TV shows
     */
    public Set<String> categories() {
        return  main.shows().getAllCategories();
    }


    /**
     *Filters the list of TV shows based on the given category and updates the scene with the filtered list.
     *@param category the category to filter the list of TV shows by
     *@throws IOException if an input or output exception occurred
     */
    public void filterByCategory(String category) throws IOException {
        // filter the list of movies and TV shows based on the search query
        List<VideoObject> filteredShows = main.shows().getVideoByCategory(category);
        // clear the shows and movies
        VideoLayout.getChildren().clear();
        // update the scene with the filtered list of movies and TV shows
        updateSceneWithShows(filteredShows);
    }

    /**

     *Sets the categories for the menu item.
     *@param menuItem the menu item where the categories will be displayed
     *@throws IOException if there is an error loading the category button FXML file
     */
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
     * (Experimantal not working) adds or removes a show from the favorite list.
     *
     * @param card the card controller of the show
     */
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

    /**
     * Returns the favorite list.
     *
     * @return the favorite list
     */
    public HashSet<VideoObject> getList(){
        return favList;
    }

}
