package hust.UI;

import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTreeTableColumn;
import com.jfoenix.controls.JFXTreeTableView;
import com.jfoenix.controls.RecursiveTreeItem;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
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
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    public static Scene scene = null;
    public static UserController controller = null;
    public JFXTreeTableView<Order> tableOrder;
    public JFXTextField text_filter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // 创建列来显示订单号
        JFXTreeTableColumn<Order, String> idColumn = new JFXTreeTableColumn<>("订单号");
        idColumn.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> {
            if (idColumn.validateValue(param)) {
                return param.getValue().getValue().orderNo;
            } else {
                return idColumn.getComputedValue(param);
            }
        });
        // 创建列来显示订单号
        JFXTreeTableColumn<Order, String> ctctName = new JFXTreeTableColumn<>("联系人");
        ctctName.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> {
            if (ctctName.validateValue(param)) {
                return param.getValue().getValue().contactName;
            } else {
                return ctctName.getComputedValue(param);
            }
        });
        // 创建列来显示订单号
        JFXTreeTableColumn<Order, String> ctPhone = new JFXTreeTableColumn<>("电话号码");
        ctPhone.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> {
            if (ctPhone.validateValue(param)) {
                return param.getValue().getValue().contactPhone;
            } else {
                return ctPhone.getComputedValue(param);
            }
        });
        // 创建列来显示订单号
        JFXTreeTableColumn<Order, String> takeoffDt = new JFXTreeTableColumn<>("行程日期");
        takeoffDt.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> {
            if (takeoffDt.validateValue(param)) {
                return param.getValue().getValue().takeoffDate;
            } else {
                return takeoffDt.getComputedValue(param);
            }
        });
        // 创建列来显示订单号
        JFXTreeTableColumn<Order, Number> ttNum = new JFXTreeTableColumn<>("机票数");
        ttNum.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, Number> param) -> {
            if (ttNum.validateValue(param)) {
                return param.getValue().getValue().ticketNum;
            } else {
                return ttNum.getComputedValue(param);
            }
        });
        // 创建列来显示订单号
        JFXTreeTableColumn<Order, Number> amt = new JFXTreeTableColumn<>("订单金额");
        amt.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, Number> param) -> {
            if (amt.validateValue(param)) {
                return param.getValue().getValue().amount;
            } else {
                return amt.getComputedValue(param);
            }
        });
        // 创建列来显示订单号
        JFXTreeTableColumn<Order, String> ispayy = new JFXTreeTableColumn<>("是否付款");
        ispayy.setCellValueFactory((TreeTableColumn.CellDataFeatures<Order, String> param) -> {
            if (ispayy.validateValue(param)) {
                return param.getValue().getValue().isPay;
            } else {
                return ispayy.getComputedValue(param);
            }
        });
        // 创建列来显示订单号
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

        updateOrderList(Main.id); // 更新订单信息

        // 搜索功能
        text_filter.textProperty().addListener((o, oldVal, newVal) -> {
            tableOrder.setPredicate(patientProp -> {
                final Order order = patientProp.getValue();
                return order.getOrderNo().contains(newVal)
                        || order.getContactName().contains(newVal)
                        || order.getContactPhone().contains(newVal)
                        || order.getTakeoffDate().contains(newVal)
                        || order.getOrderTime().contains(newVal);
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
        // 修改数据库中T_Bill的数据
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "update T_Bill set IsPay=1 where OrderNo=?";
            conn.setAutoCommit(false);
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, order.getOrderNo());
            if (stmt.executeUpdate() > 0) {
                ReservationController.showDialog("支付成功");
                updateOrderList(Main.id);
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
            conn = DBConnection.getConnection();
            // 删除机票
            String sqlTicket = "delete from Ticket where TNo in " +
                    "(select TNo from T_Order_Ticket where OrderNo=?)";
            PreparedStatement tickStmt = conn.prepareStatement(sqlTicket);
            tickStmt.setString(1, order.getOrderNo());
            int num = 0;
            if ((num = tickStmt.executeUpdate()) > 0) {
                System.out.println("删除的票数为：" + num);
            }
            // 删除订单
            String sql = "delete from T_Order where OrderNo=?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            conn.setAutoCommit(false);
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



}
