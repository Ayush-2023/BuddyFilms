package server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StreamServer implements Runnable{
    private int frameNumber;
    private String host;

    private File[] imageFiles;
    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Connection connection;

    StreamServer(Socket socket,StreamServer2 streamServer2) throws IOException{
        this.socket=socket;
        this.frameNumber=-1;
        this.socket=socket;
        this.objectInputStream=new ObjectInputStream(socket.getInputStream());
        this.objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        this.imageFiles=streamServer2.getFrames();
    }

    @Override
    public void run() {

    }
}
