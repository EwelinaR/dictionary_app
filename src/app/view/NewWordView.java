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

    private WindowAnimation windowAnimation;

    public NewWordView(Scene scene){
        Stage stage = createBaseStage();
        initScene(scene);
        createMainStage(scene, stage);
        windowAnimation = new WindowAnimation();
    }

    /**
     * This stage is always invisible but it allows to not show icon in the taskbar.
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
        newWordStage.setTitle("New word");
        newWordStage.setAlwaysOnTop(true);
        newWordStage.setScene(scene);
        newWordStage.show();
    }

    @Override
    public void show(){
        windowAnimation.setIsOpen(true);
        windowAnimation.start();
    }

    @Override
    public void hide() {
        windowAnimation.setIsOpen(false);
        windowAnimation.start();
    }

    private class WindowAnimation extends AnimationTimer {

        private int windowPointY;
        private boolean open;

        WindowAnimation(){
            super();
        }

        void setIsOpen(boolean open) {
            this.open = open;
            windowPointY = (int) newWordStage.getY();
        }

        @Override
        public void handle(long now) {
            if(open) open();
            else close();
        }

        private void open(){
            newWordStage.setY(windowPointY);
            if (windowPointY >= 0) {
                stop();
            }
            windowPointY += 10;
        }

        private void close(){
            newWordStage.setY(windowPointY);
            if (windowPointY <= hideWindowPointY) {
                stop();
            }
            windowPointY -= 10;
        }
    }
}
