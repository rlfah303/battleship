package edu.byuh.cis.cs203.bw_collisions_2019.ui;

import android.content.Context;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import edu.byuh.cis.cs203.bw_collisions_2019.R;


public class Prefs extends PreferenceActivity {
    public static final String MUSIC_PREF = "MUSIC_PREF";
    public static final String RAPID_FIRE_MISSILES_PREF = "RAPID_FIRE_MISSILES_PREF";
    public static final String RAPID_FIRE_DEPTH_CHARGES_PREF = "RAPID_FIRE_DEPTH_CHARGES_PREF";
    public static final String NUMBERS_PREF= "NUMBERS_PREF";
    public static final String SPEED_PREF = "SPEED_PREF";
    public static final String DIRECTION_PREF = "DIRECTION_PREF";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceScreen screen = getPreferenceManager().createPreferenceScreen(this);

        CheckBoxPreference music = new CheckBoxPreference(this);
        music.setTitle(R.string.music_set_title);
        music.setSummaryOn(R.string.music_on);
        music.setSummaryOff(R.string.music_off);
        music.setChecked(true);
        music.setKey(MUSIC_PREF);
        screen.addPreference(music);

        //todo Rapid Fire m Preference
        CheckBoxPreference rapidfire_m = new CheckBoxPreference(this);
        rapidfire_m.setTitle(R.string.rapid_fire_m);
        rapidfire_m.setSummaryOn(R.string.rapid_fire_m_on);
        rapidfire_m.setSummaryOff(R.string.rapid_fire_m_off);
        rapidfire_m.setChecked(true);
        rapidfire_m.setKey(RAPID_FIRE_MISSILES_PREF);
        screen.addPreference(rapidfire_m);

        //todo Rapid Fire d Preference
        CheckBoxPreference rapidfire_d = new CheckBoxPreference(this);
        rapidfire_d.setTitle(R.string.rapid_fire_d);
        rapidfire_d.setSummaryOn(R.string.rapid_fire_d_on);
        rapidfire_d.setSummaryOff(R.string.rapid_fire_d_off);
        rapidfire_d.setChecked(true);
        rapidfire_d.setKey(RAPID_FIRE_DEPTH_CHARGES_PREF);
        screen.addPreference(rapidfire_d);

        //todo Numbers Preference
        ListPreference number = new ListPreference(this);
        number.setTitle(R.string.enemy_number_title);
        number.setSummary(R.string.set_enemy_numbers);
        number.setKey(NUMBERS_PREF);
        String[] number_values = getResources().getStringArray(R.array.set_numbers_enemy);
        number.setEntries(number_values);
        number.setEntryValues(number_values);
        screen.addPreference(number);

        //todo Speed Preference
        ListPreference speed = new ListPreference(this);
        speed.setTitle(R.string.enemy_speed_title);
        speed.setSummary(R.string.set_enemy_speed);
        speed.setKey(SPEED_PREF);
        String[] speed_labels = getResources().getStringArray(R.array.speed_options);
        String[] speed_values = {"20", "5", "0"};
        speed.setEntries(speed_labels);
        speed.setEntryValues(speed_values);
        screen.addPreference(speed);

        //todo Direction Preference
        ListPreference direction = new ListPreference(this);
        direction.setTitle(R.string.enemy_direction_title);
        direction.setSummary(R.string.set_enemy_direction);
        direction.setKey(DIRECTION_PREF);
        String[] direction_placeholder = getResources().getStringArray(R.array.direction_options);
        String[] direction_values= {"LEFT_to_RIGHT", "RIGHT_to_LEFT","BOTH"};
        direction.setEntries(direction_placeholder);
        direction.setEntryValues(direction_values);
        screen.addPreference(direction);

        setPreferenceScreen(screen);

    }
    public static boolean getMusicPref(Context c) {
        return PreferenceManager.
                getDefaultSharedPreferences(c).getBoolean(MUSIC_PREF, true);
    }

    public static boolean getRapidFireMissilesPref(Context c) {
        return PreferenceManager.
                getDefaultSharedPreferences(c).getBoolean(RAPID_FIRE_MISSILES_PREF, true);
    }

    public static boolean getRapidFireDepthChargesPref(Context c) {
        return PreferenceManager.
                getDefaultSharedPreferences(c).getBoolean(RAPID_FIRE_DEPTH_CHARGES_PREF, true);
    }

    public static int getNumberPref(Context c) {
        String seth = PreferenceManager.
                getDefaultSharedPreferences(c).getString(NUMBERS_PREF, "6");
        return Integer.parseInt(seth);
    }

    public static int getSpeedPref(Context c) {
        String seth = PreferenceManager.
                getDefaultSharedPreferences(c).getString(SPEED_PREF, "5");
        return Integer.parseInt(seth);
    }

    public static String getDirectionPref(Context c) {
        String seth = PreferenceManager.
                getDefaultSharedPreferences(c).getString(DIRECTION_PREF, "BOTH");
        return seth;
    }
}
