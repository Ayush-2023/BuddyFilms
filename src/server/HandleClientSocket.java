package server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

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
        String url = "jdbc:mysql://localhost:3306/streamapp?autoReconnect=false&useSSL=false";
        this.connection = DriverManager.getConnection(url, "root", "TetraM0s");
    }

    @Override
    public void run() {
        System.out.println("Handling Client");
        String operation;
        try {
            //reading operation required
            operation=(String)objectInputStream.readObject();

            switch (operation) {
                case "Login" -> this.loginHandler();
                case "Signup" -> this.signupHandler();
                case "Get Friend List" -> this.returnFriendList();
                case "Get Friend Flags" -> this.returnFriendFlags();
                case "Unfriend" -> this.unfriendHandler();
                case "Send Request" -> this.sendRequestHandler();
            }
            //this.closePipes();
            System.out.println("Client Handled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendRequestHandler() throws IOException, ClassNotFoundException, SQLException {
        String username=(String)objectInputStream.readObject();
        String friend=(String)objectInputStream.readObject();

        //writing the sql query
        String query="INSERT INTO Requests(To_User,By_User) value(\""+friend+
                "\",\""+username+"\");";
        PreparedStatement preStat = connection.prepareStatement(query);
        objectOutputStream.writeBoolean(preStat.executeUpdate()==1);
        objectOutputStream.flush();
    }

    private void unfriendHandler() throws IOException, ClassNotFoundException, SQLException {
        String username=(String)objectInputStream.readObject();
        String friend=(String)objectInputStream.readObject();

        //writing the sql query
        String query1="DELETE FROM Friends WHERE Username=\""+username+
                "\" AND Friend=\""+friend+"\";";
        String query2="DELETE FROM Friends WHERE Username=\""+friend+
                "\" AND Friend=\""+username+"\";";
        PreparedStatement preStat = connection.prepareStatement(query1);
        if(preStat.executeUpdate() == 1) {
            objectOutputStream.writeBoolean(true);
            objectOutputStream.flush();
            preStat = connection.prepareStatement(query2);
            objectOutputStream.writeBoolean(preStat.executeUpdate() == 1);
        }else{
            objectOutputStream.writeBoolean(false);
        }
        objectOutputStream.flush();
    }

    private void returnFriendFlags() throws IOException, ClassNotFoundException, SQLException {
        String username=(String)objectInputStream.readObject();
        String friend=(String)objectInputStream.readObject();

        //writing the sql query
        String query1="SELECT count(*) FROM Friends WHERE Username=\""+username+
                "\" AND Friend=\""+friend+"\";";
        String query2="SELECT count(*) FROM Requests WHERE To_User=\""+friend+
                "\" AND By_User=\""+username+"\";";
        //setting statement, executing it and storing result in resultSet
        PreparedStatement preStat = connection.prepareStatement(query1);
        ResultSet result = preStat.executeQuery();

        //checking if select statement executed successfully
        if(result.next()){
            //returns count(*)!=0(ie if friend is related to username or not)  to client
            objectOutputStream.writeBoolean(true);
            objectOutputStream.flush();
            objectOutputStream.writeBoolean(result.getInt("count(*)") != 0);
            objectOutputStream.flush();
            if(result.getInt("count(*)")==0) {
                preStat = connection.prepareStatement(query2);
                result = preStat.executeQuery();
                if (result.next()) {
                    objectOutputStream.writeBoolean(true);
                    objectOutputStream.flush();
                    objectOutputStream.writeBoolean(result.getInt("count(*)") != 0);
                    objectOutputStream.flush();
                } else {
                    objectOutputStream.writeBoolean(false);
                    objectOutputStream.flush();
                }
            }
        }else {
            objectOutputStream.writeBoolean(false);
            objectOutputStream.flush();
        }
    }

    public void loginHandler() throws IOException, ClassNotFoundException, SQLException {
        //reading the User object
        user=(User)objectInputStream.readObject();

        //writing the sql query
        String query1 = "SELECT count(*) FROM Users WHERE Username=\""+user.getName()+
                "\" AND Password=\""+user.getPassword()+"\";";

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
    }

    public void signupHandler() throws IOException, ClassNotFoundException, SQLException {
        //reading the User object
        user=(User)objectInputStream.readObject();

        //writing the sql query
        String query1 = "SELECT count(*) FROM Users WHERE Username=\""+user.getName()+"\";";

        //setting statement, executing it and storing result in resultSet
        PreparedStatement preStat = connection.prepareStatement(query1);
        ResultSet result = preStat.executeQuery();

        //checking if select statement executed successfully
        if(result.next()){
            //checking if user with given username exists or not
            if(result.getInt("count(*)")==0){
                String query2="INSERT INTO Users(Username,Password) Value(\""+user.getName()+
                        "\",\""+user.getPassword()+"\");";
                PreparedStatement preparedStatement = connection.prepareStatement(query2);
                objectOutputStream.writeBoolean(preparedStatement.executeUpdate()==1);
            }else {
                objectOutputStream.writeBoolean(false);
            }
        }else{
            objectOutputStream.writeBoolean(false);
        }
        objectOutputStream.flush();
    }

    public void returnFriendList() throws IOException, ClassNotFoundException, SQLException {
        //reading username
        String username=(String)objectInputStream.readObject();

        //writing sql query
        String query1="SELECT count(*) FROM Friends WHERE Username=\""+username+"\";";
        String query2="SELECT Friend FROM Friends WHERE Username=\""+username+"\";";

        //setting statement, executing it and storing result in resultSet
        PreparedStatement preStat = connection.prepareStatement(query1);
        ResultSet result = preStat.executeQuery();

        //checking if select statement executed successfully
        if(result.next()){
             if(result.getInt("count(*)")!=0){
                 objectOutputStream.writeInt(result.getInt("count(*)"));
                 objectOutputStream.flush();
                 preStat = connection.prepareStatement(query2);
                 result = preStat.executeQuery();
                 while(result.next()){
                     objectOutputStream.writeObject(result.getString("Friend"));
                     objectOutputStream.flush();
                 }
             }else{
                 objectOutputStream.writeInt(0);
                 objectOutputStream.flush();
             }
        }else{
            objectOutputStream.writeInt(0);
            objectOutputStream.flush();
        }

    }

    public void closePipes() throws IOException {
        this.objectOutputStream.close();
        this.objectInputStream.close();
        this.socket.close();
    }
}
