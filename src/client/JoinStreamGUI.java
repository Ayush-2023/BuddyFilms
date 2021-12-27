package client;

import dataClasses.StatusData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

import java.io.*;

public class JoinStreamGUI {
    @FXML
    private Label headLabel;
    @FXML
    private ImageView imageViewItem;
    private StatusData data;
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public void exitStreamListener(ActionEvent actionEvent) throws IOException {
        this.data.setStatus(false);
        Stage stage = (Stage) headLabel.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("UserProfile.fxml"));
        Parent root = loader.load();
        stage.setTitle("Buddy Films");
        //v: width  v1: height
        stage.setScene(new Scene(root, 600, 480));
        stage.show();

        UserProfile userProfile=loader.<UserProfile>getController();
        userProfile.setUsername(username);
    }

    public void setImageViewItem(File imageFile) throws FileNotFoundException {

        File imageFile1=new File("E:\\Pictures\\New folder\\27678.jpg");
        InputStream stream = new FileInputStream(imageFile);
        Image image=new Image(stream);
        imageViewItem.setImage(image);
    }

    public void setStatus(StatusData data) {
        this.data=data;
    }
}
