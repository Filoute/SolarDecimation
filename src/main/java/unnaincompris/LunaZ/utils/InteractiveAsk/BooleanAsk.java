package unnaincompris.LunaZ.utils.InteractiveAsk;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.utils.InventoryUtils;
import unnaincompris.LunaZ.utils.ItemUtils;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.Consumer;

public class BooleanAsk implements Listener {
    private Inventory inventory;
    private final Player player;
    private final UUID playerUUID;

    private final Consumer<Boolean> action;

    private final ItemStack acceptItem = ItemUtils.createItem(new ItemStack(Material.GREEN_WOOL, 1), LanguageManager.getLanguage().ACCEPT, null, null);
    private final ItemStack deniedItem = ItemUtils.createItem(new ItemStack(Material.RED_WOOL, 1), LanguageManager.getLanguage().DENIED, null, null);
    private ItemStack infoItem;

    private boolean finished = false;

    public BooleanAsk(Player player,  Consumer<Boolean> action) {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        this.player = player;
        this.playerUUID = player.getUniqueId();
        this.action = action;
        initInventory();
        player.openInventory(inventory);
    }

    public BooleanAsk(Player player, String info, Consumer<Boolean> action) {
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
        this.player = player;
        this.playerUUID = player.getUniqueId();
        this.action = action;
        this.infoItem = ItemUtils.infoItem(info);
        initInventory();
        player.openInventory(inventory);
    }

    private void initInventory() {
        inventory = Bukkit.createInventory(null, 27, "§6Boolean Ask");
        inventory = InventoryUtils.aroundInventory(inventory, ItemUtils.placeHolders(Material.GRAY_STAINED_GLASS_PANE, false));
        inventory.setItem(12, acceptItem);
        inventory.setItem(14, deniedItem);
        if(infoItem != null)
            inventory.setItem(13, infoItem);
    }

    public void end(boolean value) {
        if(finished) return;
        this.finished = true;
        player.closeInventory();
        action.accept(value);
        HandlerList.unregisterAll(this);
        try { this.finalize(); } catch (Throwable ignored){}
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent event) {
        if(event.getPlayer().getUniqueId() != playerUUID) return;
        end(false);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(event.getCurrentItem() == null) return;
        if(event.getWhoClicked().getUniqueId() != playerUUID) return;
        if(!Arrays.equals(event.getView().getTopInventory().getContents(), inventory.getContents())) return;

        event.setCancelled(true);
        if(event.getCurrentItem().equals(acceptItem)) end(true);
        else if(event.getCurrentItem().equals(deniedItem)) end(false);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent event) {
        if(event.getPlayer().getUniqueId() != playerUUID) return;
        if(!Arrays.equals(event.getView().getTopInventory().getContents(), inventory.getContents())) return;
        end(false);
    }
}
