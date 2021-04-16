package issam;

import CnxBD.MyConnection;
import entities.UsersModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
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

public class UsersController implements Initializable {

    public Button backButton;
    @FXML
    private TextField searchField;

    @FXML
    private TableView<UsersModel> tableView;

    @FXML
    private TableColumn<UsersModel, Integer> idCol;

    @FXML
    private TableColumn<UsersModel, String> fullNameCol;

    @FXML
    private TableColumn<UsersModel, String> usernameCol;

    @FXML
    private TableColumn<UsersModel, String> passwordCol;

    @FXML
    private TableColumn<UsersModel, String> emailCol;

    @FXML
    private TableColumn<UsersModel, String> typeCol;
    
    private MyConnection myconnection;

    private final ObservableList<UsersModel> usersList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myconnection = new MyConnection();
        initCol();
        loadData();

    }

    private void initCol() {
        idCol.setCellValueFactory(new PropertyValueFactory<>("userID"));
        fullNameCol.setCellValueFactory(new PropertyValueFactory<>("fullName"));
        usernameCol.setCellValueFactory(new PropertyValueFactory<>("username"));
        passwordCol.setCellValueFactory(new PropertyValueFactory<>("password"));
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        typeCol.setCellValueFactory(new PropertyValueFactory<>("type"));
    }

    private void loadData() {
        String query = "SELECT * FROM user_accounts;";

        ResultSet rs = myconnection.execQuery(query);

        try {
            while (rs.next()){
                int id = rs.getInt(1);
                String fullName = rs.getString(2);
                String username = rs.getString(3);
                String password = rs.getString(4);
                String emailAddress = rs.getString(5);
                String type = rs.getString(6);

                usersList.add(new UsersModel(id,fullName,username,password,emailAddress,type));
            }
        } catch (SQLException e) {
            AlertMaker.showErrorMessage(e);
        }
        idCol.setSortType(TableColumn.SortType.ASCENDING);
        tableView.setItems(null);
        tableView.setItems(usersList);
        tableView.getSortOrder().add(idCol);
    }

    @FXML
    void handleAdd() {
        try {
            loadWindow("/issam/addUser.fxml", false,false);
        } catch (IOException e) {
            AlertMaker.showErrorMessage(e);
        }

    }

    @FXML
    private void handleBack() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("Menu.fxml"));
            Stage stage = (Stage) backButton.getScene().getWindow();
            stage.close();
            Scene scene = new Scene(root);

            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(Menu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }


    public void loadWindow(String filePath, boolean resize, boolean max) throws IOException {
        Stage stage = new Stage(StageStyle.DECORATED);
        Parent root = FXMLLoader.load(getClass().getResource(filePath));
        stage.setTitle("Issam");
        stage.setResizable(resize);
        stage.setMaximized(max);
        stage.setOnCloseRequest(event -> {
            if (!event.isConsumed()) {
                System.out.println("Yes");
                return;
            } else {
                System.out.println("No");
            }
        });
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    @FXML
    void handleDelete() {

        try {
            UsersModel model = tableView.getSelectionModel().getSelectedItem();
            if (model == null){
                AlertMaker.showWarningMessage("No User Selected !!!","Please Select User from table to delete.");
            }else {
                String usernameDB = null;
                int id = model.getUserID();
                System.out.println(id);
                String query = "DELETE FROM user_accounts WHERE id = '" + id + "'";

                String query1 = "SELECT username FROM user_accounts WHERE is_online = 1;";
                ResultSet resultSet = myconnection.execQuery(query1);
                while (resultSet.next()){
                    usernameDB = resultSet.getString(1);
                }
                assert usernameDB != null;
                if (usernameDB.equals(model.getUsername())){
                    AlertMaker.showErrorMessage("User error","This user can not be deleted.");
                }else {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setContentText("Are you sure to delete User with ID : " + id);
                    Optional<ButtonType> answer = alert.showAndWait();

                    if (answer.get().equals(ButtonType.OK)) {
                        myconnection.execAction(query);
                        AlertMaker.showSuccessNotification("User with ID '" + id + "' have been deleted.");
                    }else {
                        AlertMaker.showErrorNotification("User with ID '" + id + "' have not been deleted.");
                    }
                    usersList.clear();
                    loadData();
                }



            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


    }

    @FXML
    void handleEdit() {

        tableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                tableView.getSelectionModel().clearSelection();
            }
        });

        UsersModel selectedForEdit = tableView.getSelectionModel().getSelectedItem();
        if (selectedForEdit == null) {
            AlertMaker.showErrorMessage("No User selected", "Please select a User for edit.");
            return;
        }
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/mimikhan/uiDesgin/fxmls/addUser.fxml"));
            Parent parent = loader.load();

            AddUserController controller = loader.getController();
            controller.infalteUI(selectedForEdit);

            Stage stage = new Stage(StageStyle.DECORATED);
            stage.getIcons().add(new Image("/org/mimikhan/resources/icons/flower.png"));
            stage.setTitle("URBAN FLORIST");
            stage.setScene(new Scene(parent));
            stage.show();

            stage.setOnCloseRequest((e) -> {
                handleRefresh(new ActionEvent());
            });

        } catch (IOException ex) {
            AlertMaker.showErrorMessage(ex, "Error in Editing Category", ex.getLocalizedMessage());
        }

    }

    @FXML
    void handleRefresh(ActionEvent event) {
        searchField.clear();
        usersList.clear();
        System.out.println("List Clear");
        loadData();
        System.out.println("Data Loaded");

    }

    @FXML
    void handleSearch() {
        searchField.textProperty().addListener(observable -> {
            if (searchField.textProperty().get().isEmpty()) {
                tableView.setItems(usersList);
                return;
            }
            ObservableList<UsersModel> tableItems = FXCollections.observableArrayList();
            ObservableList<TableColumn<UsersModel, ?>> cols = tableView.getColumns();
            for (UsersModel usersModel : usersList) {

                for (TableColumn col : cols) {
                    String cellValue = col.getCellData(usersModel).toString();
                    cellValue = cellValue.toLowerCase();
                    if (cellValue.contains(searchField.textProperty().get().toLowerCase())) {
                        tableItems.add(usersModel);
                        break;
                    }
                }
            }
            tableView.setItems(tableItems);
        });

    }

}
