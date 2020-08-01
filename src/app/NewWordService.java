package app;

import app.html.HtmlManager;
import app.model.PhraseDescription;
import app.view.NewWordView;
import app.view.View;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;

public class NewWordService {
    private final Clipboard clipboard;
    private View windowHandler;
    private HtmlManager htmlManager;
    private boolean isWindowOpen;
    private PhraseDescription currentPhrase;


    public NewWordService(Scene scene, PhraseDescription currentPhrase){
        this.windowHandler = new NewWordView(scene);
        this.currentPhrase = currentPhrase;
        clipboard = Clipboard.getSystemClipboard();
        isWindowOpen = false;
        htmlManager = new HtmlManager();
    }

    public void closeWindow(){
        windowHandler.close();
        isWindowOpen = false;
    }

    private void openWindow(){
        windowHandler.open();
        isWindowOpen = true;
    }

    private void reopenWindow(){
        windowHandler.reopen();
        isWindowOpen = true;
    }

    private String getTextFromClipboard(){
        if(clipboard.hasString())
            return clipboard.getString();
        return null;
    }

    public void translatePhraseFromClipboard(){
        htmlManager.setPhrase(getTextFromClipboard());
        if (htmlManager.isValidPhrase()){
            if (currentPhrase.getOriginalPhrase().isEmpty().getValue()
                    || !currentPhrase.getOriginalPhrase().getValue().equals(htmlManager.getOriginalPhrase())) {
                // if phrase changed
                htmlManager.updatePhrase(currentPhrase);
                reopenWindow();
            }
            else if (isWindowOpen) {
                closeWindow();
            } else {
                openWindow();
            }
        } else if (isWindowOpen) {
            closeWindow();
        }
        // TODO toast - incorrect phrase in clipboard
    }
}
