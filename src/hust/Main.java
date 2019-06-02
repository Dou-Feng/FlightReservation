package hust;

import hust.DB.DBConnection;
import hust.UI.ReservationController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    public static Scene scene = null;
    public static Stage currentStage = null;
    public static String id;
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/SignLayout.fxml"));
            Pane pane = fxmlLoader.load();
            scene = new Scene(pane, pane.getPrefWidth(),pane.getPrefHeight());
            currentStage = stage;
            currentStage.setTitle("Flight");
            currentStage.getIcons().add(new Image(this.getClass().getResourceAsStream("/res/images/flight.png")));
            currentStage.setScene(scene);
            currentStage.setResizable(false);
            currentStage.show();
        }   catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setWindowToCenter() {
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        Main.currentStage.setX((screenBounds.getWidth() - scene.getWidth())/2);
        Main.currentStage.setY((screenBounds.getHeight() - scene.getHeight())/2);
    }


    public static void main(String[] args) {
        DBConnection.getConnection();
        launch(args);

    }
}
