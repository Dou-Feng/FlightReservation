package hust.UI;

import com.jfoenix.controls.JFXCheckBox;
import hust.bean.Passenger;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;

import java.util.ArrayList;

public class PassengerCell extends ListCell<Passenger> {
    VBox mainBox;

    Label passengerIndex;
    Label nameLabel;
    Label typeLabel;
    Label cerTypeLabel;

    HBox nameBox;
    HBox typeBox;
    HBox cerBox;

    TextField nameText;
    JFXCheckBox typeCheckBox;
    MenuButton cerTypeMenuBtn;
    MenuItem item1;
    MenuItem item2;
    MenuItem item3;
    MenuItem item4;
    TextField cerTypeText;

    public static ArrayList<String> hashList = new ArrayList<>();
    static {
        hashList.add("一");hashList.add("二");hashList.add("三");hashList.add("四");hashList.add("五");
        hashList.add("六");hashList.add("七");hashList.add("八");hashList.add("九");

    }
    public PassengerCell() {

        mainBox = new VBox();
        passengerIndex = new Label();
        passengerIndex.setMinHeight(40);
        passengerIndex.setStyle("-fx-font-size: 16");
        passengerIndex.setAlignment(Pos.CENTER);

        passengerIndex.setTextFill(Color.web("#000000"));
        nameLabel = new Label("*名字");
        typeLabel = new Label("类型");
        typeLabel.setPadding(new Insets(0,0,0,5));
        cerTypeLabel = new Label("*证件");
        nameLabel.setStyle("-fx-font-size: 14");
        typeLabel.setStyle("-fx-font-size: 14");
        cerTypeLabel.setStyle("-fx-font-size: 14");
        nameLabel.setTextFill(Color.web("#000000"));
        typeLabel.setTextFill(Color.web("#000000"));
        cerTypeLabel.setTextFill(Color.web("#000000"));
        nameLabel.setAlignment(Pos.CENTER);
        typeLabel.setAlignment(Pos.CENTER);
        cerTypeLabel.setAlignment(Pos.CENTER);
        //
        nameText = new TextField();

        typeCheckBox = new JFXCheckBox();
        typeCheckBox.setSelected(true);
        typeCheckBox.setText("成人(≥12)");
        typeCheckBox.setTextFill(Color.web("#000000"));
        cerTypeMenuBtn = new MenuButton();
        item1 = new MenuItem("身份证");
        item2 = new MenuItem("护照");
        item3 = new MenuItem("户口本");
        item4 = new MenuItem("出身证明");
        cerTypeMenuBtn.getItems().addAll(item1, item2, item3, item4);
        cerTypeMenuBtn.setText("身份证");
        cerTypeMenuBtn.setMinWidth(50);
        cerTypeText = new TextField();
        cerTypeText.setMinWidth(285);
        nameText.setMinWidth(380);
        nameBox = new HBox();
        nameBox.setMinHeight(40);

        nameBox.getChildren().addAll(nameLabel, nameText);
        HBox.setMargin(nameText, new Insets(0,0,0,20));

        typeBox = new HBox();
        typeBox.setMinHeight(40);
        typeBox.getChildren().addAll(typeLabel, typeCheckBox);
        HBox.setMargin(typeCheckBox, new Insets(0,0,0,25));

        cerBox = new HBox();
        typeBox.setMinHeight(40);
        cerBox.getChildren().addAll(cerTypeLabel, cerTypeMenuBtn, cerTypeText);
        HBox.setMargin(cerTypeText, new Insets(0,0,0,20));
        HBox.setMargin(cerTypeMenuBtn, new Insets(0, 0,0,20));


        mainBox.setMinWidth(550);
        mainBox.setMinHeight(170);
        mainBox.setPadding(new Insets(5,0,5,0));
        mainBox.getChildren().addAll(passengerIndex, nameBox, typeBox, cerBox);

    }


    @Override
    protected void updateItem(Passenger item, boolean empty) {
        super.updateItem(item, empty);
        if (!empty && item != null) {
            passengerIndex.setText("第"+ hashList.get(Integer.valueOf(item.getNo())) + "位乘客");
            setGraphic(mainBox);
        } else {
            setGraphic(null);
        }
    }
}
