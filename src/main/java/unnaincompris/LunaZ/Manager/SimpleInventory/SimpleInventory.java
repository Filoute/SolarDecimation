package unnaincompris.LunaZ.Manager.SimpleInventory;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.MultipleInventory.Items;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.InventoryUtils;
import unnaincompris.LunaZ.utils.ItemUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SimpleInventory implements Listener {

    private final List<HumanEntity> viewers = new ArrayList<>();
    private final HashMap<Integer, Items> content;
    @Getter private Inventory inventory;
    private final ItemStack placeHoldersItem = ItemUtils.placeHolders(Material.GRAY_STAINED_GLASS_PANE, false);

    private final int inventorySize;
    private final String inventoryName;
    private final InventoryUtils.Side[] side;

    public SimpleInventory(HashMap<Integer, Items> content, int inventorySize, String inventoryName) {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        this.inventoryName = inventoryName;
        this.inventorySize = setSize(inventorySize);
        this.content = content;
        this.side = new InventoryUtils.Side[]{};
        createInventory();
    }

    public SimpleInventory(HashMap<Integer, Items> content, int inventorySize, String inventoryName, InventoryUtils.Side[] side) {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        this.inventoryName = inventoryName;
        this.inventorySize = setSize(inventorySize);
        this.content = content;
        this.side = side;
        createInventory();
    }

    private void createInventory() {
        this.inventory = Bukkit.getServer().createInventory(null, this.inventorySize, ColorUtils.translate(this.inventoryName));
        InventoryUtils.setSideInventory(this.inventory, new ItemStack[]{this.placeHoldersItem}, this.side);
        for(int key : content.keySet()) {
            if(key <= this.inventorySize)
                this.inventory.setItem(key, content.get(key).buildFirstContent());
        }
    }

    private int setSize(int inventorySize) {
        if(inventorySize < 0) return -1;
        else if(inventorySize <= 9) return 9;
        else if(inventorySize <= 18) return 18;
        else if(inventorySize <= 27) return 27;
        else if(inventorySize <= 36) return 36;
        else return 45;
    }
    public void display(HumanEntity... newViewers) {
        Arrays.asList(newViewers).forEach(newViewer -> {
            newViewer.openInventory(this.inventory);
            if(!this.viewers.contains(newViewer))
                this.viewers.add(newViewer);
        });
    }
    public void update(HumanEntity... newViewers) {
        createInventory();
        if(newViewers != null)
            Arrays.asList(newViewers).forEach(humanEntity -> {if(!this.viewers.contains(humanEntity)) this.viewers.add(humanEntity);});
        this.viewers.forEach(this::display);
    }
    public void kill() {
        viewers.forEach(HumanEntity::closeInventory);
        HandlerList.unregisterAll(this);
        try { this.finalize(); } catch (Throwable ignored){}
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getInventory().equals(this.inventory))
            event.setCancelled(true);
        if(!event.isCancelled() || event.getCurrentItem() == null) return;
        Items.ActionItem actionItem = getActionItem(event.getCurrentItem());
        if(actionItem != null) {
            actionItem.onClick(event);
        }

    }
    public boolean isSysItem(ItemStack item) {
        if(item == null) return false;
        return item.equals(placeHoldersItem);
    }
    private Items.ActionItem getActionItem(ItemStack item) {
        List<Items> content = new ArrayList<>(this.content.values());
        for(Items inventoryItem : content) {
            for (Items.ActionItem actionItem : inventoryItem.build()) {
                if (actionItem.getItem().equals(item)) {
                    return actionItem;
                }
            }
        }
        return null;
    }
    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        if(event.getInventory().equals(this.inventory))
            this.viewers.remove(event.getPlayer());
    }
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if(event.getInventory().equals(this.inventory))
            if(!this.viewers.contains(event.getPlayer())) this.viewers.add(event.getPlayer());
    }
}
