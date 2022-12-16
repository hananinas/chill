package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;

/**
 * The `catController` class is a controller for the category button in the main, movies, and shows views.
 * It allows the user to filter the movies and shows displayed by their categories.
 */
public class catController {

    @FXML
    private Button categorieButton;
    private mainController main;
    private showsController show;

    private movieController movie;

    private String name;

    /**
     * Sets the main controller for this category button.
     * @param main the main controller
     */
    public void getController(mainController main) {
        this.main = main;
         name = "main";
    }

    /**
     * Sets the shows controller for this category button.
     * @param main the shows controller
     */
    public void getController(showsController main) {
        this.show = main;
        name = "show";
    }

    /**
     * Sets the movie controller for this category button.
     * @param main the movie controller
     */
    public void getController(movieController main) {
        this.movie = main;
        name = "movie";
    }

    /**
     * Sets the text of the categorieButton to the given category name.
     * @param category the category name to display on the button
     */
    public void setText(String category){
        categorieButton.setText(category);
    }

    /**
     * Filters the movies and shows displayed by the category of the categorieButton.
     * @throws IOException if there is an error loading the new view
     */
    public void filterByCategories() throws IOException {
        switch (name) {
            case "main" -> main.filterByCategory(categorieButton.getText());
            case "show" -> show.filterByCategory(categorieButton.getText());
            case "movie" -> movie.filterByCategory(categorieButton.getText());
        }
    }




}
