package unnaincompris.LunaZ.Manager.Weapon.Commands;

import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Commands.Manager.SubCommand;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.Manager.MultipleInventory.Items;
import unnaincompris.LunaZ.Manager.MultipleInventory.MultipleInventory;
import unnaincompris.LunaZ.Manager.Weapon.Weapon;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.*;
import unnaincompris.LunaZ.utils.InteractiveAsk.BooleanAsk;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class ListWeapon extends SubCommand {
    public ListWeapon() {
        super("list", Config.SERVER_NAME + ".Weapon.List", false);
    }

    //Config.SERVER_NAME + ".Weapon.ListEdit"

    private final HashMap<ViewersType, MultipleInventory> playerEditInventory = new HashMap<>();
    enum ViewersType {
        Admin, Player;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            StringBuilder toSend = new StringBuilder(Config.SERVER_NAME_TRANSLATE + " ");
            for(File file : FileUtils.getAllFile(Config.WEAPON_DATA, ".json")) {
                toSend.append(", ").append(file.getName().replace(".json", ""));
            }
            sender.sendMessage(ColorUtils.translate(toSend.toString()));
        } else {
            Player player = (Player) sender;
            ViewersType viewersType = player.hasPermission(Config.SERVER_NAME + ".Weapon.ListEdit") ? ViewersType.Admin : ViewersType.Player;

            if(!playerEditInventory.containsKey(viewersType)) {
                Items content = new Items(() -> {
                    List<Items.ActionItem> items = new ArrayList<>();
                    for (File file : FileUtils.getAllFile(Config.WEAPON_DATA, ".json")) {
                        ItemStack weapon = Main.getInstance().getWeaponManager().fromSysName(file.getName().replace(".json", "")).getItem();
                        if (player.hasPermission(Config.SERVER_NAME + ".Weapon.ListEdit")) {
                            ItemUtils.setLore(weapon, Arrays.asList("&aLeft click &7to edit this weapon", "&cRight click &7to delete this weapon", "&6Middle click &7to give"));
                        }
                        items.add(new Items.ActionItem() {
                            @Override
                            public void onClick(InventoryClickEvent event) {
                                if(event.isRightClick()) {
                                    Weapon weapon = Main.getInstance().getWeaponManager().getWeapon(event.getCurrentItem());
                                    if (event.isShiftClick()) {
                                        JsonUtils.deleteJson(Config.WEAPON_DATA, weapon.systemName);
                                        playerEditInventory.get(viewersType).update(player);
                                    } else {
                                        new BooleanAsk(player, StringUtils.fastReplace(LanguageManager.getLanguage().CONFIRMATION_TO_DELETE, "{toDelete}->" + weapon.displayName),
                                                (respond) -> {
                                                    if (respond) {
                                                        JsonUtils.deleteJson(Config.WEAPON_DATA, weapon.systemName);
                                                        player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().SUCCESSFULLY_DELETE_FILE, "{file_name}->" + weapon.systemName)));
                                                        playerEditInventory.get(viewersType).update(player);
                                                    }
                                                });
                                    }
                                } else if (event.getClick() == ClickType.MIDDLE) {
                                    Weapon weapon = Main.getInstance().getWeaponManager().getWeapon(event.getCurrentItem());
                                    player.getInventory().addItem(weapon.getItem());
                                    player.getInventory().addItem(weapon.magazineToChange.getItem());
                                } else if (event.isLeftClick()) {
                                    Weapon weapon = Main.getInstance().getWeaponManager().getWeapon(event.getCurrentItem());
                                    CreateData data = new CreateData(weapon);
                                    data.loadInv();
                                    data.refreshInv(player);
                                }
                            }
                            @Override
                            public ItemStack getItem() {
                                return weapon;
                            }
                        });
                    }
                    return items;
                });
                MultipleInventory newInv = new MultipleInventory(content, "&7Weapon List (&6%current_page%&7/%max_page%)");
                playerEditInventory.put(viewersType, newInv);
            }
            playerEditInventory.get(viewersType).display(player);
        }
    }
}
