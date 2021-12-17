package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MyFriendList {
    private String username;
    private String[] friends;
    private int index=0;

    public Button visitFriendButton1;
    public Button visitFriendButton2;
    public Button visitFriendButton3;
    public Button visitFriendButton4;
    public Button visitFriendButton5;
    public Button visitFriendButton6;
    public TextField friendNameField1;
    public TextField friendNameField2;
    public TextField friendNameField3;
    public TextField friendNameField4;
    public TextField friendNameField5;
    public TextField friendNameField6;
    public Button upButton;
    public Button downButton;
    public Button backButton;

    public void setFields(String username) throws IOException, ClassNotFoundException {
        this.setUsername(username);
        Socket socket = new Socket("localhost", 5436);

        ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());

        //writing operation
        objectOutputStream.writeObject("Get Friend List");
        objectOutputStream.flush();
        //writing username
        objectOutputStream.writeObject(this.username);
        objectOutputStream.flush();

        //for debugging
        System.out.println("Getting size");
        int size=objectInputStream.readInt();
        friends=new String[size];
        for(int i=0;i<size;i++){
            friends[i]= (String) objectInputStream.readObject();
        }

        this.index=0;
        this.scrollList();
    }

    public void visitFriend1Listener(ActionEvent actionEvent) throws IOException {
        if (!this.friendNameField1.getText().isEmpty()) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OthersProfile.fxml"));
            Parent root = loader.load();
            stage.setTitle("Buddy Films");
            //v: width  v1: height
            stage.setScene(new Scene(root, 460, 410));
            stage.show();

            //passing message to next GUI controller
            OthersProfile othersProfileObject = loader.<OthersProfile>getController();
            othersProfileObject.init(this.username, friendNameField1.getText());
        }
    }
    public void visitFriend2Listener(ActionEvent actionEvent) throws IOException {
        if (!this.friendNameField2.getText().isEmpty()) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OthersProfile.fxml"));
            Parent root = loader.load();
            stage.setTitle("Buddy Films");
            //v: width  v1: height
            stage.setScene(new Scene(root, 460, 410));
            stage.show();

            //passing message to next GUI controller
            OthersProfile othersProfileObject = loader.<OthersProfile>getController();
            othersProfileObject.init(this.username, friendNameField2.getText());
        }
    }
    public void visitFriend3Listener(ActionEvent actionEvent) throws IOException {
        if (!this.friendNameField3.getText().isEmpty()) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OthersProfile.fxml"));
            Parent root = loader.load();
            stage.setTitle("Buddy Films");
            //v: width  v1: height
            stage.setScene(new Scene(root, 460, 410));
            stage.show();

            //passing message to next GUI controller
            OthersProfile othersProfileObject = loader.<OthersProfile>getController();
            othersProfileObject.init(this.username, friendNameField3.getText());
        }
    }
    public void visitFriend4Listener(ActionEvent actionEvent) throws IOException {
        if (!this.friendNameField4.getText().isEmpty()) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OthersProfile.fxml"));
            Parent root = loader.load();
            stage.setTitle("Buddy Films");
            //v: width  v1: height
            stage.setScene(new Scene(root, 460, 410));
            stage.show();

            //passing message to next GUI controller
            OthersProfile othersProfileObject = loader.<OthersProfile>getController();
            othersProfileObject.init(this.username, friendNameField4.getText());
        }
    }
    public void visitFriend5Listener(ActionEvent actionEvent) throws IOException {
        if (!this.friendNameField5.getText().isEmpty()) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OthersProfile.fxml"));
            Parent root = loader.load();
            stage.setTitle("Buddy Films");
            //v: width  v1: height
            stage.setScene(new Scene(root, 460, 410));
            stage.show();

            //passing message to next GUI controller
            OthersProfile othersProfileObject = loader.<OthersProfile>getController();
            othersProfileObject.init(this.username, friendNameField5.getText());
        }
    }
    public void visitFriend6Listener(ActionEvent actionEvent) throws IOException {
        if (!this.friendNameField6.getText().isEmpty()) {
            Stage stage = (Stage) backButton.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("OthersProfile.fxml"));
            Parent root = loader.load();
            stage.setTitle("Buddy Films");
            //v: width  v1: height
            stage.setScene(new Scene(root, 460, 410));
            stage.show();

            //passing message to next GUI controller
            OthersProfile othersProfileObject = loader.<OthersProfile>getController();
            othersProfileObject.init(this.username, friendNameField6.getText());
        }
    }

    public void goUpListener(ActionEvent actionEvent){
        if(this.index>0){
            this.index--;
            this.scrollList();
        }
    }
    public void goDownListener(ActionEvent actionEvent){
        if(this.index+5<this.friends.length){
            this.index++;
            this.scrollList();
        }
    }

    public void goBackListener(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage) backButton.getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("UserProfile.fxml"));
        Parent root= loader.load();
        stage.setTitle("Buddy Films");
        //v: width  v1: height
        stage.setScene(new Scene(root, 460, 410));
        stage.show();

        //passing message to next GUI controller
        UserProfile userProfileObject = loader.<UserProfile>getController();
        userProfileObject.setUsername(this.username);
    }

    public void setUsername(String username) {
        this.username=username;
    }

    public void scrollList(){
        if(friends.length>index+0)friendNameField1.setText(friends[index+0]);
        if(friends.length>index+1)friendNameField2.setText(friends[index+1]);
        if(friends.length>index+2)friendNameField3.setText(friends[index+2]);
        if(friends.length>index+3)friendNameField4.setText(friends[index+3]);
        if(friends.length>index+4)friendNameField5.setText(friends[index+4]);
        if(friends.length>index+5)friendNameField6.setText(friends[index+5]);
    }
}
