package unnaincompris.LunaZ.utils;

import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryUtils {

    public static Inventory fillInventory(Inventory inventory, ItemStack... itemStack) {
        int currentItem = 0;
        for(int i = 0; i < inventory.getSize() ; i++) {
            if(inventory.getContents()[0] == null || inventory.getContents()[0].getType() == Material.AIR) {
                inventory.setItem(i, itemStack[currentItem]);
                currentItem++;
                if(currentItem > itemStack.length) currentItem = 0;
            }
        }
        return inventory;
    }

    public static Inventory aroundInventory(Inventory inventory, ItemStack... items) {
        setSideInventory(inventory, items, new Side[]{Side.UP, Side.RIGHT, Side.DOWN, Side.LEFT});
        return inventory;
    }

    public static Side[] allSide() {
        return new Side[]{Side.UP, Side.RIGHT, Side.DOWN, Side.LEFT};
    }

    public enum Side{
        RIGHT, LEFT, UP, DOWN;
    }

    public static Inventory setSideInventory(Inventory inventory, ItemStack[] items, Side[] sides){
        for(Side side : sides){
            int currentItem = 0;
            int[] pos = {};
            if(side == Side.RIGHT) {
                pos = new int[]{8, 17, 26, 35, 44, 53};
            } else if(side == Side.LEFT) {
                pos = new int[]{0, 9, 18, 27, 36, 45};
            } else if(side == Side.UP) {
                pos = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
            } else if(side == Side.DOWN) {
                final int size = inventory.getSize();
                pos = new int[]{size - 9, size - 8, size - 7, size - 6, size - 5, size - 4, size - 3, size - 2, size - 1};
            }
            for(int i = 0 ; i < pos.length ; i++) {
                if(pos[i] > inventory.getSize() - 1) continue;
                inventory.setItem(pos[i], items[currentItem]);
                currentItem++;
                if(currentItem > items.length-1) currentItem = 0;
            }
        }
        return inventory;
    }

}
