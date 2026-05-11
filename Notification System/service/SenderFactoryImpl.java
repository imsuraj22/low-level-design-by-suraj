import java.util.HashMap;
import java.util.Map;

import enums.ChannelType;
import sender.EmailSender;
import sender.InAppSender;
import sender.NotificationSender;
import sender.PushSender;
import sender.SenderFactory;

public class SenderFactoryImpl implements SenderFactory {
    private Map<ChannelType,NotificationSender> sendMap;

    public SenderFactoryImpl(){
        sendMap=new HashMap<>();
        sendMap.put(ChannelType.EMAIL, new EmailSender());
        sendMap.put(ChannelType.PUSH, new PushSender());
        sendMap.put(ChannelType.IN_APP, new InAppSender());
    }

    public NotificationSender getSender(ChannelType channelType){
        return sendMap.get(channelType);
    }
}
