package unnaincompris.LunaZ.Manager.PlayerData.Commands.Value.Luna;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import unnaincompris.LunaZ.Commands.Manager.SubCommand;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.Manager.PlayerData.PlayerData;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.Config;
import unnaincompris.LunaZ.utils.StringUtils;

public class LunaShow extends SubCommand {

    public LunaShow() {
        super("show", Config.SERVER_NAME + ".Luna.Show", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        if(player == null) {
            PlayerData toShowData = Main.getInstance().getPlayerDataManager().getDataFromUUID(((Player) sender).getUniqueId());
            sender.sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                    LanguageManager.getLanguage().HAVE_LUNA, "{have}->" + toShowData.getLuna()
            )));
        } else {
            PlayerData toShowData = Main.getInstance().getPlayerDataManager().getDataFromUUID((player).getUniqueId());
            sender.sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                    LanguageManager.getLanguage().PLAYER_HAVE_LUNA, "{have}->" + toShowData.getLuna(), "{player}->" + player.getDisplayName()
            )));
        }
    }
}
