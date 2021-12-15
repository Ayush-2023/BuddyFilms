package client;

import dataClasses.User;
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

public class SignupGUI {

    public Button signupButton;
    public TextField usernameField;
    public TextField passwordField;
    public TextField confirmPasswordField;

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private User newUser;

    public void signupListener(ActionEvent actionEvent) throws IOException {
        //checking that password is same as confirmed password
        if(passwordField.getText().equals(confirmPasswordField.getText())&&passwordField.getText().length()>5) {
            socket = new Socket("localhost", 5436);

            objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
            objectInputStream = new ObjectInputStream(socket.getInputStream());

            newUser = new User(usernameField.getText(), passwordField.getText());
            //writing operation
            objectOutputStream.writeObject("Signup");
            objectOutputStream.flush();
            //writing object
            objectOutputStream.writeObject(newUser);
            objectOutputStream.flush();

            //read validity
            //for debugging
            System.out.println("Getting Status");
            Boolean validity = objectInputStream.readBoolean();
            System.out.println(validity);
            if(validity) {
                Stage stage = (Stage) usernameField.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("LoginGUI.fxml"));
                stage.setTitle("Buddy Films-Login");
                //v: width  v1: height
                stage.setScene(new Scene(root, 400, 350));
                stage.show();
            }else{
                Stage stage =(Stage) usernameField.getScene().getWindow();
                FXMLLoader loader= new FXMLLoader(getClass().getResource("FailureGUI.fxml"));
                Parent root=loader.load();
                stage.setTitle("Login Failure");
                stage.setScene(new Scene(root, 300, 200));
                stage.show();

                FailureGUI failureGUIObject=loader.<FailureGUI>getController();
                failureGUIObject.setFailureMessageLabel("Username already in use");
            }
        }else{
            Stage stage =(Stage) usernameField.getScene().getWindow();
            FXMLLoader loader= new FXMLLoader(getClass().getResource("FailureGUI.fxml"));
            Parent root=loader.load();
            stage.setTitle("Login Failure");
            stage.setScene(new Scene(root, 300, 200));
            stage.show();

            FailureGUI failureGUIObject=loader.<FailureGUI>getController();
            failureGUIObject.setFailureMessageLabel("Password and Confirmed Password Mismatch");
        }
    }
}
