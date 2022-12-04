package unnaincompris.LunaZ.Manager.Language.commands;

import unnaincompris.LunaZ.Commands.Manager.SubCommandExecutor;

import java.util.Collections;

public class LanguageExecutor extends SubCommandExecutor {

    public LanguageExecutor() {
        super("Language", Collections.singletonList("Lang"), null);

        this.addSubCommand(new EditCommand());
    }
}
