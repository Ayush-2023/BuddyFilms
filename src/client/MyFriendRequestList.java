package client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.ScrollEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MyFriendRequestList implements Initializable {
    @FXML
    private ListView listViewItem;
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
        stage.setScene(new Scene(root, 460, 410));
        stage.show();

        //passing message to next GUI controller
        UserProfile userProfileObject = loader.<UserProfile>getController();
        userProfileObject.setUsername(this.username);
    }

    public void setFields(String username) throws IOException, ClassNotFoundException {
        this.username=username;
        this.headLabel.setText("Friend Request");
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

        this.listViewItem.getItems().clear();
        this.listViewItem.getItems().addAll(requests);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.listViewItem.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {

                try {
                    socket = new Socket("localhost", 5436);
                    objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
                    objectInputStream = new ObjectInputStream(socket.getInputStream());

                    //writing operation
                    objectOutputStream.writeObject("Accept Request");
                    objectOutputStream.flush();
                    //writing object
                    objectOutputStream.writeObject(username);
                    objectOutputStream.flush();
                    objectOutputStream.writeObject((String)listViewItem.getSelectionModel().getSelectedItem());
                    objectOutputStream.flush();

                    //reading status
                    System.out.println("Getting status");
                    //status of 2 deletion and one insertion
                    if (objectInputStream.readBoolean() && objectInputStream.readBoolean() && objectInputStream.readBoolean()) {
                        System.out.println("Request Accepted");
                        setFields(username);
                    } else {
                        System.out.println("Error");
                    }
                }catch(IOException | ClassNotFoundException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
