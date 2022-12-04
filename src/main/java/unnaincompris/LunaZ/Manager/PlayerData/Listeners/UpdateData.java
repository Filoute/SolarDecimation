package unnaincompris.LunaZ.Manager.PlayerData.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Zombie;
import org.bukkit.entity.ZombieHorse;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.PlayerData.PlayerData;

public class UpdateData implements Listener {
    public UpdateData() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onEntityDeath(EntityDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if(entity instanceof Zombie || entity instanceof ZombieHorse) {
            if(entity.getKiller() != null) {
                PlayerData data = Main.getInstance().getPlayerDataManager().getDataFromUUID(entity.getKiller().getUniqueId());
                data.setZombieKills(data.getZombieKills() + 1);
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if(player.getKiller() != null) {
            PlayerData killerData = Main.getInstance().getPlayerDataManager().getDataFromUUID(player.getKiller().getUniqueId());
            killerData.setPlayerKills(killerData.getPlayerKills() + 1);
        }
        PlayerData playerData = Main.getInstance().getPlayerDataManager().getDataFromUUID(player.getUniqueId());
        playerData.setPlayerDeath(playerData.getPlayerDeath() + 1);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockPlaceEvent event) {
        PlayerData playerData = Main.getInstance().getPlayerDataManager().getDataFromUUID(event.getPlayer().getUniqueId());
        playerData.setBlockPlace(playerData.getBlockPlace() + 1);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onBlockPlace(BlockBreakEvent event) {
        PlayerData playerData = Main.getInstance().getPlayerDataManager().getDataFromUUID(event.getPlayer().getUniqueId());
        playerData.setBlockBreak(playerData.getBlockBreak() + 1);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMessageSent(AsyncPlayerChatEvent event) {
        PlayerData playerData = Main.getInstance().getPlayerDataManager().getDataFromUUID(event.getPlayer().getUniqueId());
        playerData.setMessageSent(playerData.getMessageSent() + 1);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onMessageSent(PlayerCommandPreprocessEvent event) {
        PlayerData playerData = Main.getInstance().getPlayerDataManager().getDataFromUUID(event.getPlayer().getUniqueId());
        playerData.setCommandExecuted(playerData.getCommandExecuted() + 1);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerMove(PlayerMoveEvent event) {
        PlayerData playerData = Main.getInstance().getPlayerDataManager().getDataFromUUID(event.getPlayer().getUniqueId());
        final Location to = event.getTo();
        final Location from = event.getFrom();
        if(to != null && (to.getX() != from.getX() || to.getY() != from.getY() || to.getZ() != from.getZ())) {
            double blockTravel = Math.abs(from.getX() - to.getX()) + Math.abs(from.getZ() - to.getZ());
            playerData.setBlockTravel((long) (playerData.getBlockTravel() + blockTravel));
            if(to.getY() - from.getY() > 0) {
                double blockStep = to.getY() - from.getY();
                playerData.setBlockStep((long) (playerData.getBlockStep() + blockStep));
            }
        }
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        Main.getInstance().getPlayerDataManager().savePlayerData(event.getPlayer().getUniqueId());
    }
}
