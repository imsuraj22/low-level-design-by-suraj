package model;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import enums.ChannelType;
import enums.NotificationType;

public class NotificationRequest {
    private Long userId;
    private List<ChannelType> channelTypes;
    private String subject;   // optional
    private String body;


    private LocalDateTime scheduledAt;
    

    private String idempotencyKey;

    public NotificationRequest(){
        
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public List<ChannelType> getChannelTypes() {
        return channelTypes;
    }

    public void setChannelTypes(List<ChannelType> channelTypes) {
        this.channelTypes = channelTypes;
    }

   

    public LocalDateTime getScheduledAt() {
        return scheduledAt;
    }

    public void setScheduledAt(LocalDateTime scheduledAt) {
        this.scheduledAt = scheduledAt;
    }

    public String getIdempotencyKey() {
        return idempotencyKey;
    }

    public void setIdempotencyKey(String idempotencyKey) {
        this.idempotencyKey = idempotencyKey;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

   
}
