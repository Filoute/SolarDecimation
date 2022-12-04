package unnaincompris.LunaZ;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.plugin.java.JavaPlugin;
import unnaincompris.LunaZ.Commands.Manager.CommandManager;
import unnaincompris.LunaZ.Handlers.Manager.HandlerManager;
import unnaincompris.LunaZ.Manager.ActionBarManager.ActionBarManager;
import unnaincompris.LunaZ.Manager.Consumable.ConsumableManager;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.Manager.PlayerData.PlayerDataManager;
import unnaincompris.LunaZ.Manager.ScoreboardManager.ScoreboardManager;
import unnaincompris.LunaZ.Manager.Weapon.WeaponManager;
import unnaincompris.LunaZ.utils.Config;
import unnaincompris.LunaZ.utils.ManagerEnabler;
import unnaincompris.LunaZ.utils.StringUtils;
import unnaincompris.LunaZ.utils.Time.Timer;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public final class Main extends JavaPlugin {

    @Getter private static Main instance;
    private Config serverConfig;

    /* */
    @Getter private HandlerManager handlerManager;
    @Getter private CommandManager commandManager;
    @Getter private LanguageManager languageManager;
    @Getter private PlayerDataManager playerDataManager;
    @Getter private ScoreboardManager scoreboardManager;
    @Getter private ActionBarManager actionBarManager;
    /* */

    /* */
    @Getter private WeaponManager weaponManager;
    @Getter private ConsumableManager consumableManager;
    /* */

    @Override
    public void onEnable() {
        instance = this;

        setupConfig();
        enableManager();
    }

    @Override
    public void onDisable() {
        Timer.resetTimer(false);
        StringUtils.infoGood(Config.SERVER_NAME + " plugins is currently stopping");

        playerDataManager.saveAllData();
        serverConfig.saveConfig();
        languageManager.saveLanguage();
        closePlayersInventory();

        StringUtils.sendMessage(Config.SERVER_NAME + " stop in " + Timer.getBeautyStats(), "&7", "&6&l", "&e&l");
    }

    private void enableManager() {
        Timer.resetTimer(false);
        try {
            for (Field field : this.getClass().getDeclaredFields()) {
                if (!ManagerEnabler.class.isAssignableFrom(field.getType())) continue;
                field.setAccessible(true);
                Constructor<?> constructor = field.getType().getDeclaredConstructor();
                field.set(this, constructor.newInstance());
                StringUtils.infoIdc("Successfully load " + field.getName());
            }
        } catch (Exception exception) {
            StringUtils.sendErrorMessage("Unable to load Handlers", exception, "&4&l");
            return;
        }
        StringUtils.info("Successfully load all Handlers in " + Timer.getBeautyStats());
    }

    private void setupConfig() {
        Timer.resetTimer(false);
        serverConfig = new Config();
        StringUtils.info("Successfully load Config in " + Timer.getBeautyStats());
    }

    private void closePlayersInventory() {
        Bukkit.getOnlinePlayers().forEach(HumanEntity::closeInventory);
    }
}
