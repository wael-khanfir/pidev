/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package issam;

import CnxBD.MyConnection;
import entities.Reservation;
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

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * FXML Controller class
 *
 * @author Bassem
 */
public class Consulter_reservationController implements Initializable {

    @FXML
    private TableView<Reservation> tableView;

    @FXML
    private TableColumn<Reservation, Integer> idCol;

    @FXML
    private TableColumn<Reservation, String> firstNameCol;

    @FXML
    private TableColumn<Reservation, String> lastNameCol;

    @FXML
    private TableColumn<Reservation, String> emailCol;

    @FXML
    private TableColumn<Reservation, Integer> numCol;

    @FXML
    private TextField searchField;

    @FXML
    private Button back;


    private MyConnection myConnection;


    private final ObservableList<Reservation> reservations = FXCollections.observableArrayList();
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        myConnection = new MyConnection();

        initColumns();
        loadData();
    }

    private void loadData() {

        reservations.clear();

        String query = "SELECT * FROM reservation";

        try {
            ResultSet set = myConnection.execQuery(query);
            while (set.next()) {
                int id = set.getInt(1);
                String first_name = set.getString(2);
                String last_name = set.getString(3);
                String email = set.getString(4);
                int appointed_num = set.getInt(5);

                reservations.add(new Reservation(id,first_name,last_name,email,appointed_num));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        tableView.setItems(null);
        tableView.setItems(reservations);
    }

    private void initColumns() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCol.setCellValueFactory(new PropertyValueFactory<>("first_name"));
        lastNameCol.setCellValueFactory(new PropertyValueFactory<>("last_name"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        numCol.setCellValueFactory(new PropertyValueFactory<>("appointed_num"));
    }

    @FXML
    private void handleSearch() {
        searchField.textProperty().addListener(observable -> {
            if(searchField.textProperty().get().isEmpty()) {
                tableView.setItems(reservations);
                return;
            }
            ObservableList<Reservation> tableData = FXCollections.observableArrayList();
            ObservableList<TableColumn<Reservation, ?>> cols = tableView.getColumns();
            for (Reservation reservation : reservations) {
                if (cols.stream().map(col -> col.getCellData(reservation).toString()).map(String::toLowerCase).anyMatch(cellValue -> cellValue.contains(searchField.textProperty().get().toLowerCase()))) {
                    tableData.add(reservation);
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
        Reservation selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("No reservation selected");
            alert.setContentText("Please select a reservation for edit.");
            alert.show();
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("reservation.fxml"));
            Parent parent = loader.load();

            ReservationController controller = loader.getController();
            controller.infalteUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.setTitle("Edit Reservation");
            stage.setScene(new Scene(parent));
            stage.show();

        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex,"Error in Editing Reservations",ex.getLocalizedMessage());
        }
    }
    

    
    public void delete() {
        Reservation selectedForRemove = tableView.getSelectionModel().getSelectedItem();
        if (selectedForRemove == null) {
            AlertMaker.showErrorMessage("No reservation selected", "Please select a reservation to delete.");
        }else {
            int reservationId = selectedForRemove.getId();
            System.out.println(reservationId);
            String query = "DELETE FROM reservation WHERE id = '"+ reservationId +"';";
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setContentText("Are you sure to delete reservation with ID : " + reservationId);
            Optional<ButtonType> answer = alert.showAndWait();

            if (answer.get().equals(ButtonType.OK)) {
                myConnection.execAction(query);
                AlertMaker.showSuccessNotification("Reservation with ID '" + reservationId + "' have been deleted.");
            }else {
                AlertMaker.showErrorNotification("Reservation with ID '" + reservationId + "' have not been deleted.");
            }
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
