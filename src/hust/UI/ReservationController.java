package hust.UI;

import com.gluonhq.charm.glisten.control.ToggleButtonGroup;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXTextField;
import hust.DB.DBConnection;
import hust.bean.Flight;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.property.DoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.util.Callback;
import javafx.util.Duration;
import org.controlsfx.control.MaskerPane;

import javax.management.Query;
import java.net.URL;
import java.security.Key;
import java.sql.*;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {
    @FXML
    private Pane orderPane; // 查询pane，处理查询的事务
    @FXML
    private Pane spinPane; // 用于过场
    @FXML
    private BorderPane flightPane; // 显示航班信息
    @FXML
    private Pane payPane; // 显示订单金额和付款
    @FXML
    private JFXButton UserOrderBtn; // 用户的订单信息按钮，按下可以打开一个新的窗口
                                    // 显示用户的订单信息和通知信息

    @FXML
    private ToggleButton roundTripBtn; // 往返旅途
    @FXML
    private ToggleButton oneWayBtn; // 单程旅途

    @FXML
    private JFXTextField flyFromText; // 出发城市
    @FXML
    private JFXTextField flyToText; // 到达城市
    @FXML
    private DatePicker departingDate; // 启程日期
    @FXML
    private DatePicker returningDate; // 返程日期
    @FXML
    private JFXButton travelerBtn; // 选择人数
    @FXML
    private VBox popupList; // 选择人数列表
    @FXML
    private JFXButton adultMinusBtn;
    @FXML
    private JFXButton adultPlusBtn;
    @FXML
    private JFXButton childMinusBtn;
    @FXML
    private JFXButton childPlusBtn;
    @FXML
    private JFXButton infantMinusBtn;
    @FXML
    private JFXButton infantPlusBtn;
    @FXML
    private Label adultsNumLabel;

    @FXML
    private Label childrenNumLabel;
    @FXML
    private Label infantsNumLabel;


    @FXML
    private JFXListView<Flight> flightList; // 显示航班信息

    private ObservableList<Flight> flightObservableList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 考虑第一个pane的初始化工作
        // 1. 获得text中出发时间、城市；达到时间城市的信息
        // 2. 能够处理错误，比如有一个或者多个输入框必要输入没有输入
        // 3. 能够在Menubutton的下拉菜单中处理人数更新的操作, 并在Menubutton的text处显示人数信息
        // 4. 返程按钮按下后，返程日期需要设计为disable
        // 5. 按下搜索按钮后，需要进行信息的搜集，当前pane隐藏，下一个pane的显示，中间用MaskerPane过渡
        // 6. 考虑场景切换的动画效果
        // 7. 在datePicker中当前日期以前的日期无法选择
        // 8. 点击订单按钮，打开一个新的窗口。当前窗口被取代。（最后实现）

        flightList.setItems(flightObservableList);
        // 实现自定义的item
        flightList.setCellFactory(new Callback<ListView<Flight>, ListCell<Flight>>() {
            @Override
            public ListCell<Flight> call(ListView<Flight> param) {
                param.setOnMouseClicked(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {
                        // do something when click
                    }
                });
                return new FlightCell();
            }

        });

        // 初始按钮的一些设置
        adultMinusBtn.setDisable(true);
        childMinusBtn.setDisable(true);
        infantMinusBtn.setDisable(true);


