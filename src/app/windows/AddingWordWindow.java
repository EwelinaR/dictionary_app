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

    private Stage addingWordStage;

    @Override
    public void show(){
        AnimationTimer timer = new PopUpAnimation();
        timer.start();
        addingWordStage.show();
    }

    @Override
    public void close() {
        addingWordStage.hide();
    }

    private class PopUpAnimation extends AnimationTimer {
        private int windowsSize = 250;
        @Override
        public void handle(long now) {
            addingWordStage.setY(-windowsSize);
            if (windowsSize <= 0) {
                stop();
            }
            windowsSize -= 10;
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
        addingWordStage.setY(0);
        addingWordStage.setX((Screen.getPrimary().getVisualBounds().getWidth() - scene.getWidth()) / 2);
        addingWordStage.setTitle("New word");
        addingWordStage.setAlwaysOnTop(true);
        addingWordStage.setScene(scene);
    }
}
