package unnaincompris.LunaZ.Manager.Weapon.Magazine;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Manager.Weapon.Weapon;
import unnaincompris.LunaZ.utils.Config;
import unnaincompris.LunaZ.utils.JsonUtils;
import unnaincompris.LunaZ.utils.NBTUtils;

import java.util.ArrayList;
import java.util.List;

public class MagazineManager {

    public List<Magazine> magazineCache = new ArrayList<>();

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

    public Magazine getMagazine(ItemStack item) {
        NBTItem nbtItem = NBTUtils.toNBT(item);
        if(nbtItem.hasNBTData())
            if(nbtItem.hasKey("magazineSystemName"))
                return fromSysName(nbtItem.getString("magazineSystemName"));
        return null;
    }
    public Magazine fromSysName(String systemName) {
        for(Magazine magazine : magazineCache)
            if(magazine.systemName.equalsIgnoreCase(systemName)) {
                return magazine;
            }
        Magazine magazine = JsonUtils.getData(JsonUtils.getOrCreateJson(Config.MAGAZINE_DATA, systemName), Magazine.class);
        magazineCache.add(magazine);
        return magazine;
    }

    public ItemStack haveMagazine(Player player, Weapon weapon) {
        for(ItemStack item : player.getInventory().getContents()) {
            if(item == null || item.getType() == Material.AIR) continue;
            NBTItem nbtItem = NBTUtils.toNBT(item);
            if(nbtItem.hasNBTData())
                if(nbtItem.hasKey("magazineType"))
                    if(nbtItem.getString("magazineType").equalsIgnoreCase(weapon.magazine.bullet.bulletType))
                        return item;

        }
        return null;
    }
}
