package app.controllers;

import app.AddingWordService;
import app.Observer;
import app.model.PhraseDescription;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class AddingWordController implements Observer {

    @FXML
    private Label originalPhrase;
    @FXML
    private Label translatedPhrase;
    @FXML
    private ListView phraseExamples;
    @FXML
    public ImageView pronunciationImage;
    @FXML
    public ImageView phraseImage;

    private AddingWordService service;
    private PhraseDescription currentPhrase;

    public void initialize(){
        currentPhrase = new PhraseDescription();
        service = new AddingWordService();
        Platform.runLater( () -> service.init(originalPhrase.getScene(), this) );

        originalPhrase.textProperty().bind(currentPhrase.getOriginalPhrase());
        translatedPhrase.textProperty().bind(currentPhrase.getTranslatedPhrase());
        phraseExamples.itemsProperty().bind(currentPhrase.getExamples());
        Bindings.bindBidirectional(this.pronunciationImage.imageProperty(), currentPhrase.getPronunciationImage());
        Bindings.bindBidirectional(this.phraseImage.imageProperty(), currentPhrase.getPhraseImage());
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
        currentPhrase.setOriginalPhrase(service.getTextFromClipboard());
        if(service.getPhraseDescription(currentPhrase) == -1)
            return;
        service.showWindow();
    }
}
