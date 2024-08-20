package screens;

import chatroomwithdbclient.ChatRoomWithDbClient;
import chatroomwithdbclient.ServerHandler;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SignUpScreen extends AnchorPane {

    protected final TextField username_text;
    protected final TextField email_text;
    protected final TextField password_text;
    protected final Button signUp_btn;
    protected final Button login_btn;
    ServerHandler handler;
    Socket socket;

    public SignUpScreen() {

        username_text = new TextField();
        email_text = new TextField();
        password_text = new PasswordField();
        signUp_btn = new Button();
        login_btn = new Button();

        setId("AnchorPane");
        setPrefHeight(700.0);
        setPrefWidth(750.0);

        username_text.setLayoutX(179.0);
        username_text.setLayoutY(195.0);
        username_text.setPrefHeight(22.0);
        username_text.setPrefWidth(367.0);
        username_text.setPromptText("Enter username");

        email_text.setLayoutX(179.0);
        email_text.setLayoutY(248.0);
        email_text.setPrefHeight(22.0);
        email_text.setPrefWidth(367.0);
        email_text.setPromptText("Enter email");

        password_text.setLayoutX(179.0);
        password_text.setLayoutY(299.0);
        password_text.setPrefHeight(22.0);
        password_text.setPrefWidth(367.0);
        password_text.setPromptText("Enter password");
        

        signUp_btn.setLayoutX(140.0);
        signUp_btn.setLayoutY(407.0);
        signUp_btn.setMnemonicParsing(false);
        signUp_btn.setOnAction(this::handleOnBtnSignUp);
        signUp_btn.setPrefHeight(40.0);
        signUp_btn.setPrefWidth(180.0);
        signUp_btn.setText("Sign Up");
        
        login_btn.setLayoutX(400.0);
        login_btn.setLayoutY(407.0);
        login_btn.setMnemonicParsing(false);
        login_btn.setOnAction(this::handleOnBtnLogin);
        login_btn.setPrefHeight(40.0);
        login_btn.setPrefWidth(180.0);
        login_btn.setText("Login");

        getChildren().add(username_text);
        getChildren().add(email_text);
        getChildren().add(password_text);
        getChildren().add(signUp_btn);
        getChildren().add(login_btn);
        try {
             handler = ServerHandler.getInstance();
        } catch (IOException ex) {
            Logger.getLogger(SignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    protected void handleOnBtnSignUp(javafx.event.ActionEvent actionEvent) {

        if (username_text.getText().isEmpty() || email_text.getText().isEmpty() || password_text.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("All fields must be filled!");
            alert.show();
        } else {
            String message = "signUp," + username_text.getText() + "," + email_text.getText() + "," + password_text.getText();
            try {
                handler.sendMessageToServer(message);
            } catch (IOException ex) {
                Logger.getLogger(SignUpScreen.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }
;
    
    protected void handleOnBtnLogin(javafx.event.ActionEvent actionEvent){
        ChatRoomWithDbClient.setScreen("loginScreen");
    }

}
