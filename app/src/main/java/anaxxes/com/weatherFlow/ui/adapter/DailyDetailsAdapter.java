package anaxxes.com.weatherFlow.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import anaxxes.com.weatherFlow.databinding.ItemDailyDetailsBinding;
import anaxxes.com.weatherFlow.models.DailyDetailsModel;

public class DailyDetailsAdapter extends RecyclerView.Adapter<DailyDetailsAdapter.ViewHolder> {

    private ArrayList<DailyDetailsModel> list;
    private Context context;

    public DailyDetailsAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(ItemDailyDetailsBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.setData(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ItemDailyDetailsBinding binding;

        public ViewHolder(ItemDailyDetailsBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        public void setData(DailyDetailsModel model) {
            if(model.getVisible()){
                binding.getRoot().setVisibility(View.VISIBLE);
                binding.tvDailyDetails.setText(model.getDailyDetailsHeading());
                binding.tvDailyValue.setText(model.getDailyDetailsValue());
               binding.imgDailyDetails.setImageDrawable(ContextCompat.getDrawable(context,model.getImageId()));
            }else{
                binding.getRoot().setVisibility(View.GONE);
            }

        }
    }

    public void updateData(ArrayList<DailyDetailsModel> newList) {
        list = newList;
        notifyDataSetChanged();
    }
}
