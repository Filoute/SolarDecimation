package unnaincompris.LunaZ.Manager.MultipleInventory;

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
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.InventoryUtils;
import unnaincompris.LunaZ.utils.ItemUtils;
import unnaincompris.LunaZ.utils.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MultipleInventory implements Listener {

    private final Items content;
    private final HashMap<Integer, Items> barItems = new HashMap<>();
    private final ItemStack nextPageItem = ItemUtils.createItem(Material.GREEN_STAINED_GLASS_PANE, LanguageManager.getLanguage().NEXT_PAGE, 1, null, null);
    private final ItemStack previousPageItem = ItemUtils.createItem(Material.RED_STAINED_GLASS_PANE, LanguageManager.getLanguage().PREVIOUS_PAGE, 1, null, null);
    private final ItemStack placeHoldersItem = ItemUtils.placeHolders(Material.WHITE_STAINED_GLASS_PANE, false);

    private final int inventorySize;
    @Getter private final String inventoryName;

    private final List<Inventory> inventory = new ArrayList<>();
    private final List<HumanEntity> viewers = new ArrayList<>();

    @Getter private Inventory currentInventory;

    public MultipleInventory(Items content, String inventoryName) {
        this(content, inventoryName, -1);
    }

    public MultipleInventory(Items content, String inventoryName, int inventorySize) {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        this.content = content;
        this.inventoryName = inventoryName;
        this.inventorySize = setSize(inventorySize);
        createInventory();
    }

    private int getAmountOfInv(List<ItemStack> items) {
        if(this.inventorySize < 0) {
            int amount = 0;
            int index = items.size();
            while(true) {
                amount++;
                if(index < 45)
                    break;
                index -= 45;
            }
            return amount;
        } else {
            return (int)Math.ceil((items.size()) / (this.inventorySize - 10.0));
        }
    }

    private void createInventory() {
        this.inventory.clear();
        List<ItemStack> items = this.content.buildContent();
        if(items.isEmpty()) items.add(ItemUtils.AIR);
        int maxInv = getAmountOfInv(items);
        //int maxInv = (int)Math.ceil((items.size()) / (this.inventorySize - 10.0));
        //if(maxInv < 1) maxInv = (int)Math.ceil((double) ((items.size()) / setSize(45)));

        for(int i = 0 ; i < maxInv ; i++) {
            final Inventory newInv;
            if(this.inventorySize < 0) {
                newInv = Bukkit.getServer().createInventory(null, setSize(items.size()), ColorUtils.translate(
                        StringUtils.fastReplace(this.inventoryName, "%current_page%->" + (i + 1), "%max_page%->" + maxInv)));
            } else {
                newInv = Bukkit.getServer().createInventory(null, this.inventorySize, ColorUtils.translate(
                        StringUtils.fastReplace(this.inventoryName, "%current_page%->" + (i + 1), "%max_page%->" + maxInv)));
            }
            InventoryUtils.setSideInventory(newInv, new ItemStack[]{placeHoldersItem}, new InventoryUtils.Side[]{InventoryUtils.Side.DOWN});
            if(i != maxInv-1) newInv.setItem(newInv.getSize() - 3, this.nextPageItem);
            if(i != 0) newInv.setItem(newInv.getSize() - 7, this.previousPageItem);


            for(int y = 0 ; y < setSize(newInv.getSize()-9) - 9 ; y++){
                if(items.isEmpty()) break;
                newInv.setItem(y, items.get(0));
                items.remove(0);
            }
            this.inventory.add(newInv);
        }
        this.currentInventory = this.inventory.get(0);
    }

    private int setSize(int inventorySize) {
        if(inventorySize < 0) return -1;
        else if(inventorySize <= 9) return 9 + 9;
        else if(inventorySize <= 18) return 18 + 9;
        else if(inventorySize <= 27) return 27 + 9;
        else if(inventorySize <= 36) return 36 + 9;
        else return 45 + 9;
    }
    public void kill() {
        viewers.forEach(HumanEntity::closeInventory);
        HandlerList.unregisterAll(this);
        try { this.finalize(); } catch (Throwable ignored){}
    }

    public MultipleInventory addBarItem(int slot, Items item) {
        if(slot == 3 || slot == 7) return this;
        for(Inventory inv : this.inventory){
            inv.setItem(((setSize(inv.getSize()-9) - 1) - 9) + slot, item.buildFirstContent());
        }
        if(!this.barItems.containsKey(slot) && !this.barItems.containsValue(item))
            this.barItems.put(slot, item);
        return this;
    }

    public void display(HumanEntity... newViewers) {
        Arrays.asList(newViewers).forEach(newViewer -> {
            newViewer.openInventory(this.currentInventory);
                if(!this.viewers.contains(newViewer))
                    this.viewers.add(newViewer);
        });

    }
    public void update(HumanEntity... newViewers) {
        int currentInventoryIndex = inventory.indexOf(currentInventory);
        createInventory();
        for(int key : this.barItems.keySet())
            addBarItem(key, this.barItems.get(key));
        if(newViewers != null)
            Arrays.asList(newViewers).forEach(humanEntity -> {if(!this.viewers.contains(humanEntity)) this.viewers.add(humanEntity);});

        if(this.inventory.get(currentInventoryIndex) != null)
            this.currentInventory = this.inventory.get(currentInventoryIndex);
        else
            this.currentInventory = this.inventory.get(this.inventory.size()-1);

        this.viewers.forEach(this::display);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        for(Inventory inv : this.inventory)
            if(event.getInventory().equals(inv)) {
                event.setCancelled(true);
                break;
            }
        if(!event.isCancelled() || event.getCurrentItem() == null) return;

        if(event.getCurrentItem().equals(nextPageItem) && this.inventory.indexOf(this.currentInventory) != this.inventory.size()-1) {
            this.currentInventory = this.inventory.get(this.inventory.indexOf(this.currentInventory)+1);
            display(event.getWhoClicked());
        }
        if(event.getCurrentItem().equals(previousPageItem) && this.inventory.indexOf(this.currentInventory) != 0) {
            this.currentInventory = this.inventory.get(this.inventory.indexOf(this.currentInventory)-1);
            display(event.getWhoClicked());
        }
        Items.ActionItem actionItem = getActionItem(event.getCurrentItem());
        if(actionItem != null) {
            actionItem.onClick(event);
        }

    }

    private Items.ActionItem getActionItem(ItemStack item) {
        for(Items.ActionItem actionItem : this.content.build()) {
            if(actionItem.getItem().equals(item)) {
                return actionItem;
            }
        }
        return null;
    }

    @EventHandler
    public void onCloseInventory(InventoryCloseEvent event) {
        if(event.getInventory().equals(this.currentInventory))
            this.viewers.remove(event.getPlayer());
    }
    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if(event.getInventory().equals(this.currentInventory))
            if(!this.viewers.contains(event.getPlayer())) this.viewers.add(event.getPlayer());
    }

    public boolean isSysItem(ItemStack item) {
        if(item == null) return false;
        return item.equals(placeHoldersItem) || item.equals(nextPageItem) || item.equals(previousPageItem);
    }
}
