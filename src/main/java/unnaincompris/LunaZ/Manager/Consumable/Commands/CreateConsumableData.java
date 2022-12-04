package unnaincompris.LunaZ.Manager.Consumable.Commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Consumable.Consumable;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.Manager.MultipleInventory.Items;
import unnaincompris.LunaZ.Manager.SimpleInventory.SimpleInventory;
import unnaincompris.LunaZ.utils.BukkitTasks;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.InteractiveAsk.IntegerAsk;
import unnaincompris.LunaZ.utils.InteractiveAsk.StringAsk;
import unnaincompris.LunaZ.utils.InventoryUtils;
import unnaincompris.LunaZ.utils.ItemUtils;
import unnaincompris.LunaZ.utils.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class CreateConsumableData { // Info Need to make Effect

    public int heartToHeal;
    public int timeToGetHeal;
    public int timeWhileGettingHeal;
    public int foodToGain;
    public int timeToGetFood;
    public int timeWhileGettingFood;

    public int modelData;
    public String displayName;

    public SimpleInventory inventory;
    public String sysName;

    public void loadInv(String sysName) {
        this.sysName = sysName;
        loadInv();
    }

    private final Items heartToHealItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    heartToHeal = (int) integer.doubleValue();
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.RED_TERRACOTTA, "&cHearth to Heal", 1, Collections.singletonList("&7Current: " + heartToHeal), null);
        }
    }));
    private final Items timeToGetHealItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    timeToGetHeal = (int) integer.doubleValue();
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.RED_TERRACOTTA, "&cTime to get heal", 1, Collections.singletonList("&7Current: " + timeToGetHeal + " &c[can be 0]"), null);
        }
    }));
    private final Items timeWhileGettingHealItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    timeWhileGettingHeal = (int) integer.doubleValue();
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.RED_TERRACOTTA, "&cTime While Getting Heal", 1, Collections.singletonList("&7Current: " + timeWhileGettingHeal + " &c[can be 0]"), null);
        }
    }));

    private final Items foodToGainItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    foodToGain = (int) integer.doubleValue();
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.COOKED_BEEF, "&6Food to gain", 1, Collections.singletonList("&7Current: " + foodToGain), null);
        }
    }));
    private final Items timeToGetFoodItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    timeToGetFood = (int) integer.doubleValue();
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.COOKED_BEEF, "&cTime to get Food", 1, Collections.singletonList("&7Current: " + timeToGetFood + " &c[can be 0]"), null);
        }
    }));
    private final Items timeWhileGettingFoodItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    timeWhileGettingFood = (int) integer.doubleValue();
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.COOKED_BEEF, "&6Time while getting Food", 1, Collections.singletonList("&7Current: " + timeWhileGettingFood + " &c[can be 0]"), null);
        }
    }));

    private final Items modelDataItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    modelData = (int) integer.doubleValue();
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.BOOK, "&6Model data", 1, Collections.singletonList("&7Current: " + modelData), null);
        }
    }));
    private final Items displayNameItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new StringAsk(player, (name) -> {
                if(!name.equals(LanguageManager.getLanguage().CANCEL_KEYWORD))
                    displayName = name;
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.PAPER, "&5Display Name", 1, Collections.singletonList("&7Current: " + displayName), null);
        }
    }));

    private final Items createConsumable = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            event.getWhoClicked().closeInventory();
            new Consumable(heartToHeal, timeToGetHeal, timeWhileGettingHeal, foodToGain, timeToGetFood, timeWhileGettingFood,
                    modelData, displayName, sysName, new ArrayList<>());
            event.getWhoClicked().sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().CONSUMABLE_CREATED, "{consumable}->" + displayName + " [" + sysName + "]")));
            Main.getInstance().getConsumableManager().unload(sysName);
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.GREEN_WOOL, "&2Create", 1, null, null);
        }
    }));

    public CreateConsumableData(Consumable consumable) {
        sysName = consumable.systemName;
        heartToHeal = consumable.heartToHeal;
        timeToGetHeal = consumable.timeToGetHeal;
        timeWhileGettingHeal = consumable.timeWhileGettingHeal;
        foodToGain = consumable.foodToGain;
        timeToGetFood = consumable.timeToGetFood;
        timeWhileGettingFood = consumable.timeWhileGettingFood;
        displayName = consumable.displayName;
        modelData = consumable.modelData;
    }

    public CreateConsumableData(){}

    public void loadInv() {
        this.inventory = new SimpleInventory(new HashMap<Integer, Items>() {{
            put(11, heartToHealItems);
            put(15, foodToGainItems);
            put(19, timeToGetHealItems);
            put(21, modelDataItems);
            put(23, displayNameItems);
            put(25, timeToGetFoodItems);
            put(29, timeWhileGettingHealItems);
            put(33, timeWhileGettingFoodItems);
            put(40, createConsumable);
        }}, 5*9, "&aCreating &7new " + sysName, InventoryUtils.allSide());
    }

    public void refreshInv(Player newViewers) {
        BukkitTasks.sync(()->this.inventory.update(newViewers));
    }

}
