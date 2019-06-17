package hust.UI;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import hust.DB.DBConnection;
import hust.Main;
import hust.bean.Flight;
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
import java.util.ResourceBundle;

public class ManagerController implements Initializable {

    public static Scene scene = null;
    public static ManagerController controller = null;
    public JFXTreeTableView<FlightTotal> table;
    public JFXTextField text_filter;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
// 第二个列表
        JFXTreeTableColumn<FlightTotal, String> ticketNo = new JFXTreeTableColumn<>("航班编号");
        ticketNo.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightTotal, String> param) -> {
            if (ticketNo.validateValue(param)) {
                return param.getValue().getValue().flightNo;
            } else {
                return ticketNo.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<FlightTotal, String> passType = new JFXTreeTableColumn<>("日期");
        passType.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightTotal, String> param) -> {
            if (passType.validateValue(param)) {
                return param.getValue().getValue().date;
            } else {
                return passType.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<FlightTotal, Number> tkofTime = new JFXTreeTableColumn<>("收入总量（元）");
        tkofTime.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightTotal, Number> param) -> {
            if (tkofTime.validateValue(param)) {
                return param.getValue().getValue().amount;
            } else {
                return tkofTime.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<FlightTotal, Number> tkofCity = new JFXTreeTableColumn<>("售出票数（张）");
        tkofCity.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightTotal, Number> param) -> {
            if (tkofCity.validateValue(param)) {
                return param.getValue().getValue().sellNum;
            } else {
                return tkofCity.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<FlightTotal, Number> ladCity = new JFXTreeTableColumn<>("总票数（张）");
        ladCity.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightTotal, Number> param) -> {
            if (ladCity.validateValue(param)) {
                return param.getValue().getValue().seatNum;
            } else {
                return ladCity.getComputedValue(param);
            }
        });
        JFXTreeTableColumn<FlightTotal, String> stp = new JFXTreeTableColumn<>("上座率");
        stp.setCellValueFactory((TreeTableColumn.CellDataFeatures<FlightTotal, String> param) -> {
            if (stp.validateValue(param)) {
                return param.getValue().getValue().sellPercent;
            } else {
                return stp.getComputedValue(param);
            }
        });
        table.setEditable(false);
        table.setShowRoot(false);
        table.getColumns().setAll(ticketNo, passType, tkofTime, tkofCity, ladCity, stp);
        table.setColumnResizePolicy(JFXTreeTableView.CONSTRAINED_RESIZE_POLICY);

        updateTable();

        // 搜索功能
        text_filter.textProperty().addListener((o, oldVal, newVal) -> {
            table.setPredicate(orderProp -> {
                final FlightTotal flightTotal = orderProp.getValue();
                return flightTotal.getFlightNo().contains(newVal)
                        || flightTotal.getDate().contains(newVal);
            });
        });


    }

    public void updateTable() {
        ObservableList<FlightTotal> flights = FXCollections.observableArrayList();
        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            String sql = "select * from v_total;";
            Statement pStmt = conn.createStatement();
            ResultSet rs = pStmt.executeQuery(sql);
            while(rs.next()) {
                flights.add(new FlightTotal(rs.getString(1), rs.getString(2),
                        rs.getInt(3), rs.getInt(4), rs.getInt(5), rs.getFloat(6)));
            }
        } catch (SQLException e) {
            ReservationController.showDialog("更新列表失败");
        }
        TreeItem<FlightTotal> flightRoot = new RecursiveTreeItem<>(flights, RecursiveTreeObject::getChildren);
        table.setRoot(flightRoot);



    }

    //-----------------------air craft ----------------------------------
    public JFXTextField planeNoLabel;
    public JFXTextField fcSeatLabel;
    public JFXTextField bcSeatLabel;
    public JFXTextField ecSeatLabel;

    @FXML
    protected void addPlane(ActionEvent event) {
        if (planeNoLabel.getText().isEmpty() || fcSeatLabel.getText().isEmpty()
                || bcSeatLabel.getText().isEmpty() || ecSeatLabel.getText().isEmpty()) {
            ReservationController.showDialog("请输入正确的信息");
            return;
        }
        String planeNo = planeNoLabel.getText();
        Connection conn = null;
        try {
            int fcSeat = Integer.valueOf(fcSeatLabel.getText());
            int bcSeat = Integer.valueOf(bcSeatLabel.getText());
            int ecSeat = Integer.valueOf(ecSeatLabel.getText());
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into AirCraft values (?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, planeNo);
            stmt.setInt(2, fcSeat);
            stmt.setInt(3, bcSeat);
            stmt.setInt(4, ecSeat);
            if (stmt.executeUpdate() > 0) {
                ReservationController.showDialog("添加飞机成功");
                clearPlane(null);
            } else {
                ReservationController.showDialog("添加飞机失败");
                return;
            }
            conn.commit(); // 提交ß
        }catch (Exception e) {
            System.out.println(e.getMessage());
            ReservationController.showDialog("添加失败");
        }

    }
    @FXML
    protected void clearPlane(ActionEvent event) {
        planeNoLabel.clear();
        fcSeatLabel.clear();
        bcSeatLabel.clear();
        ecSeatLabel.clear();
    }

    //-----------------------air craft ----------------------------------
    public JFXTextField flightNoLabel;
    public JFXTextField planeNoLabel2;
    public JFXTextField takeoffCityLabel;
    public JFXTextField landingCityLabel;
    public JFXTimePicker takeoffTime;
    public JFXTimePicker landingTime;


    @FXML
    protected void addFlight(ActionEvent event) {
        if (flightNoLabel.getText().isEmpty() || planeNoLabel2.getText().isEmpty() ||
            takeoffCityLabel.getText().isEmpty() || landingCityLabel.getText().isEmpty() ||
            takeoffTime.getEditor().getText().isEmpty() || landingTime.getEditor().getText().isEmpty()) {
            ReservationController.showDialog("请输入正确的信息");
            return;
        }
        try {
            String flightNo = flightNoLabel.getText();
            String airCraftNo = planeNoLabel2.getText();
            String takeoffCity = takeoffCityLabel.getText();
            String landingCity = landingCityLabel.getText();
            Time tfTime = Time.valueOf(takeoffTime.getValue());
            Time ldTime = Time.valueOf(landingTime.getValue());
            Time duration = getDiff(ldTime, tfTime);

            Connection conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into Flight values(?,?,?,?,?,?,?)";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, flightNo);
            stmt.setString(2, airCraftNo);
            stmt.setString(3, takeoffCity);
            stmt.setString(4, landingCity);
            stmt.setTime(5, tfTime);
            stmt.setTime(6, ldTime);
            stmt.setTime(7, duration);
            if (stmt.executeUpdate() > 0) {
                ReservationController.showDialog("添加航班成功");
                clearFlight(null);
            } else {
                ReservationController.showDialog("添加航班失败");
                return;
            }
            conn.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ReservationController.showDialog("添加航班信息失败");
        }
    }

    @FXML
    protected void clearFlight(ActionEvent event) {
        flightNoLabel.clear();
        planeNoLabel2.clear();
        takeoffCityLabel.clear();
        landingCityLabel.clear();
        takeoffTime.getEditor().clear();
        landingTime.getEditor().clear();
    }



    public static Time getDiff(Time t1, Time t2) {
        if (t1.getHours() <= t2.getHours()) {
            t1.setHours(t1.getHours() + 24);
        }
        int h = t1.getHours() - t2.getHours();
        int m = t1.getMinutes() - t2.getMinutes();
        if (m < 0) {
            h--;
            m += 60;
        }
        return new Time(h, m, 0);
    }

    //-----------------------seats ----------------------------------
    public JFXTextField flightNoLabel2;
    public JFXDatePicker seatDate;
    public JFXTextField fcRemainLabel;
    public JFXTextField bcRemainLabel;
    public JFXTextField ecRemainLabel;
    public JFXTextField fcPriceLabel;
    public JFXTextField bcPriceLabel;
    public JFXTextField ecPriceLabel;

    @FXML
    protected void addSeat(ActionEvent event) {
        if (flightNoLabel2.getText().isEmpty() || fcRemainLabel.getText().isEmpty() ||
                bcRemainLabel.getText().isEmpty() || ecRemainLabel.getText().isEmpty() ||
                fcPriceLabel.getText().isEmpty() || bcPriceLabel.getText().isEmpty() ||
                ecPriceLabel.getText().isEmpty() || seatDate.getEditor().getText().isEmpty()) {
            ReservationController.showDialog("请输入正确的信息");
            return;
        }
        try {
            Connection conn = DBConnection.getConnection();
            conn.setAutoCommit(false);
            String sql = "insert into Seats (FNo, Date, FCRemain, BCRemain, ECRemain, FCPrice, BCPrice, ECPrice)values(?,?,?,?,?,?,?,?);";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, flightNoLabel2.getText());
            stmt.setDate(2,Date.valueOf(seatDate.getValue()));
            stmt.setInt(3, Integer.valueOf(fcRemainLabel.getText()));
            stmt.setInt(4, Integer.valueOf(bcRemainLabel.getText()));
            stmt.setInt(5, Integer.valueOf(ecRemainLabel.getText()));
            stmt.setFloat(6, Integer.valueOf(fcPriceLabel.getText()));
            stmt.setFloat(7, Integer.valueOf(bcPriceLabel.getText()));
            stmt.setFloat(8, Integer.valueOf(ecPriceLabel.getText()));
            if (stmt.executeUpdate() > 0) {
                ReservationController.showDialog("添加座位信息成功");
                clearSeat(null);
            } else {
                ReservationController.showDialog("添加座位信息失败");
                return;
            }
            conn.commit();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            ReservationController.showDialog("添加座位信息失败");
        }
    }
    @FXML
    protected void clearSeat(ActionEvent event) {
        flightNoLabel2.clear();
        seatDate.getEditor().clear();
        fcRemainLabel.clear();
        bcRemainLabel.clear();
        ecRemainLabel.clear();
        fcPriceLabel.clear();
        bcPriceLabel.clear();
        ecPriceLabel.clear();
    }


    //-----------------------table  ----------------------------------
    @FXML
    protected void refresh(ActionEvent event) {
        updateTable();
    }

    @FXML
    protected void back(ActionEvent event) {
        Main.scene = ReservationController.reservationScene;
        Main.currentStage.setScene(Main.scene);
//        Main.setWindowToCenter();
        Main.currentStage.show();
    }


    public static class FlightTotal extends RecursiveTreeObject<FlightTotal> {
        private final SimpleStringProperty flightNo;
        private final SimpleStringProperty date;
        private final SimpleIntegerProperty amount;
        private final SimpleIntegerProperty sellNum;
        private final SimpleIntegerProperty seatNum;
        private final SimpleStringProperty sellPercent;

        FlightTotal(String flightNo, String date, int amount, int sellNum, int seatNum, float percent) {
            this.flightNo = new SimpleStringProperty(flightNo);
            this.date = new SimpleStringProperty(date);
            this.amount = new SimpleIntegerProperty(amount);
            this.sellNum = new SimpleIntegerProperty(sellNum);
            this.seatNum = new SimpleIntegerProperty(seatNum);
            this.sellPercent = new SimpleStringProperty(String.format("%.2f", percent * 100) + "%");
        }

        public String getFlightNo() {
            return flightNo.get();
        }

        public SimpleStringProperty flightNoProperty() {
            return flightNo;
        }

        public void setFlightNo(String flightNo) {
            this.flightNo.set(flightNo);
        }

        public String getDate() {
            return date.get();
        }

        public SimpleStringProperty dateProperty() {
            return date;
        }

        public void setDate(String date) {
            this.date.set(date);
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

        public int getSellNum() {
            return sellNum.get();
        }

        public SimpleIntegerProperty sellNumProperty() {
            return sellNum;
        }

        public void setSellNum(int sellNum) {
            this.sellNum.set(sellNum);
        }

        public int getSeatNum() {
            return seatNum.get();
        }

        public SimpleIntegerProperty seatNumProperty() {
            return seatNum;
        }

        public void setSeatNum(int seatNum) {
            this.seatNum.set(seatNum);
        }

        public String getSellPercent() {
            return sellPercent.get();
        }

        public SimpleStringProperty sellPercentProperty() {
            return sellPercent;
        }

        public void setSellPercent(String sellPercent) {
            this.sellPercent.set(sellPercent);
        }
    }
}
