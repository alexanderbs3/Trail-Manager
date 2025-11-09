package com.example.trailmanager.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.trailmanager.R;
import com.example.trailmanager.models.UserConfig;

public class MainActivity extends AppCompatActivity {
    //AIzaSyDcyD4FyRSR4bO9sKaZTnqXKNtsJltMxQI
    //AIzaSyCPttDyjnsASS3woFgFd7ok6j82tn1JFq4
    private static final int PERMISSION_REQUEST_CODE = 100;
    private Button btnConfig;
    private Button btnRegisterTrail;
    private Button btnViewTrails;
    private Button btnCredits;
    private UserConfig userConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userConfig = new UserConfig(this);

        initViews();
        setupListeners();
        checkPermissions();
    }

    private void initViews() {
        btnConfig = findViewById(R.id.btnConfig);
        btnRegisterTrail = findViewById(R.id.btnRegisterTrail);
        btnViewTrails = findViewById(R.id.btnViewTrails);
        btnCredits = findViewById(R.id.btnCredits);
    }

    private void setupListeners() {
        btnConfig.setOnClickListener(v -> openConfigActivity());

        btnRegisterTrail.setOnClickListener(v -> {
            if (checkUserConfigured()) {
                openRegisterTrailActivity();
            } else {
                Toast.makeText(this,
                        "Por favor, configure seus dados primeiro!",
                        Toast.LENGTH_LONG).show();
                openConfigActivity();
            }
        });

        btnViewTrails.setOnClickListener(v -> openViewTrailsActivity());

        btnCredits.setOnClickListener(v -> openCreditsActivity());
    }

    private void checkPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        };

        // Para Android 13+ (API 33), também precisamos da permissão de notificação
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions = new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.POST_NOTIFICATIONS
            };
        }

        boolean allGranted = true;
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                allGranted = false;
                break;
            }
        }

        if (!allGranted) {
            ActivityCompat.requestPermissions(this, permissions, PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_CODE) {
            boolean allGranted = true;

            for (int result : grantResults) {
                if (result != PackageManager.PERMISSION_GRANTED) {
                    allGranted = false;
                    break;
                }
            }

            if (allGranted) {
                Toast.makeText(this, "Permissões concedidas!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this,
                        "Permissões são necessárias para o funcionamento do app!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }

    private boolean checkUserConfigured() {
        return userConfig.isConfigured();
    }

    private void openConfigActivity() {
        Intent intent = new Intent(this, ConfigActivity.class);
        startActivity(intent);
    }

    private void openRegisterTrailActivity() {
        Intent intent = new Intent(this, RegisterTrailActivity.class);
        startActivity(intent);
    }

    private void openViewTrailsActivity() {
        Intent intent = new Intent(this, ViewTrailsActivity.class);
        startActivity(intent);
    }

    private void openCreditsActivity() {
        Intent intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Atualizar estado dos botões se necessário
    }
}