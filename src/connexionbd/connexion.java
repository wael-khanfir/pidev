/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package connexionbd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class connexion {

    private static String url = "jdbc:mysql://127.0.0.1:3306/pidev";
    private static String user = "root";
    private static String password = "";
    private static Connection conn;

    private static connexion inst;

    public static Object getInstance() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }

    private connexion() {
        try {
            conn = DriverManager.getConnection(url, user, password);
            System.out.println("connexion etablie");
        } catch (SQLException e) {
            System.out.println("erreur");
        }
    }

    public static connexion getinstance() {
        if (inst == null) {
            inst = new connexion();

        }
        return inst;

    }

    public Connection getConn() {
        return conn;
    }
}
