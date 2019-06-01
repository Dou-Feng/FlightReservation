package hust.UI;

import com.jfoenix.controls.JFXCheckBox;
import com.jfoenix.controls.JFXListView;
import hust.Main;
import hust.bean.Passenger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;

import javax.swing.*;
import java.net.URL;
import java.util.ResourceBundle;

public class InfoController implements Initializable {
    public static Scene infoScene = null; // 单例模式
    public static InfoController controller = null;
    @FXML
    private Label flightLabel;
    @FXML
    private Label takeoffLabel;
    @FXML
    private Label takeoffCityLabel;
    @FXML
    private Label landingLabel;
    @FXML
    private Label landingCityLabel;
    @FXML
    private Label durationLabel;

    @FXML
    private TextField contactNameText;
    @FXML
    private TextField contactPhoneText;

    public JFXListView<Passenger> passengerList;

    public static short passengerIndex = 0;

    private ObservableList<Passenger> passengerObservableList = FXCollections.observableArrayList();
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //
        passengerList.setItems(passengerObservableList);
        passengerList.setCellFactory(new Callback<ListView<Passenger>, ListCell<Passenger>>() {
            @Override
            public ListCell<Passenger> call(ListView<Passenger> param) {
                return new PassengerCell();
            }
        });
    }

    @FXML
    // 添加乘客信息项目item
    protected void addPassenger(ActionEvent event) {
        if(passengerIndex >= 9) {
            ReservationController.showDialog("不能再添加乘机人");
        } else {
            passengerObservableList.add(new Passenger(passengerIndex++));
        }
    }

    @FXML
    // 确认
    // 4个方面工作：
    // 1. 检查乘客信息输入是否合法，如果不合法（在插入到数据库中报错）需要进行弹窗提醒
    // 2. 自动生成订单号，把联系人的姓名、电话、还有订单日期等信息存入数据库
    // 3. 插入机票信息，自动生成机票编号；
    // 4. 生成账单，根据数据库中的数据和用户输入的信息（座位类型、乘客数、乘客类型），查询座位表得到价格
    //    然后生成最后的账单。
    // 这里涉及的4张表的插入，所以需要事务来提供进行一致性和完整性的保证
    protected void confirm(ActionEvent event) {

    }

    @FXML
    protected void backToFlight(ActionEvent event) {
        try {
            clearText();
            Main.currentStage.setScene(Main.scene);
            Main.currentStage.show();
            ReservationController.controller.switchAnimation(ReservationController.controller.spinPane,
                        ReservationController.controller.flightPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearText() {
        contactNameText.clear();
        contactPhoneText.clear();

    }

    public void setFlightLabel(String text) {
        flightLabel.setText(text);
    }

    public void setTakeoffLabel(String text) {
        takeoffLabel.setText(text);
    }

    public void setTakeoffCityLabel(String text) {
        takeoffCityLabel.setText(text);
    }
    public void setLandingLabel(String text) {
        landingLabel.setText(text);
    }
    public void setLandingCityLabel(String text) {
        landingCityLabel.setText(text);
    }
    public void setDurationLabel(String text) {
        durationLabel.setText(text);
    }

}
