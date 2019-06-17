package hust;

import hust.DB.DBConnection;
import hust.UI.ManagerController;
import hust.UI.ReservationController;
import hust.UI.UserController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

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

            FXMLLoader fxmlLoader2 = new FXMLLoader(Main.obj.getClass().getResource("/layout/ReservationLayout.fxml"));
            Pane pane2 = fxmlLoader2.load(); // 从fxml文件中载入一个窗口
            ReservationController.controller = fxmlLoader2.getController();
            ReservationController.reservationScene = new Scene(pane2, pane2.getPrefWidth(),pane2.getPrefHeight());
            ReservationController.reservationScene.getStylesheets().add(Main.obj.getClass().
                    getResource("/res/style/list-cell-color.css").toString()); // add CSS

            FXMLLoader fxmlLoader3 = new FXMLLoader(Main.obj.getClass().getResource("/layout/tableLayout.fxml"));
            Pane pane3 = fxmlLoader3.load(); // 从fxml文件中载入一个窗口
            UserController.scene = new Scene(pane3, pane3.getPrefWidth(), pane3.getPrefHeight()); // 写入场景
            UserController.controller = fxmlLoader3.getController();

            FXMLLoader fxmlLoader4 = new FXMLLoader(Main.obj.getClass().getResource("/layout/managerLayout.fxml"));
            BorderPane pane4 = fxmlLoader4.load(); // 从fxml文件中载入一个窗口
            ManagerController.scene = new Scene(pane4, pane4.getPrefWidth(), pane4.getPrefHeight()); // 写入场景
            ManagerController.controller = fxmlLoader4.getController();
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
                Main.scene = ReservationController.reservationScene;
                setWindowToCenter();
            } else if (url.contains("tableLayout.fxml")) {
                Main.scene = UserController.scene;
                UserController.controller.updateOrderList(Main.id);
            } else if (url.contains("managerLayout.fxml")) {
                Main.scene = ManagerController.scene;
            }
            Main.currentStage.setScene(Main.scene); // 把场景加载到当前的程序中
            Main.currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
