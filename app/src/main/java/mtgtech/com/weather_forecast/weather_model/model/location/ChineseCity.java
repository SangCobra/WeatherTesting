package mtgtech.com.weather_forecast.weather_model.model.location;

/**
 * Chinese city.
 */

public class ChineseCity {

    /**
     * cityId : 101010100
     * province : 北京
     * city : 北京
     * district : 北京
     * latitude : 39.904987
     * longitude : 116.40529
     */

    private String cityId;
    private String province;
    private String city;
    private String district;
    private String latitude;
    private String longitude;

    public ChineseCity(String cityId,
                       String province, String city, String district,
                       String latitude, String longitude) {
        this.cityId = cityId;
        this.province = province;
        this.city = city;
        this.district = district;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
