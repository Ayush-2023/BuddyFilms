package client;

import javafx.event.ActionEvent;
import javafx.scene.control.Label;

public class UserProfile {
    public Label nameField;

    public void lookFriendListener(ActionEvent actionEvent) {

    }

    public void showFriendListListener(ActionEvent actionEvent) {
    }

    public void startStreamListener(ActionEvent actionEvent) {
    }

    public void scheduleStreamListener(ActionEvent actionEvent) {
    }

    public void joinSTreamListener(ActionEvent actionEvent) {
    }

    public void showRequestListener(ActionEvent actionEvent) {
    }

    public void setUsername(String username){
        this.nameField.setText(username);
    }
}
