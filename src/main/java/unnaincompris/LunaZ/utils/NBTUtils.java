package unnaincompris.LunaZ.utils;

import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

import java.util.UUID;

public class NBTUtils {
    private final NBTItem item;

    public NBTUtils(ItemStack item) {
        this.item = new NBTItem(item);
    }

    public ItemStack build() {
        return this.item.getItem();
    }

    public NBTUtils set(String key, Object value) {
        if(value == null) return this;
        if(value instanceof String)         item.setString(key,     value.toString());
        else if(value instanceof Boolean)   item.setBoolean(key,    (Boolean) value);
        else if(value instanceof Byte)      item.setByte(key,       (Byte) value);
        else if(value instanceof Double)    item.setDouble(key,     (Double) value);
        else if(value instanceof Float)     item.setFloat(key,      (Float) value);
        else if(value instanceof byte[])    item.setByteArray(key,  (byte[]) value);
        else if(value instanceof int[])     item.setIntArray(key,   (int[]) value);
        else if(value instanceof Integer)   item.setInteger(key,    (Integer) value);
        else if(value instanceof ItemStack) item.setItemStack(key,  (ItemStack) value);
        else if(value instanceof Long)      item.setLong(key,       (Long) value);
        else if(value instanceof Short)     item.setShort(key,      (Short) value);
        else if(value instanceof UUID)      item.setUUID(key,       (UUID) value);
        return this;
    }

    public static NBTItem toNBT(ItemStack item) {
        return new NBTItem(item);
    }
}
