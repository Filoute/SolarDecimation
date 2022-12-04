package unnaincompris.LunaZ.Manager.Weapon.Bullet;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Manager.Weapon.Weapon;
import unnaincompris.LunaZ.utils.ItemUtils;
import unnaincompris.LunaZ.utils.NBTUtils;

public class Bullet {
    public final int bulletSpeed;
    public final int bulletWeight;
    public final int modelData;
    public final String systemName;
    public final String bulletDisplayName;
    public final String bulletType;

    public void shoot(Player shooter, Weapon weapon) {

    }

    public ItemStack getItem() {
        return new NBTUtils(ItemUtils.setCustomModelTexture(
                ItemUtils.createItem(Material.OAK_BUTTON, bulletDisplayName + " &Bullet [" + bulletType + "]", 1, null, null), modelData))
                .set("bulletSystemName", systemName).set("bulletType", bulletType).build();
    }

    public ItemStack haveBullet(Player player) {
        for(ItemStack item : player.getInventory().getContents()) {
            if(item == null || item.getType() == Material.AIR) continue;
            NBTItem nbtItem = NBTUtils.toNBT(item);
            if(nbtItem.hasNBTData())
                if(nbtItem.hasKey("bulletSystemName"))
                    if(nbtItem.getString("bulletSystemName").equalsIgnoreCase(this.systemName))
                        return item;

        }
        return null;
    }

    public void removeBullet(Player player, int amount) {
        for (ItemStack item : player.getInventory().getContents()) {
            if (item == null || item.getType() == Material.AIR) continue;
            NBTItem nbtItem = NBTUtils.toNBT(item);
            if (nbtItem.hasNBTData()) {
                if (nbtItem.hasKey("bulletSystemName")) {
                    if (nbtItem.getString("bulletSystemName").equals(this.systemName)) {
                        if (item.getAmount() <= amount) {
                            amount -= item.getAmount();
                            item.setType(Material.AIR);
                        } else {
                            item.setAmount(item.getAmount() - amount);
                            return;
                        }
                    }
                }
            }
        }
    }

    public Bullet(String bulletType, String bulletDisplayName, String systemName, int modelData, int bulletSpeed, int bulletWeight) {
        this.bulletDisplayName = bulletDisplayName;
        this.systemName = systemName;
        this.modelData = modelData;
        this.bulletSpeed = bulletSpeed;
        this.bulletWeight = bulletWeight;
        this.bulletType = bulletType;
    }
}
