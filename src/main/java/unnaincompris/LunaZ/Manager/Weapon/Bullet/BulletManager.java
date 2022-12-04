package unnaincompris.LunaZ.Manager.Weapon.Bullet;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Manager.Weapon.Magazine.Magazine;
import unnaincompris.LunaZ.Manager.Weapon.Weapon;
import unnaincompris.LunaZ.utils.Config;
import unnaincompris.LunaZ.utils.JsonUtils;
import unnaincompris.LunaZ.utils.NBTUtils;

public class BulletManager {
    public static Magazine getMagazine(String systemName) {
        return JsonUtils.getData(JsonUtils.getOrCreateJson(Config.MAGAZINE_DATA, systemName), Magazine.class);
    }

    public static void saveMagazine(Magazine magazine) {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(Config.MAGAZINE_DATA, magazine.systemName), magazine);
    }

    public boolean isAnMagazine(ItemStack item) {
        if(item == null || item.getType() == Material.AIR) return false;
        NBTItem nbtItem = NBTUtils.toNBT(item);
        if(nbtItem.hasNBTData())
            return nbtItem.hasKey("MagazineType");
        return false;
    }

    public ItemStack haveMagazine(Player player, Weapon weapon) {
        for(ItemStack item : player.getInventory().getContents()) {
            if(item == null || item.getType() == Material.AIR) continue;
            NBTItem nbtItem = NBTUtils.toNBT(item);
            if(nbtItem.hasNBTData())
                if(nbtItem.hasKey("MagazineType"))
                    for(Magazine magazine : weapon.compatibleMagazine)
                        if(nbtItem.getString("MagazineType").equalsIgnoreCase(magazine.bullet.bulletType)) {
                            return item;
                        }
        }
        return null;
    }
}
