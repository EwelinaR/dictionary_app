package app;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class Main extends Application {

    private Stage primaryStage;

    private void initAddingWordWindow() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("../resources/view/add_word.fxml"));
        new Scene(fxmlLoader.load(), 600, 250);
    }

    private void initMainWindow() throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("../resources/view/sample.fxml"));
        primaryStage.setTitle("Your private Dictionary");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.getIcons().add(new Image("file:src/resources/img/icon_white_letter.PNG"));
    }

    private void initTrayIcon(){
        Platform.setImplicitExit(false);
        SwingUtilities.invokeLater(this::addAppToTray);
    }

    private TrayIcon configTrayIcon(){
        String letter = "D";
        Dimension trayIconSize = new TrayIcon(new BufferedImage(1, 1, BufferedImage.TYPE_4BYTE_ABGR)).getSize();
        try {
            File imageFile = new File("src/resources/img/icon_white_letter.PNG");
            java.awt.Image image = ImageIO.read(imageFile);
            image = image.getScaledInstance(trayIconSize.width, trayIconSize.height, java.awt.Image.SCALE_SMOOTH);
            return new TrayIcon(image);
        }
        catch (IOException e){
            e.printStackTrace();
            BufferedImage image = new BufferedImage(trayIconSize.width, trayIconSize.height, BufferedImage.TYPE_4BYTE_ABGR);
            Graphics2D g = image.createGraphics();
            g.setBackground(Color.WHITE);
            g.setFont(new Font("Serif", Font.BOLD, trayIconSize.width));

            g.drawString(letter, (trayIconSize.width - g.getFontMetrics().stringWidth(letter))/2, trayIconSize.height);
            g.dispose();
            return new TrayIcon(image);
        }
    }

    private MenuItem getOpenAppItem(){
        MenuItem openItem = new MenuItem("show application");
        openItem.addActionListener(event -> Platform.runLater(this::showStage));

        Font defaultFont = Font.decode(null);
        Font boldFont = defaultFont.deriveFont(Font.BOLD);
        openItem.setFont(boldFont);
        return openItem;
    }

    private MenuItem getExitItem(SystemTray tray, TrayIcon trayIcon){
        MenuItem exitItem = new MenuItem("Exit");
        exitItem.addActionListener(event -> {
            Platform.exit();
            tray.remove(trayIcon);
            System.exit(0);
        });
        return exitItem;
    }

    private void addAppToTray() {
        try {
            Toolkit.getDefaultToolkit();

            if (!SystemTray.isSupported()) {
                System.out.println("No system tray support, application exiting.");
                Platform.exit();
            }
            SystemTray tray = SystemTray.getSystemTray();
            TrayIcon trayIcon = configTrayIcon();
            trayIcon.addActionListener(event -> Platform.runLater(this::showStage));

            final PopupMenu popup = new PopupMenu();
            popup.add(getOpenAppItem());
            popup.addSeparator();
            popup.add(getExitItem(tray, trayIcon));
            trayIcon.setPopupMenu(popup);

            SwingUtilities.invokeLater(() ->
                    trayIcon.displayMessage(
                            "Dictionary app started!",
                            "Successfully run dictionary application\nNow you can translate words!",
                            TrayIcon.MessageType.INFO
                    )
            );
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.err.println("Unable to init system tray");
            e.printStackTrace();
        }
    }

    private void showStage() {
        if (primaryStage != null) {
            primaryStage.show();
            primaryStage.toFront();
        }
    }

    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage = primaryStage;
        initAddingWordWindow();
        initMainWindow();
        initTrayIcon();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
