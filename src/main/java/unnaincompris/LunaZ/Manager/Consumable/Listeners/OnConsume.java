package unnaincompris.LunaZ.Manager.Consumable.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Consumable.ConsumableManager;
import unnaincompris.LunaZ.utils.StringUtils;

public class OnConsume implements Listener {
    public OnConsume() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    private ConsumableManager manager = Main.getInstance().getConsumableManager();

    @EventHandler
    public void onConsume(PlayerItemConsumeEvent event) {
        if(manager == null)
            manager = Main.getInstance().getConsumableManager();
        ItemStack item = event.getItem();
        StringUtils.log("1");
        if(item.getType() != Material.CHORUS_FRUIT) return;
        StringUtils.log("2");
        if(manager.isAnConsumable(item)) {
            StringUtils.log("3");
            event.setCancelled(true);
            Main.getInstance().getConsumableManager().getConsumable(item).apply(event.getPlayer());
        }
    }
}
