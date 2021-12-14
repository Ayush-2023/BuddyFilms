package sample.client;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;

import java.net.Socket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import sample.dataClasses.*;

public class LoginGUI extends Application {
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    public PasswordField passwordField;
    public TextField usernameField;

    private User user;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("LoginGUI.fxml"));
        primaryStage.setTitle("Buddy Films-Login");
        primaryStage.setScene(new Scene(root, 400, 350));
        primaryStage.show();
    }

    public void signUpHandler(ActionEvent actionEvent) {

    }

    public void signInHandler(ActionEvent actionEvent) throws IOException {
        socket = new Socket("localhost", 5436);

        objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        objectInputStream=new ObjectInputStream(socket.getInputStream());

        user=new User(usernameField.getText(),passwordField.getText());
        //writing operation
        objectOutputStream.writeObject("Login");
        objectOutputStream.flush();
        //writing object
        objectOutputStream.writeObject(user);
        objectOutputStream.flush();

        //read validity
        System.out.println("Getting Status");
        Boolean validity=(Boolean)objectInputStream.readBoolean();
        System.out.println("Hello");

        if(validity){
            System.out.println(usernameField.getText());
            System.out.println(passwordField.getText());
        }
        else{
            try{
                Stage stage =(Stage) usernameField.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("FailureGUI.fxml"));
                stage.setTitle("Login Failure");
                stage.setScene(new Scene(root, 300, 200));
                stage.show();
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}