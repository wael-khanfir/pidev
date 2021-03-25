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
import entity.projection;
import java.sql.ResultSet;

/**
 *
 * @author FK Info
 */
public class Service_projection {
    Connection c = connexion.getinstance().getConn();
    
     public void ajouter_projection(projection p)
     {
         PreparedStatement req;
        try {
            req = c.prepareStatement("insert into projection(nom,genre,age_recommande,duree)values(?,?,?,?)");
        
         req.setString(1, p.getNom());
         req.setString(2, p.getGenre());
         req.setInt(3, p.getAge_recommande());
         req.setString(4, p.getDuree());
     req.execute();
        } catch (SQLException ex) {
            Logger.getLogger(Service_projection.class.getName()).log(Level.SEVERE, null, ex);
        }
     
}
      public ResultSet getall_projection() {
         
        try {
            PreparedStatement req = c.prepareStatement("SELECT * FROM projection");
            ResultSet rs = req.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
      return null;
    }
    public boolean supprimer_projection(projection p) {
         boolean ok=false;
        try {
            
            PreparedStatement req = c.prepareStatement("delete from projection where nom = ? ");
            req.setString(1, p.getNom());
            req.executeUpdate();
ok=true;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return ok;
    }
    public void modifier_projection(projection p ,String nom) {
        try {
            PreparedStatement req = c.prepareStatement("update projection set nom=?,genre=? ,age_recommande=?,duree=? where nom=?");
            
            req.setString(1, p.getNom());
            req.setString(2, p.getGenre());
            req.setInt(3, p.getAge_recommande());
            req.setString(4, p.getDuree());
            req.setString(5,nom);
          
            req.executeUpdate();
        } catch (SQLException ex) {
            System.out.println(ex);
        }
    }
    public projection recherche_projection(String nom) {
        projection p = new projection();
        try {
            PreparedStatement req = c.prepareStatement("select * from projection where nom=?  ");
            req.setString(1, nom);
            ResultSet rs = req.executeQuery();
            rs.next();
            p.setId(rs.getInt("id"));
            p.setNom(rs.getString("nom"));
            p.setGenre(rs.getString("genre"));
            p.setAge_recommande(rs.getInt("age_recommande"));
            p.setDuree(rs.getString("duree"));
           
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return p;
    }
    public ResultSet tri_projection() {
         
        try {
            PreparedStatement req = c.prepareStatement("SELECT * FROM projection ORDER BY duree");
            ResultSet rs = req.executeQuery();
            return rs;
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        return null;
}
}