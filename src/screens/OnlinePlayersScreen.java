package screens;

import chatroomwithdbclient.ServerHandler;
import chatroomwithdbclient.UserDto;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.util.Callback;

public class OnlinePlayersScreen extends AnchorPane {

    protected final TableView<UserDto> tableView_col;
    protected final TableColumn<UserDto, String> playerName_col;
    protected final TableColumn<UserDto, String> status_col;
    protected final Button btn_signOut;
    protected final Button btn_refresh;
    ServerHandler handler;
    

    public static ObservableList<UserDto> users = FXCollections.observableArrayList();

    public OnlinePlayersScreen() {

        tableView_col = new TableView<>();
        playerName_col = new TableColumn<>();
        status_col = new TableColumn<>();
        btn_signOut = new Button();
        btn_refresh = new Button();

        setId("AnchorPane");
        setPrefHeight(850.0);
        setPrefWidth(900.0);

        tableView_col.setLayoutX(92.0);
        tableView_col.setLayoutY(60.0);
        tableView_col.setPrefHeight(500.0);
        tableView_col.setPrefWidth(600.0);

        playerName_col.setPrefWidth(300.0);
        playerName_col.setText("Player name");
        tableView_col.getColumns().add(playerName_col);
        tableView_col.getColumns().add(status_col);

        status_col.setPrefWidth(300.0);
        status_col.setText("Status");

        btn_signOut.setLayoutX(300.0);
        btn_signOut.setLayoutY(600.0);
        btn_signOut.setMnemonicParsing(false);
        btn_signOut.setOnAction(this::handleOnBtnSignOut);
        btn_signOut.setPrefHeight(60.0);
        btn_signOut.setPrefWidth(138.0);
        btn_signOut.setText("SignOut");
        
        
        btn_refresh.setLayoutX(100.0);
        btn_refresh.setLayoutY(600.0);
        btn_refresh.setMnemonicParsing(false);
        btn_refresh.setOnAction(this::handleOnBtnRefresh);
        btn_refresh.setPrefHeight(60.0);
        btn_refresh.setPrefWidth(138.0);
        btn_refresh.setText("Refresh");

        // Bind TableView columns to UserDto properties
        playerName_col.setCellValueFactory(new PropertyValueFactory<>("username"));
        status_col.setCellValueFactory(new PropertyValueFactory<>("online"));

        // Set items in the TableView
        tableView_col.setItems(users);
        getChildren().add(tableView_col);
        getChildren().add(btn_signOut);
        getChildren().add(btn_refresh);

        try {
            handler = ServerHandler.getInstance();
        } catch (IOException ex) {
            Logger.getLogger(OnlinePlayersScreen.class.getName()).log(Level.SEVERE, null, ex);
        }

        playerName_col.setCellFactory(new Callback<TableColumn<UserDto, String>, TableCell<UserDto, String>>() {
            @Override
            public TableCell<UserDto, String> call(TableColumn<UserDto, String> param) {
                return new TableCell<UserDto, String>() {
                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty || item == null) {
                            setText(null);
                        } else {
                            setText(item);
                            setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    if (!isEmpty() && event.getClickCount() == 2) {
                                        try {
                                            handler.sendMessageToServer("request" + "," + item);

                                        } catch (IOException ex) {
                                            Logger.getLogger(OnlinePlayersScreen.class.getName()).log(Level.SEVERE, null, ex);
                                        }
                                    }
                                }
                            });
                        }
                    }

                };

            }
        });

    }

    protected void handleOnBtnSignOut(javafx.event.ActionEvent actionEvent) {
        showRequestAlert("Are you sure you want to quit?");
        
    }
    
     protected void handleOnBtnRefresh(javafx.event.ActionEvent actionEvent) {
        try {
            users.clear();
            handler.sendMessageToServer("updateList");
        } catch (IOException ex) {
            Logger.getLogger(OnlinePlayersScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
     
     private void showRequestAlert(String content) {

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(content);
        ButtonType accept = new ButtonType("Okay");
        ButtonType decline = new ButtonType("Cancel");
        alert.getButtonTypes().setAll(accept, decline);
        alert.showAndWait().ifPresent(buttonType -> {
            if (buttonType == decline) {
                
            } else if (buttonType == accept) {
                try {
                    handler.sendMessageToServer("signOut");
                } catch (IOException ex) {
                    Logger.getLogger(OnlinePlayersScreen.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
}
