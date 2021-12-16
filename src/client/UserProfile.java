package client;

import dataClasses.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class UserProfile {
    public Label nameField;

    private String username;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket socket;

    public void lookFriendListener(ActionEvent actionEvent) {

    }

    public void showFriendListListener(ActionEvent actionEvent) throws IOException, ClassNotFoundException {

        Stage stage =(Stage) nameField.getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("MyFriendList.fxml"));
        Parent root= loader.load();
        stage.setTitle("Buddy Films-My Friends List");
        //v: width  v1: height
        stage.setScene(new Scene(root, 460, 410));
        stage.show();

        //passing message to next GUI controller
        MyFriendList myFriendList = loader.<MyFriendList>getController();
        myFriendList.setFields(this.username);
    }

    public void startStreamListener(ActionEvent actionEvent) {

    }

    public void scheduleStreamListener(ActionEvent actionEvent) {
    }

    public void joinSTreamListener(ActionEvent actionEvent) {
    }

    public void showRequestListener(ActionEvent actionEvent) {
    }

    public void setUsername(String username){
        this.nameField.setText(username);
        this.username=username;
    }

}
