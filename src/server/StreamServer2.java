package server;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class StreamServer2 {
    private String host;
    private int size;
    private File[] imageFiles;
    private Connection connection;

    StreamServer2(ObjectOutputStream objectOutputStream, ObjectInputStream objectInputStream,int portNumber){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String url = "jdbc:mysql://localhost:3306/streamapp?autoReconnect=true&useSSL=false";
            this.connection = DriverManager.getConnection(url, "root", "TetraM0s");
            //reading usernames
            this.host=(String)objectInputStream.readObject();
            this.size=(int)objectInputStream.readInt();
            this.imageFiles=new File[this.size];
            for(int i=0;i<this.size;i++){
                imageFiles[i]=(File)objectInputStream.readObject();
            }

            //writing sql query
            String query="INSERT INTO Streams(ID,Code,Size) VALUE(\""+this.host+
                    "\","+portNumber+","+this.size+");";

            //executing sql statements
            PreparedStatement preStat= connection.prepareStatement(query);
            objectOutputStream.writeBoolean(preStat.executeUpdate()==1);
            objectOutputStream.flush();


        } catch (IOException | ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    public void exitStream(ObjectOutputStream objectOutputStream){
        try {
            //writing sql query
            String query = "DELETE FROM Streams WHERE ID=\"" + this.host + "\";";

            //executing sql statements
            PreparedStatement preStat = connection.prepareStatement(query);
            objectOutputStream.writeBoolean(preStat.executeUpdate() == 1);
            objectOutputStream.flush();
        }catch(SQLException |IOException e){
            e.getMessage();
        }
    }

    public File[] getFrames(){
        return this.imageFiles;
    }


}
