import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class Serializer extends Thread{
    private MyBlockingQueue queue;
    private int counter;

    public Serializer(MyBlockingQueue queue) {
        this.queue = queue;
        counter = 1;
    }

    @Override
    public void run() {
        while(true){
            try {
                Email mail = queue.dequeue();
                FileOutputStream fos = new FileOutputStream("inbox/"+counter+".ser");
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(mail);
                System.out.println("MESSAGE #"+counter+" is serialized .");
                oos.close();
                fos.close();
                counter++;

            } catch (InterruptedException | FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
