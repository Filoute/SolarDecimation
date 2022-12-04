package unnaincompris.LunaZ.Manager.PlayerData.Commands.Value.Money;

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

public class MoneyShow extends SubCommand {

    public MoneyShow() {
        super("show", Config.SERVER_NAME + ".Money.Show", true);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        if(player == null) {
            PlayerData toShowData = Main.getInstance().getPlayerDataManager().getDataFromUUID(((Player) sender).getUniqueId());
            sender.sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                    LanguageManager.getLanguage().HAVE_MONEY, "{have}->" + toShowData.getMoney()
            )));
        } else {
            PlayerData toShowData = Main.getInstance().getPlayerDataManager().getDataFromUUID((player).getUniqueId());
            sender.sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                    LanguageManager.getLanguage().PLAYER_HAVE_MONEY, "{have}->" + toShowData.getMoney(), "{player}->" + player.getDisplayName()
            )));
        }
    }
}
