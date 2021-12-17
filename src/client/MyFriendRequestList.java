package client;

import dataClasses.User;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class MyFriendRequestList {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private String username;
    private String[] requests;
    private int index;

    public Button acceptButton1;
    public Button acceptButton2;
    public Button acceptButton3;
    public Button acceptButton4;
    public Button goUpButton;
    public Button goDownButton;
    public Button backButton;
    public TextField requestField1;
    public TextField requestField2;
    public TextField requestField3;
    public TextField requestField4;

    public void acceptListener1(ActionEvent actionEvent) {

    }

    public void acceptListener2(ActionEvent actionEvent) {

    }

    public void acceptListener3(ActionEvent actionEvent) {

    }

    public void acceptListener4(ActionEvent actionEvent) {

    }

    public void scrollUpListener(ActionEvent actionEvent) {

    }

    public void scrollDownListener(ActionEvent actionEvent) {

    }

    public void goBackListener(ActionEvent actionEvent) {

    }

    public void setFields(String username) throws IOException {
        this.username=username;
        socket = new Socket("localhost", 5436);

        objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        objectInputStream=new ObjectInputStream(socket.getInputStream());

        //writing operation
        objectOutputStream.writeObject("Get Request List");
        objectOutputStream.flush();
        //writing object
        objectOutputStream.writeObject(this.username);
        objectOutputStream.flush();

    }

    public void scrollList(){
        if(requests.length>index+0)requestField1.setText(requests[index+0]);
    }
}
