package sample;

import org.jnativehook.keyboard.NativeKeyEvent;
import org.jnativehook.keyboard.NativeKeyListener;

import java.util.LinkedList;
import java.util.List;

public class GlobalKeyListener implements NativeKeyListener {
    private final int[] SHORTCUT = new int[]{NativeKeyEvent.VC_CONTROL,
                                                NativeKeyEvent.VC_C,
                                                NativeKeyEvent.VC_D};

    private List<Integer> pressedKeys = new LinkedList<>();
    private int lastPressed = -1;

    private void checkShortcut(){
        for(int i = 0; i < SHORTCUT.length; i++){
            if(pressedKeys.get(i) != SHORTCUT[i]){
                return;
            }
        }
        // TODO open pop-up
        System.out.println("shortcut detected!");   // test
    }

    @Override
    public void nativeKeyPressed(NativeKeyEvent nativeKeyEvent) {
        int key = nativeKeyEvent.getKeyCode();
        if(pressedKeys.isEmpty() || lastPressed != key){
            pressedKeys.add(key);
            lastPressed = key;
            if(pressedKeys.size() == SHORTCUT.length){
                checkShortcut();
            }
        }
    }

    @Override
    public void nativeKeyReleased(NativeKeyEvent nativeKeyEvent) {
        int key = nativeKeyEvent.getKeyCode();
        pressedKeys.remove((Integer)key);
    }

    @Override
    public void nativeKeyTyped(NativeKeyEvent nativeKeyEvent) { /* unimplemented */ }
}
