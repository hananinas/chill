package controllers;

import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

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
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class showsController implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    private List<VideoObject> movies;

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

    public void switchToHome(ActionEvent event) throws IOException {
        main.switchToHome(event);
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


        // filter the list of movies and TV shows based on the search query
        List<VideoObject> filteredShows = main.shows().videoSearch(lowerCaseQuery);

        // clear the shows from the scene
        VideoLayout.getChildren().clear();

        // update the scene with the filtered list of movies and TV shows
        updateSceneWithShows(filteredShows);
    }

    public void updateSceneWithShows(List<VideoObject> filteredShows) throws IOException {
        int row = 0;
        int column = 1;

        for (int i = 0; i < filteredShows.size(); i++) {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/view/show.fxml"));
            VBox carBox = fxmlLoader.load();
            ShowCardController cardController = fxmlLoader.getController();
            cardController.setData(filteredShows.get(i));

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

}
