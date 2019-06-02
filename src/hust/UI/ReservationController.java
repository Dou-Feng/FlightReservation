package hust.UI;

import com.jfoenix.controls.*;
import hust.DB.DBConnection;
import hust.Main;
import hust.bean.Flight;
import hust.bean.Passenger;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.util.Callback;
import javafx.util.Duration;
import java.net.URL;
import java.sql.*;
import java.util.Iterator;
import java.util.ResourceBundle;

public class ReservationController implements Initializable {
    public static ReservationController controller = null; // 单例模式
    public static Scene reservationScene = null;
    public Pane orderPane; // 查询pane，处理查询的事务

    public Pane spinPane; // 用于过场

    public BorderPane flightPane; // 显示航班信息

    public Pane infoPane; //
    @FXML
    private ScrollPane scrollPane; // 滑动pane

    public Pane payPane; // 显示订单金额和付款

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




    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // 考虑第一个pane的初始化工作
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
        // for debug
        flightObservableList.add(new Flight());
        // 初始按钮的一些设置
        adultMinusBtn.setDisable(true);
        childMinusBtn.setDisable(true);
        infantMinusBtn.setDisable(true);

        // disable horizontal scroll (useless)
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);

        // 第二阶段
        // 点击预订按钮
        // 在点击预订按钮后，首先需要得知用户选择的航班的信息，（航班号，日期，座位类型，票价等）

        // 绑定一个cell factory 填充乘客的列表
        passengerList.setItems(passengerObservableList);
        passengerList.setCellFactory(new Callback<ListView<Passenger>, ListCell<Passenger>>() {
            @Override
            public ListCell<Passenger> call(ListView<Passenger> param) {
                return new PassengerCell();
            }
        });

    }



    //---------------------- order pane implementation-----------------------
    // 1. 获得text中出发时间、城市；达到时间城市的信息
    // 2. 能够处理错误，比如有一个或者多个输入框必要输入没有输入，返回一个弹框提示用户输入
    // 3. 能够在Menubutton的下拉菜单中处理人数更新的操作, 并在Menubutton的text处显示人数信息
    // 4. 返程按钮按下后，返程日期需要设计为disable
    // 5. 按下搜索按钮后，需要进行信息的搜集，当前pane隐藏，下一个pane的显示，中间用MaskerPane过渡
    // 6. 考虑场景切换的动画效果
    // 7. 在datePicker中当前日期以前的日期无法选择
    // 8. 点击订单按钮，打开一个新的窗口。当前窗口被取代。（最后实现）
    // 用来保存机票的信息
    private ObservableList<Passenger> passengerObservableList = FXCollections.observableArrayList();
    public static Date takeoffDate = Date.valueOf("2019-05-31");

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
            showDialog("请输入正确的信息");
            return;
        }
        String takeoffCity = flyFromText.getText();
        String landingCity = flyToText.getText();
        Date takeoffDate = Date.valueOf(departingDate.getEditor().getText());
        Date returnDate = !returningDate.getEditor().getText().isEmpty()?Date.valueOf(returningDate.getEditor().getText()):null;
        if (takeoffDate == null) {
            System.out.println("Debug: Input right take off time");
            showDialog("请输入正确的出行时间");
            return ;
        }
        int adult = Integer.valueOf(adultsNumLabel.getText());
        int children = Integer.valueOf(childrenNumLabel.getText());
        int infant = Integer.valueOf(infantsNumLabel.getText());
        if (returnDate == null) {
            oneWayBtn.setSelected(true);
            oneWaySelected(null);
        }
        // play animation to invoke spin pane
        switchAnimation(orderPane, spinPane);

        flightList.getItems().clear(); // 清除列表项

        ReservationController.takeoffDate = takeoffDate; // 设置全局变量用来处理订单信息
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                try {
                    // 处理查询
                    Connection connection = DBConnection.getConnection();
                    String sql = "select * from v_flight where Date=? and TakeoffCity=? and LandingCity=? and " +
                                    "FCRemain+BCRemain+ECRemain>=? order by TakeoffTime asc";
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


    public static void switchAnimation(Pane p1, Pane p2) {
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

    public static void showDialog(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("信息");
        alert.setContentText(message);
        alert.showAndWait();

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

        result.append("名成人");
        if (childrenNum > 0) {
            result.append(", ");
            result.append(childrenNum);
            result.append("名小孩");

        }
        if (infantNum > 0) {
            result.append(", ");
            result.append(infantNum);
            result.append("名婴儿");
        }
        travelerBtn.setText(result.toString());
    }

    @FXML
    protected void popListMouseExited(MouseEvent event) {
        popupList.setVisible(false);
//        System.out.println("Mouse Exit popup list.");
    }

    @FXML
    protected void searchKeyPressed(KeyEvent event) {
//        System.out.println("Key pressed");
        if (event.getCode() == KeyCode.ENTER) {
            searchFlight(null);
        }
    }

    // 打开一个用户的订单信息窗口
    @FXML
    protected void openUserTable(ActionEvent event) {
        Main.openNewWin(Main.id, "Open user's order table", "/layout/tableLayout.fxml");
    }

    // -----------------------------flight pane implementation ------------------------------
    @FXML
    protected void flightPaneKeyHandle(KeyEvent event) {
        if (event.getCode() == KeyCode.ESCAPE) {
            backToOrderPane(null);
        }
    }

    // -----------------------------information pane implementation ------------------------------

    public static short passengerIndex = 0;
    @FXML
    private JFXButton addPassengerBtn;

    private static int addPassengerNum = -1;

    @FXML
    // 添加乘客信息项目item
    public void addPassenger(ActionEvent event) {

        if(passengerIndex >= 9) {
            ReservationController.showDialog("不能再添加乘机人");
            return;
        }
        // 查询数据库，余票是否支持插入乘客
        try {
            if (addPassengerNum == -1) {
                short seatC = FlightCell.seatClass2short(FlightCell.seatClass); // 用来记录座位类型
                String seatRemain = (seatC == 1 ? "FCRemain" : seatC == 2 ? "BCRemain" : "ECRemain");
                String querySql = "select " + seatRemain + " from Seats where FNo=? and Date=?;";
                Connection conn = DBConnection.getConnection();
                PreparedStatement pStmt = conn.prepareStatement(querySql);
                pStmt.setString(1, flightLabel.getText());
                pStmt.setDate(2, takeoffDate);
                ResultSet rs = pStmt.executeQuery();
                if (rs.next())
                    addPassengerNum = rs.getInt(1);
                else
                    throw new SQLException("输入了错误的信息！");
            }
            if (addPassengerNum > 0) {// 如果还有剩余座位，可以添加
                passengerObservableList.add(new Passenger(passengerIndex++));
                if (--addPassengerNum == 0)
                    addPassengerBtn.setDisable(true);
            } else
                addPassengerBtn.setDisable(true);
        } catch (SQLException e) {
            showDialog("输入了错误的信息");
            e.printStackTrace();
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
        if (contactNameText.getText().isEmpty() || contactPhoneText.getText().isEmpty()) {
            ReservationController.showDialog("请填写联系人信息");
            return;
        }
        int validPassengerNum = 0;
        Iterator<Passenger> it = passengerObservableList.iterator();
        while (it.hasNext()) {
            Passenger p = it.next();
            if (p.isValid())
                validPassengerNum++;
            else {
                if (passengerObservableList.size() > 1) // 保留一个
                    it.remove(); // 去掉无效的乘客信息
            }
        }
        if (validPassengerNum == 0) {
            ReservationController.showDialog("乘客人数至少为1");
            return;
        }
        switchAnimation(infoPane, spinPane); // 过场动画

        // 开启一个线程，开始数据库操作
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                try {
                    conn = DBConnection.getConnection();
                    conn.setAutoCommit(false); // 取消自动提交
                    // 首先生成订单信息
                    String sqlOrder = "insert into T_Order (OrderNo, ContactName, ContactPNo, TicketNum, takeoffDate)values(?,?,?,?,?);";
                    PreparedStatement pstmt = conn.prepareStatement(sqlOrder);
                    int startONo = getNumFromDatabase(conn,"select count(*) from T_Order;");
                    String orderNo = String.format("%8d", ++startONo).replace(" ", "0");
                    pstmt.setString(1, orderNo);
                    pstmt.setString(2, contactNameText.getText());
                    pstmt.setString(3, contactPhoneText.getText());
                    pstmt.setInt(4, passengerObservableList.size());
                    pstmt.setDate(5, takeoffDate);
                    pstmt.executeUpdate(); // 执行第一次sql语句，插入一条记录到订单表中

                    // 然后把该订单记录插入到用户订单表中
                    String sqlUserOrder = "insert into T_User_Order values (?,?);";
                    PreparedStatement userOrderStmt = conn.prepareStatement(sqlUserOrder);
                    userOrderStmt.setString(1, Main.id);
                    userOrderStmt.setString(2, orderNo);
                    userOrderStmt.executeUpdate(); // 第2次执行sql语句，插入一条记录到用户订单表中

                    // 创建一些sql语句
                    String sqlPassenger = "insert into Passenger values(?,?,?,?,?);";
                    String sqlTicket = "insert into Ticket values (?,?,?,?,?);";
                    String queryPassenger = "select * from Passenger where PName like ?;";

                    float amount = 0.0f; // 用来统计订单的账单情况
                    int passengerNum = 0; // 用来记录乘客人数
                    short seatC = FlightCell.seatClass2short(FlightCell.seatClass); // 用来记录座位类型
                    float price = 0.0f; // 用来记录价格
                    int seatRemain_t = 0; // 用来记录剩余座位数
                    String seatRemain =  (seatC==1?"FCRemain":seatC==2?"BCRemain":"ECRemain");
                    // 从视图中查询到座位的价格和剩余座位数
                    String sqlFlightInfo = "select * from v_flight where Date=? and FNo=? ";
                    PreparedStatement prStmt = conn.prepareStatement(sqlFlightInfo);
                    prStmt.setString(2, flightLabel.getText());
                    prStmt.setDate(1, takeoffDate);
                    ResultSet rs = prStmt.executeQuery();
                    if(rs.next()) {
                        int fcremain = rs.getInt(8);
                        int bcremain = rs.getInt(10);
                        int ecremain = rs.getInt(12);
                        float fcprice = rs.getFloat(9);
                        float bcprice = rs.getFloat(11);
                        float ecprice = rs.getFloat(13);
                        seatRemain_t = (seatC==1?fcremain:seatC==2?bcremain:ecremain);
                        price = (seatC==1?fcprice:seatC==2?bcprice:ecprice);

                    } else {
                        throw new SQLException("Can not find flight information");
                    }

                    // 从数据库中得到乘客表中的表项数
                    int startPNo = getNumFromDatabase(conn, "select count(*) from Passenger;");
                    // 从数据库中获取机票的最大编号信息
                    int startTNo = getNumFromDatabase(conn, "select count(*) from Ticket;");
                    for (Passenger p : passengerObservableList) {
                        PreparedStatement passStmt = conn.prepareStatement(queryPassenger);
                        passStmt.setString(1, p.getName());
                        ResultSet prs = passStmt.executeQuery(); // 通过姓名查询乘客
                        if (!prs.next()) {
                            // 如果数据库中不存在这名乘客（不存在同名的乘客），插入到数据库中
                            PreparedStatement pstmt2 = conn.prepareStatement(sqlPassenger);
                            p.setNo(String.format("%12d", ++startPNo).replace(" ", "0"));
                            pstmt2.setString(1, p.getNo());
                            pstmt2.setString(2, p.getName());
                            pstmt2.setShort(3, p.getCerType());
                            pstmt2.setString(4, p.getCerNo());
                            pstmt2.setShort(5, p.getpType());
                            pstmt2.executeUpdate(); // 第3次执行，插入一条乘客记录
                        } else {
                            p.setNo(prs.getString(1)); // 得到乘客的编号
                        }
                        // 准备插入一张机票信息：机票编号、航班编号、乘客编号、起飞时间、座位类型、票价
                        PreparedStatement pstmt3 = conn.prepareStatement(sqlTicket);
                        String ticketNo = String.format("%13d", ++startTNo).replace(" ", "0");
                        pstmt3.setString(1, ticketNo);
                        pstmt3.setString(2, flightLabel.getText());
                        pstmt3.setString(3, p.getNo());
                        pstmt3.setShort(4, seatC);
                        pstmt3.setFloat(5, price);
                        pstmt3.executeUpdate(); // 第4次执行sql语句，插入一张机票记录
                        amount += p.getpType()==1?price:price/2; // 累计添加账单的总量,小孩子打折

                        // 把订单号和机票号插入到订单机票表中
                        String insertOT = "insert into T_Order_Ticket values (?,?)";
                        PreparedStatement insertOTStmt = conn.prepareStatement(insertOT);
                        insertOTStmt.setString(1, orderNo);
                        insertOTStmt.setString(2, ticketNo);
                        insertOTStmt.executeUpdate(); // 第5次执行sql语句，插入一条记录到订单机票表中
                        passengerNum++; // 每张票生成后，乘客数加一
                    }
                    // 修改座位表信息
                    String updateSeat = "update Seats set " + seatRemain + "=? where FNo=? and Date=?";
                    PreparedStatement updateSeatStmt = conn.prepareStatement(updateSeat);
                    updateSeatStmt.setInt(1, seatRemain_t - passengerNum);
                    updateSeatStmt.setString(2, flightLabel.getText());
                    updateSeatStmt.setDate(3, takeoffDate);
                    updateSeatStmt.executeUpdate(); // 第6次执行sql语句，更新座位数
                    // 生成账单
                    String insertPay = "insert into T_Bill values (?,?,?);";
                    PreparedStatement payStmt = conn.prepareStatement(insertPay);
                    payStmt.setString(1, orderNo);
                    payStmt.setFloat(2, amount);
                    payStmt.setInt(3,0);
                    payStmt.executeUpdate(); // 第7次执行sql语句，插入一条记录到订单机票表中

                    conn.commit(); // 最后的commit，让7次sql操作保持完整性
                    ReservationController.amount = amount;
                    ReservationController.orderNo = orderNo;
                    payPaneAmountLabel.setText("订单金额：" + String.valueOf(amount) + "元");
                    payPaneOrderLabel.setText("订单号："+orderNo);
                    clearText();
                    switchAnimation(spinPane, payPane); // 如果执行顺利，那么最终会到达支付界面
                } catch (SQLException e) {
                    System.out.println(e.getMessage());
                    System.out.println("debug: sql insert fail. rollback");
                    try {
                        conn.rollback();
                        clearText();
                        switchAnimation(spinPane, errorPane); // 切换到错误的界面
                    } catch (SQLException c) {
                        System.out.println("debug: rollback fail, T_T");
                        Platform.exit(); // crash!!!
                    }
                }
            }
        });

    }

    private int getNumFromDatabase(Connection conn, String sql) throws SQLException{
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql);
        rs.next();
        return rs.getInt(1);
    }

    @FXML
    protected void backToFlight(ActionEvent event) {
        try {
            clearText();
            switchAnimation(infoPane, flightPane);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearText() {
        contactNameText.clear();
        contactPhoneText.clear();
        passengerObservableList.clear();
        passengerIndex = 0;
        addPassengerBtn.setDisable(false);
        addPassengerNum = -1;  // 保持下次操作的正确性，把addPassenger置为未初始化状态

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



    @FXML
    protected void infoPaneKeyHandle(KeyEvent event) {
        if(event.getCode() == KeyCode.ENTER) {
            confirm(null);
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            backToFlight(null);
        }
    }



    //-------------------- pay pane implementation------------------
    public static float amount = 0.0f; // 用来显示订单的金额
    public static String orderNo = "11111111"; // 用来显示订单号

    @FXML
    private Label payPaneOrderLabel;
    @FXML
    private Label payPaneAmountLabel;
    @FXML
    private JFXButton payBtn;
    @FXML
    private JFXButton payPaneBackBtn;

    @FXML
    // 按下支付按钮，订单的支付状态改变为支付
    protected void payButtonClicked(ActionEvent event) {
        // 进行数据库操作
        switchAnimation(payPane, spinPane);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Connection conn = null;
                try {
                    conn = DBConnection.getConnection();
                    conn.setAutoCommit(false);
                    String sql = "update T_Bill set IsPay=? where T_Bill.OrderNo=?;";
                    PreparedStatement preparedStatement = conn.prepareStatement(sql);
                    preparedStatement.setInt(1, 1);
                    preparedStatement.setString(2, orderNo);
                    preparedStatement.executeUpdate();
                    conn.commit();
                    showDialog("支付成功！");
                } catch (Exception e) {
                    if (conn != null) {
                        try {
                            conn.rollback();
                            showDialog("支付失败！");
                        } catch (SQLException c) {
                            System.out.println("T_Bill rollback fail, Alert !");
                            Platform.exit(); // termination
                        }
                    }
                } finally {
                    switchAnimation(spinPane, orderPane);
                }
            }
        });

    }

    @FXML
    protected void fromPayToOrder(ActionEvent event) {
        switchAnimation(payPane, orderPane);
    }

    @FXML
    protected void payPaneKeyHandle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            payButtonClicked(null);
        }
        if (event.getCode() == KeyCode.ESCAPE) {
            switchAnimation(payPane, orderPane);
        }
    }
    //-------------------- error pane implementation------------------
    @FXML
    private Pane errorPane;

    @FXML
    JFXButton errorPaneBackBtn;

    @FXML
    protected void fromErrToOrder(ActionEvent event) {
        switchAnimation(errorPane, orderPane);
    }

    @FXML
    protected void errorPaneKeyHandle(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER || event.getCode() == KeyCode.ESCAPE) {
            switchAnimation(errorPane, orderPane);
        }

    }
}

