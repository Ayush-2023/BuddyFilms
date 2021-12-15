package client;

import dataClasses.User;
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
        try {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("SignupGUI.fxml"));
            stage.setTitle("Buddy Films-Sign Up");
            stage.setScene(new Scene(root, 310, 220));
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
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
        //for debugging
        System.out.println("Getting Status");
        boolean validity=objectInputStream.readBoolean();

        if(validity){
            try{
                Stage stage =(Stage) usernameField.getScene().getWindow();
                FXMLLoader loader= new FXMLLoader(getClass().getResource("UserProfile.fxml"));
                Parent root= loader.load();
                stage.setTitle("Buddy Films");
                //v: width  v1: height
                stage.setScene(new Scene(root, 460, 410));
                stage.show();

                //passing message to next GUI controller
                UserProfile userProfileObject = loader.<UserProfile>getController();
                userProfileObject.setUsername(user.getName());
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
        else{
            try{
                Stage stage =(Stage) usernameField.getScene().getWindow();
                FXMLLoader loader= new FXMLLoader(getClass().getResource("FailureGUI.fxml"));
                Parent root=loader.load();
                stage.setTitle("Login Failure");
                stage.setScene(new Scene(root, 300, 200));
                stage.show();

                FailureGUI failureGUIObject=loader.<FailureGUI>getController();
                failureGUIObject.setFailureMessageLabel("Login credentials incorrect");
            }
            catch(IOException e) {
                e.printStackTrace();
            }
        }
    }
}