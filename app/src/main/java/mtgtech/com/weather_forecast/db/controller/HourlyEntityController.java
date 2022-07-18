package mtgtech.com.weather_forecast.db.controller;

import androidx.annotation.NonNull;

import java.util.List;

import mtgtech.com.weather_forecast.weather_model.model.option.provider.WeatherSource;
import mtgtech.com.weather_forecast.db.entity.DaoSession;
import mtgtech.com.weather_forecast.db.entity.HourlyEntity;
import mtgtech.com.weather_forecast.db.entity.HourlyEntityDao;
import mtgtech.com.weather_forecast.db.propertyConverter.WeatherSourceConverter;

public class HourlyEntityController extends AbsEntityController<HourlyEntity> {
    
    public HourlyEntityController(DaoSession session) {
        super(session);
    }

    // insert.

    public void insertHourlyList(@NonNull String cityId, @NonNull WeatherSource source,
                                 @NonNull List<HourlyEntity> entityList) {
        deleteHourlyEntityList(cityId, source);
        getSession().getHourlyEntityDao().insertInTx(entityList);
        getSession().clear();
    }

    // delete.

    public void deleteHourlyEntityList(@NonNull String cityId, @NonNull WeatherSource source) {
        getSession().getHourlyEntityDao().deleteInTx(selectHourlyEntityList(cityId, source));
        getSession().clear();
    }

    // select.

    public List<HourlyEntity> selectHourlyEntityList(@NonNull String cityId, @NonNull WeatherSource source) {
        return getNonNullList(
                getSession().getHourlyEntityDao()
                        .queryBuilder()
                        .where(
                                HourlyEntityDao.Properties.CityId.eq(cityId),
                                HourlyEntityDao.Properties.WeatherSource.eq(
                                        new WeatherSourceConverter().convertToDatabaseValue(source)
                                )
                        ).list()
        );
    }
}
