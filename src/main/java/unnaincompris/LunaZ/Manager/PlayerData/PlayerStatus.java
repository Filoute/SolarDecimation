package unnaincompris.LunaZ.Manager.PlayerData;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import unnaincompris.LunaZ.Main;
import unnaincompris.LunaZ.utils.PlayerUtils;
import unnaincompris.LunaZ.utils.Task;

import java.util.HashMap;
import java.util.UUID;

public class PlayerStatus {

    @Expose private final UUID UUID;


    /* Info Weapon */
    @Expose public boolean reloadingWeapon;
    @Expose public boolean reloadingMagazine;
    @Expose public boolean zooming;
    /*             */
    @Expose @Getter private float defaultSpeed;
    @Expose @Getter public float currentSpeed;

    private Player player;
    @Expose final private HashMap<String, Task> delayAction = new HashMap<>();

    public void applyValueSpeed(String reason, float change, int time) {
        if(!setPlayer())
            return;
        changeWalkSpeed(-change);
        if(delayAction.containsKey(reason))
            cancelByReason(reason, true);
        delayAction.put(reason, new Task(() -> changeWalkSpeed(+change)).runLater(time));
    }

    public void applyFactorSpeed(String reason, float factor, int time) {
        if(!setPlayer())
            return;
        if(time < 0) time = Integer.MAX_VALUE;
        changeWalkSpeed(-(currentSpeed * factor));
        if(delayAction.containsKey(reason))
            cancelByReason(reason, true);
        delayAction.put(reason, new Task(() -> changeWalkSpeed(+(currentSpeed * factor))).runLater(time));
    }

    public void cancelByReason(String reason, boolean execute) {
        delayAction.get(reason).cancel(execute);
    }


    private void changeWalkSpeed(float newSpeed) {
        if(!setPlayer()) return;
        player.setWalkSpeed(newSpeed);
        currentSpeed = newSpeed;
    }

    private boolean setPlayer() {
        if(PlayerUtils.isOnline(UUID)) {
            if(player == null)
                player = Bukkit.getPlayer(UUID);
            return true;
        }
        return false;
    }

    public static PlayerStatus getPlayerStatus(UUID id) {
        return Main.getInstance().getPlayerDataManager().getDataFromUUID(id).getPlayerStatus();
    }
    public static PlayerStatus getPlayerStatus(Player id) {
        return Main.getInstance().getPlayerDataManager().getDataFromUUID(id.getUniqueId()).getPlayerStatus();
    }

    public PlayerStatus(UUID UUID) {
        this.UUID = UUID;
        reloadingWeapon = false;
        reloadingMagazine = false;
        zooming = false;

        if(!PlayerUtils.isOnline(UUID)) return;
        player = Bukkit.getPlayer(UUID);
        defaultSpeed = player.getWalkSpeed();
        currentSpeed = defaultSpeed;
    }
}
