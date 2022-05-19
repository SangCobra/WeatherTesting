package anaxxes.com.weatherFlow.db.controller;

import androidx.annotation.NonNull;

import java.util.List;

import anaxxes.com.weatherFlow.basic.model.option.provider.WeatherSource;
import anaxxes.com.weatherFlow.db.entity.DailyEntity;
import anaxxes.com.weatherFlow.db.entity.DailyEntityDao;
import anaxxes.com.weatherFlow.db.entity.DaoSession;
import anaxxes.com.weatherFlow.db.propertyConverter.WeatherSourceConverter;

public class DailyEntityController extends AbsEntityController<DailyEntity> {
    
    public DailyEntityController(DaoSession session) {
        super(session);
    }

    // insert.

    public void insertDailyList(@NonNull String cityId, @NonNull WeatherSource source,
                                @NonNull List<DailyEntity> entityList) {
        deleteDailyEntityList(cityId, source);
        getSession().getDailyEntityDao().insertInTx(entityList);
        getSession().clear();
    }

    // delete.

    public void deleteDailyEntityList( @NonNull String cityId, @NonNull WeatherSource source) {
        getSession().getDailyEntityDao().deleteInTx(selectDailyEntityList(cityId, source));
        getSession().clear();
    }

    // select.

    @NonNull
    public List<DailyEntity> selectDailyEntityList( @NonNull String cityId, @NonNull WeatherSource source) {
        return getNonNullList(
                getSession().getDailyEntityDao()
                        .queryBuilder()
                        .where(
                                DailyEntityDao.Properties.CityId.eq(cityId),
                                DailyEntityDao.Properties.WeatherSource.eq(
                                        new WeatherSourceConverter().convertToDatabaseValue(source)
                                )
                        ).list()
        );
    }
}
