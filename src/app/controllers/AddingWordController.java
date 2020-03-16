package app.controllers;

import app.AddingWordService;
import app.Observer;
import app.model.PhraseDescription;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

public class AddingWordController implements Observer {

    @FXML
    private Label originalPhrase;
    @FXML
    private Label translatedPhrase;
    @FXML
    private ListView<String> phraseExamples;
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
        phraseExamples.setItems(currentPhrase.getAllExamples());
        Bindings.bindBidirectional(this.pronunciationImage.imageProperty(), currentPhrase.getPronunciationImage());
        Bindings.bindBidirectional(this.phraseImage.imageProperty(), currentPhrase.getPhraseImage());
    }

    @FXML
    public void showTranslatedExamplePopup(MouseEvent mouseEvent){
        Node node = mouseEvent.getPickResult().getIntersectedNode();
        if (node instanceof Cell) {
            Cell cell = (Cell) node;
            cell.setTooltip(new Tooltip(currentPhrase.getTranslationOfExample(cell.getText())));
        }
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
        String phrase = service.getTextFromClipboard();
        if(phrase != null && service.isValidPhrase(phrase)){
            service.setPhraseDescription(currentPhrase);
            service.showWindow();
        }
    }
}
