package unnaincompris.LunaZ.Manager.ScoreboardManager;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.utils.BukkitTasks;
import unnaincompris.LunaZ.utils.ManagerEnabler;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ScoreboardManager implements ManagerEnabler, Listener {
    private final Map<UUID, ScoreboardUpdater> boards = new HashMap<>();

    public ScoreboardManager() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        Bukkit.getOnlinePlayers().forEach(this::loadBoard);

        BukkitTasks.asyncTimer(() -> {
            boards.values().forEach(ScoreboardUpdater::run);
        }, 20, 20);
    }

    public void loadBoard(Player player) {
        ScoreboardUpdater updater = new ScoreboardUpdater(this, player);
        this.boards.put(player.getUniqueId(), updater);
    }

    public void unloadBoard(Player player) {
        this.boards.remove(player.getUniqueId());
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        loadBoard(player);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        unloadBoard(player);
    }
}
