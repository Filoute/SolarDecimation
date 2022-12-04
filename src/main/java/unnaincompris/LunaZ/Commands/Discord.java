package unnaincompris.LunaZ.Commands;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import unnaincompris.LunaZ.Commands.Manager.BaseCommand;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.Config;
import unnaincompris.LunaZ.utils.StringUtils;

public class Discord extends BaseCommand {
    public Discord() {
        super("Discord", Config.SERVER_NAME + ".Discord.Show");
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof ConsoleCommandSender) {
            sender.sendMessage(ColorUtils.translate("/discord -> &c&lhttps://discord.gg/88gWxXjq"));
            return true;
        }

        Player player = (Player) sender;
        TextComponent message = new TextComponent();
        message.setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, "https://discord.gg/88gWxXjq"));
        message.setText("&7" + StringUtils.repeat(50, "﹊"));
        player.spigot().sendMessage(message);
        message.setText(StringUtils.center("&c&lClick here to join our discord", 50));
        player.spigot().sendMessage(message);
        message.setText("&7" + StringUtils.repeat(50, "﹎"));
        player.spigot().sendMessage(message);

        return true;
    }
}
