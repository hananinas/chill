package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;


public class catController {

    @FXML
    private Button categorieButton;
    private mainController main;
    private showsController show;

    private movieController movie;

    private String name;

    public void getController(mainController main) {
        this.main = main;
         name = "main";
    }
    public void getController(showsController main) {
        this.show = main;
        name = "show";
    }
    public void getController(movieController main) {
        this.movie = main;
        name = "movie";
    }


    public void setText(String category){
        categorieButton.setText(category);
    }

    public void filterByCategories() throws IOException {
        switch (name) {
            case "main" -> main.filterByCategory(categorieButton.getText());
            case "show" -> show.filterByCategory(categorieButton.getText());
            case "movie" -> movie.filterByCategory(categorieButton.getText());
        }
    }




}
