package sample;

import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

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
        if(passwordField.getText().equals(confirmPasswordField.getText())) {
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
        }
    }
}
