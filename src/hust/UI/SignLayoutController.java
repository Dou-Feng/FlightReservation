package hust.UI;

import hust.Main;
import com.jfoenix.controls.*;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert;
import javafx.scene.layout.BorderPane;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


public class SignLayoutController implements Initializable {

    public JFXRadioButton rbtnDoctor;
    public JFXRadioButton rbtnPatient;
    public Label labelUserName;
    public Label labelPassword;
    public JFXTextField tUserName;
    public JFXPasswordField tPassword;
    public JFXButton btnSign;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelUserName.setVisible(false); // 用户名为空，提示不可见
        labelPassword.setVisible(false); // 密码为空，提示不可见

        // 把两个Radio Button添加到Toggle Group中
        final ToggleGroup group = new ToggleGroup();
        rbtnDoctor.setToggleGroup(group);
        rbtnPatient.setToggleGroup(group);

        // 用户名被删除，密码也同时消失
        tUserName.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (!tPassword.getText().isEmpty()) {
                    tPassword.clear();
                }
            }
        });

        // 鼠标选中用户名，用户名的输入被全选
        tUserName.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                Platform.runLater(new Runnable() { // 创建一个线程来运行
                    @Override
                    public void run() {
                        if (tUserName.isFocused()) {
                            labelUserName.setVisible(false);
                        }
                        if (tUserName.isFocused() && !tUserName.getText().isEmpty()) {
                            tUserName.selectAll();
                        }
                    }
                });
            }
        });

        // 密码被选中，取消提示
        tPassword.focusedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                if (tPassword.isFocused()) {
                    labelPassword.setVisible(false);
                }
            }
        });

        // 登陆按钮点击事件
        btnSign.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });
    }

    // 创建一个临时对话框
    private static void createAlert(String str) {
        System.out.println(str);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Flight");
        alert.setHeaderText("Message:");
        alert.setContentText(str);
        alert.showAndWait();
    }

    // 登陆成功，创建一个新的界面窗口
    private void signIn(String id, String toast,String url) {
        try {
            System.out.println(toast);
            Main.id = id;
            // 读取一个fxml文件
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(url));
            BorderPane borderPane = fxmlLoader.load(); // 从fxml文件中载入一个窗口
            Main.scene = new Scene(borderPane, borderPane.getPrefWidth(), borderPane.getPrefHeight()); // 写入场景
            ReservationController.controller = fxmlLoader.getController();
            Main.scene.getStylesheets().add(getClass().getResource("/res/style/list-cell-color.css").toString());
            Main.currentStage.setScene(Main.scene); // 把场景加载到当前的程序中
            Main.setWindowToCenter();
            Main.currentStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
