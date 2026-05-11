import java.util.Arrays;

import enums.ChannelType;
import model.NotificationRequest;
import repository.InMemoryNotificationRepository;
import repository.NotificationRepository;
import sender.SenderFactory;

public class Main {
    public static void main(String[] args) {
        NotificationRepository repository=new InMemoryNotificationRepository();

        SenderFactory senderFactory=new SenderFactoryImpl();
        NotificationService notificationService=new NotificationServiceImpl(repository, senderFactory);

        NotificationRequest notificationRequest=new NotificationRequest();

        notificationRequest.setUserId(101L);
        notificationRequest.setChannelTypes(Arrays.asList(
            ChannelType.EMAIL,ChannelType.IN_APP,ChannelType.PUSH
        ));

        notificationRequest.setSubject("Order Update");
        notificationRequest.setBody("Your order has been placed successfully");

        String NotificationId=notificationService.send(notificationRequest);
        System.out.println("Final Status : "+notificationService.getStatus(NotificationId));
    }
}
