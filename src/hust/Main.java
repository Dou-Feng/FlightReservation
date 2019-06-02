package hust;

import hust.DB.DBConnection;
import hust.UI.ReservationController;
import hust.UI.UserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    public static Object obj = null;
    public static Scene scene = null;
    public static Stage currentStage = null;
    public static String id = "root"; // init for debug
    @Override
    public void start(Stage stage) {
        try {
            obj = this;
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

    // 创建一个新的界面窗口
    public static void openNewWin(String id, String toast,String url) {
        try {
            System.out.println(toast);
            Main.id = id;
            // 读取一个fxml文件
            if (url.contains("ReservationLayout.fxml")) {
                if (ReservationController.reservationScene == null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.obj.getClass().getResource(url));
                    Pane pane = fxmlLoader.load(); // 从fxml文件中载入一个窗口
                    Main.scene = new Scene(pane, pane.getPrefWidth(), pane.getPrefHeight()); // 写入场景
                    ReservationController.controller = fxmlLoader.getController();
                    ReservationController.reservationScene = Main.scene;
                    Main.scene.getStylesheets().add(Main.obj.getClass().
                            getResource("/res/style/list-cell-color.css").toString());
                    Main.setWindowToCenter();
                } else {
                    Main.scene = ReservationController.reservationScene;
                }
            } else if (url.contains("tableLayout.fxml")) {
                if (UserController.scene == null) {
                    FXMLLoader fxmlLoader = new FXMLLoader(Main.obj.getClass().getResource(url));
                    Pane pane = fxmlLoader.load(); // 从fxml文件中载入一个窗口
                    Main.scene = new Scene(pane, pane.getPrefWidth(), pane.getPrefHeight()); // 写入场景
                    UserController.scene = Main.scene;
                    UserController.controller = fxmlLoader.getController();
                } else {
                    Main.scene = UserController.scene;
                    UserController.controller.updateOrderList(Main.id);
                }
            }
            Main.currentStage.setScene(Main.scene); // 把场景加载到当前的程序中
            Main.currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
