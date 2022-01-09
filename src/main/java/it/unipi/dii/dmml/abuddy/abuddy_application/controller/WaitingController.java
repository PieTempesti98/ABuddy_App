package it.unipi.dii.dmml.abuddy.abuddy_application.controller;

import it.unipi.dii.dmml.abuddy.abuddy_application.config.InfoSession;
import it.unipi.dii.dmml.abuddy.abuddy_application.utilities.DatabaseUtils;
import it.unipi.dii.dmml.abuddy.abuddy_application.utilities.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class WaitingController {

    @FXML
    public void refresh(ActionEvent actionEvent) {

        boolean isCluster = DatabaseUtils.isClustered(InfoSession.getSession().getEmail());
        if(isCluster)
            Utils.changeScene("chat.fxml",actionEvent);
        else
            System.out.println("Unfortunately you haven't been assigned to a group yet!");
    }

    public void logout(ActionEvent actionEvent) {
        InfoSession.unsetSession();
        Utils.changeScene("hello-view.fxml", actionEvent);
    }

    public void goback(ActionEvent actionEvent) {

        Utils.goBackToMain(actionEvent);
    }
}
