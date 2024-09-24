package Battleship;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @SuppressWarnings("exports")
    @Override
    public void start(Stage primaryStage) {
        try {
            primaryStage.setTitle("Battleship Game");

            Image icon = new Image(getClass().getResource("Image") + "icon.png");
            primaryStage.getIcons().add(icon);

            Parent root = FXMLLoader.load(getClass().getResource("homescreen.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add(String.valueOf(getClass().getResource("application.css")));
            primaryStage.setScene(scene);

            primaryStage.setResizable(false);

            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
