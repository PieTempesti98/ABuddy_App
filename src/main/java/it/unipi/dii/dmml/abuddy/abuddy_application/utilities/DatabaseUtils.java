package it.unipi.dii.dmml.abuddy.abuddy_application.utilities;
import it.unipi.dii.dmml.abuddy.abuddy_application.config.ParameterConfig;
import it.unipi.dii.dmml.abuddy.abuddy_application.entities.Answer;
import it.unipi.dii.dmml.abuddy.abuddy_application.entities.Message;
import it.unipi.dii.dmml.abuddy.abuddy_application.entities.User;
import javafx.util.Pair;
import weka.core.Instances;
import weka.experiment.InstanceQuery;

import java.io.File;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;

public class DatabaseUtils {

    private static final String[] QUESTION_ORDER = {"Strongly Disagree", "Disagree", "Uncertain", "Agree", "Strongly Agree"};
    private static final String[] YEAR_ORDER = {"First", "Second", "Third", "Fourth", "Fifth", "Other"};
    private static final String[] AVERAGE_ORDER = {"50", "50-59", "60-69", "70-79", "80-89", "90"};
    private static final String[] AGE_ORDER = {"18-24", "25-29", "30"};
    private static final String[] DEVICES_ORDER = {"1-3", "3-6", "6-9", "9-12", "12"};


    static Connection connection = null;

