
package anaxxes.com.weatherFlow.weather.json.accu.search;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Country {

    @SerializedName("ID")
    @Expose
    private String id;
    @SerializedName("LocalizedName")
    @Expose
    private String localizedName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocalizedName() {
        return localizedName;
    }

    public void setLocalizedName(String localizedName) {
        this.localizedName = localizedName;
    }

}
