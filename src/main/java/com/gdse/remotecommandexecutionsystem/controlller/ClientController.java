package com.gdse.remotecommandexecutionsystem.controlller;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.sql.Time;
import java.util.ResourceBundle;

public class ClientController implements Initializable {

    @FXML
    private AnchorPane ancClient;

    @FXML
    private AnchorPane ancCommand;

    @FXML
    private Button btnBye;

    @FXML
    private Button btnDate;

    @FXML
    private Button btnHelp;

    @FXML
    private Button btnSend;

    @FXML
    private Button btnTime;

    @FXML
    private Button btnUptime;

    @FXML
    private TextArea txtChat;

    @FXML
    private TextField txtInput;

    private Socket socket;
    private DataOutputStream dataOutputStream;
    private DataInputStream dataInputStream;

    @FXML
    void btnByeOnClick(ActionEvent event) throws IOException {
        String message = "bye";
        txtChat.appendText(message + "\n");
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
        txtInput.clear();
    }

    @FXML
    void btnDateOnClick(ActionEvent event) throws IOException {
        String message = "date";
        txtChat.appendText(message + "\n");
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
        txtInput.clear();
    }

    @FXML
    void btnHelpOnClick(ActionEvent event) {
        ancCommand.setVisible(true);
        txtChat.appendText( "Use Respond commands." + "\n");
    }

    @FXML
    void btnSendOnClick(ActionEvent event) throws IOException {
        String message = "Client: " + txtInput.getText();
        txtChat.appendText(message + "\n");
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
        txtInput.clear();
    }

    @FXML
    void btnTimeOnClick(ActionEvent event) throws IOException {
        String message = "time";
        txtChat.appendText(message + "\n");
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
        txtInput.clear();
    }

    @FXML
    void btnUptimeOnClick(ActionEvent event) throws IOException {
        String message = "uptime";
        txtChat.appendText(message + "\n");
        dataOutputStream.writeUTF(message);
        dataOutputStream.flush();
        txtInput.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ancCommand.setVisible(false);
        new Thread(() -> {
            try {
                socket = new Socket("localhost", 5000);
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream = new DataOutputStream(socket.getOutputStream());

//                String message = dataInputStream.readUTF();
//                do {
//                    if (message.equals("time")) {
//                        txtChat.appendText( "Current server time -> " + "\n");
//                    } else if (message.equals("date")) {
//                        txtChat.appendText( "Today's date -> " + "\n");
//                    } else if (message.equals("uptime")) {
//                        txtChat.appendText( "Time since server started -> " + "\n");
//                    } else {
//                        Platform.runLater(() -> txtChat.appendText(message + "\n"));
//                    }
//                } while (!message.equals("bye"));

                while (true) {
                    String message = dataInputStream.readUTF();
                    Platform.runLater(() -> txtChat.appendText(message + "\n"));
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
