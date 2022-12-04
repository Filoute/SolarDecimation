package unnaincompris.LunaZ.utils.Time;

import unnaincompris.LunaZ.utils.StringUtils;

public class Timer {
    private static long durationNSStatic = 0;
    public static void resetTimer(boolean logMessage) {
        if(logMessage) {
            Time stats = getStats();
            StringUtils.info("Reset Timer after "
                    + (stats.getHours() > 0 ? stats.getHours() + "h " : "")
                    + (stats.getMinute() > 0 ? stats.getMinute() + "min " : "")
                    + (stats.getSecond() > 0 ? stats.getSecond() + "s " : "")
                    + (stats.getMillisecond() > 0 ? stats.getMillisecond() + "ms " : "")
                    + (stats.getNanosecond() > 0 ? stats.getNanosecond() + "ns " : ""));
        }
        durationNSStatic = System.nanoTime();
    }

    public static String getBeautyStats() {
        Time stats = getStats();
        return (stats.getHours() > 0 ? stats.getHours() + "h " : "")
                + (stats.getMinute() > 0 ? stats.getMinute() + "min " : "")
                + (stats.getSecond() > 0 ? stats.getSecond() + "s " : "")
                + (stats.getMillisecond() > 0 ? stats.getMillisecond() + "ms " : "")
                + (stats.getNanosecond() > 0 ? stats.getNanosecond() + "ns " : "");
    }
    private static Time getStats() {
        return new Time(System.nanoTime() - durationNSStatic);
    }

    private long durationMS = 0;
    public void resetTimer() {
        durationMS = System.currentTimeMillis();
    }
    public boolean hasTimePassed(long timeInMS){
        return System.currentTimeMillis() - durationMS >= timeInMS;
    }
}
