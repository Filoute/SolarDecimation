package unnaincompris.LunaZ.Handlers.Manager;

import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import unnaincompris.LunaZ.Handlers.DynamicHandler;
import unnaincompris.LunaZ.Handlers.Test.TestHandler;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.utils.ManagerEnabler;

import java.util.ArrayList;
import java.util.List;

public class HandlerManager implements ManagerEnabler {

    private final List<Handler> handlerList = new ArrayList<>();

    public HandlerManager() {
        handlerList.add(new TestHandler());
        handlerList.add(new DynamicHandler());

        this.handlerList.stream().filter(handler -> handler instanceof Listener).forEach(
                handler -> Bukkit.getPluginManager().registerEvents((Listener) handler, Main.getInstance()));
    }

    public void disable() {
        try {
            this.handlerList.forEach(Handler::disable);
        } catch(Exception e) {
            e.printStackTrace();
        }

        this.handlerList.clear();
    }

    public Handler getHandler(Class<?> neededHandler) {
        return this.handlerList.stream().filter(handler -> handler.getClass() == neededHandler).findFirst().orElse(null);
    }
}
