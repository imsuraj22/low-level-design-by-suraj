package repository;

import java.util.List;
import java.util.Optional;

import enums.NotificationStatus;
import model.Notification;

public interface NotificationRepository {

    void save(Notification notification);

    Optional<Notification> findById(String id);

    void update(Notification notification);

    List<Notification> findByStatus(NotificationStatus status);
}