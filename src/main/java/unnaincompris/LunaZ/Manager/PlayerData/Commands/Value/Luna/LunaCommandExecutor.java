package unnaincompris.LunaZ.Manager.PlayerData.Commands.Value.Luna;

import unnaincompris.LunaZ.Commands.Manager.SubCommandExecutor;

public class LunaCommandExecutor extends SubCommandExecutor {
    public LunaCommandExecutor() {
        super("Luna", null);

        this.addSubCommand(new LunaPay());
        this.addSubCommand(new LunaGive());
        this.addSubCommand(new LunaRemove());
        this.addSubCommand(new LunaShow());
    }
}
