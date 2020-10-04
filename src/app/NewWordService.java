package app;

import app.exceptions.InvalidPhraseException;
import app.exceptions.NullClipboardException;
import app.html.HtmlManager;
import app.model.PhraseDescription;
import app.view.NewWordView;
import app.view.View;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;

import java.io.IOException;

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

    private String getTextFromClipboard() throws NullClipboardException {
        if(clipboard.hasString()) {
            String text = clipboard.getString();
            text = text.trim();
            if(text.isEmpty())
                return null;
            return text.replace("\n", " ");
        }
        return null;
    }

    public void updateWindowBasingOnClipboard(){
        if (isWindowOpen) {
            closeWindow();
            return;
        }
        String copiedString = "";
        try {
            copiedString = getTextFromClipboard();
            htmlManager.loadPage(copiedString);
            if (!htmlManager.getOriginalPhrase().equals(currentPhrase.getOriginalPhrase().getValue())) {
                // if phrase changed
                htmlManager.updatePhrase(currentPhrase);
            }
            openWindow();
        } catch (NullClipboardException e){
            windowHandler.showToast("The phrase from the clipboard is null.");
        } catch (InvalidPhraseException e) {
            windowHandler.showToast("The phrase from the clipboard is invalid:\n   " + copiedString);
        } catch (IOException e) {
            windowHandler.showToast("No Internet connection!");
        }
    }
}
