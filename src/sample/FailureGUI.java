package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import java.io.IOException;

public class FailureGUI {
    public Button retryButton;
    public Label failureMessageLabel;

    public void retryHandler(ActionEvent actionEvent){
        try{
            Stage stage =(Stage) retryButton.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("LoginGUI.fxml"));
            stage.setTitle("Buddy Films-Login");
            stage.setScene(new Scene(root, 400, 350));
            stage.show();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }
}
