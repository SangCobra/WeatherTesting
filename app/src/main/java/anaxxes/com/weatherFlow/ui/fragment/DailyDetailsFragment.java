package anaxxes.com.weatherFlow.ui.fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.common.control.manager.AdmobManager;

import anaxxes.com.weatherFlow.R;
import anaxxes.com.weatherFlow.databinding.FragmentDailyDetailsBinding;
import anaxxes.com.weatherFlow.utils.manager.AdIdUtils;

public class DailyDetailsFragment extends Fragment {

    private FragmentDailyDetailsBinding binding;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentDailyDetailsBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }
}