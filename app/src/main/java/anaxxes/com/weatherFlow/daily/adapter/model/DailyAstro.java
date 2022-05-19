package anaxxes.com.weatherFlow.daily.adapter.model;

import anaxxes.com.weatherFlow.basic.model.weather.Astro;
import anaxxes.com.weatherFlow.basic.model.weather.MoonPhase;
import anaxxes.com.weatherFlow.daily.adapter.DailyWeatherAdapter;

public class DailyAstro implements DailyWeatherAdapter.ViewModel {

    private Astro sun;
    private Astro moon;
    private MoonPhase moonPhase;

    public DailyAstro(Astro sun, Astro moon, MoonPhase moonPhase) {
        this.sun = sun;
        this.moon = moon;
        this.moonPhase = moonPhase;
    }

    public Astro getSun() {
        return sun;
    }

    public void setSun(Astro sun) {
        this.sun = sun;
    }

    public Astro getMoon() {
        return moon;
    }

    public void setMoon(Astro moon) {
        this.moon = moon;
    }

    public MoonPhase getMoonPhase() {
        return moonPhase;
    }

    public void setMoonPhase(MoonPhase moonPhase) {
        this.moonPhase = moonPhase;
    }

    public static boolean isCode(int code) {
        return code == 7;
    }

    @Override
    public int getCode() {
        return 7;
    }
}
