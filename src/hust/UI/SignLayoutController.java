package hust.UI;

import hust.DB.DBConnection;
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
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;


public class SignLayoutController implements Initializable {

    public JFXRadioButton rbtnUser;
    public JFXRadioButton rbtnManager;
    public Label labelUserName;
    public Label labelPassword;
    public JFXTextField tUserName;
    public JFXPasswordField tPassword;
    public JFXButton btnSign;
    public Pane mainPane;
    public Pane spinPane;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        labelUserName.setVisible(false); // 用户名为空，提示不可见
        labelPassword.setVisible(false); // 密码为空，提示不可见

        // 把两个Radio Button添加到Toggle Group中
        final ToggleGroup group = new ToggleGroup();
        rbtnUser.setToggleGroup(group);
        rbtnManager.setToggleGroup(group);

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
                String id = tUserName.getText();
                String passwd = tPassword.getText();
                if (id.isEmpty()) { // 如果没输入用户名
                    labelUserName.setVisible(true);
                } else if (passwd.isEmpty()) { // 如果没输入密码
                    labelPassword.setVisible(true);
                } else { // 进行登陆验证
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                Connection conn = DBConnection.getConnection();
                                String sql = "select * from T_User where userName=? and password=?;";
                                PreparedStatement stmt = conn.prepareStatement(sql);
                                stmt.setString(1, id);
                                stmt.setString(2, passwd);
                                ResultSet rs = stmt.executeQuery();
                                if (rs.next()) {
                                    if (rbtnUser.isSelected()) {

                                        Main.openNewWin(id, "User Sign In Successfully!","/layout/ReservationLayout.fxml");
                                    } else {
                                        createAlert("登录成功暂未实现，请谅解。");
                                    }
                                } else {
                                    createAlert("用户名或密码错误");
                                }
                            } catch (SQLException e) {
                                createAlert("数据库发生故障T_T");
                                e.printStackTrace();
                            }
                        }
                    });
                }
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


}
