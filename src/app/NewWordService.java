package app;

import app.html.DikiHtmlParser;
import app.html.HtmlParser;
import app.keyListener.KeyShortcutFinder;
import app.model.PhraseDescription;
import app.util.Observer;
import app.view.NewWordView;
import app.view.View;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;

import java.io.*;
import java.net.URL;

public class NewWordService {
    private KeyShortcutFinder keyShortcutFinder;
    private final Clipboard clipboard;
    private View windowHandler;
    private HtmlParser parser;
    private boolean isOpen;

    public NewWordService(){
        clipboard = Clipboard.getSystemClipboard();
        isOpen = false;
    }
    public void init(Scene scene, Observer observer){
        keyShortcutFinder = new KeyShortcutFinder(observer);
        this.windowHandler = new NewWordView(scene);
    }

    public boolean isOpenWindow(){
        return isOpen;
    }

    public void closeWindow(){
        windowHandler.hide();
        isOpen = false;
    }

    public void showWindow(){
        windowHandler.show();
        isOpen = true;
    }

    public String getTextFromClipboard(){
        if(clipboard.hasString())
            return clipboard.getString();
        return null;
    }

    public void setPhraseDescription(PhraseDescription phrase){
        if(parser.isValidHtml()){
            phrase.setOriginalPhrase(parser.getOriginalPhrase());
            phrase.setAllTranslatedPhrases(parser.getTranslatedPhrases());
            System.out.println(phrase.getOriginalPhrase().toString());
            System.out.println(phrase.getAllTranslatedPhrases().toString());
            phrase.setPronunciationImage(parser.getPronunciationImage());
            phrase.setAllPhraseImages(parser.getAllPhraseImages());
            phrase.setAllExamples(parser.getExamples());
            phrase.setAudioUrl(parser.getAudioUrl());
            phrase.setDefaults();
        }
    }

    public boolean isValidPhrase(String phrase){
        parser = new DikiHtmlParser(phrase);
        try {
            URL url = new URL("https://www.diki.pl/"+phrase);
            InputStream is = url.openStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            parser.setPageContent(br);

            is.close();
            br.close();
        }
        catch (IOException ie) {
            return false;
        }
        return parser.isValidHtml();
    }
}
