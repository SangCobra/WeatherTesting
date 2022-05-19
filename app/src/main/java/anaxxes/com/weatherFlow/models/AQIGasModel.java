package anaxxes.com.weatherFlow.models;

public class AQIGasModel {
    private String heading;

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    private String value;
    private int color;


    public AQIGasModel(String heading, String value, int color) {
        this.heading = heading;
        this.value = value;
        this.color = color;
    }
}
