package anaxxes.com.weatherFlow.utils;

import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Size;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;

import java.util.Calendar;
import java.util.Objects;
import java.util.TimeZone;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.basic.model.location.Location;
import anaxxes.com.weatherFlow.basic.model.weather.Daily;
import anaxxes.com.weatherFlow.basic.model.weather.Weather;
import anaxxes.com.weatherFlow.main.MainActivity;
import anaxxes.com.weatherFlow.resource.ResourceHelper;
import anaxxes.com.weatherFlow.resource.provider.ResourceProvider;
import anaxxes.com.weatherFlow.ui.widget.astro.SunMoonView;

public class SunMoonUtils {

    @Size(2)
    private float[] startTimes;
    @Size(2)
    private float[] endTimes;
    @Size(2)
    private float[] currentTimes;
    @Size(2)
    private float[] animCurrentTimes;
    @Size(3)
    private AnimatorSet[] attachAnimatorSets;
    private ResourceProvider provider;
    private SunMoonView sunMoonView;
    private Location location;

    public SunMoonUtils(SunMoonView sunMoonView, Location location, ResourceProvider provider,Boolean isNight) {
        this.sunMoonView = sunMoonView;
        this.location = location;
        this.provider = provider;

//        ensureTime(loc,1,location.getTimeZone());

        Calendar cal = Calendar.getInstance();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (isNight) {
            this.sunMoonView.setColors(
                    ContextCompat.getColor(sunMoonView.getContext(), R.color.colorActualBlack),
                    ColorUtils.setAlphaComponent(ContextCompat.getColor(sunMoonView.getContext(), R.color.colorActualBlack), (int) (0.66 * 255)),
                    ColorUtils.setAlphaComponent(ContextCompat.getColor(sunMoonView.getContext(), R.color.colorActualBlack), (int) (0.33 * 255)),
                    ColorUtils.setAlphaComponent(ContextCompat.getColor(sunMoonView.getContext(), R.color.colorActualBlack), (int) (0.33 * 255)),
                    true, true
            );
        } else {
            this.sunMoonView.setColors(
                    ContextCompat.getColor(sunMoonView.getContext(), R.color.colorYellow),
                    ColorUtils.setAlphaComponent(ContextCompat.getColor(sunMoonView.getContext(), R.color.colorActualBlack), (int) (0.66 * 255)),
                    ColorUtils.setAlphaComponent(ContextCompat.getColor(sunMoonView.getContext(), R.color.colorYellow), (int) (0.33 * 255)),
                    ColorUtils.setAlphaComponent(ContextCompat.getColor(sunMoonView.getContext(), R.color.colorYellow), (int) (0.33 * 255)),
                    true, false
            );
        }


    }

    public void setMoonDrawable() {
        this.sunMoonView.setMoonDrawable(ResourceHelper.getMoonDrawable(provider));
    }

    public void setSunDrawable() {
        this.sunMoonView.setSunDrawable(ResourceHelper.getSunDrawable(provider));
    }


    @SuppressLint("Range")
    public void ensureTime(Daily today, Daily tomorrow, TimeZone timeZone) {

//        Daily today = location.getWeather().getDailyForecast().get(todayIndex);
//        Daily tomorrow = location.getWeather().getDailyForecast().get(tommorrowIndex);
//        TimeZone timeZone = location.getTimeZone();
        Calendar calendar = Calendar.getInstance();
        if (timeZone != null) {
            calendar.setTimeZone(timeZone);
        }
        int currentTime = SunMoonView.decodeTime(calendar);
        calendar.setTimeZone(TimeZone.getDefault());

        calendar.setTime(Objects.requireNonNull(today.sun().getRiseDate()));
        int sunriseTime = SunMoonView.decodeTime(calendar);

        calendar.setTime(Objects.requireNonNull(today.sun().getSetDate()));
        int sunsetTime = SunMoonView.decodeTime(calendar);

        startTimes = new float[2];
        endTimes = new float[2];
        currentTimes = new float[]{currentTime, currentTime};
        attachAnimatorSets = new AnimatorSet[2];



        // sun.
        startTimes[0] = sunriseTime;
        endTimes[0] = sunsetTime;

        // moon.
        if (!today.moon().isValid() || !tomorrow.moon().isValid()) {
            // do not have moonrise and moonset data.
            if (currentTime < sunriseTime) {
                // predawn. --> moon move from [sunset of yesterday] to [sunrise of today].
                calendar.setTime(Objects.requireNonNull(
                        today.sun().getSetDate()
                ));
                startTimes[1] = SunMoonView.decodeTime(calendar) - 24 * 60;
                endTimes[1] = sunriseTime;
            } else {
                // moon move from [sunset of today] to [sunrise of tomorrow]
                calendar.setTime(Objects.requireNonNull(
                        today.sun().getSetDate()
                ));
                startTimes[1] = SunMoonView.decodeTime(calendar);

                calendar.setTime(Objects.requireNonNull(
                        tomorrow.sun().getRiseDate()
                ));
                endTimes[1] = SunMoonView.decodeTime(calendar) + 24 * 60;
            }
        } else {
            // have moonrise and moonset data.
            if (currentTime < sunriseTime) {
                // predawn. --> moon move from [moonrise of yesterday] to [moonset of today].
                calendar.setTime(Objects.requireNonNull(
                        today.moon().getRiseDate()
                ));
                startTimes[1] = SunMoonView.decodeTime(calendar) - 24 * 60;

                calendar.setTime(Objects.requireNonNull(
                        today.moon().getSetDate()
                ));
                endTimes[1] = SunMoonView.decodeTime(calendar);
                if (endTimes[1] < startTimes[1]) {
                    endTimes[1] += 24 * 60;
                }
            } else {
                // moon move from [moonrise of today] to [moonset of tomorrow].
                calendar.setTime(Objects.requireNonNull(
                        today.moon().getRiseDate()
                ));
                startTimes[1] = SunMoonView.decodeTime(calendar);

                calendar.setTime(Objects.requireNonNull(
                        tomorrow.moon().getSetDate()
                ));
                endTimes[1] = SunMoonView.decodeTime(calendar);
                if (endTimes[1] < startTimes[1]) {
                    endTimes[1] += 24 * 60;
                }
            }
        }

        animCurrentTimes = new float[]{currentTimes[0], currentTimes[1]};


        this.sunMoonView.setTime(startTimes, endTimes, animCurrentTimes);
        this.sunMoonView.setDayIndicatorRotation(0);
        this.sunMoonView.setNightIndicatorRotation(0);
    }

