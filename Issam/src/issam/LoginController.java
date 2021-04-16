/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package issam;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import CnxBD.MyConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import tray.notification.NotificationType;
import tray.notification.TrayNotification;

/**
 * FXML Controller class
 *
 * @author Bassem
 */
public class LoginController implements Initializable {

    @FXML
    private Button cnc;
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Text show;

    private MyConnection myconnection;


    private String dbuser, dbpw = null;
    private int dbId = 0;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        myconnection = new MyConnection();
        handleFirstTime();
        usernameField.setFocusTraversable(true);
        usernameField.requestFocus();
    }

    private void handleFirstTime() {
        try {
            int output = 0;

            String query = "select exists(select 1 from user_accounts) AS 'user_accounts';";

            ResultSet set = myconnection.execQuery(query);

            while (set.next()){
                output = set.getInt(1);
            }

            if (output == 0){

                String insertQuery = "INSERT INTO user_accounts(full_name, username, password, email,user_type) \n" +
                        "VALUES ('Default User','admin','admin','-','ADMIN')";

                myconnection.execAction(insertQuery);
                System.out.println("Adding default user because no user exists...");
            }
        } catch (SQLException throwables) {
            AlertMaker.showErrorMessage(throwables);
        }
    }

    @FXML
    private void connect() {
        try {
            String user = usernameField.getText();
            System.out.println("User :" + user);
            String pw = passwordField.getText();
            System.out.println("Pass :" + pw);


            String query = "SELECT * from user_accounts WHERE username = '" + user + "';";

            ResultSet resultSet = myconnection.execQuery(query);

            while (resultSet.next()){
                dbId = resultSet.getInt("id");
                dbuser = resultSet.getString("username");
                dbpw = resultSet.getString("password");
            }

            System.out.println("ID : " + dbId);
            System.out.println("username :" + dbuser);
            System.out.println("password :" + dbpw);
            if (user.equals(dbuser) && pw.equals(dbpw)) {
                String query1 = "UPDATE user_accounts SET is_online = 1 WHERE id = '" + dbId + "';";

                myconnection.execAction(query1);

                TrayNotification trayNotification = new TrayNotification();
                trayNotification.setNotificationType(NotificationType.SUCCESS);
                trayNotification.setTitle("Login Successful");
                trayNotification.setMessage("Login as " + user + " Successful");
                trayNotification.showAndDismiss(Duration.millis(2000));
                cnc.getScene().getWindow().hide();
                loadWindow();
            } else {
                clearFields();
                Platform.runLater(() -> usernameField.requestFocus());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setContentText("Username or password incorrect.");
                alert.showAndWait();
            }
        } catch (SQLException throwables) {
            System.out.println(throwables.getLocalizedMessage());
        }
    }
    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

    private void loadWindow(){
        String user = usernameField.getText();
        try {
            Stage stage = new Stage();
            FXMLLoader loader = new FXMLLoader();
            Parent root = loader.load(getClass().getResource("/issam/Menu.fxml").openStream());
            Menu controller = loader.getController();
            controller.getUser(user);
            stage.initStyle(StageStyle.DECORATED);
            stage.setTitle("Issam");

            stage.setOnCloseRequest(event -> {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you Sure that you want to exit?", ButtonType.OK, ButtonType.CANCEL);
                alert.setTitle("Confirm Exit");
                Optional<ButtonType> answer = alert.showAndWait();

                if (answer.get().equals(ButtonType.OK)) {

                    String query = "UPDATE user_accounts SET is_online = 0 WHERE username = '" + user + "';";
                    if (myconnection.execAction(query)) {
                        AlertMaker.showSuccessNotification("Exit Successful.");
                        System.exit(1);
                    } else {
                        AlertMaker.showErrorNotification("Exiting software failed due to logging out.");
                    }
                }else {
                    event.consume();
                }
            });
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            AlertMaker.showErrorMessage(e);
        }
    }


    public void handleEnter(KeyEvent event) {

        if (event.getCode().equals(KeyCode.ENTER)) {
            connect();
        }

    }
}

