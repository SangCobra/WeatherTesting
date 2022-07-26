package mtgtech.com.weather_forecast.settings.model;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import mtgtech.com.weather_forecast.R;

/**
 * About app link.
 */

public class AboutAppLink {

    public static final String LINK_ALIPAY = "ALIPAY";
    public static final String LINK_WECHAT = "WECHAT";
    public int iconRes;
    public String title;
    public String url;
    public boolean email;

    private AboutAppLink(int iconRes, String title, String url, boolean email) {
        this.iconRes = iconRes;
        this.title = title;
        this.url = url;
        this.email = email;
    }

    public static List<AboutAppLink> buildLinkList(Context context) {
        List<AboutAppLink> list = new ArrayList<>(2);

        list.add(new AboutAppLink(
                R.drawable.ic_github,
                context.getString(R.string.gitHub),
                "https://github.com/WangDaYeeeeee/GeometricWeather",
                false
        ));
        list.add(new AboutAppLink(
                R.drawable.ic_email,
                context.getString(R.string.email),
                "mailto:wangdayeeeeee@gmail.com",
                true
        ));

        return list;
    }

    public static List<AboutAppLink> buildDonateLinkList(Context context) {
        List<AboutAppLink> list = new ArrayList<>(2);

        list.add(new AboutAppLink(
                R.drawable.ic_alipay,
                context.getString(R.string.alipay),
                LINK_ALIPAY,
                false
        ));
        list.add(new AboutAppLink(
                R.drawable.ic_wechat_pay,
                context.getString(R.string.wechat),
                LINK_WECHAT,
                false
        ));

        return list;
    }
}
