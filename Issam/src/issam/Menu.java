/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package issam;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import CnxBD.MyConnection;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bassem
 */
public class Menu implements Initializable {


    public Button usersButton;
    @FXML
    private TextField userTypeField;
    @FXML
    private Button dcnx;

    @FXML
    private Button Invite;

    @FXML
    private Button reserve;

    @FXML
    private Button addInvite;

    @FXML
    private Button addReserve;

    @FXML
    private Button meetingButton;

    private MyConnection myconnection;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       myconnection  = new MyConnection();
       checkUserType();
    }

    private void checkUserType() {

        try {
            String userType = null;

            String query = "SELECT user_type FROM user_accounts WHERE is_online = 1;";

            ResultSet resultSet = myconnection.execQuery(query);

            while (resultSet.next()){
                userType = resultSet.getString(1);
            }
            System.out.println("User Type : " + userType);

            assert userType != null;
            if (userType.equals("USER") || userType.equals("user") || userType.equals("User")) {
                reserve.setDisable(true);
                addInvite.setDisable(true);
                usersButton.setDisable(true);
            } else if (userType.equals("ADMIN") || userType.equals("admin")){
                meetingButton.setDisable(true);
                addReserve.setDisable(true);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }


    public void getUser(String username) {
        userTypeField.setText(username);
    }


    @FXML
    private void dcnx() {
        try {
            String usernameFromBack = userTypeField.getText();
            System.out.println(usernameFromBack);
            String query = "UPDATE user_accounts SET is_online = 0 WHERE username = '" + usernameFromBack + "';";
            if (myconnection.execAction(query)) {
                AlertMaker.showSuccessNotification("Logout successfull");
                Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));
                Stage stage = (Stage) dcnx.getScene().getWindow();
                stage.close();
                Scene scene = new Scene(root);

                stage.setScene(scene);
                stage.show();

            } else {
                AlertMaker.showErrorMessage("Logout Failed","Logout failed due to some error.");
            }
        } catch (IOException e) {
            AlertMaker.showErrorMessage(e);
        }
    }
    @FXML
    void fen_addInvit() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Invite.fxml"));
            Stage stage = (Stage) addInvite.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void fen_addReserve() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("reservation.fxml"));
            Stage stage = (Stage) addReserve.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void fen_invit() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("list_invite.fxml"));
            Stage stage = (Stage) Invite.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void fen_reserve() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Consulter_reservation.fxml"));
            Stage stage = (Stage) reserve.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    void fen_meeting() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("web.fxml"));
            Stage stage = (Stage) meetingButton.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(root);

            stage.setMaximized(true);

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void fen_users() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("users.fxml"));
            Stage stage = (Stage) usersButton.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(root);

            stage.setResizable(false);

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}
