package app.controllers;

import app.AddingWordService;
import app.Observer;
import app.model.PhraseDescription;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.paint.Color;

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
    @FXML
    public ImageView audioButton;
    @FXML
    public ImageView imageLeftArrow;
    @FXML
    public ImageView imageRightArrow;

    private AddingWordService service;
    private PhraseDescription currentPhrase;
    private DropShadow dropShadow;

    public void initialize(){
        currentPhrase = new PhraseDescription();
        service = new AddingWordService();
        Platform.runLater( () -> service.init(originalPhrase.getScene(), this) );

        originalPhrase.textProperty().bind(currentPhrase.getOriginalPhrase());
        translatedPhrase.textProperty().bind(currentPhrase.getTranslatedPhrase());
        phraseExamples.setItems(currentPhrase.getAllExamples());
        Bindings.bindBidirectional(this.pronunciationImage.imageProperty(), currentPhrase.getPronunciationImage());
        Bindings.bindBidirectional(this.phraseImage.imageProperty(), currentPhrase.getPhraseImage());

        dropShadow = new DropShadow();
        dropShadow.setRadius(5.0);
        dropShadow.setColor(Color.BLUE);
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


    @Override
    public void update() {
        String phrase = service.getTextFromClipboard();
        if(phrase != null && service.isValidPhrase(phrase)){
            service.setPhraseDescription(currentPhrase);
            service.showWindow();
            if(currentPhrase.getAudioUrl().equals("")){
                audioButton.setDisable(true);
            }
            else{
                audioButton.setDisable(false);
            }
        }
    }

    @FXML
    public void playAudio(MouseEvent mouseEvent) {
        Media media = new Media(currentPhrase.getAudioUrl());
        MediaPlayer mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setOnEndOfMedia(() -> {
            audioButton.setDisable(false);
            audioButton.setEffect(null);
            audioButton.setCursor(Cursor.HAND);
        });
        audioButton.setDisable(true);
        audioButton.setEffect(dropShadow);
        audioButton.setCursor(Cursor.DEFAULT);
        mediaPlayer.play();
    }

    @FXML
    public void prevImage(MouseEvent mouseEvent) {

    }

    @FXML
    public void nextImage(MouseEvent mouseEvent) {

    }
}
