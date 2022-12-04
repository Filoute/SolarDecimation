package unnaincompris.LunaZ.Manager.PlayerData;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import unnaincompris.LunaZ.Manager.PlayerData.Annotation.ShowPlayerData;
import unnaincompris.LunaZ.Manager.PlayerData.data.Stats;

import java.util.UUID;

public class PlayerData {
    //private final String language = "EN"; TODO

    @Expose @Getter private final Stats stats = new Stats();
    @Expose @Getter private final PlayerStatus playerStatus;
    @Expose @Getter private final UUID UUID;
    @Expose @Getter @Setter private long discordID = 0;
    @Expose @Getter @Setter private long money = 0;
    @Expose @Getter @Setter private long luna = 0;

    // Statistics
    @ShowPlayerData @Expose @Getter @Setter private long playerKills = 0;
    @ShowPlayerData @Expose @Getter @Setter private long playerDeath = 0;
    @ShowPlayerData @Expose @Getter @Setter private long zombieKills = 0;
    @ShowPlayerData @Expose @Getter @Setter private long blockBreak = 0;
    @ShowPlayerData @Expose @Getter @Setter private long blockPlace = 0;
    @ShowPlayerData @Expose @Getter @Setter private long messageSent = 0;
    @ShowPlayerData @Expose @Getter @Setter private long commandExecuted = 0;
    @ShowPlayerData @Expose @Getter @Setter private long blockTravel = 0;
    @ShowPlayerData @Expose @Getter @Setter private long blockStep = 0;

    private ValueManager valueManager;

    public ValueManager getValueManager() {
        if(valueManager == null)
            valueManager = new ValueManager(this);
        return valueManager;
    }// Use for Luna & Money

    public PlayerData(UUID UUID) {
        this.UUID = UUID;
        playerStatus = new PlayerStatus(UUID);
    }
}
