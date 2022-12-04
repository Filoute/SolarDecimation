package unnaincompris.LunaZ.Manager.PlayerData.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import unnaincompris.LunaZ.Commands.Manager.SubCommand;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.PlayerData.PlayerData;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class ShowPlayerData extends SubCommand implements Listener {
    public ShowPlayerData() {
        super("show", Collections.singletonList("Info"), Config.SERVER_NAME + ".PlayerData.Show", true);
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        // if(args == null || args.length == 0 || args[0] == null) return;
        UUID targetUUID = ((Player) sender).getUniqueId();
        if(args != null)
            if(args.length != 0 && args[0] != null)
                targetUUID = UUIDUtils.getUUID(args[0]);
        if(targetUUID == null || !Main.getInstance().getPlayerDataManager().exist(targetUUID)){
            targetUUID = ((Player) sender).getUniqueId();
        }
        final Inventory inv = Bukkit.createInventory(null, 27, ColorUtils.translate(Config.SERVER_PREFIX_INGAME + " &7Stats of &6" + UUIDUtils.getName(targetUUID)));
        InventoryUtils.aroundInventory(inv, ItemUtils.placeHolders(Material.GRAY_STAINED_GLASS_PANE, false), ItemUtils.placeHolders(Material.ORANGE_STAINED_GLASS_PANE, false));
        inv.setItem(13, item(targetUUID));
        ((Player) sender).openInventory(inv);
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if(ColorUtils.strip(event.getWhoClicked().getOpenInventory().getTitle()).contains(Config.SERVER_PREFIX_INGAME + " Stats of")) {
            event.setCancelled(true);
        }
    }

    public ItemStack item(UUID target) {
        ItemStack stats = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta itemMeta = (SkullMeta) stats.getItemMeta();
        itemMeta.setOwningPlayer(Bukkit.getOfflinePlayer(target));

        List<String> lore = new ArrayList<>();
        PlayerData targetData = Main.getInstance().getPlayerDataManager().getDataFromUUID(target);

        for (Field field : targetData.getClass().getDeclaredFields()) {
            try {
                for (Annotation annotation : field.getDeclaredAnnotations()) {
                    if (annotation instanceof unnaincompris.LunaZ.Manager.PlayerData.Annotation.ShowPlayerData) {
                        field.setAccessible(true);
                        String currentStats = field.getName().substring(0, 1).toUpperCase() + field.getName().substring(1);
                        lore.add("&c" + currentStats + ": &7" + field.get(targetData));
                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        itemMeta.setDisplayName(ColorUtils.translate("&7Stats:"));
        itemMeta.setLore(ColorUtils.translate(lore));
        stats.setItemMeta(itemMeta);
        return stats;
    }
}
