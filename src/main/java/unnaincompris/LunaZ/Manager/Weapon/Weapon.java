package unnaincompris.LunaZ.Manager.Weapon;

import com.google.gson.annotations.Expose;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.ActionBarManager.ActionBarManager;
import unnaincompris.LunaZ.Manager.ActionBarManager.Message;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.Manager.PlayerData.PlayerStatus;
import unnaincompris.LunaZ.Manager.Weapon.Magazine.Magazine;
import unnaincompris.LunaZ.utils.*;
import unnaincompris.LunaZ.utils.Time.Timer;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class Weapon {

    @Expose public double damage;
    @Expose public double dispersion;
    @Expose public long shootingSpeed;
    @Expose public int modelData;
    @Expose public String displayName;
    @Expose public int reloadTime;
    @Expose public int weight;
    //TODO
    @Expose public Magazine magazine;
    @Expose public boolean canReloadWithBullet;
    @Expose public boolean onlyWithBullet;
    @Expose public unnaincompris.LunaZ.Manager.Weapon.Bullet.Bullet bullet;

    @Expose public String systemName;

    public Weapon(double damage, double dispersion, long shootingSpeed /* Time between shot in MS */, int modelData, String displayName, String systemName, int reloadTime, int weight, Magazine magazine) {
        this.damage = damage; this.dispersion = dispersion; this.shootingSpeed = shootingSpeed;
        this.modelData = modelData;
        this.systemName = systemName; this.displayName = displayName;
        this.reloadTime = reloadTime;
        this.weight = weight;
        saveWeapon();
    }
    private void saveWeapon() {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(Config.WEAPON_DATA, systemName), this);
    }

    private Timer timer = new Timer();
    public void shoot(Player shooter, ItemStack item) {
        if(timer == null)
            timer = new Timer();
        if(timer.hasTimePassed(shootingSpeed)) {
            int reamingMagazine = NBTUtils.toNBT(item).getInteger("WeaponReamingMagazine");
            shooter.getInventory().setItemInMainHand(new NBTUtils(item).set("WeaponReamingMagazine", reamingMagazine-1).build());
            displayBullet(shooter, item);
            bullet.shoot(shooter, this);
            timer.resetTimer();
        }
    }

    public void affectDamage(LivingEntity entity, Player shooter) {
        if(entity instanceof Player) {
            affectDamage((Player) entity, shooter);
            return;
        }
        entity.damage(damage, shooter);
    }
    private void affectDamage(Player entity, Player shooter) {
        double damageToDealt = damage;
        //TODO damage equation
        entity.damage(damageToDealt, shooter);
    }

    public ItemStack getAsSimpleItem() { // Simple = no lore
        return new NBTUtils(ItemUtils.setCustomModelTexture(ItemUtils.createItem(Material.STICK, displayName, 1, null, null), modelData))
                .set("weaponSystemName", this.systemName).set("weaponTag", true).build();
    }

    public ItemStack getItem() { // With lore
        return new NBTUtils(ItemUtils.setCustomModelTexture(ItemUtils.createItem(Material.STICK, displayName, 1, Arrays.asList("&7Damage: &c" + damage, "&7Magazine: &7" + magazine.magazineSize, "&7Shooting Speed: &7" + shootingSpeed, "&7Bullet dispersion: &7" + dispersion, "&7Reload time: &7" + reloadTime), null), modelData))
                .set("weaponSystemName", this.systemName).set("weaponTag", true).set("WeaponReamingMagazine", magazine.magazineSize).build();
    }

    public void reload(ItemStack item, Player player) {
        if(magazine.haveMagazine(player) == null) return;
        PlayerStatus playerStatus = PlayerStatus.getPlayerStatus(player);
        if(playerStatus.reloadingWeapon) return;
        else playerStatus.reloadingWeapon = true;
        playerStatus.applyFactorSpeed("ReloadWeapon", 0.80f, -1);
        new BukkitRunnable() {
            final AtomicInteger counter = new AtomicInteger();
            @Override
            public void run() {
                final ActionBarManager actionBarManager = Main.getInstance().getActionBarManager();
                counter.getAndIncrement();
                ItemStack mag = magazine.haveMagazine(player);
                if (!player.getInventory().getItemInMainHand().equals(item) || mag == null) {
                    actionBarManager.removeByReason(player, "ReloadWeapon");
                    playerStatus.reloadingWeapon = false;
                    playerStatus.cancelByReason("ReloadWeapon", true);
                    cancel();
                    return;
                }
                if (counter.get() == reloadTime) {
                    playerStatus.cancelByReason("ReloadWeapon", true);
                    playerStatus.reloadingWeapon = false;
                    magazine.removeMag(player, 1);
                    actionBarManager.getByPlayer(player).removeByReason("DisplayBullets");
                    actionBarManager.addPriority(player, new Message(20, "ReloadWeapon", () -> LanguageManager.getLanguage().WEAPON_STOP_RELOADING));
                    player.getInventory().setItemInMainHand(new NBTUtils(item).set("WeaponReamingMagazine", magazine.magazineSize).build());
                    this.cancel();
                    return;
                }
                Message.MessageUpdater message = () ->
                        StringUtils.fastReplace(LanguageManager.getLanguage().WEAPON_RELOADING,
            "{progress}->" + StringUtils.progressBar(counter.get(), reloadTime));
                actionBarManager.addPriority(player, new Message(20, "ReloadWeapon", message));
            }
        }.runTaskTimer(Main.getInstance(), 0, 5);
    }

    public void displayBullet(Player player, ItemStack item) {
        final ActionBarManager actionBarManager = Main.getInstance().getActionBarManager();
        int reamingMagazine = NBTUtils.toNBT(item).getInteger("WeaponReamingMagazine");
        Message.MessageUpdater message = () -> StringUtils.fastReplace(LanguageManager.getLanguage().WEAPON_DISPLAY_MAGAZINE,
                "{current}->" + (reamingMagazine - 1), "{max}->" + magazine.magazineSize);
        actionBarManager.add(player, new Message(20, "DisplayBullets", message));
    }
}
