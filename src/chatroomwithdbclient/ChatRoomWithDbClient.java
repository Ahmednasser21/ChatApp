package chatroomwithdbclient;

import java.io.IOException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import screens.LoginScreen;
import screens.OnlinePlayersScreen;
import screens.SignUpScreen;

public class ChatRoomWithDbClient extends Application {

    public static HashMap<String, Pane> screens = new HashMap<>();
    public static Scene mainScene;
    Stage stage;

    @Override
    public void start(Stage stage) throws Exception {
        this.stage = stage;
        addScreens();
        mainScene = new Scene(screens.get("loginScreen"));
        stage.setScene(mainScene);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                event.consume();
                showRequestAlert("Are you sure you want to quit?");
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void addScreens() {
        screens.put("loginScreen", new LoginScreen());
        screens.put("signUpScreen", new SignUpScreen());
        screens.put("chatScreen", new ChatScreen());
        screens.put("onlineScreen", new OnlinePlayersScreen());
    }

    public static void setScreen(String screenName) {
        Platform.runLater(() -> {
            if (screens.containsKey(screenName)) {
                mainScene.setRoot(screens.get(screenName));
            } else {
                showAlert("Screen Error", "Screen " + screenName + " not found.");
            }
        });
    }

    public static void showAlert(String title, String msg) {
        Platform.runLater(() -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(msg);
            alert.show();
        });

    }

    private void showRequestAlert(String content) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(content);
        ButtonType accept = new ButtonType("Okay");
        ButtonType decline = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(accept, decline);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == decline) {
                alert.close();
            } else if (buttonType == accept) {
                try {
                    ServerHandler.getInstance().sendMessageToServer("signOut");
                    Platform.exit();
                } catch (IOException ex) {
                    Logger.getLogger(OnlinePlayersScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

}
