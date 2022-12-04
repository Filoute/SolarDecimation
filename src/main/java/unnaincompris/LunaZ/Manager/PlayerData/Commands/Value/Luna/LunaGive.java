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

public class LunaGive extends SubCommand {

    public LunaGive() {
        super("give", Config.SERVER_NAME + ".Luna.Give", false);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = Bukkit.getPlayer(args[0]);
        if(player == null) {
            sender.sendMessage(StringUtils.fastReplace(LanguageManager.getLanguage().INVALID_ARGUMENT, "{argument}->" + args[0], "{hint}->(Need a online player)"));
            return;
        }
        if(!StringUtils.isInteger(args[1])) {
            sender.sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().INVALID_ARGUMENT, "{argument}->" + args[1], "{hint}->(Need an Integer)")));
            return;
        }
        int value = Integer.parseInt(args[1]);
        PlayerData target = Main.getInstance().getPlayerDataManager().getDataFromUUID(player.getUniqueId());
        target.getValueManager().addLuna(value, true);
    }
}
