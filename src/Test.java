public class Test {
    public static void main(String[] args) {
        MyBlockingQueue queue = new MyBlockingQueue(10);
        Thread mailReceiver = new ReceiveMailImap(queue);
        Thread serializer = new Serializer(queue);
        mailReceiver.start();
        serializer.start();
    }
}