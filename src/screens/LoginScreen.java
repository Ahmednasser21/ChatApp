package screens;

import chatroomwithdbclient.ChatRoomWithDbClient;
import chatroomwithdbclient.ServerHandler;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class LoginScreen extends AnchorPane {

    protected final TextField username_text;
    protected final TextField password_text;
    protected final Button btn_login;
    protected final Button btn_signUp;
    ServerHandler handler;
    String receivedMessage;

    public LoginScreen() {

        username_text = new TextField();
        password_text = new PasswordField();
        btn_login = new Button();
        btn_signUp = new Button();

        setId("AnchorPane");
        setPrefHeight(700.0);
        setPrefWidth(750.0);

        username_text.setLayoutX(148.0);
        username_text.setLayoutY(198.0);
        username_text.setPrefHeight(31.0);
        username_text.setPrefWidth(446.0);
        username_text.setPromptText("Enter username");

        password_text.setLayoutX(148.0);
        password_text.setLayoutY(257.0);
        password_text.setPrefHeight(31.0);
        password_text.setPrefWidth(446.0);
        password_text.setPromptText("Enter password");

        btn_login.setLayoutX(180.0);
        btn_login.setLayoutY(330.0);
        btn_login.setMnemonicParsing(false);
        btn_login.setOnAction(this::handleOnBtnLogin);
        btn_login.setPrefHeight(40.0);
        btn_login.setPrefWidth(140.0);
        btn_login.setText("Login");
        
        btn_signUp.setLayoutX(440);
        btn_signUp.setLayoutY(330);
        btn_signUp.setMnemonicParsing(false);
        btn_signUp.setOnAction(this::handleOnBtnSignUp);
        btn_signUp.setPrefHeight(40);
        btn_signUp.setPrefWidth(140);
        btn_signUp.setText("SignUp");

        getChildren().add(username_text);
        getChildren().add(password_text);
        getChildren().add(btn_login);
        getChildren().add(btn_signUp);
        try {
            
            handler = ServerHandler.getInstance();
            
        } catch (IOException ex) {
            Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        

    }

    protected void handleOnBtnLogin(javafx.event.ActionEvent actionEvent) {
        String message = "login"+","+username_text.getText()+","+password_text.getText();
        try {
            handler.sendMessageToServer(message);
        } catch (IOException ex) {
            Logger.getLogger(LoginScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    };
    
    protected void handleOnBtnSignUp(javafx.event.ActionEvent actionEvent){
        ChatRoomWithDbClient.setScreen("signUpScreen");
    }
    

   
    
    

}
