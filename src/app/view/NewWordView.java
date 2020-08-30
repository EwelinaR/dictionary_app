package app.view;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class NewWordView implements View {

    private int hideWindowPointY = -250;
    private Stage newWordStage;
    private Toast messageToast;

    private WindowAnimation windowAnimation;

    public NewWordView(Scene scene){
        Stage stage = createBaseStage();
        initScene(scene);
        createMainStage(scene, stage);
        windowAnimation = new WindowAnimation();
        messageToast = new Toast(newWordStage);
    }

    /**
     * This stage is always invisible but it allows to not open icon in the taskbar.
     * It is used as parent stage of main stage.
     *
     * @return Stage
     */
    private Stage createBaseStage(){
        Stage stage = new Stage();
        stage.initStyle(StageStyle.UTILITY);
        stage.setOpacity(0);
        stage.show();
        return stage;
    }

    private void initScene(Scene scene){
        // add linear gradient from white to transparent
        Stop[] stops = new Stop[] { new Stop(0, Color.WHITE), new Stop(1, Color.TRANSPARENT)};
        LinearGradient linear = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        scene.setFill(linear);
        String css = this.getClass().getResource("/resources/css/stylesheet.css").toExternalForm();
        scene.getStylesheets().add(css);
    }

    private void createMainStage(Scene scene, Stage baseStage){
        newWordStage = new Stage();
        newWordStage.initOwner(baseStage);
        newWordStage.initStyle(StageStyle.TRANSPARENT);
        newWordStage.setY(hideWindowPointY);
        newWordStage.setX((Screen.getPrimary().getVisualBounds().getWidth() - scene.getWidth()) / 2);
        newWordStage.setAlwaysOnTop(true);
        newWordStage.setScene(scene);
        newWordStage.show();
    }

    @Override
    public void open(){
        windowAnimation.setIsOpen(true);
    }

    @Override
    public void close() {
        windowAnimation.setIsOpen(false);
    }

    @Override
    public void reopen() {
        windowAnimation.reopen();
    }

    @Override
    public void showToast(String message){
        messageToast.show(message);
    }

    private class WindowAnimation extends AnimationTimer {

        private int windowPointY;
        private boolean open;
        private boolean reopen;

        WindowAnimation(){
            super();
            reopen = false;
        }

        void setIsOpen(boolean open) {
            windowPointY = (int) newWordStage.getY();
            this.open = open;
             if(windowPointY == 0 || windowPointY == hideWindowPointY) {
                 start();
             }
        }

        private void reopen() {
            windowPointY = (int) newWordStage.getY();
            if (windowPointY == hideWindowPointY) {
                // if window is close
                // open window
                open = true;
                start();
            } else if (windowPointY == 0) {
                // if window is open
                // quick close and open window
                open = false;
                reopen = true;
                start();
            } else if (!open){
                // if window is closing
                // quick close and open window
                reopen = true;
            } else {
                // if window is opening
                // immediately start closing and then open window
                open = false;
                reopen = true;
            }
        }

        @Override
        public void handle(long now) {
            if(open) open();
            else close();
        }

        private void open() {
            newWordStage.setY(windowPointY);
            if (windowPointY >= 0) {
                stop();
            }
            windowPointY += 10 + 5*(reopen ? 1 : 0);
            if (windowPointY >= 0) {
                windowPointY = 0;
            }
        }

        private void close() {
            newWordStage.setY(windowPointY);
            if (windowPointY <= hideWindowPointY) {
                if (reopen) {
                    open = true;
                    reopen = false;
                } else {
                    stop();
                }
            }
            windowPointY -= 10 + 5*(reopen ? 1 : 0);
            if (windowPointY <= hideWindowPointY) {
                windowPointY = hideWindowPointY;
            }
        }
    }
}
