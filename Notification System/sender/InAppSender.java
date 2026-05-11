package sender;

import model.Notification;

public class InAppSender implements NotificationSender {
	@Override
	public boolean send(Notification notification) {
System.out.println(
            "Sending In-App Notification to User "
            + notification.getUserId()
            + " : "
            + notification.getBody()
        );
		
		return true;
	}
}
