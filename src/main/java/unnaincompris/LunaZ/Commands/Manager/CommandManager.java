package unnaincompris.LunaZ.Commands.Manager;

import org.bukkit.command.CommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import unnaincompris.LunaZ.Commands.Discord;
import unnaincompris.LunaZ.Commands.Tutorial;
import unnaincompris.LunaZ.utils.Config;
import unnaincompris.LunaZ.utils.ManagerEnabler;
import unnaincompris.LunaZ.utils.nms.NmsUtils;

import java.util.ArrayList;
import java.util.List;

public class CommandManager implements ManagerEnabler {

    private final CommandMap commandMap;
    private final List<BaseCommand> commands;

    public CommandManager() {
        commandMap = NmsUtils.getCommandMap();
        commands = new ArrayList<>();

        this.commands.add(new Tutorial());
        this.commands.add(new Discord());

        commands.forEach(this::registerCommand);
    }

    public void registerCommand(BukkitCommand command) {
        commandMap.register(Config.SERVER_NAME.toLowerCase(), command);
    }
}
