public class EmailStatPrinter implements EmailStatObserver {

    public void printEmail(){
        System.out.println("An email received at "+java.time.LocalTime.now());
    }
    @Override
    public void update(EmailObservable obj) {
        printEmail();
    }
}
