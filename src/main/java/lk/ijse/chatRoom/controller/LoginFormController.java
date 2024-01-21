package lk.ijse.chatRoom.controller;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import lk.ijse.chatRoom.Dao.UserDao;
import lk.ijse.chatRoom.Dao.impl.UserDaoImpl;
import lk.ijse.chatRoom.Server;
import lk.ijse.chatRoom.bo.Impl.UserBoImpl;
import lk.ijse.chatRoom.bo.UserBo;
import lk.ijse.chatRoom.dto.UserDto;

import java.io.IOException;
import java.net.Socket;
import java.sql.SQLException;

public class LoginFormController {

    @FXML
    private Button forgetPasswordButton;

    @FXML
    private JFXButton loginButton;

    @FXML
    private JFXButton registerBtn;

    @FXML
    private TextField txtPassword;

    @FXML
    private TextField txtUserName;
    UserBo userBo=new UserBoImpl();

    @FXML
    void forgetPasswordOnAction(ActionEvent event) {

    }
    public void initialize() throws SQLException, IOException{
        new Thread(() -> {
            try {
                Server server = Server.getInstance();
                server.makeSocket();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @FXML
    void loginButtonOnAction(ActionEvent event) throws IOException {
        boolean userNamePassword;

        if (!txtUserName.getText().isEmpty()) {
            try {
                userNamePassword = userBo.checkUserNamePassword(new UserDto(txtUserName.getText(), txtPassword.getText()));
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            if (userNamePassword) {
                Stage primaryStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/chat_form.fxml"));

                ChatFormController controller = new ChatFormController();
                controller.setClientName(txtUserName.getText());

                primaryStage.setScene(new Scene(fxmlLoader.load()));
                primaryStage.setTitle(txtUserName.getText());
                primaryStage.setResizable(true);
                primaryStage.centerOnScreen();
                primaryStage.setOnCloseRequest(windowEvent -> {
                    controller.shutdown();
                });
                primaryStage.show();

                txtUserName.clear();
            }
            else {
                new Alert(Alert.AlertType.ERROR, "Incorrect").show();
            }
        }
        else {
            new Alert(Alert.AlertType.ERROR, "Please enter your name").show();
        }
    }


    @FXML
    void registerButtonOnAction(ActionEvent event) {

    }


}
