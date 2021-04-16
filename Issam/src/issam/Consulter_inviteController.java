/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package issam;

import CnxBD.MyConnection;
import entities.Invite;
import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 * FXML Controller class
 *
 * @author Bassem
 */
public class Consulter_inviteController implements Initializable {

    public Button btnedit;
    @FXML
    private Button back;

    @FXML
    private TextField searchField;

    @FXML
    private TableView<Invite> tableView;

    @FXML
    private TableColumn<Invite, Integer> idCol;

    @FXML
    private TableColumn<Invite, String> firstNameCol;

    @FXML
    private TableColumn<Invite, String> lastNameCol;

    @FXML
    private TableColumn<Invite, String> emailCol;

    @FXML
    private TableColumn<Invite, String> careerCol;

    @FXML
    private TableColumn<Invite, String> typeCol;

    @FXML
    private TableColumn<Invite, String> dateCol;

    @FXML
    private TableColumn<Invite, String> numCol;

    @FXML
    private Button btnsupp;

    private MyConnection myConnection;


    private final ObservableList<Invite> invites = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        myConnection = new MyConnection();

        initColumns();
        loadData();
        checkUserType();

    }

    private void checkUserType() {

        try {
            String userType = null;

            String query = "SELECT user_type FROM user_accounts WHERE is_online = 1;";

            ResultSet resultSet = myConnection.execQuery(query);

            while (resultSet.next()){
                userType = resultSet.getString(1);
            }
            System.out.println("User Type : " + userType);

            assert userType != null;
            if (userType.equals("USER") || userType.equals("user")) {
                btnedit.setDisable(true);
                btnsupp.setDisable(true);
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }



    private void loadData() {

        invites.clear();

        String query = "SELECT * FROM invite";

        try {
            ResultSet set = myConnection.execQuery(query);
            while (set.next()) {
                int id = set.getInt(1);
                String first_name = set.getString(2);
                String last_name = set.getString(3);
                String email = set.getString(4);
                String career = set.getString(5);
                String type = set.getString(6);
                String date = String.valueOf(set.getDate(7));
                int num = set.getInt(8);

                invites.add(new Invite(id,first_name,last_name,email,career,type,date,num));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        tableView.setItems(null);
        tableView.setItems(invites);
    }


    // Defining Columns in table view
    private void initColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        careerCol.setCellValueFactory(new PropertyValueFactory<>("career"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
        dateCol.setCellValueFactory(new PropertyValueFactory<>("date"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("num"));
    }

    @FXML
    private void handleSearch() {
        searchField.textProperty().addListener(observable -> {
            if(searchField.textProperty().get().isEmpty()) {
                tableView.setItems(invites);
                return;
            }
            ObservableList<Invite> tableData = FXCollections.observableArrayList();
            ObservableList<TableColumn<Invite, ?>> cols = tableView.getColumns();
            for (Invite invite : invites) {
                if (cols.stream().map(col -> col.getCellData(invite).toString()).map(String::toLowerCase).anyMatch(cellValue -> cellValue.contains(searchField.textProperty().get().toLowerCase()))) {
                    tableData.add(invite);
                }
            }
            tableView.setItems(tableData);
        });
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
    private void fenedit() {
        Invite selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No invite selected");
            alert.setContentText("Please select a invite for edit.");
            alert.show();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Invite.fxml"));
            Parent parent = loader.load();

            InviteController controller = loader.getController();
            controller.infalteUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Invite");
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex,"Error in Editing Invite",ex.getLocalizedMessage());
        }
    }
    

    
    public void delete() {
        Invite selectedForRemove = tableView.getSelectionModel().getSelectedItem();
        if (selectedForRemove == null) {
            AlertMaker.showErrorMessage("No invite selected", "Please select a invite to delete.");
        }else {
            int inviteId = selectedForRemove.getId();
            System.out.println(inviteId);
            String query = "DELETE FROM invite WHERE id = '"+ inviteId +"';";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure to delete invite with ID : " + inviteId);
            Optional<ButtonType> answer = alert.showAndWait();

            if (answer.get().equals(ButtonType.OK)) {
                myConnection.execAction(query);
                AlertMaker.showSuccessNotification("Invite with ID '" + inviteId + "' have been deleted.");
            }else {
                AlertMaker.showErrorNotification("Invite with ID '" + inviteId + "' have not been deleted.");
            }
            invites.clear();
            loadData();
        }
       
    }
}

/*
private void fetColumnList() {

        try {
            ResultSet rs = cnx.createStatement().executeQuery(SQL);

            //SQL FOR SELECTING ALL OF CUSTOMER
              TableColumn test = new TableColumn("test");
            for (int i = 0; i < rs.getMetaData().getColumnCount(); i++) {
                //We are using non property style for making dynamic table
                final int j = i;
                TableColumn col = new TableColumn(rs.getMetaData().getColumnName(i + 1).toUpperCase());

                col.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<ObservableList, String>, ObservableValue<String>>() {
                    public ObservableValue<String> call(TableColumn.CellDataFeatures<ObservableList, String> param) {
                        return new SimpleStringProperty(param.getValue().get(j).toString());
                    }
                });
                table_invite.getColumns().removeAll(col);
                table_invite.getColumns().addAll(col);

                System.out.println("Column [" + i + "]");
                table_invite.setRowFactory(tv -> {

            // Define our new TableRow
            TableRow<Invite> row = new TableRow<>();
            btnsupp.setOnMouseClicked((MouseEvent event) -> {
            Object selectedItems = table_invite.getSelectionModel().getSelectedItems().get(0);
            String data1 = selectedItems.toString().split(",")[0].substring(1);
            try {
                delete(data1);
            } catch (SQLException ex) {
                Logger.getLogger(Consulter_inviteController.class.getName()).log(Level.SEVERE, null, ex);
            }
            });
            return row;
});

            }

        } catch (Exception e) {
            System.out.println("Error " + e.getMessage());

        }

    }

    private void fetRowList() {
        data = FXCollections.observableArrayList();
        ResultSet rs;
        try {
            rs = cnx.createStatement().executeQuery(SQL);

            while (rs.next()) {
                //Iterate Row
                ObservableList row = FXCollections.observableArrayList();
                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                    //Iterate Column
                    row.add(rs.getString(i));
                }
                System.out.println("Row [1] added " + row);
                data.add(row);

            }
            table_invite.setItems(data);
        } catch (SQLException ex) {
            System.err.println(ex.getMessage());
        }
    }
 */
