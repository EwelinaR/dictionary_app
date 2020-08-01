package app.keyListener;

import app.util.Observer;
import org.jnativehook.GlobalScreen;
import org.jnativehook.NativeHookException;
import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class KeyShortcutFinder implements NativeKeyListener, Observer {
    private int[] shortcut = new int[]{NativeKeyEvent.VC_CONTROL, NativeKeyEvent.VC_SPACE};
    private List<Integer> pressedKeys = new LinkedList<>();
    private int lastPressed = -1;
    private Observer notification;
    private ResetShortcutTimeManager resetShortcutTimeManager;

    public KeyShortcutFinder(Observer notification){
        initGlobalKeyListener();
        this.notification = notification;
        resetShortcutTimeManager = new ResetShortcutTimeManager(this);
    }

    private void initGlobalKeyListener(){
        // turn off jnativehook logger
        Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());
        logger.setLevel(Level.OFF);
        // handle events in main thread
        GlobalScreen.setEventDispatcher(new JavaFxDispatchService());
        try {
            GlobalScreen.registerNativeHook();
        }
        catch (NativeHookException ex) {
            System.exit(1);
        }
        GlobalScreen.addNativeKeyListener(this);
    }

    private void checkShortcut(){
        for(int i = 0; i < shortcut.length; i++){
            if(pressedKeys.get(i) != shortcut[i]){
                return;
            }
        }
        System.out.println("wykryto skrót");
        notification.update();
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int key = nativeKeyEvent.getKeyCode();
        if(pressedKeys.isEmpty() || lastPressed != key){
            pressedKeys.add(key);
            lastPressed = key;
            if(pressedKeys.size() == shortcut.length){
                checkShortcut();
            }
            resetShortcutTimeManager.startTimer();
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        int key = nativeKeyEvent.getKeyCode();
        pressedKeys.remove((Integer)key);
        lastPressed = -1;
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) { /* unimplemented */ }

    @Override
    public void update() {
        pressedKeys.clear();
        lastPressed = -1;
    }
}
