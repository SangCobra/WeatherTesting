
package mtgtech.com.weather_forecast.weather_forecast.json.accu;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Datum {

    @SerializedName("mold_level")
    @Expose
    private Double moldLevel;
    @SerializedName("aqi")
    @Expose
    private Double aqi;
    @SerializedName("pm10")
    @Expose
    private Double pm10;
    @SerializedName("co")
    @Expose
    private Double co;
    @SerializedName("o3")
    @Expose
    private Double o3;
    @SerializedName("predominant_pollen_type")
    @Expose
    private String predominantPollenType;
    @SerializedName("so2")
    @Expose
    private Double so2;
    @SerializedName("pollen_level_tree")
    @Expose
    private Double pollenLevelTree;
    @SerializedName("pollen_level_weed")
    @Expose
    private Double pollenLevelWeed;
    @SerializedName("no2")
    @Expose
    private Double no2;
    @SerializedName("pm25")
    @Expose
    private Double pm25;
    @SerializedName("pollen_level_grass")
    @Expose
    private Double pollenLevelGrass;

    public Double getMoldLevel() {
        return moldLevel;
    }

    public void setMoldLevel(Double moldLevel) {
        this.moldLevel = moldLevel;
    }

    public Double getAqi() {
        return aqi;
    }

    public void setAqi(Double aqi) {
        this.aqi = aqi;
    }

    public Double getPm10() {
        return pm10;
    }

    public void setPm10(Double pm10) {
        this.pm10 = pm10;
    }

    public Double getCo() {
        return co;
    }

    public void setCo(Double co) {
        this.co = co;
    }

    public Double getO3() {
        return o3;
    }

    public void setO3(Double o3) {
        this.o3 = o3;
    }

    public String getPredominantPollenType() {
        return predominantPollenType;
    }

    public void setPredominantPollenType(String predominantPollenType) {
        this.predominantPollenType = predominantPollenType;
    }

    public Double getSo2() {
        return so2;
    }

    public void setSo2(Double so2) {
        this.so2 = so2;
    }

    public Double getPollenLevelTree() {
        return pollenLevelTree;
    }

    public void setPollenLevelTree(Double pollenLevelTree) {
        this.pollenLevelTree = pollenLevelTree;
    }

    public Double getPollenLevelWeed() {
        return pollenLevelWeed;
    }

    public void setPollenLevelWeed(Double pollenLevelWeed) {
        this.pollenLevelWeed = pollenLevelWeed;
    }

    public Double getNo2() {
        return no2;
    }

    public void setNo2(Double no2) {
        this.no2 = no2;
    }

    public Double getPm25() {
        return pm25;
    }

    public void setPm25(Double pm25) {
        this.pm25 = pm25;
    }

    public Double getPollenLevelGrass() {
        return pollenLevelGrass;
    }

    public void setPollenLevelGrass(Double pollenLevelGrass) {
        this.pollenLevelGrass = pollenLevelGrass;
    }

}
