import java.io.Serializable;

public  class Email implements Serializable {
    private String emailAddress;
    private String subject;
    private String content;


    public Email(String emailAddress, String subject, String content) {
        this.emailAddress = emailAddress;
        this.subject = subject;
        this.content = content;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public String getSubject() {
        return subject;
    }

    public String getContent() {
        return content;
    }

    public String getDetails(){
        return this.emailAddress+" : "+this.subject+" : "+this.content;
    }
}