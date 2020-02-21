package app;

import app.dictionary.DikiHtmlParser;
import app.dictionary.HtmlParser;
import app.keyListener.KeyShortcutFinder;
import app.model.PhraseDescription;
import app.windows.AddingWordWindow;
import app.windows.NewWindowHandler;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;

import java.io.*;
import java.net.URL;

public class AddingWordService{
    private KeyShortcutFinder keyShortcutFinder;
    private final Clipboard clipboard;
    private NewWindowHandler windowHandler;
    private HtmlParser parser;

    public AddingWordService(){
        clipboard = Clipboard.getSystemClipboard();

    }
    public void init(Scene scene, Observer observer){
        keyShortcutFinder = new KeyShortcutFinder(observer);
        this.windowHandler = new AddingWordWindow(scene);
    }

    public void closeWindow(){
        windowHandler.close();
    }

    public void showWindow(){
        windowHandler.show();
    }

    public String getTextFromClipboard(){
        if(clipboard.hasString())
            return clipboard.getString();
        return null;
    }

    private void setPhraseDescription(PhraseDescription phrase, HtmlParser parser){
        phrase.setAllTranslatedPhrases(parser.getTranslatedPhrases());
        phrase.setTranslatedPhrase(phrase.getAllTranslatedPhrases().get(0));
        System.out.println(phrase.getAllTranslatedPhrases().toString());
        System.out.println(phrase.getOriginalPhrase());
        phrase.setPronunciationImage(parser.getPronunciationImage());
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

    public void setPhraseDescription(PhraseDescription phrase){
        setPhraseDescription(phrase, parser);

    }
}
