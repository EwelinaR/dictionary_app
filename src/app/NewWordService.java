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
        if(clipboard.hasString()) {
            String text = clipboard.getString();
            text = text.trim();
            if(text.isEmpty())
                return null;
            return text.replace("\n", " ");
        }
        return null;
    }

    public void translatePhraseFromClipboard(){
        String copiedString = getTextFromClipboard();
        htmlManager.setPhrase(copiedString);
        // TODO change this statements to try and add my own exception
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
            // if phrase is invalid and window is open then close window
            closeWindow();
        } else {
            windowHandler.showToast("Phrase from clipboard is invalid:\n" + copiedString);
        }
    }
}
