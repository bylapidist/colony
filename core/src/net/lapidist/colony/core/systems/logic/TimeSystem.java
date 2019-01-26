package net.lapidist.colony.core.systems.logic;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.core.events.Events;
import net.lapidist.colony.core.events.time.SeasonChangeEvent;
import net.lapidist.colony.core.events.time.TimeChangeEvent;

@Wire
public class TimeSystem extends BaseSystem {

    private int day = 1;
    private Season currentSeason = Season.SPRING;
    private TimeOfDay currentTime = TimeOfDay.DAWN;
    private int currentTimeTick = 0;
    private float dayLengthMultiplier = 0.01f;
    private int seasonLengthSpring = 5;
    private int seasonLengthSummer = 5;
    private int seasonLengthAutumn = 5;
    private int seasonLengthWinter = 5;
    private int seasonStart = 0;

    private float getDayDuration() {
        return currentTime.getDuration(currentSeason)
                * dayLengthMultiplier;
    }

    private void nextDay() {
        if (currentTime.ordinal() != TimeOfDay.values().length - 1) {
            currentTime = TimeOfDay.values()[currentTime.ordinal() + 1];
        } else {
            currentTime = TimeOfDay.values()[0];
        }

        runTimeChange();
    }

    private void incrementDay() {
        ++day;
    }

    private void incrementSeason() {
        switch (currentSeason) {
            case WINTER:
                currentSeason = Season.SPRING;
                break;
            case SPRING:
                currentSeason = Season.SUMMER;
                break;
            case SUMMER:
                currentSeason = Season.AUTUMN;
                break;
            case AUTUMN:
                currentSeason = Season.WINTER;
        }

        seasonStart = day;
        currentTimeTick = 0;
    }

    private void runTimeChange() {
        incrementDay();
        Events.fire(new TimeChangeEvent(currentTime));

        int seasonLength = 0;
        switch (Season.values()[currentSeason.ordinal()]) {
            case WINTER:
                seasonLength = seasonLengthWinter;
                break;
            case SPRING:
                seasonLength = seasonLengthSpring;
                break;
            case SUMMER:
                seasonLength = seasonLengthSummer;
                break;
            case AUTUMN:
                seasonLength = seasonLengthAutumn;
        }

        if (day >= seasonStart + seasonLength) {
            incrementSeason();
            Events.fire(new SeasonChangeEvent(currentSeason));
        }

        currentTimeTick = 0;
    }

    @Override
    protected void processSystem() {
        ++currentTimeTick;

        if (currentTimeTick >= getDayDuration()) {
            nextDay();
        }
    }

    public int getDay() {
        return day;
    }

    public Season getCurrentSeason() {
        return currentSeason;
    }

    public TimeOfDay getCurrentTime() {
        return currentTime;
    }

    public int getYearByDay(int day) {
        return day / (seasonLengthSpring + seasonLengthSummer + seasonLengthAutumn + seasonLengthWinter) + 1;
    }

    public int getYear() {
        return day / (seasonLengthSpring + seasonLengthSummer + seasonLengthAutumn + seasonLengthWinter) + 1;
    }

    public enum Season {
        WINTER("Winter"),
        SPRING("Spring"),
        SUMMER("Summer"),
        AUTUMN("Autumn");

        private final String seasonText;

        Season(String seasonText) {
            this.seasonText = seasonText;
        }

        @Override
        public String toString() {
            return this.seasonText;
        }
    }

    public enum TimeOfDay {
        NIGHT("Night", 36000, 30000, 36000, 42000),
        DAWN("Dawn", 10000, 10000, 10000, 10000),
        MORNING("Morning", 20000, 20000, 20000, 20000),
        MIDDAY("Midday", 36000, 42000, 36000, 30000),
        EVENING("Evening", 20000, 20000, 20000, 20000),
        DUSK("Dusk", 10000, 10000, 10000, 10000);

        private final String timeOfDayText;
        private final int durationSpring;
        private final int durationSummer;
        private final int durationAutumn;
        private final int durationWinter;

        private final Color colorSpring = new Color(1, 1, 1, 1);
        private final Color colorSummer = new Color(1, 1, 1, 1);
        private final Color colorAutumn = new Color(1, 1, 1, 1);
        private final Color colorWinter = new Color(1, 1, 1, 1);

        private final Color ambientLightNight = new Color(0.1f, 0.1f, 0.1f, 0.1f);
        private final Color ambientLightDawn = new Color(0.1f, 0.1f, 0.1f, 0.3f);
        private final Color ambientLightMorning = new Color(0.1f, 0.1f, 0.1f, 0.5f);
        private final Color ambientLightMidday = new Color(0.1f, 0.1f, 0.1f, 0.7f);
        private final Color ambientLightEvening = new Color(0.1f, 0.1f, 0.1f, 0.5f);
        private final Color ambientLightDusk = new Color(0.1f, 0.1f, 0.1f, 0.2f);

        TimeOfDay(String timeOfDayText, int durationSpring, int durationSummer, int durationAutumn, int durationWinter) {
            this.timeOfDayText = timeOfDayText;
            this.durationSpring = durationSpring;
            this.durationSummer = durationSummer;
            this.durationAutumn = durationAutumn;
            this.durationWinter = durationWinter;
        }

        public int getDuration(Season season) {
            switch(Season.values()[season.ordinal()]) {
                case WINTER:
                    return durationWinter;
                case SPRING:
                    return durationSpring;
                case SUMMER:
                    return durationSummer;
                case AUTUMN:
                    return durationAutumn;
                default:
                    return durationSpring;
            }
        }

        public Color getColor(Season season) {
            switch(Season.values()[season.ordinal()]) {
                case WINTER:
                    return colorWinter;
                case SPRING:
                    return colorSpring;
                case SUMMER:
                    return colorSummer;
                case AUTUMN:
                    return colorAutumn;
                default:
                    return colorSpring;
            }
        }

        public Color getAmbientLight(TimeOfDay timeOfDay) {
            switch(TimeOfDay.values()[timeOfDay.ordinal()]) {
                case NIGHT:
                    return ambientLightNight;
                case DAWN:
                    return ambientLightDawn;
                case MORNING:
                    return ambientLightMorning;
                case MIDDAY:
                    return ambientLightMidday;
                case EVENING:
                    return ambientLightEvening;
                case DUSK:
                    return ambientLightDusk;
                default:
                    return colorSpring;
            }
        }

        @Override
        public String toString() {
            return this.timeOfDayText;
        }
    }
}
