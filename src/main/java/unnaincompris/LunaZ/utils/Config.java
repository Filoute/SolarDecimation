package unnaincompris.LunaZ.utils;

import com.google.gson.annotations.Expose;
import lombok.Setter;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.utils.Color.ColorUtils;

import java.io.File;

public class Config {

    public static File PLUGIN_FOLDER;
    public static File LANGUAGE_FOLDER;
    public static File PLAYER_DATA;
    public static File WEAPON_DATA;
    public static File BULLET_DATA;
    public static File MAGAZINE_DATA;
    public static File CONSUMABLE_DATA;

    @Expose public static String SERVER_NAME = "LunaZ";
    @Expose public static String SERVER_NAME_TRANSLATE = ColorUtils.translate("&cLuna&5&lZ");
    @Expose public static String SERVER_PREFIX_CONSOLE = "LZ";
    @Expose public static String SERVER_PREFIX_INGAME = ColorUtils.translate("&7[&cL&5&lZ&7]");
    public static String SERVER_VERSION = "v0.3.0";

    @Expose @Setter public static String LANG = "EN";

    public Config() {
        this.setup();
    }

    private void setup() {
        PLUGIN_FOLDER = FileUtils.getOrCreateFile(Main.getInstance().getDataFolder().getParentFile(), "LunaZ"); // Risky to change
        /* */
        LANGUAGE_FOLDER = FileUtils.getOrCreateFile(PLUGIN_FOLDER, "Language");
        PLAYER_DATA = FileUtils.getOrCreateFile(PLUGIN_FOLDER, "Player");
        WEAPON_DATA = FileUtils.getOrCreateFile(PLUGIN_FOLDER, "Weapon");
        BULLET_DATA = FileUtils.getOrCreateFile(PLUGIN_FOLDER, "Bullet");
        MAGAZINE_DATA = FileUtils.getOrCreateFile(PLUGIN_FOLDER, "Magazine");
        CONSUMABLE_DATA = FileUtils.getOrCreateFile(PLUGIN_FOLDER, "Consumable");
    }

    public void saveConfig() {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(PLUGIN_FOLDER, "config"), this);
    }

}
