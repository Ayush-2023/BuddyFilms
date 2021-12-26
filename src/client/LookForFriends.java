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

public class LookForFriends implements Initializable {
    @FXML
    private ListView listViewItem;
    @FXML
    private Label headLabel;
    @FXML
    private Button goBackButton;
    @FXML
    private TextField nameField;

    private String username;
    private String[] userMatchList;

    public void setFields(String username) {
        this.username=username;
        this.headLabel.setText("Look for friends");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.listViewItem.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observableValue, Object o, Object t1) {
                try{
                    Stage stage = (Stage) goBackButton.getScene().getWindow();
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("OthersProfile.fxml"));
                    Parent root = loader.load();
                    stage.setTitle("Buddy Films");
                    //v: width  v1: height
                    stage.setScene(new Scene(root, 300, 200));
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
        userProfileObject.setUsername(this.username);
    }

    public void searchListener(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        Socket socket = new Socket("localhost", 5436);

        ObjectOutputStream objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream objectInputStream=new ObjectInputStream(socket.getInputStream());

        //writing operation
        objectOutputStream.writeObject("Search Users");
        objectOutputStream.flush();
        //writing username
        objectOutputStream.writeObject(this.username);
        objectOutputStream.flush();
        objectOutputStream.writeObject(this.nameField.getText());
        objectOutputStream.flush();

        //for debugging
        System.out.println("Getting size");
        int size=objectInputStream.readInt();
        this.userMatchList=new String[size];
        for(int i=0;i<size;i++){
            this.userMatchList[i]= (String) objectInputStream.readObject();
        }

        this.listViewItem.getItems().clear();
        this.listViewItem.getItems().addAll(userMatchList);
    }
}
