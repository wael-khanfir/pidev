/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;


import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import connexionbd.connexion;
import entity.email;
import entity.projection;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javax.mail.MessagingException;
import service.Service_projection;

/**
 * FXML Controller class
 *
 * @author FK Info
 */
public class ViewController implements Initializable {


    @FXML
    private Button id_button;
    @FXML
    private TextField nom;
    @FXML
    private TextField genre;
    @FXML
    private TextField age;
    @FXML
    private TextField duree;
    @FXML
    private TableView<projection> tb_projection;
    @FXML
    private TableColumn<projection, Integer> tb_id;
    @FXML
    private TableColumn<projection, String> tb_nom;
    @FXML
    private TableColumn<projection, String> tb_genre;
    @FXML
    private TableColumn<projection, Integer> tb_age;
    @FXML
    private TableColumn<projection, String> tb_duree;
    @FXML
    private TextField id_supp;
    @FXML
    private Button supp;
    @FXML
    private Button modifier;
    @FXML
    private Button r_id;
    @FXML
    private TextField mail;
    @FXML
    private Button eemail;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
     afficher_projection();
    }    
    public void afficher_projection()
    {
        Service_projection ps = new Service_projection();
        ResultSet resultset=ps.getall_projection();
        List<projection> pl = new ArrayList<projection>();
        try {
            while(resultset.next()){
                projection p1 = new projection();
                p1.setId(resultset.getInt("id"));
                p1.setNom(resultset.getString("nom"));
                p1.setGenre(resultset.getString("genre"));
                p1.setAge_recommande(resultset.getInt("age_recommande"));
                 p1.setDuree(resultset.getString("duree"));
               
                pl.add(p1);
                
               
                }
            ObservableList<projection> listprojection = FXCollections.observableArrayList(pl);
        //tb_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tb_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tb_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        tb_age.setCellValueFactory(new PropertyValueFactory<>("age_recommande"));
        tb_duree.setCellValueFactory(new PropertyValueFactory<>("duree"));
      
        tb_projection.setItems(listprojection);
        } catch (SQLException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }   

   @FXML
    void ajouter_proj(ActionEvent event) {
       Service_projection ps= new Service_projection();
        projection p =new projection();
        //p.setId(Integer.parseInt(id.getText()) );
       
        p.setNom(nom.getText());
        p.setGenre(genre.getText());
        p.setAge_recommande(Integer.parseInt(age.getText()) );
        p.setDuree(duree.getText());
       ps.ajouter_projection(p);
       System.out.println("projection ajouter avec succé");
       afficher_projection();
    }

    @FXML
    private void supp_projection(ActionEvent event) {
        Service_projection ps= new Service_projection();
        projection p =new projection();
        p.setNom((id_supp.getText()) );
        ps.supprimer_projection(p);
        System.out.println("projection supprimer avec succé");
        afficher_projection();
    }

    @FXML
    private void modifier_projct(ActionEvent event) {
        Service_projection ps= new Service_projection();
        projection p =new projection();
        p.setNom(nom.getText());
        p.setGenre(genre.getText());
        p.setAge_recommande(Integer.parseInt(age.getText()) );
        p.setDuree(duree.getText());
     
       ps.modifier_projection(p, (nom.getText() ));
       System.out.println("projection modifier avec succé");
       afficher_projection();
    }

    @FXML
    private void rech_projection(ActionEvent event) {
         Service_projection ps = new Service_projection();
        ResultSet resultset=ps.getall_projection();
        
        ObservableList<projection> listprojection = FXCollections.observableArrayList(ps.recherche_projection((id_supp.getText())));
//        tb_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tb_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tb_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        tb_age.setCellValueFactory(new PropertyValueFactory<>("age_recommande"));
        tb_duree.setCellValueFactory(new PropertyValueFactory<>("duree"));
        tb_projection.setItems(listprojection);
    }   

    @FXML
    private void envoyer_email(ActionEvent event) throws MessagingException {
        String test;
        test=mail.getText();
        email.sendmail(test);
        
    }

    @FXML
    private void trier_projection(ActionEvent event) {
        Service_projection ps = new Service_projection();
        ResultSet resultset=ps.tri_projection();
        List<projection> pl = new ArrayList<projection>();
        try {
            while(resultset.next()){
                projection p1 = new projection();
                p1.setId(resultset.getInt("id"));
                p1.setNom(resultset.getString("nom"));
                p1.setGenre(resultset.getString("genre"));
                p1.setAge_recommande(resultset.getInt("age_recommande"));
                 p1.setDuree(resultset.getString("duree"));
               
                pl.add(p1);
                
               
                }
            ObservableList<projection> listprojection = FXCollections.observableArrayList(pl);
//        tb_id.setCellValueFactory(new PropertyValueFactory<>("id"));
        tb_nom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        tb_genre.setCellValueFactory(new PropertyValueFactory<>("genre"));
        tb_age.setCellValueFactory(new PropertyValueFactory<>("age_recommande"));
        tb_duree.setCellValueFactory(new PropertyValueFactory<>("duree"));
      
        tb_projection.setItems(listprojection);
        } catch (SQLException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    } 
    
    
    }

    

