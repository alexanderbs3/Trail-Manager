package com.example.trailmanager.activities;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.trailmanager.R;
import com.example.trailmanager.database.TrailDAO;
import com.example.trailmanager.database.TrailPointDAO;
import com.example.trailmanager.models.Trail;
import com.example.trailmanager.models.TrailPoint;
import com.example.trailmanager.models.UserConfig;
import com.example.trailmanager.services.LocationTrackingService;
import com.example.trailmanager.utils.CalorieCalculator;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class RegisterTrailActivity extends AppCompatActivity implements
        OnMapReadyCallback, LocationTrackingService.LocationListener {

    private GoogleMap map;
    private LocationTrackingService locationService;
    private boolean serviceBound = false;

    private TextView tvSpeed;
    private TextView tvMaxSpeed;
    private TextView tvDistance;
    private TextView tvCalories;
    private Chronometer chronometer;
    private Button btnStartStop;
    private Button btnFinish;

    private Trail currentTrail;
    private List<TrailPoint> trailPoints;
    private Polyline pathPolyline;
    private Marker currentLocationMarker;
    private Circle accuracyCircle;

    private boolean isTracking = false;
    private long startTime = 0;
    private double totalDistance = 0;
    private double maxSpeed = 0;
    private Location lastLocation = null;

    private UserConfig userConfig;
    private TrailDAO trailDAO;
    private TrailPointDAO pointDAO;

    private Handler updateHandler;
    private Runnable updateRunnable;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationTrackingService.LocalBinder binder =
                    (LocationTrackingService.LocalBinder) service;
            locationService = binder.getService();
            locationService.setLocationListener(com.example.trailmanager.activities.RegisterTrailActivity.this);
            serviceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            serviceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_trail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Registrar Trilha");
        }

        userConfig = new UserConfig(this);
        trailDAO = new TrailDAO(this);
        pointDAO = new TrailPointDAO(this);
        trailPoints = new ArrayList<>();

        initViews();
        setupMap();
        setupListeners();
        setupUpdateHandler();
    }

    private void initViews() {
        tvSpeed = findViewById(R.id.tvSpeed);
        tvMaxSpeed = findViewById(R.id.tvMaxSpeed);
        tvDistance = findViewById(R.id.tvDistance);
        tvCalories = findViewById(R.id.tvCalories);
        chronometer = findViewById(R.id.chronometer);
        btnStartStop = findViewById(R.id.btnStartStop);
        btnFinish = findViewById(R.id.btnFinish);

        btnFinish.setEnabled(false);
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    private void setupListeners() {
        btnStartStop.setOnClickListener(v -> {
            if (isTracking) {
                pauseTracking();
            } else {
                startTracking();
            }
        });

        btnFinish.setOnClickListener(v -> showFinishDialog());
    }

    private void setupUpdateHandler() {
        updateHandler = new Handler();
        updateRunnable = new Runnable() {
            @Override
            public void run() {
                updateCalories();
                updateHandler.postDelayed(this, 1000);
            }
        };
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        try {
            map.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        // Configurar tipo de mapa
        int mapType = userConfig.getMapType();
        if (mapType == UserConfig.MAP_TYPE_SATELLITE) {
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        // Desabilitar controles padrão
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);
    }

    private void startTracking() {
        isTracking = true;
        btnStartStop.setText("Pausar");
        btnFinish.setEnabled(true);

        if (currentTrail == null) {
            // Primeira vez - criar nova trilha
            String trailName = "Trilha " +
                    new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
                            .format(new Date());

            currentTrail = new Trail(trailName, new Date());
            startTime = SystemClock.elapsedRealtime();
            chronometer.setBase(startTime);
            chronometer.start();

            // Iniciar serviço de localização
            Intent serviceIntent = new Intent(this, LocationTrackingService.class);
            startService(serviceIntent);
            bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE);
        } else {
            // Retomando após pausa
            long pauseDuration = SystemClock.elapsedRealtime() - chronometer.getBase();
            chronometer.setBase(SystemClock.elapsedRealtime() - pauseDuration);
            chronometer.start();
        }

        updateHandler.post(updateRunnable);
    }

    private void pauseTracking() {
        isTracking = false;
        btnStartStop.setText("Continuar");
        chronometer.stop();
        updateHandler.removeCallbacks(updateRunnable);
    }

    @Override
    public void onLocationUpdate(Location location) {
        if (!isTracking) return;

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        // Atualizar marcador de posição atual
        if (currentLocationMarker == null) {
            MarkerOptions markerOptions = new MarkerOptions()
                    .position(latLng)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
                    .title("Você está aqui");
            currentLocationMarker = map.addMarker(markerOptions);
        } else {
            currentLocationMarker.setPosition(latLng);
        }

        // Atualizar círculo de acurácia
        if (accuracyCircle != null) {
            accuracyCircle.remove();
        }
        CircleOptions circleOptions = new CircleOptions()
                .center(latLng)
                .radius(location.getAccuracy())
                .strokeColor(Color.argb(100, 0, 150, 255))
                .fillColor(Color.argb(50, 0, 150, 255))
                .strokeWidth(2);
        accuracyCircle = map.addCircle(circleOptions);

        // Adicionar ponto à trilha
        TrailPoint point = new TrailPoint(
                location.getLatitude(),
                location.getLongitude(),
                location.getAltitude(),
                location.getAccuracy(),
                location.getSpeed(),
                new Date()
        );
        trailPoints.add(point);

        // Calcular distância
        if (lastLocation != null) {
            float[] results = new float[1];
            Location.distanceBetween(
                    lastLocation.getLatitude(), lastLocation.getLongitude(),
                    location.getLatitude(), location.getLongitude(),
                    results
            );
            totalDistance += results[0];
        }
        lastLocation = location;

        // Atualizar velocidade máxima
        double currentSpeed = location.getSpeed();
        if (currentSpeed > maxSpeed) {
            maxSpeed = currentSpeed;
        }

        // Atualizar UI
        updateUI(location);

        // Desenhar caminho
        drawPath();

        // Mover câmera
        moveCamera(latLng, location.getBearing());
    }

    private void updateUI(Location location) {
        // Velocidade atual
        double speedKmh = location.getSpeed() * 3.6;
        tvSpeed.setText(String.format(Locale.getDefault(), "%.1f km/h", speedKmh));

        // Velocidade máxima
        double maxSpeedKmh = maxSpeed * 3.6;
        tvMaxSpeed.setText(String.format(Locale.getDefault(), "%.1f km/h", maxSpeedKmh));

        // Distância
        if (totalDistance < 1000) {
            tvDistance.setText(String.format(Locale.getDefault(), "%.0f m", totalDistance));
        } else {
            tvDistance.setText(String.format(Locale.getDefault(), "%.2f km", totalDistance / 1000));
        }
    }

    private void updateCalories() {
        if (!isTracking || lastLocation == null) return;

        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
        double elapsedSeconds = elapsedMillis / 1000.0;
        double avgSpeed = (totalDistance / elapsedSeconds) * 3.6; // km/h

        double calories = CalorieCalculator.calculateInstantCalories(
                userConfig.getWeight(),
                (long) elapsedSeconds,
                avgSpeed,
                userConfig.getGender()
        );

        tvCalories.setText(String.format(Locale.getDefault(), "%.0f kcal", calories));
    }

    private void drawPath() {
        if (trailPoints.size() < 2) return;

        List<LatLng> points = new ArrayList<>();
        for (TrailPoint point : trailPoints) {
            points.add(new LatLng(point.getLatitude(), point.getLongitude()));
        }

        if (pathPolyline != null) {
            pathPolyline.remove();
        }

        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(points)
                .color(Color.BLUE)
                .width(10);

        pathPolyline = map.addPolyline(polylineOptions);
    }

    private void moveCamera(LatLng latLng, float bearing) {
        String navMode = userConfig.getNavigationMode();

        CameraPosition.Builder cameraBuilder = new CameraPosition.Builder()
                .target(latLng)
                .zoom(17);

        if (navMode.equals(UserConfig.NAV_MODE_COURSE_UP)) {
            cameraBuilder.bearing(bearing);
        } else {
            cameraBuilder.bearing(0);
        }

        map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraBuilder.build()));
    }

    private void showFinishDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Finalizar Trilha")
                .setMessage("Deseja finalizar e salvar esta trilha?")
                .setPositiveButton("Sim", (dialog, which) -> finishTrail())
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private void finishTrail() {
        if (currentTrail == null) return;

        pauseTracking();

        // Finalizar trilha
        currentTrail.setEndTime(new Date());
        currentTrail.setTotalDistance(totalDistance);
        currentTrail.setMaxSpeed(maxSpeed);

        long elapsedMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
        double elapsedSeconds = elapsedMillis / 1000.0;
        double avgSpeed = totalDistance / elapsedSeconds;
        currentTrail.setAvgSpeed(avgSpeed);

        double calories = CalorieCalculator.calculateInstantCalories(
                userConfig.getWeight(),
                (long) elapsedSeconds,
                avgSpeed * 3.6,
                userConfig.getGender()
        );
        currentTrail.setCaloriesBurned(calories);

        // Salvar no banco de dados
        long trailId = trailDAO.insertTrail(currentTrail);

        for (TrailPoint point : trailPoints) {
            point.setTrailId(trailId);
        }
        pointDAO.insertPoints(trailPoints);

        Toast.makeText(this, "Trilha salva com sucesso!", Toast.LENGTH_SHORT).show();

        // Parar serviço
        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
        }
        stopService(new Intent(this, LocationTrackingService.class));

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        updateHandler.removeCallbacks(updateRunnable);

        if (serviceBound) {
            unbindService(serviceConnection);
            serviceBound = false;
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        if (isTracking) {
            new AlertDialog.Builder(this)
                    .setTitle("Sair")
                    .setMessage("Há uma trilha em andamento. Deseja pausar e sair?")
                    .setPositiveButton("Sim", (dialog, which) -> {
                        pauseTracking();
                        finish();
                    })
                    .setNegativeButton("Não", null)
                    .show();
        } else {
            finish();
        }
        return true;
    }
}
