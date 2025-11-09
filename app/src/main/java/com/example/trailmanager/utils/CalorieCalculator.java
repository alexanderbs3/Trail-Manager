package com.example.trailmanager.utils;

import com.example.trailmanager.models.UserConfig;

public class CalorieCalculator {

    public static double calculateCalories(float weight, double durationMinutes,
                                           double avgSpeedKmh, String gender) {
        double met = getMETFromSpeed(avgSpeedKmh);
        double durationHours = durationMinutes / 60.0;
        double calories = met * weight * durationHours;

        if (gender.equals(UserConfig.GENDER_FEMALE)) {
            calories *= 0.95;
        }

        return calories;
    }

    public static double calculateInstantCalories(float weight, long durationSeconds,
                                                  double currentSpeedKmh, String gender) {
        double durationMinutes = durationSeconds / 60.0;
        return calculateCalories(weight, durationMinutes, currentSpeedKmh, gender);
    }

    public static double calculateCaloriesByDistance(float weight, double distanceKm,
                                                     double avgSpeedKmh, String gender) {
        if (avgSpeedKmh <= 0) return 0;

        double timeHours = distanceKm / avgSpeedKmh;
        double timeMinutes = timeHours * 60;

        return calculateCalories(weight, timeMinutes, avgSpeedKmh, gender);
    }

    private static double getMETFromSpeed(double speedKmh) {
        if (speedKmh < 2.0) return 2.0;
        else if (speedKmh < 4.0) return 2.5;
        else if (speedKmh < 5.0) return 3.5;
        else if (speedKmh < 6.5) return 4.3;
        else if (speedKmh < 8.0) return 5.0;
        else if (speedKmh < 10.0) return 8.0;
        else if (speedKmh < 12.0) return 10.0;
        else if (speedKmh < 14.0) return 12.0;
        else return 14.0;
    }

    public static String getIntensityDescription(double speedKmh) {
        if (speedKmh < 2.0) return "Parado";
        else if (speedKmh < 4.0) return "Caminhada Lenta";
        else if (speedKmh < 5.0) return "Caminhada Moderada";
        else if (speedKmh < 6.5) return "Caminhada Rápida";
        else if (speedKmh < 8.0) return "Caminhada Muito Rápida";
        else if (speedKmh < 10.0) return "Corrida Leve";
        else if (speedKmh < 12.0) return "Corrida Moderada";
        else return "Corrida Intensa";
    }

    public static double getCaloriesPerMinute(float weight, double speedKmh, String gender) {
        double met = getMETFromSpeed(speedKmh);
        double caloriesPerHour = met * weight;
        double caloriesPerMinute = caloriesPerHour / 60.0;

        if (gender.equals(UserConfig.GENDER_FEMALE)) {
            caloriesPerMinute *= 0.95;
        }

        return caloriesPerMinute;
    }
}
