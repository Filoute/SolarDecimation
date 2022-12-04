package unnaincompris.LunaZ.Manager.PlayerData.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import unnaincompris.LunaZ.Commands.Manager.SubCommand;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.Config;
import unnaincompris.LunaZ.utils.InteractiveAsk.BooleanAsk;
import unnaincompris.LunaZ.utils.StringUtils;

public class ResetPlayerData extends SubCommand {

    public ResetPlayerData() {
        super("reset", Config.SERVER_NAME + "PlayerData.Reset", false);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        if(player == null) return;
        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().SUCCESSFULLY_DELETE_FILE, "{file_name}->" + player.getUniqueId())));
            Main.getInstance().getPlayerDataManager().deletePlayerData(player.getUniqueId());
        }
        else
            new BooleanAsk((Player) sender, "&7Are sure you want to delete " + player.getName() + "player data",
            (respond) -> {
                if(respond) {
                    sender.sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().SUCCESSFULLY_DELETE_FILE, "{file_name}->" + player.getUniqueId())));
                    Main.getInstance().getPlayerDataManager().deletePlayerData(player.getUniqueId());
                }
            });
    }
}
