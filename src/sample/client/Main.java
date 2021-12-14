package sample.client;

import javafx.application.Application;
import javafx.application.Platform;

public class Main {

    public static void main(String[] args) {
        Application.launch(LoginGUI.class,args);
    }

    public static void logout(){
        Platform.exit();
    }
}
