package unnaincompris.LunaZ.Manager.Consumable.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import unnaincompris.LunaZ.Commands.Manager.SubCommand;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.Manager.Weapon.Commands.CreateData;
import unnaincompris.LunaZ.utils.Config;
import unnaincompris.LunaZ.utils.StringUtils;

public class CreateConsumable extends SubCommand implements Listener {
    public CreateConsumable() {
        super("create", Config.SERVER_NAME + ".Consumable.Create", true);
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args == null || args.length == 0) {
            sender.sendMessage(StringUtils.fastReplace(LanguageManager.getLanguage().NEED_TO_PUT_ARGUMENT, "{argument_number}->1", "{hint}->(Need a SYSTEM Weapon Name)"));
            return;
        }
        if(Main.getInstance().getConsumableManager().consumableExist(args[0])) {
            sender.sendMessage(StringUtils.fastReplace(LanguageManager.getLanguage().CONSUMABLE_ALREADY_EXIST, "{consumable}->" + args[0]));
            return;
        }
        CreateConsumableData data = new CreateConsumableData();
        data.loadInv(args[0]);
        data.refreshInv(player);
    }
}
