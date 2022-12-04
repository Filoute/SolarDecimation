package unnaincompris.LunaZ.utils.Time;

import lombok.Getter;

import java.text.SimpleDateFormat;

public class Time {
    public static SimpleDateFormat fullDate = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    @Getter public int hours, minute, second;
    @Getter public long millisecond, nanosecond;

    public Time(int hours, int minute, int second, long millisecond, long nanosecond) {
        this.hours = hours;
        this.minute = minute;
        this.second = second;
        this.millisecond = millisecond;
        this.nanosecond = nanosecond;
        setGoodTime();
    }

    public Time(int minute, int second, long millisecond, long nanosecond) {
        this(0, minute, second, millisecond, nanosecond);
    }

    public Time(int second, long millisecond, long nanosecond) {
        this(0, second, millisecond, nanosecond);
    }

    public Time(long millisecond, long nanosecond) {
        this(0, millisecond, nanosecond);
    }

    public Time(long nanosecond) {
        this(0, nanosecond);
    }

    public boolean setGoodTime() {
        boolean changed = false;
        while (nanosecond >= 1000000) {
            nanosecond -= 1000000;
            millisecond++;
            changed = true;
        }
        while (millisecond >= 1000) {
            millisecond -= 1000;
            second++;
            changed = true;
        }
        while (second >= 60) {
            second -= 60;
            minute++;
            changed = true;
        }
        while (minute >= 60) {
            minute -= 60;
            hours++;
            changed = true;
        }
        return changed;
    }

    public String getBeautyStats() {
        return (hours > 0 ? hours + "h " : "")
                + (minute > 0 ? minute + "min " : "")
                + (second > 0 ? second + "s " : "")
                + (millisecond > 0 ? millisecond + "ms " : "")
                + (nanosecond > 0 ? nanosecond + "ns " : "");
    }
}
