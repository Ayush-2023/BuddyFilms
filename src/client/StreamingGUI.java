package client;

import dataClasses.StatusData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StreamingGUI{
    @FXML
    private Label headLabel;
    @FXML
    private MediaView mediaViewItem;
    @FXML
    private Button stopAndPlayButton;
    @FXML
    private Button pauseAndResumeButton;
    private boolean playing,paused;
    private StatusData streaming;

    private File mediaFile;
    private Media media;
    private MediaPlayer mediaPlayer;
    private String username;

    public void stopAndPlayLitener(ActionEvent actionEvent) {
        mediaPlayer.stop();
    }

    public void pauseAndResumeListener(ActionEvent actionEvent) {
        paused=!paused;
        if(paused){
            mediaPlayer.pause();
        }else{
            mediaPlayer.play();
        }
    }

    public void setMedia(String url){
        try {
            mediaFile = new File(url);
            media = new Media(mediaFile.toURI().toURL().toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setAutoPlay(false);
            paused = true;

            mediaViewItem.setMediaPlayer(mediaPlayer);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void setCode(String code){
        this.headLabel.setText("Code :"+code);
    }

    public void endStreamListener(ActionEvent actionEvent) throws IOException {
        mediaPlayer.stop();
        this.streaming.setStatus(false);
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

    public void exitListener(ActionEvent actionEvent) throws IOException {
        mediaPlayer.stop();
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

    public void setStatus(StatusData streaming) {
        this.streaming=streaming;
    }
    public void setUsername(String username){
        this.username=username;
    }
}
