import java.io.FileWriter;
import java.io.IOException;

class EmailStatRecorder implements EmailStatObserver{
    public  void recordData(){
        FileWriter outputStream = null;
        try {
            outputStream = new FileWriter("record.txt",true);
            outputStream.append("\n"+"An email received at "+java.time.LocalTime.now());
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void update(EmailObservable obj) {
        recordData();
    }
}