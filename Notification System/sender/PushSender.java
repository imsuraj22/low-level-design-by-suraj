package sender;

import model.Notification;

public class PushSender implements NotificationSender {

	@Override
	public boolean send(Notification notification) {
		System.out.println(
            "Sending Push Notification to User "
            + notification.getUserId()
            + " : "
            + notification.getBody()
        );
		return true;

	}
}
