package unnaincompris.LunaZ.Handlers.Test;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import unnaincompris.LunaZ.Handlers.Manager.Handler;

public class TestHandler extends Handler implements Listener {

    @EventHandler
    public void onPlaceBlock(BlockPlaceEvent event) {
        if(event.getBlock().getType() != Material.COAL_BLOCK) return;
    }
}
