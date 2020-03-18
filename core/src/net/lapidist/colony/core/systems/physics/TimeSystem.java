package net.lapidist.colony.core.systems.physics;

import com.artemis.BaseSystem;
import com.artemis.annotations.Wire;
import com.badlogic.gdx.ai.msg.MessageManager;
import com.badlogic.gdx.graphics.Color;
import net.lapidist.colony.core.events.Events;

@Wire
public class TimeSystem extends BaseSystem {

    private static final float DAY_LENGTH_MULTIPLIER = 0.1f;
    private static final int SEASON_LENGTH_SPRING = 5;
    private static final int SEASON_LENGTH_SUMMER = 5;
    private static final int SEASON_LENGTH_AUTUMN = 5;
    private static final int SEASON_LENGTH_WINTER = 5;

    private int day = 1;
    private int seasonStart = 0;
    private int currentTimeTick = 0;
    private Season currentSeason = Season.SPRING;
    private TimeOfDay currentTime = TimeOfDay.DAWN;

    private float getDayDuration() {
        return currentTime.getDuration(currentSeason)
                * DAY_LENGTH_MULTIPLIER;
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
        MessageManager.getInstance().dispatchMessage(
                0,
                Events.TIME_CHANGE,
                getCurrentTime()
        );
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
            default:
        }

        seasonStart = day;
        currentTimeTick = 0;
        MessageManager.getInstance().dispatchMessage(
                0,
                Events.SEASON_CHANGE,
                getCurrentSeason()
        );
    }

    private void runTimeChange() {
        incrementDay();

        int seasonLength = 0;
        switch (Season.values()[currentSeason.ordinal()]) {
            case WINTER:
                seasonLength = SEASON_LENGTH_WINTER;
                break;
            case SPRING:
                seasonLength = SEASON_LENGTH_SPRING;
                break;
            case SUMMER:
                seasonLength = SEASON_LENGTH_SUMMER;
                break;
            case AUTUMN:
                seasonLength = SEASON_LENGTH_AUTUMN;
            default:
        }

        if (day >= seasonStart + seasonLength) {
            incrementSeason();
        }

        currentTimeTick = 0;
    }

    @Override
    protected final void processSystem() {
        ++currentTimeTick;

        if (currentTimeTick >= getDayDuration()) {
            nextDay();
        }
    }

    public final int getDay() {
        return day;
    }

    public final Season getCurrentSeason() {
        return currentSeason;
    }

    public final TimeOfDay getCurrentTime() {
        return currentTime;
    }

    public final int getYearByDay(final int dayToGet) {
        return dayToGet
                / (SEASON_LENGTH_SPRING
                + SEASON_LENGTH_SUMMER
                + SEASON_LENGTH_AUTUMN
                + SEASON_LENGTH_WINTER) + 1;
    }

    public final int getYear() {
        return day
                / (SEASON_LENGTH_SPRING
                + SEASON_LENGTH_SUMMER
                + SEASON_LENGTH_AUTUMN
                + SEASON_LENGTH_WINTER) + 1;
    }

    public enum Season {
        WINTER("Winter"),
        SPRING("Spring"),
        SUMMER("Summer"),
        AUTUMN("Autumn");

        private final String seasonText;

        Season(final String seasonTextToSet) {
            this.seasonText = seasonTextToSet;
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

        private final Color colorSpring = new Color(
                0.9f, 0.9f, 0.9f, 1
        );
        private final Color colorSummer = new Color(
                1f, 1f, 1f, 1
        );
        private final Color colorAutumn = new Color(
                1f, 0.5f, 0.5f, 1
        );
        private final Color colorWinter = new Color(
                0.3f, 0.3f, 1f, 1
        );

        private final Color ambientLightNight = new Color(
                0.1f, 0.1f, 0.1f, 0.1f
        );
        private final Color ambientLightDawn = new Color(
                0.1f, 0.1f, 0.1f, 0.3f
        );
        private final Color ambientLightMorning = new Color(
                0.1f, 0.1f, 0.1f, 0.5f
        );
        private final Color ambientLightMidday = new Color(
                0.1f, 0.1f, 0.1f, 0.7f
        );
        private final Color ambientLightEvening = new Color(
                0.1f, 0.1f, 0.1f, 0.5f
        );
        private final Color ambientLightDusk = new Color(
                0.1f, 0.1f, 0.1f, 0.2f
        );

        TimeOfDay(
                final String timeOfDayTextToSet,
                final int durationSpringToSet,
                final int durationSummerToSet,
                final int durationAutumnToSet,
                final int durationWinterToSet
        ) {
            this.timeOfDayText = timeOfDayTextToSet;
            this.durationSpring = durationSpringToSet;
            this.durationSummer = durationSummerToSet;
            this.durationAutumn = durationAutumnToSet;
            this.durationWinter = durationWinterToSet;
        }

        public int getDuration(final Season season) {
            switch (Season.values()[season.ordinal()]) {
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

        public Color getColor(final Season season) {
            switch (Season.values()[season.ordinal()]) {
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

        public Color getAmbientLight(final TimeOfDay timeOfDay) {
            switch (TimeOfDay.values()[timeOfDay.ordinal()]) {
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
