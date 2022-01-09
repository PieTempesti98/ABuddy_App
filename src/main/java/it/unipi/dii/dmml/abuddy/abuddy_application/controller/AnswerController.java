package it.unipi.dii.dmml.abuddy.abuddy_application.controller;

import it.unipi.dii.dmml.abuddy.abuddy_application.config.InfoSession;
import it.unipi.dii.dmml.abuddy.abuddy_application.entities.Answer;
import it.unipi.dii.dmml.abuddy.abuddy_application.utilities.DatabaseUtils;
import it.unipi.dii.dmml.abuddy.abuddy_application.utilities.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AnswerController implements Initializable {

    @FXML
    public ComboBox gender;
    public ComboBox age;
    public TextField university;
    public ComboBox levelYear;
    public ComboBox average;
    public ComboBox quest11;
    public ComboBox quest12;
    public ComboBox quest13;
    public ComboBox quest14;
    public ComboBox quest15;
    public ComboBox quest16;
    public ComboBox quest17;
    public ComboBox quest18;
    public ComboBox quest19;
    public ComboBox quest20;
    public ComboBox quest21;
    public ComboBox quest22;
    public ComboBox quest23;
    public ComboBox quest24;
    public ComboBox quest25;
    public ComboBox quest26;
    public ComboBox quest27;
    public ComboBox quest28;
    public ComboBox quest29;
    public ComboBox quest30;
    public ComboBox quest31;
    public ComboBox quest32;
    public ComboBox quest33;
    public ComboBox quest34;
    public ComboBox quest35;
    public ComboBox quest36;
    public ComboBox quest37;
    public ScrollPane scrollPane;

    public void submit(ActionEvent actionEvent) {
        ArrayList<String> quests = new ArrayList<>();
        quests.add((String) quest11.getValue());
        quests.add((String) quest12.getValue());
        quests.add((String) quest13.getValue());
        quests.add((String) quest14.getValue());
        quests.add((String) quest15.getValue());
        quests.add((String) quest16.getValue());
        quests.add((String) quest17.getValue());
        quests.add((String) quest18.getValue());
        quests.add((String) quest19.getValue());
        quests.add((String) quest20.getValue());
        quests.add((String) quest21.getValue());
        quests.add((String) quest22.getValue());
        quests.add((String) quest23.getValue());
        quests.add((String) quest24.getValue());
        quests.add((String) quest25.getValue());
        quests.add((String) quest26.getValue());
        quests.add((String) quest27.getValue());
        quests.add((String) quest28.getValue());
        quests.add((String) quest29.getValue());
        quests.add((String) quest30.getValue());
        quests.add((String) quest31.getValue());
        quests.add((String) quest32.getValue());
        quests.add((String) quest33.getValue());
        quests.add((String) quest34.getValue());
        quests.add((String) quest35.getValue());
        quests.add((String) quest36.getValue());
        quests.add((String) quest37.getValue());
        Answer answer = new Answer(
                InfoSession.getSession().getUsername(),
                (String) age.getValue(),
                (String) gender.getValue(),
                university.getText(),
                (String) average.getValue(),
                (String) levelYear.getValue(),
                quests
        );
        DatabaseUtils.insertAnswer(answer);
        Utils.changeScene("waiting.fxml", actionEvent);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Answer answer = DatabaseUtils.getAnswer(InfoSession.getSession().getUsername());

        if (answer != null) {
            gender.getSelectionModel().select(answer.getGender());
            age.getSelectionModel().select(answer.getAge());
            average.getSelectionModel().select(answer.getAverage());
            university.setText(answer.getUniversity());
            levelYear.getSelectionModel().select(answer.getYear());
            quest11.getSelectionModel().select(answer.getAnswers().get(0));
            quest12.getSelectionModel().select(answer.getAnswers().get(1));
            quest13.getSelectionModel().select(answer.getAnswers().get(2));
            quest14.getSelectionModel().select(answer.getAnswers().get(3));
            quest15.getSelectionModel().select(answer.getAnswers().get(4));
            quest16.getSelectionModel().select(answer.getAnswers().get(5));
            quest17.getSelectionModel().select(answer.getAnswers().get(6));
            quest18.getSelectionModel().select(answer.getAnswers().get(7));
            quest19.getSelectionModel().select(answer.getAnswers().get(8));
            quest20.getSelectionModel().select(answer.getAnswers().get(9));
            quest21.getSelectionModel().select(answer.getAnswers().get(10));
            quest22.getSelectionModel().select(answer.getAnswers().get(11));
            quest23.getSelectionModel().select(answer.getAnswers().get(12));
            quest24.getSelectionModel().select(answer.getAnswers().get(13));
            quest25.getSelectionModel().select(answer.getAnswers().get(14));
            quest26.getSelectionModel().select(answer.getAnswers().get(15));
            quest27.getSelectionModel().select(answer.getAnswers().get(16));
            quest28.getSelectionModel().select(answer.getAnswers().get(17));
            quest29.getSelectionModel().select(answer.getAnswers().get(18));
            quest30.getSelectionModel().select(answer.getAnswers().get(19));
            quest31.getSelectionModel().select(answer.getAnswers().get(20));
            quest32.getSelectionModel().select(answer.getAnswers().get(21));
            quest33.getSelectionModel().select(answer.getAnswers().get(22));
            quest34.getSelectionModel().select(answer.getAnswers().get(23));
            quest35.getSelectionModel().select(answer.getAnswers().get(24));
            quest36.getSelectionModel().select(answer.getAnswers().get(25));
            quest37.getSelectionModel().select(answer.getAnswers().get(26));
            return;
        }
        gender.getSelectionModel().select(0);
        age.getSelectionModel().select(0);
        average.getSelectionModel().select(0);
        university.setText("University of Pisa");
        levelYear.getSelectionModel().select(0);
        quest11.getSelectionModel().select(0);
        quest12.getSelectionModel().select(0);
        quest13.getSelectionModel().select(0);
        quest14.getSelectionModel().select(0);
        quest15.getSelectionModel().select(0);
        quest16.getSelectionModel().select(0);
        quest17.getSelectionModel().select(0);
        quest18.getSelectionModel().select(0);
        quest19.getSelectionModel().select(0);
        quest20.getSelectionModel().select(0);
        quest21.getSelectionModel().select(0);
        quest22.getSelectionModel().select(0);
        quest23.getSelectionModel().select(0);
        quest24.getSelectionModel().select(0);
        quest25.getSelectionModel().select(0);
        quest26.getSelectionModel().select(0);
        quest27.getSelectionModel().select(0);
        quest28.getSelectionModel().select(0);
        quest29.getSelectionModel().select(0);
        quest30.getSelectionModel().select(0);
        quest31.getSelectionModel().select(0);
        quest32.getSelectionModel().select(0);
        quest33.getSelectionModel().select(0);
        quest34.getSelectionModel().select(0);
        quest35.getSelectionModel().select(0);
        quest36.getSelectionModel().select(0);
        quest37.getSelectionModel().select(0);
    }

    public void goback(ActionEvent actionEvent) {

        Utils.goBackToMain(actionEvent);
    }
}
