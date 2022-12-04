package unnaincompris.LunaZ.Manager.ActionBarManager;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;
import unnaincompris.LunaZ.utils.Color.ColorUtils;

import java.util.ArrayList;
import java.util.List;

public class Message {

    public final int showTime;
    public List<MessageUpdater> message = new ArrayList<>();
    public int showCounter = 0;
    public final String messageReason;

    public Message(int showTime, String messageReason, MessageUpdater message) {
        this.message.add(message); this.showTime = showTime; this.messageReason = messageReason;
    }

    public void add(MessageUpdater newString) {
        showCounter = 0;
        message.add(newString);
    }

    public boolean show(Player player) {
        StringBuilder toDisplay = new StringBuilder();
        for(MessageUpdater string : this.message)
            toDisplay.append(string.getMessage());
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(ColorUtils.translate(toDisplay.toString())));
        showCounter++;
        return showCounter >= showTime;
    }

    public interface MessageUpdater {
        String getMessage();
    }
}
