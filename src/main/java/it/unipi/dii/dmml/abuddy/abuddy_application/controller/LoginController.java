package it.unipi.dii.dmml.abuddy.abuddy_application.controller;

import it.unipi.dii.dmml.abuddy.abuddy_application.config.ParameterConfig;
import it.unipi.dii.dmml.abuddy.abuddy_application.config.InfoSession;
import it.unipi.dii.dmml.abuddy.abuddy_application.entities.User;
import it.unipi.dii.dmml.abuddy.abuddy_application.utilities.DatabaseUtils;
import it.unipi.dii.dmml.abuddy.abuddy_application.utilities.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class LoginController {
    public Label errorField;
    @FXML TextField username;
    @FXML PasswordField password;
    private ActionEvent event;

    @FXML
    private void login(ActionEvent ae){
        this.event = ae;

        String username = this.username.getText();
        String password = this.password.getText();
        //Controlli
        if(username.length() == 0)
            return;
        if(password.length() == 0)
            return;
        //Admin
        if(username.equals(ParameterConfig.getAdminUsername()) && password.equals(ParameterConfig.getAdminPassword())){
            InfoSession.setAdmin();
            Utils.changeScene("admin.fxml",ae);
            return;
        }
        //Regular User
       User logged = DatabaseUtils.getUser(username);
        if(logged != null){
            if(!password.equals(logged.getPassword())){
                errorField.setText("Wrong password");
                return;
            }
            InfoSession.setSession(logged);

            if(logged.getClusterId() >= 0)
                Utils.changeScene("chat.fxml",ae);
            else
                Utils.changeScene("waiting.fxml",ae);

        }
        else
            errorField.setText("User not found");
    }

    public void goback(ActionEvent actionEvent) {

        Utils.goBackToMain(actionEvent);
    }
}
