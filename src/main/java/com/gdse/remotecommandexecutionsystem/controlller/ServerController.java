package com.gdse.remotecommandexecutionsystem.controlller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ServerController implements Initializable {

    @FXML
    private AnchorPane ancServer;

    @FXML
    private Button btnSend;

    @FXML
    private TextArea txtChat;

    @FXML
    private TextField txtInput;

    private ServerSocket serverSocket;
    private final List<ClientHandler> clientList = new ArrayList<>();

    @FXML
    void btnSendOnClick(javafx.event.ActionEvent event) {
        String message = "Server's Message -> " + txtInput.getText();
        txtChat.appendText(message + "\n");

        for (ClientHandler client : clientList) {
            try {
                client.sendMessage(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        txtInput.clear();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        new Thread(() -> {
            try {
                serverSocket = new ServerSocket(5000);
                Platform.runLater(() -> txtChat.appendText("Server started...\n"));

                while (true) {
                    Socket clientSocket = serverSocket.accept();
                    ClientHandler handler = new ClientHandler(clientSocket);
                    clientList.add(handler);
                    new Thread(handler).start();

                    Platform.runLater(() -> txtChat.appendText("New client connected\n"));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    class ClientHandler implements Runnable {
        private final Socket socket;
        private final DataInputStream dis;
        private final DataOutputStream dos;

        public ClientHandler(Socket socket) throws IOException {
            this.socket = socket;
            this.dis = new DataInputStream(socket.getInputStream());
            this.dos = new DataOutputStream(socket.getOutputStream());
        }

        public void sendMessage(String message) throws IOException {
            dos.writeUTF(message);
            dos.flush();
        }

        @Override
        public void run() {
            try {
                while (true) {
                    String finalMessage = dis.readUTF();
                    Platform.runLater(() -> txtChat.appendText(finalMessage + "\n"));

                    for (ClientHandler client : clientList) {

                        if (client == this) {
                            if (finalMessage.equals("bye")) {
                                client.sendMessage("Disconnected from server");
                                clientList.remove(this);
                                Platform.runLater(() -> txtChat.appendText("Client disconnected\n"));
                            } else if (finalMessage.equals("time")) {
//                            client.sendMessage("Current server time -> " + "\n");
                            } else if (finalMessage.equals("date")) {
//                            client.sendMessage("Today's date -> " + "\n");
                            } else if (finalMessage.equals("uptime")) {
//                            client.sendMessage("Time since server started -> " + "\n");
                            }
                        } else if (client != this) {
                            client.sendMessage(finalMessage);
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
