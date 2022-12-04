package unnaincompris.LunaZ.Manager.PlayerData.Commands.Value.Money;

import unnaincompris.LunaZ.Commands.Manager.SubCommandExecutor;

import java.util.Arrays;

public class MoneyCommandExecutor extends SubCommandExecutor {
    public MoneyCommandExecutor() {
        super("Money", Arrays.asList("Bal", "Balance"), null);

        this.addSubCommand(new MoneyPay());
        this.addSubCommand(new MoneyGive());
        this.addSubCommand(new MoneyRemove());
        this.addSubCommand(new MoneyShow());
    }
}
