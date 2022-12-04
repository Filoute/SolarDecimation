package unnaincompris.LunaZ.Manager.PlayerData.data;

import com.google.gson.annotations.Expose;
import lombok.Getter;

public class Stats {
    @Expose @Getter private int speed = 1;
    @Expose @Getter private int dexterity = 1;
    @Expose @Getter private int defence = 1;
    @Expose @Getter private int resistance = 1;
    @Expose @Getter private int melee = 1;

    @Expose @Getter private double radioactivity = 0; // on 100.00
}
