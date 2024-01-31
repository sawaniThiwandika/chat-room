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
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import lk.ijse.chatRoom.ClientHandler;
import lk.ijse.chatRoom.Dao.UserDao;
import lk.ijse.chatRoom.Dao.impl.UserDaoImpl;
import lk.ijse.chatRoom.Server;
import lk.ijse.chatRoom.bo.Impl.UserBoImpl;
import lk.ijse.chatRoom.bo.UserBo;
import lk.ijse.chatRoom.dto.UserDto;

import java.io.IOException;
import java.net.Socket;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;

public class LoginFormController {
    @FXML
    private AnchorPane loginPage;
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
    List<ClientHandler> clientList;
    Server server;
    @FXML
    void forgetPasswordOnAction(ActionEvent event) {

    }

    public void initialize() throws SQLException, IOException {
        new Thread(() -> {
            try {
              server = Server.getInstance();
                server.makeSocket();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

    }

    @FXML
    void loginButtonOnAction(ActionEvent event) throws IOException, SQLException {
        boolean userNamePassword;

        if (!txtUserName.getText().isEmpty()) {

                userNamePassword =server.checkUser (txtUserName.getText(), txtPassword.getText());

            if (userNamePassword) {
                Stage primaryStage = new Stage();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/view/chat_form.fxml"));
                Parent root = fxmlLoader.load();
                ChatFormController controller = fxmlLoader.getController();
                System.out.println("controller " + controller);
                controller.initialize(txtUserName.getText());

                primaryStage.setScene(new Scene(root));
                primaryStage.setTitle(txtUserName.getText());
                primaryStage.setResizable(true);
                primaryStage.centerOnScreen();
                primaryStage.setOnCloseRequest(windowEvent -> {
                    controller.shutdown();
                });
                primaryStage.show();

                txtUserName.clear();
            } else {
                new Alert(Alert.AlertType.ERROR, "Incorrect").show();
            }
        } else {
            new Alert(Alert.AlertType.ERROR, "Please enter your name").show();
        }
    }


    @FXML
    void registerButtonOnAction(ActionEvent event) throws IOException {
        AnchorPane anchorPane = FXMLLoader.load(getClass().getResource("/view/add_user_form.fxml"));
        Scene scene = new Scene(anchorPane);
        Stage stage = (Stage) loginPage.getScene().getWindow();
        stage.setScene(scene);
        stage.setTitle("add user");
        stage.centerOnScreen();
    }


}
