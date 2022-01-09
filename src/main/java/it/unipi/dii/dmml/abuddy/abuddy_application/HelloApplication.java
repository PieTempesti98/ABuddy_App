package it.unipi.dii.dmml.abuddy.abuddy_application;

import it.unipi.dii.dmml.abuddy.abuddy_application.clustering.KValid_Clusterer;
import it.unipi.dii.dmml.abuddy.abuddy_application.config.ParameterConfig;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import it.unipi.dii.dmml.abuddy.abuddy_application.kv.KValid;
import javafx.util.Pair;
import weka.classifiers.evaluation.Evaluation;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.FilteredClusterer;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

import java.util.ArrayList;


public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 900);
        stage.setScene(scene);
        stage.setTitle("ABuddy");
        stage.show();


    }

    public static void main(String[] args) {
        launch();
    }
}