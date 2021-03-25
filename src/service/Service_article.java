/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package service;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import connexionbd.connexion;
import entity.article;
import entity.projection;
import java.sql.ResultSet;
/**
 *
 * @author FK Info
 */
public class Service_article {
     Connection c = connexion.getinstance().getConn();
    
     public void ajouter_article(article a)
     {
         PreparedStatement req;
        try {
            req = c.prepareStatement("insert into article(titre,description,nom_film)values(?,?,?)");
        
         req.setString(1, a.getTitre());
         req.setString(2, a.getDescription());
           req.setString(3, a.getNom_film());
        
     req.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Service_article.class.getName()).log(Level.SEVERE, null, ex);
        }
     
}
      public ResultSet getall_article() {
         
        try {
            PreparedStatement req = c.prepareStatement("SELECT titre FROM article");
            ResultSet rs = req.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
      return null;
    }
    public boolean supprimer_article(article a) {
         boolean ok=false;
        try {
            
            PreparedStatement req = c.prepareStatement("delete from article where titre = ? ");
            req.setString(1, a.getTitre());
            req.executeUpdate();
ok=true;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return ok;
    }
    public void modifier_projection(article a ,String titre) {
        try {
            PreparedStatement req = c.prepareStatement("update article set titre=?,description=?,nom_film=? where titre=?");
            
            req.setString(1, a.getTitre());
            req.setString(2, a.getDescription());
            req.setString(3, a.getNom_film());
            req.setString(4,titre);
          
            req.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    
}
