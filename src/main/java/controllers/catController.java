package controllers;

import Model.DataAccess;
import Model.Display;
import Model.VideoData;
import Model.VideoObject;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;


public class catController {

    @FXML
    private Button categorieButton;
    private mainController main;
    private showsController show;

    private MovieController movie;

    String name;
    public void getController(mainController main) {
        this.main = main;
         name = "main";
    }
    public void getController(showsController main) {
        this.show = main;
        name = "show";
    }
    public void getController(MovieController main) {
        this.movie = main;
        name = "movie";
    }



    public void setText(String category){
        categorieButton.setText(category);
    }

    public void filterByCategories(ActionEvent event) throws IOException {
        if(name.equals("main")){
            main.filterByCategory(categorieButton.getText());
        } else if (name.equals("show")) {
            show.filterByCategory(categorieButton.getText());
        } else if (name.equals("movie")){
            movie.filterByCategory(categorieButton.getText());
        }
    }




}
