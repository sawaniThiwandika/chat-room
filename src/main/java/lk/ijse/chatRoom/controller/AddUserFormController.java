package lk.ijse.chatRoom.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.chatRoom.bo.Impl.UserBoImpl;
import lk.ijse.chatRoom.bo.UserBo;
import lk.ijse.chatRoom.dto.UserDto;

import java.io.IOException;
import java.sql.SQLException;

public class AddUserFormController {

    @FXML
    private Button addBtn;

    @FXML
    private AnchorPane addUser;

    @FXML
    private Button backBtn;

    @FXML
    private TextField txtConfirmPassword;


    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;
    UserBo userBo=new UserBoImpl();
    @FXML
    void addButtonOnAction(ActionEvent event) {
        String userName = txtUserName.getText();
        String password = txtPassword.getText();
        String confirm=txtConfirmPassword.getText();


        if(txtUserName.getText().isEmpty()||txtConfirmPassword.getText().isEmpty()||txtPassword.getText().isEmpty()){
            new Alert(Alert.AlertType.ERROR,"Empty fields").show();

        }

        else {

                boolean equals = txtPassword.getText().equals(txtConfirmPassword.getText());
                if (equals) {
                    UserDto newUser=new UserDto(txtUserName.getText(),txtPassword.getText());
                    boolean isSaved = false;
                    try {
                        isSaved = userBo.saveUser(newUser);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    if (isSaved){
                        new Alert(Alert.AlertType.CONFIRMATION, "success").show();
                    }


                } else {
                    new Alert(Alert.AlertType.ERROR, "password invalid...").show();
                }

        }
    }


    @FXML
    void backButtonOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/login_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) addUser.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("login");
        stage.centerOnScreen();
    }


}
