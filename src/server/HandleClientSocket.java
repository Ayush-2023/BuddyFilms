package server;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import dataClasses.StatusData;
import dataClasses.User;
import javafx.scene.image.WritableImage;

public class HandleClientSocket implements Runnable {
    private User user;

    private Socket socket;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private Connection connection;

    private Boolean streaming;

    public HandleClientSocket(Socket socket) throws IOException, ClassNotFoundException, SQLException {
        this.socket=socket;
        user=new User();
        objectInputStream=new ObjectInputStream(socket.getInputStream());
        objectOutputStream=new ObjectOutputStream(socket.getOutputStream());
        Class.forName("com.mysql.jdbc.Driver");
        String url = "jdbc:mysql://localhost:3306/streamapp?autoReconnect=true&useSSL=false";
        this.connection = DriverManager.getConnection(url, "root", "TetraM0s");
        streaming=false;
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
                case "Get Request List" -> this.returnRequestList();
                case "Accept Request" -> this.acceptRequest();
                case "Search Users" -> this.searchUsersHandler();
                case "Reject Request" -> this.rejectRequest();
                case "Start Stream" -> this.startStream();
            }
            this.closePipes();
            System.out.println("Client Handled");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void startStream() throws IOException, ClassNotFoundException, SQLException {
        StatusData streaming=new StatusData();
        synchronized (streaming) {


            //sending the port address
            objectOutputStream.writeInt(20000);
            objectOutputStream.flush();
            //reading the host(id) and size

            String host=(String)objectInputStream.readObject();
            int size=(int)objectInputStream.readInt();

            //thread for handling join Stream
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        ServerSocket serverSocket = new ServerSocket(20000);
                        while (streaming.getStatus()) {
                            Socket socket = serverSocket.accept();
                            //
                            new Thread(new StreamServer(socket)).start();
                        }
                        serverSocket.close();
                    }catch(IOException e){
                        e.printStackTrace();
                    }
                }
            }).start();

            //thread for inserting the new Entry
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //writing sql query
                        String query1 = "INSERT INTO Streams(ID,Code,Size,Name) VALUE(\"" + host +
                                "\"," + 20000 + "," + size + ",\"Video\");";

                        String query2 ="INSERT INTO Streamdata(Host,Frame_no) VALUE(\""+host+
                                "\",0);";
                        //executing sql statements
                        PreparedStatement preStat = connection.prepareStatement(query1);
                        preStat.executeUpdate();
                        preStat=connection.prepareStatement(query2);
                        preStat.executeUpdate();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
            File imageFile;
            for(int i=0;i<size;i++){
               System.out.print(i+"-->");
               if(objectInputStream.readBoolean()) {
                   streaming.setStatus(true);
                   //for debugging
                   System.out.println("Status true, reading object");
                   imageFile=(File)objectInputStream.readObject();
                   InputStream in = new FileInputStream(imageFile);
                   String query = "UPDATE Streamdata SET Image=?,Frame_no=(Frame_no+1) WHERE Host=\"" + host + "\";";
                   PreparedStatement preStat = connection.prepareStatement(query);
                   preStat.setBlob(1, in);
                   preStat.executeUpdate();
                   try{
                       Thread.sleep(1);
                   }catch (InterruptedException e){
                       e.printStackTrace();
                   }
               }else{
                   streaming.setStatus(false);
                   break;
               }
            }

            String query1 = "DELETE from Streamdata WHERE Host=\"" + host + "\";";
            String query2 = "DELETE from Streams WHERE ID=\"" + host + "\";";
            PreparedStatement preStat;
            preStat= connection.prepareStatement(query1);
            preStat.executeUpdate();
            preStat = connection.prepareStatement(query2);
            preStat.executeUpdate();

        }
    }

    private void rejectRequest() throws IOException, ClassNotFoundException, SQLException {
        //reading usernames
        String username=(String)objectInputStream.readObject();
        String friend=(String)objectInputStream.readObject();

        //writing sql query
        String query="DELETE FROM Requests WHERE To_User=\""+username+
                "\" AND By_USER=\""+friend+"\";";

        PreparedStatement preStat;

        preStat = connection.prepareStatement(query);
        objectOutputStream.writeBoolean(preStat.executeUpdate()==1);
        objectOutputStream.flush();
    }

    private void searchUsersHandler() throws IOException, ClassNotFoundException, SQLException {
        //reading pattern
        String username=(String)objectInputStream.readObject();
        String pattern=(String)objectInputStream.readObject();

        //writing sql query
        String query1="SELECT count(*) FROM Users WHERE Username LIKE \"%"+pattern+
                "%\" AND Username!=\""+username+"\";";
        String query2="SELECT Username FROM Users WHERE Username LIKE \"%"+pattern+
                "%\" AND Username!=\""+username+"\";";

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
                    objectOutputStream.writeObject(result.getString("Username"));
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

    private void acceptRequest() throws IOException, ClassNotFoundException, SQLException {
        //reading usernames
        String username=(String)objectInputStream.readObject();
        String friend=(String)objectInputStream.readObject();

        //writing sql query
        String query1="INSERT INTO Friends(Username,Friend) Value(\""+username+
                "\",\""+friend+"\");";
        String query2="INSERT INTO Friends(Username,Friend) Value(\""+friend+
                "\",\""+username+"\");";
        String query3="DELETE FROM Requests WHERE To_User=\""+username+
                "\" AND By_USER=\""+friend+"\";";

        PreparedStatement preStat;

        preStat = connection.prepareStatement(query1);
        objectOutputStream.writeBoolean(preStat.executeUpdate()==1);
        objectOutputStream.flush();
        preStat = connection.prepareStatement(query2);
        objectOutputStream.writeBoolean(preStat.executeUpdate()==1);
        objectOutputStream.flush();
        preStat = connection.prepareStatement(query3);
        objectOutputStream.writeBoolean(preStat.executeUpdate()==1);
        objectOutputStream.flush();
    }

    private void returnRequestList() throws IOException, ClassNotFoundException, SQLException {
        //reading username
        String username=(String)objectInputStream.readObject();

        //writing sql query
        String query1="SELECT count(*) FROM Requests WHERE To_User=\""+username+"\";";
        String query2="SELECT By_User FROM Requests WHERE To_User=\""+username+"\";";

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
                    objectOutputStream.writeObject(result.getString("By_User"));
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
                } else {
                    objectOutputStream.writeBoolean(false);
                }
                objectOutputStream.flush();
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
