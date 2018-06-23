package sample;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;


public class Controller {

    double canvasWidth, canvasHeight;

    AddNewAlarmWindow newWindow = new AddNewAlarmWindow();

    public void newAlarmHandler (ActionEvent e) {
        try {
            Alarm newAlarm = newWindow.displayNewAlarmWindow();
            alarms.add(newAlarm);
            table.setItems(alarms);
        }
        catch (IOException a) {
            System.out.println("IOException happened");
        }
    }

    public void closeProgramEvent (ActionEvent e) {
        Platform.exit();
    }

    public void deleteAlarmEvent (ActionEvent e) {
        int selected = table.getSelectionModel().getSelectedIndex();
        if (selected >= 0) {
            table.getItems().remove(selected);
        }
    }

    DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("HH:mm");

    @FXML
    Canvas timeCanvas;
    GraphicsContext gc;

    @FXML
    GridPane gridPane;

    @FXML
    private TableView<Alarm> table;

    private final ObservableList<Alarm> alarms =
            FXCollections.observableArrayList();

    @FXML
    private TableColumn<Alarm, String> descriptionColumn;

    @FXML
    private TableColumn<Alarm, String> alarmTimeColumn;

    public void initialize() {

        gc = timeCanvas.getGraphicsContext2D();
        gc.setLineWidth(3);

        gridPane.prefWidthProperty().addListener( (observable, oldValue, newValue) -> {
            if (newValue.doubleValue() / gridPane.prefHeightProperty().getValue() > 8 / 6.0) {
                timeCanvas.setWidth(gridPane.prefHeightProperty().doubleValue() * 8 / 6.0);
            }
            else {
                timeCanvas.setWidth(newValue.doubleValue());
            }
            timeCanvas.setHeight(timeCanvas.getWidth() * 3 / 8.0);
        } );
        gridPane.prefHeightProperty().addListener( (observable, oldValue, newValue) -> {
            if (gridPane.prefWidthProperty().getValue() / newValue.doubleValue() < 8 / 6.0) {
                timeCanvas.setHeight(gridPane.prefWidthProperty().doubleValue() * 3 / 8.0);
            }
            else {
                timeCanvas.setHeight(newValue.doubleValue() * 0.5);
            }
            timeCanvas.setWidth(timeCanvas.getHeight() * 8 / 3.0);
        } );

        descriptionColumn.setCellValueFactory(new PropertyValueFactory<Alarm, String>("description"));
        alarmTimeColumn.setCellValueFactory(new PropertyValueFactory<Alarm, String>("alarmTime"));
        table.setItems(alarms);

        Timeline clock = new Timeline(new KeyFrame(Duration.ZERO, e -> {

            gc.clearRect(0,0, timeCanvas.getWidth(), timeCanvas.getHeight());
            LocalTime localTime = LocalTime.now();
            draw(localTime.getHour() / 10, 0, timeCanvas.getHeight() / 6, 1);
            draw(localTime.getHour() % 10, timeCanvas.getWidth() / 8, timeCanvas.getHeight() / 6, 1);
            draw(localTime.getMinute() / 10, timeCanvas.getWidth() / 8 * 3, timeCanvas.getHeight() / 6, 1);
            draw(localTime.getMinute() % 10, timeCanvas.getWidth() / 8 * 4, timeCanvas.getHeight() / 6, 1);
            draw(localTime.getSecond() / 10, timeCanvas.getWidth() / 8 * 6, timeCanvas.getHeight() / 6, 1);
            draw(localTime.getSecond() % 10, timeCanvas.getWidth() / 8 * 7, timeCanvas.getHeight() / 6, 1);
            drawColon(timeCanvas.getWidth() / 8 * 2, timeCanvas.getHeight() / 6);
            drawColon(timeCanvas.getWidth() / 8 * 5, timeCanvas.getHeight() / 6);
            for (int i = 0; i < alarms.size(); i++) {
                if (localTime.format(dateFormat).equals(alarms.get(i).getAlarmTime())) {
                    System.out.println("Alarm!");
                }
            }
            /*
            *System.out.println("Grid Width:" + gridPane.getWidth());
            *System.out.println("Grid Height:" + gridPane.getHeight());
            *System.out.println("Canvas Width:" + timeCanvas.getWidth());
            *System.out.println("Canvas Height:" + timeCanvas.getHeight());
            */
        }),
                new KeyFrame(Duration.seconds(1))
        );

        Timeline colonTimeline = new Timeline(new KeyFrame(Duration.seconds(2), e -> {
            gc.clearRect(timeCanvas.getWidth() / 8 * 2, timeCanvas.getHeight() / 6,
                    timeCanvas.getWidth() / 8, timeCanvas.getHeight() / 3 * 2);
            gc.clearRect(timeCanvas.getWidth() / 8 * 5, timeCanvas.getHeight() / 6,
                    timeCanvas.getWidth() / 8, timeCanvas.getHeight() / 3 * 2);
        }),
                new KeyFrame(Duration.ZERO)
        );

        clock.setCycleCount(Animation.INDEFINITE);
        clock.play();
        colonTimeline.setCycleCount(Animation.INDEFINITE);
        colonTimeline.play();
    }




