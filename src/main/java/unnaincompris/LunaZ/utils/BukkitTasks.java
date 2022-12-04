package unnaincompris.LunaZ.utils;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import unnaincompris.LunaZ.Main;

public class BukkitTasks {
    public static void sync(Callable callable) {
        Bukkit.getScheduler().runTask(Main.getInstance(), callable::call);
    }

    public static BukkitTask syncLater(Callable callable, long delay) {
        return Bukkit.getScheduler().runTaskLater(Main.getInstance(), callable::call, delay);
    }

    public static BukkitTask syncTimer(Callable callable, long delay, long value) {
        return Bukkit.getScheduler().runTaskTimer(Main.getInstance(), callable::call, delay, value);
    }

    public static void async(Callable callable) {
        Bukkit.getScheduler().runTaskAsynchronously(Main.getInstance(), callable::call);
    }

    public static BukkitTask asyncLater(Callable callable, long delay) {
        return Bukkit.getScheduler().runTaskLaterAsynchronously(Main.getInstance(), callable::call, delay);
    }

    public static BukkitTask asyncTimer(Callable callable, long delay, long value) {
        return Bukkit.getScheduler().runTaskTimerAsynchronously(Main.getInstance(), callable::call, delay, value);
    }


    public interface Callable {
        void call();
    }
}
