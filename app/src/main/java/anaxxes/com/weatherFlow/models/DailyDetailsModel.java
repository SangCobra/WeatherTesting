package anaxxes.com.weatherFlow.models;

public class DailyDetailsModel {

    private int imageId;
    private String dailyDetailsHeading;

    public Boolean getVisible() {
        return isVisible;
    }

    public void setVisible(Boolean visible) {
        isVisible = visible;
    }

    private Boolean isVisible;

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDailyDetailsHeading() {
        return dailyDetailsHeading;
    }

    public void setDailyDetailsHeading(String dailyDetailsHeading) {
        this.dailyDetailsHeading = dailyDetailsHeading;
    }

    public String getDailyDetailsValue() {
        return dailyDetailsValue;
    }

    public void setDailyDetailsValue(String dailyDetailsValue) {
        this.dailyDetailsValue = dailyDetailsValue;
    }

    private String dailyDetailsValue;

    public DailyDetailsModel(int imageId, String dailyDetailsHeading, String dailyDetailsValue,Boolean isVisible) {
        this.imageId = imageId;
        this.dailyDetailsHeading = dailyDetailsHeading;
        this.dailyDetailsValue = dailyDetailsValue;
        this.isVisible = isVisible;
    }
}
