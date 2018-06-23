package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class AddNewAlarmWindow {

    public Alarm displayNewAlarmWindow() throws IOException {
        Stage primaryStage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addAlarmWindow.fxml"));
        Parent root = (Parent) loader.load();
        AddAlarmWindow controller = loader.getController();
        primaryStage.setTitle("New Alarm");
        primaryStage.setScene(new Scene(root, 300, 300));
        primaryStage.showAndWait();
        return controller.alarm;
    }
}
