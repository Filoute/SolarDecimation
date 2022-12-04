package unnaincompris.LunaZ.Manager.ActionBarManager;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PlayerMessage {
    @Getter private final Player player;
    private final List<Message> messages = new ArrayList<>();

    public PlayerMessage(Player player) {
        this.player = player;
    }

    public void display() {
        if(messages.isEmpty()) return;
        final Message message = messages.get(0);
        if(message.show(player)) {
            messages.remove(0);
        }
    }

    public Message getByReason(String reason) {
        for(Message message : messages)
            if(message.messageReason.equalsIgnoreCase(reason))
                return message;
        return null;
    }

    public void removeByReason(String reason) {
        Message toRemove = null;
        for (Message message : messages)
            if (message.messageReason.equalsIgnoreCase(reason)) {
                toRemove = message;
                break;
            }
        if (toRemove == null) return;
        this.messages.remove(toRemove);
    }

    public void add(Message message) {
        this.messages.add(message);
    }
    public void set(Message message) {
        int index = this.messages.indexOf(getByReason(message.messageReason));
        this.messages.set(index, message);
    }
    public void addPriority(Message message) {
        this.messages.add(0, message);
    }
    public void setPriority(Message message) {
        this.messages.remove(getByReason(message.messageReason));
        this.messages.add(0, message);
    }
}
