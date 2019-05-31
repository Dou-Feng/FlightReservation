package hust;

import hust.DB.DBConnection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    public static Scene scene = null;
    public static Stage currentStage = null;
    public static boolean isPatient = true;
    public static String id;
    @Override
    public void start(Stage stage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/ReservationLayout.fxml"));
            Pane pane = fxmlLoader.load();
            scene = new Scene(pane, pane.getPrefWidth(),pane.getPrefHeight());
            scene.getStylesheets().add(getClass().getResource("/res/style/list-cell-color.css").toString());
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


    public static void main(String[] args) {
        DBConnection.getConnection();
        launch(args);

    }
}
