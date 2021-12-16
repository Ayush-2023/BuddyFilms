package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class OthersProfile {
    public Label usernameLabel;
    private String username;
    private String myname;

    public Button unfriendButton;
    public Button requestButton;
    public Button goBackButton;

    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Socket socket;

    private boolean friended,sentRequest,receivedRequest;

    public void unfriendListener(ActionEvent actionEvent) {

    }

    public void goBackListener(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage) goBackButton.getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("UserProfile.fxml"));
        Parent root= loader.load();
        stage.setTitle("Buddy Films");
        //v: width  v1: height
        stage.setScene(new Scene(root, 460, 410));
        stage.show();

        //passing message to next GUI controller
        UserProfile userProfileObject = loader.<UserProfile>getController();
        userProfileObject.setUsername(this.myname);
    }

    public void setFlags(boolean friended, boolean sentRequest, boolean recievedRequest){
        this.sentRequest=sentRequest;
        this.friended=friended;
        this.receivedRequest=recievedRequest;
        if(friended) {
            this.unfriendButton.setText("Unfriend");
            this.requestButton.setText("Already Friends");
        }if(sentRequest){
            this.requestButton.setText("Request Sent");
            this.unfriendButton.setText("Not a Friend");
        }else if(receivedRequest){
            this.goBackButton.setText("Request Received");
            this.unfriendButton.setText("Not a Friend");
        }else{
            this.goBackButton.setText("Send Request");
            this.unfriendButton.setText("Not a Friend");
        }
    }

    public void init(String myname,String username) throws IOException {
        this.username=username;
        this.myname=myname;
        this.usernameLabel.setText(this.username);

        socket = new Socket("localhost", 5436);

        objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        objectInputStream=new ObjectInputStream(socket.getInputStream());

        //writing operation
        objectOutputStream.writeObject("Get Friend Flags");
        objectOutputStream.flush();
        //writing myname,username
        objectOutputStream.writeObject(this.myname);
        objectOutputStream.flush();
        objectOutputStream.writeObject(this.username);
        objectOutputStream.flush();


    }
}
