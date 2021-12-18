package client;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.URL;
import java.util.ResourceBundle;

public class MyFriendList implements Initializable {

    @FXML
    private Label headLabel;
    @FXML
    private Button backButton;
    @FXML
    private ListView listViewItem;

    private String username;
    private String[] friends;

    public void setFields(String username) throws IOException, ClassNotFoundException {
        this.setUsername(username);
        this.headLabel.setText(this.username+"'s Friend list");
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

        this.listViewItem.getItems().addAll(friends);
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        this.listViewItem.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                try{
                    Stage stage = (Stage) backButton.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("OthersProfile.fxml"));
                    Parent root = loader.load();
                    stage.setTitle("Buddy Films");
                    //v: width  v1: height
                    stage.setScene(new Scene(root, 460, 410));
                    stage.show();

                    //passing message to next GUI controller
                    OthersProfile othersProfileObject = loader.<OthersProfile>getController();
                    othersProfileObject.init(username,(String)listViewItem.getSelectionModel().getSelectedItem());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
