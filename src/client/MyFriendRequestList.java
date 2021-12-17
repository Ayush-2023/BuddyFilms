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

public class MyFriendRequestList {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private String username;
    private String[] requests;
    private int index;

    public TextField requestField1;
    public TextField requestField2;
    public TextField requestField3;
    public TextField requestField4;

    public void acceptListener1(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (!this.requestField1.getText().isEmpty()) {
            socket = new Socket("localhost", 5436);

            objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
            objectInputStream=new ObjectInputStream(socket.getInputStream());

            //writing operation
            objectOutputStream.writeObject("Accept Request");
            objectOutputStream.flush();
            //writing object
            objectOutputStream.writeObject(this.username);
            objectOutputStream.flush();
            objectOutputStream.writeObject(this.requestField1.getText());
            objectOutputStream.flush();

            //reading status
            System.out.println("Getting status");
            //status of 2 deletion and one insertion
            if(objectInputStream.readBoolean()&&objectInputStream.readBoolean()&&objectInputStream.readBoolean()){
                System.out.println("Request Accepted");
                this.setFields(this.username);
            }else{
                System.out.println("Error");
            }
        }
    }

    public void acceptListener2(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (!this.requestField2.getText().isEmpty()) {
            if (!this.requestField1.getText().isEmpty()) {
                socket = new Socket("localhost", 5436);

                objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
                objectInputStream=new ObjectInputStream(socket.getInputStream());

                //writing operation
                objectOutputStream.writeObject("Accept Request");
                objectOutputStream.flush();
                //writing object
                objectOutputStream.writeObject(this.username);
                objectOutputStream.flush();
                objectOutputStream.writeObject(this.requestField2.getText());
                objectOutputStream.flush();

                //reading status
                System.out.println("Getting status");
                //status of 2 deletion and one insertion
                if(objectInputStream.readBoolean()&&objectInputStream.readBoolean()&&objectInputStream.readBoolean()){
                    this.setFields(this.username);
                }else{
                    System.out.println("Error");
                }
            }
        }
    }

    public void acceptListener3(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (!this.requestField3.getText().isEmpty()) {
            if (!this.requestField1.getText().isEmpty()) {
                socket = new Socket("localhost", 5436);

                objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
                objectInputStream=new ObjectInputStream(socket.getInputStream());

                //writing operation
                objectOutputStream.writeObject("Accept Request");
                objectOutputStream.flush();
                //writing object
                objectOutputStream.writeObject(this.username);
                objectOutputStream.flush();
                objectOutputStream.writeObject(this.requestField3.getText());
                objectOutputStream.flush();

                //reading status
                System.out.println("Getting status");
                //status of 2 deletion and one insertion
                if(objectInputStream.readBoolean()&&objectInputStream.readBoolean()&&objectInputStream.readBoolean()){
                    this.setFields(this.username);
                }else{
                    System.out.println("Error");
                }
            }
        }
    }

    public void acceptListener4(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (!this.requestField4.getText().isEmpty()) {
            if (!this.requestField1.getText().isEmpty()) {
                socket = new Socket("localhost", 5436);

                objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
                objectInputStream=new ObjectInputStream(socket.getInputStream());

                //writing operation
                objectOutputStream.writeObject("Accept Request");
                objectOutputStream.flush();
                //writing object
                objectOutputStream.writeObject(this.username);
                objectOutputStream.flush();
                objectOutputStream.writeObject(this.requestField4.getText());
                objectOutputStream.flush();

                //reading status
                System.out.println("Getting status");
                //status of 2 deletion and one insertion
                if(objectInputStream.readBoolean()&&objectInputStream.readBoolean()&&objectInputStream.readBoolean()){
                    this.setFields(this.username);
                }else{
                    System.out.println("Error");
                }
            }
        }
    }

    public void scrollUpListener(ActionEvent actionEvent) {
        if(this.index>0){
            this.index--;
            this.scrollList();
        }
    }

    public void scrollDownListener(ActionEvent actionEvent) {
        if(this.index+5<this.requests.length){
            this.index++;
            this.scrollList();
        }
    }

    public void goBackListener(ActionEvent actionEvent) throws IOException {
        Stage stage =(Stage) requestField1.getScene().getWindow();
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

        this.index=0;
        scrollList();
    }

    public void scrollList(){
        if(requests.length>index+0){
            requestField1.setText(requests[index+0]);
        }else{
            requestField1.setText("");
        }
        if(requests.length>index+1){
            requestField2.setText(requests[index+1]);
        }else{
            requestField2.setText("");
        }
        if(requests.length>index+2){
            requestField3.setText(requests[index+2]);
        }else{
            requestField3.setText("");
        }
        if(requests.length>index+3){
            requestField4.setText(requests[index+3]);
        }else{
            requestField4.setText("");
        }
    }
}
