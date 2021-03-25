/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import connexionbd.connexion;
import entity.article;
import entity.projection;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import service.Service_article;
import service.Service_projection;

/**
 * FXML Controller class
 *
 * @author FK Info
 */
public class ArticleController implements Initializable {

    @FXML
    private ComboBox<String> box;
    @FXML
    private TextField titre;
    @FXML
    private TextField description;
    @FXML
    private Button ajout_article;
    @FXML
    private ListView<String> liste_article;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        afficher_film();
        //afficher_article();
        test();
    }    
 public void afficher_film()
 {
      ObservableList<String> combo= FXCollections.observableArrayList();
       try {
           
            String consulta = "select nom from projection";
         Connection c = connexion.getinstance().getConn();
            PreparedStatement ps =c.prepareStatement(consulta);
              ResultSet rs = ps.executeQuery();
               
              while ( rs.next() ) 
             {  
               combo.add(rs.getString("nom"));
                 
               
             }
               
           box.setItems(combo);
            
        } catch (SQLException e) {
        }
        
    }

    @FXML
    private void ajouter_article(ActionEvent event) {
         Service_article as= new Service_article();
        article a =new article();
        //p.setId(Integer.parseInt(id.getText()) );
       
        a.setTitre(titre.getText());
        a.setDescription(description.getText());
        a.setNom_film(box.getValue());
       as.ajouter_article(a);
       System.out.println("article ajouter avec succé");
      //afficher_article();
      test();
    
    }
    public void afficher_article()
    {
        Service_article as = new Service_article();
        ResultSet resultset=as.getall_article();
        List<article> al = new ArrayList<article>();
        try {
            while(resultset.next()){
                article a1 = new article();
                a1.setId(resultset.getInt("id"));
                a1.setTitre(resultset.getString("titre"));
                a1.setDescription(resultset.getString("description"));
                
                a1.setNom_film(resultset.getString("nom_film"));
               
                al.add(a1);
                
               
                }
            ObservableList<article> listarticle = FXCollections.observableArrayList(al);
        //tb_id.setCellValueFactory(new PropertyValueFactory<>("id"));
       
      
       //liste_article.setItems(listarticle);
        
      
        } catch (SQLException ex) {
            Logger.getLogger(ViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    public void test()
    {
        ObservableList<String> list= FXCollections.observableArrayList();
       try {
           
            String consulta = "select * from article";
         Connection c = connexion.getinstance().getConn();
            PreparedStatement ps =c.prepareStatement(consulta);
              ResultSet rs = ps.executeQuery();
               
              while ( rs.next() ) 
             {  
               list.add(rs.getString("titre"));
               list.add(rs.getString("description"));
               list.add(rs.getString("nom_film"));
                 
               
             }
               
           liste_article.setItems(list);
            
        } catch (SQLException e) {
        }
        
        
    }
            
            
 }

