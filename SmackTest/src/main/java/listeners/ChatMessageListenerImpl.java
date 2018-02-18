package listeners;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import org.jivesoftware.smack.chat.Chat;
import org.jivesoftware.smack.chat.ChatManagerListener;
import org.jivesoftware.smack.chat.ChatMessageListener;
import org.jivesoftware.smack.packet.Message;

import java.util.*;


public class ChatMessageListenerImpl implements ChatManagerListener, ChatMessageListener {
    private ImageView image;
    private List newMsg;

    public ChatMessageListenerImpl() {
    }

    public ChatMessageListenerImpl(ImageView image, List newMsg) {
        this.newMsg = newMsg;
        this.image=image;
    }

    public List getNewMsg() {
        return newMsg;
    }

    @Override
    public void chatCreated(Chat chat, boolean createdLocally) {
        if (!createdLocally) {
            chat.addMessageListener(this);
        }
    }

    @Override
    public void processMessage(Chat chat, Message message) {
        newMsg.add(message.getBody());
        image.setImage(new Image(getClass().getResource("/pictures/newMessage.png").toString(), 512, 512, true, true));
    }
}
