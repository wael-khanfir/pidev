
package CnxBD;

import javafx.scene.control.Alert;

import java.sql.*;

/**
 *
 * @author wadie
 */
public class MyConnection {

    private static Connection con = null;
    private static Statement stmt = null;


    // Credentials, It ll create database if not exists. so no need to create one.

    public static String url="jdbc:mysql://localhost:3306/pidev_issam";
    public static String user="root";
    public static String pwd="";

    // Default Constructor
    public MyConnection() {
        createConnection();
        setupInviteTable();
        setupReservationTable();
        setupInterviewTable();
        setupUserTable();
    }

    public static void main(String[] args) {

    }

    // Method to create Connection
    private void createConnection() {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url,user,pwd);
            System.out.println("Connection established...");
        } catch (ClassNotFoundException | SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Database Not Found. Please Check if server's running or not?");
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // Method to execute CRUD operations like Select etc
    public ResultSet execQuery(String qu){
        ResultSet resultSet;
        try {
            stmt = con.createStatement();
            resultSet = stmt.executeQuery(qu);
        } catch (SQLException e) {
            System.out.println("Exception at execQuery:database " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return null;
        }
        return resultSet;
    }

    // Method to execute CRUD operations like Insert, Update, Delete
    public boolean execAction(String query){
        try {
            stmt = con.createStatement();
            stmt.execute(query);
            return true;
        } catch (SQLException e) {
            System.out.println("Exception at execAction:database " + e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
            return false;
        }
    }

    // User database table creation
    private void setupInviteTable() {
        String TABLE_NAME = "INVITE";
        try {
            stmt = con.createStatement();
            java.sql.DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null,null,TABLE_NAME.toUpperCase(),null);
            if (tables.next()){
                System.out.println("Table " + TABLE_NAME + " already exists");
            }else {
                String query = "create table " + TABLE_NAME +
                        "(\n" +
                        "id int auto_increment,\n" +
                        "first_name varchar(50) not null,\n" +
                        "last_name varchar(50) not null,\n" +
                        "email varchar(50) not null,\n" +
                        "career varchar(50) not null,\n" +
                        "interview_type varchar(50) not null ,\n" +
                        "interview_date date,\n" +
                        "appointed_num int ,\n" +
                        "primary key (id)\n" +
                        ");";
                stmt.execute(query);
                stmt.close();
                System.out.println("Table " + TABLE_NAME + " created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // Reservation database table creation
    private void setupReservationTable() {
        String TABLE_NAME = "RESERVATION";
        try {
            stmt = con.createStatement();
            java.sql.DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null,null,TABLE_NAME.toUpperCase(),null);
            if (tables.next()){
                System.out.println("Table " + TABLE_NAME + " already exists");
            }else {
                String query = "create table " + TABLE_NAME +
                        "(\n" +
                        "id int auto_increment,\n" +
                        "first_name varchar(50) not null,\n" +
                        "last_name varchar(50) not null,\n" +
                        "email varchar(100) not null,\n" +
                        "appointed_num int ,\n" +
                        "primary key (id)\n" +
                        ");";
                stmt.execute(query);
                stmt.close();
                System.out.println("Table " + TABLE_NAME + " created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // Interview database table creation
    private void setupInterviewTable() {
        String TABLE_NAME = "INTERVIEW";
        try {
            stmt = con.createStatement();
            java.sql.DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null,null,TABLE_NAME.toUpperCase(),null);
            if (tables.next()){
                System.out.println("Table " + TABLE_NAME + " already exists");
            }else {
                String query = "create table " + TABLE_NAME +
                        "(\n" +
                        "id int auto_increment,\n" +
                        "nom_invite varchar(50) not null,\n" +
                        "appointed_num int ,\n" +
                        "interview_type varchar(50) not null,\n" +
                        "primary key (id)\n" +
                        ");";
                stmt.execute(query);
                stmt.close();
                System.out.println("Table " + TABLE_NAME + " created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }

    // User database table creation
    private void setupUserTable() {
        String TABLE_NAME = "USER_ACCOUNTS";
        try {
            stmt = con.createStatement();
            java.sql.DatabaseMetaData dbm = con.getMetaData();
            ResultSet tables = dbm.getTables(null,null,TABLE_NAME.toUpperCase(),null);
            if (tables.next()){
                System.out.println("Table " + TABLE_NAME + " already exists");
            }else {
                String query = "create table " + TABLE_NAME +
                        "(\n" +
                        "id int auto_increment,\n" +
                        "full_name varchar(50) not null,\n" +
                        "username varchar(50) not null ,\n" +
                        "password varchar(50) not null,\n" +
                        "email varchar(50) not null,\n" +
                        "user_type varchar(50) not null,\n" +
                        "is_online boolean default 0,\n" +
                        "primary key (id)\n" +
                        ");";
                stmt.execute(query);
                stmt.close();
                System.out.println("Table " + TABLE_NAME + " created.");
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText(e.getMessage());
            alert.showAndWait();
        }
    }




}

/*
Old Code


public Myconnection(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            cnx=DriverManager.getConnection(url,login,pwd);
            System.out.println("connecton etablie "+url);
        } catch (SQLException | ClassNotFoundException ex) {
            System.out.println(ex.getMessage());
            System.out.println("connection non etablie");
        }
    }

    //design pattern singleton
     public static Connection getConnection(){
     return cnx;
     }


    public static Myconnection getIstance(){
    if (myc==null){
    myc=new Myconnection();
    }
    return myc;
    }

 */
