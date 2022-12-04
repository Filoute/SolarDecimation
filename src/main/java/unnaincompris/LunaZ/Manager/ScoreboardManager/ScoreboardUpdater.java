package unnaincompris.LunaZ.Manager.ScoreboardManager;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import fr.mrmicky.fastboard.FastBoard;
import lombok.Getter;
import net.sacredlabyrinth.phaed.simpleclans.Clan;
import net.sacredlabyrinth.phaed.simpleclans.SimpleClans;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.Manager.Language.LanguageManager;
import unnaincompris.LunaZ.Manager.PlayerData.PlayerData;
import unnaincompris.LunaZ.utils.Color.ColorUtils;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardUpdater implements Listener {

    private final ScoreboardManager manager;
    private final Player player;
    private final @Getter FastBoard board;

    private PlayerData playerData;

    public ScoreboardUpdater(ScoreboardManager manager, Player player) {
        this.manager = manager;
        this.player = player;
        this.board = new FastBoard(player);
        this.board.updateTitle(ColorUtils.translate("&cLuna&5&lZ"));
    }

    final List<String> scoreboardContent = new ArrayList<>();

    public void run() {
        try {
            scoreboardContent.clear();
            playerData = Main.getInstance().getPlayerDataManager().getDataFromUUID(player.getUniqueId());

            scoreboardContent.add("");
            scoreboardContent.add(player.getDisplayName());
            scoreboardContent.add("&8| &7Luna : &6" + playerData.getLuna());
            scoreboardContent.add("&8| &7Money : &e" + playerData.getMoney());
            Clan clan = SimpleClans.getInstance().getClanManager().getClanByPlayerUniqueId(player.getUniqueId());
            if(clan != null)
                scoreboardContent.add("&8| &7Gang: " + clan.getTag() + " &7[&6"+ clan.getOnlineMembers().size() + "&7/&6" + clan.getSize() + "&7]");
            scoreboardContent.add(" ");
            scoreboardContent.add("&7Information:");
            scoreboardContent.add("&8| &7Kills : &c" + playerData.getPlayerKills());
            scoreboardContent.add("&8| &7Zombie Kills : &c" + playerData.getZombieKills());
            scoreboardContent.add("&8| &7Death : &c" + playerData.getPlayerDeath());
            List<String> currentZone = getRegionNameAsList(player);
            if (isADistrict(currentZone)) {
                scoreboardContent.add("");
                setDistrictContent(getDistrictName(ColorUtils.strip(ColorUtils.translate(currentZone.get(currentZone.size()-1)))));
                if (isASpecifyDistrict(LanguageManager.getLanguage().RADIOACTIVE, currentZone)) {
                    setRadioactiveContent();
                }
            }
        }catch (Exception ex) {
            ex.printStackTrace();
        }
        board.updateLines(ColorUtils.translate(scoreboardContent));
    }

    private void setDistrictContent(String district) {
        scoreboardContent.add("&8| &7Current zone : " + district);
        scoreboardContent.add("&8| &7Player in zone : " + getPlayerInSameZone());
    }

    private void setRadioactiveContent() {
        double radioactivity = playerData.getStats().getRadioactivity();
        scoreboardContent.add("&8| &aRadioactivity : " + (radioactivity < 30 ? "&2" : (radioactivity < 75 ? "&6" : "&c")) + radioactivity + " &7/&2 100.00");
    }


    private int getPlayerInSameZone() {
        int playerInSameZone = 0;
        for(Player player : Bukkit.getOnlinePlayers()) {
            if(isInSameRegion(this.player, player))
                playerInSameZone++;
        }
        return playerInSameZone;
    }

    private boolean isADistrict(List<String> zones) {
        for(String zone : zones) {
            if(isADistrict(ColorUtils.strip(ColorUtils.translate(zone))))
                return true;
        }
        return false;
    }

    private boolean isASpecifyDistrict(String district, List<String> zone) {
        for(String str : zone) {
            if(ColorUtils.strip(ColorUtils.translate(district)).equalsIgnoreCase(ColorUtils.strip(ColorUtils.translate(str))))
                return true;
        }
        return false;
    }

    private String getDistrictName(String zone) {
        if(ColorUtils.strip(ColorUtils.translate(LanguageManager.getLanguage().DISTRICT_1)).equalsIgnoreCase(zone)) return LanguageManager.getLanguage().DISTRICT_1;
        else if(ColorUtils.strip(ColorUtils.translate(LanguageManager.getLanguage().DISTRICT_2)).equalsIgnoreCase(zone)) return LanguageManager.getLanguage().DISTRICT_2;
        else if(ColorUtils.strip(ColorUtils.translate(LanguageManager.getLanguage().DISTRICT_3)).equalsIgnoreCase(zone)) return LanguageManager.getLanguage().DISTRICT_3;
        else if(ColorUtils.strip(ColorUtils.translate(LanguageManager.getLanguage().MILITARY)).equalsIgnoreCase(zone)) return LanguageManager.getLanguage().MILITARY;
        else if(ColorUtils.strip(ColorUtils.translate(LanguageManager.getLanguage().RADIOACTIVE)).equalsIgnoreCase(zone)) return LanguageManager.getLanguage().RADIOACTIVE;
        return "";
    }

    private boolean isADistrict(String zone) {
        return ColorUtils.strip(ColorUtils.translate(LanguageManager.getLanguage().DISTRICT_1)).equalsIgnoreCase(zone) ||
                ColorUtils.strip(ColorUtils.translate(LanguageManager.getLanguage().DISTRICT_2)).equalsIgnoreCase(zone) ||
                ColorUtils.strip(ColorUtils.translate(LanguageManager.getLanguage().DISTRICT_3)).equalsIgnoreCase(zone) ||
                ColorUtils.strip(ColorUtils.translate(LanguageManager.getLanguage().MILITARY)).equalsIgnoreCase(zone) ||
                ColorUtils.strip(ColorUtils.translate(LanguageManager.getLanguage().RADIOACTIVE)).equalsIgnoreCase(zone);
    }

    private boolean isInSameRegion(Player one, Player two) {
        List<String> oneList = getRegionNameAsList(one);
        List<String> twoList = getRegionNameAsList(two);
        if(twoList.size() == 0 || oneList.size() == 0) return false;
        return twoList.get(twoList.size()-1).equalsIgnoreCase(oneList.get(oneList.size()-1));
    }

    private List<String> getRegionNameAsList(Player player) {
        List<String> currentZone = new ArrayList<>();
        WorldGuard.getInstance().getPlatform().getRegionContainer().
                get(BukkitAdapter.adapt(player.getLocation().getWorld())).
                getApplicableRegions(BukkitAdapter.asBlockVector(player.getLocation())).getRegions().forEach(protectedRegion -> currentZone.add(protectedRegion.getId()));
        return currentZone;
    }

}
