package client;

import dataClasses.StatusData;
import javafx.geometry.Rectangle2D;
import javafx.scene.image.WritableImage;
import javafx.scene.robot.Robot;

import javax.imageio.ImageIO;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

public class CreateFrame implements Runnable {
    private String username;
    private ObjectOutputStream objectOutputStream;
    private ObjectInputStream objectInputStream;
    private StatusData streaming;


    CreateFrame(StatusData streaming,
                ObjectInputStream in,ObjectOutputStream out,
                String username)
    {
        this.streaming=streaming;
        this.objectInputStream=in;
        this.objectOutputStream=out;
        this.username=username;
    }

    @Override
    public synchronized void run() {
        try{
            //writing username and size
            objectOutputStream.writeObject(this.username);
            objectOutputStream.flush();
            objectOutputStream.writeInt(100);
            objectOutputStream.flush();
            File imageFile=new File("E:\\Pictures\\pic1.jpg");
            for(int i=0;i<100&&streaming.getStatus();i++){
                System.out.println("Sending Status true and image");
                objectOutputStream.writeObject(Boolean.valueOf(true));
                objectOutputStream.flush();
                System.out.print("Hello " );
//                WritableImage canvas = new WritableImage(200,200);
//                javafx.scene.robot.Robot robot = new Robot();
//                WritableImage imgReturn = robot.getScreenCapture(canvas, new Rectangle2D(0,0,200,200));
//                ImageIO.write((RenderedImage) imgReturn,"jpg",imageFile);
                System.out.println("World");
                objectOutputStream.writeObject(imageFile);
                objectOutputStream.flush();
                System.out.println("Image sent");
                try{
                    Thread.sleep(1);
                }catch (InterruptedException e){
                    e.getMessage();
                }
            }
            objectOutputStream.writeObject(Boolean.valueOf(false));
            objectOutputStream.flush();

        }catch (Exception e){
            e.getMessage();
        }
    }
}
