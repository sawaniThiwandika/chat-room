package lk.ijse.chatRoom.controller;


import com.google.protobuf.StringValue;
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
    @FXML
    private Button sinhalaBtn;
    DataOutputStream dataOutputStream;
    DataInputStream dataInputStream;
    Socket socket;
    ServerSocket serverSocket;
    String user = "";


    String path;

    GridPane gridPaneEmoji = new GridPane();
    GridPane gridPaneSinhala = new GridPane();
    ScrollPane scrollPane = new ScrollPane();




    public void initialize(String userName) throws SQLException, IOException {

        setClientName(userName);
        anchorPane.getChildren().add(scrollPane);
        scrollPane.setVisible(false);
        new Thread(new Runnable() {

            @Override
            public void run() {
                try {
                    socket = new Socket("localhost", 3000);
                    dataInputStream = new DataInputStream(socket.getInputStream());
                    dataOutputStream = new DataOutputStream(socket.getOutputStream());
                    System.out.println("Client connected");
                    //ServerFormController.receiveMessage(clientName+" joined.");

                    while (socket.isConnected()) {
                        String receivingMsg = dataInputStream.readUTF();
                        receiveMessage(receivingMsg,vBox);

                    }
                } catch (IOException e) {
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
    }

    public  void receiveMessage(String msg, VBox vBox) throws IOException {
        if (msg.matches(".*\\.(png|jpe?g|gif)$")) {
            HBox hBoxName = new HBox();
            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            hBoxName.setAlignment(Pos.CENTER_LEFT);
            String textName = msg.split("[-]")[0];

            if (getClientName().equals(textName)){
                hBoxName.setAlignment(Pos.CENTER_RIGHT);
                hBox.setAlignment(Pos.CENTER_RIGHT);
                textName="Me";

            }
            Label nameLabel = new Label(textName);
            hBoxName.getChildren().add(nameLabel);

            Image image = new Image(msg.split("[-]")[1]);
            ImageView imageView = new ImageView(image);
            imageView.setFitHeight(200);
            imageView.setFitWidth(200);


            hBox.setPadding(new Insets(5, 5, 5, 10));
            hBox.getChildren().add(imageView);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    vBox.getChildren().add(hBoxName);
                    vBox.getChildren().add(hBox);
                }
            });

        } else {
            String name = msg.split("-")[0];
            String msgFromServer = msg.split("-")[1];

            HBox hBox = new HBox();
            hBox.setAlignment(Pos.CENTER_LEFT);
            HBox hBoxName = new HBox();
            hBoxName.setAlignment(Pos.CENTER_LEFT);
            Label textName = new Label();
            Label text = new Label(msgFromServer);
            text.setStyle("-fx-background-color: #abb8c3;-fx-font-size: 20;-fx-color: black; -fx-font-weight: bold; -fx-background-radius: 5px");

            if (getClientName().equals(name)){
                hBoxName.setAlignment(Pos.CENTER_RIGHT);
                hBox.setAlignment(Pos.CENTER_RIGHT);
                text.setStyle("-fx-background-color: #0693e3; -fx-font-size:20;-fx-text-fill: white; -fx-font-weight: bold; -fx-color: white; -fx-background-radius: 5px");
                name="Me";
            }

            textName.setText(name);
            hBoxName.getChildren().add(textName);
            hBox.setPadding(new Insets(5, 5, 5, 10));
            text.setPadding(new Insets(5, 10, 5, 10));

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
        if (!msgToSend.isEmpty()) {
            if (!msgToSend.matches(".*\\.(png|jpe?g|gif)$")) {

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
            } else {
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
            path = null;
        }

    }

    @FXML
    void emojiButtonOnAction(ActionEvent event) {

        if (!scrollPane.isVisible()) {
            openEmojiBox();
        } else {
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

    public void openEmojiBox() {
        int x = 0;
        int y = 0;

        // GridPane gridPane = new GridPane();
        gridPaneEmoji.setPrefSize(400, 400);
        gridPaneEmoji.setLayoutX(200);
        gridPaneEmoji.setLayoutY(200);
        gridPaneEmoji.setStyle("-fx-font-size: 30");

        String[] emojies = {"&#128512;", "&#128513;", "&#128514;", "&#128515;", "&#128516;", "&#128517;", "\uD83D\uDE00",
                "\uD83D\uDE01", "\uD83D\uDE02", "\uD83D\uDE03", "\uD83D\uDE04", "\uD83D\uDE05", "&#8986;", "\uD83D\uDE00",
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
                "\uD83D\uDE13"};


        for (int i = 0; i < emojies.length; i++) {
            JFXButton button = new JFXButton(htmlToUnicode(emojies[i]));
            button.setStyle("-fx-font-size: 15; -fx-text-fill: black; -fx-background-color: #F0F0F0; -fx-border-radius: 50");
            button.setOnAction(this::emojiButtonAction);

            if (x / 4 == 1) {
                x = 0;
                y = y + 1;
            }

            gridPaneEmoji.add(button, x, y);
            x++;
        }

        scrollPane.setContent(gridPaneEmoji);
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
        System.out.println("user " + text);
        user = text;

    }

    public String getClientName() {
        return user;

    }
    @FXML
    void SinahalaButtonOnAction(ActionEvent event) {
        if (!scrollPane.isVisible()) {
            openSinhalaLetters();
        } else {

            scrollPane.setVisible(false);

        }
    }
    public void openSinhalaLetters(){
        int x = 0;
        int y = 0;

        // GridPane gridPane = new GridPane();
        gridPaneSinhala.setPrefSize(150, 300);
        gridPaneSinhala.setLayoutX(200);
        gridPaneSinhala.setLayoutY(200);
        gridPaneSinhala.setStyle("-fx-font-size: 30");
        int[] sinhalaLetterCodePoints = {
                0x0D9A, // ක
                0x0D9B, // ඛ
                0x0D9A, 0x0DCF, // කා
                0x0D9B, 0x0DCF, // ඛා
                0x0D9A, 0x0DD0, // කැ
                0x0D9B, 0x0DD0, // ඛැ
                0x0D9A, 0x0DD1, // කෑ
                0x0D9B, 0x0DD1, // ඛෑ
                0x0D9A, 0x0DD2, // කි
                0x0D9B, 0x0DD2, // ඛි
                0x0D9A, 0x0DD3, // කී
                0x0D9B, 0x0DD3, // ඛී
                0x0D9A, 0x0DD4, // කු
                0x0D9B, 0x0DD4, // ඛු
                0x0D9A, 0x0DD6, // කූ
                0x0D9B, 0x0DD6, // ඛූ
                0x0DAF, // ද
                0x0DB6, // බ
                0x0DB4, // ප
                0x0DB8, // ම
                0x0DC0, // ව
                0x0DC3, // ස
                0x0DC4, // හ
                0x0DBA, // ය
                0x0DBB, // ර
                0x0DB1, // න
                0x0DBD, // ල
                0x0DC5, // ළ
                0x0DA2, // ජ
                0x0DC1, // ශ
                0x0DC2  // ෂ
        };



        for (int codePoint : sinhalaLetterCodePoints) {
            String string = Character.toString((char) codePoint);

            JFXButton button = new JFXButton(string);
            button.setStyle("-fx-font-size: 15; -fx-text-fill: black; -fx-background-color: #F0F0F0; -fx-border-radius: 50");
            button.setOnAction(this::sinhalaLetterButtonAction);

            if (x / 4 == 1) {
                x = 0;
                y = y + 1;
            }

            gridPaneSinhala.add(button, x, y);
            x++;

        }

        scrollPane.setContent(gridPaneSinhala);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setVisible(true);
    }
    private void sinhalaLetterButtonAction(ActionEvent event) {
        JFXButton button = (JFXButton) event.getSource();
        textMessage.appendText(button.getText());
        scrollPane.setVisible(false);

    }
}