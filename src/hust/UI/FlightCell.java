package hust.UI;

import com.jfoenix.controls.JFXButton;
import hust.Main;
import hust.bean.Flight;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import javax.sound.sampled.Line;
import java.sql.Time;
import java.util.ServiceConfigurationError;


public class FlightCell extends ListCell<Flight> {

    HBox mainBox;

    ImageView flightImage;
    Label flightLabel;
    HBox flightBox;

    VBox takeoffBox;
    Label takeoffLabel;
    Label takeoffCityLabel;

    VBox arrowBox;
    ImageView arrowImage;
    HBox durationBox;
    Label durationLabel;
    ImageView clockImage;

    VBox landingBox;
    Label landingCityLabel;
    Label landingLabel;

    JFXButton firstOrderBtn;
    VBox firstBox;
    Label firstLabel;
    Label firstPriceLabel;
    Label firstRemainLabel;

    JFXButton businessOrderBtn;
    VBox businessBox;
    Label businessLabel;
    Label businessPriceLabel;
    Label businessRemainLabel;

    JFXButton economyOrderBtn;
    VBox economyBox;
    Label economyLabel;
    Label economyPriceLabel;
    Label economyRemainLabel;

    Time l_duration;
    Time l_takeoff;
    Time l_landing;

    public FlightCell() {
        mainBox = new HBox();
        mainBox.setMinHeight(70);
        mainBox.setMinWidth(430);
        mainBox.setPrefWidth(435);
        flightImage = new ImageView(new Image(this.getClass().
                getResourceAsStream("/res/images/flight_icon.png")));
        flightImage.setFitHeight(20.0);
        flightImage.setFitWidth(20.0);
        flightLabel = new Label();
        flightLabel.setStyle("-fx-font-size: 14");
        flightLabel.setTextFill(Color.web("#000000"));
        flightBox = new HBox();
        flightBox.setMinWidth(80);
        flightBox.setAlignment(Pos.CENTER);
        flightBox.getChildren().addAll(flightImage, flightLabel);

        // 起飞信息
        takeoffBox = new VBox();
        takeoffBox.setAlignment(Pos.CENTER);
        takeoffBox.setMinWidth(70);
        takeoffLabel = new Label();
        takeoffLabel.setStyle("-fx-font-size: 16");
        takeoffLabel.setTextFill(Color.web("#000000"));
        takeoffCityLabel = new Label();
        takeoffCityLabel.setStyle("-fx-font-size:11");
        takeoffCityLabel.setTextFill(Color.web("#9c9c9c"));
        takeoffBox.getChildren().addAll(takeoffLabel, takeoffCityLabel);

        // 中间的箭头
        arrowBox = new VBox();
        arrowBox.setAlignment(Pos.CENTER);
        arrowBox.setMinWidth(80);
        arrowImage = new ImageView(new Image(this.getClass().
                getResourceAsStream("/res/images/arrow_from_to.png")));
        arrowImage.setFitWidth(120);
        arrowImage.setFitHeight(10);

        // 下面的航班时长
        durationBox = new HBox();
        durationBox.setAlignment(Pos.CENTER);
        durationBox.setMinWidth(50);
        clockImage = new ImageView(new Image(this.getClass().
                getResourceAsStream("/res/images/clock.png")));
        clockImage.setFitHeight(15);
        clockImage.setFitWidth(15);
        durationLabel = new Label();
        durationLabel.setTextFill(Color.web("#9c9c9c"));
        durationBox.getChildren().addAll(clockImage, durationLabel);
        arrowBox.getChildren().addAll(arrowImage, durationBox);

        // 降落信息
        landingBox = new VBox();
        landingBox.setMinWidth(70);

        landingBox.setAlignment(Pos.CENTER);
        landingLabel = new Label();
        landingLabel.setStyle("-fx-font-size: 16");
        landingLabel.setTextFill(Color.web("#000000"));
        landingCityLabel = new Label();
        landingCityLabel.setStyle("-fx-font-size: 11");
        landingCityLabel.setTextFill(Color.web("#9c9c9c"));
        landingBox.getChildren().addAll(landingLabel, landingCityLabel);

        // 头等舱预订
        firstOrderBtn = new JFXButton();
        // 商务预订
        businessOrderBtn = new JFXButton();
        // 经济舱预订
        economyOrderBtn = new JFXButton();

        HBox.setMargin(flightBox,new Insets(0, 2, 0, 10));
        HBox.setMargin(landingBox, new Insets(0, 15, 0, 0));
        HBox.setMargin(firstOrderBtn, new Insets(0,10,0,0));
        HBox.setMargin(businessOrderBtn, new Insets(0,10,0,0));
        mainBox.getChildren().addAll(flightBox, takeoffBox, arrowBox, landingBox,
                firstOrderBtn, businessOrderBtn, economyOrderBtn);

        firstOrderBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleOrderBtn(SeatClass.FIRST);
            }
        });
        businessOrderBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleOrderBtn(SeatClass.BUSINESS);
            }
        });
        economyOrderBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                handleOrderBtn(SeatClass.ECONOMY);
            }
        });
    }
    @Override
    protected void updateItem(Flight item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty && item != null) {
            try {
                flightLabel.setText(item.getFlightNo());

                takeoffLabel.setText(String.format("%02d:%02d", item.getTakeoffTime().getHours(),
                        item.getTakeoffTime().getMinutes()));
                l_takeoff = item.getTakeoffTime();
                takeoffCityLabel.setText(item.getTakeoffCity());

                durationLabel.setText(String.format("%dh%dm",
                        item.getDuration().getHours(), item.getDuration().getMinutes()));
                l_duration = item.getDuration();
                landingLabel.setText(String.format("%02d:%02d",
                        item.getLandingTime().getHours(), item.getLandingTime().getMinutes()));
                l_landing = item.getLandingTime();
                landingCityLabel.setText(item.getLandingCity());

                initBtn((char)1, firstOrderBtn, item);
                initBtn((char)2, businessOrderBtn, item);
                initBtn((char)3, economyOrderBtn, item);

                this.setGraphic(mainBox);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            this.setGraphic(null);  // 解决了listview中的item无法被删除的问题
        }
    }

    private void initBtn(char t, JFXButton btn, Flight item) {
        if (btn == null)
            return;
        btn.setAlignment(Pos.CENTER);
        btn.setPrefHeight(70);
        btn.setMinWidth(85);
        btn.setText("");
        btn.setStyle("-fx-background-color:#d22d4e");
        btn.setButtonType(JFXButton.ButtonType.RAISED);
        switch (t) {
            case 1:
                firstBox = new VBox();
                firstBox.setAlignment(Pos.CENTER);
                firstLabel = new Label("头等舱");
                firstPriceLabel = new Label(String.format("￥%d",(int)item.getFCPrice()));
                firstRemainLabel = new Label(String.format("剩余：%d", item.getFCRemain()));
                if (item.getFCRemain() == 0)
                    btn.setDisable(true);
                firstLabel.setTextFill(Color.web("#ffff"));
                firstPriceLabel.setTextFill(Color.web("#ffff"));
                firstRemainLabel.setTextFill(Color.web("#ffff"));
                firstBox.getChildren().addAll(firstLabel, firstPriceLabel, firstRemainLabel);
                firstOrderBtn.setGraphic(firstBox);
                break;
            case 2:
                businessBox = new VBox();
                businessBox.setAlignment(Pos.CENTER);
                businessLabel = new Label("商务舱");
                businessPriceLabel = new Label(String.format("￥%d",(int)item.getBCPrice()));
                businessRemainLabel = new Label(String.format("剩余：%d", item.getBCRemain()));
                if (item.getBCRemain() == 0)
                    btn.setDisable(true);
                businessLabel.setTextFill(Color.web("#ffff"));
                businessPriceLabel.setTextFill(Color.web("#ffff"));
                businessRemainLabel.setTextFill(Color.web("#ffff"));
                businessBox.getChildren().addAll(businessLabel, businessPriceLabel, businessRemainLabel);
                businessOrderBtn.setGraphic(businessBox);
                break;
            case 3:
                economyBox = new VBox();
                economyBox.setAlignment(Pos.CENTER);
                economyLabel = new Label("经济舱");
                economyPriceLabel = new Label(String.format("￥%d",(int)item.getECPrice()));
                economyRemainLabel = new Label(String.format("剩余：%d", item.getECRemain()));
                if (item.getECRemain() == 0)
                    btn.setDisable(true);
                economyLabel.setTextFill(Color.web("#ffff"));
                economyPriceLabel.setTextFill(Color.web("#ffff"));
                economyRemainLabel.setTextFill(Color.web("#ffff"));
                economyBox.getChildren().addAll(economyLabel, economyPriceLabel, economyRemainLabel);
                economyOrderBtn.setGraphic(economyBox);
                break;
        }
    }

    // init to debug
    public static SeatClass seatClass = SeatClass.ECONOMY;

    private void handleOrderBtn(SeatClass sc) {
        seatClass = sc;
        // 打开一个新的窗口
        ReservationController.controller.switchAnimation(ReservationController.controller.flightPane,
                    ReservationController.controller.spinPane);
        try {
            if (InfoController.infoScene == null) {
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/layout/InfoLayout.fxml"));
                Pane pane = fxmlLoader.load();
                Scene scene = new Scene(pane, pane.getPrefWidth(), pane.getPrefHeight());
                scene.getStylesheets().add(getClass().getResource("/res/style/list-cell-color.css").toString());
                InfoController.infoScene = scene;
                InfoController.controller =  fxmlLoader.getController();
            }
            InfoController.controller.setFlightLabel(flightLabel.getText());
            InfoController.controller.setTakeoffLabel(takeoffLabel.getText());
            InfoController.controller.setTakeoffCityLabel(takeoffCityLabel.getText());
            InfoController.controller.setLandingLabel(landingLabel.getText());
            InfoController.controller.setLandingCityLabel(landingCityLabel.getText());
            InfoController.controller.setDurationLabel(durationLabel.getText());

            Main.currentStage.setScene(InfoController.infoScene);
            Main.setWindowToCenter();
            Main.currentStage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

enum SeatClass {
    FIRST, BUSINESS, ECONOMY
}