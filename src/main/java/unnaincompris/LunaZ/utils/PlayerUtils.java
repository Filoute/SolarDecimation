package unnaincompris.LunaZ.utils;

import org.bukkit.Bukkit;

import java.util.UUID;

public class PlayerUtils {

    public static boolean isOnline(UUID UUID) {
        return Bukkit.getPlayer(UUID) != null;
    }

}
