package unnaincompris.LunaZ.Manager.Consumable.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Commands.Manager.SubCommand;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Consumable.Consumable;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.Manager.MultipleInventory.Items;
import unnaincompris.LunaZ.Manager.MultipleInventory.MultipleInventory;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.*;
import unnaincompris.LunaZ.utils.InteractiveAsk.BooleanAsk;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ListConsumable extends SubCommand {
    public ListConsumable() {
        super("list", Config.SERVER_NAME + ".Consumable.List", false);
    }

    //Config.SERVER_NAME + ".Consumable.ListEdit"

    private final HashMap<ViewersType, MultipleInventory> playerEditInventory = new HashMap<>();
    enum ViewersType {
        Admin, Player;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            StringBuilder toSend = new StringBuilder(Config.SERVER_NAME_TRANSLATE + " ");
            for(File file : FileUtils.getAllFile(Config.CONSUMABLE_DATA, ".json")) {
                toSend.append(", ").append(file.getName().replace(".json", ""));
            }
            sender.sendMessage(ColorUtils.translate(toSend.toString()));
        } else {
            Player player = (Player) sender;
            ViewersType viewersType = player.hasPermission(Config.SERVER_NAME + ".Consumable.ListEdit") ? ViewersType.Admin : ViewersType.Player;

            if(!playerEditInventory.containsKey(viewersType)) {
                Items content = new Items(() -> {
                    List<Items.ActionItem> items = new ArrayList<>();
                    for (File file : FileUtils.getAllFile(Config.CONSUMABLE_DATA, ".json")) {
                        ItemStack consumable = Main.getInstance().getConsumableManager().fromSysName(file.getName().replace(".json", "")).getItem();
                        if (player.hasPermission(Config.SERVER_NAME + ".Consumable.ListEdit")) {
                            ItemUtils.setLore(consumable, Arrays.asList("&aLeft click &7to edit this weapon", "&cRight click &7to delete this weapon", "&6Middle click &7to give"));
                        }
                        items.add(new Items.ActionItem() {
                            @Override
                            public void onClick(InventoryClickEvent event) {
                                if(event.isRightClick()) {
                                    Consumable consumable = Main.getInstance().getConsumableManager().getConsumable(event.getCurrentItem());
                                    if (event.isShiftClick()) {
                                        JsonUtils.deleteJson(Config.CONSUMABLE_DATA, consumable.systemName);
                                        playerEditInventory.get(viewersType).update(player);
                                    } else {
                                        new BooleanAsk(player, StringUtils.fastReplace(LanguageManager.getLanguage().CONFIRMATION_TO_DELETE, "{toDelete}->" + consumable.displayName),
                                                (respond) -> {
                                                    if (respond) {
                                                        JsonUtils.deleteJson(Config.CONSUMABLE_DATA, consumable.systemName);
                                                        player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().SUCCESSFULLY_DELETE_FILE, "{file_name}->" + consumable.systemName)));
                                                        playerEditInventory.get(viewersType).update(player);
                                                    }
                                                });
                                    }
                                } else if (event.getClick() == ClickType.MIDDLE) {
                                    Consumable consumable = Main.getInstance().getConsumableManager().getConsumable(event.getCurrentItem());
                                    player.getInventory().addItem(consumable.getItem());
                                } else if (event.isLeftClick()) {
                                    Consumable consumable = Main.getInstance().getConsumableManager().getConsumable(event.getCurrentItem());
                                    CreateConsumableData data = new CreateConsumableData(consumable);
                                    data.loadInv();
                                    data.refreshInv(player);
                                }
                            }
                            @Override
                            public ItemStack getItem() {
                                return consumable;
                            }
                        });
                    }
                    return items;
                });
                MultipleInventory newInv = new MultipleInventory(content, "&7Consumable List (&6%current_page%&7/%max_page%)");
                playerEditInventory.put(viewersType, newInv);
            }
            playerEditInventory.get(viewersType).display(player);
        }
    }
}
