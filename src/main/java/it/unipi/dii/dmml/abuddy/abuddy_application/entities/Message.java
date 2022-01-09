package it.unipi.dii.dmml.abuddy.abuddy_application.entities;

public class Message {

    private String username;
    private String text;
    private long timestamp;
    private int clusterId;

    public Message(String username, String text, long timestamp, int clusterId) {
        this.username = username;
        this.text = text;
        this.timestamp = timestamp;
        this.clusterId = clusterId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }


    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public int getClusterId() {
        return clusterId;
    }

    public void setClusterId(int clusterId) {
        this.clusterId = clusterId;
    }
}
