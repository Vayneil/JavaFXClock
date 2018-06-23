package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddAlarmWindow {

    @FXML
        Button addButton;

    @FXML
        TextField descriptionField;

    @FXML
        TextField hourField;

    @FXML
        TextField minuteField;

    Alarm alarm = new Alarm("", "");

    public void setTime(ActionEvent e) {
        alarm.setDescription(descriptionField.getText());
        alarm.setAlarmTime(hourField.getText() + ":" + minuteField.getText());
        Stage stage = (Stage) addButton.getScene().getWindow();
        stage.close();
    }
}
