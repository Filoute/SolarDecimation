package unnaincompris.LunaZ.Manager.Language;

import com.google.gson.annotations.Expose;
import unnaincompris.LunaZ.Manager.Language.Categories.*;

public class Language {

    @Editable @Expose @InventoryItem public String PREVIOUS_PAGE = "&cPrevious Pages";
    @Editable @Expose @InventoryItem public String NEXT_PAGE = "&aNext Pages";
    @Editable @Expose @InventoryItem public String ACCEPT = "&aAccept";
    @Editable @Expose @InventoryItem public String DENIED = "&cDenied";
    @Editable @Expose @InventoryItem public String BACK = "&cBack";

    @Editable @Expose @Other public String CANCEL_KEYWORD = "cancel";
    @Editable @Expose @Other public String CONFIRM_KEYWORD = "confirm";
    @Editable @Expose @Other public String SUCCESSFULLY_DELETE_FILE = "&7You successfully &cdelete &6{file_name}!";
    @Editable @Expose @Other public String NEED_TO_PUT_ARGUMENT = "&7You need to put an {argument_number} argument! {hint}";
    @Editable @Expose @Other public String INVALID_ARGUMENT = "&7Argument {argument} are invalid! {hint}";

    @Editable @Expose @Weapon public String WEAPON_ALREADY_EXIST = "&7The weapon &c{weapon} &7already exist";
    @Editable @Expose @Weapon public String WEAPON_CREATED = "&7The weapon {weapon} have been &acreated!";
    @Editable @Expose @Weapon public String WEAPON_RELOADING = "&7Reloading&c{progress}!";
    @Editable @Expose @Weapon public String WEAPON_DISPLAY_MAGAZINE = "&7{current}/{max}";
    @Editable @Expose @Weapon public String WEAPON_STOP_RELOADING = "&7Stopping the reload process";

    @Editable @Expose @Magazine public String MAGAZINE_RELOADING = "&7Reloading &c{progress}!";
    @Editable @Expose @Magazine public String MAGAZINE_STOP_RELOADING = "&7Stopping the reload process";

    @Editable @Expose @Consumable public String CONSUMABLE_USE = "&7Using &6{consumable_name}{progress}!";
    @Editable @Expose @Consumable public String CONSUMABLE_CREATED = "&7The consumable {consumable} have been &acreated!";
    @Editable @Expose @Consumable public String CONSUMABLE_ALREADY_EXIST = "&7The consumable &c{consumable} &7already exist";
    @Editable @Expose @Consumable public String CONSUMABLE_ABORT_CONSUME = "&7The consume of &c{consumable} &7have been aborted because you was not holding it";

    @Editable @Expose @FactionName public String RADIOACTIVE = "Radioactive";
    @Editable @Expose @FactionName public String MILITARY = "Military";
    @Editable @Expose @FactionName public String DISTRICT_1 = "TOWN";
    @Editable @Expose @FactionName public String DISTRICT_2 = "TOWN";
    @Editable @Expose @FactionName public String DISTRICT_3 = "TOWN";

    /* MONEY */
    @Editable @Expose @Money public String DONT_HAVE_ENOUGH_MONEY = "&7You don't have enough money you need {need} more";
    @Editable @Expose @Money public String RECEIVE_MONEY_FROM_PLAYER = "&7You receive {received}$ from {sender}. You have {have}$ now.";
    @Editable @Expose @Money public String RECEIVE_MONEY = "&7You receive {received}$. You have {have}$ now.";
    @Editable @Expose @Money public String LOSE_MONEY = "&7You lose {used}$. You have {have}$ now";
    @Editable @Expose @Money public String HAVE_MONEY = "&7You currently have {have}$.";
    @Editable @Expose @Money public String PLAYER_HAVE_MONEY = "&7{player} currently have {have}$.";
    @Editable @Expose @Money public String SEND_MONEY = "&7You send {sent}$ to {player}.";
    @Editable @Expose @Money public String CANNOT_PAY_IT_SELF_MONEY = "&7You can't pay you dumb bitch.";
    /* LUNA */
    @Editable @Expose @Money public String DONT_HAVE_ENOUGH_LUNA = "&7You don't have enough Luna you need {need} more";
    @Editable @Expose @Money public String RECEIVE_LUNA_FROM_PLAYER = "&7You receive {received} luna from {sender}. You have {have} luna now.";
    @Editable @Expose @Money public String RECEIVE_LUNA = "&7You receive {received} money. You have {have} money now.";
    @Editable @Expose @Money public String LOSE_LUNA = "&7You lose {used} luna. You have {have} Luna now";
    @Editable @Expose @Money public String HAVE_LUNA = "&7You currently have {have} luna.";
    @Editable @Expose @Money public String PLAYER_HAVE_LUNA = "&7{player} currently have {have} luna.";
    @Editable @Expose @Money public String SEND_LUNA = "&7You send {sent} luna to {player}.";
    @Editable @Expose @Money public String CANNOT_PAY_IT_SELF_LUNA = "&7You can't pay you dumb bitch.";

    @Expose public String ASK_STRING = "&7Type the string in the chat or type &c{cancel} &7 to cancel.";
    @Expose public String ASK_INTEGER = "&7Type the integer in the chat or type &c{cancel} &7 to cancel.";
    @Expose public String NEW_VALUE_TEXT = "&7Specify a new text for &6{complement}&7.";
    @Expose public String CANNOT_DELETE_CURRENT_LANG = "&7You can't delete current language";
    @Expose public String CONFIRMATION_TO_DELETE = "&7Are you sure you want to delete &6{toDelete} &7?";
    @Expose public String LANGUAGE_ALREADY_EXIST = "&7The language &6{lang} &7already exist";
    @Expose public String AVAILABLE_PLACEHOLDERS = "&7Available placeholders:";
    @Expose public String DEFAULT = "&7Default:";

}
