package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    private void initAddingWordWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../resources/view/add_word.fxml"));
        new Scene(fxmlLoader.load(), 600, 250);
    }

    private void initMainWindow(Stage primaryStage) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../resources/view/sample.fxml"));
        primaryStage.setTitle("English words");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();
        primaryStage.setOnCloseRequest(e -> { Platform.exit(); System.exit(0); });
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        initAddingWordWindow();
        initMainWindow(primaryStage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
