package com.example.hodofiles.ui.settings;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.hodofiles.R;

public class SettingsFragment extends Fragment {
    private SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        Button button = view.findViewById(R.id.button_interests);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InterestFragment interestFragment = new InterestFragment();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment_container, interestFragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //Get the current preference
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(requireContext());

        //Handle current theme with switch syncing
        SwitchCompat themeSwitch = view.findViewById(R.id.switch_theme);
        boolean isDarkMode = sharedPreferences.getBoolean("dark_mode", false);
        themeSwitch.setChecked(isDarkMode);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                saveThemePreference(true);
                requireActivity().recreate();
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                saveThemePreference(false);
                requireActivity().recreate();
            }
        });


    }

    // Save the theme preference in SharedPreferences
    private void saveThemePreference(boolean isDarkMode) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("dark_mode", isDarkMode);
        editor.apply();
    }


}