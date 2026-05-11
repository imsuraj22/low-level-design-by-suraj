import enums.NotificationStatus;
import model.NotificationRequest;

public interface NotificationService {

    String send(NotificationRequest request);
    String schedule(NotificationRequest request);
    NotificationStatus getStatus(String notificationId);
}