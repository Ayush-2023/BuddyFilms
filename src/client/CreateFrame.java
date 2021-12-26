package client;

import javafx.scene.image.Image;

import java.io.*;
import java.net.Socket;
import java.util.Objects;

public class CreateFrame implements Runnable {
    private String username;
    private File imageDir;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;

    CreateFrame(ObjectInputStream objectInputStream,ObjectOutputStream objectOutputStream,
                String username) throws IOException {
        this(new String("E:\\Pictures\\New folder"),objectInputStream,objectOutputStream,username);
    }

    CreateFrame(String directory,ObjectInputStream objectInputStream,ObjectOutputStream objectOutputStream,
               String username) throws IOException {
        this.imageDir=new File(directory);
        this.objectInputStream=objectInputStream;
        this.objectOutputStream=objectOutputStream;
    }

    @Override
    public void run() {
        try{
            //writing username
            objectOutputStream.writeObject(this.username);
            objectOutputStream.flush();
            if(this.imageDir.isDirectory()) {
                objectOutputStream.writeInt(Objects.requireNonNull(this.imageDir.list()).length);
                objectOutputStream.flush();
                for(File file: Objects.requireNonNull(imageDir.listFiles())){
                    objectOutputStream.writeObject(file);
                    objectOutputStream.flush();
                }
            }

            //reading status
            //for debugging
            System.out.println("Getting status");
            if(objectInputStream.readBoolean()) {
                System.out.println("Successfully transferred all the files");
            }else{
                System.out.println("Error");
            }


        }catch (Exception e){
            e.getMessage();
        }
    }
}
