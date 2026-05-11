package sender;

import model.Notification;

public class EmailSender implements NotificationSender {
    @Override
    public boolean send(Notification notification) {
        System.out.println("Sending Email to User "+notification.getUserId()+" : "+notification.getBody());
        return true;
    }
    
}
