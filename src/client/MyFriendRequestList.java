package client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MyFriendRequestList implements Initializable {
    @FXML
    private ListView listViewItem;
    private ObservableList<String> userList;
    @FXML
    private Label headLabel;

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private String username;
    private String[] requests;

    public void goBackListener(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage) listViewItem.getScene().getWindow();
        FXMLLoader loader= new FXMLLoader(getClass().getResource("UserProfile.fxml"));
        Parent root= loader.load();
        stage.setTitle("Buddy Films");
        //v: width  v1: height
        stage.setScene(new Scene(root, 460, 360));
        stage.show();

        //passing message to next GUI controller
        UserProfile userProfileObject = loader.<UserProfile>getController();
        userProfileObject.setUsername(this.username);
    }

    public void setFields(String username) throws IOException, ClassNotFoundException {
        this.username=username;
        this.headLabel.setText("Received Requests");
        socket = new Socket("localhost", 5436);

        objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        objectInputStream=new ObjectInputStream(socket.getInputStream());

        //writing operation
        objectOutputStream.writeObject("Get Request List");
        objectOutputStream.flush();
        //writing object
        objectOutputStream.writeObject(this.username);
        objectOutputStream.flush();

        //for debugging
        System.out.println("Getting size");
        int size=objectInputStream.readInt();
        requests=new String[size];
        for(int i=0;i<size;i++){
            requests[i]= (String) objectInputStream.readObject();
        }
        System.out.println(size);
        this.userList = FXCollections.observableArrayList();
        this.userList.clear();
        this.userList.addAll(requests);
        this.listViewItem.getItems().clear();
        this.listViewItem.setItems(userList);
    }

    public void rejectListener(String myName,String username){
        //for debugging
        System.out.println(myName+"||"+username);
        try{
            socket = new Socket("localhost", 5436);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            //writing operation
            objectOutputStream.writeObject("Reject Request");
            objectOutputStream.flush();
            //writing object
            objectOutputStream.writeObject(myName);
            objectOutputStream.flush();
            objectOutputStream.writeObject(username);
            objectOutputStream.flush();

            //reading status
            System.out.println("Getting status");
            //status of 2 deletion and one insertion
            if (objectInputStream.readBoolean()) {
                System.out.println("Request Rejected");
                setFields(myName);
            } else {
                System.out.println("Error");
            }
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    public  void acceptListener(String myName,String username){
        //for debugging
        System.out.println(myName+"||"+username);
        try {
            socket = new Socket("localhost", 5436);
            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            //writing operation
            objectOutputStream.writeObject("Accept Request");
            objectOutputStream.flush();
            //writing object
            objectOutputStream.writeObject(myName);
            objectOutputStream.flush();
            objectOutputStream.writeObject(username);
            objectOutputStream.flush();

            //reading status
            System.out.println("Getting status");
            //status of 2 deletion and one insertion
            if (objectInputStream.readBoolean() && objectInputStream.readBoolean() && objectInputStream.readBoolean()) {
                System.out.println("Request Accepted");
                setFields(myName);
            } else {
                System.out.println("Error");
            }
        }catch(IOException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.listViewItem.setCellFactory((Callback<ListView<String>,ListCell<String>>) param ->{
            return new ListCell<String>() {
                @Override
                protected void updateItem(String user, boolean empty){
                    super.updateItem(user,empty);
                    if(user!=null) {
                        Button rejectButton = new Button("Reject");
                        Button acceptButton = new Button("Accept");
                        rejectButton.setOnAction(actionEvent -> {
                            rejectListener(username, user);
                        });
                        acceptButton.setOnAction(actionEvent -> {
                            acceptListener(username, user);
                        });
                        HBox root = new HBox(10);
                        root.getChildren().addAll(new Label(user), acceptButton, rejectButton);
                        setText(null);
                        setGraphic(root);
                    }
                }
            };
        });
    }
}
