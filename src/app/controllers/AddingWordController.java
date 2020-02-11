package app.controllers;

import app.AddingWordService;
import app.Observer;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;

public class AddingWordController implements Observer {

    @FXML
    private Label englishWord;
    private AddingWordService service;

    public void initialize(){
        service = new AddingWordService();
        Platform.runLater( () -> service.init(englishWord.getScene(), this) );
    }

    @FXML
    public void closePopUp(MouseEvent mouseEvent) {
        service.closeWindow();
    }

    @FXML
    public void addWord(MouseEvent mouseEvent) {
        service.closeWindow();
    }

    @Override
    public void update() {
        service.showWindow();
        englishWord.setText(service.getTextFromClipboard());
    }
}
