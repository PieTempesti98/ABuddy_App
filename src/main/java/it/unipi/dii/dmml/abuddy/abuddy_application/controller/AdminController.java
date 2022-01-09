package it.unipi.dii.dmml.abuddy.abuddy_application.controller;

import it.unipi.dii.dmml.abuddy.abuddy_application.clustering.KValid_Clusterer;
import it.unipi.dii.dmml.abuddy.abuddy_application.config.InfoSession;
import it.unipi.dii.dmml.abuddy.abuddy_application.utilities.DatabaseUtils;
import it.unipi.dii.dmml.abuddy.abuddy_application.utilities.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.util.Pair;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.FilteredClusterer;

import java.util.ArrayList;

public class AdminController {

    @FXML
    public void forceCluster(ActionEvent actionEvent) {

        ArrayList<Pair> assignements = null;

        KValid_Clusterer kvalid = new KValid_Clusterer();
        try {
            if(kvalid.load_dataset() == 0)
                return;
            FilteredClusterer cluster = kvalid.cluster();
            ClusterEvaluation eval = kvalid.evaluateCluster(cluster);
            assignements = kvalid.getAssignements(eval);
            DatabaseUtils.assignCluster(assignements);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void logout(ActionEvent actionEvent) {
        InfoSession.unsetAdmin();
        Utils.changeScene("hello-view.fxml", actionEvent);
    }


}
