package anaxxes.com.weatherFlow.main;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.common.control.manager.AdmobManager;

import java.util.ArrayList;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.main.adapter.AriQualityAdapter;
import anaxxes.com.weatherFlow.models.AirQualityModel;
import anaxxes.com.weatherFlow.utils.manager.AdIdUtils;

public class AirQualityActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private ImageView backHome;
    private AriQualityAdapter adapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ari_quality);
        initView();
        AdmobManager.getInstance().loadBanner(this, AdIdUtils.idBanner);
    }
    private void initView(){
        recyclerView = findViewById(R.id.rcvAirQuality);
        backHome = findViewById(R.id.backToHome);
        ArrayList<AirQualityModel> listAdapter = new ArrayList<>();
        int[] listColor = {
                R.color.colorLevel_1,
                R.color.colorLevel_2,
                R.color.colorLevel_3,
                R.color.colorLevel_4,
                R.color.colorLevel_5,
                R.color.colorLevel_6
        };
        String[] listAdjust = {
                "Good",
                "Moderate",
                "Unhealthy for Sensitive Groups",
                "Unhealthy",
                "Very Unhealthy",
                "Hazardous"
        };
        String[] listNumber = {
                "0-50",
                "51-100",
                "101-150",
                "151-200",
                "201-300",
                "301+"
        };
        for (int i = 0; i < 6; i++){
            listAdapter.add(new AirQualityModel(listColor[i], listAdjust[i], listNumber[i]));
        }
        adapter = new AriQualityAdapter(this, listAdapter);
        backHome.setOnClickListener(v -> finish());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

    }
}
