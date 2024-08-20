package chatroomwithdbclient;

import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import screens.OnlinePlayersScreen;

public class ChatScreen extends AnchorPane {

    protected static TextArea _textArea;
    protected final TextField _textField;
    protected final Button btn_sendMesage;
    Socket socket;
    ServerHandler handler;
    Label currentName;


    public ChatScreen() {
        _textArea = new TextArea();
        _textField = new TextField();
        btn_sendMesage = new Button();
        currentName = new Label();

        setId("AnchorPane");
        setPrefHeight(500.0);
        setPrefWidth(750.0);

        _textArea.setLayoutX(61.0);
        _textArea.setLayoutY(36.0);
        _textArea.setPrefHeight(282.0);
        _textArea.setPrefWidth(640.0);

        _textField.setLayoutX(61.0);
        _textField.setLayoutY(361.0);
        _textField.setPrefHeight(31.0);
        _textField.setPrefWidth(640.0);
        
        
        currentName.setLayoutX(375);
        currentName.setLayoutY(10);
        currentName.setText("");

        btn_sendMesage.setLayoutX(61.0);
        btn_sendMesage.setLayoutY(420.0);
        btn_sendMesage.setMnemonicParsing(false);
        btn_sendMesage.setOnAction(this::handleOnBtnSendMessage);
        btn_sendMesage.setText("Send Message");

        getChildren().add(_textArea);
        getChildren().add(_textField);
        getChildren().add(btn_sendMesage);
        getChildren().add(currentName);

        try {
            handler = ServerHandler.getInstance();

        } catch (IOException ex) {
            Logger.getLogger(ChatScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }

    protected void handleOnBtnSendMessage(javafx.event.ActionEvent actionEvent) {
        String messageText = _textField.getText().trim();
        if (!messageText.isEmpty()) {
            String fullMessage = "message," + messageText;
            new Thread(() -> {
                try {
                    handler.sendMessageToServer(fullMessage);
                    javafx.application.Platform.runLater(() -> {
                        
                        _textField.clear();
                    });
                } catch (IOException ex) {
                    Logger.getLogger(ChatScreen.class.getName()).log(Level.SEVERE, null, ex);
                    javafx.application.Platform.runLater(() -> {
                        showErrorAlert("Failed to send message");
                    });
                }
            }).start();
        }
    }

    private void showErrorAlert(String content) {
        javafx.application.Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.ERROR, content, ButtonType.OK);
            alert.show();
        });
    }

    
    
    
    public static String returnTheSameMessage(String message)
    {
        Platform.runLater(()->{
             _textArea.appendText(message+"\n");
        });
        return message;
    }
    
    
    
}
