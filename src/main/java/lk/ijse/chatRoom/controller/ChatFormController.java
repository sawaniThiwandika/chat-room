package lk.ijse.chatRoom.controller;


import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;
import com.vdurmont.emoji.EmojiParser;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.stage.FileChooser;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class ChatFormController {

    @FXML
    private Button cameraButton;

    @FXML
    private JFXTextArea chatList;

    @FXML
    private Button emojiIcon;

    @FXML
    private JFXTextArea textMessage;

    @FXML
    private Button voiceButton;

    @FXML
    private Button sendButton;
    @FXML
    private VBox vBox;
    @FXML
    private AnchorPane anchorPane;
    @FXML
    private ScrollPane scrollpane;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    Socket socket;
    ServerSocket serverSocket;
    String  user="";



    String path;

    GridPane gridPane = new GridPane();
    ScrollPane scrollPane=new ScrollPane(gridPane);



    public void initialize(String userName) throws SQLException, IOException {

        setClientName(userName);
        anchorPane.getChildren().add(scrollPane);
        scrollPane.setVisible(false);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try{
                    socket = new Socket("localhost", 3000);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    System.out.println("Client connected");
                    //ServerFormController.receiveMessage(clientName+" joined.");

                    while (socket.isConnected()){
                        String receivingMsg = dataInputStream.readUTF();
                        receiveMessage(receivingMsg,vBox);

                    }
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

        vBox.heightProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observableValue, Number oldValue, Number newValue) {
                scrollpane.setVvalue((Double) newValue);
            }
        });

        // emoji();

    }
    public static void receiveMessage(String msg, VBox vBox) throws IOException {
        if (msg.matches(".*\\.(png|jpe?g|gif)$")){
            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_LEFT);
            Text textName = new Text(msg.split("[-]")[0]);
            TextFlow textFlowName = new TextFlow(textName);
            hBoxName.getChildren().add(textFlowName);

            Image image = new Image(msg.split("[-]")[1]);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5,5,5,10));
            hBox.getChildren().add(imageView);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBoxName);
                    vBox.getChildren().add(hBox);
                }
            });

        }else {
            String name = msg.split("-")[0];
            String msgFromServer = msg.split("-")[1];

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBox.setPadding(new Insets(5,5,5,10));

            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_LEFT);

            Label textName = new Label(name);
            hBoxName.getChildren().add(textName);

            Label text = new Label(msgFromServer);
            text.setStyle("-fx-background-color: #abb8c3;-fx-font-size: 20;-fx-color: black; -fx-font-weight: bold; -fx-background-radius: 5px");
            text.setPadding(new Insets(5,10,5,10));

            hBox.getChildren().add(text);


            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBoxName);
                    vBox.getChildren().add(hBox);
                }
            });
        }
    }
    private void sendMsg(String msgToSend) {
        if (!msgToSend.isEmpty()){
            if (!msgToSend.matches(".*\\.(png|jpe?g|gif)$")){

                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 0, 10));
                Label labelText = new Label(msgToSend);
                labelText.setAlignment(Pos.CENTER_RIGHT);
                labelText.setPadding(new Insets(5, 10, 5, 10));
                labelText.setPadding(new Insets(5, 5, 0, 10));

                labelText.setStyle("-fx-background-color: #0693e3; -fx-font-size:20;-fx-text-fill: white; -fx-font-weight: bold; -fx-color: white; -fx-background-radius: 5px");
                hBox.getChildren().add(labelText);

                HBox hBoxTime = new HBox();
                hBoxTime.setAlignment(Pos.CENTER_RIGHT);
                hBoxTime.setPadding(new Insets(0, 5, 5, 10));
                String stringTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                Text time = new Text(stringTime);
                time.setStyle("-fx-font-size: 10");

                hBoxTime.getChildren().add(time);

                vBox.getChildren().add(hBox);
                vBox.getChildren().add(hBoxTime);
                vBox.setAlignment(Pos.TOP_LEFT);

                textMessage.clear();
            }
            else {
                HBox hBox = new HBox();
                hBox.setAlignment(Pos.CENTER_RIGHT);
                hBox.setPadding(new Insets(5, 5, 0, 10));
                ImageView imageview = new ImageView(msgToSend);
                imageview.setFitHeight(200);
                imageview.setFitWidth(200);
                hBox.getChildren().add(imageview);

                HBox hBoxTime = new HBox();
                hBoxTime.setAlignment(Pos.CENTER_RIGHT);
                hBoxTime.setPadding(new Insets(0, 5, 5, 10));
                String stringTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"));
                Text time = new Text(stringTime);
                time.setStyle("-fx-font-size: 10");

                hBoxTime.getChildren().add(time);

                vBox.getChildren().add(hBox);
                vBox.getChildren().add(hBoxTime);
                vBox.setAlignment(Pos.TOP_LEFT);

            }
            try {
                dataOutputStream.writeUTF(getClientName() + "-" + msgToSend);
                dataOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }



    private void loadAllChats() throws IOException {

    }

    @FXML
    void VoiceButtonOnAction(ActionEvent event) {

    }

    @FXML
    void cameraButtonOnAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            path = selectedFile.getAbsolutePath();
            sendMsg(path);
        } else {
            path=null;
        }

    }

    @FXML
    void emojiButtonOnAction(ActionEvent event) {

        if(!scrollPane.isVisible()){
            openEmojiBox();
        }
        else {
            scrollPane.setVisible(false);
        }


    }

    private String htmlToUnicode(String htmlEntity) {
        return EmojiParser.parseToUnicode(htmlEntity);


    }

    @FXML
    void sendButtonOnAction(ActionEvent event) throws IOException {
        sendMsg(textMessage.getText());

    }
    public void shutdown() {
        // cleanup code here...
        // ServerFormController.receiveMessage(clientName+" left.");
    }
    public  void openEmojiBox(){
        int x = 0;
        int y = 0;

        // GridPane gridPane = new GridPane();
        gridPane.setPrefSize(150, 300);
        gridPane.setLayoutX(200);
        gridPane.setLayoutY(200);
        gridPane.setStyle("-fx-font-size: 30");

        String[] emojies={"&#128512;","&#128513;","&#128514;","&#128515;","&#128516;","&#128517;","\uD83D\uDE00",
                "\uD83D\uDE01", "\uD83D\uDE02", "\uD83D\uDE03", "\uD83D\uDE04", "\uD83D\uDE05","&#8986;","\uD83D\uDE00",
                "\uD83D\uDE01",
                "\uD83D\uDE02",
                "\uD83D\uDE03",
                "\uD83D\uDE04",
                "\uD83D\uDE05",
                "\uD83D\uDE06",
                "\uD83D\uDE07",
                "\uD83D\uDE08",
                "\uD83D\uDE09",
                "\uD83D\uDE0A",
                "\uD83D\uDE0B",
                "\uD83D\uDE0C",
                "\uD83D\uDE0D",
                "\uD83D\uDE0E",
                "\uD83D\uDE0F",
                "\uD83D\uDE10",
                "\uD83D\uDE11",
                "\uD83D\uDE12",
                "\uD83D\uDE13" };



        for (int i = 0; i < emojies.length; i++) {
            JFXButton button = new JFXButton(htmlToUnicode(emojies[i]));
            button.setStyle("-fx-font-size: 15; -fx-text-fill: black; -fx-background-color: #F0F0F0; -fx-border-radius: 50");
            button.setOnAction(this::emojiButtonAction);

            if (x / 4 == 1) {
                x = 0;
                y = y + 1;
            }

            gridPane.add(button, x, y);
            x++;
        }


        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVisible(true);
    }
    private void emojiButtonAction(ActionEvent event) {
        JFXButton button = (JFXButton) event.getSource();
        textMessage.appendText(button.getText());
        scrollPane.setVisible(false);

    }

    public void setClientName(String text) {
        System.out.println("user "+text);
        user = text;

    }
    public String getClientName() {
       return user;

    }
}