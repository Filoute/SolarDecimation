package unnaincompris.LunaZ.Manager.Weapon.Magazine;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.ActionBarManager.ActionBarManager;
import unnaincompris.LunaZ.Manager.ActionBarManager.Message;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.Manager.PlayerData.PlayerStatus;
import unnaincompris.LunaZ.Manager.Weapon.Bullet.Bullet;
import unnaincompris.LunaZ.utils.ItemUtils;
import unnaincompris.LunaZ.utils.NBTUtils;
import unnaincompris.LunaZ.utils.StringUtils;

public class Magazine {

    public final int magazineSize;
    public int currentBullet;
    public final String magazineDisplayName;
    public final String systemName;
    public final Bullet bullet;
    public final int modelData;
    public final boolean canBeReloaded; // Info With bullet
    public final int timePerBullet;

    public ItemStack getItem() {
        return new NBTUtils(ItemUtils.setCustomModelTexture(
                ItemUtils.createItem(Material.OAK_PLANKS, magazineDisplayName + " &7Magazine [" + currentBullet + "/" + magazineSize + "]", 1, null, null), modelData))
                .set("magazineSystemName", systemName).set("magazineType", bullet.bulletType).set("remainBullet", currentBullet).build();
    }

    public void reload(ItemStack item, Player player) {
        if(bullet.haveBullet(player) == null) return;
        PlayerStatus playerStatus = PlayerStatus.getPlayerStatus(player);
        if(playerStatus.reloadingMagazine) return;
        else playerStatus.reloadingMagazine = true;
        playerStatus.applyFactorSpeed("ReloadMagazine", 0.80f, -1);
        new BukkitRunnable() {
            final ActionBarManager actionBarManager = Main.getInstance().getActionBarManager();
            int counter = 0;
            public void run() {
                counter++;
                ItemStack playerItem = player.getInventory().getItemInMainHand();
                if(!playerItem.equals(item) || bullet.haveBullet(player) == null || isFull(item)) {
                    actionBarManager.removeByReason(player, "ReloadMagazine");
                    playerStatus.reloadingMagazine = false;
                    playerStatus.cancelByReason("ReloadMagazine", true);
                    cancel();
                    return;
                }
                if(counter == timePerBullet) {
                    counter = 0;
                    bullet.removeBullet(player, 1);
                    player.getInventory().setItemInMainHand(new NBTUtils(item).set("remainBullet", NBTUtils.toNBT(playerItem).getInteger("remainBullet") + 1).build());
                }
                Message.MessageUpdater message = () ->
                        StringUtils.fastReplace(LanguageManager.getLanguage().WEAPON_RELOADING,
                                "{progress}->" + StringUtils.progressBar(counter, timePerBullet));
                actionBarManager.addPriority(player, new Message(20, "ReloadMagazine", message));
            }
        }.runTaskTimer(Main.getInstance(), 0, timePerBullet);
    }

    public boolean isFull(ItemStack magazine) {
        NBTItem item = NBTUtils.toNBT(magazine);
        if(item.hasNBTData()) {
            if(item.hasKey("remainBullet")) {
                return Main.getInstance().getWeaponManager().getMagazineManager().getMagazine(magazine).magazineSize == item.getInteger("remainBullet");
            }
        }
        return false;
    }

    public void removeMag(Player player, int amount) {
        for(ItemStack item : player.getInventory().getContents()) {
            if(item == null || item.getType() == Material.AIR) continue;
            NBTItem nbtItem = NBTUtils.toNBT(item);
            if(nbtItem.hasNBTData()) {
                if (nbtItem.hasKey("magazineSystemName")) {
                    if (nbtItem.getString("magazineSystemName").equals(this.systemName)) {
                        if (item.getAmount() <= amount) {
                            amount -= item.getAmount();
                            item.setType(Material.AIR);
                        }
                        else {
                            item.setAmount(item.getAmount() - amount);
                            return;
                        }
                    }
                }
            }
        }
    }

    public ItemStack haveMagazine(Player player) {
        for(ItemStack item : player.getInventory().getContents()) {
            if(item == null || item.getType() == Material.AIR) continue;
            NBTItem nbtItem = NBTUtils.toNBT(item);
            if(nbtItem.hasNBTData())
                if(nbtItem.hasKey("magazineType"))
                    if(nbtItem.getString("magazineType").equalsIgnoreCase(bullet.bulletType))
                        return item;

        }
        return null;
    }

    public Magazine(int magazineSize, String magazineDisplayName, String systemName, Bullet bullet, int modelData, boolean canBeReloaded, int timePerBullet) {
        this.magazineSize = magazineSize;
        this.magazineDisplayName = magazineDisplayName;
        this.systemName = systemName;
        this.bullet = bullet;
        this.modelData = modelData;
        this.canBeReloaded = canBeReloaded;
        this.timePerBullet = timePerBullet;
    }
}