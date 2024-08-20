package chatroomwithdbclient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import screens.OnlinePlayersScreen;

public class ServerHandler {

    DataInputStream reader;
    DataOutputStream writer;
    static Socket socket;
    private static ServerHandler instance = null;
    String sender;
    String receiver;
    public Vector<UserDto> onlinePlayerNames = new Vector<>();

    private ServerHandler() throws IOException {
        socket = new Socket("127.0.0.1", 6004);
        reader = new DataInputStream(socket.getInputStream());
        writer = new DataOutputStream(socket.getOutputStream());
        readMessageFromServer();
    }

    public static ServerHandler getInstance() throws IOException {
        if (instance == null) {
            instance = new ServerHandler();
        }
        return instance;
    }

    public void readMessageFromServer() throws IOException {
        new Thread(() -> {
            try {
                while (!socket.isClosed()) {
                    String receivedMessage = reader.readUTF();
                    if (receivedMessage.equals("loginSuccess")) {
                        ChatRoomWithDbClient.setScreen("onlineScreen");
                    } else if (receivedMessage.equals("loginFail")) {
                        showErrorAlert("username or password is invalid");
                    } else if (receivedMessage.equals("registeredSuccessfully")) {
                        showAcceptanceAlert("registered Successfully");
                    } else if (receivedMessage.equals("alreadyExist")) {
                        showErrorAlert("This user already exists");
                    } else if (receivedMessage.startsWith("onlinePlayersStart") || receivedMessage.equals("updateList")) {
                        String[] splittedMessage = receivedMessage.split(",");
                        if (splittedMessage.length > 1) {
                            UserDto user = new UserDto();
                            user.setUsername(splittedMessage[1]);
                            
                            Platform.runLater(() -> {
                                OnlinePlayersScreen.users.add(user);
                            });
                        }
                    } else if (receivedMessage.startsWith("handleRequest")) {
                        String[] message = receivedMessage.split(",");
                        sender = message[1];
                        receiver = message[2];
                        showRequestAlert("You got request from " + sender);
                    } else if (receivedMessage.equals("request declined")) {
                        showErrorAlert("Request declined!");
                    } else if (receivedMessage.equals("navigate")) {
                        ChatRoomWithDbClient.setScreen("chatScreen");
                    } else if(receivedMessage.startsWith("message")){
                        String[] parts = receivedMessage.split(",");
                        ChatScreen.returnTheSameMessage(parts[1]);
                    }else if(receivedMessage.equals("singOutSuccessfully")){
                        ChatRoomWithDbClient.setScreen("loginScreen");
                    }
                    
                    else {
                        break;
                    }
                }
            } catch (IOException ex) {
                System.out.println("server is closed");
                System.out.println(ex.getMessage());
            }
        }).start();
    }

    public void sendMessageToServer(String message) throws IOException {
        writer.writeUTF(message);
    }

    private void showErrorAlert(String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION, content, ButtonType.CLOSE);
            alert.show();
        });
    }

    
    private void showAcceptanceAlert(String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(content);
            ButtonType button = new ButtonType("Okay");
            alert.getButtonTypes().setAll(button);
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == button) {
                    ChatRoomWithDbClient.setScreen("loginScreen");
                }
            });
        });
    }

    private void showRequestAlert(String content) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText(content);
            ButtonType accept = new ButtonType("Accept");
            ButtonType decline = new ButtonType("Decline");
            alert.getButtonTypes().setAll(accept, decline);
            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == decline) {
                    try {
                        sendMessageToServer("decline");
                    } catch (IOException ex) {
                        Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                } else if (buttonType == accept) {
                    try {
                        sendMessageToServer("accepted" + "," + sender + "," + receiver);

                    } catch (IOException ex) {
                        Logger.getLogger(ServerHandler.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            });
        });
    }
}
