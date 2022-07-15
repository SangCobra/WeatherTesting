package anaxxes.com.weatherFlow.ui.adapter.location;

import android.view.View;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.common.control.manager.AdmobManager;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.utils.manager.AdIdUtils;

public class AdHolder extends RecyclerView.ViewHolder {
    private FrameLayout frAd;
    public AdHolder(@NonNull View itemView) {
        super(itemView);
        frAd = itemView.findViewById(R.id.fr_ad_native);
        AdmobManager.getInstance().loadNative(itemView.getContext(), AdIdUtils.idNative, frAd);
    }
}
