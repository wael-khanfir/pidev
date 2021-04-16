package issam;


import CnxBD.MyConnection;
import entities.UsersModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AddUserController implements Initializable {

    @FXML
    private Label headerLabel;

    @FXML
    private ComboBox<String> userTypeCB;

    @FXML
    private TextField fullNameTF;

    @FXML
    private TextField usernameTF;

    @FXML
    private PasswordField passwordTF;

    @FXML
    private TextField emailTF;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private MyConnection myconnection;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        myconnection = new MyConnection();

    }

    @FXML
    void handleCancel() {
        cancelButton.getScene().getWindow().hide();
    }

    private boolean validateEmail(){
        Pattern pattern = Pattern.compile("[a-zA-Z0-9][a-zA-Z0-9._]*@[a-zA-Z0-9]+([.][a-zA-Z]+)+");
        Matcher matcher = pattern.matcher(emailTF.getText());
        if (matcher.find() & matcher.group().equals(emailTF.getText())){
            return true;
        }else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please Check Email.");
            alert.showAndWait();
            return false;
        }

    }

    @FXML
    void handleSave() {
        String fullName = fullNameTF.getText();
        String username = usernameTF.getText();
        String password = passwordTF.getText();
        String email = emailTF.getText();
        String type = userTypeCB.getSelectionModel().getSelectedItem();

        boolean flag = fullName.isEmpty() || username.isEmpty() || password.isEmpty() || email.isEmpty() || type.isEmpty();
        if (flag) {
            AlertMaker.showErrorMessage("Cant process user", "Please Enter in all fields");
            return;
        }

        if (validateEmail()){
            if(saveButton.getText().equals("Save")) {
                String insertQuery = "INSERT INTO user_accounts(full_name, username, password, email,user_type)\n" +
                        "VALUES ('" + fullName + "','" + username + "','" + password + "','" + email + "','" + type + "'); ";

                if (myconnection.execAction(insertQuery)) {
                    AlertMaker.showSuccessNotification("User has been inserted successfully.");
                    System.out.println("Success in insert");
                    clearFields();
                } else {
                    AlertMaker.showErrorNotification("User has not been inserted.");
                    System.out.println("Insertion Failed");
                }
            }else if (saveButton.getText().equals("Update")){
                String updateQuery = "UPDATE user_accounts " +
                        "SET full_name = '" + fullName + "', password = '"+ password +"'," +
                        "email = '" + email + "', user_type = '" + type + "' " +
                        "WHERE username = '" + username + "';";
                if (myconnection.execAction(updateQuery)) {
                    AlertMaker.showSuccessNotification("User has been updated successfully.");
                    System.out.println("Success in update");
                    clearFields();
                } else {
                    AlertMaker.showErrorNotification("User has not been updated.");
                    System.out.println("Update Failed");
                }
            }
        }



    }

    void infalteUI(UsersModel users) {
        fullNameTF.setText(users.getFullName());
        usernameTF.setText(users.getUsername());
        passwordTF.setText(users.getPassword());
        emailTF.setText(users.getEmail());
        userTypeCB.setValue(users.getType());
        headerLabel.setText("Edit Category");
        saveButton.setText("Update");
    }

    private void clearFields() {
        fullNameTF.clear();
        usernameTF.clear();
        passwordTF.clear();
        emailTF.clear();
        userTypeCB.setValue("");
    }

}

