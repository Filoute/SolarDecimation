package unnaincompris.LunaZ.Manager.PlayerData.Commands;

import unnaincompris.LunaZ.Commands.Manager.SubCommandExecutor;
import unnaincompris.LunaZ.Manager.PlayerData.Commands.Value.Luna.LunaCommandExecutor;
import unnaincompris.LunaZ.Manager.PlayerData.Commands.Value.Money.MoneyCommandExecutor;

public class PlayerDataCommandExecutor extends SubCommandExecutor {

    public PlayerDataCommandExecutor() {
        super("Stats", null);

        this.addSubCommand(new ResetPlayerData());
        this.addSubCommand(new ShowPlayerData());
        new LunaCommandExecutor();
        new MoneyCommandExecutor();
    }
}
