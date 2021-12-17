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

    private boolean friended,sentRequest;

    public void unfriendListener(ActionEvent actionEvent) throws IOException {
        if(friended){
            socket = new Socket("localhost", 5436);

            objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
            objectInputStream=new ObjectInputStream(socket.getInputStream());

            //writing operation
            objectOutputStream.writeObject("Unfriend");
            objectOutputStream.flush();
            //writing myname,username
            objectOutputStream.writeObject(this.myname);
            objectOutputStream.flush();
            objectOutputStream.writeObject(this.username);
            objectOutputStream.flush();

            //reading status
            //for debugging
            System.out.println("Getting status");
            if(objectInputStream.readBoolean()&&objectInputStream.readBoolean()){
                this.friended=false;
                this.setFlags();
            }else{
                System.out.println("Error");
            }
        }
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

    public void setFlags(){
        if(friended) {
            this.unfriendButton.setText("Unfriend");
            this.requestButton.setText("Already Friends");
        }else{
            this.unfriendButton.setText("Not a Friend");
            if(sentRequest){
                this.requestButton.setText("Request Sent");
            }else{
                this.requestButton.setText("Send Request");
            }
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

        //reading validity
        //for debugging
        System.out.println("Getting Status");
        boolean valid;
        valid=objectInputStream.readBoolean();
        if(valid){
            this.friended=objectInputStream.readBoolean();
            if(this.friended){
                this.sentRequest=false;
            } else{
                valid=objectInputStream.readBoolean();
                if(valid){
                    this.sentRequest=objectInputStream.readBoolean();
                }else{
                    System.out.println("Error");
                }
            }
        }else{
            System.out.println("Error");
        }
        //for debugging
        System.out.println("Friend :"+friended);
        System.out.println("Sent Friend Request :"+sentRequest);
        setFlags();
    }

    public void handleReqeuestListener(ActionEvent actionEvent) throws IOException {
        if(!friended&&!sentRequest){
            socket = new Socket("localhost", 5436);

            objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
            objectInputStream=new ObjectInputStream(socket.getInputStream());

            //writing operation
            objectOutputStream.writeObject("Send Request");
            objectOutputStream.flush();
            //writing myname,username
            objectOutputStream.writeObject(this.myname);
            objectOutputStream.flush();
            objectOutputStream.writeObject(this.username);
            objectOutputStream.flush();

            //reading status
            //for debugging
            System.out.println("Getting status");
            if(objectInputStream.readBoolean()) {
                this.sentRequest=true;
                this.setFlags();
            }else{
                System.out.println("Error");
            }
        }
    }
}
