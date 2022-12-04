package unnaincompris.LunaZ.Commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import unnaincompris.LunaZ.Commands.Manager.BaseCommand;
import unnaincompris.LunaZ.utils.Config;

import java.util.Collections;

public class Tutorial extends BaseCommand {
    public Tutorial() {
        super("tutorial", Collections.singletonList("tut"), Config.SERVER_NAME + ".Tutorial.Tp", true);
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ((Player)sender).teleport(new Location(Bukkit.getWorld("world"), 1620, 89, 978));
        return true;
    }
}
