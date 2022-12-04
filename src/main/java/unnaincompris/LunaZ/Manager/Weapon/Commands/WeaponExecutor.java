package unnaincompris.LunaZ.Manager.Weapon.Commands;

import unnaincompris.LunaZ.Commands.Manager.SubCommandExecutor;

public class WeaponExecutor extends SubCommandExecutor {
    public WeaponExecutor() {
        super("Weapon", null);

        this.addSubCommand(new ListWeapon());
        this.addSubCommand(new CreateWeapon());
    }
}
