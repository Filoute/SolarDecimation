package unnaincompris.LunaZ.Manager.Consumable;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Manager.Consumable.Commands.ConsumableExecutor;
import unnaincompris.LunaZ.Manager.Consumable.Listeners.OnConsume;
import unnaincompris.LunaZ.utils.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConsumableManager implements ManagerEnabler {
    public ConsumableManager() {
        new ConsumableExecutor();
        new OnConsume();
    }

    public List<Consumable> consumableList = new ArrayList<>();

    public boolean isAnConsumable(ItemStack item) {
        NBTItem nbtItem = NBTUtils.toNBT(item);
        return nbtItem.hasNBTData() && nbtItem.hasKey("consumableTag");
    }
    public Consumable getConsumable(ItemStack item) {
        NBTItem nbtItem = NBTUtils.toNBT(item);
        if(nbtItem.hasNBTData())
            if(nbtItem.hasKey("consumableSystemName"))
                return fromSysName(nbtItem.getString("consumableSystemName"));
        return null;
    }
    public Consumable fromSysName(String systemName) {
        for(Consumable consumable : consumableList)
            if(consumable.systemName.equalsIgnoreCase(systemName)) {
                return consumable;
            }
        Consumable consumable = JsonUtils.getData(JsonUtils.getOrCreateJson(Config.CONSUMABLE_DATA, systemName), Consumable.class);
        consumableList.add(consumable);
        return consumable;
    }
    public boolean consumableExist(String consumableName) {
        for(File file : FileUtils.getAllFile(Config.CONSUMABLE_DATA, ".json"))
            if(file.getName().replace(".json", "").equalsIgnoreCase(consumableName))
                return true;
        return false;
    }
    public void unload(String systemName) {
        Consumable toRemove = null;
        for(Consumable consumable : consumableList)
            if(consumable.systemName.equalsIgnoreCase(systemName)) {
                toRemove = consumable;
            }
        if(toRemove != null)
            consumableList.remove(toRemove);
    }

    public boolean isTheSame(Consumable consumable, ItemStack item) {
        return consumable.systemName.equals(getConsumable(item).systemName);
    }
    public boolean isTheSame(ItemStack consumable, ItemStack item) {
        return getConsumable(consumable).systemName.equals(getConsumable(item).systemName);
    }
    public boolean isTheSame(Consumable consumable, Consumable item) {
        return consumable.systemName.equals(item.systemName);
    }
}
