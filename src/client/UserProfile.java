package client;

import dataClasses.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserProfile {
    public Label nameField;
    public TextField streamCodeFiled;

    //these buttons are redundant(no use, except for defining fxml field ids)
    public Button showRequestButton;
    public Button logoutButton;
    public Button lookFriendButton;
    public Button showFriendListButton;
    public Button startStreamButton;
    public Button scheduleStreamButton;
    public Button joinStreamButton;

    private String username;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket socket;

    public void setUsername(String username){
        this.nameField.setText(username);
        this.username=username;
    }

    public void lookFriendListener(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Stage stage =(Stage) nameField.getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("LookForFriends.fxml"));
        Parent root= loader.load();
        stage.setTitle("Buddy Films-Look For Friends");
        //v: width  v1: height
        stage.setScene(new Scene(root, 350, 350));
        stage.show();

        //passing message to next GUI controller
        LookForFriends lookForFriendsController = loader.<LookForFriends>getController();
        lookForFriendsController.setFields(this.username);
    }

    public void showFriendListListener(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Stage stage =(Stage) nameField.getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("MyFriendList.fxml"));
        Parent root= loader.load();
        stage.setTitle("Buddy Films-My Friends List");
        //v: width  v1: height
        stage.setScene(new Scene(root, 300, 250));
        stage.show();

        //passing message to next GUI controller
        MyFriendList myFriendList = loader.<MyFriendList>getController();
        myFriendList.setFields(this.username);
    }

    public void startStreamListener(ActionEvent actionEvent) {

    }

    public void scheduleStreamListener(ActionEvent actionEvent) {
    }

    public void showRequestListener(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Stage stage =(Stage) nameField.getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("MyFriendRequestList.fxml"));
        Parent root= loader.load();
        stage.setTitle("Buddy Films-"+this.username+"'s Request List");
        //v: width  v1: height
        stage.setScene(new Scene(root, 300, 200));
        stage.show();

        //passing message to next GUI controller
        MyFriendRequestList myFriendRequestList = loader.<MyFriendRequestList>getController();
        myFriendRequestList.setFields(this.username);
    }

    public void logoutListener(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage) nameField.getScene().getWindow();
        Parent root= FXMLLoader.load(getClass().getResource("LoginGUI.fxml"));
        stage.setTitle("Buddy Films-"+this.username+"'s Friends Request List");
        //v: width  v1: height
        stage.setScene(new Scene(root, 460, 410));
        stage.show();
    }

    public void joinSecretStreamListener(ActionEvent actionEvent) {
    }
}
