package app;

import app.keyListener.KeyShortcutFinder;
import app.windows.AddingWordWindow;
import app.windows.NewWindowHandler;
import javafx.scene.Scene;
import javafx.scene.input.Clipboard;

public class AddingWordService{
    private KeyShortcutFinder keyShortcutFinder;
    private final Clipboard clipboard;
    private NewWindowHandler windowHandler;

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
        return clipboard.getString();
    }
}
