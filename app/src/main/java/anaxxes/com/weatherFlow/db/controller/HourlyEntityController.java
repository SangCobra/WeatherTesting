package anaxxes.com.weatherFlow.db.controller;

import androidx.annotation.NonNull;

import java.util.List;

import anaxxes.com.weatherFlow.basic.model.option.provider.WeatherSource;
import anaxxes.com.weatherFlow.db.entity.DaoSession;
import anaxxes.com.weatherFlow.db.entity.HourlyEntity;
import anaxxes.com.weatherFlow.db.entity.HourlyEntityDao;
import anaxxes.com.weatherFlow.db.propertyConverter.WeatherSourceConverter;

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
