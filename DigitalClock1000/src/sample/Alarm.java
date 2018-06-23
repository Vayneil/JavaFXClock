package sample;

import javafx.beans.property.SimpleStringProperty;

public class Alarm {
    private final SimpleStringProperty description;
    private final SimpleStringProperty alarmTime;

    public Alarm(String description, String alarmTime) {
        this.description = new SimpleStringProperty(description);
        this.alarmTime = new SimpleStringProperty(alarmTime);
    }

    public String getDescription() {
        return description.get();
    }

    public void setDescription(String desc) {
        description.set(desc);
    }

    public String getAlarmTime() {
        return alarmTime.get();
    }

    public void setAlarmTime(String aTm) {
        alarmTime.set(aTm);
    }

}

