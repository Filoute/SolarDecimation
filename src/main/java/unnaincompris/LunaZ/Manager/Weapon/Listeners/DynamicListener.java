package unnaincompris.LunaZ.Manager.Weapon.Listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.PlayerData.PlayerStatus;
import unnaincompris.LunaZ.Manager.Weapon.Weapon;
import unnaincompris.LunaZ.Manager.Weapon.WeaponManager;
import unnaincompris.LunaZ.utils.NBTUtils;

public class DynamicListener implements Listener {
    public DynamicListener() {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        manager = Main.getInstance().getWeaponManager();
    }
    private final WeaponManager manager;

    @EventHandler
    public void onChangeItemToApplySpeed(PlayerItemHeldEvent event) {
        final Player player = event.getPlayer();
        final ItemStack item = player.getInventory().getItem(event.getNewSlot());
        final PlayerStatus playerStatus = PlayerStatus.getPlayerStatus(player);
        if(item == null || item.getType() == Material.AIR || !manager.isAnWeapon(item)) {
            if(manager.isAnWeapon(player.getInventory().getItem(event.getPreviousSlot())))
                playerStatus.cancelByReason("HoldWeapon", true);
            return;
        }
        final Weapon weapon = manager.getWeapon(item);
        playerStatus.applyValueSpeed("HoldWeapon", playerStatus.getDefaultSpeed() - (weapon.weight / 1000f), -1);
    }

    @EventHandler
    public void onClickToShoot(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(item.getType() == Material.AIR) return;
        if(event.getAction() != Action.RIGHT_CLICK_AIR && event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if(!Main.getInstance().getWeaponManager().isAnWeapon(item)) return;
        event.setCancelled(true);
        Player player = event.getPlayer();
        Weapon weapon = Main.getInstance().getWeaponManager().getWeapon(item);
        int reamingMagazine = NBTUtils.toNBT(item).getInteger("WeaponReamingMagazine");
        if(reamingMagazine > 0)
            weapon.shoot(player, item);
        else
            weapon.reload(item, player);
    }

    @EventHandler
    public void onClickToReload(PlayerInteractEvent event) {
        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if(item.getType() == Material.AIR) return;
        if(event.getAction() != Action.LEFT_CLICK_AIR && event.getAction() != Action.LEFT_CLICK_BLOCK) return;
        if(!event.getPlayer().isSneaking()) return;
        if(!manager.isAnWeapon(item)) return;
        event.setCancelled(true);
        manager.getWeapon(item).reload(item, event.getPlayer());
    }

    @EventHandler
    public void onSneakToDisplayBullet(PlayerToggleSneakEvent event) {
        final ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        if (item.getType() == Material.AIR) return;
        if (manager.isAnWeapon(item)) {
            final Player player = event.getPlayer();
            final Weapon weapon = manager.getWeapon(item);
            weapon.displayBullet(player, item);
        }
    }
}