package unnaincompris.LunaZ.Manager.MultipleInventory;

import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class Items {

    private final ItemUpdater items;

    public Items(ItemUpdater items) {
        this.items = items;
    }

    public List<ActionItem> build() {
        return items.getItems();
    }
    public List<ItemStack> buildContent() {
        List<ItemStack> items = new ArrayList<>();
        for(ActionItem item : build()) {
            items.add(item.getItem());
        }
        return items;
    }

    public ActionItem buildFirst() {
        return items.getItems().get(0);
    }

    public ItemStack buildFirstContent() {
        return items.getItems().get(0).getItem();
    }

    public interface ItemUpdater {
        List<ActionItem> getItems();
    }

    public interface ActionItem {
        void onClick(InventoryClickEvent event);
        ItemStack getItem();
    }
}

