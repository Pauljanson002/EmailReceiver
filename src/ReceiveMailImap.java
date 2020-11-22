import javax.mail.*;
import javax.mail.search.FlagTerm;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;

public class ReceiveMailImap extends Thread implements EmailObservable{
    private ArrayList<EmailStatObserver> observerList;
    private MyBlockingQueue queue;
    private int counter;
    public ReceiveMailImap(MyBlockingQueue queue) {
        this.queue = queue;
        counter = 1;
        observerList = new ArrayList<>();
    }



    public  void doit() throws MessagingException, IOException {
        Folder folder = null;
        Store store = null;
        try {
            Properties props = System.getProperties();
            props.setProperty("mail.store.protocol", "imaps");

            Session session = Session.getDefaultInstance(props, null);
            // session.setDebug(true);
            store = session.getStore("imaps");
            store.connect("imap.gmail.com","user", "pass");
            folder = store.getFolder("Inbox");
            /* Others GMail folders :
             * [Gmail]/All Mail   This folder contains all of your Gmail messages.
             * [Gmail]/Drafts     Your drafts.
             * [Gmail]/Sent Mail  Messages you sent to other people.
             * [Gmail]/Spam       Messages marked as spam.
             * [Gmail]/Starred    Starred messages.
             * [Gmail]/Trash      Messages deleted from Gmail.
             */
            folder.open(Folder.READ_WRITE);
            Message messages[] = folder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            System.out.println("No of Messages : " + folder.getMessageCount());
            System.out.println("No of Unread Messages : " + folder.getUnreadMessageCount());
            for (int i=messages.length-1; i >= 0; --i) {
                Message msg = messages[i];
                if (!msg.isSet(Flags.Flag.SEEN)) {
                    System.out.println("MESSAGE #" + (counter++) + ":");
                    notifyAllObservers();
                    String from = "unknown";
                    if (msg.getReplyTo().length >= 1) {
                        from = msg.getReplyTo()[0].toString();
                    }
                    else if (msg.getFrom().length >= 1) {
                        from = msg.getFrom()[0].toString();
                    }
                    String subject = msg.getSubject();
                    //   System.out.println("Saving ... " + subject +" " + from);
                    saveParts(from,subject,msg.getContent());
                    msg.setFlag(Flags.Flag.SEEN,true);
                }}
        } finally {
            if (folder != null) { folder.close(true); }
            if (store != null) { store.close(); }
        }
    }

    public  void saveParts(String from,String subject,Object content)
    {
        try {
            if (content instanceof Multipart) {
                StringBuffer messageContent = new StringBuffer();
                Multipart multipart = (Multipart) content;
                for (int i = 0; i < multipart.getCount(); i++) {
                    Part part = multipart.getBodyPart(i);
                    if (part.isMimeType("text/plain")) {
                        messageContent.append(part.getContent().toString());
                    }
                }
                Email mail = new Email(from,subject,messageContent.toString());
                System.out.println("Mail enqueued "+mail.getSubject());
                queue.enqueue(mail);
                return;
            }
            Email mail = new Email(from,subject,content.toString());
            System.out.println("Mail enqueued "+mail.getSubject());
            queue.enqueue(mail);
            return;

        } catch (IOException | MessagingException | InterruptedException e) {
            e.printStackTrace();
        }
        return;
    }
    @Override
    public void addObserver(EmailStatObserver o) {
        observerList.add(o);
    }

    @Override
    public void removeObserver(EmailStatObserver o) {
        observerList.remove(o);
    }

    @Override
    public void notifyAllObservers() {
        for(EmailStatObserver o : observerList){
            o.update(this);
        }
    }

    public  void run()  {
        try {
            EmailStatPrinter printer = new EmailStatPrinter();
            EmailStatRecorder recorder = new EmailStatRecorder();
            addObserver(printer);
            addObserver(recorder);
            while(true) {
                doit();
                Thread.sleep(1000);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}