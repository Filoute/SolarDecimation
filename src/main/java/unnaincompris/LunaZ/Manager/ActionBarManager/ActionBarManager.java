package unnaincompris.LunaZ.Manager.ActionBarManager;

import org.bukkit.entity.Player;
import unnaincompris.LunaZ.utils.BukkitTasks;
import unnaincompris.LunaZ.utils.ManagerEnabler;

import java.util.ArrayList;
import java.util.List;

public class ActionBarManager implements ManagerEnabler {
    private final List<PlayerMessage> playerMessages = new ArrayList<>();

    public ActionBarManager() {
        BukkitTasks.syncTimer(this::display, 0, 1);
    }

    private void display() {
        if(playerMessages.isEmpty()) return;
        for(PlayerMessage playerMessage : this.playerMessages) {
            playerMessage.display();
        }
    }

    public void add(Player player, Message message) {
        PlayerMessage playerMessage = getByPlayer(player);
        if(playerMessage == null) {
            playerMessage = new PlayerMessage(player);
            this.playerMessages.add(playerMessage);
        }
        if(playerMessage.getByReason(message.messageReason) != null)
            playerMessage.set(message);
        else
            playerMessage.add(message);
    }

    public void addPriority(Player player, Message message) {
        PlayerMessage playerMessage = getByPlayer(player);
        if(playerMessage == null) {
            playerMessage = new PlayerMessage(player);
            this.playerMessages.add(playerMessage);
        }
        if(playerMessage.getByReason(message.messageReason) != null)
            playerMessage.setPriority(message);
        else
            playerMessage.addPriority(message);
    }

    public void removeByReason(Player player, String reason) {
        removeByReason(getByPlayer(player), reason);
    }

    public void removeByReason(PlayerMessage playerMessage, String reason) {
        playerMessage.removeByReason(reason);
    }

    public PlayerMessage getByPlayer(Player player) {
        for(PlayerMessage playerMessage : this.playerMessages)
            if(playerMessage.getPlayer().equals(player))
                return playerMessage;
        PlayerMessage playerMessage = new PlayerMessage(player);
        this.playerMessages.add(playerMessage);
        return playerMessage;
    }
}
