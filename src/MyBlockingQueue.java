import java.util.LinkedList;

class MyBlockingQueue {
    LinkedList<Email> queue;
    int size;

    public MyBlockingQueue(int size) {
        this.size = size;
        queue = new LinkedList<Email>();
    }

    public synchronized void enqueue(Email item) throws InterruptedException  {
        while(this.queue.size() == this.size) {
            wait();
        }
        this.queue.add(item);
        if(this.queue.size() == 1) {
            notifyAll();
        }
    }


    public synchronized Email dequeue() throws InterruptedException{
        while(this.queue.size() == 0){
            wait();
        }
        if(this.queue.size() == this.size){
            notifyAll();
        }

        return this.queue.remove(0);
    }

}