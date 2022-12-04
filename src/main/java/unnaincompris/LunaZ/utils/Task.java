package unnaincompris.LunaZ.utils;

import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import unnaincompris.LunaZ.Main;

public class Task {

    public Task(Callable callable) {
        todo = callable;
    }

    private final Callable todo;
    private BukkitTask task;

    public Task runLater(int tick) {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                todo.call();
            }
        }.runTaskLater(Main.getInstance(), tick);
        return this;
    }

    public void cancel(boolean execute) {
        if(task != null) {
            task.cancel();
            task = null;
            if (execute)
                todo.call();
        }
    }

    public Task run() {
        new BukkitRunnable() {
            @Override
            public void run() {
                todo.call();
            }
        }.runTask(Main.getInstance());
        return this;
    }

    public interface Callable {
        void call();
    }
}

