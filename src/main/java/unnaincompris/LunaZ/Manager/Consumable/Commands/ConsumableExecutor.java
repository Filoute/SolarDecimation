package unnaincompris.LunaZ.Manager.Consumable.Commands;

import unnaincompris.LunaZ.Commands.Manager.SubCommandExecutor;

public class ConsumableExecutor extends SubCommandExecutor {
    public ConsumableExecutor() {
        super("Consumable", null);

        this.addSubCommand(new CreateConsumable());
        this.addSubCommand(new ListConsumable());
    }
}
