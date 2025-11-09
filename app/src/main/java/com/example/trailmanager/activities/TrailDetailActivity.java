package com.example.trailmanager.activities;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.trailmanager.R;
import com.example.trailmanager.database.TrailDAO;
import com.example.trailmanager.models.Trail;
import com.example.trailmanager.models.TrailPoint;
import com.example.trailmanager.models.UserConfig;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class TrailDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap map;
    private Trail trail;
    private TrailDAO trailDAO;
    private UserConfig userConfig;

    private TextView tvName;
    private TextView tvDate;
    private TextView tvDuration;
    private TextView tvDistance;
    private TextView tvAvgSpeed;
    private TextView tvMaxSpeed;
    private TextView tvCalories;
    private TextView tvPoints;

    private SimpleDateFormat dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trail_detail);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Detalhes da Trilha");
        }

        trailDAO = new TrailDAO(this);
        userConfig = new UserConfig(this);
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());

        initViews();
        loadTrail();
        setupMap();
    }

    private void initViews() {
        tvName = findViewById(R.id.tvDetailName);
        tvDate = findViewById(R.id.tvDetailDate);
        tvDuration = findViewById(R.id.tvDetailDuration);
        tvDistance = findViewById(R.id.tvDetailDistance);
        tvAvgSpeed = findViewById(R.id.tvDetailAvgSpeed);
        tvMaxSpeed = findViewById(R.id.tvDetailMaxSpeed);
        tvCalories = findViewById(R.id.tvDetailCalories);
        tvPoints = findViewById(R.id.tvDetailPoints);
    }

    private void loadTrail() {
        long trailId = getIntent().getLongExtra("trail_id", -1);

        if (trailId == -1) {
            Toast.makeText(this, "Erro ao carregar trilha", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        trail = trailDAO.getTrailById(trailId);

        if (trail == null) {
            Toast.makeText(this, "Trilha não encontrada", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        displayTrailInfo();
    }

    private void displayTrailInfo() {
        tvName.setText(trail.getName());
        tvDate.setText(dateFormat.format(trail.getStartTime()));
        tvDuration.setText(trail.getFormattedDuration());
        tvDistance.setText(trail.getFormattedDistance());
        tvAvgSpeed.setText(trail.getFormattedAvgSpeed());
        tvMaxSpeed.setText(trail.getFormattedMaxSpeed());
        tvCalories.setText(trail.getFormattedCalories());
        tvPoints.setText(String.valueOf(trail.getPoints().size()) + " pontos");
    }

    private void setupMap() {
        SupportMapFragment mapFragment = (SupportMapFragment)
                getSupportFragmentManager().findFragmentById(R.id.detailMap);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        // Configurar tipo de mapa
        int mapType = userConfig.getMapType();
        if (mapType == UserConfig.MAP_TYPE_SATELLITE) {
            map.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        } else {
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        }

        // Desabilitar botão de localização
        map.getUiSettings().setMyLocationButtonEnabled(false);
        map.getUiSettings().setZoomControlsEnabled(true);

        drawTrailOnMap();
    }

    private void drawTrailOnMap() {
        if (trail == null || trail.getPoints().isEmpty()) {
            Toast.makeText(this, "Trilha sem pontos para exibir", Toast.LENGTH_SHORT).show();
            return;
        }

        List<LatLng> points = new ArrayList<>();
        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();

        for (TrailPoint point : trail.getPoints()) {
            LatLng latLng = new LatLng(point.getLatitude(), point.getLongitude());
            points.add(latLng);
            boundsBuilder.include(latLng);
        }

        // Desenhar linha da trilha
        PolylineOptions polylineOptions = new PolylineOptions()
                .addAll(points)
                .color(Color.BLUE)
                .width(10);
        map.addPolyline(polylineOptions);

        // Adicionar marcadores de início e fim
        if (points.size() > 0) {
            LatLng start = points.get(0);
            map.addMarker(new MarkerOptions()
                    .position(start)
                    .title("Início"));

            LatLng end = points.get(points.size() - 1);
            map.addMarker(new MarkerOptions()
                    .position(end)
                    .title("Fim"));
        }

        // Ajustar câmera para mostrar toda a trilha
        LatLngBounds bounds = boundsBuilder.build();
        int padding = 100; // pixels
        map.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}