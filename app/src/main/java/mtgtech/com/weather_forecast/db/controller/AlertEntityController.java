package mtgtech.com.weather_forecast.db.controller;

import androidx.annotation.NonNull;

import java.util.List;

import mtgtech.com.weather_forecast.db.entity.AlertEntity;
import mtgtech.com.weather_forecast.db.entity.AlertEntityDao;
import mtgtech.com.weather_forecast.db.entity.DaoSession;
import mtgtech.com.weather_forecast.db.propertyConverter.WeatherSourceConverter;
import mtgtech.com.weather_forecast.weather_model.model.option.provider.WeatherSource;

public class AlertEntityController extends AbsEntityController<AlertEntity> {

    public AlertEntityController(DaoSession session) {
        super(session);
    }

    // insert.

    public void insertAlertList(@NonNull String cityId, @NonNull WeatherSource source,
                                @NonNull List<AlertEntity> entityList) {
        deleteAlertList(cityId, source);
        getSession().getAlertEntityDao().insertInTx(entityList);
        getSession().clear();
    }

    // delete.

    public void deleteAlertList(@NonNull String cityId, @NonNull WeatherSource source) {
        getSession().getAlertEntityDao().deleteInTx(searchLocationAlarmEntity(cityId, source));
        getSession().clear();
    }

    // search.

    public List<AlertEntity> searchLocationAlarmEntity(@NonNull String cityId, @NonNull WeatherSource source) {
        return getNonNullList(
                getSession().getAlertEntityDao()
                        .queryBuilder()
                        .where(
                                AlertEntityDao.Properties.CityId.eq(cityId),
                                AlertEntityDao.Properties.WeatherSource.eq(
                                        new WeatherSourceConverter().convertToDatabaseValue(source)
                                )
                        ).list()
        );
    }
}
