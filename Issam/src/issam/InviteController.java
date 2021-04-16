/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package issam;

import CnxBD.MyConnection;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import entities.Invite;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Bassem
 */
public class InviteController implements Initializable {

    @FXML
    private TextField nom_invit;
    @FXML
    private TextField carriere;
    @FXML
    private TextField prenom_invit;
    @FXML
    private TextField emailField;
    @FXML
    private DatePicker date;
    @FXML
    private Button save;
    @FXML
    private Label label;
    @FXML
    private Button back;
    @FXML
    private Label msg;
    @FXML
    private Button consult;

    @FXML
    private ComboBox<String> interviewTypeCB;

    private MyConnection myconnection;


    /**
     * Initializes the controller class.
     */
//    PreparedStatement preparedStatement;
//    private Statement st;
//    private ResultSet rs;
//     Myconnection myc = Myconnection.getIstance();
//    Connection cnx = myc.getConnection();
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        myconnection = new MyConnection();

    }    

    @FXML
    private void valider() {
        String first_name = nom_invit.getText();
        String last_name = prenom_invit.getText();
        String email = emailField.getText();
        String career = carriere.getText();
        String interviewType = interviewTypeCB.getSelectionModel().getSelectedItem();


        int min = 1000;
        int max = 9999;

        int random_int = (int)(Math.random() * (max - min + 1) + min);

        System.out.println("random number : " + random_int);



        if (nom_invit.getText().isEmpty() || date.getValue() == null || prenom_invit.getText().isEmpty() || carriere.getText().isEmpty()) {
            System.out.println("Something Up...");
            
        } else {
         String st = "INSERT INTO invite (first_name,last_name,email,career,interview_type,interview_date,appointed_num) " +
                 "VALUES ('"+ first_name +"','"+ last_name +"','"+ email +"','"+ career +"','"+ interviewType +"','"+ date.getValue() +"','"+ random_int +"')";
         myconnection.execAction(st);

         
        }
    }

    @FXML
    private void back(ActionEvent event) {
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
            Parent root = FXMLLoader.load(getClass().getResource("../../not needed classes/Consulter_invite.fxml"));
            Stage stage = (Stage) consult.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(root);
            
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void infalteUI(Invite selectedForEdit) {
        nom_invit.setText(selectedForEdit.getFirst_name());
        prenom_invit.setText(selectedForEdit.getLast_name());
        carriere.setText(selectedForEdit.getCareer());
        interviewTypeCB.setValue(selectedForEdit.getType());
        date.setValue(LocalDate.parse(selectedForEdit.getDate()));

        save.setText("Update");
    }
}