//        switchPane(PaneType.SPIN);
    }

    @FXML
    // 处理搜索按钮按下之后的事件
    protected void searchFlight(ActionEvent event) {
        // 检查输入是否合法
        boolean valid = true;
        if (flyFromText.getText().isEmpty()) {
            valid = false;
            // 设置颜色变化
        }
        if (flyToText.getText().isEmpty()) {
            valid = false;
            // 设置颜色变化
        }
        if (departingDate.getEditor().getText().isEmpty()) {
            valid = false;
            // 设置颜色变化
        }

        if (!valid) {
            // open a dialog and say sorry
            System.out.println("Debug: Input the right information");
            return;
        }
        String takeoffCity = flyFromText.getText();
        String landingCity = flyToText.getText();
        Date takeoffDate = Date.valueOf(departingDate.getEditor().getText());
        Date returnDate = !returningDate.getEditor().getText().isEmpty()?Date.valueOf(returningDate.getEditor().getText()):null;
        if (takeoffDate == null) {
            System.out.println("Debug: Input right take off time");
            return ;
        }
        int adult = Integer.valueOf(adultsNumLabel.getText());
        int children = Integer.valueOf(childrenNumLabel.getText());
        int infant = Integer.valueOf(infantsNumLabel.getText());
//        System.out.println(String.format("Debug: adults: %d, children: %d, infant: %d.", adult, children, infant));
        if (returnDate == null) {
            oneWayBtn.setSelected(true);
            oneWaySelected(null);
        }
        // play animation to invoke spin pane
        switchAnimation(orderPane, spinPane);

//        System.out.println("debug: flight observable before clear list item number:" + flightObservableList.size());
        flightList.getItems().clear(); // 清除列表项

//        System.out.println("debug: flight observable list item number:" + flightObservableList.size());
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // 处理查询

                    Connection connection = DBConnection.getConnection();
                    String sql = "select * from v_flight where Date=? and TakeoffCity=? and LandingCity=? and FCRemain+BCRemain+ECRemain>=? order by TakeoffTime asc";
                    PreparedStatement pstmt = connection.prepareStatement(sql);
                    pstmt.setDate(1, takeoffDate);
                    pstmt.setString(2, takeoffCity);
                    pstmt.setString(3, landingCity);
                    pstmt.setInt(4, adult + children + infant);
                    ResultSet rs = pstmt.executeQuery();
                    int num = 0;
                    while(rs.next()) {
                        String fno = rs.getString(2);
                        String takeoffcity = rs.getString(3);
                        Time takeofftime = rs.getTime(4);
                        String landingcity = rs.getString(5);
                        Time landingtime = rs.getTime(6);
                        Time duration = rs.getTime(7);
                        int fcremain = rs.getInt(8);
                        float fcprice = rs.getFloat(9);
                        int bcremain = rs.getInt(10);
                        float bcprice = rs.getFloat(11);
                        int ecremain = rs.getInt(12);
                        float ecprice = rs.getFloat(13);
                        Flight f = new Flight(fno, takeoffcity, landingcity, takeofftime, landingtime, duration,
                                fcremain, bcremain, ecremain, fcprice, bcprice, ecprice);
                        flightObservableList.add(f);
                        num++;
                    }
                    switchAnimation(spinPane, flightPane);
//                    System.out.println("result number: " + num);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void switchAnimation(Pane p1, Pane p2) {

        KeyValue p1Opacity = new KeyValue(p1.opacityProperty(), 0.0);
        KeyValue p2Opacity = new KeyValue(p2.opacityProperty(), 1.0);
//        KeyValue p1Visibility = new KeyValue(p1.visibleProperty(), false);
//        KeyValue p2Visibility = new KeyValue(p2.visibleProperty(), true);
        p1.setVisible(false);
        p2.setVisible(true);
        KeyFrame fk1 = new KeyFrame(Duration.millis(200), p1Opacity);
        KeyFrame fk2 = new KeyFrame(Duration.millis(500), p2Opacity);

        Timeline t1 = new Timeline();
        t1.getKeyFrames().add(fk1);
        t1.play();

        Timeline t2 = new Timeline();
        t2.getKeyFrames().add(fk2);
        t2.play();
    }



    @FXML
    protected void roundTripSelected(ActionEvent event) {
        roundTripBtn.setStyle("-fx-background-color: #3b89c5");
        oneWayBtn.setStyle("-fx-background-color: white");
        returningDate.setDisable(false);
    }

    @FXML
    protected void oneWaySelected(ActionEvent event) {
        roundTripBtn.setStyle("-fx-background-color: white");
        oneWayBtn.setStyle("-fx-background-color: #3b89c5");
        returningDate.setDisable(true);
    }

    @FXML
    protected void backToOrderPane(ActionEvent event) {
        switchAnimation(flightPane, orderPane);
        spinPane.setVisible(false);
    }
    @FXML
    protected void travelerBtnClicked(ActionEvent event) {
        popupList.setVisible(!popupList.isVisible());
    }

    @FXML
    protected void minusAdult(ActionEvent event) {
        int adultNum = Integer.valueOf(adultsNumLabel.getText());
        if (adultNum == 2) {
            adultMinusBtn.setDisable(true);
        }
        adultPlusBtn.setDisable(false);
        adultsNumLabel.setText(String.valueOf(--adultNum));
        int childrenNum = Integer.valueOf(childrenNumLabel.getText());
        int infantNum = Integer.valueOf(infantsNumLabel.getText());
        if (childrenNum+infantNum > 3 * adultNum) {
            childrenNumLabel.setText("0");
            infantsNumLabel.setText("0");
            childPlusBtn.setDisable(false);
            infantPlusBtn.setDisable(false);
            childMinusBtn.setDisable(true);
            infantMinusBtn.setDisable(true);
        }
        if (infantNum > adultNum) {
            infantsNumLabel.setText("0");
            infantMinusBtn.setDisable(true);
            infantPlusBtn.setDisable(false);
        }
        handleTravelChanged();

    }
    @FXML
    protected void addAdult(ActionEvent event) {
        int adultNum = Integer.valueOf(adultsNumLabel.getText());
        if (adultNum == 8) {
            adultPlusBtn.setDisable(true);
        }
        adultMinusBtn.setDisable(false);
        adultsNumLabel.setText(String.valueOf(++adultNum));
        handleTravelChanged();
    }
    @FXML
    protected void minusChild(ActionEvent event) {
        int childrenNum = Integer.valueOf(childrenNumLabel.getText());
        int adultNum = Integer.valueOf(adultsNumLabel.getText());
        if (childrenNum <= 1)
            childMinusBtn.setDisable(true);
        childPlusBtn.setDisable(false);
        int infantNum = Integer.valueOf(infantsNumLabel.getText());
        if (infantNum < 9 && infantNum < adultNum)
            infantPlusBtn.setDisable(false);
        childrenNumLabel.setText(String.valueOf(--childrenNum));
        handleTravelChanged();
    }
    @FXML
    protected void minusInfant(ActionEvent event) {
        int infantNum = Integer.valueOf(infantsNumLabel.getText());
        int childNum = Integer.valueOf(childrenNumLabel.getText());
        if (childNum < 9)
            childPlusBtn.setDisable(false);
        if (infantNum <= 1)
            infantMinusBtn.setDisable(true);
        infantPlusBtn.setDisable(false);
        infantsNumLabel.setText(String.valueOf(--infantNum));
        handleTravelChanged();
    }
    @FXML
    protected void addChild(ActionEvent event) {
        handlePlusChildAndInfant((char)1);
    }
    @FXML
    protected void addInfant(ActionEvent event) {
        handlePlusChildAndInfant((char)2);

    }

    private void handlePlusChildAndInfant(char a) {
        int adultNum = Integer.valueOf(adultsNumLabel.getText());
        int childrenNum = Integer.valueOf(childrenNumLabel.getText());
        int infantNum = Integer.valueOf(infantsNumLabel.getText());
        if (childrenNum+infantNum >= adultNum*3-1) {
            childPlusBtn.setDisable(true);
            infantPlusBtn.setDisable(true);
        }
        if (a == 1) {
            if (childrenNum >= 8)
                childPlusBtn.setDisable(true);
            childMinusBtn.setDisable(false);
            childrenNumLabel.setText(String.valueOf(++childrenNum));
        } else {
            if (infantNum >= adultNum - 1)
                infantPlusBtn.setDisable(true);
            infantMinusBtn.setDisable(false);
            infantsNumLabel.setText(String.valueOf(++infantNum));
        }
        handleTravelChanged();
    }

    private void handleTravelChanged() {
        int adultNum = Integer.valueOf(adultsNumLabel.getText());
        int childrenNum = Integer.valueOf(childrenNumLabel.getText());
        int infantNum = Integer.valueOf(infantsNumLabel.getText());
        StringBuilder result = new StringBuilder();
        result.append(adultNum);
        if (adultNum != 1)
            result.append(" adults");
        else
            result.append(" adult");
        if (childrenNum > 0) {
            result.append(", ");
            result.append(childrenNum);
            if (childrenNum != 1)
                result.append(" children");
            else
                result.append(" child");

        }
        if (infantNum > 0) {
            result.append(", ");
            result.append(infantNum);
            if (infantNum != 1)
                result.append(" infants");
            else
                result.append(" infant");
        }
        travelerBtn.setText(result.toString());
    }



}


enum PaneType {
    ORDER, FLIGHT, SPIN, PAY
}