module com.gdse.remotecommandexecutionsystem {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires java.sql;


    opens com.gdse.remotecommandexecutionsystem to javafx.fxml;
    exports com.gdse.remotecommandexecutionsystem;

    exports com.gdse.remotecommandexecutionsystem.controlller;
    opens com.gdse.remotecommandexecutionsystem.controlller to javafx.fxml;
}