package app.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.image.Image;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PhraseDescription {
    private StringProperty originalPhrase = new SimpleStringProperty();
    private StringProperty translatedPhrase = new SimpleStringProperty();
    private ObservableList<String> examples = FXCollections.observableArrayList();
    private ObservableList<String> translatedExamples = FXCollections.observableArrayList();
    private ObjectProperty<Image> pronunciationImage = new SimpleObjectProperty<>();
    private ObjectProperty<Image> phraseImage = new SimpleObjectProperty<>();

    private List<String> allTranslatedPhrases = new ArrayList<>();
    private List<Image> allPhraseImages = new ArrayList<>();
    private String audioUrl = "";

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

    public ObservableList<String> getAllExamples() {
        return examples;
    }

    public void setAllExamples(Map<String, String> examples) {
        this.examples.remove(0, this.examples.size());
        this.translatedExamples.remove(0, this.translatedExamples.size());

        for (Map.Entry<String, String> entry : examples.entrySet()) {
            this.examples.add(entry.getKey());
            this.translatedExamples.add(entry.getValue());
        }
    }

    public String getTranslationOfExample(String example){
        for(int i = 0; i < examples.size(); i++){
            if(examples.get(i).equals(example)){
                return translatedExamples.get(i);
            }
        }
        return "";
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

    public List<Image> getAllPhraseImages(){
        return allPhraseImages;
    }
    public void setAllPhraseImages(List<Image> images){
        this.allPhraseImages = images;
    }

    public void updateDefaultValues() {
        if(allTranslatedPhrases != null && allTranslatedPhrases.size() > 0)
            setTranslatedPhrase(allTranslatedPhrases.get(0));
        else
            setTranslatedPhrase(null);
        if(allPhraseImages != null && allPhraseImages.size() > 0)
            setPhraseImage(allPhraseImages.get(0));
        else
            setPhraseImage(null);
    }

    public String getAudioUrl(){
        return audioUrl;
    }

    public void setAudioUrl(String audioUrl){
        this.audioUrl = audioUrl;
    }
}