    private static void openJDBCConnection()
    {
        if(connection == null)
        {
            try {
                String dbParams = ParameterConfig.getIp() +":"+ParameterConfig.getPort();
                connection = DriverManager.getConnection(
                        "jdbc:mysql://"+dbParams+"/"+ParameterConfig.getDbname()+"+?zeroDateTimeBehavior=CONVERT_TO_NULL&serverTimezone=CET",
                        "root","");
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    private static void closeJDBCConnection()
    {
        if(connection != null)
        {
            try { connection.close(); connection = null;}catch (SQLException ignored){}
        }
    }

    public static boolean addUserToDB(User user)
    {
        openJDBCConnection();
        if(connection == null)
            return false;
        String insertStmt = "INSERT INTO user (fullName, email, username,clusterId,password)" +
                " VALUES (?, ?, ?, ?, ?)";

        try(PreparedStatement pstmt = connection.prepareStatement(insertStmt)) {
            pstmt.setString(1, user.getFullName());
            pstmt.setString(2, user.getEmail());
            pstmt.setString(3, user.getUsername());
            pstmt.setInt(4, user.getClusterId());
            pstmt.setString(5, user.getPassword());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
            closeJDBCConnection();
            return false;
        }
        closeJDBCConnection();
        return true;

    }

    public static ArrayList<Message> getMessagesByCluster(int cluster, long timestamp)
    {
        openJDBCConnection();

        //retrieve only unread messages
        String queryStmt = "SELECT * FROM messages WHERE clusterId = ? AND timeSent > ?";

        ResultSet rs = null;
        ArrayList<Message> messages = new ArrayList<Message>();

        try(PreparedStatement pstmt = connection.prepareStatement(queryStmt)) {

            pstmt.setInt(1, cluster);
            pstmt.setLong(2, timestamp);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
                String username = rs.getString("username");
                String text = rs.getString("text");
                long msgTimestamp = rs.getLong("timeSent");

                Message message = new Message(username,text,msgTimestamp,cluster);
                messages.add(message);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(rs != null)
            {
                try { rs.close(); }catch (SQLException ignored){}
                closeJDBCConnection();
            }
        }
        return messages;

    }

    public static void setUnsatisfied(String username){

        openJDBCConnection();
        String queryUpdate = "UPDATE user SET clusterId = -1 WHERE username = ? ";
        try(PreparedStatement pstmt = connection.prepareStatement(queryUpdate)) {

            pstmt.setString(1,username);
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            closeJDBCConnection();
        }

    }

    public static Instances getDataset() throws Exception {

        InstanceQuery queryDB = new InstanceQuery();
        File props = new File("src/main/resources/config/DatabaseUtils.props");
        queryDB.setCustomPropsFile(props);
        queryDB.setUsername("root");
        queryDB.setPassword("");
        queryDB.setQuery("SELECT f.* FROM form f natural join user u where u.clusterId = -1");
        queryDB.connectToDatabase();
        return queryDB.retrieveInstances();


    }

    public static void assignCluster(ArrayList<Pair> clusterIds){


       String queryUpdate = "UPDATE user SET clusterId = ? WHERE username = ? ";

        int startingPoint = getMaxNumCluster();
        if(startingPoint > 0)
            startingPoint++;

        openJDBCConnection();

        //Pair: <key = username,value = id of cluster they've been assigned to>
       for(Pair couple: clusterIds){
           System.out.println(couple.getKey() + " -> " + couple.getValue());

           try(PreparedStatement pstmt = connection.prepareStatement(queryUpdate)) {

               pstmt.setInt(1, (Integer) couple.getValue() + startingPoint);
               pstmt.setString(2, (String) couple.getKey());
               pstmt.executeUpdate();

           } catch (SQLException e) {
               e.printStackTrace();
               closeJDBCConnection();


           }


       }

        closeJDBCConnection();

    }

    public static User getUser(String username){

        openJDBCConnection();

        User selectedUser = null;

        String queryStmt = "SELECT * FROM user WHERE username = ?";

        ResultSet rs = null;
        ArrayList<Message> messages = new ArrayList<Message>();

        try(PreparedStatement pstmt = connection.prepareStatement(queryStmt)) {

            pstmt.setString(1, username);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
                String fullName = rs.getString("fullName");
                String password = rs.getString("password");
                String email = rs.getString("email");
                int clusterId = rs.getInt("clusterId");

               selectedUser = new User(fullName,email,password,username,clusterId);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(rs != null)
            {
                try { rs.close(); }catch (SQLException ignored){}
            }
            closeJDBCConnection();
        }

        return selectedUser;

    }


    public static boolean isClustered(String email) {

        openJDBCConnection();

        User selectedUser = null;
        ResultSet rs = null;
        int cluster = -1;

        String queryStmt = "SELECT clusterId FROM user WHERE email = ?";


        try(PreparedStatement pstmt = connection.prepareStatement(queryStmt)) {

            pstmt.setString(1, email);
            rs = pstmt.executeQuery();
            while(rs.next())
            {
                cluster = rs.getInt("clusterId");


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(rs != null)
            {
                try { rs.close(); }catch (SQLException ignored){}
            }
            closeJDBCConnection();
        }

        return (cluster > -1);

    }

    public static boolean insertMessage(Message m){
        openJDBCConnection();
        String insert = "INSERT INTO messages(username, clusterId, text, timeSent) VALUES (?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(insert)){
            ps.setString(1,m.getUsername());
            ps.setInt(2,m.getClusterId());
            ps.setString(3,m.getText());
            ps.setLong(4,m.getTimestamp());
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            closeJDBCConnection();
            return false;
        }
        closeJDBCConnection();
        return true;
    }

    public static Answer getAnswer(String username) {

        Answer answer = null;
        ResultSet rs = null;

        openJDBCConnection();
        String select = "SELECT * FROM form WHERE username = ?";
        try(PreparedStatement ps = connection.prepareStatement(select)){
            ps.setString(1,username);
            rs = ps.executeQuery();

            String age = null;
            String gender = null;
            String university = null;
            String average = null;
            String year = null;
            ArrayList<String> answers = new ArrayList<>();
            while(rs.next())
            {
                age = AGE_ORDER[rs.getInt("age")];
                gender = rs.getString("gender");
                university = rs.getString("university");
                year = YEAR_ORDER[rs.getInt("year")];
                average = AVERAGE_ORDER[rs.getInt("average")];

                for(int i = 11; i <= 12; i++){
                    String ans = rs.getString("quest"+i);
                    answers.add(ans);
                }

                for(int i = 13; i <= 14; i++){
                    int ans = rs.getInt("quest"+i);
                    answers.add(DEVICES_ORDER[ans]);
                }

                for(int i = 15; i <= 37; i++){

                    int ans = rs.getInt("quest"+i);
                    answers.add(QUESTION_ORDER[ans]);
                }

                answer = new Answer(username, age, gender, university,average, year, answers);

            }


        }catch(SQLException e){
            e.printStackTrace();
            closeJDBCConnection();
            return answer;

        }

        closeJDBCConnection();
        return answer;
    }

    public static boolean insertAnswer(Answer answer) {

        openJDBCConnection();
        String insert = "REPLACE INTO form(username, age, gender, university,average,year,quest11," +
                "quest12," +
                "quest13," +
                "quest14," +
                "quest15," +
                "quest16," +
                "quest17," +
                "quest18," +
                "quest19," +
                "quest20," +
                "quest21," +
                "quest22," +
                "quest23," +
                "quest24," +
                "quest25," +
                "quest26," +
                "quest27," +
                "quest28," +
                "quest29," +
                "quest30," +
                "quest31," +
                "quest32," +
                "quest33," +
                "quest34,"+
                "quest35,"+
                "quest36,"+
                "quest37) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try(PreparedStatement ps = connection.prepareStatement(insert)){
            ps.setString(1,answer.getUsername());
            ps.setInt(2,Arrays.asList(AGE_ORDER).indexOf(answer.getYear()));
            ps.setString(3,answer.getGender());
            ps.setString(4,answer.getUniversity());
            ps.setInt(5,Arrays.asList(AVERAGE_ORDER).indexOf(answer.getYear()));
            ps.setInt(6, Arrays.asList(YEAR_ORDER).indexOf(answer.getYear()));

            for(int i = 0; i < answer.getAnswers().size(); i++) {

                if(i == 2 || i == 3)
                    ps.setInt(i + 7, Arrays.asList(DEVICES_ORDER).indexOf(answer.getAnswers().get(i)));
                else if(i >= 4)
                    ps.setInt(i + 7, Arrays.asList(QUESTION_ORDER).indexOf(answer.getAnswers().get(i)));
                else
                    ps.setString(i + 7, answer.getAnswers().get(i));

            }
            ps.executeUpdate();
        }catch(SQLException e){
            e.printStackTrace();
            closeJDBCConnection();
            return false;
        }
        closeJDBCConnection();
        return true;


    }

    private static int getMaxNumCluster(){

        int max = 0;
        ResultSet rs = null;
        openJDBCConnection();

        String queryStmt = "SELECT max(clusterId) as start FROM user ";


        try(PreparedStatement pstmt = connection.prepareStatement(queryStmt)) {

            rs = pstmt.executeQuery();
            while(rs.next())
            {
                max = rs.getInt("start");
                if(max == -1)
                    max = 0;


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(rs != null)
            {
                try { rs.close(); }catch (SQLException ignored){}
                closeJDBCConnection();
            }

        }


        return max;


    }

    public static int getNumberOfUsers(int clusterId){

        int nUsers = 0;

        ResultSet rs = null;
        openJDBCConnection();

        String queryStmt = "SELECT count(*) as count FROM user WHERE clusterID = ?";


        try(PreparedStatement pstmt = connection.prepareStatement(queryStmt)) {

            pstmt.setInt(1,clusterId);

            rs = pstmt.executeQuery();
            while(rs.next())
            {
                nUsers = rs.getInt("count");


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            if(rs != null)
            {
                try { rs.close(); }catch (SQLException ignored){}
                closeJDBCConnection();
            }

        }
        return nUsers;

    }

}
