package anaxxes.com.weatherFlow.db.controller;

import androidx.annotation.NonNull;

import java.util.List;

import anaxxes.com.weatherFlow.basic.model.option.provider.WeatherSource;
import anaxxes.com.weatherFlow.db.entity.DaoSession;
import anaxxes.com.weatherFlow.db.entity.MinutelyEntity;
import anaxxes.com.weatherFlow.db.entity.MinutelyEntityDao;
import anaxxes.com.weatherFlow.db.propertyConverter.WeatherSourceConverter;

public class MinutelyEntityController extends AbsEntityController<MinutelyEntity> {
    
    public MinutelyEntityController(DaoSession session) {
        super(session);
    }

    // insert.

    public void insertMinutelyList(@NonNull String cityId, @NonNull WeatherSource source,
                                   @NonNull List<MinutelyEntity> entityList) {
        deleteMinutelyEntityList(cityId, source);
        getSession().getMinutelyEntityDao().insertInTx(entityList);
        getSession().clear();
    }

    // delete.

    public void deleteMinutelyEntityList(@NonNull String cityId, @NonNull WeatherSource source) {
        getSession().getMinutelyEntityDao().deleteInTx(selectMinutelyEntityList(cityId, source));
        getSession().clear();
    }

    // select.

    public List<MinutelyEntity> selectMinutelyEntityList(@NonNull String cityId, @NonNull WeatherSource source) {
        return getNonNullList(
                getSession().getMinutelyEntityDao()
                        .queryBuilder()
                        .where(
                                MinutelyEntityDao.Properties.CityId.eq(cityId),
                                MinutelyEntityDao.Properties.WeatherSource.eq(
                                        new WeatherSourceConverter().convertToDatabaseValue(source)
                                )
                        ).list()
        );
    }
}
