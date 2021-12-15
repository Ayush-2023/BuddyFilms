package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.*;

import dataClasses.User;

public class HandleClientSocket implements Runnable {
    private User user;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Connection connection;

    public HandleClientSocket(Socket socket) throws IOException, ClassNotFoundException, SQLException {
        this.socket=socket;
        user=new User();
        objectInputStream=new ObjectInputStream(socket.getInputStream());
        objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/streamapp";
        this.connection = DriverManager.getConnection(url, "root", "TetraM0s");
    }

    @Override
    public void run() {
        System.out.println("Handling Client");
        String operation;
        try {
            //reading operation required
            operation=(String)objectInputStream.readObject();

            if(operation.equals("Login")){

                //reading the User object
                user=(User)objectInputStream.readObject();

                //writing the sql query
                String query1 = "SELECT count(*) FROM User where Username=\""+user.getName()+
                        "\"and Password=\""+user.getPassword()+"\";";

                //setting statement, executing it and storing result in resultSet
                PreparedStatement preStat = connection.prepareStatement(query1);
                ResultSet result = preStat.executeQuery();

                //checking if select statement executed successfully
                if(result.next()){
                    //returns count(*)==1(ie if username exits or not)  to client
                    objectOutputStream.writeBoolean(result.getInt("count(*)") == 1);
                }else{
                    objectOutputStream.writeBoolean(false);
                }
                objectOutputStream.flush();
            }else{
                /*
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
            }
            System.out.println("Client Handled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
}
