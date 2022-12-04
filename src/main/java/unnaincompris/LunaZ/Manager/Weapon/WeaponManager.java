package unnaincompris.LunaZ.Manager.Weapon;

import de.tr7zw.nbtapi.NBTItem;
import lombok.Getter;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Manager.Weapon.Bullet.BulletManager;
import unnaincompris.LunaZ.Manager.Weapon.Commands.WeaponExecutor;
import unnaincompris.LunaZ.Manager.Weapon.Listeners.DynamicListener;
import unnaincompris.LunaZ.Manager.Weapon.Magazine.MagazineManager;
import unnaincompris.LunaZ.utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class WeaponManager implements ManagerEnabler {

    private final @Getter BulletManager bulletManager;
    private final @Getter MagazineManager magazineManager;

    public WeaponManager() {
        bulletManager = new BulletManager();
        magazineManager = new MagazineManager();

        new WeaponExecutor();
        new DynamicListener();
    }

    public List<Weapon> weaponList = new ArrayList<>();

    public boolean isAnWeapon(ItemStack item) {
        NBTItem nbtItem = NBTUtils.toNBT(item);
        return nbtItem.hasNBTData() && nbtItem.hasKey("weaponTag");
    }
    public Weapon getWeapon(ItemStack item) {
        NBTItem nbtItem = NBTUtils.toNBT(item);
        if(nbtItem.hasNBTData())
            if(nbtItem.hasKey("weaponSystemName"))
                return fromSysName(nbtItem.getString("weaponSystemName"));
        return null;
    }
    public Weapon fromSysName(String systemName) {
        for(Weapon weapon : weaponList)
            if(weapon.systemName.equalsIgnoreCase(systemName)) {
                return weapon;
            }
        Weapon weapon = JsonUtils.getData(JsonUtils.getOrCreateJson(Config.WEAPON_DATA, systemName), Weapon.class);
        weaponList.add(weapon);
        return weapon;
    }
    public boolean weaponExist(String weaponName) {
        for(File file : FileUtils.getAllFile(Config.WEAPON_DATA, ".json"))
            if(file.getName().replace(".json", "").equalsIgnoreCase(weaponName))
                return true;
        return false;
    }
    public void unload(String systemName) {
        Weapon toRemove = null;
        for(Weapon weapon : weaponList)
            if(weapon.systemName.equalsIgnoreCase(systemName)) {
                toRemove = weapon;
            }
        if(toRemove != null)
            weaponList.remove(toRemove);
    }
}
