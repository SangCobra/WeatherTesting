package mtgtech.com.weather_forecast.view.weather_widget.astro;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PathEffect;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Size;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.ColorUtils;
import androidx.core.view.ViewCompat;

import java.util.Calendar;

import mtgtech.com.weather_forecast.R;
import mtgtech.com.weather_forecast.utils.DisplayUtils;
import mtgtech.com.weather_forecast.view.weather_widget.DayNightShaderWrapper;

public class SunMoonView extends View {

    private final static float ICON_SIZE_DIP = 24;
    private final static float LINE_SIZE_DIP = 3.5f;
    private final static float DOTTED_LINE_SIZE_DIP = 1;
    private final static float MARGIN_DIP = 16;
    private static final int ARC_ANGLE = 135;
    private static final float SHADOW_ALPHA_FACTOR_LIGHT = 0.1f;
    private static final float SHADOW_ALPHA_FACTOR_DARK = 0.2f;
    int iconSize;
    // 0 - day / 1 - night.
    @Size(2)
    private Drawable[] iconDrawables;
    private Paint paint;
    private Xfermode clearXfermode;
    private DayNightShaderWrapper x1ShaderWrapper;
    private DayNightShaderWrapper x2ShaderWrapper;
    private PathEffect effect;
    private RectF rectF;
    private Boolean isNight = false;
    private Boolean isDrawn = false;
    @Size(2)
    private float[] iconRotations;
    @Size(2)
    private float[] iconAlphas;
    @Size(2)
    private float[][] iconPositions;
    @Size(2)
    private float[] startTimes;
    @Size(2)
    private float[] currentTimes;
    @Size(2)
    private float[] endTimes;
    @Size(2)
    private float[] progresses;
    @Size(2)
    private float[] maxes;
    @Size(3)
    private int[] lineColors;
    @Size(2)
    private int[] x1ShaderColors;
    @Size(2)
    private int[] x2ShaderColors;
    @ColorInt
    private int rootColor;
    private float lineSize;
    private float dottedLineSize;
    private float margin;

    public SunMoonView(@NonNull Context context) {
        super(context);
        this.initialize();
    }

