package client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class StreamingGUI{
    @FXML
    private MediaView mediaViewItem;
    @FXML
    private Button stopAndPlayButton;
    @FXML
    private Button pauseAndResumeButton;
    private Boolean streaming;
    private boolean playing,paused;

    private File mediaFile;
    private Media media;
    private MediaPlayer mediaPlayer;

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
}