    public void drawColon(double x, double y) {
        gc.fillOval(x + 1 / 16.0 * timeCanvas.getWidth(),y + timeCanvas.getHeight() * 7 / 30,
                timeCanvas.getWidth() / 160, timeCanvas.getHeight() / 60);
        gc.fillOval(x + 1 / 16.0 * timeCanvas.getWidth(), y + timeCanvas.getHeight() * 11 / 30,
                timeCanvas.getWidth() / 160, timeCanvas.getHeight() / 60);
    }

    public void drawZero(double x, double y, double size) {
        drawLine(x, y, size, 1);
        drawLine(x, y, size, 2);
        drawLine(x, y, size, 4);
        drawLine(x, y, size, 5);
        drawLine(x, y, size, 6);
        drawLine(x, y, size, 7);
    }

    public void drawOne(double x, double y, double size) {
        drawLine(x, y, size, 2);
        drawLine(x, y, size, 5);
    }

    public void drawTwo(double x, double y, double size) {
        drawLine(x, y, size, 1);
        drawLine(x, y, size, 2);
        drawLine(x, y, size, 3);
        drawLine(x, y, size, 6);
        drawLine(x, y, size, 7);
    }

    public void drawThree(double x, double y, double size) {
        drawLine(x, y, size, 1);
        drawLine(x, y, size, 2);
        drawLine(x, y, size, 3);
        drawLine(x, y, size, 5);
        drawLine(x, y, size, 6);
    }

    public void drawFour(double x, double y, double size) {
        drawLine(x, y, size, 2);
        drawLine(x, y, size, 3);
        drawLine(x, y, size, 4);
        drawLine(x, y, size, 5);
    }

    public void drawFive(double x, double y, double size) {
        drawLine(x, y, size, 1);
        drawLine(x, y, size, 3);
        drawLine(x, y, size, 4);
        drawLine(x, y, size, 5);
        drawLine(x, y, size, 6);
    }

    public void drawSix(double x, double y, double size) {
        drawFive(x, y, size);
        drawLine(x, y, size, 7);
    }

    public void drawSeven(double x, double y, double size) {
        drawLine(x, y, size, 1);
        drawLine(x, y, size, 2);
        drawLine(x, y, size, 5);
    }

    public void drawEight(double x, double y, double size) {
        drawSix(x, y, size);
        drawLine(x, y, size, 2);
    }
    public void drawNine(double x, double y, double size) {
        drawFive(x, y, size);
        drawLine(x, y, size, 2);
    }

    public void draw(int number, double x, double y, double size) {
        switch(number) {
            case 0: {
                drawZero(x, y, size);
                break;
            }
            case 1: {
                drawOne(x, y, size);
                break;
            }
            case 2: {
                drawTwo(x, y, size);
                break;
            }
            case 3: {
                drawThree(x, y, size);
                break;
            }
            case 4: {
                drawFour(x, y, size);
                break;
            }
            case 5: {
                drawFive(x, y, size);
                break;
            }
            case 6: {
                drawSix(x, y, size);
                break;
            }
            case 7: {
                drawSeven(x, y, size);
                break;
            }
            case 8: {
                drawEight(x, y, size);
                break;
            }
            case 9: {
                drawNine(x, y, size);
                break;
            }
            default: {
                System.out.println("Blad");
            }
        }
    }

    public void drawLine(double x, double y, double size, int numberOfLine) {
        switch(numberOfLine) {
            case 1: {
                gc.strokeLine(x + 1/40.0*timeCanvas.getWidth(), y, x + 1/10.0*timeCanvas.getWidth(), y);
                break;
            }
            case 2: {
                gc.strokeLine(x + timeCanvas.getWidth()/80.0*9, y + 1/30.0*timeCanvas.getHeight(),
                        x + 9/80.0*timeCanvas.getWidth(), y + 3/10.0*timeCanvas.getHeight());
                break;
            }
            case 3: {
                gc.strokeLine(x + 1/40.0*timeCanvas.getWidth(), y + 1/3.0*timeCanvas.getHeight(),
                        x + 1/10.0*timeCanvas.getWidth(), y + 1/3.0*timeCanvas.getHeight());
                break;
            }
            case 4: {
                gc.strokeLine(x + 1/80.0*timeCanvas.getWidth(), y + 1/30.0*timeCanvas.getHeight(),
                        x + 1/80.0*timeCanvas.getWidth(), y + 3/10.0*timeCanvas.getHeight());
                break;
            }
            case 5: {
                gc.strokeLine(x + timeCanvas.getWidth()/80.0*9, y + 11/30.0*timeCanvas.getHeight(),
                        x + timeCanvas.getWidth()/80.0*9, y + 19/30.0*timeCanvas.getHeight());
                break;
            }
            case 6: {
                gc.strokeLine(x + 1/40.0*timeCanvas.getWidth(), y + 2/3.0*timeCanvas.getHeight(),
                        x + 1/10.0*timeCanvas.getWidth(), y + 2/3.0*timeCanvas.getHeight());
                break;
            }
            case 7: {
                gc.strokeLine(x + 1/80.0*timeCanvas.getWidth(), y + 11/30.0*timeCanvas.getHeight(),
                        x + 1/80.0*timeCanvas.getWidth(), y + 19/30.0*timeCanvas.getHeight());
                break;
            }
            default: {
                System.out.println("error");
            }
        }
    }


}

