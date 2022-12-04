package unnaincompris.LunaZ.Manager.Weapon.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import unnaincompris.LunaZ.Commands.Manager.SubCommand;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.Config;
import unnaincompris.LunaZ.utils.StringUtils;

public class CreateWeapon extends SubCommand implements Listener {
    public CreateWeapon() {
        super("create", Config.SERVER_NAME + ".Weapon.Create", true);
        Bukkit.getPluginManager().registerEvents(this, Main.getInstance());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        if(args == null || args.length == 0) {
            sender.sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().NEED_TO_PUT_ARGUMENT, "{argument_number}->1", "{hint}->(Need a SYSTEM Weapon Name)")));
            return;
        }
        if(Main.getInstance().getWeaponManager().weaponExist(args[0])) {
            sender.sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().WEAPON_ALREADY_EXIST, "{weapon}->" + args[0])));
            return;
        }
        CreateData data = new CreateData();
        data.loadInv(args[0]);
        data.refreshInv(player);
    }
}
