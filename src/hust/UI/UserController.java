package hust.UI;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import com.sun.tools.corba.se.idl.constExpr.Not;
import com.sun.tools.corba.se.idl.constExpr.Or;
import hust.DB.DBConnection;
import hust.Main;
import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableColumn;

import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    public static Scene scene = null;
    public static UserController controller = null;
    public JFXTreeTableView<Order> tableOrder;
    public JFXTextField text_filter;
    public JFXTreeTableView<Notify> tableNotify;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        JFXTreeTableColumn<Order, String> idColumn = new JFXTreeTableColumn<>("订单号");
        idColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> {
            if (idColumn.validateValue(param)) {
                return param.getValue().getValue().orderNo;
            } else {
                return idColumn.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Order, String> ctctName = new JFXTreeTableColumn<>("联系人");
        ctctName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> {
            if (ctctName.validateValue(param)) {
                return param.getValue().getValue().contactName;
            } else {
                return ctctName.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Order, String> ctPhone = new JFXTreeTableColumn<>("电话号码");
        ctPhone.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> {
            if (ctPhone.validateValue(param)) {
                return param.getValue().getValue().contactPhone;
            } else {
                return ctPhone.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Order, String> takeoffDt = new JFXTreeTableColumn<>("行程日期");
        takeoffDt.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> {
            if (takeoffDt.validateValue(param)) {
                return param.getValue().getValue().takeoffDate;
            } else {
                return takeoffDt.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Order, Number> ttNum = new JFXTreeTableColumn<>("机票数");
        ttNum.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, Number> param) -> {
            if (ttNum.validateValue(param)) {
                return param.getValue().getValue().ticketNum;
            } else {
                return ttNum.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Order, Number> amt = new JFXTreeTableColumn<>("订单金额");
        amt.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, Number> param) -> {
            if (amt.validateValue(param)) {
                return param.getValue().getValue().amount;
            } else {
                return amt.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Order, String> ispayy = new JFXTreeTableColumn<>("是否付款");
        ispayy.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> {
            if (ispayy.validateValue(param)) {
                return param.getValue().getValue().isPay;
            } else {
                return ispayy.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Order, String> dt = new JFXTreeTableColumn<>("订单时间");
        dt.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> {
            if (dt.validateValue(param)) {
                return param.getValue().getValue().orderTime;
            } else {
                return dt.getComputedValue(param);
            }
        });

        tableOrder.setEditable(false);
        tableOrder.setShowRoot(false);
        tableOrder.getColumns().setAll(idColumn, ctctName, ctPhone, takeoffDt, ttNum, ispayy, amt, dt);
        tableOrder.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);


        // 第二个列表
        JFXTreeTableColumn<Notify, String> ticketNo = new JFXTreeTableColumn<>("机票编号");
        ticketNo.setCellValueFactory((TreeTableColumn.CellDataFeatures<Notify, String> param) -> {
            if (ticketNo.validateValue(param)) {
                return param.getValue().getValue().ticketNo;
            } else {
                return ticketNo.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Notify, String> passType = new JFXTreeTableColumn<>("乘客姓名");
        passType.setCellValueFactory((TreeTableColumn.CellDataFeatures<Notify, String> param) -> {
            if (passType.validateValue(param)) {
                return param.getValue().getValue().passengerName;
            } else {
                return passType.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Notify, String> tkofTime = new JFXTreeTableColumn<>("起飞日期");
        tkofTime.setCellValueFactory((TreeTableColumn.CellDataFeatures<Notify, String> param) -> {
            if (tkofTime.validateValue(param)) {
                return param.getValue().getValue().takeoffDate;
            } else {
                return tkofTime.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Notify, String> tkofCity = new JFXTreeTableColumn<>("起飞城市");
        tkofCity.setCellValueFactory((TreeTableColumn.CellDataFeatures<Notify, String> param) -> {
            if (tkofCity.validateValue(param)) {
                return param.getValue().getValue().takeoffCity;
            } else {
                return tkofCity.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Notify, String> ladCity = new JFXTreeTableColumn<>("降落城市");
        ladCity.setCellValueFactory((TreeTableColumn.CellDataFeatures<Notify, String> param) -> {
            if (ladCity.validateValue(param)) {
                return param.getValue().getValue().landingCity;
            } else {
                return ladCity.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<Notify, String> stp = new JFXTreeTableColumn<>("座位类型");
        stp.setCellValueFactory((TreeTableColumn.CellDataFeatures<Notify, String> param) -> {
            if (stp.validateValue(param)) {
                return param.getValue().getValue().seatClass;
            } else {
                return stp.getComputedValue(param);
            }
        });
        tableNotify.setEditable(false);
        tableNotify.setShowRoot(false);
        tableNotify.getColumns().setAll(ticketNo, passType, tkofTime, tkofCity, ladCity, stp);
        tableNotify.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);
        updateOrderList(Main.id); // 更新订单信息
        updateNotifyList(); // 更新通知

        // 搜索功能
        text_filter.textProperty().addListener((o, oldVal, newVal) -> {
            tableOrder.setPredicate(orderProp -> {
                final Order order = orderProp.getValue();
                return order.getOrderNo().contains(newVal)
                        || order.getContactName().contains(newVal)
                        || order.getContactPhone().contains(newVal)
                        || order.getTakeoffDate().contains(newVal)
                        || order.getOrderTime().contains(newVal);
            });
            tableNotify.setPredicate(notifyProp -> {
                final Notify notify = notifyProp.getValue();
                return notify.getTicketNo().contains(newVal)
                        || notify.getPassengerName().contains(newVal)
                        || notify.getTakeoffCity().contains(newVal)
                        || notify.getLandingCity().contains(newVal)
                        || notify.getSeatClass().contains(newVal)
                        || notify.getTakeoffDate().contains(newVal);
            });
        });
    }

    @FXML
    protected void pay(ActionEvent event) {
        Order order;
        if (!tableOrder.getSelectionModel().isEmpty())
            order = tableOrder.getSelectionModel().getSelectedItem().getValue();
        else {
            ReservationController.showDialog("请选择一个订单");
            return;
        }
        if (order.getIsPay().equals("是")) {
            ReservationController.showDialog("订单已经付款");
            return;
        }

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            Date date = getDBDate();
            if (Date.valueOf(order.getTakeoffDate()).compareTo(date) < 0) {
                ReservationController.showDialog("超过了支付的时间");
                return;
            }
            // 修改数据库中T_Bill的数据
            String sql = "update T_Bill set IsPay=1 where OrderNo=?";
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, order.getOrderNo());
            if (stmt.executeUpdate() > 0) {
                ReservationController.showDialog("支付成功");
                updateOrderList(Main.id);
                updateNotifyList(); // 同时更新通知列表
            } else {
                ReservationController.showDialog("支付失败");
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                ReservationController.showDialog("支付失败");
                conn.rollback();
            } catch (SQLException c) {
                System.out.println("debug: pay button clciked rollback fail");
            }
            e.printStackTrace();
        }
    }

    public static Date getDBDate() throws SQLException{
        Connection conn = DBConnection.getConnection();
        String sqlDate = "select Date(now());";
        Statement dateStmt = conn.createStatement();
        ResultSet dateRs = dateStmt.executeQuery(sqlDate);
        Date date = null;
        if (dateRs.next()) {
            return dateRs.getDate(1);
        } else {
            throw new SQLException("Query date failed!");
        }
    }

    @FXML
    protected void refund(ActionEvent event) {
        Order order;
        if (!tableOrder.getSelectionModel().isEmpty())
            order = tableOrder.getSelectionModel().getSelectedItem().getValue();
        else {
            ReservationController.showDialog("请选择一个订单");
            return;
        }
        // 删除数据库中ticket,T_Order的数据，并级联删除T_Order_Ticket, T_User_Order中的记录
        Connection conn = null;
        try {
            Date date = getDBDate();
            if (Date.valueOf(order.getTakeoffDate()).compareTo(date)  < 0) { // 航班起飞之后的不能退票
                ReservationController.showDialog("超过了退票的时间");
                return;
            }
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            // 得到航班编号、座位类型
            int seatClass = 0;
            String Fno = null;
            String querySeatClass = "select Ticket.FNo ,Ticket.ClassType from Ticket where TNo in " +
                    "(select TNo from T_Order_Ticket where OrderNo=?)";
            PreparedStatement seatClassStmt = conn.prepareStatement(querySeatClass);
            seatClassStmt.setString(1, order.getOrderNo());
            ResultSet scRs = seatClassStmt.executeQuery();
            if (scRs.next()) {
                Fno = scRs.getString(1); // 得到机票的航班
                seatClass = scRs.getInt(2); // 得到机票的座位类型
            }
            // 删除机票
            String sqlTicket = "delete from Ticket where TNo in " +
                    "(select TNo from T_Order_Ticket where OrderNo=?)";
            PreparedStatement tickStmt = conn.prepareStatement(sqlTicket);
            tickStmt.setString(1, order.getOrderNo());
            int num = 0;
            if ((num = tickStmt.executeUpdate()) > 0) {
                System.out.println("删除的票数为：" + num);
            }

            // 更新座位信息
            String seatClassStr = seatClass==1?"FCRemain":seatClass==2?"BCRemain":"ECRemain";
            String updateSeat = "update Seats set " + seatClassStr +
                                    "=Seats." + seatClassStr + "+? where Seats.FNo=? and Seats.Date=?;";
            PreparedStatement upStmt = conn.prepareStatement(updateSeat);
            upStmt.setInt(1, num);
            upStmt.setString(2, Fno);
            upStmt.setDate(3, Date.valueOf(order.getTakeoffDate()));
            upStmt.executeUpdate();
            // 删除订单
            String sql = "delete from T_Order where OrderNo=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, order.getOrderNo());
            if (stmt.executeUpdate() > 0) {
                ReservationController.showDialog("退票成功");
            } else {
                ReservationController.showDialog("退票失败");
            }
            conn.commit();
            updateOrderList(Main.id);
        } catch (SQLException e) {
            try {
                ReservationController.showDialog("退票失败");
                conn.rollback();
            } catch (SQLException c) {
                System.out.println("debug: pay button clicked rollback fail");
                Platform.exit();
            }
            e.printStackTrace();
        }
    }

    @FXML
    protected void refresh(ActionEvent event) {
        updateOrderList(Main.id);
        updateNotifyList(); // 更新通知
    }

    @FXML
    protected void back(ActionEvent event) {
        Main.scene = ReservationController.reservationScene;
        Main.currentStage.setScene(Main.scene);
//        Main.setWindowToCenter();
        Main.currentStage.show();
    }

    public void updateOrderList(String userNo) {
        ObservableList<Order> orders = FXCollections.observableArrayList();
        Connection conn = null;
        try {
            String sql = "select * from v_order where v_order.OrderNo " +
                    "in (select OrderNo from T_User_Order where T_User_Order.Username=?);";
            conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, userNo);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()) {
                SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd\n  HH:mm:ss");
                orders.add(new Order(rs.getString(1), rs.getString(2),
                        rs.getString(3), rs.getDate(4).toString(), rs.getInt(5),
                        (int)rs.getFloat(6), rs.getShort(7)==1?"是":"否",
                        format.format(rs.getTimestamp(8))));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        TreeItem<Order> orderRoot = new RecursiveTreeItem<>(orders, RecursiveTreeObject::getChildren);
        tableOrder.setRoot(orderRoot);
    }

    public void updateNotifyList() {
        ObservableList<Notify> notifies = FXCollections.observableArrayList();
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "select * from v_nofity where v_nofity.OrderNo in " +
                    "(select OrderNo from T_User_Order where T_User_Order.Username=?)";
            PreparedStatement pStmt = conn.prepareStatement(sql);
            pStmt.setString(1, Main.id);
            ResultSet rs = pStmt.executeQuery();
            while(rs.next()) {
                short seat = rs.getShort(7);
                String seatC = seat==1?"头等舱":seat==2?"商务舱":"经济舱";
                notifies.add(new Notify(rs.getString(2), rs.getString(3), rs.getDate(4).toString(), rs.getString(5),
                        rs.getString(6), seatC));
            }
        } catch (SQLException e) {
            ReservationController.showDialog("更新列表失败");
        }
        TreeItem<Notify> notifyRoot = new RecursiveTreeItem<>(notifies, RecursiveTreeObject::getChildren);
        tableNotify.setRoot(notifyRoot);
    }

    public static class Order extends RecursiveTreeObject<Order> {
        private final SimpleStringProperty orderNo;
        private final SimpleStringProperty contactName;
        private final SimpleStringProperty contactPhone;
        private final SimpleStringProperty takeoffDate;
        private final SimpleIntegerProperty ticketNum;
        private final SimpleIntegerProperty amount;
        private final SimpleStringProperty isPay;
        private final SimpleStringProperty orderTime;

        Order(String orderNo, String contactName, String contactPhone, String takeoffDate, int ticketNum,
                int amount, String isPay, String orderTime) {
            this.orderNo = new SimpleStringProperty(orderNo);
            this.contactName = new SimpleStringProperty(contactName);
            this.contactPhone = new SimpleStringProperty(contactPhone);
            this.takeoffDate = new SimpleStringProperty(takeoffDate);
            this.ticketNum = new SimpleIntegerProperty(ticketNum);
            this.isPay = new SimpleStringProperty(isPay);
            this.amount = new SimpleIntegerProperty(amount);
            this.orderTime = new SimpleStringProperty(orderTime);
        }

        public String getOrderNo() {
            return orderNo.get();
        }

        public SimpleStringProperty orderNoProperty() {
            return orderNo;
        }

        public void setOrderNo(String orderNo) {
            this.orderNo.set(orderNo);
        }

        public String getContactName() {
            return contactName.get();
        }

        public SimpleStringProperty contactNameProperty() {
            return contactName;
        }

        public void setContactName(String contactName) {
            this.contactName.set(contactName);
        }

        public String getContactPhone() {
            return contactPhone.get();
        }

        public SimpleStringProperty contactPhoneProperty() {
            return contactPhone;
        }

        public void setContactPhone(String contactPhone) {
            this.contactPhone.set(contactPhone);
        }

        public String getTakeoffDate() {
            return takeoffDate.get();
        }

        public SimpleStringProperty takeoffDateProperty() {
            return takeoffDate;
        }

        public void setTakeoffDate(String takeoffDate) {
            this.takeoffDate.set(takeoffDate);
        }

        public int getTicketNum() {
            return ticketNum.get();
        }

        public SimpleIntegerProperty ticketNumProperty() {
            return ticketNum;
        }

        public void setTicketNum(int ticketNum) {
            this.ticketNum.set(ticketNum);
        }

        public String getIsPay() {
            return isPay.get();
        }

        public SimpleStringProperty isPayProperty() {
            return isPay;
        }

        public void setIsPay(String isPay) {
            this.isPay.set(isPay);
        }

        public int getAmount() {
            return amount.get();
        }

        public SimpleIntegerProperty amountProperty() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount.set(amount);
        }

        public String getOrderTime() {
            return orderTime.get();
        }

        public SimpleStringProperty orderTimeProperty() {
            return orderTime;
        }

        public void setOrderTime(String orderTime) {
            this.orderTime.set(orderTime);
        }
    }

    public static class Notify extends RecursiveTreeObject<Notify> {
        private final SimpleStringProperty ticketNo;
        private final SimpleStringProperty passengerName;
        private final SimpleStringProperty takeoffDate;
        private final SimpleStringProperty takeoffCity;
        private final SimpleStringProperty landingCity;
        private final SimpleStringProperty seatClass;

        public Notify(String ticketNo, String passengerName, String takeoffDate,
                      String takeoffCity, String landingCity, String seatClass) {
            this.ticketNo = new SimpleStringProperty(ticketNo);
            this.passengerName = new SimpleStringProperty(passengerName);
            this.takeoffDate = new SimpleStringProperty(takeoffDate);
            this.takeoffCity = new SimpleStringProperty(takeoffCity);
            this.landingCity = new SimpleStringProperty(landingCity);
            this.seatClass = new SimpleStringProperty(seatClass);
        }

        public String getTicketNo() {
            return ticketNo.get();
        }

        public SimpleStringProperty ticketNoProperty() {
            return ticketNo;
        }

        public void setTicketNo(String ticketNo) {
            this.ticketNo.set(ticketNo);
        }

        public String getPassengerName() {
            return passengerName.get();
        }

        public SimpleStringProperty passengerNameProperty() {
            return passengerName;
        }

        public void setPassengerName(String passengerName) {
            this.passengerName.set(passengerName);
        }

        public String getTakeoffDate() {
            return takeoffDate.get();
        }

        public SimpleStringProperty takeoffDateProperty() {
            return takeoffDate;
        }

        public void setTakeoffDate(String takeoffDate) {
            this.takeoffDate.set(takeoffDate);
        }

        public String getTakeoffCity() {
            return takeoffCity.get();
        }

        public SimpleStringProperty takeoffCityProperty() {
            return takeoffCity;
        }

        public void setTakeoffCity(String takeoffCity) {
            this.takeoffCity.set(takeoffCity);
        }

        public String getLandingCity() {
            return landingCity.get();
        }

        public SimpleStringProperty landingCityProperty() {
            return landingCity;
        }

        public void setLandingCity(String landingCity) {
            this.landingCity.set(landingCity);
        }

        public String getSeatClass() {
            return seatClass.get();
        }

        public SimpleStringProperty seatClassProperty() {
            return seatClass;
        }

        public void setSeatClass(String seatClass) {
            this.seatClass.set(seatClass);
        }
    }

}
