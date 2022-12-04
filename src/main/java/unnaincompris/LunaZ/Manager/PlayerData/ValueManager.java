package unnaincompris.LunaZ.Manager.PlayerData;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import unnaincompris.LunaZ.utils.StringUtils;

public class ValueManager {
    private final PlayerData playerData;
    private final Player player;

    public ValueManager(PlayerData playerData) {
        this.playerData = playerData;
        this.player = Bukkit.getPlayer(playerData.getUUID());
    }

    public boolean hasMoney(long money) {
        return has(playerData.getMoney(), money);
    }
    public boolean hasLuna(long luna) {
        return has(playerData.getLuna(), luna);
    }

    public boolean ifHasTakeLuna(long luna, boolean sendMessage) {
        if(has(playerData.getLuna(), luna)) {
            playerData.setLuna(playerData.getLuna() - luna);
            player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                    LanguageManager.getLanguage().LOSE_LUNA, "{used}->" + luna, "{have_more}->" + playerData.getLuna())));
            return true;
        }
        if(sendMessage && Bukkit.getPlayer(playerData.getUUID()) != null)
            player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                    LanguageManager.getLanguage().DONT_HAVE_ENOUGH_LUNA, "{need}->" + difference(playerData.getLuna(), luna))));
        return false;
    }
    public boolean ifHasTakeMoney(long money, boolean sendMessage) {
        if(has(playerData.getMoney(), money)) {
            playerData.setMoney(playerData.getMoney() - money);
            player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                    LanguageManager.getLanguage().LOSE_MONEY, "{used}->" + money, "{have_more}->" + playerData.getMoney())));
            return true;
        }
        if(sendMessage && Bukkit.getPlayer(playerData.getUUID()) != null)
            player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                    LanguageManager.getLanguage().DONT_HAVE_ENOUGH_MONEY, "{need}->" + difference(playerData.getMoney(), money))));
        return false;
    }

    public void removeLuna(long luna, boolean sendMessage) {
        if(luna > playerData.getLuna()) luna = playerData.getLuna();
        luna = playerData.getLuna() - luna;
        playerData.setLuna(luna);
        if(sendMessage)
            player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().LOSE_LUNA, "{used}->" + luna, "{have}->" + playerData.getLuna())));
    }
    public void removeMoney(long money, boolean sendMessage) {
        if(money > playerData.getMoney()) money = playerData.getMoney();
        money = playerData.getMoney() - money;
        playerData.setMoney(money);
        if(sendMessage)
            player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().LOSE_MONEY, "{used}->" + money, "{have}->" + playerData.getMoney())));
    }

    public void addLuna(long luna, boolean sendMessage) {
        playerData.setLuna(playerData.getLuna() + luna);
        if(sendMessage)
            player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().RECEIVE_LUNA, "{received}->" + luna, "{have}->" + playerData.getLuna())));
    }
    public void addMoney(long money, boolean sendMessage) {
        playerData.setMoney(playerData.getMoney() + money);
        if(sendMessage)
            player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(LanguageManager.getLanguage().RECEIVE_MONEY, "{received}->" + money, "{have}->" + playerData.getMoney())));
    }

    public void sendLuna(long luna, PlayerData target, boolean sendMessage) {
        if(ifHasTakeLuna(luna, false)) {
            target.getValueManager().addLuna(luna, false);
            if(sendMessage) {
                player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                        LanguageManager.getLanguage().SEND_LUNA, "{sent}->" + luna, "{player}->" + Bukkit.getPlayer(target.getUUID()).getDisplayName())));
                Bukkit.getPlayer(target.getUUID()).sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                        LanguageManager.getLanguage().RECEIVE_LUNA_FROM_PLAYER, "{received}->" + luna, "{sender}->" + player.getDisplayName(), "{have}->" + target.getLuna()
                )));
            }
            } else {
            if(sendMessage) {
                player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                        LanguageManager.getLanguage().DONT_HAVE_ENOUGH_LUNA, "{need}->" + difference(playerData.getLuna(), luna))));
            }
        }
    }
    public void sendMoney(long money, PlayerData target, boolean sendMessage) {
        if(ifHasTakeMoney(money, false)) {
            target.getValueManager().addMoney(money, false);
            if(sendMessage) {
                player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                        LanguageManager.getLanguage().SEND_MONEY, "{sent}->" + money, "{player}->" + Bukkit.getPlayer(target.getUUID()).getDisplayName())));
                Bukkit.getPlayer(target.getUUID()).sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                        LanguageManager.getLanguage().RECEIVE_MONEY_FROM_PLAYER, "{received}->" + money, "{sender}->" + player.getDisplayName(), "{have}->" + target.getMoney()
                )));
            }
        } else {
            if(sendMessage) {
                player.sendMessage(ColorUtils.translate(StringUtils.fastReplace(
                        LanguageManager.getLanguage().DONT_HAVE_ENOUGH_MONEY, "{need}->" + difference(playerData.getMoney(), money))));
            }
        }
    }

    private boolean has(long have, long need) {
        return have >= need;
    }
    private long difference(long have, long need) {
        return need - have;
    }
}
