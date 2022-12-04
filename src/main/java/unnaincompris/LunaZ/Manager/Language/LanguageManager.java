package unnaincompris.LunaZ.Manager.Language;

import lombok.Getter;
import unnaincompris.LunaZ.Manager.Language.commands.LanguageExecutor;
import unnaincompris.LunaZ.utils.Config;
import unnaincompris.LunaZ.utils.JsonUtils;
import unnaincompris.LunaZ.utils.ManagerEnabler;
import unnaincompris.LunaZ.utils.StringUtils;
import unnaincompris.LunaZ.utils.Time.Timer;

public class LanguageManager implements ManagerEnabler {

    @Getter private static Language language;

    public LanguageManager() {
        setup();
        new LanguageExecutor();
    }

    public void setup() {
        Timer.resetTimer(false);
        language = JsonUtils.getData(JsonUtils.getOrCreateJson(Config.LANGUAGE_FOLDER, "language_" + Config.LANG), Language.class);
        if(language == null) language = new Language();
        StringUtils.info("Setuping language in " + Timer.getBeautyStats());
    }

    public void saveLanguage() {
        Timer.resetTimer(false);
        JsonUtils.writeData(JsonUtils.getOrCreateJson(Config.LANGUAGE_FOLDER, "language_" + Config.LANG), language);
        StringUtils.info("Saving language in " + Timer.getBeautyStats());
    }

    public void setDefault(String langName) {
        JsonUtils.writeData(JsonUtils.getOrCreateJson(Config.LANGUAGE_FOLDER, "language_" + langName), new Language());
    }

    public void modifyLanguage(String to) {
        saveLanguage();
        Config.setLANG(to.toUpperCase());
        setup();
    }

    public void createLanguage(String name, boolean setLang) {
        saveLanguage();
        if(name.length() > 8) {
            name = name.substring(0, 8);
        }
        JsonUtils.getOrCreateJson(Config.LANGUAGE_FOLDER, "language_" + name);
        setDefault(name);
        if(setLang) {
            Config.setLANG(name);
            setup();
        }
    }
}
