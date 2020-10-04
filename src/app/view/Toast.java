package app.view;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

class Toast {
    private Text text;
    private Stage stage;
    private Stage ownerStage;

    Toast(Stage ownerStage) {
        this.ownerStage = ownerStage;
        initStage();
        initTextField();
        addTextToStage();
    }

    private void initStage() {
        stage = new Stage();
        stage.initOwner(ownerStage);
        stage.initStyle(StageStyle.TRANSPARENT);
    }

    private void initTextField() {
        text = new Text();
        text.setFont(Font.font("Verdana", 15));
        text.setFill(Color.WHITE);
    }

    private void addTextToStage() {
        StackPane root = new StackPane(text);
        root.setId("toast");
        root.getStylesheets().add(Toast.class.getResource("/resources/css/stylesheet.css").toExternalForm());
        root.setOpacity(0);

        Scene scene = new Scene(root);
        scene.setFill(Color.TRANSPARENT);
        stage.setScene(scene);
        stage.show();
    }

    private void updateToastPosition() {
        stage.setY(ownerStage.getY() + ownerStage.getHeight() + 10);
        stage.setX(ownerStage.getX());
        stage.setWidth(ownerStage.getWidth());
    }

    private void updateMessage(String message) {
        // cut too long text
        if(message.length() > 70)
            message = message.substring(0, 70) + " (...)";
        text.setText(message);
    }

    void show(String message) {
        int fadeDelay = 500;
        updateToastPosition();
        updateMessage(message);

        Timeline fadeInTimeline = new Timeline();
        KeyFrame fadeInKey = new KeyFrame(Duration.millis(fadeDelay),
                new KeyValue(stage.getScene().getRoot().opacityProperty(), 1));
        fadeInTimeline.getKeyFrames().add(fadeInKey);
        fadeInTimeline.setOnFinished((ae) -> new Thread(() -> playToastAnimation(fadeDelay)).start());
        fadeInTimeline.play();
    }

    private void playToastAnimation(int fadeDelay) {
        int toastDelay = 1500;
        try {
            Thread.sleep(toastDelay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Timeline fadeOutTimeline = new Timeline();
        KeyFrame fadeOutKey1 = new KeyFrame(Duration.millis(fadeDelay),
                new KeyValue(stage.getScene().getRoot().opacityProperty(), 0));
        fadeOutTimeline.getKeyFrames().add(fadeOutKey1);
        fadeOutTimeline.play();
    }
}
