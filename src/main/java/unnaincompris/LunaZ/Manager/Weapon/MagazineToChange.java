package unnaincompris.LunaZ.Manager.Weapon;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.utils.ItemUtils;
import unnaincompris.LunaZ.utils.NBTUtils;

public class MagazineToChange {

    private final Weapon weapon;

    public MagazineToChange(Weapon weapon) {
        this.weapon = weapon;
    }

    public ItemStack getItem() {
        return new NBTUtils(ItemUtils.setCustomModelTexture(
                ItemUtils.createItem(Material.OAK_PLANKS, weapon.displayName + " &7Magazine [" + weapon.magazineSize + "]", 1, null, null), weapon.modelData))
                .set("MagazineOwner", weapon.systemName).build();
    }
}
