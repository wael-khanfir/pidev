/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package issam;

import CnxBD.MyConnection;
import entities.Reservation;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author Bassem
 */
public class ReservationController implements Initializable {

    @FXML
    private Button back;

    @FXML
    private Button consult;

    @FXML
    private TextField priceField;

    @FXML
    private TextField numberField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField cardNumField;

    @FXML
    private TextField expiryDateField;

    @FXML
    private TextField cvcField;

    @FXML
    private Button payButton;

    private MyConnection myconnection;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        myconnection = new MyConnection();

    }

    @FXML
    private void handlePay() {

        String firstName = firstNameField.getText();
        String lastName = lastNameField.getText();
        String email = emailField.getText();
        int number = Integer.parseInt(numberField.getText());

        System.out.println("Card Number :" + cardNumField.getText());
        System.out.println("Expiry Date :" + expiryDateField.getText());
        System.out.println("CVC Code :" + cvcField.getText());

        String st = "insert into reservation(first_name, last_name, email, appointed_num) " +
                "VALUES('"+ firstName +"','"+ lastName +"','"+ email +"','"+ number +"') ";
        if (myconnection.execAction(st)) {
            AlertMaker.showSuccessNotification("Successfully inserted reservation.");
        } else {
            AlertMaker.showErrorNotification("Reservation not inserted.");
        }

    }

    @FXML
    private void back() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
            Stage stage = (Stage) back.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void consulter() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(""));
            Stage stage = (Stage) consult.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void infalteUI(Reservation selectedForEdit) {
        firstNameField.setText(selectedForEdit.getFirst_name());
        lastNameField.setText(selectedForEdit.getLast_name());
        emailField.setText(selectedForEdit.getEmail());
    }

    public void handleEnter() {
        try {

            String first_name = null,last_name = null,email = null;

            int number = Integer.parseInt(numberField.getText());

            String query = "SELECT first_name,last_name,email FROM invite WHERE appointed_num = '"+ number +"';";

            ResultSet resultSet = myconnection.execQuery(query);

            while (resultSet.next()){
                first_name = resultSet.getString(1);
                last_name = resultSet.getString(2);
                email = resultSet.getString(3);
            }

            firstNameField.setText(first_name);
            lastNameField.setText(last_name);
            emailField.setText(email);

        } catch (SQLException throwables) {
            AlertMaker.showErrorMessage(throwables);
        }

    }
}

/*
Adding items to combobox

try {
            rs = cnx.createStatement().executeQuery(SQL);
        } catch (SQLException ex) {
            Logger.getLogger(InterviewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            while (rs.next()) {  // loop

                // Now add the comboBox addAll statement
                comb_invite.getItems().addAll(rs.getString("nom"));
            }} catch (SQLException ex) {
            Logger.getLogger(InterviewController.class.getName()).log(Level.SEVERE, null, ex);
        }

        comb_invite.setValue("Nom Invite");
        //comb_invite.setItems(invitelist);
        type.setValue("Type Interview");
        type.setItems(typelist);
 */
