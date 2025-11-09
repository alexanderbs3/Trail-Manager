package com.example.trailmanager.models;

import android.content.Context;
import android.content.SharedPreferences;

public class UserConfig {
    private static final String PREFS_NAME = "TrailManagerPrefs";
    private static final String KEY_WEIGHT = "weight";
    private static final String KEY_HEIGHT = "height";
    private static final String KEY_GENDER = "gender";
    private static final String KEY_BIRTHDATE = "birthdate";
    private static final String KEY_MAP_TYPE = "map_type";
    private static final String KEY_NAVIGATION_MODE = "navigation_mode";

    public static final String GENDER_MALE = "M";
    public static final String GENDER_FEMALE = "F";

    public static final int MAP_TYPE_NORMAL = 1;
    public static final int MAP_TYPE_SATELLITE = 2;

    public static final String NAV_MODE_NORTH_UP = "north_up";
    public static final String NAV_MODE_COURSE_UP = "course_up";

    private SharedPreferences prefs;

    public UserConfig(Context context) {
        this.prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }

    // Weight (kg)
    public void setWeight(float weight) {
        prefs.edit().putFloat(KEY_WEIGHT, weight).apply();
    }

    public float getWeight() {
        return prefs.getFloat(KEY_WEIGHT, 70.0f);
    }

    // Height (cm)
    public void setHeight(float height) {
        prefs.edit().putFloat(KEY_HEIGHT, height).apply();
    }

    public float getHeight() {
        return prefs.getFloat(KEY_HEIGHT, 170.0f);
    }

    // Gender
    public void setGender(String gender) {
        prefs.edit().putString(KEY_GENDER, gender).apply();
    }

    public String getGender() {
        return prefs.getString(KEY_GENDER, GENDER_MALE);
    }

    // Birthdate (timestamp)
    public void setBirthdate(long timestamp) {
        prefs.edit().putLong(KEY_BIRTHDATE, timestamp).apply();
    }

    public long getBirthdate() {
        return prefs.getLong(KEY_BIRTHDATE, 0);
    }

    // Map Type
    public void setMapType(int mapType) {
        prefs.edit().putInt(KEY_MAP_TYPE, mapType).apply();
    }

    public int getMapType() {
        return prefs.getInt(KEY_MAP_TYPE, MAP_TYPE_NORMAL);
    }

    // Navigation Mode
    public void setNavigationMode(String navMode) {
        prefs.edit().putString(KEY_NAVIGATION_MODE, navMode).apply();
    }

    public String getNavigationMode() {
        return prefs.getString(KEY_NAVIGATION_MODE, NAV_MODE_NORTH_UP);
    }

    // Calcular idade
    public int getAge() {
        long birthdate = getBirthdate();
        if (birthdate == 0) return 30; // Idade padrão

        long now = System.currentTimeMillis();
        long diff = now - birthdate;
        return (int) (diff / (1000L * 60 * 60 * 24 * 365));
    }

    // Verificar se as configurações foram preenchidas
    public boolean isConfigured() {
        return getWeight() > 0 && getHeight() > 0 && getBirthdate() != 0;
    }
}
