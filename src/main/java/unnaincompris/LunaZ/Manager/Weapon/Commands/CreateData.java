package unnaincompris.LunaZ.Manager.Weapon.Commands;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.Manager.MultipleInventory.Items;
import unnaincompris.LunaZ.Manager.SimpleInventory.SimpleInventory;
import unnaincompris.LunaZ.Manager.Weapon.Weapon;
import unnaincompris.LunaZ.utils.BukkitTasks;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.InteractiveAsk.IntegerAsk;
import unnaincompris.LunaZ.utils.InteractiveAsk.StringAsk;
import unnaincompris.LunaZ.utils.InventoryUtils;
import unnaincompris.LunaZ.utils.ItemUtils;
import unnaincompris.LunaZ.utils.StringUtils;

import java.util.Collections;
import java.util.HashMap;

public class CreateData {
    public double damage;
    public double dispersion;
    public long shootingSpeed;
    public int magazineSize;
    public int bulletSpeed;
    public int modelData;
    public String displayName;
    public int reloadTime;
    public int weight;

    public SimpleInventory inventory;
    public String sysName;

    private final Items damageItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    damage = integer;
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.RED_TERRACOTTA, "&cDamage", 1, Collections.singletonList("&7Current: " + damage), null);
        }
    }));
    private final Items dispersionItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    dispersion = integer;
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.ANVIL, "&7dispersion", 1, Collections.singletonList("&7Current: " + dispersion), null);
        }
    }));
    private final Items shootingSpeedItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    shootingSpeed = (long) integer.doubleValue();
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.SUGAR, "&fShooting Speed", 1, Collections.singletonList("&7Current: " + shootingSpeed), null);
        }
    }));
    private final Items magazineSizeItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    magazineSize = (int) integer.doubleValue();
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.BOOKSHELF, "&7Magazine Size", 1, Collections.singletonList("&7Current: " + magazineSize), null);
        }
    }));
    private final Items bulletSpeedItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    bulletSpeed = (int) integer.doubleValue();
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.SUGAR, "&fBullet speed", 1, Collections.singletonList("&7Current: " + bulletSpeed), null);
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
            return ItemUtils.createItem(Material.RED_TERRACOTTA, "&6Model data", 1, Collections.singletonList("&7Current: " + modelData), null);
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
            return ItemUtils.createItem(Material.RED_TERRACOTTA, "&5Display Name", 1, Collections.singletonList("&7Current: " + displayName), null);
        }
    }));
    private final Items reloadTimeItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    reloadTime = (int) integer.doubleValue();
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.BOOK, "&7Reload time", 1, Collections.singletonList("&7Current: " + reloadTime), null);
        }
    }));

    private final Items weightItems = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            Player player = (Player) event.getWhoClicked();
            new IntegerAsk(player, (integer) -> {
                if (integer != -1)
                    weight = (int) integer.doubleValue();
                refreshInv(player);
            });
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.ANVIL, "&7Weight", 1, Collections.singletonList("&7Current: " + weight), null);
        }
    }));
    private final Items createWeapon = new Items(() -> Collections.singletonList(new Items.ActionItem() {
        @Override
        public void onClick(InventoryClickEvent event) {
            event.getWhoClicked().closeInventory();
            new Weapon(damage, dispersion, magazineSize, shootingSpeed, modelData, displayName, sysName, bulletSpeed, reloadTime, weight);
            event.getWhoClicked().sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().WEAPON_CREATED, "{weapon}->" + displayName + " [" + sysName + "]")));
            Main.getInstance().getWeaponManager().unload(sysName);
        }
        @Override
        public ItemStack getItem() {
            return ItemUtils.createItem(Material.GREEN_WOOL, "&2Create", 1, null, null);
        }
    }));

    public CreateData(Weapon weapon) {
        sysName = weapon.systemName;
        damage = weapon.damage;
        dispersion = weapon.dispersion;
        shootingSpeed = weapon.shootingSpeed;
        magazineSize = weapon.magazineSize;
        bulletSpeed = weapon.bulletSpeed;
        modelData = weapon.modelData;
        displayName = weapon.displayName;
        reloadTime = weapon.reloadTime;
        weight = weapon.weight;
    }

    public CreateData(){}

    public void loadInv(String sysName) {
        this.sysName = sysName;
        loadInv();
    }

    public void loadInv() {
        this.inventory = new SimpleInventory(new HashMap<Integer, Items>() {{
            put(11, damageItems);
            put(15, dispersionItems);
            put(19, shootingSpeedItems);
            put(20, magazineSizeItems);
            put(22, weightItems);
            put(24, modelDataItems);
            put(25, displayNameItems);
            put(29, reloadTimeItems);
            put(33, bulletSpeedItems);
            put(40, createWeapon);
        }}, 5*9, "&aCreating &7new " + sysName, InventoryUtils.allSide());
    }

    public void refreshInv(Player newViewers) {
        BukkitTasks.sync(()->this.inventory.update(newViewers));
    }
}
