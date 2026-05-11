package sender;

import enums.ChannelType;

public interface SenderFactory {
    NotificationSender getSender(ChannelType channelType);
}
