package sample.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;

import sample.dataClasses.User;
import sample.dataClasses.ValidityStatus;

public class HandleClientSocket implements Runnable {
    private User user;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    public HandleClientSocket(Socket socket) throws IOException {
        this.socket=socket;
        user=new User();
        objectInputStream=new ObjectInputStream(socket.getInputStream());
        objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void run() {
        System.out.println("Handling Client");
        String operation;
        try {
            //reading operation required
            operation=(String)objectInputStream.readObject();
            user=(User)objectInputStream.readObject();
            objectOutputStream.writeBoolean(false);
            objectOutputStream.flush();

            if(operation.equals("Login")){
                Class.forName("com.mysql.jdbc.Driver");
                String url = "jdbc:mysql://localhost:13720/demo";
                Connection connection = DriverManager.getConnection(url, "root", "TetraM0s");
                /*
                //reading the User object
                user=(User)objectInputStream.readObject();
                String query1 = "SELECT count(*) FROM User where Username="+user.getName()+";";
                String query2 = "Insert into User(Username,Password) value("
                        +user.getName()+","+user.getPassword()+");";
                PreparedStatement preStat = connection.prepareStatement(query1);

                ResultSet result = preStat.executeQuery();
                if(result.next()){
                    if(result.getInt("count(*)")==0){
                        preStat = connection.prepareStatement(query2);
                        if(preStat.executeUpdate(query2)==1){
                            objectOutputStream.writeBoolean(true);
                        }
                    }else {
                        objectOutputStream.writeBoolean(false);
                    }
                }else{
                    objectOutputStream.writeBoolean(false);
                }
                */
            }else{

            }
            System.out.println("Client Handled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
