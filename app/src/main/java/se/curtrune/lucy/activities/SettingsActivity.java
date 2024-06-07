package se.curtrune.lucy.activities;

import static se.curtrune.lucy.util.Logger.log;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.os.LocaleListCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

import se.curtrune.lucy.R;
import se.curtrune.lucy.workers.SettingsWorker;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.settings, new SettingsFragment())
                    .commit();
        }
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public static class SettingsFragment extends PreferenceFragmentCompat {
        private SharedPreferences.OnSharedPreferenceChangeListener sharedPreferenceChangeListener = new SharedPreferences.OnSharedPreferenceChangeListener() {
            @Override
            public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, @Nullable String key) {
                log("...shared preference changed", key);
                Preference preference = findPreference(key);
                String summary = (String) preference.getSummary();
                log("...summary", summary);
                if( key.equals("pref_current_language")){
                    log("...will change language to english, maybe");
                    String languageCode = summary.equals("svenska") ? "sv": "en";
                    LocaleListCompat localeListCompat = LocaleListCompat.forLanguageTags(languageCode);
                    AppCompatDelegate.setApplicationLocales(localeListCompat);
                }else if( key.equals("pref_panic_action")){
                    log("pref_panic_action");
                }else if( key.equals("pref_dark_mode")){
                    log("key pref_dark_mode");
                    SettingsWorker.toggleDarkMode(requireContext());
                }else{
                    log("no action for key", key);
                }
            }
        };
        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            log("SettingsFragment.onCreatedPreferences(Bundle, String)");
            setPreferencesFromResource(R.xml.root_preferences, rootKey);
            SharedPreferences sharedPreferences = getPreferenceScreen().getSharedPreferences();
            sharedPreferences.registerOnSharedPreferenceChangeListener(sharedPreferenceChangeListener);
        }
    }
}