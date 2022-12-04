package unnaincompris.LunaZ.utils.InteractiveAsk;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.StringUtils;

import java.util.UUID;
import java.util.function.Consumer;

public class StringAsk implements Listener {
    private final UUID playerUUID;


    private final Consumer<String> action;

    private boolean finished = false;

    public StringAsk(Player player,  Consumer<String> action) {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().ASK_STRING, "{cancel}->" + LanguageManager.getLanguage().CANCEL_KEYWORD)));

        this.playerUUID = player.getUniqueId();
        this.action = action;
        player.closeInventory();
    }
    public void end(String value) {
        if(finished) return;
        this.finished = true;
        action.accept(value);

        HandlerList.unregisterAll(this);
        try { this.finalize(); } catch (Throwable ignored){}
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        if(event.getPlayer().getUniqueId() != playerUUID) return;
        end(LanguageManager.getLanguage().CANCEL_KEYWORD);
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if(event.getPlayer().getUniqueId() != playerUUID) return;
        event.setCancelled(true);
        end(ColorUtils.reversedTranslate(event.getMessage()));
    }
}
