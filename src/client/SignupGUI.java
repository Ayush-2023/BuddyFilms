package client;

import dataClasses.User;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import utilityClasses.BlowFishEncryption;
import utilityClasses.InvalidInputException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class SignupGUI {

    public Button signupButton;
    public TextField usernameField;
    public PasswordField passwordField;
    public PasswordField confirmPasswordField;

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;

    private User newUser;

    public void signupListener(ActionEvent actionEvent) throws IOException {
        //checking that password is same as confirmed password
        String password="";
        String confirmedPassword="";
        try {
            password =new BlowFishEncryption().encryptData(passwordField.getText());
            confirmedPassword=new BlowFishEncryption().encryptData(confirmPasswordField.getText());
            if(!password.equals(confirmedPassword)){
                throw new InvalidInputException("Confirmed Password does not match password");
            }else if(!(password.length()>5)){
                throw  new InvalidInputException("Password Too small");
            }
        }catch(InvalidInputException e){
            Stage stage =(Stage) usernameField.getScene().getWindow();
            FXMLLoader loader= new FXMLLoader(getClass().getResource("FailureGUI.fxml"));
            Parent root=loader.load();
            stage.setTitle("Login Failure");
            stage.setScene(new Scene(root, 300, 200));
            stage.show();

            FailureGUI failureGUIObject=loader.<FailureGUI>getController();
            failureGUIObject.setFailureMessageLabel(e.getMessage());
        }
        socket = new Socket("localhost", 5436);

        objectOutputStream = new ObjectOutputStream(socket.getOutputStream());
        objectInputStream = new ObjectInputStream(socket.getInputStream());

        newUser = new User(usernameField.getText(), password);
        //writing operation
        objectOutputStream.writeObject("Signup");
        objectOutputStream.flush();
        //writing object
        objectOutputStream.writeObject(newUser);
        objectOutputStream.flush();

        //read validity
        //for debugging
        System.out.println("Getting Status");
        Boolean valid = objectInputStream.readBoolean();
        System.out.println(valid);
        if(valid) {
            Stage stage = (Stage) usernameField.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("LoginGUI.fxml"));
            stage.setTitle("Buddy Films-Login");
            //v: width  v1: height
            stage.setScene(new Scene(root, 380, 350));
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
    }
}
