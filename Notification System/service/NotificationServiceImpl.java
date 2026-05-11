import java.time.LocalDateTime;
import java.util.UUID;


import enums.ChannelType;
import enums.NotificationStatus;
import model.Notification;
import model.NotificationRequest;
import repository.NotificationRepository;
import sender.NotificationSender;
import sender.SenderFactory;

public class NotificationServiceImpl implements NotificationService {

    private NotificationRepository notificationRepository;
    private SenderFactory senderFactory;

    public NotificationServiceImpl(NotificationRepository notificationRepository, SenderFactory senderFactory) {
        this.notificationRepository = notificationRepository;
        this.senderFactory = senderFactory;
    }
    

    @Override
    public String send(NotificationRequest request) {
        String lastNotificationId = null;
        for(ChannelType channelType : request.getChannelTypes()) {
            String id=UUID.randomUUID().toString();
            Notification notification=new Notification(id,request.getUserId(),channelType);
            notification.setSubject(request.getSubject());
            notification.setBody(request.getBody());
            notificationRepository.save(notification);

            NotificationSender sender=senderFactory.getSender(channelType);
            boolean success=sender.send(notification);

            if(success){
                notification.setStatus(NotificationStatus.SENT);
                notification.setSentAt(LocalDateTime.now());
            }else{
                notification.setStatus(NotificationStatus.FAILED);
            }
            notificationRepository.update(notification);
            lastNotificationId=id;
        }
        return lastNotificationId;
    }

    @Override
    public String schedule(NotificationRequest request) {
        String id=UUID.randomUUID().toString();
        ChannelType channel=request.getChannelTypes().get(0);
        Notification notification=new Notification(id, request.getUserId(), channel);
        notification.setBody(request.getBody());
        notification.setSubject(request.getSubject());
        notification.setScheduledAt(request.getScheduledAt());
        notification.setStatus(NotificationStatus.PENDING);

        notificationRepository.save(notification);
        return id;
    }

    @Override
    public NotificationStatus getStatus(String notificationId) {
        return notificationRepository.findById(notificationId).map(Notification::getStatus).orElse(null);
    }
    
}
