package controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;


public class catController {

    @FXML
    private Button categorieButton;
    private final mainController main;

    public catController(mainController main) {
        this.main = main;
    }

    public void setText(String category){
        categorieButton.setText(category);
    }
}
