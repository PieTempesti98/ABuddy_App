package it.unipi.dii.dmml.abuddy.abuddy_application.controller;

import it.unipi.dii.dmml.abuddy.abuddy_application.clustering.KValid_Clusterer;
import it.unipi.dii.dmml.abuddy.abuddy_application.config.InfoSession;
import it.unipi.dii.dmml.abuddy.abuddy_application.config.ParameterConfig;
import it.unipi.dii.dmml.abuddy.abuddy_application.utilities.DatabaseUtils;
import it.unipi.dii.dmml.abuddy.abuddy_application.utilities.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.paint.Paint;
import javafx.util.Pair;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.FilteredClusterer;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class AdminController implements Initializable {

    public Label output;
    public Label dateCluster;

    @FXML
    public void forceCluster(ActionEvent actionEvent) {

        ArrayList<Pair> assignements = null;

        KValid_Clusterer kvalid = new KValid_Clusterer();
        try {
            int instances = kvalid.load_dataset();
            if(instances <= 1) {
                output.setText("Error: people to cluster are not enough.");

                if(!output.getStyleClass().isEmpty())
                    output.getStyleClass().clear();

                output.getStyleClass().add("error");
                return;
            }
            FilteredClusterer cluster = kvalid.cluster();
            ClusterEvaluation eval = kvalid.evaluateCluster(cluster);
            assignements = kvalid.getAssignements(eval);
            DatabaseUtils.assignCluster(assignements);
            output.setText("Successfully clustered " + instances + " people!");

            if(!output.getStyleClass().isEmpty())
                output.getStyleClass().clear();

            output.getStyleClass().add("success");
            String newDate = new SimpleDateFormat("yyyy-MM-dd").format(new Date(System.currentTimeMillis()));
            ParameterConfig.setLastCluster(newDate);
            dateCluster.setText("Last performed clustering: " + ParameterConfig.getLastCluster());
            ParameterConfig.saveToXML();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void logout(ActionEvent actionEvent) {
        InfoSession.unsetAdmin();
        Utils.changeScene("hello-view.fxml", actionEvent);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        dateCluster.setText("Last performed clustering: " + ParameterConfig.getLastCluster());
    }
}
