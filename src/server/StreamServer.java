package server;

import javax.xml.transform.Result;
import java.io.*;
import java.net.Socket;
import java.sql.*;

public class StreamServer implements Runnable{
    private int frameNumber;
    private int code;

    private Socket socket;
    private ObjectInputStream objectInputStream;
    private ObjectOutputStream objectOutputStream;
    private Connection connection;
    private String host;

    StreamServer(Socket socket) throws IOException{
        this.socket=socket;
        this.frameNumber=-1;
        this.socket=socket;
        this.objectInputStream=new ObjectInputStream(socket.getInputStream());
        this.objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        this.code=this.objectInputStream.readInt();
    }

    @Override
    public void run() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/streamapp?autoReconnect=true&useSSL=false";
            this.connection = DriverManager.getConnection(url, "root", "TetraM0s");

            //writing sql query
            String query1="SELECT ID FROM Streams WHERE Code=\""+code+"\";";
            PreparedStatement preparedStatement=connection.prepareStatement(query1);
            ResultSet result= preparedStatement.executeQuery();
            if(result.next()){
                this.host=result.getString("ID");
            }

            String query2="SELECT Image FROM StreamData WHERE Code=\";";
            preparedStatement=connection.prepareStatement(query2);


        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
    }
}
