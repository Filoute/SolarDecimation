package unnaincompris.LunaZ.Manager.PlayerData;

import unnaincompris.LunaZ.Manager.PlayerData.Commands.PlayerDataCommandExecutor;
import unnaincompris.LunaZ.Manager.PlayerData.Listeners.UpdateData;
import unnaincompris.LunaZ.utils.Config;
import unnaincompris.LunaZ.utils.FileUtils;
import unnaincompris.LunaZ.utils.JsonUtils;
import unnaincompris.LunaZ.utils.ManagerEnabler;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDataManager implements ManagerEnabler {
    private HashMap<UUID, PlayerData> caches = new HashMap<>();

    public PlayerDataManager() {
        new PlayerDataCommandExecutor();
        new UpdateData();
    }

    public PlayerData getDataFromUUID(UUID UUID) {
        if(caches.containsKey(UUID))
            return caches.get(UUID);
        else {
            PlayerData data = JsonUtils.getData(JsonUtils.getOrCreateJson(Config.PLAYER_DATA, UUID.toString()), PlayerData.class);
            if(data == null) data = new PlayerData(UUID);
            caches.put(UUID, data);
            return data;
        }
    }

    public void saveAllData() {
        caches.values().forEach(this::savePlayerData);
    }

    public void savePlayerData(PlayerData playerData) {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(Config.PLAYER_DATA, playerData.getUUID().toString()), playerData);
    }

    public void deletePlayerData(UUID UUID) {
        FileUtils.deleteFile(JsonUtils.getOrCreateJson(Config.PLAYER_DATA, UUID.toString()));
    }

    public void savePlayerData(UUID UUID) {
        if(!caches.containsKey(UUID)) JsonUtils.writeData(JsonUtils.getOrCreateJson(Config.PLAYER_DATA, UUID.toString()), new PlayerData(UUID));
        JsonUtils.writeData(JsonUtils.getOrCreateJson(Config.PLAYER_DATA, UUID.toString()), caches.get(UUID));
    }

    public boolean exist(UUID UUID) {
        return FileUtils.isExist(Config.PLAYER_DATA, UUID.toString() + ".json");
    }

    public void emptyCache() {
        saveAllData();
        caches = new HashMap<>();
    }

}
