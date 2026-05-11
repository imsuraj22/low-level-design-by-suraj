package repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import enums.NotificationStatus;
import model.Notification;

public class InMemoryNotificationRepository implements NotificationRepository{
    
    private ConcurrentHashMap<String,Notification> notificationMap;

    public InMemoryNotificationRepository(){
        this.notificationMap=new ConcurrentHashMap<>();
    }

    @Override
    public void save(Notification notification) {
        notificationMap.put(notification.getId(), notification);
    }

    @Override
    public Optional<Notification> findById(String id) {
        return Optional.ofNullable(notificationMap.get(id));
    }

    @Override
    public void update(Notification notification) {
        notificationMap.put(notification.getBody(), notification);
    }

    @Override
    public List<Notification> findByStatus(NotificationStatus status) {
       List<Notification> result=new ArrayList<>();
       for(Notification notification:notificationMap.values()){
        if(notification.getStatus()==status){
            result.add(notification);
        }
       }
       return result;
    }
    
     
}
