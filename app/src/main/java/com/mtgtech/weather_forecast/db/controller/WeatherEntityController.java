package com.mtgtech.weather_forecast.db.controller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import com.mtgtech.weather_forecast.db.entity.DaoSession;
import com.mtgtech.weather_forecast.db.entity.WeatherEntity;
import com.mtgtech.weather_forecast.db.entity.WeatherEntityDao;
import com.mtgtech.weather_forecast.db.propertyConverter.WeatherSourceConverter;
import com.mtgtech.weather_forecast.weather_model.model.option.provider.WeatherSource;

public class WeatherEntityController extends AbsEntityController<WeatherEntity> {

    public WeatherEntityController(DaoSession session) {
        super(session);
    }

    // insert.

    public void insertWeatherEntity(@NonNull String cityId, @NonNull WeatherSource source,
                                    @NonNull WeatherEntity entity) {
        deleteWeather(cityId, source);
        getSession().getWeatherEntityDao().insert(entity);
        getSession().clear();
    }

    // delete.

    public void deleteWeather(@NonNull String cityId, @NonNull WeatherSource source) {
        getSession().getWeatherEntityDao().deleteInTx(selectWeatherEntityList(cityId, source));
        getSession().clear();
    }

    // select.

    @Nullable
    public WeatherEntity selectWeatherEntity(@NonNull String cityId, @NonNull WeatherSource source) {
        List<WeatherEntity> entityList = selectWeatherEntityList(cityId, source);
        if (entityList.size() <= 0) {
            return null;
        } else {
            return entityList.get(0);
        }
    }

    @NonNull
    private List<WeatherEntity> selectWeatherEntityList(@NonNull String cityId, @NonNull WeatherSource source) {
        return getNonNullList(
                getSession().getWeatherEntityDao().queryBuilder()
                        .where(
                                WeatherEntityDao.Properties.CityId.eq(cityId),
                                WeatherEntityDao.Properties.WeatherSource.eq(
                                        new WeatherSourceConverter().convertToDatabaseValue(source)
                                )
                        ).list()
        );
    }
}
