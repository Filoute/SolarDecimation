package unnaincompris.LunaZ.Handlers;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import unnaincompris.LunaZ.Handlers.Manager.Handler;
import unnaincompris.LunaZ.utils.Color.ColorUtils;

public class DynamicHandler extends Handler implements Listener {

    @EventHandler(priority = EventPriority.LOW)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        event.setMessage(pingPlayer(event.getMessage()));

        event.setFormat(event.getPlayer().getDisplayName() + ColorUtils.translate(" &8>> &r" + event.getMessage()));
    }

    private String pingPlayer(String currentMessage) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(currentMessage.contains(player.getName())) {
                currentMessage = currentMessage.replace(player.getName(), ColorUtils.translate("&b@" + player.getName() + "&r"));
                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 100000.0f, 100000.0f);
            }
        }
        return currentMessage;
    }
}
