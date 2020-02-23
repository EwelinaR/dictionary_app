package app.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;

public class PhraseDescription {
    private StringProperty originalPhrase = new SimpleStringProperty();
    private StringProperty translatedPhrase = new SimpleStringProperty();
    private SimpleListProperty<String> examples = new SimpleListProperty<>(FXCollections.observableArrayList());
    private ObjectProperty<Image> pronunciationImage = new SimpleObjectProperty<>();
    private ObjectProperty<Image> phraseImage = new SimpleObjectProperty<>();

    private List<String> allTranslatedPhrases = new ArrayList<>();
    private List<Image> allPhraseImages = new ArrayList<>();

    public StringProperty getOriginalPhrase() {
        return originalPhrase;
    }
    public void setOriginalPhrase(String originalPhrase) {
        this.originalPhrase.set(originalPhrase);
    }

    public StringProperty getTranslatedPhrase() {
        return translatedPhrase;
    }
    public void setTranslatedPhrase(String translatedPhrase) {
        this.translatedPhrase.set(translatedPhrase);
    }

    public SimpleListProperty<String> getExamples() {
        return examples;
    }
    public void addExample(String example) {
        this.examples.add(example);
    }
    public void removeExamples(){
        this.examples.clear();
    }

    public ObjectProperty<Image> getPronunciationImage(){
        return pronunciationImage;
    }
    public void setPronunciationImage(Image image){
        pronunciationImage.set(image);
    }

    public ObjectProperty<Image> getPhraseImage() {
        return phraseImage;
    }
    public void setPhraseImage(Image phraseImage) {
        this.phraseImage.set(phraseImage);
    }

    public List<String> getAllTranslatedPhrases(){
        return allTranslatedPhrases;
    }
    public void setAllTranslatedPhrases(List<String> translatedPhrases){
        this.allTranslatedPhrases = translatedPhrases;
    }
    public void removeAllTranslatedPhrases(){
        this.allTranslatedPhrases = new ArrayList<>();
    }

    public List<Image> getAllPhraseImages(){
        return allPhraseImages;
    }
    public void setAllPhraseImages(List<Image> images){
        this.allPhraseImages = images;
    }

    public void setDefaults() {
        if(allTranslatedPhrases != null && allTranslatedPhrases.size() > 0)
            setTranslatedPhrase(allTranslatedPhrases.get(0));
        else
            setTranslatedPhrase(null);
        if(allPhraseImages != null && allPhraseImages.size() > 0)
            setPhraseImage(allPhraseImages.get(0));
        else
            setPhraseImage(null);
    }
}
