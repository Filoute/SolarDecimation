package unnaincompris.LunaZ.Manager.Consumable;

import com.google.gson.annotations.Expose;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.ActionBarManager.ActionBarManager;
import unnaincompris.LunaZ.Manager.ActionBarManager.Message;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.utils.*;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.Time.Time;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Consumable {

    @Expose public String systemName;

    @Expose public List<PotionEffect> effectList;
    @Expose public int heartToHeal;
    @Expose public int timeToGetHeal;
    @Expose public int timeWhileGettingHeal;
    @Expose public int foodToGain;
    @Expose public int timeToGetFood;
    @Expose public int timeWhileGettingFood;

    @Expose public int modelData;
    @Expose public String displayName;

    private ItemStack item;

    public Consumable(int heartToHeal, int timeToGetHeal, int timeWhileGettingHeal, int foodToGain, int timeToGetFood, int timeWhileGettingFood, int modelData, String displayName, String systemName, List<PotionEffect> effectList) {
        this.heartToHeal = heartToHeal; this.timeToGetHeal = timeToGetHeal; this.timeWhileGettingHeal = timeWhileGettingHeal;
        this.foodToGain = foodToGain; this.timeToGetFood = timeToGetFood; this.timeWhileGettingFood = timeWhileGettingFood;
        this.modelData = modelData;
        this.displayName = displayName; this.systemName = systemName;
        this.effectList = effectList;
        saveConsumable();
    }

    public Consumable() {
        if(effectList == null) effectList = new ArrayList<>();
    }

    private void saveConsumable() {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(Config.CONSUMABLE_DATA, systemName), this);
    }

    public ItemStack getAsSimpleItem() { // Simple = no lore
        return new NBTUtils(ItemUtils.setCustomModelTexture(ItemUtils.createItem(Material.STICK, displayName, 1, null, null), modelData))
                .set("consumableSystemName", this.systemName).set("consumableTag", true).build();
    }

    public ItemStack getItem() { // With lore
        List<String> lore = new ArrayList<>();
        if(heartToHeal != 0) {
            if (timeWhileGettingHeal == 0)
                lore.add("&cHeal&7: &7" + heartToHeal + "&c♡" + (timeToGetHeal != 0 ? " &7[" + timeToGetHeal + "]" : ""));
            else
                lore.add("&cHeal&7: &7" + (heartToHeal / (timeWhileGettingHeal * 20)) + "&c♡s" + (timeToGetHeal != 0 ? " &7[" + timeToGetHeal + "]" : ""));
        }
        if(foodToGain != 0) {
            if (timeWhileGettingFood == 0)
                lore.add("&6Food&7: &7" + foodToGain + "&6♨" + (timeToGetFood != 0 ? " &7[" + timeToGetFood + "]" : ""));
            else
                lore.add("&6Food&7: &7" + (foodToGain / (timeWhileGettingFood * 20)) + "&6♨s" + (timeToGetFood != 0 ? " &7[" + timeToGetFood + "]" : ""));

        }
        for(PotionEffect effect : effectList) {
            lore.add("&d" + StringUtils.upperCaseFirstLetter(effect.getType().getName()) + "&7: " + effect.getAmplifier() + " [" + new Time(effect.getDuration() / 20, 0, 0).getBeautyStats() + "]");
        }
        return new NBTUtils(ItemUtils.setCustomModelTexture(ItemUtils.createItem(Material.CHORUS_FRUIT, displayName, 1, lore, null), modelData))
                .set("consumableSystemName", this.systemName).set("consumableTag", true).build(); // PlayerItemConsumeEvent
    }

    public void apply(Player player) {
        StringUtils.log("4");
        new BukkitRunnable() {
            final AtomicInteger counter = new AtomicInteger(-1);
            boolean isItemRemove = false;
            final ActionBarManager actionBarManager = Main.getInstance().getActionBarManager();
            @Override
            public void run() {
                counter.getAndIncrement();
                actionBarManager.getByPlayer(player).removeByReason("ConsumableTimer");
                if (!Main.getInstance().getConsumableManager().getConsumable(player.getInventory().getItemInMainHand()).systemName.equals(systemName) && !isItemRemove) {
                    player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().CONSUMABLE_ABORT_CONSUME,
                            "{consumable}->" + displayName)));
                    StringUtils.log("5");
                    cancel();
                    return;
                }
                if((counter.get() > timeToGetHeal || counter.get() > timeToGetFood) && !isItemRemove) {
                    StringUtils.log("6");
                    isItemRemove = true;
                    if(player.getInventory().getItemInMainHand().getAmount() == 1) player.getInventory().setItemInMainHand(ItemUtils.AIR);
                    else player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                }

                if(counter.get() > timeToGetHeal && counter.get() > timeToGetFood) {
                    StringUtils.log("7");
                    player.addPotionEffects(effectList);
                    cancel();
                    return;
                }
                if(counter.get() <= timeToGetHeal)
                    healRunnable(counter, player);
                if(counter.get() <= timeToGetFood)
                    foodRunnable(counter, player);
            }
        }.runTaskTimer(Main.getInstance(), 0, 1);
    }

    public void healRunnable(AtomicInteger counter, Player player) {
        if (counter.get() == timeToGetHeal) {
            StringUtils.log("Heal");
            if(timeWhileGettingHeal != 0) {
                new BukkitRunnable() {
                    final AtomicInteger counter = new AtomicInteger();
                    @Override
                    public void run() {
                        counter.getAndIncrement();
                        if(counter.get() <= heartToHeal) {
                            cancel();
                            return;
                        }
                        double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                        player.setHealth(Math.min(maxHealth, player.getHealth() + 1));
                    }
                }.runTaskTimer(Main.getInstance(), 0, timeWhileGettingHeal / heartToHeal);
            }
            else {
                double maxHealth = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
                player.setHealth(Math.min(maxHealth, player.getHealth() + heartToHeal));
                StringUtils.log("Heal Done");
            }
            return;
        }
        if(counter.get() == 0) {
            StringUtils.log("Heal Message");
            final ActionBarManager actionBarManager = Main.getInstance().getActionBarManager();
            Message.MessageUpdater message = () ->
                    StringUtils.fastReplace(LanguageManager.getLanguage().CONSUMABLE_USE,
                            "{progress}->" + StringUtils.progressBar(counter.get(), timeToGetHeal)
                            , "{consumable_name}->" + displayName);

            if(actionBarManager.getByPlayer(player).getByReason("ConsumableTimer") != null) {
                actionBarManager.getByPlayer(player).getByReason("ConsumableTimer").add(message);
            } else {
                actionBarManager.add(player, new Message(20, "ConsumableTimer", message));
            }
        }
    }
    public void foodRunnable(AtomicInteger counter, Player player) {
        if (counter.get() == timeToGetFood) {
            StringUtils.log("Food");
            if(timeWhileGettingFood != 0) {
                new BukkitRunnable() {
                    final AtomicInteger counter = new AtomicInteger();
                    @Override
                    public void run() {
                        counter.getAndIncrement();
                        if(counter.get() <= foodToGain) {
                            cancel();
                            return;
                        }
                        player.setFoodLevel(Math.min(20, player.getFoodLevel() + 1));
                    }
                }.runTaskTimer(Main.getInstance(), 0, timeWhileGettingFood / foodToGain);
            }
            else
                player.setFoodLevel(Math.min(20, player.getFoodLevel() + foodToGain));
            StringUtils.log("Food feed");
            return;
        }
        if(counter.get() == 0) {
            StringUtils.log("Food Message");
            Message.MessageUpdater message = () ->
                    StringUtils.fastReplace(LanguageManager.getLanguage().CONSUMABLE_USE,
                            "{progress}->" + StringUtils.progressBar(counter.get(), timeToGetFood)
                            , "{consumable_name}->" + displayName);

            if(Main.getInstance().getActionBarManager().getByPlayer(player).getByReason("ConsumableTimer") != null) {
                Main.getInstance().getActionBarManager().getByPlayer(player).getByReason("ConsumableTimer").add(message);
            } else {
                Main.getInstance().getActionBarManager().add(player, new Message(20, "ConsumableTimer", message));
            }
        }
    }
}
