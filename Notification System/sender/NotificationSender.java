package sender;

import model.Notification;

public interface NotificationSender {
    boolean send(Notification notification);

}
