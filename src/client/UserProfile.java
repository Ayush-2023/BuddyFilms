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
    private Boolean streaming;

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

    public void startStreamListener(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        int code=0;

        SettingGUIForHost:{
            Stage stage = (Stage) nameField.getScene().getWindow();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("StreamingGUI.fxml"));
            Parent root = loader.load();
            stage.setTitle("Buddy Films");
            //v: width  v1: height
            stage.setScene(new Scene(root, 600, 480));
            stage.show();
            //giving input for the video to be played
            StreamingGUI streamingGUIObject = loader.<StreamingGUI>getController();
            streamingGUIObject.setMedia("E:\\Videos\\Movies\\23m_1613182452_8810.mp4");
        }
        CommunicateWithServer:{
            //Creating stream entry in database server
            socket = new Socket("localhost", 5436);

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            //writing operation
            objectOutputStream.writeObject("Start Stream");
            objectOutputStream.flush();
            code=objectInputStream.readInt();

        }
        try {
            Thread createFrame = new Thread(new CreateFrame(objectInputStream,objectOutputStream,username));
            createFrame.start();
        }catch(IOException e){
            e.printStackTrace();
        }
        synchronized (this.streaming) {
            this.streaming = true;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        while (streaming) {
                            objectOutputStream.writeObject((Boolean) true);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        }
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