    public SunMoonView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.initialize();
    }

    public SunMoonView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.initialize();
    }

    public static int decodeTime(String time) {
        String[] t = time.split(":");
        return decodeTime(Integer.parseInt(t[0]), Integer.parseInt(t[1]));
    }

    public static int decodeTime(Calendar calendar) {
        return decodeTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
    }

    public static int decodeTime(int hour, int minute) {
        return hour * 60 + minute;
    }

    private void initialize() {
        this.iconDrawables = new Drawable[2];

        this.iconRotations = new float[]{0, 0};
        this.iconAlphas = new float[]{0, 0};
        this.iconPositions = new float[][]{{0, 0}, {0, 0}};

        this.startTimes = new float[]{decodeTime("6:00"), decodeTime("18:00")};
        this.endTimes = new float[]{decodeTime("18:00"), decodeTime("6:00") + 24 * 60};
        this.currentTimes = new float[]{decodeTime("12:00"), decodeTime("12:00")};
        this.progresses = new float[]{-1, -1};
        this.maxes = new float[]{100, 100};

        this.lineColors = new int[]{Color.BLACK, Color.GRAY, Color.LTGRAY};

        this.x1ShaderColors = new int[]{Color.GRAY, Color.WHITE};
        this.x2ShaderColors = new int[]{Color.BLACK, Color.WHITE};
        this.rootColor = Color.WHITE;

        lineSize = DisplayUtils.dpToPx(getContext(), LINE_SIZE_DIP);
        dottedLineSize = DisplayUtils.dpToPx(getContext(), DOTTED_LINE_SIZE_DIP);
        margin = DisplayUtils.dpToPx(getContext(), MARGIN_DIP);

        iconSize = (int) DisplayUtils.dpToPx(getContext(), ICON_SIZE_DIP);

        this.paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);

        this.clearXfermode = new PorterDuffXfermode(PorterDuff.Mode.CLEAR);
        this.x1ShaderWrapper = new DayNightShaderWrapper(getMeasuredWidth(), getMeasuredHeight());
        this.x2ShaderWrapper = new DayNightShaderWrapper(getMeasuredWidth(), getMeasuredHeight());

        this.effect = new DashPathEffect(
                new float[]{
                        DisplayUtils.dpToPx(getContext(), 3),
                        2 * DisplayUtils.dpToPx(getContext(), 3)
                }, 0
        );
        this.rectF = new RectF();
    }

    public void setTime(@Size(2) float[] startTimes, @Size(2) float[] endTimes, @Size(2) float[] currentTimes) {
        this.startTimes = startTimes;
        this.endTimes = endTimes;
        this.currentTimes = currentTimes;

        setIndicatorPosition(0);
        setIndicatorPosition(1);

        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setColors(@ColorInt int sunLineColor, @ColorInt int moonLineColor,
                          @ColorInt int backgroundLineColor, @ColorInt int rootColor,
                          boolean lightTheme, boolean isNight) {

        this.isNight = isNight;
        this.lineColors = new int[]{sunLineColor, moonLineColor, backgroundLineColor};
        ensureShader(rootColor, sunLineColor, moonLineColor, lightTheme, true);
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setDayIndicatorRotation(float rotation) {
        iconRotations[0] = rotation;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    public void setNightIndicatorRotation(float rotation) {
        iconRotations[1] = rotation;
        ViewCompat.postInvalidateOnAnimation(this);
    }

    private void ensureShader(@ColorInt int rootColor,
                              @ColorInt int sunLineColor, @ColorInt int moonLineColor,
                              boolean lightTheme, boolean isNight) {
        int lineShadowShader = lightTheme
                ? ColorUtils.setAlphaComponent(sunLineColor, (int) (255 * SHADOW_ALPHA_FACTOR_LIGHT))
                : ColorUtils.setAlphaComponent(moonLineColor, (int) (255 * SHADOW_ALPHA_FACTOR_DARK));

        x1ShaderColors[0] = blendColor(lineShadowShader, rootColor);
        x1ShaderColors[1] = rootColor;

        x2ShaderColors[0] = blendColor(lineShadowShader, x1ShaderColors[0]);
        x2ShaderColors[1] = rootColor;

        this.rootColor = rootColor;


        if (x2ShaderWrapper.isDifferent(
                getMeasuredWidth(), getMeasuredHeight(), lightTheme, x2ShaderColors)) {
            x2ShaderWrapper.setShader(
                    new LinearGradient(
                            0, rectF.top,
                            0, getMeasuredHeight() - margin,
                            x2ShaderColors[0], x2ShaderColors[1],
                            Shader.TileMode.CLAMP
                    ),
                    getMeasuredWidth(), getMeasuredHeight(),
                    lightTheme,
                    x2ShaderColors
            );
        }

        if (x1ShaderWrapper.isDifferent(
                getMeasuredWidth(), getMeasuredHeight(), lightTheme, x1ShaderColors)) {
            x1ShaderWrapper.setShader(
                    new LinearGradient(
                            0, rectF.top,
                            0, getMeasuredHeight() - margin,
                            x1ShaderColors[0], x1ShaderColors[1],
                            Shader.TileMode.CLAMP
                    ),
                    getMeasuredWidth(), getMeasuredHeight(),
                    lightTheme,
                    x1ShaderColors
            );
        }


    }

    @ColorInt
    public int blendColor(@ColorInt int foreground, @ColorInt int background) {
        int scr = Color.red(foreground);
        int scg = Color.green(foreground);
        int scb = Color.blue(foreground);
        int sa = foreground >>> 24;
        int dcr = Color.red(background);
        int dcg = Color.green(background);
        int dcb = Color.blue(background);
        int color_r = dcr * (0xff - sa) / 0xff + scr * sa / 0xff;
        int color_g = dcg * (0xff - sa) / 0xff + scg * sa / 0xff;
        int color_b = dcb * (0xff - sa) / 0xff + scb * sa / 0xff;
        return ((color_r << 16) + (color_g << 8) + color_b) | (0xff000000);
    }

    private void ensureProgress(int index) {
        maxes[index] = endTimes[index] - startTimes[index];
        progresses[index] = currentTimes[index] - startTimes[index];
        progresses[index] = Math.max(progresses[index], 0);
        progresses[index] = Math.min(progresses[index], maxes[index]);
    }

    private void setIndicatorPosition(int index) {
        ensureProgress(index);
        float startAngle = 270 - ARC_ANGLE / 2f;
        float progressSweepAngle = (float) (1.0 * progresses[index] / maxes[index] * ARC_ANGLE);
        float progressEndAngle = startAngle + progressSweepAngle;
        float deltaAngle = progressEndAngle - 180;
        float deltaWidth = (float) Math.abs(rectF.width() / 2 * Math.cos(Math.toRadians(deltaAngle)));
        float deltaHeight = (float) Math.abs(rectF.width() / 2 * Math.sin(Math.toRadians(deltaAngle)));

        if (progressSweepAngle == 0 && iconAlphas[index] != 0) {
            iconAlphas[index] = 0;
        } else if (progressSweepAngle != 0 && iconAlphas[index] == 0) {
            iconAlphas[index] = 1;
        }

        if (iconDrawables[index] != null) {
            if (progressEndAngle < 270) {
                iconPositions[index][0] = rectF.centerX() - deltaWidth - iconSize / 2f;
                iconPositions[index][1] = rectF.centerY() - deltaHeight - iconSize / 2f;
            } else {
                iconPositions[index][0] = rectF.centerX() + deltaWidth - iconSize / 2f;
                iconPositions[index][1] = rectF.centerY() - deltaHeight - iconSize / 2f;
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = (int) (MeasureSpec.getSize(widthMeasureSpec) - 2 * margin);
        double deltaRadians = Math.toRadians((180 - ARC_ANGLE) / 2d);
        int radius = (int) (width / 2 / Math.cos(deltaRadians));
        int height = (int) (radius - width / 2 * Math.tan(deltaRadians));
        setMeasuredDimension(
                MeasureSpec.makeMeasureSpec((int) (width + 2 * margin), MeasureSpec.EXACTLY),
                MeasureSpec.makeMeasureSpec((int) (height + 2 * margin), MeasureSpec.EXACTLY)
        );

        int centerX = getMeasuredWidth() / 2;
        int centerY = (int) (margin + radius);
        rectF.set(
                centerX - radius,
                centerY - radius,
                centerX + radius,
                centerY + radius
        );

        ensureShader(rootColor, lineColors[0], lineColors[1], x1ShaderWrapper.isLightTheme(), false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // shadow.
        float startAngle = 270 - ARC_ANGLE / 2f;
        float progressSweepAngleDay = (float) (1.0 * progresses[0] / maxes[0] * ARC_ANGLE);
        float progressEndAngleDay = startAngle + progressSweepAngleDay;
        float progressSweepAngleNight = (float) (1.0 * progresses[1] / maxes[1] * ARC_ANGLE);
        float progressEndAngleNight = startAngle + progressSweepAngleNight;
        if (progressEndAngleDay == progressEndAngleNight) {
            drawShadow(canvas, 0, progressEndAngleDay, x2ShaderWrapper.getShader());
        } else if (progressEndAngleDay > progressEndAngleNight) {
            if (isNight) {
                drawShadow(canvas, 1, progressEndAngleNight, x2ShaderWrapper.getShader());

            } else {
                drawShadow(canvas, 0, progressEndAngleDay, x1ShaderWrapper.getShader());

            }
        } else { // progressEndAngleDay < progressEndAngleNight
            if (isNight) {
                drawShadow(canvas, 1, progressEndAngleNight, x1ShaderWrapper.getShader());
            } else {
                drawShadow(canvas, 0, progressEndAngleDay, x2ShaderWrapper.getShader());
            }
        }


        // sub line.
        paint.setColor(ContextCompat.getColor(getContext(), R.color.colorTextWhite));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(dottedLineSize);
        paint.setPathEffect(effect);
        canvas.drawArc(rectF, startAngle, ARC_ANGLE, false, paint);
        canvas.drawLine(
                margin,
                getMeasuredHeight() - margin,
                getMeasuredWidth() - margin,
                getMeasuredHeight() - margin,
                paint
        );

        // path.
        if (isNight) {
            drawPathLine(canvas, 1, startAngle, (float) (1.0 * progresses[1] / maxes[1] * ARC_ANGLE));
        } else {
            drawPathLine(canvas, 0, startAngle, (float) (1.0 * progresses[0] / maxes[0] * ARC_ANGLE));
        }


        int restoreCount;

        // icon.

        try {
            if (isNight) {

                restoreCount = canvas.save();

                canvas.translate(iconPositions[1][0], iconPositions[1][1]);
                canvas.rotate(iconRotations[1], iconSize / 2f, iconSize / 2f);
                iconDrawables[1].draw(canvas);
                canvas.restoreToCount(restoreCount);

            } else {
                restoreCount = canvas.save();

                canvas.translate(iconPositions[0][0], iconPositions[0][1]);
                canvas.rotate(iconRotations[0], iconSize / 2f, iconSize / 2f);
                iconDrawables[0].draw(canvas);
                canvas.restoreToCount(restoreCount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void drawShadow(Canvas canvas, int index, float progressEndAngle, Shader shader) {
        if (progresses[index] > 0) {
            int layerId = canvas.saveLayer(
                    rectF.left, rectF.top, rectF.right, rectF.top + rectF.height() / 2,
                    null, Canvas.ALL_SAVE_FLAG
            );

            paint.setStyle(Paint.Style.FILL);
            paint.setShader(shader);
            canvas.drawArc(
                    rectF,
                    270 - ARC_ANGLE / 2f,
                    ARC_ANGLE,
                    false,
                    paint
            );
            paint.setShader(null);

            paint.setXfermode(clearXfermode);
            canvas.drawRect(
                    (float) (
                            rectF.centerX() + rectF.width() / 2
                                    * Math.cos((360 - progressEndAngle) * Math.PI / 180)
                    ),
                    rectF.top,
                    rectF.right,
                    rectF.top + rectF.height() / 2,
                    paint
            );
            paint.setXfermode(null);
            canvas.restoreToCount(layerId);
        }
    }

    private void drawPathLine(Canvas canvas, int index, float startAngle, float progressSweepAngle) {
        if (progresses[index] > 0) {
            paint.setColor(lineColors[index]);
            paint.setStrokeWidth(lineSize);
            paint.setPathEffect(null);
            canvas.drawArc(rectF, startAngle, progressSweepAngle, false, paint);
        }
    }

    public void setSunDrawable(Drawable d) {
        if (d != null) {
            iconDrawables[0] = d;
            iconDrawables[0].setBounds(0, 0, iconSize, iconSize);
        }
    }

    public void setMoonDrawable(Drawable d) {
        if (d != null) {
            iconDrawables[1] = d;
            iconDrawables[1].setBounds(0, 0, iconSize, iconSize);
        }
    }
}