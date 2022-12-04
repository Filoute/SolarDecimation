package unnaincompris.LunaZ.Manager.Weapon.Magazine.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Weapon.Magazine.MagazineManager;

public class DynamicListeners implements Listener {

    public DynamicListeners() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        manager = Main.getInstance().getWeaponManager().getMagazineManager();
    }

    private final MagazineManager manager;

    @EventHandler
    public void onMagazineReload(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(item.getType() == Material.AIR) return;
        if(event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if(!event.getPlayer().isSneaking()) return;
        if(!manager.isAnMagazine(item)) return;
        event.setCancelled(true);
        manager.getMagazine(item).reload(item, event.getPlayer());
    }

}