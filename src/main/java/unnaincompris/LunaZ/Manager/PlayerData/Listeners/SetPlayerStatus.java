package unnaincompris.LunaZ.Manager.PlayerData.Listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class SetPlayerStatus implements Listener {

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        setStatus(event.getPlayer());
    }

    //TODO add in main
    public static void setStatus(Player player) {
        //TODO
    }
}