    public void onEnterScreen() {
        try {
            ValueAnimator timeDay = ValueAnimator.ofObject(new FloatEvaluator(), startTimes[0], currentTimes[0]);
            timeDay.addUpdateListener(animation -> {
                animCurrentTimes[0] = (Float) animation.getAnimatedValue();
                sunMoonView.setTime(startTimes, endTimes, animCurrentTimes);
            });

            double totalRotationDay = 360.0 * 7 * (currentTimes[0] - startTimes[0]) / (endTimes[0] - startTimes[0]);
            ValueAnimator rotateDay = ValueAnimator.ofObject(
                    new FloatEvaluator(), 0, (int) (totalRotationDay - totalRotationDay % 360)
            );
            rotateDay.addUpdateListener(animation ->
                    sunMoonView.setDayIndicatorRotation((Float) animation.getAnimatedValue())
            );

            attachAnimatorSets[0] = new AnimatorSet();
            attachAnimatorSets[0].playTogether(timeDay, rotateDay);
            attachAnimatorSets[0].setInterpolator(new OvershootInterpolator(1f));
            attachAnimatorSets[0].setDuration(getPathAnimatorDuration(0));
            attachAnimatorSets[0].start();

            ValueAnimator timeNight = ValueAnimator.ofObject(new FloatEvaluator(), startTimes[1], currentTimes[1]);
            timeNight.addUpdateListener(animation -> {
                animCurrentTimes[1] = (Float) animation.getAnimatedValue();
                sunMoonView.setTime(startTimes, endTimes, animCurrentTimes);
            });

            double totalRotationNight = 360.0 * 4 * (currentTimes[1] - startTimes[1]) / (endTimes[1] - startTimes[1]);
            ValueAnimator rotateNight = ValueAnimator.ofObject(
                    new FloatEvaluator(), 0, (int) (totalRotationNight - totalRotationNight % 360)
            );
            rotateNight.addUpdateListener(animation ->
                    sunMoonView.setNightIndicatorRotation(-1 * (Float) animation.getAnimatedValue())
            );

            attachAnimatorSets[1] = new AnimatorSet();
            attachAnimatorSets[1].playTogether(timeNight, rotateNight);
            attachAnimatorSets[1].setInterpolator(new OvershootInterpolator(1f));
            attachAnimatorSets[1].setDuration(getPathAnimatorDuration(1));
            attachAnimatorSets[1].start();

//            if (phaseAngle > 0) {
//                ValueAnimator moonAngle = ValueAnimator.ofObject(new FloatEvaluator(), 0, phaseAngle);
//                moonAngle.addUpdateListener(animation ->
//                        phaseView.setSurfaceAngle((Float) animation.getAnimatedValue())
//                );
//
//                attachAnimatorSets[2] = new AnimatorSet();
//                attachAnimatorSets[2].playTogether(moonAngle);
//                attachAnimatorSets[2].setInterpolator(new DecelerateInterpolator());
//                attachAnimatorSets[2].setDuration(getPhaseAnimatorDuration());
//                attachAnimatorSets[2].start();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private long getPathAnimatorDuration(int index) {
        long duration = (long) Math.max(
                1000 + 3000.0
                        * (currentTimes[index] - startTimes[index])
                        / (endTimes[index] - startTimes[index]),
                0
        );
        return Math.min(duration, 4000);
    }
}
