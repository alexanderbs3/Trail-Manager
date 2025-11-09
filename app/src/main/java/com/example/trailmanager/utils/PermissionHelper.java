
package com.example.trailmanager.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Classe utilitária para gerenciar permissões do aplicativo
 */
public class PermissionHelper {

    public static final int LOCATION_PERMISSION_REQUEST_CODE = 100;
    public static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 101;
    public static final int ALL_PERMISSIONS_REQUEST_CODE = 102;

    /**
     * Verifica se todas as permissões de localização estão concedidas
     */
    public static boolean hasLocationPermissions(Context context) {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Verifica se a permissão de notificação está concedida (Android 13+)
     */
    public static boolean hasNotificationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context,
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true; // Em versões anteriores, não é necessário
    }

    /**
     * Verifica se todas as permissões necessárias estão concedidas
     */
    public static boolean hasAllPermissions(Context context) {
        return hasLocationPermissions(context) && hasNotificationPermission(context);
    }

    /**
     * Solicita permissões de localização
     */
    public static void requestLocationPermissions(Activity activity) {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        ActivityCompat.requestPermissions(activity, permissions,
                LOCATION_PERMISSION_REQUEST_CODE);
    }

    /**
     * Solicita permissão de notificação (Android 13+)
     */
    public static void requestNotificationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            String[] permissions = {
                    Manifest.permission.POST_NOTIFICATIONS
            };

            ActivityCompat.requestPermissions(activity, permissions,
                    NOTIFICATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * Solicita todas as permissões necessárias de uma vez
     */
    public static void requestAllPermissions(Activity activity) {
        List<String> permissionsList = new ArrayList<>();

        // Permissões de localização
        if (!hasLocationPermissions(activity)) {
            permissionsList.add(Manifest.permission.ACCESS_FINE_LOCATION);
            permissionsList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }

        // Permissão de notificação (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (!hasNotificationPermission(activity)) {
                permissionsList.add(Manifest.permission.POST_NOTIFICATIONS);
            }
        }

        if (!permissionsList.isEmpty()) {
            String[] permissions = permissionsList.toArray(new String[0]);
            ActivityCompat.requestPermissions(activity, permissions,
                    ALL_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Verifica se o usuário negou permanentemente as permissões
     */
    public static boolean shouldShowLocationPermissionRationale(Activity activity) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.ACCESS_FINE_LOCATION) ||
                ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    /**
     * Verifica se todas as permissões foram concedidas a partir do resultado
     */
    public static boolean verifyPermissions(int[] grantResults) {
        if (grantResults.length == 0) {
            return false;
        }

        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }

        return true;
    }

    /**
     * Retorna array com permissões necessárias para a versão do Android
     */
    public static String[] getRequiredPermissions() {
        List<String> permissions = new ArrayList<>();

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS);
        }

        return permissions.toArray(new String[0]);
    }

    /**
     * Verifica se uma permissão específica está concedida
     */
    public static boolean isPermissionGranted(Context context, String permission) {
        return ContextCompat.checkSelfPermission(context, permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Verifica se o app tem permissão para executar em background
     */
    public static boolean hasBackgroundLocationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_BACKGROUND_LOCATION)
                    == PackageManager.PERMISSION_GRANTED;
        }
        return true; // Em versões anteriores, não é necessário
    }

    /**
     * Solicita permissão de localização em background (Android 10+)
     */
    public static void requestBackgroundLocationPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!hasBackgroundLocationPermission(activity)) {
                String[] permissions = {
                        Manifest.permission.ACCESS_BACKGROUND_LOCATION
                };

                ActivityCompat.requestPermissions(activity, permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
    }
}