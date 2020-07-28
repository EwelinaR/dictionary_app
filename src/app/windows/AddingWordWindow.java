package app.windows;

import javafx.animation.AnimationTimer;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.paint.CycleMethod;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class AddingWordWindow implements NewWindowHandler {

    private int hideWindowPointY = -250;
    private Stage addingWordStage;

    @Override
    public void show(){
        AnimationTimer timer = new PopUpAnimation(true);
        timer.start();
    }

    @Override
    public void close() {
        AnimationTimer timer = new PopUpAnimation(false);
        timer.start();
    }

    private class PopUpAnimation extends AnimationTimer {
        private int windowPointY;
        private boolean open;

        PopUpAnimation(boolean open){
            super();
            this.open = open;
            if(open) windowPointY = hideWindowPointY;
            else windowPointY = 0;
        }

        @Override
        public void handle(long now) {
            if(open) open();
            else close();
        }

        private void open(){
            addingWordStage.setY(windowPointY);
            if (windowPointY >= 0) {
                stop();
            }
            windowPointY += 10;
        }

        private void close(){
            addingWordStage.setY(windowPointY);
            if (windowPointY <= hideWindowPointY) {
                stop();
            }
            windowPointY -= 10;
        }
    }

    public AddingWordWindow(Scene scene){
        Stage utilityParentStage = new Stage();
        utilityParentStage.initStyle(StageStyle.UTILITY);
        utilityParentStage.setOpacity(0);
        utilityParentStage.show();
        addingWordStage = new Stage();
        addingWordStage.initOwner(utilityParentStage);

        Stop[] stops = new Stop[] { new Stop(0, Color.WHITE), new Stop(1, Color.TRANSPARENT)};
        LinearGradient linear = new LinearGradient(0, 0, 0, 1, true, CycleMethod.NO_CYCLE, stops);
        scene.setFill(linear);

        addingWordStage.initStyle(StageStyle.TRANSPARENT);
        String css = this.getClass().getResource("/resources/css/stylesheet.css").toExternalForm();
        scene.getStylesheets().add(css);
        addingWordStage.setY(hideWindowPointY);
        addingWordStage.setX((Screen.getPrimary().getVisualBounds().getWidth() - scene.getWidth()) / 2);
        addingWordStage.setTitle("New word");
        addingWordStage.setAlwaysOnTop(true);
        addingWordStage.setScene(scene);

        addingWordStage.show();
    }
}
