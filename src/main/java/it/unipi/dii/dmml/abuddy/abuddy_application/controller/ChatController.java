package it.unipi.dii.dmml.abuddy.abuddy_application.controller;

import it.unipi.dii.dmml.abuddy.abuddy_application.config.InfoSession;
import it.unipi.dii.dmml.abuddy.abuddy_application.entities.Message;
import it.unipi.dii.dmml.abuddy.abuddy_application.entities.User;
import it.unipi.dii.dmml.abuddy.abuddy_application.utilities.DatabaseUtils;
import it.unipi.dii.dmml.abuddy.abuddy_application.utilities.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    public VBox contentRoot;
    public TextField newMessage;
    public Label errorMessage;
    public Label welcome;
    private User sessionUser;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        sessionUser = InfoSession.getSession();
        welcome.setText("Hello, " + sessionUser.getFullName());
        uploadMessages(0);

    }

    public void refreshBoard(ActionEvent actionEvent) {
        long timestamp = System.currentTimeMillis();
        uploadMessages(timestamp);

    }

    public void logout(ActionEvent actionEvent) {
        InfoSession.unsetSession();
        Utils.changeScene("hello-view.fxml", actionEvent);
    }

    public void leave(ActionEvent actionEvent) {
        sessionUser.setUnsatisfied();
        Utils.changeScene("waiting.fxml", actionEvent);
    }

    public void send(ActionEvent actionEvent) {
        Message m = new Message(sessionUser.getUsername(), newMessage.getText(), System.currentTimeMillis(), sessionUser.getClusterId());
        if(DatabaseUtils.insertMessage(m)) {
            appendMessage(m);
            if(!errorMessage.getText().equals("")){
                errorMessage.setText("");
            }
            newMessage.setText("");
            return;
        }
        errorMessage.setText("Errore: messaggio non inviato");
    }

    private void uploadMessages(long timestamp){
        ArrayList<Message> messages = DatabaseUtils.getMessagesByCluster(sessionUser.getClusterId(), timestamp);
        for(Message m: messages){
            appendMessage(m);
        }
    }

    private void appendMessage(Message m){
        VBox message = new VBox();
        //message.setSpacing(10);

        Label author = new Label();
        author.getStyleClass().add("author");
        Label text = new Label();
        text.getStyleClass().add("text");
        Label timestamp = new Label();
        timestamp.getStyleClass().add("timestamp");
        Date date = new Date(m.getTimestamp());

        timestamp.setText(date.toString());
        if(m.getUsername().equals(sessionUser.getUsername()))
            message.getStyleClass().add("mess-sent");
        else
            message.getStyleClass().add("mess-recv");
        author.setText(m.getUsername());
        text.setText(m.getText());

        message.getChildren().add(author);
        message.getChildren().add(text);
        message.getChildren().add(timestamp);

        contentRoot.getChildren().add(message);
    }

    public void goback(ActionEvent actionEvent) {

        Utils.goBackToMain(actionEvent);
    }
}
