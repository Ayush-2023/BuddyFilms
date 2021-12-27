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
    private WritableImage canvas;
    private Robot robot;


    CreateFrame(StatusData streaming,
                ObjectInputStream in,ObjectOutputStream out,
                String username)
    {
        this.streaming=streaming;
        this.objectInputStream=in;
        this.objectOutputStream=out;
        this.username=username;
        this.canvas=new WritableImage(1,1);
        this.robot=new Robot();
    }

    @Override
    public void run() {
        try{
            //writing username and size
            objectOutputStream.writeObject(this.username);
            objectOutputStream.flush();
            objectOutputStream.writeInt(100);
            objectOutputStream.flush();
            File imageFile=new File("E:\\Pictures\\pic1.jpg");
            int i;
            synchronized (streaming) {
                for (i = 0; (i < 100 )&(streaming.getStatus()); i++) {
                    System.out.println("Sending Status true and image");
                    objectOutputStream.writeBoolean(true);
                    objectOutputStream.flush();
                    System.out.print("Hello ");
//                    WritableImage imgReturn = robot.getScreenCapture(canvas, new Rectangle2D(0,0,1,1));
//                    System.out.println("World");
//                    ImageIO.write((RenderedImage) imgReturn,"jpg",imageFile);
                    objectOutputStream.writeObject(imageFile);
                    objectOutputStream.flush();
                    System.out.println("Image sent");
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        e.getMessage();
                    }
                }
            }
            System.out.println(i+"-->Sending end signal");
            objectOutputStream.writeBoolean(false);
            objectOutputStream.flush();

        }catch (Exception e){
            e.getMessage();
        }
    }
}
